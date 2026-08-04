package me.bestnuts.drive.api.manager;

import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleGroup;
import me.bestnuts.drive.api.model.vehicle.data.GroupFactorySender;
import me.bestnuts.drive.api.model.vehicle.data.GroupRestoreFactorySender;

public interface GroupFactory extends Factory<GroupFactorySender, VehicleGroup>, RestoreFactory<GroupRestoreFactorySender, VehicleGroup> {
}
