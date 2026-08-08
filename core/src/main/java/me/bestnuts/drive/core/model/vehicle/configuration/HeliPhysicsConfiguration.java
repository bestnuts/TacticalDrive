package me.bestnuts.drive.core.model.vehicle.configuration;

import lombok.Getter;
import me.bestnuts.drive.api.model.vehicle.configuration.PhysicsConfiguration;
import me.bestnuts.drive.api.model.vehicle.configuration.VehicleConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

@Getter
public final class HeliPhysicsConfiguration extends PhysicsConfiguration {

    private final double acceleration;
    private final double forwardDrag;

    private final double hoverThrust;
    private final double climbPower;
    private final double maxFallSpeed;
    private final double groundClearance;

    public HeliPhysicsConfiguration(@NotNull VehicleConfiguration parent, @NotNull FileConfiguration configuration) {
        super(parent, configuration);
        acceleration = configuration.getDouble("physics.acceleration", 8.0);
        forwardDrag = configuration.getDouble("physics.forward-drag", 1.2);

        hoverThrust = configuration.getDouble("physics.hover-thrust", 0.85);
        climbPower = configuration.getDouble("physics.climb-power", 1.0);
        maxFallSpeed = configuration.getDouble("physics.max-fall-speed", 20.0);
        groundClearance = configuration.getDouble("physics.ground-clearance", 1.0);
    }
}
