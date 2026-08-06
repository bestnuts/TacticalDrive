package me.bestnuts.drive.core.model.vehicle.configuration;

import lombok.Getter;
import me.bestnuts.drive.api.model.vehicle.configuration.HandleConfiguration;
import me.bestnuts.drive.api.model.vehicle.configuration.VehicleConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

@Getter
public final class CarHandleConfiguration extends HandleConfiguration {

    private final double wheelFriction;
    private final double steeringAngleMax;

    private final double steerSmoothing;
    private final double steerSpeedFalloff;
    private final double cornerGrip;

    public CarHandleConfiguration(@NotNull VehicleConfiguration parent, @NotNull FileConfiguration configuration) {
        super(parent, configuration);
        wheelFriction = configuration.getDouble("handle.wheel-friction", 0.87);
        steeringAngleMax = configuration.getDouble("handle.steering-angle-max", 22.5);

        steerSmoothing = configuration.getDouble("handle.steer-smoothing", 5.0);
        steerSpeedFalloff = configuration.getDouble("handle.steer-speed-falloff", 0.05);
        cornerGrip = configuration.getDouble("handle.corner-grip", 4.0);
    }
}
