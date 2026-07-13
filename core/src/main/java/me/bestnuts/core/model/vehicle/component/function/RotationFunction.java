package me.bestnuts.core.model.vehicle.component.function;

import me.bestnuts.api.model.vehicle.Vehicle;
import me.bestnuts.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.api.model.vehicle.component.function.VehicleFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class RotationFunction extends VehicleFunction {

    public RotationFunction(@NotNull VehicleEntity parent, int delay, @NotNull Map<String, String> param) {
        super(parent, delay, param);
    }

    @Override
    public void execute(@NotNull Vehicle vehicle) {

    }
}
