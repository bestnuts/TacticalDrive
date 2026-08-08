package me.bestnuts.drive.plugin.listener;

import me.bestnuts.drive.core.service.VehicleSeatService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractVehicle implements Listener {

    private final VehicleSeatService seatService;

    public PlayerInteractVehicle(VehicleSeatService seatService) {
        this.seatService = seatService;
    }

    @EventHandler
    public void dismountSeat(EntityDismountEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        seatService.release(player);
    }

    @EventHandler
    public void interactSeat(PlayerInteractEntityEvent event) {
        seatService.request(event.getPlayer(), event.getRightClicked());
    }
}
