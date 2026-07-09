package me.bestnuts.api.manager;

import me.bestnuts.api.model.vehicle.Vehicle;
import me.bestnuts.api.model.vehicle.component.VehicleEntity;
import me.bestnuts.api.model.vehicle.component.VehicleGroup;
import me.bestnuts.api.model.vehicle.configuration.VehicleConfiguration;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface VehicleCreator {
    Vehicle create(@NotNull VehicleEntity entity, @NotNull VehicleGroup group, @NotNull VehicleConfiguration configuration);
}
