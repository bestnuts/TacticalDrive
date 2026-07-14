package me.bestnuts.core.model.vehicle;

import lombok.Getter;
import me.bestnuts.api.model.vehicle.Vehicle;
import me.bestnuts.api.model.vehicle.VehicleRegistryType;
import me.bestnuts.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.api.model.vehicle.component.bone.VehicleGroup;
import me.bestnuts.api.model.vehicle.configuration.VehicleConfiguration;
import me.bestnuts.core.model.vehicle.component.function.CarSuspensionFunction;
import me.bestnuts.core.model.vehicle.component.function.CarWheelFunction;
import me.bestnuts.core.model.vehicle.configuration.CarPhysicsConfiguration;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static me.bestnuts.api.bukkit.util.Constant.FIXED_DELTA_TIME;

@Getter
public final class VehicleCar extends Vehicle {

    private double steer;
    private double speed;

    private final List<CarWheelFunction.WheelOutput> wheelOutputs = new ArrayList<>();
    private final List<CarSuspensionFunction.SuspensionOutput> suspensionOutputs = new ArrayList<>();

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

        Location location = updateValue();
        wheelOutputs.clear();
        suspensionOutputs.clear();
        Vector direction = location.getDirection();
        Vector movement = direction.multiply(speed);
        entity().getEntity().teleport(updatePosition(location, movement));
    }

    private Location updateValue() {
        //바퀴
        double combinedForwardForce = 0.0;
        double combinedLateralForce = 0.0;
        double combinedWheelSteer = 0.0;
        int steerableWheelCount = 0;

        if (this.speed == 0.0) {
            this.steer = 0.0;
        }

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
            this.steer = this.steer + (targetSteer - this.steer) * 5.0 * FIXED_DELTA_TIME;
        }

        double totalEngineForce = combinedForwardForce * (configuration().getPhysics().getHorsePower() * 745.7) * 0.02;

        double mass = configuration().getPhysics().getMass();
        double maxSpeed = configuration().getPhysics().getMaxSpeed();
        double maxReverseSpeed = maxSpeed * 0.3;

        double speedMagnitude = Math.abs(this.speed);
        double airDrag = 0.5 * 1.2 * configuration().getPhysics().getDragCoefficient() * (speedMagnitude * speedMagnitude);
        double rollingDrag = mass * 9.81 * 0.015;
        double totalResistanceMagnitude = airDrag + rollingDrag;

        if (speedMagnitude < 0.01) {
            totalResistanceMagnitude = 0.0;
        }

        double netForce;
        if (this.speed >= 0.0) {
            netForce = totalEngineForce - totalResistanceMagnitude;
        } else {
            netForce = totalEngineForce + totalResistanceMagnitude;
        }

        double acceleration = netForce / mass;

        this.speed += acceleration * FIXED_DELTA_TIME;

        if (Math.abs(combinedLateralForce) > (mass * 0.4) && this.speed > 5.0) {
            this.steer += (combinedLateralForce / mass) * 1.5 * FIXED_DELTA_TIME;
        }

        if (this.speed > maxSpeed) this.speed = maxSpeed;
        if (Math.abs(this.speed) < 0.01) this.speed = 0.0;
        if (this.speed < -maxReverseSpeed) this.speed = -maxReverseSpeed;

        Location location = entity().getLocation();

        if (this.speed != 0.0) {
            location.setRotation((float) (location.getYaw() + steer), 0);
        }

        //서스펜션
        double totalWheelWorldY = 0.0;
        int activeSuspensionCount = 0;

        for (CarSuspensionFunction.SuspensionOutput output : suspensionOutputs) {
            totalWheelWorldY += output.wheelWorldY();
            activeSuspensionCount++;
        }

        if (activeSuspensionCount > 0) {
            double averageGroundY = totalWheelWorldY / activeSuspensionCount;
            double targetVehicleY = averageGroundY + ((CarPhysicsConfiguration) configuration().getPhysics()).getRestLength();

            double currentY = location.getY();
            double smoothingFactor = 15.0;

            double newY = currentY + (targetVehicleY - currentY) * smoothingFactor * FIXED_DELTA_TIME;
            location.setY(newY);
        }

        return location;
    }

    private Location updatePosition(Location location, Vector velocity) {
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
