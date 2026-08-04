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

public final class CarWheelFunction extends VehicleFunction {

    private final boolean steerable;
    private final boolean driven;
    private final double sideSign;
    private final String link;

    private VehicleSeat seat;
    private CarHandleConfiguration handleConfiguration;

    private double roll = 0.0;

    public CarWheelFunction(@NotNull VehicleEntity parent, int delay, @NotNull Map<String, String> param) {
        super(parent, delay, param);
        this.steerable = Boolean.parseBoolean(param.getOrDefault("steerable", "false"));
        this.driven = Boolean.parseBoolean(param.getOrDefault("driven", "false"));
        this.sideSign = Double.parseDouble(param.getOrDefault("side-sign", "0"));
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
        double lateralForce = 0.0;
        double structuralSteer = 0.0;

        if (this.steerable) {
            structuralSteer = rawSteering * handleConfiguration.getSteeringAngleMax() * handleConfiguration.getSteeringSensitivity();
        }

        double surfaceFriction = 1.0 * handleConfiguration.getWheelFriction();

        if (this.driven && Math.abs(rawThrottle) > 0.01) {
            double slipRatio = (vehicle.motion().getSpeed() < 3.0) ? 0.7 : 0.1;
            forwardForce = rawThrottle * surfaceFriction * (1.0 - slipRatio);
        }

        if (Math.abs(vehicle.motion().getSteer()) > 0.1) {
            double centrifugalTarget = Math.sin(Math.toRadians(vehicle.motion().getSteer())) * vehicle.motion().getSpeed();
            lateralForce = centrifugalTarget * surfaceFriction * (1.0 + (this.sideSign * 0.1));
        }

        float carYaw = vehicle.entity().getLocation().getYaw();
        double visualSteer = structuralSteer;

        if (vehicle.motion().getSpeed() < 0) {
            structuralSteer = -structuralSteer;
        }

        this.roll += (vehicle.motion().getSpeed() * vehicle.motion().getSpeed()) * 256.0 * Math.signum(vehicle.motion().getSpeed());
        this.roll = this.roll % 360.0;
        float rollRad = (float) Math.toRadians(this.roll);

        updateRotation(vehicle, carYaw, visualSteer, rollRad);

        return new WheelOutput(forwardForce, lateralForce, structuralSteer);
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
