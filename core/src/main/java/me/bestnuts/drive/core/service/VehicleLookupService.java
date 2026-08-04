package me.bestnuts.drive.core.service;

import lombok.RequiredArgsConstructor;
import me.bestnuts.drive.api.bukkit.register.VehicleFactoryRegistry;
import me.bestnuts.drive.api.bukkit.util.DataKeyHelper;
import me.bestnuts.drive.api.manager.AbstractVehicleManager;
import me.bestnuts.drive.api.manager.VehicleFactory;
import me.bestnuts.drive.api.model.vehicle.Vehicle;
import me.bestnuts.drive.api.model.vehicle.data.DataKey;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public final class VehicleLookupService {

    private final AbstractVehicleManager vehicleManager;
    private final VehicleFactoryRegistry factoryRegistry;

    public @NotNull Optional<Vehicle> findOrRestore(@NotNull Entity entity) {
        String id = DataKeyHelper.get(entity, DataKey.VEHICLE_ROOT_ID, String.class);
        if (id == null || id.isEmpty()) return Optional.empty();
        UUID rootId = UUID.fromString(id);
        Optional<Vehicle> found = vehicleManager.find(rootId);
        if (found.isPresent()) return found;
        return restore(entity, rootId);
    }

    private @NotNull Optional<Vehicle> restore(@NotNull Entity entity, @NotNull UUID rootId) {
        Entity root = entity.getWorld().getEntity(rootId);
        if (root == null) return Optional.empty();
        String type = DataKeyHelper.get(root, DataKey.VEHICLE_ROOT_TYPE, String.class);
        if (type == null) return Optional.empty();
        VehicleFactory factory = factoryRegistry.find(type);
        if (factory == null) return Optional.empty();
        Vehicle vehicle = factory.regenerate(root);
        if (vehicle == null) return Optional.empty();
        vehicleManager.register(vehicle);
        return Optional.of(vehicle);
    }
}
