package me.bestnuts.drive.api.model.vehicle.component.function;

import lombok.Getter;
import me.bestnuts.drive.api.model.vehicle.Vehicle;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Getter
public abstract class VehicleFunction {

    private final VehicleEntity parent;
    private final int delay;
    private int tick;

    public VehicleFunction(@NotNull VehicleEntity parent, int delay, @NotNull Map<String, String> param) {
        this.parent = parent;
        this.delay = delay;
        this.tick = delay;
    }

    public void run(@NotNull Vehicle vehicle) {
        if (tick > 0) {
            tick--;
            return;
        }
        execute(vehicle);
        tick = delay;
    }

    public abstract void execute(@NotNull Vehicle vehicle);
}
