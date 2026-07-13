package me.bestnuts.api.model.vehicle.data;

import me.bestnuts.api.model.vehicle.component.bone.VehicleEntity;
import org.bukkit.configuration.ConfigurationSection;

public record FunctionFactorySender(VehicleEntity entity, ConfigurationSection section) {
}
