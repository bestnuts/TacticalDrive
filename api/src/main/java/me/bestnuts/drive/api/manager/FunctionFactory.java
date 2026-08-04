package me.bestnuts.drive.api.manager;

import me.bestnuts.drive.api.model.vehicle.component.function.VehicleFunction;
import me.bestnuts.drive.api.model.vehicle.data.FunctionFactorySender;

import java.util.List;

public interface FunctionFactory extends Factory<FunctionFactorySender, List<VehicleFunction>> {
}
