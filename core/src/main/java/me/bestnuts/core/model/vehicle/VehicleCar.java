package me.bestnuts.core.model.vehicle;

import lombok.Getter;
import me.bestnuts.api.model.vehicle.Vehicle;
import me.bestnuts.api.model.vehicle.VehicleRegistryType;
import me.bestnuts.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.api.model.vehicle.component.bone.VehicleGroup;
import me.bestnuts.api.model.vehicle.configuration.VehicleConfiguration;
import me.bestnuts.core.model.vehicle.component.function.CarWheelFunction;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class VehicleCar extends Vehicle {

    private static final double deltaTime = 0.05;

    private double steer;
    private double speed;

    private final List<CarWheelFunction.WheelOutput> wheelOutputs = new ArrayList<>();

    public VehicleCar(@NotNull VehicleEntity entity, @NotNull VehicleGroup group, @NotNull VehicleConfiguration configuration) {
        super(entity, group, configuration);
    }

    @Override
    public @NotNull String type() {
        return VehicleRegistryType.CAR.getName();
    }

    @Override
    public void tick() {
        super.tick();

        double combinedForwardForce = 0.0;
        double combinedLateralForce = 0.0;
        double combinedWheelSteer = 0.0;
        int steerableWheelCount = 0;

        for (CarWheelFunction.WheelOutput output : wheelOutputs) {

            combinedForwardForce += output.forwardForce();
            combinedLateralForce += output.lateralForce();

            if (output.wheelSteer() != 0.0) {
                combinedWheelSteer += output.wheelSteer();
                steerableWheelCount++;
            }
        }

        if (steerableWheelCount > 0) {
            double targetSteer = combinedWheelSteer / steerableWheelCount;
            this.steer = this.steer + (targetSteer - this.steer) * 5.0 * deltaTime;
        }

        double totalEngineForce = combinedForwardForce * (configuration().getPhysics().getHorsePower() * 745.7) * 0.02;

        double mass = configuration().getPhysics().getMass();
        double maxSpeed = configuration().getPhysics().getMaxSpeed();

        double airDrag = 0.5 * 1.2 * configuration().getPhysics().getDragCoefficient() * (this.speed * this.speed);
        double rollingDrag = mass * 9.81 * 0.015;
        double totalResistance = airDrag + rollingDrag;

        double netForce = totalEngineForce - totalResistance;
        double acceleration = netForce / mass;

        this.speed += acceleration * deltaTime;

        if (Math.abs(combinedLateralForce) > (mass * 0.4) && this.speed > 5.0) {
            this.steer += (combinedLateralForce / mass) * 15.0 * deltaTime;
        }

        if (this.speed > maxSpeed) this.speed = maxSpeed;
        if (this.speed < 0.0) this.speed = 0.0;

        wheelOutputs.clear();

        Location location = entity().getLocation();

        if (this.speed != 0.0) {
            location.setRotation((float) (location.getYaw() + steer * deltaTime), 0);
        }


        Vector direction = location.getDirection();
        Vector movement = direction.multiply(speed);
        movement.setY(0);
        entity().getEntity().teleport(move(location, movement));
    }

    private Location move(Location location, Vector velocity) {
        World world = location.getWorld();
        if (world == null) return location;

        double distance = velocity.length();
        if (distance < 0.001) return location;

        Vector direction = velocity.clone().normalize();

        RayTraceResult rayResult = world.rayTraceBlocks(
                location,
                direction,
                distance,
                org.bukkit.FluidCollisionMode.NEVER,
                true
        );

        Location targetLoc = location.clone().add(velocity);

        if (rayResult == null || rayResult.getHitBlock() == null) {
            return targetLoc;
        }

        Vector hitPoint = rayResult.getHitPosition();
        BlockFace hitFace = rayResult.getHitBlockFace();

        if (hitFace == null) return location;
        Vector normal = hitFace.getDirection();

        Vector safeHitPoint = hitPoint.clone().add(normal.clone().multiply(0.05));

        Vector remainingMove = targetLoc.toVector().subtract(hitPoint);
        double dotProduct = remainingMove.dot(normal);
        Vector slidingVector = remainingMove.subtract(normal.multiply(dotProduct));

        Vector finalVectorPosition = safeHitPoint.add(slidingVector);

        Location correctedLoc = new Location(world, finalVectorPosition.getX(), finalVectorPosition.getY(), finalVectorPosition.getZ());
        correctedLoc.setYaw(location.getYaw());
        correctedLoc.setPitch(location.getPitch());

        return correctedLoc;
    }
}
