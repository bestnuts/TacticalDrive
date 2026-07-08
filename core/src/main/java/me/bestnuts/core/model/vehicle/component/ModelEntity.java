package me.bestnuts.core.model.vehicle.component;

import me.bestnuts.api.model.vehicle.component.VehicleGroup;
import me.bestnuts.api.model.vehicle.component.VehicleModel;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public final class ModelEntity extends VehicleModel {

    public ModelEntity(@NotNull VehicleGroup group, @NotNull Entity entity) {
        super(group, entity);
    }
}
