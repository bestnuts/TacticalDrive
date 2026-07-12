package me.bestnuts.api.manager;

import me.bestnuts.api.model.vehicle.configuration.SharedConfiguration;
import me.bestnuts.api.model.vehicle.configuration.VehicleConfiguration;
import me.bestnuts.api.model.vehicle.data.ConfigurationFactorySender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface ConfigurationFactory extends Factory<ConfigurationFactorySender, SharedConfiguration> {

    @NotNull Optional<FileConfiguration> parameter(@NotNull String name);

    <C extends SharedConfiguration> C getConfiguration(@NotNull Class<C> type, @NotNull VehicleConfiguration parent, @NotNull FileConfiguration configuration);
}
