package me.bestnuts.api.model.vehicle;

import me.bestnuts.api.model.vehicle.component.bone.VehicleBone;
import me.bestnuts.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.api.model.vehicle.component.bone.VehicleGroup;
import me.bestnuts.api.model.vehicle.configuration.VehicleConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public abstract class Vehicle {

    private final VehicleEntity entity;
    private final VehicleGroup group;
    private final Map<UUID, VehicleBone> byIdBoneMap;
    private final VehicleConfiguration configuration;

    public Vehicle(@NotNull VehicleEntity entity, @NotNull VehicleGroup group, @NotNull VehicleConfiguration configuration) {
        this.entity = entity;
        this.group = group;
        Map<UUID, VehicleBone> tempMap = new HashMap<>();
        collectBones(group, tempMap);
        this.byIdBoneMap = Map.copyOf(tempMap);
        this.configuration = configuration;
    }

    private void collectBones(@NotNull VehicleGroup targetGroup, @NotNull Map<UUID, VehicleBone> map) {
        for (VehicleBone bone : targetGroup.bones()) {
            map.put(bone.getUniqueId(), bone);
        }

        for (VehicleGroup child : targetGroup.children()) {
            collectBones(child, map);
        }
    }

    public @NotNull VehicleEntity entity() {
        return entity;
    }

    public @NotNull VehicleGroup group() {
        return group;
    }

    public @NotNull Optional<VehicleBone> findBoneById(@NotNull UUID id) {
        return Optional.ofNullable(byIdBoneMap.get(id));
    }

    public @NotNull VehicleConfiguration configuration() {
        return configuration;
    }

    public abstract @NotNull String type();

    public abstract void tick();
}
