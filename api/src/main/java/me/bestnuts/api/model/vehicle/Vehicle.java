package me.bestnuts.api.model.vehicle;

import me.bestnuts.api.model.vehicle.component.VehicleEntity;
import me.bestnuts.api.model.vehicle.component.VehicleGroup;
import me.bestnuts.api.model.vehicle.configuration.VehicleConfiguration;
import org.jetbrains.annotations.NotNull;

public abstract class Vehicle {

    private final VehicleEntity entity;
    private final VehicleGroup group;
    private final VehicleConfiguration configuration;

    public Vehicle(@NotNull VehicleEntity entity, @NotNull VehicleGroup group, @NotNull VehicleConfiguration configuration) {
        this.entity = entity;
        this.group = group;
        this.configuration = configuration;
    }

    public @NotNull VehicleEntity entity() {
        return entity;
    }

    public @NotNull VehicleGroup group() {
        return group;
    }

    public @NotNull VehicleConfiguration configuration() {
        return configuration;
    }

    public abstract @NotNull String type();

    public abstract void tick();
}
