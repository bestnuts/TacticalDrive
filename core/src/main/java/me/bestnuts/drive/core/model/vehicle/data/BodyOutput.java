package me.bestnuts.drive.core.model.vehicle.data;

import me.bestnuts.drive.api.model.vehicle.VehicleHitbox;
import org.bukkit.util.Vector;

public record BodyOutput(boolean lock, Vector offset, VehicleHitbox hitbox,
                         Vector impactVelocity, Vector separation) implements CarOutput {
}
