package me.bestnuts.plugin.listener;

import me.bestnuts.core.manager.DriverManager;
import me.bestnuts.core.repository.GlobalRepository;
import org.bukkit.Input;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInputEvent;

public class PlayerInput implements Listener {

    private final DriverManager driverManager;

    public PlayerInput(GlobalRepository repository) {
        driverManager = repository.getDriverManager();
    }

    @EventHandler
    public void onPlayerInput(PlayerInputEvent event) {
        Input input = event.getInput();

        boolean isW = input.isForward();
        boolean isS = input.isBackward();
        boolean isA = input.isLeft();
        boolean isD = input.isRight();

        float sideway = isA && !isD ? -1 : !isA && isD ? 1 : 0;
        float forward = isW && !isS ? 1 : !isW && isS ? -1 : 0;
        driverManager.find(event.getPlayer().getUniqueId()).ifPresent(
                driver -> {
                    driver.getInput().setSideway(sideway);
                    driver.getInput().setForward(forward);
                }
        );
    }
}
