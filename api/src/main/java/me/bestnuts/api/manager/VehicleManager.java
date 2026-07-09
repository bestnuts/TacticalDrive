package me.bestnuts.api.manager;

import me.bestnuts.api.model.vehicle.Vehicle;
import me.bestnuts.api.model.vehicle.VehicleRegistryType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;

public interface VehicleManager extends Manager<UUID, Vehicle> {

    @NotNull Collection<Vehicle> getByType(VehicleRegistryType type);

    @NotNull Collection<Vehicle> getAll();
}
