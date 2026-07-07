package me.bestnuts.api.model.vehicle;

import lombok.Getter;
import me.bestnuts.api.model.vehicle.component.VehicleEntity;
import me.bestnuts.api.model.vehicle.component.VehicleGroup;
import me.bestnuts.api.model.vehicle.configuration.VehicleConfiguration;
import org.jetbrains.annotations.NotNull;

public interface Vehicle {

    @NotNull String type();

    @NotNull VehicleEntity entity();

    @NotNull VehicleConfiguration configuration();

    @NotNull VehicleGroup group();

    void tick();

    @Getter
    enum RegistryType {
        CAR("car"),
        HELI("heli");

        private final String name;

        RegistryType(String name) {
            this.name = name;
        }
    }
}
