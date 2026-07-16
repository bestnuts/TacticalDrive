package me.bestnuts.core.model.vehicle.component.function;

import me.bestnuts.api.bukkit.util.FunctionParamHelper;
import me.bestnuts.api.model.entity.Driver;
import me.bestnuts.api.model.vehicle.Vehicle;
import me.bestnuts.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.api.model.vehicle.component.bone.VehicleSeat;
import me.bestnuts.api.model.vehicle.component.function.VehicleFunction;
import me.bestnuts.core.model.vehicle.VehicleCar;
import me.bestnuts.core.model.vehicle.configuration.CarHandleConfiguration;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

import java.util.Map;

public final class CarWheelFunction extends VehicleFunction {

    private final boolean steerable;
    private final boolean driven;
    private final double sideSign;
    private final String link;

    private VehicleCar car;
    private VehicleSeat seat;
    private CarHandleConfiguration handleConfiguration;
    private boolean isLoad;

    private double roll = 0.0;

    public CarWheelFunction(@NotNull VehicleEntity parent, int delay, @NotNull Map<String, String> param) {
        super(parent, delay, param);
        this.steerable = Boolean.parseBoolean(param.getOrDefault("steerable", "false"));
        this.driven = Boolean.parseBoolean(param.getOrDefault("driven", "false"));
        this.sideSign = Double.parseDouble(param.getOrDefault("side-sign", "0"));
        this.link = param.getOrDefault("link", "root");
    }

    @Override
    public void execute(@NotNull Vehicle vehicle) {
        if (!isLoad) {
            if (seat == null) {
                VehicleEntity entity = FunctionParamHelper.getLink(link, vehicle);
                if (!(entity instanceof VehicleSeat target)) return;
                seat = target;
                if (vehicle instanceof VehicleCar vehicleCar) {
                    car = vehicleCar;
                    handleConfiguration = (CarHandleConfiguration) car.configuration().getHandle();
                    isLoad = true;
                }
            }
            return;
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

        double surfaceFriction = 1.0;

        if (this.driven && Math.abs(rawThrottle) > 0.01) {
            double slipRatio = (car.getSpeed() < 3.0) ? 0.7 : 0.1;
            forwardForce = rawThrottle * surfaceFriction * (1.0 - slipRatio);
        }

        if (Math.abs(car.getSteer()) > 0.1) {
            double centrifugalTarget = Math.sin(Math.toRadians(car.getSteer())) * car.getSpeed();
            lateralForce = centrifugalTarget * surfaceFriction * (1.0 + (this.sideSign * 0.1));
        }

        float steerYaw = (float) (car.entity().getLocation().getYaw() + structuralSteer);

        if (rawThrottle < 0) {
            structuralSteer = -structuralSteer;
        }

        car.getWheelOutputs().add(new WheelOutput(forwardForce, lateralForce, structuralSteer));

        this.roll += (car.getSpeed() * car.getSpeed()) * 256.0 * Math.signum(car.getSpeed());
        this.roll = this.roll % 360.0;
        float rollRad = (float) Math.toRadians(this.roll);

        updateRotation(steerYaw, rollRad);
    }

    private void updateRotation(float yaw, float roll) {
        Entity entity = getParent().getEntity();
        entity.setRotation(yaw, 0);
        if (!(entity instanceof Display display)) {
            return;
        }
        Quaternionf quaternionf = new Quaternionf().rotationX(roll);

        Transformation transformation = display.getTransformation();

        transformation.getLeftRotation().set(quaternionf);

        display.setTransformation(transformation);
    }

    public record WheelOutput(double forwardForce, double lateralForce, double wheelSteer) {
    }
}
