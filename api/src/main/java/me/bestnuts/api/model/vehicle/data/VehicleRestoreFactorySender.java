package me.bestnuts.api.model.vehicle.data;

import org.bukkit.entity.Entity;

import java.util.List;

public record VehicleRestoreFactorySender(Entity root, List<Entity> entities) {
}
