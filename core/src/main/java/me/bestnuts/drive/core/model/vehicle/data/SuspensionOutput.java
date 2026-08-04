package me.bestnuts.drive.core.model.vehicle.data;

import org.bukkit.util.Vector;

public record SuspensionOutput(double upwardForce, double wheelWorldY, boolean lock, Vector offset) implements CarOutput {
}
