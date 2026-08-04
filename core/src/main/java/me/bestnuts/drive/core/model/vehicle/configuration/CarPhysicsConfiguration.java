package me.bestnuts.drive.core.model.vehicle.configuration;

import lombok.Getter;
import me.bestnuts.drive.api.model.vehicle.configuration.PhysicsConfiguration;
import me.bestnuts.drive.api.model.vehicle.configuration.VehicleConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

@Getter
public final class CarPhysicsConfiguration extends PhysicsConfiguration {

    private final double restLength;

    private final double engineForceScale;
    private final double rollingResistance;
    private final double reverseSpeedRatio;

    private final double bodyAngleSmoothing;
    private final double rideHeightSmoothing;
    private final double rideHeightSmoothingLocked;

    private final double collisionTorqueImpact;
    private final double collisionSpeedLoss;
    private final double collisionPushDistance;
    private final double wallFriction;

    public CarPhysicsConfiguration(@NotNull VehicleConfiguration parent, @NotNull FileConfiguration configuration) {
        super(parent, configuration);
        restLength = configuration.getDouble("physics.rest-length", 1.5);

        engineForceScale = configuration.getDouble("physics.engine-force-scale", 0.02);
        rollingResistance = configuration.getDouble("physics.rolling-resistance", 0.015);
        reverseSpeedRatio = configuration.getDouble("physics.reverse-speed-ratio", 0.3);

        bodyAngleSmoothing = configuration.getDouble("physics.body-angle-smoothing", 10.0);
        rideHeightSmoothing = configuration.getDouble("physics.ride-height-smoothing", 15.0);
        rideHeightSmoothingLocked = configuration.getDouble("physics.ride-height-smoothing-locked", 40.0);

        collisionTorqueImpact = configuration.getDouble("physics.collision-torque-impact", 25.0);
        collisionSpeedLoss = configuration.getDouble("physics.collision-speed-loss", 0.4);
        collisionPushDistance = configuration.getDouble("physics.collision-push-distance", 0.15);
        wallFriction = configuration.getDouble("physics.wall-friction", 0.5);
    }
}
