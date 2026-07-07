package me.bestnuts.core.repository;

import lombok.Getter;
import me.bestnuts.core.manager.DriverManager;
import me.bestnuts.core.manager.VehicleManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@Getter
public final class GlobalRepository {

    private final DriverManager driverManager;
    private final VehicleManager vehicleManager;

    private final SharedRepository shared;
    private final CarRepository car;

    public GlobalRepository(@NotNull JavaPlugin plugin) {
        driverManager = new DriverManager();
        vehicleManager = new VehicleManager();
        shared = new SharedRepository();
        car = new CarRepository(shared, plugin.getDataFolder());
    }
}
