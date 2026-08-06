package me.bestnuts.drive.core.model.vehicle.component.function;

import me.bestnuts.drive.api.bukkit.util.FunctionParamHelper;
import me.bestnuts.drive.api.bukkit.util.RotationHelper;
import me.bestnuts.drive.api.model.entity.Driver;
import me.bestnuts.drive.api.model.vehicle.Vehicle;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleSeat;
import me.bestnuts.drive.api.model.vehicle.component.function.VehicleFunction;
import me.bestnuts.drive.api.model.vehicle.data.VehicleOutput;
import me.bestnuts.drive.core.model.vehicle.configuration.CarHandleConfiguration;
import me.bestnuts.drive.core.model.vehicle.data.WheelOutput;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

import java.util.Map;

import static me.bestnuts.drive.api.bukkit.util.Constant.FIXED_DELTA_TIME;

public final class CarWheelFunction extends VehicleFunction {

    private static final double THROTTLE_EPSILON = 0.01;
    private static final double SPEED_EPSILON = 0.01;
    private static final double TRACTION_SPEED = 3.0;
    private static final double MIN_RADIUS = 0.05;

    private final boolean steerable;
    private final boolean driven;
    private final double sideSign;
    private final double radius;
    private final String link;

    private VehicleSeat seat;
    private CarHandleConfiguration handleConfiguration;

    private double roll = 0.0;

    public CarWheelFunction(@NotNull VehicleEntity parent, int delay, @NotNull Map<String, String> param) {
        super(parent, delay, param);
        this.steerable = Boolean.parseBoolean(param.getOrDefault("steerable", "false"));
        this.driven = Boolean.parseBoolean(param.getOrDefault("driven", "false"));
        this.sideSign = Double.parseDouble(param.getOrDefault("side-sign", "0"));
        this.radius = Math.max(Double.parseDouble(param.getOrDefault("radius", "0.5")), MIN_RADIUS);
        this.link = param.getOrDefault("link", "root");
    }

    @Override
    public @Nullable VehicleOutput execute(@NotNull Vehicle vehicle) {
        if (seat == null) {
            if (!(FunctionParamHelper.getLink(link, vehicle) instanceof VehicleSeat target)) return null;
            seat = target;
            handleConfiguration = (CarHandleConfiguration) vehicle.configuration().getHandle();
        }
        Driver driver = seat.getDriver();

        double rawSteering = 0.0;
        double rawThrottle = 0.0;

        if (driver != null) {
            rawSteering = driver.getInput().getSideway();
            rawThrottle = driver.getInput().getForward();
        }

        double forwardForce = 0.0;
        double structuralSteer = 0.0;

        double speed = vehicle.motion().getSpeed();

        if (this.steerable) {
            double speedFactor = 1.0 / (1.0 + Math.abs(speed) * handleConfiguration.getSteerSpeedFalloff());
            structuralSteer = rawSteering * handleConfiguration.getSteeringAngleMax()
                    * handleConfiguration.getSteeringSensitivity() * speedFactor;
        }

        double tireFriction = handleConfiguration.getWheelFriction();

        if (this.driven && Math.abs(rawThrottle) > THROTTLE_EPSILON) {
            boolean braking = Math.abs(speed) > SPEED_EPSILON && rawThrottle * speed < 0;
            if (braking) {
                forwardForce = rawThrottle * tireFriction * handleConfiguration.getBrakeForce();
            } else {
                double slipRatio = (Math.abs(speed) < TRACTION_SPEED) ? 0.7 : 0.1;
                forwardForce = rawThrottle * tireFriction * (1.0 - slipRatio);
            }
        }

        float carYaw = vehicle.entity().getLocation().getYaw();

        this.roll = (this.roll + Math.toDegrees(speed / this.radius) * FIXED_DELTA_TIME) % 360.0;
        float rollRad = (float) Math.toRadians(this.roll);

        updateRotation(vehicle, carYaw, structuralSteer, rollRad);

        return new WheelOutput(getParent().getUniqueId(), this.steerable, forwardForce, tireFriction, structuralSteer);
    }

    private void updateRotation(@NotNull Vehicle vehicle, float carYaw, double steer, float spin) {
        Entity entity = getParent().getEntity();
        entity.setRotation(carYaw, 0);
        if (!(entity instanceof Display display)) {
            return;
        }

        Quaternionf rotation = RotationHelper.tilt(vehicle.motion().getPitch(), vehicle.motion().getRoll())
                .mul(new Quaternionf().rotationY((float) Math.toRadians(-steer)))
                .mul(new Quaternionf().rotationX(spin));

        Transformation transformation = display.getTransformation();

        transformation.getLeftRotation().set(rotation);

        display.setTransformation(transformation);
    }
}
