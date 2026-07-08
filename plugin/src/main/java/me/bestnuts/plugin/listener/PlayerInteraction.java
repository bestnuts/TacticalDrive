package me.bestnuts.plugin.listener;

import me.bestnuts.api.manager.VehicleManager;
import me.bestnuts.api.model.vehicle.dto.DataKey;
import me.bestnuts.core.manager.DriverManager;
import me.bestnuts.core.repository.GlobalRepository;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class PlayerInteraction implements Listener {

    private final DriverManager driverManager;
    private final VehicleManager vehicleManager;

    public PlayerInteraction(GlobalRepository repository) {
        driverManager = repository.getDriverManager();
        vehicleManager = repository.getVehicleManager();
    }

    @EventHandler
    public void interactSeat(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        driverManager.find(player.getUniqueId()).ifPresent(driver -> {
            if (!entity.getPersistentDataContainer().has(DataKey.VEHICLE_ROOT_ID.key())) return;
            String id = entity.getPersistentDataContainer().get(DataKey.VEHICLE_ROOT_ID.key(), PersistentDataType.STRING);
            if (id == null || id.isEmpty()) return;
            vehicleManager.find(UUID.fromString(id)).ifPresent(vehicle -> {
                    player.sendMessage("인터렉션 감지");
            });
        });
    }
}
