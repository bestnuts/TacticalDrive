package me.bestnuts.drive.core.manager;

import me.bestnuts.drive.api.model.vehicle.configuration.DefaultConfiguration;
import me.bestnuts.drive.api.model.vehicle.configuration.FuelConfiguration;
import me.bestnuts.drive.api.model.vehicle.configuration.HandleConfiguration;
import me.bestnuts.drive.api.model.vehicle.configuration.PhysicsConfiguration;
import me.bestnuts.drive.api.model.vehicle.data.ConfigurationFactorySender;
import me.bestnuts.drive.core.model.vehicle.configuration.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Optional;

public final class HeliConfigurationFactory extends DirectoryConfigurationFactory {

    public HeliConfigurationFactory(@NotNull File root) {
        super(root, "vehicles/heli");
        register(DefaultConfiguration.class, SharedDefaultConfiguration::new)
                .register(FuelConfiguration.class, SharedFuelConfiguration::new)
                .register(HandleConfiguration.class, HeliHandleConfiguration::new)
                .register(PhysicsConfiguration.class, HeliPhysicsConfiguration::new);
    }

    @Override
    @NotNull
    public HeliConfiguration generate(@NotNull ConfigurationFactorySender sender) {
        return new HeliConfiguration(this, sender);
    }

    @Override
    @Nullable
    public HeliConfiguration generate(@NotNull String name) {
        Optional<FileConfiguration> optional = parameter(name);
        return optional.map(configuration -> generate(new ConfigurationFactorySender(configuration, name))).orElse(null);
    }
}
