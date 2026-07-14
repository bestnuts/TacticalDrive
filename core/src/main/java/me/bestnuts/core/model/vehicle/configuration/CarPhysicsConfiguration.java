package me.bestnuts.core.model.vehicle.configuration;

import lombok.Getter;
import me.bestnuts.api.model.vehicle.configuration.PhysicsConfiguration;
import me.bestnuts.api.model.vehicle.configuration.VehicleConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

@Getter
public final class CarPhysicsConfiguration extends PhysicsConfiguration {

    private final double restLength;

    public CarPhysicsConfiguration(@NotNull VehicleConfiguration parent, @NotNull FileConfiguration configuration) {
        super(parent, configuration);
        restLength = configuration.getDouble("physics.rest-length", 1.5);
    }
}
