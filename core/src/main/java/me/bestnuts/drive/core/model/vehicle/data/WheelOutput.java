package me.bestnuts.drive.core.model.vehicle.data;

import java.util.UUID;

public record WheelOutput(UUID boneId, boolean steerable, double forwardForce, double lateralForce, double wheelSteer) implements CarOutput {
}
