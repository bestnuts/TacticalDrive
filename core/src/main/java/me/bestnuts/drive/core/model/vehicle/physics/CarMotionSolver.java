package me.bestnuts.drive.core.model.vehicle.physics;

import me.bestnuts.drive.api.bukkit.util.RotationHelper;
import me.bestnuts.drive.api.model.vehicle.configuration.VehicleConfiguration;
import me.bestnuts.drive.api.model.vehicle.data.VehicleMotion;
import me.bestnuts.drive.core.model.vehicle.configuration.CarHandleConfiguration;
import me.bestnuts.drive.core.model.vehicle.configuration.CarPhysicsConfiguration;
import me.bestnuts.drive.core.model.vehicle.data.BodyOutput;
import me.bestnuts.drive.core.model.vehicle.data.SuspensionOutput;
import me.bestnuts.drive.core.model.vehicle.data.WheelOutput;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

import static me.bestnuts.drive.api.bukkit.util.Constant.FIXED_DELTA_TIME;

public final class CarMotionSolver {

    private static final double HORSEPOWER_TO_WATT = 745.7;
    private static final double AIR_DENSITY = 1.2;
    private static final double DYNAMIC_PRESSURE_FACTOR = 0.5;

    private static final double FALLBACK_CHASSIS_LENGTH = 2.5;
    private static final double FALLBACK_CHASSIS_WIDTH = 1.6;

    private static final double MOVEMENT_EPSILON = 0.001;
    private static final double SPEED_EPSILON = 0.01;
    private static final double TORQUE_EPSILON = 0.01;
    private static final double OFFSET_EPSILON = 0.1;

    private static final double CEILING_INSET = 0.1;
    private static final double GROUND_INSET = 0.05;
    private static final double SURFACE_OFFSET = 0.05;
    private static final double VERTICAL_NORMAL_THRESHOLD = 0.5;

    private final CarPhysicsConfiguration physics;
    private final CarHandleConfiguration handle;

    public CarMotionSolver(@NotNull VehicleConfiguration configuration) {
        this.physics = (CarPhysicsConfiguration) configuration.getPhysics();
        this.handle = (CarHandleConfiguration) configuration.getHandle();
    }

    public @NotNull Location solve(@NotNull Location location,
                                   @NotNull VehicleMotion motion,
                                   @NotNull List<WheelOutput> wheels,
                                   @NotNull List<SuspensionOutput> suspensions,
                                   @NotNull List<BodyOutput> bodies) {
        ChassisSize chassis = measureChassis(suspensions);
        CollisionState collision = resolveCollision(suspensions, bodies);
        updateBodyAngles(motion, suspensions, chassis);
        double deltaY = resolveVerticalMotion(location, motion, suspensions);
        updateSteer(motion, wheels);
        integratePlanarMotion(location, motion, wheels, suspensions, collision, chassis);

        Location moved = move(location, resolveVelocity(location, motion, collision), motion);
        return moved.add(0, deltaY, 0);
    }

    private @NotNull CollisionState resolveCollision(@NotNull List<SuspensionOutput> suspensions, @NotNull List<BodyOutput> bodies) {
        boolean locked = false;
        double torqueFactor = 0.0;
        Vector pushBack = new Vector(0, 0, 0);
        Vector impactVelocity = new Vector(0, 0, 0);
        Vector separation = new Vector(0, 0, 0);

        for (SuspensionOutput output : suspensions) {
            if (!output.wall()) continue;
            locked = true;
            Vector offset = output.offset();
            torqueFactor += (offset.getX() * offset.getZ());
            pushBack.add(new Vector(-offset.getX(), 0, -offset.getZ()));
        }

        for (BodyOutput output : bodies) {
            impactVelocity.add(output.impactVelocity());
            separation.add(output.separation());
            if (!output.lock()) continue;
            locked = true;
            Vector offset = output.offset();
            torqueFactor += (offset.getX() * offset.getZ());
            pushBack.add(new Vector(-offset.getX(), 0, -offset.getZ()));
        }

        return new CollisionState(locked, torqueFactor, pushBack, impactVelocity, separation);
    }

    private void updateBodyAngles(@NotNull VehicleMotion motion, @NotNull List<SuspensionOutput> suspensions, @NotNull ChassisSize chassis) {
        double frontLeftY = 0.0, frontRightY = 0.0;
        double rearLeftY = 0.0, rearRightY = 0.0;
        int flCount = 0, frCount = 0, rlCount = 0, rrCount = 0;

        for (SuspensionOutput output : suspensions) {
            Vector offset = output.offset();
            if (offset.getZ() > 0 && offset.getX() < 0) { frontLeftY += output.wheelWorldY(); flCount++; }
            else if (offset.getZ() > 0 && offset.getX() > 0) { frontRightY += output.wheelWorldY(); frCount++; }
            else if (offset.getZ() < 0 && offset.getX() < 0) { rearLeftY += output.wheelWorldY(); rlCount++; }
            else if (offset.getZ() < 0 && offset.getX() > 0) { rearRightY += output.wheelWorldY(); rrCount++; }
        }

        double smoothing = physics.getBodyAngleSmoothing() * FIXED_DELTA_TIME;

        if (flCount == 0 || frCount == 0 || rlCount == 0 || rrCount == 0) {
            motion.setPitch(motion.getPitch() * (1.0 - smoothing));
            motion.setRoll(motion.getRoll() * (1.0 - smoothing));
            return;
        }

        double avgFrontY = (frontLeftY / flCount + frontRightY / frCount) * 0.5;
        double avgRearY = (rearLeftY / rlCount + rearRightY / rrCount) * 0.5;
        double avgLeftY = (frontLeftY / flCount + rearLeftY / rlCount) * 0.5;
        double avgRightY = (frontRightY / frCount + rearRightY / rrCount) * 0.5;

        double targetPitch = Math.toDegrees(Math.atan2(avgFrontY - avgRearY, chassis.length()));
        double targetRoll = Math.toDegrees(Math.atan2(avgLeftY - avgRightY, chassis.width()));

        motion.setPitch(motion.getPitch() + (targetPitch - motion.getPitch()) * smoothing);
        motion.setRoll(motion.getRoll() + (targetRoll - motion.getRoll()) * smoothing);
    }

    private @NotNull ChassisSize measureChassis(@NotNull List<SuspensionOutput> suspensions) {
        double totalLengthOffset = 0.0;
        double totalWidthOffset = 0.0;
        int lengthSampleCount = 0;
        int widthSampleCount = 0;

        for (SuspensionOutput output : suspensions) {
            Vector offset = output.offset();
            for (SuspensionOutput innerOutput : suspensions) {
                Vector innerOffset = innerOutput.offset();

                double deltaZ = offset.getZ() - innerOffset.getZ();
                if (deltaZ > OFFSET_EPSILON) {
                    totalLengthOffset += deltaZ;
                    lengthSampleCount++;
                }

                double deltaX = offset.getX() - innerOffset.getX();
                if (deltaX > OFFSET_EPSILON) {
                    totalWidthOffset += deltaX;
                    widthSampleCount++;
                }
            }
        }

        double length = lengthSampleCount > 0 ? (totalLengthOffset / lengthSampleCount) : FALLBACK_CHASSIS_LENGTH;
        double width = widthSampleCount > 0 ? (totalWidthOffset / widthSampleCount) : FALLBACK_CHASSIS_WIDTH;
        return new ChassisSize(length, width);
    }

    private double resolveVerticalMotion(@NotNull Location location, @NotNull VehicleMotion motion, @NotNull List<SuspensionOutput> suspensions) {
        double currentY = location.getY();
        double totalUpwardForce = 0.0;
        double climbLimitY = Double.NEGATIVE_INFINITY;
        double floorY = Double.NEGATIVE_INFINITY;
        for (SuspensionOutput output : suspensions) {
            climbLimitY = Math.max(climbLimitY, output.climbLimitY());
            if (!output.grounded()) continue;
            totalUpwardForce += output.upwardForce();
            double anchorY = output.climbLimitY() - physics.getMaxStepHeight();
            floorY = Math.max(floorY, output.wheelWorldY() + (currentY - anchorY));
        }
        motion.setClimbLimitY(climbLimitY);

        double acceleration = (totalUpwardForce / physics.getMass()) - physics.getGravity();
        double verticalVelocity = motion.getVerticalVelocity() + acceleration * FIXED_DELTA_TIME;
        verticalVelocity = Math.max(verticalVelocity, -physics.getMaxFallSpeed());
        double deltaY = verticalVelocity * FIXED_DELTA_TIME;

        if (currentY + deltaY < floorY) {
            motion.setVerticalVelocity(0.0);
            return floorY - currentY;
        }

        if (Math.abs(deltaY) <= MOVEMENT_EPSILON) {
            motion.setVerticalVelocity(verticalVelocity);
            return deltaY;
        }

        RayTraceResult yHit = location.getWorld().rayTraceBlocks(
                location,
                new Vector(0, deltaY > 0 ? 1 : -1, 0),
                Math.abs(deltaY),
                FluidCollisionMode.NEVER,
                true
        );

        if (yHit == null || yHit.getHitBlock() == null) {
            motion.setVerticalVelocity(verticalVelocity);
            return deltaY;
        }

        motion.setVerticalVelocity(0.0);
        double hitY = yHit.getHitPosition().getY();
        return deltaY > 0 ? (hitY - CEILING_INSET) - currentY : (hitY + GROUND_INSET) - currentY;
    }

    private void updateSteer(@NotNull VehicleMotion motion, @NotNull List<WheelOutput> wheels) {
        double combinedWheelSteer = 0.0;
        int steerableWheelCount = 0;

        for (WheelOutput output : wheels) {
            if (!output.steerable()) continue;
            combinedWheelSteer += output.wheelSteer();
            steerableWheelCount++;
        }

        if (steerableWheelCount == 0) return;

        double targetSteer = combinedWheelSteer / steerableWheelCount;
        motion.setSteer(motion.getSteer() + (targetSteer - motion.getSteer()) * handle.getSteerSmoothing() * FIXED_DELTA_TIME);
    }

    private void integratePlanarMotion(@NotNull Location location, @NotNull VehicleMotion motion,
                                       @NotNull List<WheelOutput> wheels, @NotNull List<SuspensionOutput> suspensions,
                                       @NotNull CollisionState collision, @NotNull ChassisSize chassis) {
        double mass = physics.getMass();
        double combinedForwardForce = 0.0;
        double combinedGrip = 0.0;

        for (WheelOutput output : wheels) {
            double surfaceFriction = surfaceFrictionOf(output.boneId(), suspensions);
            if (surfaceFriction <= 0.0) continue;
            combinedForwardForce += output.forwardForce() * surfaceFriction;
            combinedGrip += output.grip() * surfaceFriction;
        }

        double gripLimit = wheels.isEmpty() ? 0.0
                : (mass * physics.getGravity() / wheels.size()) * combinedGrip * handle.getCornerGrip();

        double speed = motion.getSpeed();
        double lateral = motion.getLateralSpeed();

        double longitudinalForce = combinedForwardForce * (physics.getHorsePower() * HORSEPOWER_TO_WATT) * physics.getEngineForceScale();
        double lateralForce = -lateral * mass / FIXED_DELTA_TIME;

        double demand = Math.hypot(longitudinalForce, lateralForce);
        if (demand > gripLimit && demand > MOVEMENT_EPSILON) {
            double scale = gripLimit / demand;
            longitudinalForce *= scale;
            lateralForce *= scale;
        }

        double speedMagnitude = Math.abs(speed);
        double airDrag = DYNAMIC_PRESSURE_FACTOR * AIR_DENSITY * physics.getDragCoefficient() * (speedMagnitude * speedMagnitude);
        double rollingDrag = mass * physics.getGravity() * physics.getRollingResistance();
        double resistance = speedMagnitude < SPEED_EPSILON ? 0.0 : airDrag + rollingDrag;

        double netForce = speed >= 0.0 ? longitudinalForce - resistance : longitudinalForce + resistance;
        speed += (netForce / mass) * FIXED_DELTA_TIME;
        lateral += (lateralForce / mass) * FIXED_DELTA_TIME;

        if (collision.locked() && Math.abs(speed) > SPEED_EPSILON) {
            double torque = collision.torqueFactor() * physics.getCollisionTorqueImpact() * FIXED_DELTA_TIME;
            motion.setSteer(speed > 0.0 ? motion.getSteer() + torque : motion.getSteer() - torque);
            speed = -speed * physics.getCollisionRestitution() * (1.0 - physics.getCollisionSpeedLoss());
        }

        double maxSpeed = physics.getMaxSpeed();
        if (speed > maxSpeed) speed = maxSpeed;
        if (speed < -maxSpeed * physics.getReverseSpeedRatio()) speed = -maxSpeed * physics.getReverseSpeedRatio();
        if (Math.abs(speed) < SPEED_EPSILON) speed = 0.0;
        if (Math.abs(lateral) < SPEED_EPSILON) lateral = 0.0;

        Vector impact = collision.impactVelocity();
        if (impact.lengthSquared() > MOVEMENT_EPSILON) {
            Vector forward = location.getDirection().setY(0).normalize();
            Vector lateralAxis = RotationHelper.rotateByYaw(location, new Vector(1, 0, 0)).setY(0).normalize();
            speed += impact.dot(forward);
            lateral += impact.dot(lateralAxis);
        }

        double yawDelta = 0.0;
        if (speed != 0.0) {
            double yawRate = (speed / chassis.length()) * Math.tan(Math.toRadians(motion.getSteer()));
            yawDelta = yawRate * FIXED_DELTA_TIME;
            location.setRotation((float) (location.getYaw() + Math.toDegrees(yawDelta)), 0);
        }

        double cos = Math.cos(yawDelta);
        double sin = Math.sin(yawDelta);
        motion.setSpeed(speed * cos - lateral * sin);
        motion.setLateralSpeed(speed * sin + lateral * cos);
    }

    private double surfaceFrictionOf(@NotNull UUID boneId, @NotNull List<SuspensionOutput> suspensions) {
        for (SuspensionOutput output : suspensions) {
            if (!output.boneId().equals(boneId)) continue;
            return output.grounded() ? output.friction() : 0.0;
        }
        return 1.0;
    }

    private @NotNull Vector resolveVelocity(@NotNull Location location, @NotNull VehicleMotion motion,
                                            @NotNull CollisionState collision) {
        Vector forwardVector = location.getDirection().setY(0).normalize();
        Vector lateralVector = RotationHelper.rotateByYaw(location, new Vector(1, 0, 0)).setY(0).normalize();

        Vector horizontalVelocity = forwardVector.multiply(motion.getSpeed() * FIXED_DELTA_TIME)
                .add(lateralVector.multiply(motion.getLateralSpeed() * FIXED_DELTA_TIME));

        if (collision.locked() && collision.pushBack().length() > MOVEMENT_EPSILON) {
            Vector worldPushDirection = RotationHelper.rotateByYaw(location, collision.pushBack().normalize());
            horizontalVelocity.add(worldPushDirection.multiply(physics.getCollisionPushDistance()));
        }

        horizontalVelocity.add(collision.separation());

        return horizontalVelocity.setY(0);
    }

    private @NotNull Location move(@NotNull Location location, @NotNull Vector velocity, @NotNull VehicleMotion motion) {
        World world = location.getWorld();
        if (world == null) return location;

        double distance = velocity.length();
        if (distance < MOVEMENT_EPSILON) {
            return keepRotation(location.clone().add(velocity), location);
        }

        Vector direction = velocity.clone().normalize();

        RayTraceResult rayResult = world.rayTraceBlocks(
                location,
                direction,
                distance,
                FluidCollisionMode.NEVER,
                true
        );

        Location targetLoc = location.clone().add(velocity);

        if (rayResult == null || rayResult.getHitBlock() == null) {
            return keepRotation(targetLoc, location);
        }

        Vector hitPoint = rayResult.getHitPosition();
        BlockFace hitFace = rayResult.getHitBlockFace();

        if (hitFace == null) {
            return keepRotation(targetLoc, location);
        }

        Vector normal = hitFace.getDirection();
        Vector safeHitPoint = hitPoint.clone().add(normal.clone().multiply(SURFACE_OFFSET));

        Vector remainingMove = targetLoc.toVector().subtract(hitPoint);
        double dotProduct = remainingMove.dot(normal);
        Vector slidingVector = remainingMove.subtract(normal.multiply(dotProduct));

        double originalRemainingLength = remainingMove.length();
        if (originalRemainingLength > MOVEMENT_EPSILON) {
            double lossRatio = 1.0 - (slidingVector.length() / originalRemainingLength);
            double finalSpeedReduction = lossRatio * physics.getWallFriction();
            motion.setSpeed(motion.getSpeed() * (1.0 - finalSpeedReduction));
        }

        Vector finalVectorPosition = safeHitPoint.add(slidingVector);

        Location correctedLoc = new Location(world, finalVectorPosition.getX(), targetLoc.getY(), finalVectorPosition.getZ());

        if (normal.getY() > VERTICAL_NORMAL_THRESHOLD || normal.getY() < -VERTICAL_NORMAL_THRESHOLD) {
            correctedLoc.setY(finalVectorPosition.getY());
        }

        return keepRotation(correctedLoc, location);
    }

    private @NotNull Location keepRotation(@NotNull Location target, @NotNull Location source) {
        target.setYaw(source.getYaw());
        target.setPitch(source.getPitch());
        return target;
    }

    private record CollisionState(boolean locked, double torqueFactor, Vector pushBack,
                                  Vector impactVelocity, Vector separation) {
    }

    private record ChassisSize(double length, double width) {
    }
}
