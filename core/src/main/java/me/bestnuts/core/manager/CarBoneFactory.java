package me.bestnuts.core.manager;

import me.bestnuts.api.manager.BoneFactory;
import me.bestnuts.api.model.vehicle.configuration.GroupImplConfiguration;
import me.bestnuts.api.model.vehicle.configuration.VehicleConfiguration;
import org.jetbrains.annotations.NotNull;

public class CarBoneFactory implements BoneFactory {

    @Override
    public @NotNull GroupImplConfiguration generate(@NotNull VehicleConfiguration configuration) {
        return new GroupImplConfiguration(configuration, configuration.getConfiguration(), null);
    }
}
