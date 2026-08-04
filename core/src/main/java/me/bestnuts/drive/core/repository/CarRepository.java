package me.bestnuts.drive.core.repository;

import me.bestnuts.drive.api.bukkit.register.VehicleFactoryRegistry;
import me.bestnuts.drive.api.manager.GroupFactory;
import me.bestnuts.drive.api.manager.VehicleFactory;
import me.bestnuts.drive.core.manager.CarBoneFactory;
import me.bestnuts.drive.core.manager.CarConfigurationFactory;
import me.bestnuts.drive.core.manager.CarFactory;
import me.bestnuts.drive.core.manager.SharedGroupFactory;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class CarRepository {

    public CarRepository(@NotNull SharedRepository shared, @NotNull VehicleFactoryRegistry factoryRegistry, @NotNull File dir) {
        GroupFactory groupFactory = new SharedGroupFactory(new CarBoneFactory(shared.getEntityFactory(), shared.getFunctionFactory()));
        CarConfigurationFactory configurationFactory = new CarConfigurationFactory(dir);
        VehicleFactory carFactory = new CarFactory(shared.getEntityFactory(), groupFactory, configurationFactory);
        factoryRegistry.register(carFactory);
    }
}
