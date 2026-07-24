package me.bestnuts.drive.api.model.vehicle.configuration;

import lombok.Getter;
import me.bestnuts.drive.api.manager.VehicleConfigurationFactory;
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

    public VehicleConfiguration(@NotNull VehicleConfigurationFactory factory,
                                @NotNull FileConfiguration configuration,
                                @NotNull String name) {
        this.configuration = configuration;
        defaultConfiguration = factory.getConfiguration(DefaultConfiguration.class, this, configuration);
        fuel = factory.getConfiguration(FuelConfiguration.class, this, configuration);
        handle = factory.getConfiguration(HandleConfiguration.class, this, configuration);
        physics = factory.getConfiguration(PhysicsConfiguration.class, this, configuration);

        this.name = name;
    }

    @Override
    @NotNull
    public String name() {
        return name;
    }

    public abstract @Nullable VehicleConfiguration regenerate();
}
