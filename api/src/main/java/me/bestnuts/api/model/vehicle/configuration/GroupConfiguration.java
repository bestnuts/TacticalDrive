package me.bestnuts.api.model.vehicle.configuration;

import me.bestnuts.api.model.vehicle.component.VehicleGroup;
import me.bestnuts.api.model.vehicle.dto.EntityFactorySender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface GroupConfiguration extends SharedConfiguration {

    @NotNull List<GroupConfiguration> children();

    @NotNull VehicleGroup create(@NotNull EntityFactorySender sender);
}
