package me.bestnuts.drive.core.service;

import lombok.RequiredArgsConstructor;
import me.bestnuts.drive.api.bukkit.util.DataKeyHelper;
import me.bestnuts.drive.api.manager.AbstractDriverManager;
import me.bestnuts.drive.api.model.entity.Driver;
import me.bestnuts.drive.api.model.vehicle.Vehicle;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleSeat;
import me.bestnuts.drive.api.model.vehicle.data.DataKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public final class VehicleSeatService {

    private final AbstractDriverManager driverManager;
    private final VehicleLookupService lookupService;

    public void request(@NotNull Player player, @NotNull Entity entity) {
        Optional<Driver> optional = driverManager.find(player.getUniqueId());
        if (optional.isEmpty() || optional.get().getSeated().isPresent()) return;
        if (lookupService.findOrRestore(entity).isEmpty()) return;
        DataKeyHelper.set(entity, DataKey.VEHICLE_INTERACT_ID, player.getUniqueId().toString());
    }

    public void release(@NotNull Player player, @NotNull Entity entity) {
        Optional<Driver> optional = driverManager.find(player.getUniqueId());
        if (optional.isEmpty() || optional.get().getSeated().isEmpty()) return;
        if (lookupService.findOrRestore(entity).isEmpty()) return;
        Driver driver = optional.get();
        driver.getSeated().ifPresent(seat -> seat.setDriver(null));
        driver.setSeatedVehicle(null);
        driver.setSeatedVehicleSeat(null);
    }

    public void mount(@NotNull UUID driverId, @NotNull Vehicle vehicle, @NotNull VehicleSeat seat) {
        driverManager.find(driverId).ifPresent(driver -> {
            seat.setDriver(driver);
            driver.setSeatedVehicle(vehicle);
            driver.setSeatedVehicleSeat(seat);
        });
    }
}
