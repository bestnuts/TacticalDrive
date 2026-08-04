package me.bestnuts.drive.core.model.vehicle.component.function;

import me.bestnuts.drive.api.bukkit.register.SurfaceFrictionRegistry;
import me.bestnuts.drive.api.bukkit.util.FunctionParamHelper;
import me.bestnuts.drive.api.bukkit.util.RotationHelper;
import me.bestnuts.drive.api.model.vehicle.Vehicle;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.drive.api.model.vehicle.component.function.VehicleFunction;
import me.bestnuts.drive.api.model.vehicle.data.VehicleMotion;
import me.bestnuts.drive.api.model.vehicle.data.VehicleOutput;
import me.bestnuts.drive.core.model.vehicle.configuration.CarPhysicsConfiguration;
import me.bestnuts.drive.core.model.vehicle.data.SuspensionOutput;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Map;

import static me.bestnuts.drive.api.bukkit.util.Constant.FIXED_DELTA_TIME;

public final class CarSuspensionFunction extends VehicleFunction {

    private static final double PROBE_BUFFER = 1.5;
    private static final double GROUND_INSET = 0.05;

    private final SurfaceFrictionRegistry frictionRegistry;

    private final double height;
    private final double stiffness;
    private final double damping;
    private final double restLength;

    private final Vector offset;
    private final String link;

    private VehicleEntity pivot;
    private CarPhysicsConfiguration physics;
    private double previousCompression;
    private double climbLimitY;
    private boolean wasGrounded;

    public CarSuspensionFunction(@NotNull SurfaceFrictionRegistry frictionRegistry,
                                 @NotNull VehicleEntity parent, int delay, @NotNull Map<String, String> param) {
        super(parent, delay, param);
        this.frictionRegistry = frictionRegistry;
        this.height = Double.parseDouble(param.getOrDefault("height", "0.5"));
        this.stiffness = Double.parseDouble(param.getOrDefault("stiffness", "12000.0"));
        this.damping = Double.parseDouble(param.getOrDefault("damping", "1800.0"));
        this.restLength = Double.parseDouble(param.getOrDefault("length", "1.2"));

        this.offset = FunctionParamHelper.getVector(param.getOrDefault("offset", "0;0;0"), new Vector());
        this.link = param.getOrDefault("link", "root");
    }

    @Override
    public @Nullable VehicleOutput execute(@NotNull Vehicle vehicle) {
        if (pivot == null) {
            pivot = FunctionParamHelper.getLink(link, vehicle);
            physics = (CarPhysicsConfiguration) vehicle.configuration().getPhysics();
        }

        Location anchor = resolveAnchor(vehicle.motion());
        double anchorY = anchor.getY();
        this.climbLimitY = anchorY + physics.getMaxStepHeight();

        if (pivot.getLocation().getBlock().getType().isSolid()) {
            return buried(anchor);
        }

        GroundProbe probe = probeGround(anchor);

        if (probe != null && probe.hitY() > this.climbLimitY) {
            leaveGround();
            return droop(anchor, true);
        }

        if (probe == null || probe.hitY() < anchorY - restLength) {
            leaveGround();
            return droop(anchor, false);
        }

        double compression = Math.min(restLength - (anchorY - probe.hitY()), restLength);
        double wheelWorldY = Math.min(probe.hitY() + GROUND_INSET, anchorY);

        if (!wasGrounded) {
            previousCompression = compression;
            wasGrounded = true;
        }

        double force = springForce(compression);
        updateTranslation(anchor, wheelWorldY);

        return new SuspensionOutput(getParent().getUniqueId(), force, wheelWorldY, this.climbLimitY,
                true, false, frictionRegistry.find(probe.material()), this.offset);
    }

    private @NotNull Location resolveAnchor(@NotNull VehicleMotion motion) {
        Location pivotLocation = pivot.getLocation().clone();
        Quaternionf orientation = RotationHelper.orientation(pivotLocation.getYaw(), motion.getPitch(), motion.getRoll());
        return pivotLocation.add(RotationHelper.rotate(orientation, this.offset));
    }

    private @Nullable GroundProbe probeGround(@NotNull Location anchor) {
        Location rayStart = anchor.clone().add(0, PROBE_BUFFER, 0);
        if (rayStart.getBlock().getType().isSolid()) {
            return new GroundProbe(rayStart.getY(), rayStart.getBlock().getType());
        }

        RayTraceResult hit = anchor.getWorld().rayTraceBlocks(
                rayStart, new Vector(0, -1, 0), restLength + PROBE_BUFFER, FluidCollisionMode.NEVER, true);

        if (hit == null || hit.getHitBlock() == null) return null;
        return new GroundProbe(hit.getHitPosition().getY(), hit.getHitBlock().getType());
    }

    private void leaveGround() {
        previousCompression = 0.0;
        wasGrounded = false;
    }

    private double springForce(double compression) {
        double springForce = compression * this.stiffness;

        double compressionVelocity = (compression - this.previousCompression) / FIXED_DELTA_TIME;
        this.previousCompression = compression;
        double dampingForce = compressionVelocity * this.damping;

        double total = springForce + dampingForce;
        if (total < 0) total = 0;

        double limit = physics.getMass() * physics.getGravity() * physics.getSuspensionForceLimit();
        return Math.min(total, limit);
    }

    private @NotNull SuspensionOutput buried(@NotNull Location anchor) {
        previousCompression = restLength;
        wasGrounded = true;
        double wheelWorldY = anchor.getY();
        updateTranslation(anchor, wheelWorldY);
        return new SuspensionOutput(getParent().getUniqueId(), springForce(restLength), wheelWorldY, this.climbLimitY,
                true, false, frictionRegistry.getDefaultFriction(), this.offset);
    }

    private @NotNull SuspensionOutput droop(@NotNull Location anchor, boolean wall) {
        double wheelWorldY = anchor.getY() - restLength;
        updateTranslation(anchor, wheelWorldY);
        return new SuspensionOutput(getParent().getUniqueId(), 0.0, wheelWorldY, this.climbLimitY,
                false, wall, frictionRegistry.getDefaultFriction(), this.offset);
    }

    private void updateTranslation(@NotNull Location anchor, double wheelWorldY) {
        if (!(getParent().getEntity() instanceof org.bukkit.entity.Display display)) {
            return;
        }

        Location carLocation = pivot.getLocation();

        Vector localDiff = RotationHelper.inverseYaw(carLocation.getYaw(),
                anchor.toVector().subtract(carLocation.toVector()));
        float localY = (float) ((wheelWorldY - carLocation.getY()) + this.height);

        Transformation transformation = display.getTransformation();
        Vector3f translation = transformation.getTranslation();

        translation.set((float) localDiff.getX(), localY, (float) localDiff.getZ());

        display.setTransformation(transformation);
    }

    private record GroundProbe(double hitY, Material material) {
    }
}
