package me.bestnuts.drive.core.manager;

import me.bestnuts.drive.api.manager.BoneFactory;
import me.bestnuts.drive.api.manager.GroupFactory;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleGroup;
import me.bestnuts.drive.api.model.vehicle.configuration.GroupConfiguration;
import me.bestnuts.drive.api.model.vehicle.configuration.GroupImplConfiguration;
import me.bestnuts.drive.api.model.vehicle.configuration.VehicleConfiguration;
import me.bestnuts.drive.api.model.vehicle.data.EntityFactorySender;
import me.bestnuts.drive.api.model.vehicle.data.GroupFactorySender;
import me.bestnuts.drive.api.model.vehicle.data.GroupRestoreFactorySender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class SharedGroupFactory implements GroupFactory {

    private final BoneFactory boneFactory;

    public SharedGroupFactory(@NotNull BoneFactory boneFactory) {
        this.boneFactory = boneFactory;
    }

    @Override
    public @Nullable VehicleGroup generate(@NotNull GroupFactorySender sender) {
        VehicleConfiguration configuration = sender.configuration();
        ConfigurationSection section = configuration.getConfiguration().getConfigurationSection("group");
        if (section == null) return null;
        return getGroup(new GroupImplConfiguration(configuration, section), sender.sender());
    }

    @Override
    public @Nullable VehicleGroup regenerate(@NotNull GroupRestoreFactorySender sender) {
        VehicleConfiguration configuration = sender.configuration();
        ConfigurationSection section = configuration.getConfiguration().getConfigurationSection("group");
        if (section == null) return null;
        return getGroup(new GroupImplConfiguration(configuration, section), sender.entities());
    }

    private @NotNull VehicleGroup getGroup(@NotNull GroupImplConfiguration configuration, @NotNull EntityFactorySender sender) {
        VehicleGroup rootGroup;
        String groupName = configuration.groupName();
        if (configuration.boneSection() == null) {
            rootGroup = new VehicleGroup(null, groupName, sender, boneFactory);
        } else {
            rootGroup = new VehicleGroup(null, groupName, sender.withSection(configuration.boneSection()), boneFactory);
        }

        buildTree(rootGroup, configuration, sender);
        return rootGroup;
    }

    private @Nullable VehicleGroup getGroup(@NotNull GroupImplConfiguration configuration, @NotNull List<Entity> entities) {
        String groupName = configuration.groupName();
        if (configuration.boneSection() == null) return null;
        VehicleGroup rootGroup = new VehicleGroup(null, groupName, configuration.boneSection(), entities, boneFactory);

        buildTree(rootGroup, configuration, entities);
        return rootGroup;
    }

    private void buildTree(@NotNull VehicleGroup parentGroup, @NotNull GroupConfiguration parentConfig, @NotNull EntityFactorySender sender) {
        for (GroupConfiguration childConfig : parentConfig.children()) {
            ConfigurationSection boneSection = childConfig.boneSection();
            EntityFactorySender newSender = boneSection == null ? null : sender.withSection(boneSection);
            VehicleGroup childGroup = parentGroup.addChild(childConfig.groupName(), newSender, boneFactory);

            buildTree(childGroup, childConfig, sender);
        }
    }

    private void buildTree(@NotNull VehicleGroup parentGroup, @NotNull GroupConfiguration parentConfig, @NotNull List<Entity> entities) {
        for (GroupConfiguration childConfig : parentConfig.children()) {
            ConfigurationSection boneSection = childConfig.boneSection();
            if (boneSection == null) continue;
            VehicleGroup childGroup = parentGroup.addChild(childConfig.groupName(), boneSection, entities, boneFactory);

            buildTree(childGroup, childConfig, entities);
        }
    }
}
