package me.bestnuts.drive.api.manager;

import me.bestnuts.drive.api.model.vehicle.Vehicle;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleGroup;
import me.bestnuts.drive.api.model.vehicle.configuration.VehicleConfiguration;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface VehicleCreator {
    Vehicle create(@NotNull VehicleEntity entity, @NotNull VehicleGroup group, @NotNull VehicleConfiguration configuration);
}
