package me.bestnuts.drive.core.service;

import lombok.RequiredArgsConstructor;
import me.bestnuts.drive.api.manager.AbstractVehicleManager;
import me.bestnuts.drive.api.manager.VehicleFactory;
import me.bestnuts.drive.api.model.vehicle.Vehicle;
import me.bestnuts.drive.api.model.vehicle.data.VehicleFactorySender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public final class VehicleSpawnService {

    private final AbstractVehicleManager manager;
    private final VehicleSeatService seatService;

    public @Nullable Vehicle spawn(@NotNull VehicleFactory factory, @NotNull VehicleFactorySender sender) {
        Vehicle vehicle = factory.generate(sender);
        if (vehicle == null) return null;
        manager.register(vehicle);
        return vehicle;
    }

    public void despawn(@NotNull Vehicle vehicle) {
        seatService.releaseAll(vehicle);
        manager.unregister(vehicle.entity().getUniqueId());
        vehicle.remove();
    }
}
