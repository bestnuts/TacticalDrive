package me.bestnuts.core.repository;

import lombok.Getter;
import me.bestnuts.api.bukkit.register.VehicleFactoryHook;
import me.bestnuts.core.manager.CarBoneFactory;
import me.bestnuts.core.manager.CarConfigurationFactory;
import me.bestnuts.core.manager.CarFactory;
import me.bestnuts.core.manager.CarGroupFactory;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@Getter
public final class CarRepository {

    private final CarFactory carFactory;

    public CarRepository(SharedRepository shared, @NotNull File dir) {
        CarGroupFactory groupFactory = new CarGroupFactory(new CarBoneFactory(shared.getEntityFactory()));
        CarConfigurationFactory configurationFactory = new CarConfigurationFactory(shared.getEntityFactory(), dir);
        carFactory = new CarFactory(shared.getEntityFactory(), groupFactory, configurationFactory);
        VehicleFactoryHook.registerHooks(carFactory);
    }
}
