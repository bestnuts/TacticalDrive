package me.bestnuts.api.model.vehicle.component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public abstract class VehicleBone {

    @NotNull
    private final VehicleGroup group;
}
