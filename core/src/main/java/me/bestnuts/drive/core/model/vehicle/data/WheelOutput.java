package me.bestnuts.drive.core.model.vehicle.data;

import java.util.UUID;

public record WheelOutput(UUID boneId, boolean steerable, double forwardForce, double grip, double wheelSteer) implements CarOutput {
}
