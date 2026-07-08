package me.bestnuts.core.model.vehicle.configuration;

import me.bestnuts.api.manager.BoneCreator;
import me.bestnuts.api.manager.BoneCreatorHelper;
import me.bestnuts.api.manager.EntityFactory;
import me.bestnuts.api.model.vehicle.component.VehicleBone;
import me.bestnuts.api.model.vehicle.component.VehicleGroup;
import me.bestnuts.api.model.vehicle.component.VehicleModel;
import me.bestnuts.api.model.vehicle.component.VehicleWheel;
import me.bestnuts.api.model.vehicle.configuration.BoneAbstractConfiguration;
import me.bestnuts.api.model.vehicle.configuration.VehicleConfiguration;
import me.bestnuts.api.model.vehicle.dto.EntityFactorySender;
import me.bestnuts.core.model.vehicle.component.ModelEntity;
import me.bestnuts.core.model.vehicle.component.WheelEntity;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CarBoneConfiguration extends BoneAbstractConfiguration {

    private static final Map<String, BoneCreator> function = BoneCreatorHelper.functionMapping(Map.of(
            VehicleModel.class, ModelEntity::new,
            VehicleWheel.class, WheelEntity::new
    ));

    public CarBoneConfiguration(@NotNull EntityFactory entityFactory, @NotNull VehicleConfiguration parent) {
        super(entityFactory, parent);
    }

    @Override
    public @NotNull List<VehicleBone> create(@NotNull VehicleGroup group, @NotNull EntityFactorySender sender) {
        List<VehicleBone> bones = new ArrayList<>();
        ConfigurationSection section = sender.section();
        for (String key : section.getKeys(false)) {
            ConfigurationSection boneSection = section.getConfigurationSection(key);
            if (boneSection == null) continue;
            BoneCreator creator = function.get(boneSection.getString("type"));
            if (creator == null) continue;
            EntityFactorySender entityFactorySender = sender.withSection(boneSection);
            creator.create(group, getEntityFactory().generate(entityFactorySender));
        }
        return bones;
    }
}
