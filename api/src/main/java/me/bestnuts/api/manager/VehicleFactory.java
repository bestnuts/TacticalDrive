package me.bestnuts.api.manager;

import me.bestnuts.api.model.vehicle.Vehicle;
import me.bestnuts.api.model.vehicle.dto.VehicleFactorySender;
import org.jetbrains.annotations.NotNull;

public interface VehicleFactory extends Factory<VehicleFactorySender, Vehicle> {
    @NotNull String name();
}
