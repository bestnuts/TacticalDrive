package me.bestnuts.drive.api.manager;

import me.bestnuts.drive.api.model.vehicle.Vehicle;
import me.bestnuts.drive.api.model.vehicle.VehicleRegistryType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;

public interface VehicleManager extends Manager<UUID, Vehicle> {

    @NotNull Collection<Vehicle> getByType(VehicleRegistryType type);

    @NotNull Collection<Vehicle> getAll();
}
