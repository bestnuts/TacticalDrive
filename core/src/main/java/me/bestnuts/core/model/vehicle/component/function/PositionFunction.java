package me.bestnuts.core.model.vehicle.component.function;

import me.bestnuts.api.bukkit.util.FunctionParamHelper;
import me.bestnuts.api.model.vehicle.Vehicle;
import me.bestnuts.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.api.model.vehicle.component.function.VehicleFunction;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public final class PositionFunction extends VehicleFunction {

    private final Vector world;
    private final Vector local;
    private final String link;

    private VehicleEntity pivot;

    public PositionFunction(@NotNull VehicleEntity parent, int delay, @NotNull Map<String, String> param) {
        super(parent, delay, param);
        this.world = FunctionParamHelper.getVector(param.get("world"), new Vector());
        this.local = FunctionParamHelper.getVector(param.get("local"), new Vector());
        this.link = param.getOrDefault("link", "root");
    }

    @Override
    public void execute(@NotNull Vehicle vehicle) {
        if (pivot == null) {
            Optional<? extends VehicleEntity> optional = vehicle.findBoneByPath(link);
            if (optional.isEmpty()) {
                pivot = vehicle.entity();
            } else {
                pivot = optional.get();
            }
        }
        Location location = pivot.getLocation().clone();
        location.add(world);

        double yaw = Math.toRadians(location.getYaw());
        double pitch = Math.toRadians(location.getPitch());
        double cosY = Math.cos(yaw);
        double sinY = Math.sin(yaw);
        double cosP = Math.cos(pitch);
        double sinP = Math.sin(pitch);

        double x = (local.getX() * cosY) + (local.getY() * sinY * sinP) + (local.getZ() * sinY * cosP);
        double y = (local.getY() * cosP) - (local.getZ() * sinP);
        double z = -((local.getX() * sinY) + (local.getY() * cosY * sinP) + (local.getZ() * cosY * cosP));

        location.add(x, y, z);
        getParent().getEntity().teleport(location);
    }
}
