package me.bestnuts.api.model.vehicle.configuration;

import me.bestnuts.api.model.vehicle.component.VehicleGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface GroupConfiguration {

    @Nullable BoneConfiguration boneConfiguration();

    @NotNull List<GroupConfiguration> children();

    @NotNull VehicleGroup create();
}
