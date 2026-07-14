package me.bestnuts.api.bukkit.util;

import me.bestnuts.api.model.vehicle.Vehicle;
import me.bestnuts.api.model.vehicle.component.bone.VehicleEntity;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class FunctionParamHelper {

    public static @NotNull NamespacedKey getNamespacedKey(@NotNull String input) {
        String[] content = input.split(";");
        if (content.length < 2) {
            return new NamespacedKey(NamespacedKey.MINECRAFT_NAMESPACE, "");
        }
        return new NamespacedKey(content[0], content[1]);
    }

    public static @NotNull Vector getVector(@NotNull String input, @NotNull Vector def) {
        String[] content = input.split(";");
         if (content.length < 3) {
             return def;
         }
         return new Vector(Double.parseDouble(content[0]), Double.parseDouble(content[1]), Double.parseDouble(content[2]));
    }

    public static @NotNull VehicleEntity getLink(@NotNull String path, @NotNull Vehicle vehicle) {
        Optional<? extends VehicleEntity> optional = vehicle.findBoneByPath(path);
        if (optional.isEmpty()) {
            return vehicle.entity();
        } else {
            return optional.get();
        }
    }

    public static @NotNull Vector rotateVectorByDirection(Location location, Vector local) {
        Vector forward = location.getDirection().normalize();
        Vector up = new Vector(0, 1, 0);
        Vector right = forward.clone().crossProduct(up).normalize();

        Vector result = new Vector(0, 0, 0);
        result.add(right.clone().multiply(-local.getX()));
        result.add(up.clone().multiply(local.getY()));
        result.add(forward.clone().multiply(local.getZ()));

        return result;
    }
}
