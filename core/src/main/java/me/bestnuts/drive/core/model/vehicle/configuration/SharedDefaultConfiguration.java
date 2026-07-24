package me.bestnuts.drive.core.model.vehicle.configuration;

import me.bestnuts.drive.api.model.vehicle.configuration.DefaultConfiguration;
import me.bestnuts.drive.api.model.vehicle.configuration.VehicleConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public final class SharedDefaultConfiguration extends DefaultConfiguration {

    public SharedDefaultConfiguration(@NotNull VehicleConfiguration parent, @NotNull FileConfiguration configuration) {
        super(parent, configuration);
    }
}
