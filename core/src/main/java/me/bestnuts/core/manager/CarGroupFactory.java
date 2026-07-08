package me.bestnuts.core.manager;

import me.bestnuts.api.manager.BoneFactory;
import me.bestnuts.api.manager.GroupFactory;
import me.bestnuts.api.model.vehicle.component.VehicleGroup;
import me.bestnuts.api.model.vehicle.configuration.GroupImplConfiguration;
import me.bestnuts.api.model.vehicle.configuration.VehicleConfiguration;
import me.bestnuts.api.model.vehicle.dto.GroupFactorySender;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CarGroupFactory extends GroupFactory {

    public CarGroupFactory(BoneFactory boneFactory) {
        super(boneFactory);
    }

    @Override
    public @Nullable VehicleGroup generate(@NotNull GroupFactorySender sender) {
        VehicleConfiguration configuration = sender.configuration();
        ConfigurationSection section = configuration.getConfiguration().getConfigurationSection("group");
        if (section == null) return null;
        return getGroup(new GroupImplConfiguration(configuration, section), sender.sender());
    }
}
