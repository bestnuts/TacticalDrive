package me.bestnuts.core.model.vehicle.configuration;

import lombok.Getter;
import me.bestnuts.api.model.vehicle.configuration.HandleConfiguration;
import me.bestnuts.api.model.vehicle.configuration.VehicleConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

@Getter
public final class CarHandleConfiguration extends HandleConfiguration {

    private final double wheelFriction;
    private final double steeringAngleMax;

    public CarHandleConfiguration(@NotNull VehicleConfiguration parent, @NotNull FileConfiguration configuration) {
        super(parent, configuration);
        wheelFriction = configuration.getDouble("handle.wheel-friction", 0.87);
        steeringAngleMax = configuration.getDouble("handle.steering-angle-max", 22.5);
    }
}
