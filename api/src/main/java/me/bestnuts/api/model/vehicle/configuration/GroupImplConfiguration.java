package me.bestnuts.api.model.vehicle.configuration;

import me.bestnuts.api.model.vehicle.component.VehicleGroup;
import me.bestnuts.api.model.vehicle.dto.EntityFactorySender;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class GroupImplConfiguration implements GroupConfiguration {

    private static final Predicate<String> bonesFilter = (key) -> key.equals("bones");

    private final VehicleConfiguration parent;
    private final List<GroupConfiguration> children;
    private final String groupName;
    @Nullable
    private final ConfigurationSection boneSection;

    public GroupImplConfiguration(@NotNull VehicleConfiguration parent, @NotNull ConfigurationSection section) {
        this.parent = parent;
        this.children = new ArrayList<>();
        this.groupName = section.getName();
        this.boneSection = section.getConfigurationSection("bones");
        for (String key : section.getKeys(false)) {
            if (bonesFilter.test(key)) continue;
            ConfigurationSection groupSection = section.getConfigurationSection(key);
            if (groupSection == null) continue;
            addChild(new GroupImplConfiguration(parent, groupSection));
        }
    }

    public void addChild(@NotNull GroupConfiguration child) {
        this.children.add(child);
    }

    @Override
    public @NotNull List<GroupConfiguration> children() {
        return children;
    }

    @Override
    public @NotNull String groupName() {
        return groupName;
    }

    @Override
    public @Nullable ConfigurationSection boneSection() {
        return boneSection;
    }

    @Override
    public @NotNull VehicleGroup create(@NotNull EntityFactorySender sender) {
        VehicleGroup rootGroup;
        if (boneSection == null) {
            rootGroup = VehicleGroup.createRoot(groupName, null, parent.getBone());
        } else {
            rootGroup = VehicleGroup.createRoot(groupName, sender.withSection(boneSection), parent.getBone());
        }

        buildTree(rootGroup, this, sender);
        return rootGroup;
    }

    private void buildTree(@NotNull VehicleGroup parentGroup, @NotNull GroupConfiguration parentConfig, @NotNull EntityFactorySender sender) {
        for (GroupConfiguration childConfig : parentConfig.children()) {
            EntityFactorySender newSender = childConfig.boneSection() == null ? null : sender.withSection(childConfig.boneSection());
            VehicleGroup childGroup = parentGroup.addChild(childConfig.groupName(), newSender, parent.getBone());

            buildTree(childGroup, childConfig, sender);
        }
    }

    @Override
    public @NotNull String name() {
        return parent.name();
    }
}
