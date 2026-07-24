package me.bestnuts.drive.core.manager;

import me.bestnuts.drive.api.manager.EntityFactory;
import me.bestnuts.drive.api.manager.GroupFactory;
import me.bestnuts.drive.api.manager.VehicleFactory;
import me.bestnuts.drive.api.model.vehicle.VehicleRegistryType;
import me.bestnuts.drive.core.model.vehicle.VehicleCar;
import org.jetbrains.annotations.NotNull;

public final class CarFactory extends VehicleFactory {

    public CarFactory(EntityFactory entityFactory,
                      GroupFactory groupFactory,
                      CarConfigurationFactory configurationFactory) {
        super(entityFactory, groupFactory, configurationFactory, VehicleCar::new);
    }

    @Override
    public @NotNull String name() {
        return VehicleRegistryType.CAR.getName();
    }
}
