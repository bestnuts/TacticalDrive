package me.bestnuts.api.model.vehicle.configuration;

import me.bestnuts.api.model.vehicle.component.VehicleBone;
import me.bestnuts.api.model.vehicle.component.VehicleGroup;
import me.bestnuts.api.model.vehicle.dto.EntityFactorySender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface BoneConfiguration extends SharedConfiguration {

    @NotNull List<VehicleBone> create(@NotNull VehicleGroup group, @NotNull EntityFactorySender sender);
}
