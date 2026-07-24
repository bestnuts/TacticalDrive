package me.bestnuts.drive.api.model.vehicle.data;

import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleGroup;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import java.util.List;

public record BoneRestoreFactorySender(VehicleGroup group, ConfigurationSection section, List<Entity> entities) {
}
