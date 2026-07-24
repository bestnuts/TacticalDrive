package me.bestnuts.drive.core.manager;

import me.bestnuts.drive.api.manager.EntityFactory;
import me.bestnuts.drive.api.manager.VehicleConfigurationFactory;
import me.bestnuts.drive.api.model.vehicle.configuration.DefaultConfiguration;
import me.bestnuts.drive.api.model.vehicle.configuration.FuelConfiguration;
import me.bestnuts.drive.api.model.vehicle.configuration.HandleConfiguration;
import me.bestnuts.drive.api.model.vehicle.configuration.PhysicsConfiguration;
import me.bestnuts.drive.api.model.vehicle.data.ConfigurationFactorySender;
import me.bestnuts.core.model.vehicle.configuration.*;
import me.bestnuts.drive.core.model.vehicle.configuration.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Optional;

public final class CarConfigurationFactory extends VehicleConfigurationFactory {

    private final File dir;

    public CarConfigurationFactory(@NotNull EntityFactory entityFactory, @NotNull File file) {
        super(entityFactory);
        dir = new File(file, "vehicles/car");
        if (!dir.exists() || !dir.isDirectory()) dir.mkdirs();
        register(DefaultConfiguration.class, SharedDefaultConfiguration::new)
                .register(FuelConfiguration.class, SharedFuelConfiguration::new)
                .register(HandleConfiguration.class, CarHandleConfiguration::new)
                .register(PhysicsConfiguration.class, CarPhysicsConfiguration::new);
    }

    @Override
    @NotNull
    public Optional<FileConfiguration> parameter(@NotNull String name) {
        File file = new File(dir, name.concat(".yml"));
        if (!file.exists()) return Optional.empty();
        return Optional.of(YamlConfiguration.loadConfiguration(file));
    }

    @Override
    @NotNull
    public CarConfiguration generate(@NotNull ConfigurationFactorySender sender) {
        return new CarConfiguration(this, sender);
    }

    @Override
    @Nullable
    public CarConfiguration generate(@NotNull String name) {
        Optional<FileConfiguration> optional = parameter(name);
        return optional.map(configuration -> generate(new ConfigurationFactorySender(configuration, name))).orElse(null);
    }
}
