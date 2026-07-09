package me.bestnuts.api.model.vehicle.dto;

import me.bestnuts.api.model.vehicle.component.VehicleGroup;
import org.bukkit.entity.Entity;

import java.util.List;

public record BoneRestoreFactorySender(VehicleGroup group, List<Entity> entities) {
}
