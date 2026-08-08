package me.bestnuts.drive.core.model.vehicle.data;

public record ControlOutput(double climb, double bank, boolean forward, float lookYaw, boolean piloted) implements HeliOutput {
}
