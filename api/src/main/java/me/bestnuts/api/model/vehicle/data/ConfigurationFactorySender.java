package me.bestnuts.api.model.vehicle.data;

import org.bukkit.configuration.file.FileConfiguration;

public record ConfigurationFactorySender(FileConfiguration configuration, String name) {
}
