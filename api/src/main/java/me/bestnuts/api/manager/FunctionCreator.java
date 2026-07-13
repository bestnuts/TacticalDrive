package me.bestnuts.api.manager;

import me.bestnuts.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.api.model.vehicle.component.function.VehicleFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@FunctionalInterface
public interface FunctionCreator {
    VehicleFunction create(@NotNull VehicleEntity entity, int delay, @NotNull Map<String, String> param);
}
