package me.bestnuts.core.manager;

import me.bestnuts.api.manager.BoneCreator;
import me.bestnuts.api.manager.BoneCreatorHelper;
import me.bestnuts.api.manager.BoneFactory;
import me.bestnuts.api.manager.EntityFactory;
import me.bestnuts.api.model.vehicle.component.*;
import me.bestnuts.api.model.vehicle.dto.BoneFactorySender;
import me.bestnuts.api.model.vehicle.dto.EntityFactorySender;
import me.bestnuts.core.model.vehicle.component.ModelEntity;
import me.bestnuts.core.model.vehicle.component.SeatEntity;
import me.bestnuts.core.model.vehicle.component.WheelEntity;
import org.bukkit.configuration.ConfigurationSection;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CarBoneFactory extends BoneFactory {

    private static final Map<String, BoneCreator> function = BoneCreatorHelper.functionMapping(Map.of(
            VehicleModel.class, ModelEntity::new,
            VehicleWheel.class, WheelEntity::new,
            VehicleSeat.class, SeatEntity::new
    ));

    public CarBoneFactory(EntityFactory entityFactory) {
        super(entityFactory);
    }

    @Override
    public List<VehicleBone> generate(@NonNull BoneFactorySender boneFactorySender) {
        List<VehicleBone> bones = new ArrayList<>();
        EntityFactorySender sender = boneFactorySender.sender();
        ConfigurationSection section = sender.section();
        VehicleGroup group = boneFactorySender.group();
        for (String key : section.getKeys(false)) {
            ConfigurationSection boneSection = section.getConfigurationSection(key);
            if (boneSection == null) continue;
            String boneType = boneSection.getString("type");
            BoneCreator creator = function.get(boneType);
            if (creator == null) continue;
            EntityFactorySender entityFactorySender = sender.withSection(boneSection);
            VehicleBone bone = creator.create(group, getEntityFactory().generate(entityFactorySender), boneType);
            bones.add(bone);
        }
        return bones;
    }
}
