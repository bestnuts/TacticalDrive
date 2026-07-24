package me.bestnuts.drive.api.model.vehicle.component.bone;

import lombok.Getter;
import lombok.Setter;
import me.bestnuts.drive.api.manager.BoneType;
import me.bestnuts.drive.api.model.entity.Driver;
import me.bestnuts.drive.api.model.vehicle.data.BoneData;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@BoneType("seat")
public abstract class VehicleSeat extends VehicleBone {

    @Nullable
    @Getter
    @Setter
    private Driver driver;

    public VehicleSeat(@NotNull Entity entity, @NotNull BoneData data) {
        super(entity, data);
    }
}
