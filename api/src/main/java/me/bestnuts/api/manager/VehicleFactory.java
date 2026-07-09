package me.bestnuts.api.manager;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.bestnuts.api.bukkit.util.DataKeyHelper;
import me.bestnuts.api.model.vehicle.Vehicle;
import me.bestnuts.api.model.vehicle.component.VehicleBone;
import me.bestnuts.api.model.vehicle.component.VehicleEntity;
import me.bestnuts.api.model.vehicle.component.VehicleGroup;
import me.bestnuts.api.model.vehicle.configuration.VehicleConfiguration;
import me.bestnuts.api.model.vehicle.dto.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

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
        Optional<FileConfiguration> optional = getConfigurationFactory().parameter(sender.name());
        if (optional.isPresent()) {
            FileConfiguration configuration = optional.get();
            VehicleConfiguration vehicleConfiguration = getConfigurationFactory().generate(configuration);
            EntityFactorySender entityFactorySender = new EntityFactorySender(sender.location(), configuration);
            VehicleGroup group = getGroupFactory().generate(new GroupFactorySender(vehicleConfiguration, entityFactorySender));
            if (group == null) return null;
            Entity entity = getEntityFactory().generate(entityFactorySender);
            VehicleEntity root = new VehicleEntity(entity);
            Vehicle vehicle = creator.create(root, group, vehicleConfiguration);
            applyDataKey(vehicle);
            return vehicle;
        }
        return null;
    }

    @Override
    public @Nullable Vehicle regenerate(@NotNull VehicleRestoreFactorySender sender) {
        Entity rootEntity = sender.root();
        String name = DataKeyHelper.get(rootEntity, DataKey.VEHICLE_ROOT_NAME, String.class);
        if (name == null) return null;
        Optional<FileConfiguration> optional = getConfigurationFactory().parameter(name);
        if (optional.isPresent()) {
            FileConfiguration configuration = optional.get();
            VehicleConfiguration vehicleConfiguration = getConfigurationFactory().generate(configuration);
            VehicleGroup group = getGroupFactory().regenerate(new GroupRestoreFactorySender(vehicleConfiguration, sender.entities()));
            if (group == null) return null;
            VehicleEntity root = new VehicleEntity(rootEntity);
            Vehicle vehicle = creator.create(root, group, vehicleConfiguration);
            applyDataKey(vehicle);
            return vehicle;
        }
        return null;
    }

    public @Nullable Vehicle regenerate(@NotNull Entity root) {
        String id = root.getUniqueId().toString();
        Predicate<Entity> filter = (nearBy) -> DataKeyHelper.getOrDefault(nearBy, DataKey.VEHICLE_ROOT_ID, String.class, "").equalsIgnoreCase(id);
        root.getChunk().load(false);
        List<Entity> entities = root.getWorld().getNearbyEntities(root.getLocation(), 16, 16, 16, filter).stream().toList();
        return regenerate(new VehicleRestoreFactorySender(root, entities));
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
        DataKeyHelper.set(entity, DataKey.VEHICLE_BONE_GROUP, path);
    }
}
