package me.bestnuts.api.model.vehicle.dto;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public record EntityFactorySender(Location location, ConfigurationSection section) {
}
