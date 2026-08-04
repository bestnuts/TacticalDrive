package me.bestnuts.drive.api.model.vehicle.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class VehicleMotion {

    private double speed;
    private double steer;
    private double pitch;
    private double roll;
    private double verticalVelocity;
}
