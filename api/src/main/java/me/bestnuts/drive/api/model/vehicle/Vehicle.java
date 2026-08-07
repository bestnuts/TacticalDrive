package me.bestnuts.drive.api.model.vehicle;

import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleBone;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleGroup;
import me.bestnuts.drive.api.model.vehicle.component.function.VehicleFunction;
import me.bestnuts.drive.api.model.vehicle.configuration.VehicleConfiguration;
import me.bestnuts.drive.api.model.vehicle.data.VehicleMotion;
import me.bestnuts.drive.api.model.vehicle.data.VehicleOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public abstract class Vehicle {

    private final VehicleEntity entity;
    private final VehicleGroup group;
    private final Map<UUID, VehicleBone> boneByIdMap;
    private final Map<String, UUID> idByPathMap;
    private final VehicleConfiguration configuration;

    private final VehicleMotion motion = new VehicleMotion();
    private VehicleHitbox hitbox;

    public Vehicle(@NotNull VehicleEntity entity, @NotNull VehicleGroup group, @NotNull VehicleConfiguration configuration) {
        this.entity = entity;
        this.group = group;

        Map<UUID, VehicleBone> tempIdMap = new HashMap<>();
        Map<String, UUID> tempPathMap = new HashMap<>();
        collectBones(group, tempIdMap, tempPathMap);
        this.boneByIdMap = Map.copyOf(tempIdMap);
        this.idByPathMap = Map.copyOf(tempPathMap);

        this.configuration = configuration;
    }

    private void collectBones(
            @NotNull VehicleGroup targetGroup,
            @NotNull Map<UUID, VehicleBone> idMap,
            @NotNull Map<String, UUID> pathMap
    ) {
        for (VehicleBone bone : targetGroup.bones()) {
            UUID id = bone.getUniqueId();
            String path = bone.getAbsolutePath();

            idMap.put(id, bone);
            pathMap.put(path, id);
        }

        for (VehicleGroup child : targetGroup.children()) {
            collectBones(child, idMap, pathMap);
        }
    }

    public @NotNull VehicleEntity entity() {
        return entity;
    }

    public @NotNull VehicleGroup group() {
        return group;
    }

    public @NotNull Collection<VehicleBone> bones() {
        return boneByIdMap.values();
    }

    public void remove() {
        boneByIdMap.values().forEach(bone -> bone.getEntity().remove());
        entity.getEntity().remove();
    }

    public @NotNull Optional<VehicleBone> findBoneById(@NotNull UUID id) {
        return Optional.ofNullable(boneByIdMap.get(id));
    }

    public @NotNull Optional<VehicleBone> findBoneByPath(@NotNull String path) {
        UUID id = idByPathMap.get(path);
        if (id == null) return Optional.empty();
        return findBoneById(id);
    }

    public @NotNull VehicleConfiguration configuration() {
        return configuration;
    }

    public @NotNull VehicleMotion motion() {
        return motion;
    }

    public @Nullable VehicleHitbox hitbox() {
        return hitbox;
    }

    protected void updateHitbox(@Nullable VehicleHitbox hitbox) {
        this.hitbox = hitbox;
    }

    public abstract @NotNull String type();

    public void tick() {
        List<VehicleOutput> outputs = new ArrayList<>();
        for (VehicleBone bone : boneByIdMap.values()) {
            for (VehicleFunction function : bone.getFunctions()) {
                VehicleOutput output = function.run(this);
                if (output == null) continue;
                outputs.add(output);
            }
        }
        apply(outputs);
    }

    protected abstract void apply(@NotNull List<VehicleOutput> outputs);
}
