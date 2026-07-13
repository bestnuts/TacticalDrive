package me.bestnuts.api.model.vehicle.component.bone;

import me.bestnuts.api.manager.BoneType;
import me.bestnuts.api.model.vehicle.data.BoneData;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

@BoneType("wheel")
public abstract class VehicleWheel extends VehicleBone {

    public VehicleWheel(@NotNull Entity entity, @NotNull BoneData data) {
        super(entity, data);
    }
}
