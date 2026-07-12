package me.bestnuts.core.manager;

import me.bestnuts.api.bukkit.util.DataKeyHelper;
import me.bestnuts.api.manager.BoneCreator;
import me.bestnuts.api.manager.BoneCreatorHelper;
import me.bestnuts.api.manager.BoneFactory;
import me.bestnuts.api.manager.EntityFactory;
import me.bestnuts.api.model.vehicle.component.*;
import me.bestnuts.api.model.vehicle.data.*;
import me.bestnuts.core.model.vehicle.component.ModelEntity;
import me.bestnuts.core.model.vehicle.component.SeatEntity;
import me.bestnuts.core.model.vehicle.component.WheelEntity;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class CarBoneFactory extends BoneFactory {

    private static final Map<String, BoneCreator> function = BoneCreatorHelper.functionMapping(Map.of(
            VehicleModel.class, ModelEntity::new,
            VehicleWheel.class, WheelEntity::new,
            VehicleSeat.class, SeatEntity::new
    ));

    public CarBoneFactory(EntityFactory entityFactory) {
        super(entityFactory);
    }

    @Override
    public @NotNull Map<String, VehicleBone> generate(@NotNull BoneFactorySender boneFactorySender) {
        Map<String, VehicleBone> bones = new HashMap<>();
        EntityFactorySender sender = boneFactorySender.sender();
        ConfigurationSection section = sender.section();
        VehicleGroup group = boneFactorySender.group();
        for (String key : section.getKeys(false)) {
            ConfigurationSection boneSection = section.getConfigurationSection(key);
            if (boneSection == null) continue;
            String boneType = boneSection.getString("type");
            if (boneType == null || boneType.isEmpty()) continue;
            BoneCreator creator = function.get(boneType);
            if (creator == null) continue;
            EntityFactorySender entityFactorySender = sender.withSection(boneSection);
            VehicleBone bone = creator.create(getEntityFactory().generate(entityFactorySender), new BoneData(group, boneType, key));
            bones.put(key, bone);
        }
        return bones;
    }

    @Override
    public @NotNull Map<String, VehicleBone> regenerate(@NotNull BoneRestoreFactorySender sender) {
        Map<String, VehicleBone> bones = new HashMap<>();
        Iterator<Entity> iterator = sender.entities().iterator();
        VehicleGroup group = sender.group();
        String groupPath = group.getAbsolutePath();
        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            String path = DataKeyHelper.getOrDefault(entity, DataKey.VEHICLE_BONE_GROUP, String.class, "");
            if (!path.equals(groupPath)) continue;
            String boneType = DataKeyHelper.get(entity, DataKey.VEHICLE_BONE_TYPE, String.class);
            if (boneType == null || boneType.isEmpty()) continue;
            BoneCreator creator = function.get(boneType);
            if (creator == null) continue;
            String name = DataKeyHelper.getOrDefault(entity, DataKey.VEHICLE_BONE_NAME, String.class, "");
            VehicleBone bone = creator.create(entity, new BoneData(group, boneType, name));
            bones.put(name, bone);
            iterator.remove();
        }
        return bones;
    }
}
