package me.bestnuts.drive.core.service;

import lombok.RequiredArgsConstructor;
import me.bestnuts.drive.api.manager.AbstractDriverManager;
import me.bestnuts.drive.api.model.entity.Driver;
import me.bestnuts.drive.core.model.entity.PlayerDriver;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class DriverService {

    private final AbstractDriverManager driverManager;

    public void join(@NotNull Player player) {
        Driver driver = new PlayerDriver(player);
        driverManager.register(driver);
    }

    public void quit(@NotNull Player player) {
        driverManager.unregister(player.getUniqueId());
    }
}
