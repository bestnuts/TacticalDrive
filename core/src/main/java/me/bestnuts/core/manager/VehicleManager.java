package me.bestnuts.core.manager;

import me.bestnuts.api.manager.Manager;
import me.bestnuts.api.model.vehicle.Vehicle;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class VehicleManager implements me.bestnuts.api.manager.VehicleManager {

    private final Map<UUID, Vehicle> idMap = new HashMap<>();
    private final Map<String, Map<UUID, Vehicle>> typeMap = new HashMap<>();

    @Override
    public @NotNull Class<? extends Manager<?, ?>> type() {
        return me.bestnuts.api.manager.VehicleManager.class;
    }

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
    public @NotNull Collection<Vehicle> getByType(Vehicle.RegistryType type) {
        Map<UUID, Vehicle> subMap = typeMap.get(type.getName());
        return subMap != null ? Collections.unmodifiableCollection(subMap.values()) : Collections.emptyList();
    }

    public @NotNull Collection<Vehicle> getByType(String type) {
        Map<UUID, Vehicle> subMap = typeMap.get(type);
        return subMap != null ? Collections.unmodifiableCollection(subMap.values()) : Collections.emptyList();
    }

    @Override
    public @NotNull Collection<Vehicle> getAll() {
        return Collections.unmodifiableCollection(idMap.values());
    }
}
