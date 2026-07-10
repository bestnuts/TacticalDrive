package me.bestnuts.core.model.vehicle.configuration;

import me.bestnuts.api.model.vehicle.configuration.VehicleConfiguration;
import me.bestnuts.api.model.vehicle.dto.ConfigurationFactorySender;
import me.bestnuts.core.manager.CarConfigurationFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CarConfiguration extends VehicleConfiguration {

    private final CarConfigurationFactory factory;

    public CarConfiguration(@NotNull CarConfigurationFactory factory, @NotNull ConfigurationFactorySender sender) {
        super(factory, sender.configuration(), sender.name());
        this.factory = factory;
    }

    @Override
    public @Nullable VehicleConfiguration regenerate() {
        return factory.generate(name());
    }
}
