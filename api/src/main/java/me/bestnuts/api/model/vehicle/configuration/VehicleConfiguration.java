package me.bestnuts.api.model.vehicle.configuration;

import lombok.Getter;
import me.bestnuts.api.manager.ConfigurationFactory;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public abstract class VehicleConfiguration implements SharedConfiguration {

    private final FileConfiguration configuration;

    private final DefaultConfiguration defaultConfiguration;
    private final FuelConfiguration fuel;
    private final HandleConfiguration handle;
    private final PhysicsConfiguration physics;

    private final String name;

    public VehicleConfiguration(ConfigurationFactory factory, FileConfiguration configuration) {
        this.configuration = configuration;
        defaultConfiguration = factory.getConfiguration(DefaultConfiguration.class, this, configuration);
        fuel = factory.getConfiguration(FuelConfiguration.class, this, configuration);
        handle = factory.getConfiguration(HandleConfiguration.class, this, configuration);
        physics = factory.getConfiguration(PhysicsConfiguration.class, this, configuration);

        name = configuration.getName();
    }

    @Override
    public @NotNull String name() {
        return name;
    }

    public abstract @Nullable VehicleConfiguration regenerate();
}
