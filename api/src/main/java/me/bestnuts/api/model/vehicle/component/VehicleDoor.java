package me.bestnuts.api.model.vehicle.component;

import me.bestnuts.api.manager.BoneType;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

@BoneType("door")
public abstract class VehicleDoor extends VehicleBone {

    private boolean closed = true;

    public VehicleDoor(@NotNull VehicleGroup group, @NotNull Entity entity) {
        super(group, entity);
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
