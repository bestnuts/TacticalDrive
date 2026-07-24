package me.bestnuts.drive.core.model.vehicle.component.bone;

import me.bestnuts.drive.api.model.entity.Driver;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleSeat;
import me.bestnuts.drive.api.model.vehicle.data.BoneData;
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
    }
}
