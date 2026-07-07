package me.bestnuts.api.model.vehicle.component;

import me.bestnuts.api.model.vehicle.configuration.BoneConfiguration;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public final class VehicleGroup {

    @Nullable private final VehicleGroup parent;
    @NotNull private final List<VehicleBone> bones;
    @NotNull private final List<VehicleGroup> children = new ArrayList<>();

    public VehicleGroup(@Nullable VehicleGroup parent, @Nullable BoneConfiguration boneConfiguration) {
        this.parent = parent;
        this.bones = boneConfiguration != null ? boneConfiguration.create(this) : List.of();
    }

    public static VehicleGroup createRoot(@Nullable BoneConfiguration boneConfiguration) {
        return new VehicleGroup(null, boneConfiguration);
    }

    @Nullable
    public VehicleGroup parent() {
        return parent;
    }

    public VehicleGroup addChild(@Nullable BoneConfiguration boneConfiguration) {
        VehicleGroup child = new VehicleGroup(this, boneConfiguration);
        this.children.add(child);
        return child;
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
