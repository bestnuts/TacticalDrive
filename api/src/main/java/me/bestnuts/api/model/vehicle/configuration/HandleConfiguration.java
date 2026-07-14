package me.bestnuts.api.model.vehicle.configuration;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class HandleConfiguration implements SharedConfiguration {

    private final VehicleConfiguration parent;

    private final double steeringSensitivity;
    private final double brakeForce;

    public HandleConfiguration(@NotNull VehicleConfiguration parent, @NotNull FileConfiguration configuration) {
        this.parent = parent;
        steeringSensitivity = configuration.getDouble("handle.steering-sensitivity", 1.15);
        brakeForce = configuration.getDouble("handle.brake-force", 1.65);
    }

    @Override
    public @NotNull String name() {
        return parent.name();
    }
}
