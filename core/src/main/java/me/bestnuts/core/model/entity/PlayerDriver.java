package me.bestnuts.core.model.entity;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.bestnuts.api.model.entity.Driver;
import me.bestnuts.api.model.vehicle.Vehicle;
import me.bestnuts.api.model.vehicle.component.VehicleSeat;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

@Setter
@RequiredArgsConstructor
public class PlayerDriver implements Driver {

    private final Player player;

    private Vehicle vehicle;
    private VehicleSeat seat;

    @Override
    public @NotNull LivingEntity getEntity() {
        return player;
    }

    @Override
    public @NotNull Location getLocation() {
        return player.getLocation();
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return player.getUniqueId();
    }

    @Override
    public @NotNull Optional<Vehicle> getSeatedVehicle() {
        return Optional.ofNullable(vehicle);
    }

    @Override
    public @NotNull Optional<VehicleSeat> getSeated() {
        return Optional.ofNullable(seat);
    }
}
