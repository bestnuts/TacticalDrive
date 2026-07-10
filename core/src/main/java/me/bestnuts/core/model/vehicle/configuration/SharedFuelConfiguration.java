package me.bestnuts.core.model.vehicle.configuration;

import me.bestnuts.api.model.vehicle.configuration.FuelConfiguration;
import me.bestnuts.api.model.vehicle.configuration.VehicleConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public final class SharedFuelConfiguration extends FuelConfiguration {

    public SharedFuelConfiguration(@NotNull VehicleConfiguration parent, @NotNull FileConfiguration configuration) {
        super(parent, configuration);
    }
}
