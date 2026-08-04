package me.bestnuts.drive.core.model.vehicle.data;

import me.bestnuts.drive.api.model.vehicle.data.VehicleOutput;

public sealed interface CarOutput extends VehicleOutput permits WheelOutput, SuspensionOutput, BodyOutput {
}
