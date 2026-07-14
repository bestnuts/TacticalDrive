package me.bestnuts.core.model.vehicle.component.function;

import me.bestnuts.api.bukkit.util.FunctionParamHelper;
import me.bestnuts.api.model.vehicle.Vehicle;
import me.bestnuts.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.api.model.vehicle.component.function.VehicleFunction;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

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
    public void execute(@NotNull Vehicle vehicle) {
        if (pivot == null) {
            pivot = FunctionParamHelper.getLink(link, vehicle);
        }
        Location location = pivot.getLocation().clone();
        location.add(world);

        Vector forward = location.getDirection().normalize();
        Vector up = new Vector(0, 1, 0);
        Vector right = forward.clone().crossProduct(up).normalize();

        Vector offset = new Vector(0, 0, 0);
        offset.add(right.clone().multiply(-local.getX()));
        offset.add(up.clone().multiply(local.getY()));
        offset.add(forward.clone().multiply(local.getZ()));

        location.add(offset);
        location.setRotation(getParent().getLocation().getRotation());
        getParent().getEntity().teleport(location);
    }
}
