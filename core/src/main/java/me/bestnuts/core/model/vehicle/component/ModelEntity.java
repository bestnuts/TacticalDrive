package me.bestnuts.core.model.vehicle.component;

import me.bestnuts.api.model.vehicle.component.VehicleModel;
import me.bestnuts.api.model.vehicle.dto.BoneData;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public final class ModelEntity extends VehicleModel {

    public ModelEntity(@NotNull Entity entity, @NotNull BoneData data) {
        super(entity, data);
    }
}
