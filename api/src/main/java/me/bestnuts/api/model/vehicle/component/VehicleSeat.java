package me.bestnuts.api.model.vehicle.component;

import lombok.Getter;
import lombok.Setter;
import me.bestnuts.api.manager.BoneType;
import me.bestnuts.api.model.entity.Driver;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@BoneType("seat")
public abstract class VehicleSeat extends VehicleBone {

    @Nullable
    private Driver driver;

    public VehicleSeat(VehicleGroup group) {
        super(group);
    }
}
