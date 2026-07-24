package me.bestnuts.drive.api.model.vehicle.data;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public record EntityFactorySender(Location location, ConfigurationSection section) {

    @NotNull
    public EntityFactorySender withSection(@NotNull ConfigurationSection section) {
        return new EntityFactorySender(location, section);
    }
}
