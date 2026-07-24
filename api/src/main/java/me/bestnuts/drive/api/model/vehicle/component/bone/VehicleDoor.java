package me.bestnuts.drive.api.model.vehicle.component.bone;

import me.bestnuts.drive.api.manager.BoneType;
import me.bestnuts.drive.api.model.vehicle.data.BoneData;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

@BoneType("door")
public abstract class VehicleDoor extends VehicleBone {

    private boolean closed = true;

    public VehicleDoor(@NotNull Entity entity, @NotNull BoneData data) {
        super(entity, data);
    }

    public void open() {
        closed = false;
    }

    public void close() {
        closed = true;
    }

    public void toggle() {
        closed = !closed;
    }
}
