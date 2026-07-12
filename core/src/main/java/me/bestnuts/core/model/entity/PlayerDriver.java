package me.bestnuts.core.model.entity;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.bestnuts.api.model.entity.Driver;
import me.bestnuts.api.model.vehicle.Vehicle;
import me.bestnuts.api.model.vehicle.component.bone.VehicleSeat;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

@Setter
@RequiredArgsConstructor
public final class PlayerDriver implements Driver {

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
    public void setSeatedVehicle(@NotNull Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public @NotNull Optional<VehicleSeat> getSeated() {
        return Optional.ofNullable(seat);
    }

    @Override
    public void setSeatedVehicleSeat(@NotNull VehicleSeat seat) {
        this.seat = seat;
    }
}
