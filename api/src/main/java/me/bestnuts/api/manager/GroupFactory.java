package me.bestnuts.api.manager;

import me.bestnuts.api.model.vehicle.component.VehicleGroup;
import me.bestnuts.api.model.vehicle.configuration.GroupConfiguration;
import me.bestnuts.api.model.vehicle.configuration.GroupImplConfiguration;
import me.bestnuts.api.model.vehicle.configuration.VehicleConfiguration;
import me.bestnuts.api.model.vehicle.dto.EntityFactorySender;
import me.bestnuts.api.model.vehicle.dto.GroupFactorySender;
import me.bestnuts.api.model.vehicle.dto.GroupRestoreFactorySender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class GroupFactory implements Factory<GroupFactorySender, VehicleGroup>, RestoreFactory<GroupRestoreFactorySender, VehicleGroup> {

    private final BoneFactory boneFactory;

    public GroupFactory(BoneFactory boneFactory) {
        this.boneFactory = boneFactory;
    }

    @Override
    public @Nullable VehicleGroup generate(@NotNull GroupFactorySender sender) {
        VehicleConfiguration configuration = sender.configuration();
        ConfigurationSection section = configuration.getConfiguration().getConfigurationSection("group");
        if (section == null) return null;
        return getGroup(new GroupImplConfiguration(configuration, section), sender.sender());
    }

    public @NotNull VehicleGroup getGroup(@NotNull GroupImplConfiguration configuration, @NotNull EntityFactorySender sender) {
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

    private void buildTree(@NotNull VehicleGroup parentGroup, @NotNull GroupConfiguration parentConfig, @NotNull EntityFactorySender sender) {
        for (GroupConfiguration childConfig : parentConfig.children()) {
            ConfigurationSection boneSection = childConfig.boneSection();
            EntityFactorySender newSender = boneSection == null ? null : sender.withSection(boneSection);
            VehicleGroup childGroup = parentGroup.addChild(childConfig.groupName(), newSender, boneFactory);

            buildTree(childGroup, childConfig, sender);
        }
    }

    @Override
    public VehicleGroup regenerate(@NotNull GroupRestoreFactorySender sender) {
        VehicleConfiguration configuration = sender.configuration();
        ConfigurationSection section = configuration.getConfiguration().getConfigurationSection("group");
        if (section == null) return null;
        return getGroup(new GroupImplConfiguration(configuration, section), sender.entities());
    }

    public @NotNull VehicleGroup getGroup(@NotNull GroupImplConfiguration configuration, @NotNull List<Entity> entities) {
        VehicleGroup rootGroup;
        String groupName = configuration.groupName();
        if (configuration.boneSection() == null) {
            rootGroup = new VehicleGroup(null, groupName, entities, boneFactory);
        } else {
            rootGroup = new VehicleGroup(null, groupName, entities, boneFactory);
        }

        buildTree(rootGroup, configuration, entities);
        return rootGroup;
    }

    private void buildTree(@NotNull VehicleGroup parentGroup, @NotNull GroupConfiguration parentConfig, @NotNull List<Entity> entities) {
        for (GroupConfiguration childConfig : parentConfig.children()) {
            VehicleGroup childGroup = parentGroup.addChild(childConfig.groupName(), entities, boneFactory);

            buildTree(childGroup, childConfig, entities);
        }
    }
}
