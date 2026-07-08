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
    @Nullable
    private final ConfigurationSection section;

    public GroupImplConfiguration(@NotNull VehicleConfiguration parent, @NotNull ConfigurationSection section) {
        this.parent = parent;
        this.children = new ArrayList<>();
        this.section = section.getConfigurationSection("bones");
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
    public @Nullable ConfigurationSection section() {
        return section;
    }

    @Override
    public @NotNull VehicleGroup create(@NotNull EntityFactorySender sender) {
        VehicleGroup rootGroup;
        if (section == null) {
            rootGroup = VehicleGroup.createRoot(null, parent.getBone());
        } else {
            rootGroup = VehicleGroup.createRoot(sender.withSection(section), parent.getBone());
        }

        buildTree(rootGroup, this, sender);
        return rootGroup;
    }

    private void buildTree(@NotNull VehicleGroup parentGroup, @NotNull GroupConfiguration parentConfig, @NotNull EntityFactorySender sender) {
        for (GroupConfiguration childConfig : parentConfig.children()) {
            EntityFactorySender newSender = childConfig.section() == null ? null : sender.withSection(childConfig.section());
            VehicleGroup childGroup = parentGroup.addChild(newSender, parent.getBone());

            buildTree(childGroup, childConfig, sender);
        }
    }

    @Override
    public @NotNull String name() {
        return parent.name();
    }
}
