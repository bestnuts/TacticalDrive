package me.bestnuts.core.repository;

import lombok.Getter;
import me.bestnuts.core.manager.CarBoneFactory;
import me.bestnuts.core.manager.CarConfigurationFactory;
import me.bestnuts.core.manager.CarFactory;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@Getter
public final class CarRepository {

    private final SharedRepository shared;
    private final CarFactory carFactory;

    public CarRepository(SharedRepository shared, @NotNull File dir) {
        this.shared = shared;
        CarBoneFactory boneFactory = new CarBoneFactory();
        CarConfigurationFactory configurationFactory = new CarConfigurationFactory(shared.getEntityFactory(), dir);
        carFactory = new CarFactory(shared.getEntityFactory(), boneFactory, configurationFactory);
    }
}
