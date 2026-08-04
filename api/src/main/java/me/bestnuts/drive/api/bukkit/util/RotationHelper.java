package me.bestnuts.drive.api.bukkit.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class RotationHelper {

    private RotationHelper() {}

    public static @NotNull Quaternionf tilt(double pitch, double roll) {
        return new Quaternionf().rotationX((float) Math.toRadians(-pitch))
                .mul(new Quaternionf().rotationZ((float) Math.toRadians(-roll)));
    }

    public static @NotNull Quaternionf orientation(float yaw, double pitch, double roll) {
        return new Quaternionf().rotationY((float) Math.toRadians(-yaw)).mul(tilt(pitch, roll));
    }

    public static @NotNull Vector rotate(@NotNull Quaternionf rotation, @NotNull Vector local) {
        Vector3f result = rotation.transform(new Vector3f((float) local.getX(), (float) local.getY(), (float) local.getZ()));
        return new Vector(result.x(), result.y(), result.z());
    }

    public static @NotNull Vector inverseYaw(float yaw, @NotNull Vector world) {
        double yawRad = Math.toRadians(-yaw);
        double cos = Math.cos(yawRad);
        double sin = Math.sin(yawRad);
        return new Vector(
                world.getX() * cos - world.getZ() * sin,
                world.getY(),
                world.getX() * sin + world.getZ() * cos
        );
    }

    public static @NotNull Vector rotateByYaw(@NotNull Location location, @NotNull Vector local) {
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
