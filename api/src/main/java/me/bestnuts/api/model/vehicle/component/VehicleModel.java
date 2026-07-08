package me.bestnuts.api.model.vehicle.component;

import me.bestnuts.api.manager.BoneType;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@BoneType("model")
public abstract class VehicleModel extends VehicleBone implements VehicleEntity {

    private final Entity entity;

    public VehicleModel(@NotNull VehicleGroup group, @NotNull Entity entity) {
        super(group);
        this.entity = entity;
    }

    @Override
    public @NotNull Entity getEntity() {
        return entity;
    }

    @Override
    public @NotNull Location getLocation() {
        return entity.getLocation();
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return entity.getUniqueId();
    }

    @Override
    public boolean isValid() {
        return entity.isValid();
    }
}
