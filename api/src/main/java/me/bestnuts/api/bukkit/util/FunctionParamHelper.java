package me.bestnuts.api.bukkit.util;

import me.bestnuts.api.model.vehicle.Vehicle;
import me.bestnuts.api.model.vehicle.component.bone.VehicleEntity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class FunctionParamHelper {

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
}
