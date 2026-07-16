package me.bestnuts.core.model.vehicle;

import lombok.Getter;
import me.bestnuts.api.bukkit.util.FunctionParamHelper;
import me.bestnuts.api.model.vehicle.Vehicle;
import me.bestnuts.api.model.vehicle.VehicleRegistryType;
import me.bestnuts.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.api.model.vehicle.component.bone.VehicleGroup;
import me.bestnuts.api.model.vehicle.configuration.VehicleConfiguration;
import me.bestnuts.core.model.vehicle.component.function.CarBodyFunction;
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
    private double pitch;
    private double roll;

    private final List<CarWheelFunction.WheelOutput> wheelOutputs = new ArrayList<>();
    private final List<CarSuspensionFunction.SuspensionOutput> suspensionOutputs = new ArrayList<>();
    private final List<CarBodyFunction.BodyOutput> bodyOutputs = new ArrayList<>();

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
        bodyOutputs.clear();
        Vector direction = location.getDirection();
        Vector movement = direction.multiply(speed);
        entity().getEntity().teleport(updatePosition(location, movement));
    }

    private Location updateValue() {
        Location location = entity().getLocation();

        boolean isSuspensionLocked = false;
        double totalWheelWorldY = 0.0;
        int activeSuspensionCount = 0;
        double accumulatedTorqueFactor = 0.0;
        Vector pushBackLocalVector = new Vector(0, 0, 0);

        double frontLeftY = 0.0, frontRightY = 0.0;
        double rearLeftY = 0.0, rearRightY = 0.0;
        int flCount = 0, frCount = 0, rlCount = 0, rrCount = 0;

        for (CarSuspensionFunction.SuspensionOutput output : suspensionOutputs) {
            totalWheelWorldY += output.wheelWorldY();
            activeSuspensionCount++;

            Vector offset = output.offset();
            if (offset.getZ() > 0 && offset.getX() < 0) { frontLeftY += output.wheelWorldY(); flCount++; }
            else if (offset.getZ() > 0 && offset.getX() > 0) { frontRightY += output.wheelWorldY(); frCount++; }
            else if (offset.getZ() < 0 && offset.getX() < 0) { rearLeftY += output.wheelWorldY(); rlCount++; }
            else if (offset.getZ() < 0 && offset.getX() > 0) { rearRightY += output.wheelWorldY(); rrCount++; }

            if (output.lock()) {
                isSuspensionLocked = true;
                accumulatedTorqueFactor += (offset.getX() * offset.getZ());
                pushBackLocalVector.add(new Vector(-offset.getX(), 0, -offset.getZ()));
            }
        }

        boolean isBodyLocked = false;
        for (CarBodyFunction.BodyOutput output : bodyOutputs) {
            if (output.lock()) {
                isBodyLocked = true;
                Vector offset = output.offset();
                accumulatedTorqueFactor += (offset.getX() * offset.getZ());
                pushBackLocalVector.add(new Vector(-offset.getX(), 0, -offset.getZ()));
            }
        }
        bodyOutputs.clear();

        boolean isAnyCollisionLocked = isSuspensionLocked || isBodyLocked;

        if (flCount > 0 && frCount > 0 && rlCount > 0 && rrCount > 0) {
            double avgFrontY = (frontLeftY / flCount + frontRightY / frCount) * 0.5;
            double avgRearY = (rearLeftY / rlCount + rearRightY / rrCount) * 0.5;
            double avgLeftY = (frontLeftY / flCount + rearLeftY / rlCount) * 0.5;
            double avgRightY = (frontRightY / frCount + rearRightY / rrCount) * 0.5;

            double totalLengthOffset = 0.0;
            double totalWidthOffset = 0.0;
            int lengthSampleCount = 0;
            int widthSampleCount = 0;

            for (CarSuspensionFunction.SuspensionOutput output : suspensionOutputs) {
                Vector offset = output.offset();
                for (CarSuspensionFunction.SuspensionOutput innerOutput : suspensionOutputs) {
                    Vector innerOffset = innerOutput.offset();

                    double deltaZ = offset.getZ() - innerOffset.getZ();
                    if (deltaZ > 0.1) {
                        totalLengthOffset += deltaZ;
                        lengthSampleCount++;
                    }

                    double deltaX = offset.getX() - innerOffset.getX();
                    if (deltaX > 0.1) {
                        totalWidthOffset += deltaX;
                        widthSampleCount++;
                    }
                }
            }

            double carLength = lengthSampleCount > 0 ? (totalLengthOffset / lengthSampleCount) : 2.5;
            double carWidth = widthSampleCount > 0 ? (totalWidthOffset / widthSampleCount) : 1.6;

            double targetPitch = Math.toDegrees(Math.atan2(avgFrontY - avgRearY, carLength));
            double targetRoll = Math.toDegrees(Math.atan2(avgLeftY - avgRightY, carWidth));

            this.pitch = this.pitch + (targetPitch - this.pitch) * 10.0 * FIXED_DELTA_TIME;
            this.roll = this.roll + (targetRoll - this.roll) * 10.0 * FIXED_DELTA_TIME;
        }


        double deltaY = 0.0;
        if (activeSuspensionCount > 0) {
            double averageGroundY = totalWheelWorldY / activeSuspensionCount;
            double targetVehicleY = averageGroundY + ((CarPhysicsConfiguration) configuration().getPhysics()).getRestLength();

            double currentY = location.getY();
            double newY;

            if (targetVehicleY > currentY) {
                newY = targetVehicleY;
            } else {
                double smoothingFactor = isAnyCollisionLocked ? 40.0 : 15.0;
                newY = currentY + (targetVehicleY - currentY) * smoothingFactor * FIXED_DELTA_TIME;
            }

            deltaY = newY - currentY;
            if (Math.abs(deltaY) > 0.001) {
                Vector yDirection = new Vector(0, deltaY > 0 ? 1 : -1, 0);
                double yDistance = Math.abs(deltaY);

                RayTraceResult yHit = location.getWorld().rayTraceBlocks(
                        location,
                        yDirection,
                        yDistance,
                        org.bukkit.FluidCollisionMode.NEVER,
                        true
                );

                if (yHit != null && yHit.getHitBlock() != null) {
                    double hitY = yHit.getHitPosition().getY();
                    if (deltaY > 0) {
                        deltaY = (hitY - 0.1) - currentY;
                    } else {
                        deltaY = (hitY + 0.05) - currentY;
                    }
                }
            }
        }

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

        if (isAnyCollisionLocked) {
            if ((this.speed > 0.0 && totalEngineForce > 0.0) || (this.speed < 0.0 && totalEngineForce < 0.0)) {
                double torqueImpactFactor = 25.0;
                double speedLossFactor = 0.4;

                if (Math.abs(accumulatedTorqueFactor) < 0.01) {
                    this.speed = 0.0;
                } else {
                    if (this.speed > 0.0) {
                        this.steer += accumulatedTorqueFactor * torqueImpactFactor * FIXED_DELTA_TIME;
                    } else {
                        this.steer -= accumulatedTorqueFactor * torqueImpactFactor * FIXED_DELTA_TIME;
                    }
                    this.speed = this.speed * (1.0 - speedLossFactor);
                }
            }
        }

        if (Math.abs(combinedLateralForce) > (mass * 0.4) && this.speed > 5.0) {
            this.steer += (combinedLateralForce / mass) * 1.5 * FIXED_DELTA_TIME;
        }

        if (this.speed > maxSpeed) this.speed = maxSpeed;
        if (Math.abs(this.speed) < 0.01) this.speed = 0.0;
        if (this.speed < -maxReverseSpeed) this.speed = -maxReverseSpeed;

        if (this.speed != 0.0) {
            location.setRotation((float) (location.getYaw() + this.steer), 0);
            this.steer = this.steer * FIXED_DELTA_TIME;
        }

        Vector forwardVector = location.getDirection().setY(0).normalize();
        Vector horizontalVelocity = forwardVector.multiply(this.speed * FIXED_DELTA_TIME);

        if (isAnyCollisionLocked && pushBackLocalVector.length() > 0.001) {
            Vector worldPushDirection = FunctionParamHelper.rotateVectorByDirection(location, pushBackLocalVector.normalize());
            double pushDistance = 0.15;
            horizontalVelocity.add(worldPushDirection.multiply(pushDistance));
        }

        Vector combinedVelocity = horizontalVelocity.setY(deltaY);

        return updatePosition(location, combinedVelocity);
    }


    private Location updatePosition(Location location, Vector velocity) {
        World world = location.getWorld();
        if (world == null) return location;

        double distance = velocity.length();
        if (distance < 0.001) {
            Location sameLoc = location.clone().add(velocity);
            sameLoc.setYaw(location.getYaw());
            sameLoc.setPitch(location.getPitch());
            return sameLoc;
        }

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
            targetLoc.setYaw(location.getYaw());
            targetLoc.setPitch(location.getPitch());
            return targetLoc;
        }

        Vector hitPoint = rayResult.getHitPosition();
        BlockFace hitFace = rayResult.getHitBlockFace();

        if (hitFace == null) {
            targetLoc.setYaw(location.getYaw());
            targetLoc.setPitch(location.getPitch());
            return targetLoc;
        }

        Vector normal = hitFace.getDirection();

        Vector safeHitPoint = hitPoint.clone().add(normal.clone().multiply(0.05));

        Vector remainingMove = targetLoc.toVector().subtract(hitPoint);
        double dotProduct = remainingMove.dot(normal);
        Vector slidingVector = remainingMove.subtract(normal.multiply(dotProduct));

        double originalRemainingLength = remainingMove.length();
        if (originalRemainingLength > 0.001) {
            double slidingLength = slidingVector.length();
            double lossRatio = 1.0 - (slidingLength / originalRemainingLength);

            double wallFrictionFactor = 0.5;
            double finalSpeedReduction = lossRatio * wallFrictionFactor;

            this.speed = this.speed * (1.0 - finalSpeedReduction);
        }

        Vector finalVectorPosition = safeHitPoint.add(slidingVector);

        Location correctedLoc = new Location(world, finalVectorPosition.getX(), targetLoc.getY(), finalVectorPosition.getZ());

        if (normal.getY() > 0.5 || normal.getY() < -0.5) {
            correctedLoc.setY(finalVectorPosition.getY());
        }

        correctedLoc.setYaw(location.getYaw());
        correctedLoc.setPitch(location.getPitch());

        return correctedLoc;
    }

}
