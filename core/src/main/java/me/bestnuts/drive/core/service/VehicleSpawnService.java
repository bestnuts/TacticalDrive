package me.bestnuts.drive.core.service;

import lombok.RequiredArgsConstructor;
import me.bestnuts.drive.api.manager.VehicleFactory;
import me.bestnuts.drive.api.manager.AbstractVehicleManager;
import me.bestnuts.drive.api.model.vehicle.Vehicle;
import me.bestnuts.drive.api.model.vehicle.data.VehicleFactorySender;

@RequiredArgsConstructor
public final class VehicleSpawnService {

    private final AbstractVehicleManager manager;

    public void spawn(VehicleFactory factory, VehicleFactorySender sender) {
        Vehicle vehicle = factory.generate(sender);
        if (vehicle == null) return;
        manager.register(vehicle);
    }
}
