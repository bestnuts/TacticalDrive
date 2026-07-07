package me.bestnuts.api.model.vehicle.component;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface VehicleEntity {

    @NotNull
    Entity getEntity();

    @NotNull
    Location getLocation();

    @NotNull
    UUID getUniqueId();

    boolean isValid();
}
