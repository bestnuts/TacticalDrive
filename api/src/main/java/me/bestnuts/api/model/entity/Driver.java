package me.bestnuts.api.model.entity;

import me.bestnuts.api.model.vehicle.Vehicle;
import me.bestnuts.api.model.vehicle.component.VehicleSeat;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface Driver {

    @NotNull
    LivingEntity getEntity();

    @NotNull
    Location getLocation();

    @NotNull
    UUID getUniqueId();

    @NotNull
    Optional<Vehicle> getSeatedVehicle();

    @NotNull
    Optional<VehicleSeat> getSeated();
}
