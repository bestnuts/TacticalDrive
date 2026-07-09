package me.bestnuts.core.manager;

import me.bestnuts.api.manager.EntityFactory;
import me.bestnuts.api.manager.GroupFactory;
import me.bestnuts.api.manager.VehicleFactory;
import me.bestnuts.core.model.vehicle.VehicleCar;
import org.jetbrains.annotations.NotNull;

public class CarFactory extends VehicleFactory {

    public CarFactory(EntityFactory entityFactory,
                      GroupFactory groupFactory,
                      CarConfigurationFactory configurationFactory) {
        super(entityFactory, groupFactory, configurationFactory, VehicleCar::new);
    }

    @Override
    public @NotNull String name() {
        return "car";
    }
}
