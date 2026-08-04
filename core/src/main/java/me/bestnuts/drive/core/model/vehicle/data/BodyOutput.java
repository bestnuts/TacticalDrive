package me.bestnuts.drive.core.model.vehicle.data;

import org.bukkit.util.Vector;

public record BodyOutput(boolean lock, Vector offset) implements CarOutput {
}
