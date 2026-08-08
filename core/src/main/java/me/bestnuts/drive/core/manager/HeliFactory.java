package me.bestnuts.drive.core.manager;

import me.bestnuts.drive.api.manager.EntityFactory;
import me.bestnuts.drive.api.manager.GroupFactory;
import me.bestnuts.drive.api.manager.VehicleFactory;
import me.bestnuts.drive.api.model.vehicle.VehicleRegistryType;
import me.bestnuts.drive.core.model.vehicle.VehicleHeli;
import org.jetbrains.annotations.NotNull;

public final class HeliFactory extends VehicleFactory {

    public HeliFactory(EntityFactory entityFactory,
                       GroupFactory groupFactory,
                       HeliConfigurationFactory configurationFactory) {
        super(entityFactory, groupFactory, configurationFactory, VehicleHeli::new);
    }

    @Override
    public @NotNull String name() {
        return VehicleRegistryType.HELI.getName();
    }
}
