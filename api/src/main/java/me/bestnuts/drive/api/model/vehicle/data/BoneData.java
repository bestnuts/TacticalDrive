package me.bestnuts.drive.api.model.vehicle.data;

import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleGroup;
import me.bestnuts.drive.api.model.vehicle.component.function.VehicleFunction;

import java.util.List;
import java.util.function.Function;

public record BoneData(VehicleGroup group, Function<VehicleEntity, List<VehicleFunction>> tick, Function<VehicleEntity, List<VehicleFunction>> init, String type, String name) {
}
