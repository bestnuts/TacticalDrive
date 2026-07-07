package me.bestnuts.api.model.vehicle.configuration;

import me.bestnuts.api.model.vehicle.component.VehicleGroup;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class GroupImplConfiguration implements SharedConfiguration, GroupConfiguration {

    private final VehicleConfiguration parent;
    private final BoneConfiguration boneConfiguration;
    private final List<GroupConfiguration> children;

    public GroupImplConfiguration(@NotNull VehicleConfiguration parent, @NotNull ConfigurationSection section, @Nullable BoneConfiguration boneConfiguration) {
        this.parent = parent;
        this.boneConfiguration = boneConfiguration;
        this.children = new ArrayList<>();
    }

    public @NotNull GroupImplConfiguration addChild(@NotNull GroupConfiguration child) {
        this.children.add(child);
        return this;
    }

    @Override
    public @Nullable BoneConfiguration boneConfiguration() {
        return boneConfiguration;
    }

    @Override
    public @NotNull List<GroupConfiguration> children() {
        return children;
    }

    @Override
    public @NotNull VehicleGroup create() {
        VehicleGroup rootGroup = VehicleGroup.createRoot(boneConfiguration);

        buildTree(rootGroup, this);
        return rootGroup;
    }

    private void buildTree(@NotNull VehicleGroup parentGroup, @NotNull GroupConfiguration parentConfig) {
        for (GroupConfiguration childConfig : parentConfig.children()) {
            VehicleGroup childGroup = parentGroup.addChild(childConfig.boneConfiguration());

            buildTree(childGroup, childConfig);
        }
    }

    @Override
    public @NotNull String name() {
        return parent.name();
    }
}
