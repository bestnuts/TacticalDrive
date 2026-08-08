package me.bestnuts.drive.core.manager;

import me.bestnuts.drive.api.bukkit.util.DataKeyHelper;
import me.bestnuts.drive.api.manager.*;
import me.bestnuts.drive.api.model.vehicle.component.bone.*;
import me.bestnuts.drive.api.model.vehicle.data.*;
import me.bestnuts.drive.core.model.vehicle.component.bone.ModelEntity;
import me.bestnuts.drive.core.model.vehicle.component.bone.SeatEntity;
import me.bestnuts.drive.core.model.vehicle.component.bone.WheelEntity;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class SharedBoneFactory extends BoneFactory {

    private static final Map<String, BoneCreator> function = BoneCreatorHelper.functionMapping(Map.of(
            VehicleModel.class, ModelEntity::new,
            VehicleWheel.class, WheelEntity::new,
            VehicleSeat.class, SeatEntity::new
    ));

    public SharedBoneFactory(@NotNull EntityFactory entityFactory, @NotNull FunctionFactory functionFactory) {
        super(entityFactory, functionFactory);
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
            ConfigurationSection tickSection = boneSection.getConfigurationSection("tick");
            ConfigurationSection initSection = boneSection.getConfigurationSection("init");
            BoneData boneData = new BoneData(group,
                    (vehicleEntity) -> getFunctionFactory().generate(new FunctionFactorySender(vehicleEntity, tickSection)),
                    (vehicleEntity) -> getFunctionFactory().generate(new FunctionFactorySender(vehicleEntity, initSection)),
                    boneType, key);
            VehicleBone bone = creator.create(getEntityFactory().generate(entityFactorySender), boneData);
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
            ConfigurationSection boneSection = sender.section().getConfigurationSection(name);
            if (boneSection == null) continue;
            ConfigurationSection tickSection = boneSection.getConfigurationSection("tick");
            ConfigurationSection initSection = boneSection.getConfigurationSection("init");
            BoneData boneData = new BoneData(group,
                    (vehicleEntity) -> getFunctionFactory().generate(new FunctionFactorySender(vehicleEntity, tickSection)),
                    (vehicleEntity) -> getFunctionFactory().generate(new FunctionFactorySender(vehicleEntity, initSection)), boneType, name);
            VehicleBone bone = creator.create(entity, boneData);
            bones.put(name, bone);
            iterator.remove();
        }
        return bones;
    }
}
