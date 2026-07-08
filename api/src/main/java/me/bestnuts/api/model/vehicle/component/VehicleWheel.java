package me.bestnuts.api.model.vehicle.component;

import me.bestnuts.api.manager.BoneType;
import me.bestnuts.api.model.vehicle.dto.WheelContact;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@BoneType("wheel")
public abstract class VehicleWheel extends VehicleBone implements VehicleEntity {

    private final Entity entity;

    public VehicleWheel(@NotNull VehicleGroup group, @NotNull Entity entity) {
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

    public abstract WheelContact contact();
}
