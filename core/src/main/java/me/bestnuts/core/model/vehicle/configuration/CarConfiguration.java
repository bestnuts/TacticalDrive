package me.bestnuts.core.model.vehicle.configuration;

import me.bestnuts.api.model.vehicle.configuration.VehicleConfiguration;
import me.bestnuts.core.manager.CarConfigurationFactory;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CarConfiguration extends VehicleConfiguration {

    private final CarConfigurationFactory factory;

    public CarConfiguration(@NotNull CarConfigurationFactory factory, @NotNull FileConfiguration configuration) {
        super(factory, configuration, CarBoneConfiguration::new);
        this.factory = factory;
    }

    @Override
    public @Nullable VehicleConfiguration regenerate() {
        return factory.generate(name());
    }
}
