package me.bestnuts.core.model.vehicle.component;

import me.bestnuts.api.model.entity.Driver;
import me.bestnuts.api.model.vehicle.component.VehicleSeat;
import me.bestnuts.api.model.vehicle.dto.BoneData;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class SeatEntity extends VehicleSeat {

    public SeatEntity(@NotNull Entity entity, @NotNull BoneData data) {
        super(entity, data);
    }

    @Override
    public void setDriver(@Nullable Driver driver) {
        super.setDriver(driver);
        if (driver != null) {
            getEntity().addPassenger(driver.getEntity());
        }
    }
}
