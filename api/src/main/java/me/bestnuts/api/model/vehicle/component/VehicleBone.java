package me.bestnuts.api.model.vehicle.component;

import lombok.Getter;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class VehicleBone extends VehicleEntity {

    @NotNull private final VehicleGroup group;
    @NotNull private final String type;

    public VehicleBone(@NotNull Entity entity, @NotNull VehicleGroup group, @NotNull String type) {
        super(entity);
        this.group = group;
        this.type = type;
    }
}
