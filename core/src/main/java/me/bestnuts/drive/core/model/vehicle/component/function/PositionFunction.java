package me.bestnuts.drive.core.model.vehicle.component.function;

import me.bestnuts.drive.api.bukkit.util.FunctionParamHelper;
import me.bestnuts.drive.api.model.vehicle.Vehicle;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.drive.api.model.vehicle.component.function.VehicleFunction;
import me.bestnuts.drive.api.model.vehicle.data.VehicleOutput;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class PositionFunction extends VehicleFunction {

    private final Vector world;
    private final Vector local;
    private final String link;

    private VehicleEntity pivot;

    public PositionFunction(@NotNull VehicleEntity parent, int delay, @NotNull Map<String, String> param) {
        super(parent, delay, param);
        this.world = FunctionParamHelper.getVector(param.getOrDefault("world", "0;0;0"), new Vector());
        this.local = FunctionParamHelper.getVector(param.getOrDefault("local", "0;0;0"), new Vector());
        this.link = param.getOrDefault("link", "root");
    }

    @Override
    public @Nullable VehicleOutput execute(@NotNull Vehicle vehicle) {
        if (pivot == null) {
            pivot = FunctionParamHelper.getLink(link, vehicle);
        }
        Location location = pivot.getLocation().clone();
        location.add(world);

        Vector offset = FunctionParamHelper.rotateVectorByDirection(location, local);

        location.add(offset);
        location.setRotation(getParent().getLocation().getRotation());
        getParent().getEntity().teleport(location);
        return null;
    }
}
