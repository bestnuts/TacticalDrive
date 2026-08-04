package me.bestnuts.drive.core.model.vehicle.data;

public record WheelOutput(double forwardForce, double lateralForce, double wheelSteer) implements CarOutput {
}
