package me.bestnuts.core.model.vehicle.component.function;

import me.bestnuts.api.bukkit.util.FunctionParamHelper;
import me.bestnuts.api.model.vehicle.Vehicle;
import me.bestnuts.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.api.model.vehicle.component.function.VehicleFunction;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class RotationFunction extends VehicleFunction {

    private final String link;

    private VehicleEntity pivot;

    public RotationFunction(@NotNull VehicleEntity parent, int delay, @NotNull Map<String, String> param) {
        super(parent, delay, param);
        this.link = param.getOrDefault("link", "root");
    }

    @Override
    public void execute(@NotNull Vehicle vehicle) {
        if (pivot == null) {
            pivot = FunctionParamHelper.getLink(link, vehicle);
        }
        Location location = getParent().getLocation();
        location.setRotation(pivot.getLocation().getRotation());
        getParent().getEntity().teleport(location);
    }
}
