package me.bestnuts.drive.core.model.vehicle.data;

import org.bukkit.util.Vector;

public record HeliBodyOutput(boolean blocked, Vector offset) implements HeliOutput {
}
