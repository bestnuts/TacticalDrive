package me.bestnuts.api.model.vehicle.component;

import me.bestnuts.api.model.vehicle.configuration.BoneConfiguration;
import me.bestnuts.api.model.vehicle.dto.EntityFactorySender;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class VehicleGroup {

    @Nullable private final VehicleGroup parent;
    @NotNull private final List<VehicleBone> bones;
    @NotNull private final List<VehicleGroup> children = new ArrayList<>();

    public VehicleGroup(@Nullable VehicleGroup parent, @Nullable EntityFactorySender sender, @NotNull BoneConfiguration boneConfiguration) {
        this.parent = parent;
        this.bones = sender == null ? List.of() : boneConfiguration.create(this, sender);
    }

    public static VehicleGroup createRoot(@NotNull EntityFactorySender sender, @NotNull BoneConfiguration boneConfiguration) {
        return new VehicleGroup(null, sender, boneConfiguration);
    }

    public static VehicleGroup createDummy(@Nullable VehicleGroup parent, @NotNull BoneConfiguration boneConfiguration) {
        return new VehicleGroup(parent, null, boneConfiguration);
    }

    @Nullable
    public VehicleGroup parent() {
        return parent;
    }

    public VehicleGroup addChild(@Nullable EntityFactorySender sender, @NotNull BoneConfiguration boneConfiguration) {
        VehicleGroup child = new VehicleGroup(this, sender, boneConfiguration);
        this.children.add(child);
        return child;
    }

    public void consumerTransition(Consumer<VehicleGroup> consumer) {
        consumer.accept(this);
        for (VehicleGroup group : children) {
            group.consumerTransition(consumer);
        }
    }

    @NotNull
    public List<VehicleGroup> children() {
        return children;
    }

    @NotNull
    public List<VehicleBone> bones() {
        return bones;
    }
}
