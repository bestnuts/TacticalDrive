package me.bestnuts.core.model.vehicle.component.function;

import me.bestnuts.api.bukkit.util.FunctionParamHelper;
import me.bestnuts.api.model.vehicle.Vehicle;
import me.bestnuts.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.api.model.vehicle.component.function.VehicleFunction;
import me.bestnuts.api.model.vehicle.configuration.PhysicsConfiguration;
import me.bestnuts.core.model.vehicle.VehicleCar;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static me.bestnuts.api.bukkit.util.Constant.FIXED_DELTA_TIME;

public final class CarSuspensionFunction extends VehicleFunction {


    private final double stiffness;
    private final double damping;
    private final double restLength;

    private final Vector world;
    private final Vector local;
    private final String link;

    private VehicleEntity pivot;
    private VehicleCar car;
    private PhysicsConfiguration physicsConfiguration;
    private double previousCompression;
    private boolean isLoad;

    public CarSuspensionFunction(@NotNull VehicleEntity parent, int delay, @NotNull Map<String, String> param) {
        super(parent, delay, param);
        this.stiffness = Double.parseDouble(param.getOrDefault("stiffness", "12000.0"));
        this.damping = Double.parseDouble(param.getOrDefault("damping", "1800.0"));
        this.restLength = Double.parseDouble(param.getOrDefault("length", "1.2"));

        this.world = FunctionParamHelper.getVector(param.getOrDefault("world", "0;0;0"), new Vector());
        this.local = FunctionParamHelper.getVector(param.getOrDefault("local", "0;0;0"), new Vector());
        this.link = param.getOrDefault("link", "root");
    }

    @Override
    public void execute(@NotNull Vehicle vehicle) {
        if (!isLoad) {
            if (pivot == null) {
                pivot = FunctionParamHelper.getLink(link, vehicle);
            }
            if (vehicle instanceof VehicleCar vehicleCar) {
                car = vehicleCar;
                physicsConfiguration = car.configuration().getPhysics();
                isLoad = true;
            }
            return;
        }

        Location pivotLocation = pivot.getLocation().clone();
        pivotLocation.add(this.world);

        Vector rotatedLocal = FunctionParamHelper.rotateVectorByDirection(pivotLocation, this.local);
        Location suspensionTopLoc = pivotLocation.clone().add(rotatedLocal);
        World bukkitWorld = suspensionTopLoc.getWorld();

        double checkHeightBuffer = 1.5;
        Location rayStart = suspensionTopLoc.clone().add(0, checkHeightBuffer, 0);

        Block startBlock = rayStart.getBlock();
        if (startBlock.getType().isSolid()) {
            car.getSuspensionOutputs().add(new SuspensionOutput(0.0, suspensionTopLoc.getY() - this.restLength, true, this.local));
            return;
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

        double wheelWorldY = (compression > 0) ? groundY : (suspensionTopLoc.getY() - this.restLength);

        car.getSuspensionOutputs().add(new SuspensionOutput(totalUpwardForce, wheelWorldY, false, this.local));

        Location finalWheelLocation = suspensionTopLoc.clone();
        finalWheelLocation.setY(wheelWorldY);

        finalWheelLocation.setRotation(getParent().getLocation().getYaw(), getParent().getLocation().getPitch());
        getParent().getEntity().teleport(finalWheelLocation);
    }


    public record SuspensionOutput(double upwardForce, double wheelWorldY, boolean lock, Vector offset) {
    }
}
