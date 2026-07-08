package me.bestnuts.api.manager;

import me.bestnuts.api.model.vehicle.component.VehicleGroup;
import me.bestnuts.api.model.vehicle.configuration.GroupConfiguration;
import me.bestnuts.api.model.vehicle.configuration.GroupImplConfiguration;
import me.bestnuts.api.model.vehicle.configuration.VehicleConfiguration;
import me.bestnuts.api.model.vehicle.dto.EntityFactorySender;
import me.bestnuts.api.model.vehicle.dto.GroupFactorySender;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class GroupFactory implements Factory<GroupFactorySender, VehicleGroup> {

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
            rootGroup = VehicleGroup.createRoot(groupName, null, boneFactory);
        } else {
            rootGroup = VehicleGroup.createRoot(groupName, sender.withSection(configuration.boneSection()), boneFactory);
        }

        buildTree(rootGroup, configuration, sender);
        return rootGroup;
    }

    private void buildTree(@NotNull VehicleGroup parentGroup, @NotNull GroupConfiguration parentConfig, @NotNull EntityFactorySender sender) {
        for (GroupConfiguration childConfig : parentConfig.children()) {
            EntityFactorySender newSender = childConfig.boneSection() == null ? null : sender.withSection(childConfig.boneSection());
            VehicleGroup childGroup = parentGroup.addChild(childConfig.groupName(), newSender, boneFactory);

            buildTree(childGroup, childConfig, sender);
        }
    }
}
