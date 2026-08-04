package me.bestnuts.drive.core.model.vehicle.data;

import org.bukkit.util.Vector;

import java.util.UUID;

public record SuspensionOutput(UUID boneId, double upwardForce, double wheelWorldY, double climbLimitY,
                               boolean grounded, boolean wall, double friction, Vector offset) implements CarOutput {
}
