package me.bestnuts.api.model.vehicle.component;

import me.bestnuts.api.model.vehicle.dto.WheelContact;

public abstract class VehicleWheel extends VehicleBone {

    public VehicleWheel(VehicleGroup group) {
        super(group);
    }

    public abstract WheelContact contact();
}
