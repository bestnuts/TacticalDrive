package me.bestnuts.drive.core.model.vehicle;

import me.bestnuts.drive.api.model.vehicle.Vehicle;
import me.bestnuts.drive.api.model.vehicle.VehicleRegistryType;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleGroup;
import me.bestnuts.drive.api.model.vehicle.configuration.VehicleConfiguration;
import me.bestnuts.drive.api.model.vehicle.data.VehicleOutput;
import me.bestnuts.drive.core.model.vehicle.data.BodyOutput;
import me.bestnuts.drive.core.model.vehicle.data.CarOutput;
import me.bestnuts.drive.core.model.vehicle.data.SuspensionOutput;
import me.bestnuts.drive.core.model.vehicle.data.WheelOutput;
import me.bestnuts.drive.core.model.vehicle.physics.CarMotionSolver;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class VehicleCar extends Vehicle {

    private final CarMotionSolver solver;

    public VehicleCar(@NotNull VehicleEntity entity, @NotNull VehicleGroup group, @NotNull VehicleConfiguration configuration) {
        super(entity, group, configuration);
        solver = new CarMotionSolver(configuration);
    }

    @Override
    public @NotNull String type() {
        return VehicleRegistryType.CAR.getName();
    }

    @Override
    protected void apply(@NotNull List<VehicleOutput> outputs) {
        List<WheelOutput> wheelOutputs = new ArrayList<>();
        List<SuspensionOutput> suspensionOutputs = new ArrayList<>();
        List<BodyOutput> bodyOutputs = new ArrayList<>();
        for (VehicleOutput output : outputs) {
            if (!(output instanceof CarOutput carOutput)) continue;
            switch (carOutput) {
                case WheelOutput wheel -> wheelOutputs.add(wheel);
                case SuspensionOutput suspension -> suspensionOutputs.add(suspension);
                case BodyOutput body -> bodyOutputs.add(body);
            }
        }

        for (BodyOutput body : bodyOutputs) {
            updateHitbox(body.hitbox());
        }

        Location target = solver.solve(entity().getLocation(), motion(), wheelOutputs, suspensionOutputs, bodyOutputs);
        entity().getEntity().teleport(target);
    }
}
