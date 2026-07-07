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
        CarConfigurationFactory configurationFactory = new CarConfigurationFactory(dir);
        CarBoneFactory boneFactory = new CarBoneFactory();
        carFactory = new CarFactory(shared.getEntityFactory(), configurationFactory, boneFactory);
    }
}
