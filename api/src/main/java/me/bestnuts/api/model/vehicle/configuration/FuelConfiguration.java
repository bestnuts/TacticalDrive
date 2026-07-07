package me.bestnuts.api.model.vehicle.configuration;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class FuelConfiguration implements SharedConfiguration {

    private final VehicleConfiguration parent;

    private final double fuelCapacity;
    private final double fuelConsumption;

    public FuelConfiguration(@NotNull VehicleConfiguration parent, @NotNull FileConfiguration configuration) {
        this.parent = parent;

        fuelCapacity = configuration.getDouble("fuel.capacity", 100.0);
        fuelConsumption = configuration.getDouble("fuel.consumption", 1.0);
    }

    @Override
    public @NotNull String name() {
        return parent.name();
    }
}
