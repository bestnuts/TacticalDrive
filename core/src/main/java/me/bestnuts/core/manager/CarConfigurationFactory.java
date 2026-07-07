package me.bestnuts.core.manager;

import me.bestnuts.api.manager.VehicleConfigurationFactory;
import me.bestnuts.api.model.vehicle.configuration.DefaultConfiguration;
import me.bestnuts.api.model.vehicle.configuration.FuelConfiguration;
import me.bestnuts.api.model.vehicle.configuration.HandleConfiguration;
import me.bestnuts.api.model.vehicle.configuration.PhysicsConfiguration;
import me.bestnuts.core.model.vehicle.configuration.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Optional;

public class CarConfigurationFactory extends VehicleConfigurationFactory {

    private final File dir;

    public CarConfigurationFactory(@NotNull File file) {
        dir = new File(file, "vehicles");
        if (!dir.exists() || !dir.isDirectory()) dir.mkdir();
        register(DefaultConfiguration.class, SharedDefaultConfiguration::new)
                .register(FuelConfiguration.class, SharedFuelConfiguration::new)
                .register(HandleConfiguration.class, CarHandleConfiguration::new)
                .register(PhysicsConfiguration.class, CarPhysicsConfiguration::new);
    }

    @Override
    public @NotNull Optional<FileConfiguration> parameter(String name) {
        File file = new File(dir, name.concat(".yml"));
        if (!file.exists()) return Optional.empty();
        return Optional.of(YamlConfiguration.loadConfiguration(file));
    }

    @Override
    public @NotNull CarConfiguration generate(@NotNull FileConfiguration configuration) {
        return new CarConfiguration(this, configuration);
    }

    @Override
    public @Nullable CarConfiguration generate(String name) {
        Optional<FileConfiguration> optional = parameter(name);
        return optional.map(this::generate).orElse(null);
    }
}
