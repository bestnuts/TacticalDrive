package me.bestnuts.drive.core.model.vehicle.configuration;

import me.bestnuts.drive.api.model.vehicle.configuration.VehicleConfiguration;
import me.bestnuts.drive.api.model.vehicle.data.ConfigurationFactorySender;
import me.bestnuts.drive.core.manager.HeliConfigurationFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class HeliConfiguration extends VehicleConfiguration {

    private final HeliConfigurationFactory factory;

    public HeliConfiguration(@NotNull HeliConfigurationFactory factory, @NotNull ConfigurationFactorySender sender) {
        super(factory, sender.configuration(), sender.name());
        this.factory = factory;
    }

    @Override
    public @Nullable VehicleConfiguration regenerate() {
        return factory.generate(name());
    }
}
