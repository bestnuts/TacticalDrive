package me.bestnuts.core.model.vehicle;

import lombok.Getter;
import me.bestnuts.api.model.vehicle.component.VehicleGroup;
import me.bestnuts.core.model.vehicle.component.VehicleRoot;
import me.bestnuts.core.model.vehicle.configuration.CarConfiguration;
import org.jetbrains.annotations.NotNull;

@Getter
public class VehicleCar extends VehicleCore {

    public VehicleCar(@NotNull VehicleRoot entity, @NotNull VehicleGroup group, @NotNull CarConfiguration configuration) {
        super(entity, group, configuration);
    }

    @Override
    public @NotNull String type() {
        return RegistryType.CAR.getName();
    }

    @Override
    public void tick() {

    }
}
