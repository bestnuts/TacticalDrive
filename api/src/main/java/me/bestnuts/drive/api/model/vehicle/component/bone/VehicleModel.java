package me.bestnuts.drive.api.model.vehicle.component.bone;

import me.bestnuts.drive.api.manager.BoneType;
import me.bestnuts.drive.api.model.vehicle.data.BoneData;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

@BoneType("model")
public abstract class VehicleModel extends VehicleBone {

    public VehicleModel(@NotNull Entity entity, @NotNull BoneData data) {
        super(entity, data);
    }
}
