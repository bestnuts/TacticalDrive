package me.bestnuts.api.model.vehicle.component;

import lombok.Getter;
import lombok.Setter;
import me.bestnuts.api.manager.BoneType;
import me.bestnuts.api.model.entity.Driver;
import me.bestnuts.api.model.vehicle.data.BoneData;
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
