package me.bestnuts.api.model.vehicle.component;

import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@RequiredArgsConstructor
public class VehicleEntity {

    @NotNull
    private final Entity entity;

    public @NotNull Entity getEntity() {
        return entity;
    }

    public @NotNull Location getLocation() {
        return entity.getLocation();
    }

    public @NotNull UUID getUniqueId() {
        return entity.getUniqueId();
    }

    public boolean isValid() {
        return entity.isValid();
    }
}
