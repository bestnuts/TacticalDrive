package me.bestnuts.core.manager;

import me.bestnuts.api.manager.BoneFactory;
import me.bestnuts.api.model.vehicle.configuration.GroupImplConfiguration;
import me.bestnuts.api.model.vehicle.configuration.VehicleConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CarBoneFactory implements BoneFactory {

    @Override
    public @Nullable GroupImplConfiguration generate(@NotNull VehicleConfiguration configuration) {
        ConfigurationSection section = configuration.getConfiguration().getConfigurationSection("group");
        if (section == null) return null;
        return new GroupImplConfiguration(configuration, section);
    }
}
