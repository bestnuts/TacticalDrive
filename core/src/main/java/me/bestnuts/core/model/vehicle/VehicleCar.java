package me.bestnuts.core.model.vehicle;

import me.bestnuts.api.model.vehicle.Vehicle;
import me.bestnuts.api.model.vehicle.VehicleRegistryType;
import me.bestnuts.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.api.model.vehicle.component.bone.VehicleGroup;
import me.bestnuts.api.model.vehicle.configuration.VehicleConfiguration;
import org.jetbrains.annotations.NotNull;

public final class VehicleCar extends Vehicle {

    public VehicleCar(@NotNull VehicleEntity entity, @NotNull VehicleGroup group, @NotNull VehicleConfiguration configuration) {
        super(entity, group, configuration);
    }

    @Override
    public @NotNull String type() {
        return VehicleRegistryType.CAR.getName();
    }

    @Override
    public void tick() {
        super.tick();
    }
}
