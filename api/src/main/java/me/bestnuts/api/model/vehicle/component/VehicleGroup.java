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
    @NotNull private final String name;
    @NotNull private final List<VehicleBone> bones;
    @NotNull private final List<VehicleGroup> children = new ArrayList<>();

    public VehicleGroup(@Nullable VehicleGroup parent, @NotNull String name, @Nullable EntityFactorySender sender, @NotNull BoneConfiguration boneConfiguration) {
        this.parent = parent;
        this.name = name;
        this.bones = sender == null ? List.of() : boneConfiguration.create(this, sender);
    }

    public VehicleGroup(@Nullable VehicleGroup parent, @NotNull String name, @NotNull List<VehicleBone> bones) {
        this.parent = parent;
        this.name = name;
        this.bones = bones;
    }

    public static VehicleGroup createRoot(@NotNull String name, @Nullable EntityFactorySender sender, @NotNull BoneConfiguration boneConfiguration) {
        return new VehicleGroup(null, name, sender, boneConfiguration);
    }

    public VehicleGroup addChild(@NotNull String name, @Nullable EntityFactorySender sender, @NotNull BoneConfiguration boneConfiguration) {
        VehicleGroup child = new VehicleGroup(this, name, sender, boneConfiguration);
        this.children.add(child);
        return child;
    }

    @NotNull
    public String getAbsolutePath() {
        if (parent == null) return name;
        return parent.name() + "." + name;
    }

    public void consumerTransition(Consumer<VehicleGroup> consumer) {
        consumer.accept(this);
        for (VehicleGroup group : children) {
            group.consumerTransition(consumer);
        }
    }

    @Nullable
    public VehicleGroup parent() {
        return parent;
    }

    @NotNull
    public String name() {
        return name;
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
