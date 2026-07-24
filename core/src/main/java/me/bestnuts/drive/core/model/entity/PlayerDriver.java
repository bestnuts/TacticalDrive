package me.bestnuts.drive.core.model.entity;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.bestnuts.drive.api.model.entity.Driver;
import me.bestnuts.drive.api.model.entity.component.InputProvider;
import me.bestnuts.drive.api.model.vehicle.Vehicle;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleSeat;
import me.bestnuts.drive.core.model.entity.component.PlayerInputProvider;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

@Setter
@RequiredArgsConstructor
public final class PlayerDriver implements Driver {

    private final Player player;
    private final InputProvider input = new PlayerInputProvider();

    private Vehicle vehicle;
    private VehicleSeat seat;

    @Override
    public @NotNull LivingEntity getEntity() {
        return player;
    }

    @Override
    public @NotNull InputProvider getInput() {
        return input;
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
    public void setSeatedVehicle(@Nullable Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public @NotNull Optional<VehicleSeat> getSeated() {
        return Optional.ofNullable(seat);
    }

    @Override
    public void setSeatedVehicleSeat(@Nullable VehicleSeat seat) {
        this.seat = seat;
    }
}
