package me.bestnuts.drive.core.model.vehicle.physics;

import me.bestnuts.drive.api.bukkit.util.RotationHelper;
import me.bestnuts.drive.api.model.vehicle.configuration.VehicleConfiguration;
import me.bestnuts.drive.api.model.vehicle.data.VehicleMotion;
import me.bestnuts.drive.core.model.vehicle.configuration.HeliHandleConfiguration;
import me.bestnuts.drive.core.model.vehicle.configuration.HeliPhysicsConfiguration;
import me.bestnuts.drive.core.model.vehicle.data.ControlOutput;
import me.bestnuts.drive.core.model.vehicle.data.HeliBodyOutput;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static me.bestnuts.drive.api.bukkit.util.Constant.FIXED_DELTA_TIME;

public final class HeliMotionSolver {

    private static final double MOVEMENT_EPSILON = 0.001;
    private static final double SPEED_EPSILON = 0.01;
    private static final double SURFACE_OFFSET = 0.05;
    private static final double MAX_BANK_DEGREE = 60.0;

    private final HeliPhysicsConfiguration physics;
    private final HeliHandleConfiguration handle;

    public HeliMotionSolver(@NotNull VehicleConfiguration configuration) {
        this.physics = (HeliPhysicsConfiguration) configuration.getPhysics();
        this.handle = (HeliHandleConfiguration) configuration.getHandle();
    }

    public @NotNull Location solve(@NotNull Location location, @NotNull VehicleMotion motion,
                                   @NotNull List<ControlOutput> controls, @NotNull List<HeliBodyOutput> bodies) {
        ControlOutput control = controls.isEmpty() ? idle(location) : controls.get(0);

        followLookYaw(location, control);
        updateAttitude(motion, control);
        integrateVertical(motion, control);
        integrateForward(motion, control);
        integrateBanking(motion);

        return move(location, motion, blocked(bodies));
    }

    private @NotNull ControlOutput idle(@NotNull Location location) {
        return new ControlOutput(0.0, 0.0, false, location.getYaw(), false);
    }

    private boolean blocked(@NotNull List<HeliBodyOutput> bodies) {
        for (HeliBodyOutput output : bodies) {
            if (output.blocked()) return true;
        }
        return false;
    }

    private void followLookYaw(@NotNull Location location, @NotNull ControlOutput control) {
        if (!control.piloted()) return;

        float delta = control.lookYaw() - location.getYaw();
        while (delta > 180.0f) delta -= 360.0f;
        while (delta < -180.0f) delta += 360.0f;

        location.setRotation((float) (location.getYaw() + delta * handle.getYawSmoothing() * FIXED_DELTA_TIME), 0);
    }

    private void updateAttitude(@NotNull VehicleMotion motion, @NotNull ControlOutput control) {
        double targetRoll = -control.bank() * handle.getMaxRoll();
        double targetPitch = -(motion.getSpeed() / physics.getMaxSpeed()) * handle.getMaxNosePitch();

        double smoothing = handle.getAttitudeSmoothing() * FIXED_DELTA_TIME;
        motion.setRoll(motion.getRoll() + (targetRoll - motion.getRoll()) * smoothing);
        motion.setPitch(motion.getPitch() + (targetPitch - motion.getPitch()) * smoothing);
    }

    private void integrateVertical(@NotNull VehicleMotion motion, @NotNull ControlOutput control) {
        double gravity = physics.getGravity();
        double lift = 0.0;

        if (control.piloted()) {
            lift = gravity * (physics.getHoverThrust() + control.climb() * physics.getClimbPower());
        }

        double vertical = motion.getVerticalVelocity() + (lift - gravity) * FIXED_DELTA_TIME;
        motion.setVerticalVelocity(Math.max(vertical, -physics.getMaxFallSpeed()));
    }

    private void integrateForward(@NotNull VehicleMotion motion, @NotNull ControlOutput control) {
        double speed = motion.getSpeed();

        if (control.forward()) {
            speed += physics.getAcceleration() * FIXED_DELTA_TIME;
        } else {
            speed -= speed * physics.getForwardDrag() * FIXED_DELTA_TIME;
        }

        if (speed > physics.getMaxSpeed()) speed = physics.getMaxSpeed();
        if (Math.abs(speed) < SPEED_EPSILON) speed = 0.0;

        motion.setSpeed(speed);
    }

    private void integrateBanking(@NotNull VehicleMotion motion) {
        double roll = Math.max(-MAX_BANK_DEGREE, Math.min(MAX_BANK_DEGREE, motion.getRoll()));
        double acceleration = Math.tan(Math.toRadians(roll)) * physics.getGravity() * handle.getBankPower();

        double lateral = motion.getLateralSpeed() + acceleration * FIXED_DELTA_TIME;

        lateral -= lateral * handle.getBankDrag() * FIXED_DELTA_TIME;
        if (Math.abs(lateral) < SPEED_EPSILON) lateral = 0.0;

        motion.setLateralSpeed(lateral);
    }

    private @NotNull Location move(@NotNull Location location, @NotNull VehicleMotion motion, boolean blocked) {
        World world = location.getWorld();
        if (world == null) return location;

        Vector forward = location.getDirection().setY(0).normalize();
        Vector lateralAxis = RotationHelper.rotateByYaw(location, new Vector(1, 0, 0)).setY(0).normalize();

        Vector velocity = forward.multiply(motion.getSpeed() * FIXED_DELTA_TIME)
                .add(lateralAxis.multiply(motion.getLateralSpeed() * FIXED_DELTA_TIME));

        if (blocked) {
            motion.setSpeed(0.0);
            motion.setLateralSpeed(0.0);
            velocity = new Vector(0, 0, 0);
        }

        velocity.setY(resolveVertical(location, motion));

        Location target = location.clone().add(velocity);
        target.setYaw(location.getYaw());
        target.setPitch(location.getPitch());
        return target;
    }

    private double resolveVertical(@NotNull Location location, @NotNull VehicleMotion motion) {
        double deltaY = motion.getVerticalVelocity() * FIXED_DELTA_TIME;
        if (Math.abs(deltaY) <= MOVEMENT_EPSILON) return deltaY;

        double distance = Math.abs(deltaY) + (deltaY < 0 ? physics.getGroundClearance() : 0.0);
        RayTraceResult hit = location.getWorld().rayTraceBlocks(
                location,
                new Vector(0, deltaY > 0 ? 1 : -1, 0),
                distance,
                FluidCollisionMode.NEVER,
                true
        );

        if (hit == null || hit.getHitBlock() == null) return deltaY;

        motion.setVerticalVelocity(0.0);
        double hitY = hit.getHitPosition().getY();
        double limit = deltaY > 0
                ? (hitY - SURFACE_OFFSET) - location.getY()
                : (hitY + physics.getGroundClearance()) - location.getY();

        return deltaY > 0 ? Math.min(deltaY, limit) : Math.max(deltaY, limit);
    }
}
