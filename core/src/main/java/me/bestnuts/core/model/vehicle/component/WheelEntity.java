package me.bestnuts.core.model.vehicle.component;

import me.bestnuts.api.model.vehicle.component.VehicleWheel;
import me.bestnuts.api.model.vehicle.data.BoneData;
import me.bestnuts.api.model.vehicle.data.WheelContact;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public final class WheelEntity extends VehicleWheel {

    public WheelEntity(@NotNull Entity entity, @NotNull BoneData data) {
        super(entity, data);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public WheelContact contact() {
        return null;
    }
}
