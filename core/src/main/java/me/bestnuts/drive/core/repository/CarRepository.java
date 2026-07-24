package me.bestnuts.drive.core.repository;

import lombok.Getter;
import me.bestnuts.drive.api.bukkit.register.VehicleFactoryHook;
import me.bestnuts.drive.api.manager.GroupFactory;
import me.bestnuts.drive.api.manager.VehicleFactory;
import me.bestnuts.drive.core.manager.CarBoneFactory;
import me.bestnuts.drive.core.manager.CarConfigurationFactory;
import me.bestnuts.drive.core.manager.CarFactory;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@Getter
public final class CarRepository {

    private final VehicleFactory carFactory;

    public CarRepository(SharedRepository shared, @NotNull File dir) {
        GroupFactory groupFactory = new GroupFactory(new CarBoneFactory(shared.getEntityFactory(), shared.getFunctionFactory()));
        CarConfigurationFactory configurationFactory = new CarConfigurationFactory(shared.getEntityFactory(), dir);
        carFactory = new CarFactory(shared.getEntityFactory(), groupFactory, configurationFactory);
        VehicleFactoryHook.registerHooks(carFactory);
    }
}
