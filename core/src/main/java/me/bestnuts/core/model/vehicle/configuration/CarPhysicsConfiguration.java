package me.bestnuts.core.model.vehicle.configuration;

import me.bestnuts.api.model.vehicle.configuration.PhysicsConfiguration;
import me.bestnuts.api.model.vehicle.configuration.VehicleConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class CarPhysicsConfiguration extends PhysicsConfiguration {

    public CarPhysicsConfiguration(@NotNull VehicleConfiguration parent, @NotNull FileConfiguration configuration) {
        super(parent, configuration);
    }
}
