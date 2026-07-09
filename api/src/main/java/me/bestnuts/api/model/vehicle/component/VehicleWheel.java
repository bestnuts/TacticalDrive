package me.bestnuts.api.model.vehicle.component;

import me.bestnuts.api.manager.BoneType;
import me.bestnuts.api.model.vehicle.dto.WheelContact;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

@BoneType("wheel")
public abstract class VehicleWheel extends VehicleBone {

    public VehicleWheel(@NotNull VehicleGroup group, @NotNull Entity entity, @NotNull String type) {
        super(group, entity, type);
    }

    public abstract WheelContact contact();
}
