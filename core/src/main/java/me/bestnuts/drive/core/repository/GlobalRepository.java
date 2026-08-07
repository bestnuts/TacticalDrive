package me.bestnuts.drive.core.repository;

import lombok.Getter;
import me.bestnuts.drive.api.bukkit.register.SurfaceFrictionRegistry;
import me.bestnuts.drive.api.bukkit.register.VehicleFactoryRegistry;
import me.bestnuts.drive.api.bukkit.register.VehicleFunctionRegistry;
import me.bestnuts.drive.api.manager.AbstractDriverManager;
import me.bestnuts.drive.api.manager.AbstractVehicleManager;
import me.bestnuts.drive.core.manager.DriverManager;
import me.bestnuts.drive.core.manager.VehicleManager;
import me.bestnuts.drive.core.service.DriverService;
import me.bestnuts.drive.core.service.VehicleLookupService;
import me.bestnuts.drive.core.service.VehicleSeatService;
import me.bestnuts.drive.core.service.VehicleSpawnService;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@Getter
public final class GlobalRepository {

    private final VehicleFactoryRegistry factoryRegistry;
    private final VehicleFunctionRegistry functionRegistry;
    private final SurfaceFrictionRegistry frictionRegistry;

    private final AbstractDriverManager driverManager;
    private final AbstractVehicleManager vehicleManager;

    private final SharedRepository shared;
    private final CarRepository car;

    private final DriverService driverService;
    private final VehicleLookupService lookupService;
    private final VehicleSeatService seatService;
    private final VehicleSpawnService spawnService;

    public GlobalRepository(@NotNull JavaPlugin plugin) {
        factoryRegistry = new VehicleFactoryRegistry();
        functionRegistry = new VehicleFunctionRegistry();
        frictionRegistry = new SurfaceFrictionRegistry();

        driverManager = new DriverManager();
        vehicleManager = new VehicleManager();

        shared = new SharedRepository(functionRegistry);
        car = new CarRepository(shared, factoryRegistry, plugin.getDataFolder());

        driverService = new DriverService(driverManager);
        lookupService = new VehicleLookupService(vehicleManager, factoryRegistry);
        seatService = new VehicleSeatService(driverManager, lookupService);
        spawnService = new VehicleSpawnService(vehicleManager, seatService);
    }

    public void clear() {
        factoryRegistry.clear();
        functionRegistry.clear();
        frictionRegistry.clear();
        driverManager.clear();
        vehicleManager.clear();
    }
}
