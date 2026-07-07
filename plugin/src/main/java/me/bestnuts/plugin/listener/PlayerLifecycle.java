package me.bestnuts.plugin.listener;

import me.bestnuts.api.manager.DriverManager;
import me.bestnuts.api.model.entity.Driver;
import me.bestnuts.core.model.entity.PlayerDriver;
import me.bestnuts.core.repository.GlobalRepository;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerLifecycle implements Listener {

    private final DriverManager driverManager;

    public PlayerLifecycle(GlobalRepository repository) {
        this.driverManager = repository.getDriverManager();
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        Driver driver = new PlayerDriver(event.getPlayer());
        driverManager.register(driver);
    }

    @EventHandler
    public void quit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID id = player.getUniqueId();
        driverManager.unregister(id);
    }
}
