package me.bestnuts.core.model.vehicle.component;

import me.bestnuts.api.model.vehicle.component.VehicleGroup;
import me.bestnuts.api.model.vehicle.component.VehicleWheel;
import me.bestnuts.api.model.vehicle.dto.WheelContact;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public final class WheelEntity extends VehicleWheel {

    public WheelEntity(@NotNull VehicleGroup group, @NotNull Entity entity) {
        super(group, entity);
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
