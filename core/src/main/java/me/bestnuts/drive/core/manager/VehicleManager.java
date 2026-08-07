package me.bestnuts.drive.core.manager;

import me.bestnuts.drive.api.manager.AbstractVehicleManager;
import me.bestnuts.drive.api.model.vehicle.Vehicle;
import me.bestnuts.drive.api.model.vehicle.VehicleRegistryType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class VehicleManager implements AbstractVehicleManager {

    private final Map<UUID, Vehicle> idMap = new HashMap<>();
    private final Map<String, Map<UUID, Vehicle>> typeMap = new HashMap<>();

    @Override
    public @NotNull Optional<Vehicle> find(UUID id) {
        return Optional.ofNullable(idMap.get(id));
    }

    @Override
    public void unregister(UUID id) {
        Vehicle vehicle = idMap.remove(id);
        if (vehicle != null) {
            Map<UUID, Vehicle> subMap = typeMap.get(vehicle.type());
            if (subMap != null) {
                subMap.remove(id);
            }
        }
    }

    @Override
    public void register(Vehicle vehicle) {
        UUID id = vehicle.entity().getUniqueId();
        String type = vehicle.type();

        idMap.put(id, vehicle);
        typeMap.computeIfAbsent(type, k -> new HashMap<>()).put(id, vehicle);
    }

    @Override
    public void clear() {
        idMap.clear();
        typeMap.clear();
    }

    @Override
    public @NotNull Collection<Vehicle> getByType(VehicleRegistryType type) {
        Map<UUID, Vehicle> subMap = typeMap.get(type.getName());
        return subMap != null ? List.copyOf(subMap.values()) : List.of();
    }

    @Override
    public @NotNull Collection<Vehicle> getAll() {
        return List.copyOf(idMap.values());
    }
}
