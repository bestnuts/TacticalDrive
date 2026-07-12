package me.bestnuts.api.model.vehicle.component;

import me.bestnuts.api.manager.BoneType;
import me.bestnuts.api.model.vehicle.data.BoneData;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

@BoneType("model")
public abstract class VehicleModel extends VehicleBone {

    public VehicleModel(@NotNull Entity entity, @NotNull BoneData data) {
        super(entity, data);
    }
}
