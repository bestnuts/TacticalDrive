package me.bestnuts.api.model.vehicle.configuration;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class DefaultConfiguration implements SharedConfiguration {

    private final VehicleConfiguration parent;

    private final double maxHealth;

    public DefaultConfiguration(@NotNull VehicleConfiguration parent, @NotNull FileConfiguration configuration) {
        this.parent = parent;

        maxHealth = configuration.getDouble("default.max-health", 100.0);
    }

    @Override
    public @NotNull String name() {
        return parent.name();
    }
}
