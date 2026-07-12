package me.bestnuts.api.model.vehicle.component;

import me.bestnuts.api.manager.BoneType;
import me.bestnuts.api.model.vehicle.data.BoneData;
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
