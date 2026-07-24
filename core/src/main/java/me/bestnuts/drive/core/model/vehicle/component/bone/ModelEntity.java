package me.bestnuts.drive.core.model.vehicle.component.bone;

import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleModel;
import me.bestnuts.drive.api.model.vehicle.data.BoneData;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public final class ModelEntity extends VehicleModel {

    public ModelEntity(@NotNull Entity entity, @NotNull BoneData data) {
        super(entity, data);
    }
}
