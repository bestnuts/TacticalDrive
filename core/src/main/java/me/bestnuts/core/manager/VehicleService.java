package me.bestnuts.core.manager;

import lombok.RequiredArgsConstructor;
import me.bestnuts.api.manager.VehicleFactory;
import me.bestnuts.api.manager.VehicleManager;
import me.bestnuts.api.model.vehicle.Vehicle;
import me.bestnuts.api.model.vehicle.data.VehicleFactorySender;

@RequiredArgsConstructor
public final class VehicleService {

    private final VehicleManager manager;

    public void spawn(VehicleFactory factory, VehicleFactorySender sender) {
        Vehicle vehicle = factory.generate(sender);
        if (vehicle == null) return;
        manager.register(vehicle);
    }
}
