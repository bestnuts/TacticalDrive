package me.bestnuts.api.model.vehicle.component;

import me.bestnuts.api.manager.BoneType;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

@BoneType("model")
public abstract class VehicleModel extends VehicleBone {

    public VehicleModel(@NotNull VehicleGroup group, @NotNull Entity entity) {
        super(group, entity);
    }
}
