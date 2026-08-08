package me.bestnuts.drive.core.model.vehicle.component.function;

import me.bestnuts.drive.api.bukkit.util.RotationHelper;
import me.bestnuts.drive.api.model.vehicle.Vehicle;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.drive.api.model.vehicle.component.function.VehicleFunction;
import me.bestnuts.drive.api.model.vehicle.data.VehicleOutput;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

import java.util.Map;

import static me.bestnuts.drive.api.bukkit.util.Constant.FIXED_DELTA_TIME;

public final class HeliRotorFunction extends VehicleFunction {

    private static final double MAX_STEP_DEGREE = 170.0;

    private final double degreePerSecond;
    private final double speedBoost;
    private final boolean vertical;
    private final int direction;

    private double angle;

    public HeliRotorFunction(@NotNull VehicleEntity parent, int delay, @NotNull Map<String, String> param) {
        super(parent, delay, param);
        this.degreePerSecond = Double.parseDouble(param.getOrDefault("speed", "1440"));
        this.speedBoost = Double.parseDouble(param.getOrDefault("speed-boost", "1.0"));
        this.vertical = Boolean.parseBoolean(param.getOrDefault("vertical", "false"));
        this.direction = Boolean.parseBoolean(param.getOrDefault("reverse", "false")) ? -1 : 1;
    }

    @Override
    public @Nullable VehicleOutput execute(@NotNull Vehicle vehicle) {
        Entity entity = getParent().getEntity();
        if (!(entity instanceof Display display)) return null;

        angle = (angle + spinStep(vehicle)) % 360.0;
        float radian = (float) Math.toRadians(angle);

        Quaternionf spin = vertical
                ? new Quaternionf().rotationX(radian)
                : new Quaternionf().rotationY(radian);

        Quaternionf rotation = RotationHelper.tilt(vehicle.motion().getPitch(), vehicle.motion().getRoll()).mul(spin);

        Transformation transformation = display.getTransformation();
        transformation.getLeftRotation().set(rotation);

        display.setTransformation(transformation);
        return null;
    }

    private double spinStep(@NotNull Vehicle vehicle) {
        double maxSpeed = vehicle.configuration().getPhysics().getMaxSpeed();
        double ratio = maxSpeed <= 0.0 ? 0.0 : Math.abs(vehicle.motion().getSpeed()) / maxSpeed;
        double step = degreePerSecond * (1.0 + ratio * speedBoost) * FIXED_DELTA_TIME;
        return Math.min(step, MAX_STEP_DEGREE) * direction;
    }
}
