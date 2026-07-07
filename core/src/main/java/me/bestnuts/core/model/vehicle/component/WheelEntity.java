package me.bestnuts.core.model.vehicle.component;

import me.bestnuts.api.model.vehicle.component.VehicleEntity;
import me.bestnuts.api.model.vehicle.component.VehicleGroup;
import me.bestnuts.api.model.vehicle.component.VehicleWheel;
import me.bestnuts.api.model.vehicle.dto.WheelContact;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class WheelEntity extends VehicleWheel implements VehicleEntity {

    private final Entity entity;

    public WheelEntity(VehicleGroup group, Entity entity) {
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
        return true;
    }

    @Override
    public WheelContact contact() {
        return null;
    }
}
