package me.bestnuts.drive.core.model.vehicle;

import me.bestnuts.drive.api.model.vehicle.Vehicle;
import me.bestnuts.drive.api.model.vehicle.VehicleRegistryType;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleGroup;
import me.bestnuts.drive.api.model.vehicle.configuration.VehicleConfiguration;
import me.bestnuts.drive.api.model.vehicle.data.VehicleOutput;
import me.bestnuts.drive.core.model.vehicle.data.ControlOutput;
import me.bestnuts.drive.core.model.vehicle.data.HeliBodyOutput;
import me.bestnuts.drive.core.model.vehicle.data.HeliOutput;
import me.bestnuts.drive.core.model.vehicle.physics.HeliMotionSolver;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class VehicleHeli extends Vehicle {

    private final HeliMotionSolver solver;

    public VehicleHeli(@NotNull VehicleEntity entity, @NotNull VehicleGroup group, @NotNull VehicleConfiguration configuration) {
        super(entity, group, configuration);
        solver = new HeliMotionSolver(configuration);
    }

    @Override
    public @NotNull String type() {
        return VehicleRegistryType.HELI.getName();
    }

    @Override
    protected void apply(@NotNull List<VehicleOutput> outputs) {
        List<ControlOutput> controls = new ArrayList<>();
        List<HeliBodyOutput> bodies = new ArrayList<>();
        for (VehicleOutput output : outputs) {
            if (!(output instanceof HeliOutput heliOutput)) continue;
            switch (heliOutput) {
                case ControlOutput control -> controls.add(control);
                case HeliBodyOutput body -> bodies.add(body);
            }
        }

        Location target = solver.solve(entity().getLocation(), motion(), controls, bodies);
        entity().getEntity().teleport(target);
    }
}
