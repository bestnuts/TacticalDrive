package me.bestnuts.drive.core.model.vehicle.data;

import me.bestnuts.drive.api.model.vehicle.data.VehicleOutput;

public sealed interface HeliOutput extends VehicleOutput permits ControlOutput, HeliBodyOutput {
}
