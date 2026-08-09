package me.bestnuts.drive.core.model.vehicle.configuration;

import lombok.Getter;
import me.bestnuts.drive.api.model.vehicle.configuration.HandleConfiguration;
import me.bestnuts.drive.api.model.vehicle.configuration.VehicleConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

@Getter
public final class HeliHandleConfiguration extends HandleConfiguration {

    private final double maxRoll;
    private final double maxNosePitch;
    private final double attitudeSmoothing;
    private final double yawSmoothing;
    private final double bankPower;
    private final double bankDrag;

    public HeliHandleConfiguration(@NotNull VehicleConfiguration parent, @NotNull FileConfiguration configuration) {
        super(parent, configuration);
        maxRoll = configuration.getDouble("handle.max-roll", 30.0);
        maxNosePitch = configuration.getDouble("handle.max-nose-pitch", 25.0);
        attitudeSmoothing = configuration.getDouble("handle.attitude-smoothing", 6.0);
        yawSmoothing = configuration.getDouble("handle.yaw-smoothing", 1.5);
        bankPower = configuration.getDouble("handle.bank-power", 1.5);
        bankDrag = configuration.getDouble("handle.bank-drag", 0.8);
    }
}
