package me.bestnuts.api.model.vehicle.configuration;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public abstract class BoneAbstractConfiguration implements BoneConfiguration {

    private final VehicleConfiguration parent;

    public BoneAbstractConfiguration(@NotNull VehicleConfiguration parent, @NotNull ConfigurationSection section) {
        this.parent = parent;
    }

    @Override
    public @NotNull String name() {
        return parent.name();
    }
}
