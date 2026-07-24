package me.bestnuts.drive.api.manager;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.bestnuts.drive.api.bukkit.util.DataKeyHelper;
import me.bestnuts.drive.api.model.vehicle.Vehicle;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleBone;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleGroup;
import me.bestnuts.drive.api.model.vehicle.configuration.VehicleConfiguration;
import me.bestnuts.api.model.vehicle.data.*;
import me.bestnuts.drive.api.model.vehicle.data.*;
import me.bestnuts.tacticaldrive.api.model.vehicle.data.*;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Slf4j
@Getter
@RequiredArgsConstructor
public abstract class VehicleFactory implements Factory<VehicleFactorySender, Vehicle>, RestoreFactory<VehicleRestoreFactorySender, Vehicle> {

    private final EntityFactory entityFactory;
    private final GroupFactory groupFactory;
    private final VehicleConfigurationFactory configurationFactory;
    private final VehicleCreator creator;

    public abstract @NotNull String name();

    @Override
    public @Nullable Vehicle generate(@NotNull VehicleFactorySender sender) {
        VehicleConfiguration configuration = getConfigurationFactory().generate(sender.name());
        if (configuration == null) return null;
        VehicleConfiguration vehicleConfiguration = getConfigurationFactory().generate(sender.name());
        EntityFactorySender entityFactorySender = new EntityFactorySender(sender.location(), configuration.getConfiguration());
        VehicleGroup group = getGroupFactory().generate(new GroupFactorySender(vehicleConfiguration, entityFactorySender));
        if (group == null) return null;
        Entity entity = getEntityFactory().generate(entityFactorySender);
        VehicleEntity root = new VehicleEntity(entity);
        Vehicle vehicle = creator.create(root, group, configuration);
        applyDataKey(vehicle);
        return vehicle;
    }

    @Override
    public @Nullable Vehicle regenerate(@NotNull VehicleRestoreFactorySender sender) {
        Entity rootEntity = sender.root();
        String name = DataKeyHelper.get(rootEntity, DataKey.VEHICLE_ROOT_NAME, String.class);
        if (name == null) return null;
        VehicleConfiguration configuration = getConfigurationFactory().generate(name);
        if (configuration == null) return null;
        VehicleGroup group = getGroupFactory().regenerate(new GroupRestoreFactorySender(configuration, sender.entities()));
        if (group == null) return null;
        VehicleEntity root = new VehicleEntity(rootEntity);
        Vehicle vehicle = creator.create(root, group, configuration);
        applyDataKey(vehicle);
        return vehicle;
    }

    public @Nullable Vehicle regenerate(@NotNull Entity root) {
        String id = root.getUniqueId().toString();
        Predicate<Entity> filter = (nearBy) -> DataKeyHelper.getOrDefault(nearBy, DataKey.VEHICLE_ROOT_ID, String.class, "").equalsIgnoreCase(id);
        root.getChunk().load(false);
        List<Entity> entities = new ArrayList<>(root.getWorld().getNearbyEntities(root.getLocation(), 16, 16, 16, filter).stream().toList());
        Vehicle vehicle = regenerate(new VehicleRestoreFactorySender(root, entities));
        for (Entity entity : entities) {
            sendRegenerateOmitEntityLog(entity);
            entity.remove();
        }
        return vehicle;
    }


    private void applyDataKey(Vehicle vehicle) {
        String id = vehicle.entity().getUniqueId().toString();
        DataKeyHelper.set(vehicle.entity().getEntity(), DataKey.VEHICLE_ROOT_TYPE, name());
        DataKeyHelper.set(vehicle.entity().getEntity(), DataKey.VEHICLE_ROOT_NAME, vehicle.configuration().getName());
        vehicle.group().consumerTransition(
                group -> group.bones().forEach(bone -> applyDataKey(bone, id, group.getAbsolutePath()))
        );
    }

    private void applyDataKey(VehicleBone bone, String rootId, String path) {
        Entity entity = bone.getEntity();
        DataKeyHelper.set(entity, DataKey.VEHICLE_ROOT_ID, rootId);
        DataKeyHelper.set(entity, DataKey.VEHICLE_BONE_TYPE, bone.getType());
        DataKeyHelper.set(entity, DataKey.VEHICLE_BONE_NAME, bone.getName());
        DataKeyHelper.set(entity, DataKey.VEHICLE_BONE_GROUP, path);
    }

    private void sendRegenerateOmitEntityLog(Entity entity) {
        String rootId = DataKeyHelper.getOrDefault(entity, DataKey.VEHICLE_ROOT_ID, String.class, "none");
        String boneType = DataKeyHelper.getOrDefault(entity, DataKey.VEHICLE_BONE_TYPE, String.class, "none");
        String boneName = DataKeyHelper.getOrDefault(entity, DataKey.VEHICLE_BONE_NAME, String.class, "none");
        String boneGroup = DataKeyHelper.getOrDefault(entity, DataKey.VEHICLE_BONE_GROUP, String.class, "none");
        log.warn("해당 본이 재생성 과정에서 누락되었습니다. rootId : {}, boneType : {}, boneName : {}, boneGroup : {}, originId : {}", rootId, boneType, boneName, boneGroup, entity.getUniqueId());
    }
}
