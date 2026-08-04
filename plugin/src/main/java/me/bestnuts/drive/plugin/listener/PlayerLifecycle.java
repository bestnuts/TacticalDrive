package me.bestnuts.drive.plugin.listener;

import me.bestnuts.drive.core.service.DriverService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLifecycle implements Listener {

    private final DriverService driverService;

    public PlayerLifecycle(DriverService driverService) {
        this.driverService = driverService;
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        driverService.join(event.getPlayer());
    }

    @EventHandler
    public void quit(PlayerQuitEvent event) {
        driverService.quit(event.getPlayer());
    }
}
