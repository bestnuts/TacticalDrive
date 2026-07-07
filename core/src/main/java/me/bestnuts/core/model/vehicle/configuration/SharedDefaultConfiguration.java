package me.bestnuts.core.model.vehicle.configuration;

import me.bestnuts.api.model.vehicle.configuration.DefaultConfiguration;
import me.bestnuts.api.model.vehicle.configuration.VehicleConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class SharedDefaultConfiguration extends DefaultConfiguration {

    public SharedDefaultConfiguration(@NotNull VehicleConfiguration parent, @NotNull FileConfiguration configuration) {
        super(parent, configuration);
    }
}
