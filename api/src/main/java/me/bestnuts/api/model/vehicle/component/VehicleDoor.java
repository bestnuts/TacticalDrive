package me.bestnuts.api.model.vehicle.component;

import lombok.Getter;
import me.bestnuts.api.manager.BoneType;

@Getter
@BoneType("door")
public abstract class VehicleDoor extends VehicleBone {

    private boolean closed = true;

    public VehicleDoor(VehicleGroup group) {
        super(group);
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
