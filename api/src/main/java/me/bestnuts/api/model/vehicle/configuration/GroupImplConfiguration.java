package me.bestnuts.api.model.vehicle.configuration;

import me.bestnuts.api.model.vehicle.component.VehicleGroup;
import me.bestnuts.api.model.vehicle.dto.EntityFactorySender;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class GroupImplConfiguration implements GroupConfiguration {

    private static final Predicate<String> bonesFilter = (key) -> key.equals("bones");

    private final VehicleConfiguration parent;
    private final List<GroupConfiguration> children;

    public GroupImplConfiguration(@NotNull VehicleConfiguration parent, @NotNull ConfigurationSection section) {
        this.parent = parent;
        this.children = new ArrayList<>();
        for (String key : section.getKeys(false)) {
            if (bonesFilter.test(key)) continue;
            ConfigurationSection groupSection = section.getConfigurationSection(key);
            if (groupSection == null) continue;
            addChild(new GroupImplConfiguration(parent, section));
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
    public @NotNull VehicleGroup create(@NotNull EntityFactorySender sender) {
        VehicleGroup rootGroup = VehicleGroup.createRoot(sender, parent.getBone());

        buildTree(rootGroup, this, sender);
        return rootGroup;
    }

    private void buildTree(@NotNull VehicleGroup parentGroup, @NotNull GroupConfiguration parentConfig, @NotNull EntityFactorySender sender) {
        for (GroupConfiguration childConfig : parentConfig.children()) {
            VehicleGroup childGroup = parentGroup.addChild(sender, parent.getBone());

            buildTree(childGroup, childConfig, sender);
        }
    }

    @Override
    public @NotNull String name() {
        return parent.name();
    }
}
