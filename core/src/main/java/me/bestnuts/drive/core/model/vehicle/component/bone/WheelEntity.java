package me.bestnuts.drive.core.model.vehicle.component.bone;

import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleWheel;
import me.bestnuts.drive.api.model.vehicle.data.BoneData;
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
}
