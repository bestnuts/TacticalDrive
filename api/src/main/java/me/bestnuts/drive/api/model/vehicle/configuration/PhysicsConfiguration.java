package me.bestnuts.drive.api.model.vehicle.configuration;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class PhysicsConfiguration implements SharedConfiguration {

    private final VehicleConfiguration parent;

    private final double maxSpeed;
    private final double horsePower;

    private final double gravity;
    private final double mass;
    private final double dragCoefficient;

    private final int maxRPM;
    private final int idleRPM;

    public PhysicsConfiguration(@NotNull VehicleConfiguration parent, @NotNull FileConfiguration configuration) {
        this.parent = parent;

        maxSpeed = configuration.getDouble("physics.max-speed", 20.0);
        horsePower = configuration.getDouble("physics.horse-power", 25.0);

        gravity = configuration.getDouble("physics.gravity", 9.81);
        mass = configuration.getDouble("physics.mass", 500.0);
        dragCoefficient = configuration.getDouble("physics.drag-coefficient", 1.7);

        maxRPM = configuration.getInt("physics.max-rpm", 1200);
        idleRPM = configuration.getInt("physics.idle-rpm", 600);
    }

    @Override
    public @NotNull String name() {
        return parent.name();
    }
}
