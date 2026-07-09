package me.bestnuts.api.model.vehicle.dto;

import org.bukkit.configuration.file.FileConfiguration;

public record ConfigurationFactorySender(FileConfiguration configuration, String name) {
}
