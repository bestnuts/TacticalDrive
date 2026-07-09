package me.bestnuts.api.model.vehicle.dto;

import me.bestnuts.api.model.vehicle.configuration.VehicleConfiguration;
import org.bukkit.entity.Entity;

import java.util.List;

public record GroupRestoreFactorySender(VehicleConfiguration configuration, List<Entity> entities) {
}
