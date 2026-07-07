package me.bestnuts.api.manager;

import me.bestnuts.api.model.vehicle.configuration.SharedConfiguration;
import me.bestnuts.api.model.vehicle.configuration.VehicleConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface ConfigurationFactory extends Factory<FileConfiguration, SharedConfiguration> {

    @NotNull Optional<FileConfiguration> parameter(String name);

    <C extends SharedConfiguration> C getConfiguration(@NotNull Class<C> type, @NotNull VehicleConfiguration parent, @NotNull FileConfiguration configuration);
}
