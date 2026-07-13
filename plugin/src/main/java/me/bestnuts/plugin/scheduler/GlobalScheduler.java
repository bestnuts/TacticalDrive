package me.bestnuts.plugin.scheduler;

import me.bestnuts.api.manager.VehicleManager;
import me.bestnuts.api.model.vehicle.Vehicle;
import me.bestnuts.core.repository.GlobalRepository;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Iterator;

public final class GlobalScheduler {

    private final BukkitTask tickTask;
    private final VehicleManager vehicleManager;

    public GlobalScheduler(JavaPlugin plugin, GlobalRepository repository) {
        this.vehicleManager = repository.getVehicleManager();
        this.tickTask = Bukkit.getScheduler().runTaskTimer(plugin, this::runTick, 1L, 1L);
    }

    public void runTick() {
        Iterator<Vehicle> iterator = vehicleManager.getAll().iterator();
        while (iterator.hasNext()) {
            Vehicle vehicle = iterator.next();
            vehicle.tick();
        }
    }

    public void disable() {
        tickTask.cancel();
    }
}
