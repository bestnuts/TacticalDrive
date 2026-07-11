package me.bestnuts.api.model.vehicle.component;

import me.bestnuts.api.manager.BoneFactory;
import me.bestnuts.api.model.vehicle.dto.BoneFactorySender;
import me.bestnuts.api.model.vehicle.dto.BoneRestoreFactorySender;
import me.bestnuts.api.model.vehicle.dto.EntityFactorySender;
import org.bukkit.entity.Entity;
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

    public VehicleGroup(@Nullable VehicleGroup parent, @NotNull String name, @Nullable EntityFactorySender sender, @NotNull BoneFactory boneFactory) {
        this.parent = parent;
        this.name = name;
        this.bones = sender == null ? List.of() : boneFactory.generate(new BoneFactorySender(this, sender));
    }

    public VehicleGroup(@Nullable VehicleGroup parent, @NotNull String name, @NotNull List<Entity> entities, @NotNull BoneFactory boneFactory) {
        this.parent = parent;
        this.name = name;
        this.bones = boneFactory.regenerate(new BoneRestoreFactorySender(this, new ArrayList<>(entities)));
    }

    public VehicleGroup addChild(@NotNull String name, @Nullable EntityFactorySender sender, @NotNull BoneFactory boneFactory) {
        VehicleGroup child = new VehicleGroup(this, name, sender, boneFactory);
        this.children.add(child);
        return child;
    }

    public VehicleGroup addChild(@NotNull String name, @NotNull List<Entity> entities, @NotNull BoneFactory boneFactory) {
        VehicleGroup child = new VehicleGroup(this, name, entities, boneFactory);
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
