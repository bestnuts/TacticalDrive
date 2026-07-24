package me.bestnuts.drive.api.model.vehicle.data;

import me.bestnuts.drive.api.model.vehicle.configuration.VehicleConfiguration;
import org.bukkit.entity.Entity;

import java.util.List;

public record GroupRestoreFactorySender(VehicleConfiguration configuration, List<Entity> entities) {
}
