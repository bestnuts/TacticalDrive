package me.bestnuts.api.model.vehicle.component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public abstract class VehicleBone implements VehicleEntity {

    @NotNull private final VehicleGroup group;
    @NotNull private final Entity entity;

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
