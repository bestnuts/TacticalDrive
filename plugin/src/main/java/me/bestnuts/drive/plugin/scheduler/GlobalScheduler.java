package me.bestnuts.drive.plugin.scheduler;

import me.bestnuts.drive.api.manager.VehicleManager;
import me.bestnuts.drive.api.model.entity.Driver;
import me.bestnuts.drive.api.model.vehicle.Vehicle;
import me.bestnuts.drive.core.manager.DriverManager;
import me.bestnuts.drive.core.repository.GlobalRepository;
import org.bukkit.Bukkit;
import org.bukkit.Input;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Iterator;

public final class GlobalScheduler {

    private final BukkitTask tickTask;
    private final VehicleManager vehicleManager;
    private final DriverManager driverManager;

    public GlobalScheduler(JavaPlugin plugin, GlobalRepository repository) {
        this.vehicleManager = repository.getVehicleManager();
        this.driverManager = repository.getDriverManager();
        this.tickTask = Bukkit.getScheduler().runTaskTimer(plugin, this::runTick, 1L, 1L);
    }

    public void runTick() {
        Iterator<Vehicle> iterator = vehicleManager.getAll().iterator();
        while (iterator.hasNext()) {
            Vehicle vehicle = iterator.next();
            vehicle.tick();
        }

        for (Driver driver : driverManager.getAll()) {
            Input input = ((Player) driver.getEntity()).getCurrentInput();
            boolean isW = input.isForward();
            boolean isS = input.isBackward();
            boolean isA = input.isLeft();
            boolean isD = input.isRight();
            float sideway = isA && !isD ? -1 : !isA && isD ? 1 : 0;
            float forward = isW && !isS ? 1 : !isW && isS ? -1 : 0;
            driver.getInput().setSideway(sideway);
            driver.getInput().setForward(forward);
        }
    }

    public void disable() {
        tickTask.cancel();
    }
}
