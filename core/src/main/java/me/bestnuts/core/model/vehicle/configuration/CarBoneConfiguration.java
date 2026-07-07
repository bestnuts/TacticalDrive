package me.bestnuts.core.model.vehicle.configuration;

import me.bestnuts.api.manager.BoneCreator;
import me.bestnuts.api.manager.BoneCreatorHelper;
import me.bestnuts.api.model.vehicle.component.VehicleBone;
import me.bestnuts.api.model.vehicle.component.VehicleGroup;
import me.bestnuts.api.model.vehicle.component.VehicleWheel;
import me.bestnuts.api.model.vehicle.configuration.BoneAbstractConfiguration;
import me.bestnuts.api.model.vehicle.configuration.VehicleConfiguration;
import me.bestnuts.api.model.vehicle.dto.EntityFactorySender;
import me.bestnuts.core.model.vehicle.component.WheelEntity;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CarBoneConfiguration extends BoneAbstractConfiguration {

    private static final Map<String, BoneCreator> function = BoneCreatorHelper.functionMapping(Map.of(
            VehicleWheel.class, WheelEntity::new
    ));

    public CarBoneConfiguration(@NotNull VehicleConfiguration parent) {
        super(parent.getBone().getEntityFactory(), parent);
    }

    @Override
    public @NotNull List<VehicleBone> create(@NotNull VehicleGroup group, @NotNull EntityFactorySender sender) {
        List<VehicleBone> bones = new ArrayList<>();
        ConfigurationSection section = sender.section();
        for (String key : section.getKeys(false)) {
            ConfigurationSection boneSection = section.getConfigurationSection(key);
            if (boneSection == null) continue;
            BoneCreator creator = function.get(boneSection.getString("type"));
            EntityFactorySender entityFactorySender = sender.withSection(boneSection.getConfigurationSection(key));
            if (creator == null) continue;
            creator.create(group, getEntityFactory().generate(entityFactorySender));
        }
        return bones;
    }
}
