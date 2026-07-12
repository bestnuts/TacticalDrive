package me.bestnuts.api.model.vehicle.data;

import me.bestnuts.api.model.vehicle.component.VehicleGroup;

public record BoneFactorySender(VehicleGroup group, EntityFactorySender sender) {
}
