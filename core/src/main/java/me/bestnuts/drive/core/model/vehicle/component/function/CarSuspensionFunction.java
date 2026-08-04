package me.bestnuts.drive.core.model.vehicle.component.function;

import me.bestnuts.drive.api.bukkit.util.FunctionParamHelper;
import me.bestnuts.drive.api.bukkit.util.RotationHelper;
import me.bestnuts.drive.api.model.vehicle.Vehicle;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.drive.api.model.vehicle.component.function.VehicleFunction;
import me.bestnuts.drive.api.model.vehicle.configuration.PhysicsConfiguration;
import me.bestnuts.drive.api.model.vehicle.data.VehicleOutput;
import me.bestnuts.drive.core.model.vehicle.data.SuspensionOutput;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Display;
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

    private final double height;
    private final double stiffness;
    private final double damping;
    private final double restLength;

    private final Vector offset;
    private final String link;

    private VehicleEntity pivot;
    private PhysicsConfiguration physicsConfiguration;
    private double previousCompression;

    public CarSuspensionFunction(@NotNull VehicleEntity parent, int delay, @NotNull Map<String, String> param) {
        super(parent, delay, param);
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
            physicsConfiguration = vehicle.configuration().getPhysics();
        }

        Location pivotLocation = pivot.getLocation().clone();

        Quaternionf orientation = RotationHelper.orientation(
                pivotLocation.getYaw(), vehicle.motion().getPitch(), vehicle.motion().getRoll());
        Vector rotatedLocal = RotationHelper.rotate(orientation, this.offset);
        Location suspensionTopLoc = pivotLocation.clone().add(rotatedLocal);
        World bukkitWorld = suspensionTopLoc.getWorld();

        double checkHeightBuffer = 1.5;
        Location rayStart = suspensionTopLoc.clone().add(0, checkHeightBuffer, 0);

        Block startBlock = rayStart.getBlock();
        if (startBlock.getType().isSolid()) {
            return new SuspensionOutput(0.0, suspensionTopLoc.getY() - this.restLength, true, this.offset);
        }

        Vector downDirection = new Vector(0, -1, 0);
        double totalSearchDistance = this.restLength + checkHeightBuffer;

        RayTraceResult hit = bukkitWorld.rayTraceBlocks(rayStart, downDirection, totalSearchDistance, org.bukkit.FluidCollisionMode.NEVER, true);

        double currentLength = this.restLength;
        double groundY = suspensionTopLoc.getY() - this.restLength;

        if (hit != null && hit.getHitBlock() != null) {
            double actualHitY = hit.getHitPosition().getY();
            double suspensionBaseY = suspensionTopLoc.getY();

            double maxAllowedUpwardClimb = 0.6;
            double maxHitY = suspensionBaseY + maxAllowedUpwardClimb;
            double minHitY = suspensionBaseY - (this.restLength * 1.15);

            if (actualHitY > maxHitY) {
                actualHitY = maxHitY;
            }

            if (actualHitY >= minHitY) {
                currentLength = suspensionBaseY - actualHitY;
                groundY = actualHitY + 0.05;
            } else {
                currentLength = this.restLength;
                groundY = suspensionBaseY - this.restLength;
            }
        }

        double compression = this.restLength - currentLength;
        if (compression < 0) compression = 0;

        double springForce = compression * this.stiffness;

        double compressionVelocity = (compression - this.previousCompression) / FIXED_DELTA_TIME;
        this.previousCompression = compression;
        double dampingForce = compressionVelocity * this.damping;

        double totalUpwardForce = springForce + dampingForce;
        if (totalUpwardForce < 0) totalUpwardForce = 0;

        totalUpwardForce = Math.min(totalUpwardForce, (physicsConfiguration.getMass() * physicsConfiguration.getGravity()) * 2.2);

        double wheelWorldY = (compression > 0) ? (groundY) : (suspensionTopLoc.getY() - this.restLength);

        updateTranslation(suspensionTopLoc, wheelWorldY);

        return new SuspensionOutput(totalUpwardForce, wheelWorldY, false, this.offset);
    }

    private void updateTranslation(Location suspensionTopLoc, double wheelWorldY) {
        if (!(getParent().getEntity() instanceof Display display)) {
            return;
        }

        Location carLocation = pivot.getLocation();

        Vector localDiff = RotationHelper.inverseYaw(carLocation.getYaw(),
                suspensionTopLoc.toVector().subtract(carLocation.toVector()));
        float localY = (float) ((wheelWorldY - carLocation.getY()) + this.height);

        Transformation transformation = display.getTransformation();
        Vector3f translation = transformation.getTranslation();

        translation.set((float) localDiff.getX(), localY, (float) localDiff.getZ());

        display.setTransformation(transformation);
    }
}
