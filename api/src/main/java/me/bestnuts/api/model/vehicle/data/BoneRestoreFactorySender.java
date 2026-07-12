package me.bestnuts.api.model.vehicle.data;

import me.bestnuts.api.model.vehicle.component.bone.VehicleGroup;
import org.bukkit.entity.Entity;

import java.util.List;

public record BoneRestoreFactorySender(VehicleGroup group, List<Entity> entities) {
}
