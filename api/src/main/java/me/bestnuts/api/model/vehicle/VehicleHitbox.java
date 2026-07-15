package me.bestnuts.api.model.vehicle;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class VehicleHitbox {

    private final Vector size;
    private Vector min;
    private Vector max;

    public VehicleHitbox(@NotNull Vector size) {
        this.size = size.clone();
        this.min = new Vector(0, 0, 0);
        this.max = new Vector(0, 0, 0);
    }

    public void update(@NotNull Location bodyLocation) {
        Vector center = bodyLocation.toVector();
        Vector halfSize = this.size.clone().multiply(0.5);

        this.min = center.clone().subtract(halfSize);
        this.max = center.clone().add(halfSize);
    }

    public boolean intersects(@NotNull VehicleHitbox other) {
        return (this.min.getX() <= other.max.getX() && this.max.getX() >= other.min.getX()) &&
                (this.min.getY() <= other.max.getY() && this.max.getY() >= other.min.getY()) &&
                (this.min.getZ() <= other.max.getZ() && this.max.getZ() >= other.min.getZ());
    }

    public boolean contains(@NotNull Vector point) {
        return (point.getX() >= this.min.getX() && point.getX() <= this.max.getX()) &&
                (point.getY() >= this.min.getY() && point.getY() <= this.max.getY()) &&
                (point.getZ() >= this.min.getZ() && point.getZ() <= this.max.getZ());
    }

    public @NotNull VehicleHitbox calculateExpandedBox(@NotNull Vector moveVelocity) {
        VehicleHitbox expanded = new VehicleHitbox(this.size);

        expanded.min.setX(Math.min(this.min.getX(), this.min.getX() + moveVelocity.getX()));
        expanded.min.setY(Math.min(this.min.getY(), this.min.getY() + moveVelocity.getY()));
        expanded.min.setZ(Math.min(this.min.getZ(), this.min.getZ() + moveVelocity.getZ()));

        expanded.max.setX(Math.max(this.max.getX(), this.max.getX() + moveVelocity.getX()));
        expanded.max.setY(Math.max(this.max.getY(), this.max.getY() + moveVelocity.getY()));
        expanded.max.setZ(Math.max(this.max.getZ(), this.max.getZ() + moveVelocity.getZ()));

        return expanded;
    }

    public Vector getSize() {
        return this.size.clone();
    }

    public Vector getMin() {
        return this.min.clone();
    }

    public Vector getMax() {
        return this.max.clone();
    }
}

