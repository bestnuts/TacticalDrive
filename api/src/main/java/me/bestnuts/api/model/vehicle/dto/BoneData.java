package me.bestnuts.api.model.vehicle.dto;

import me.bestnuts.api.model.vehicle.component.VehicleGroup;

public record BoneData(VehicleGroup group, String type, String name) {
}
