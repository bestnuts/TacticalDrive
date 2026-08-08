package me.bestnuts.drive.core.repository;

import me.bestnuts.drive.api.bukkit.register.VehicleFactoryRegistry;
import me.bestnuts.drive.api.manager.GroupFactory;
import me.bestnuts.drive.api.manager.VehicleFactory;
import me.bestnuts.drive.core.manager.HeliConfigurationFactory;
import me.bestnuts.drive.core.manager.HeliFactory;
import me.bestnuts.drive.core.manager.SharedBoneFactory;
import me.bestnuts.drive.core.manager.SharedGroupFactory;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class HeliRepository {

    public HeliRepository(@NotNull SharedRepository shared, @NotNull VehicleFactoryRegistry factoryRegistry, @NotNull File dir) {
        GroupFactory groupFactory = new SharedGroupFactory(new SharedBoneFactory(shared.getEntityFactory(), shared.getFunctionFactory()));
        HeliConfigurationFactory configurationFactory = new HeliConfigurationFactory(dir);
        VehicleFactory heliFactory = new HeliFactory(shared.getEntityFactory(), groupFactory, configurationFactory);
        factoryRegistry.register(heliFactory);
    }
}
