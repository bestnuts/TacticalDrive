package me.bestnuts.api.model.vehicle.configuration;

import me.bestnuts.api.model.vehicle.component.VehicleGroup;
import me.bestnuts.api.model.vehicle.dto.EntityFactorySender;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface GroupConfiguration extends SharedConfiguration {

    @NotNull List<GroupConfiguration> children();

    @NotNull String groupName();

    @Nullable ConfigurationSection boneSection();

    @NotNull VehicleGroup create(@NotNull EntityFactorySender sender);
}
