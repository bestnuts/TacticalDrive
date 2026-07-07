package me.bestnuts.core.model.vehicle;

import me.bestnuts.api.model.vehicle.Vehicle;
import me.bestnuts.api.model.vehicle.component.VehicleEntity;
import me.bestnuts.api.model.vehicle.component.VehicleGroup;
import me.bestnuts.api.model.vehicle.configuration.VehicleConfiguration;
import me.bestnuts.core.model.vehicle.component.VehicleRoot;
import org.jetbrains.annotations.NotNull;

public abstract class VehicleCore implements Vehicle {

    private final VehicleEntity entity;
    private final VehicleGroup group;
    private final VehicleConfiguration configuration;

    public VehicleCore(@NotNull VehicleRoot entity, @NotNull VehicleGroup group, @NotNull VehicleConfiguration configuration) {
        this.entity = entity;
        this.group = group;
        this.configuration = configuration;
    }

    @Override
    public @NotNull VehicleEntity entity() {
        return entity;
    }

    @Override
    public @NotNull VehicleGroup group() {
        return group;
    }

    @Override
    public @NotNull VehicleConfiguration configuration() {
        return configuration;
    }
}
