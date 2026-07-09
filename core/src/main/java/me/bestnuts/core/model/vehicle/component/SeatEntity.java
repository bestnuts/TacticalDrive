package me.bestnuts.core.model.vehicle.component;

import me.bestnuts.api.model.vehicle.component.VehicleGroup;
import me.bestnuts.api.model.vehicle.component.VehicleSeat;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public final class SeatEntity extends VehicleSeat {

    public SeatEntity(@NotNull Entity entity, @NotNull VehicleGroup group, @NotNull String type) {
        super(entity, group, type);
    }
}
