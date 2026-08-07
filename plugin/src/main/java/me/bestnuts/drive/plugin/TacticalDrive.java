package me.bestnuts.drive.plugin;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.bestnuts.drive.api.bukkit.register.SurfaceFrictionRegistry;
import me.bestnuts.drive.api.bukkit.register.VehicleFactoryRegistry;
import me.bestnuts.drive.api.bukkit.register.VehicleFunctionRegistry;
import me.bestnuts.drive.api.manager.VehicleFactory;
import me.bestnuts.drive.api.model.vehicle.data.DataKey;
import me.bestnuts.drive.api.model.vehicle.data.VehicleFactorySender;
import me.bestnuts.drive.core.model.vehicle.component.function.*;
import me.bestnuts.drive.core.repository.GlobalRepository;
import me.bestnuts.drive.core.service.VehicleSeatService;
import me.bestnuts.drive.plugin.command.Arguments;
import me.bestnuts.drive.plugin.command.CommandNode;
import me.bestnuts.drive.plugin.command.Commands;
import me.bestnuts.drive.plugin.listener.PlayerInteractVehicle;
import me.bestnuts.drive.plugin.listener.PlayerLifecycle;
import me.bestnuts.drive.plugin.scheduler.GlobalScheduler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class TacticalDrive extends JavaPlugin {

    private GlobalRepository repository;
    private GlobalScheduler scheduler;

    @Override
    public void onEnable() {
        DataKey.initialize(this);
        saveDefaultConfig();
        repository = new GlobalRepository(this);
        loadFriction();
        register();
        scheduler = new GlobalScheduler(this, repository);
    }

    private void loadFriction() {
        SurfaceFrictionRegistry frictionRegistry = repository.getFrictionRegistry();
        frictionRegistry.setDefaultFriction(getConfig().getDouble("friction.default", 1.0));

        ConfigurationSection section = getConfig().getConfigurationSection("friction.blocks");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            Material material = Material.matchMaterial(key);
            if (material == null) {
                getLogger().warning("알 수 없는 블록이라 마찰 설정을 건너뜁니다. block : " + key);
                continue;
            }
            frictionRegistry.register(material, section.getDouble(key));
        }
    }

    @Override
    public void onDisable() {
        scheduler.disable();
        repository.clear();
        scheduler = null;
        repository = null;
    }

    private void register() {
        VehicleFunctionRegistry functionRegistry = repository.getFunctionRegistry();
        VehicleSeatService seatService = repository.getSeatService();

        functionRegistry.register("item-display", ItemDisplayFunction::new);
        functionRegistry.register("display-translation", DisplayTranslationFunction::new);
        functionRegistry.register("position", PositionFunction::new);
        functionRegistry.register("rotation", RotationFunction::new);
        functionRegistry.register("interaction-seat", (parent, delay, param) -> new InteractionSeatFunction(seatService, parent, delay, param));
        functionRegistry.register("car-wheel", CarWheelFunction::new);
        functionRegistry.register("car-suspension", (parent, delay, param) ->
                new CarSuspensionFunction(repository.getFrictionRegistry(), parent, delay, param));
        functionRegistry.register("car-body", (parent, delay, param) ->
                new CarBodyFunction(repository.getVehicleManager(), parent, delay, param));

        Bukkit.getPluginManager().registerEvents(new PlayerInteractVehicle(seatService), this);
        Bukkit.getPluginManager().registerEvents(new PlayerLifecycle(repository.getDriverService()), this);

        final LifecycleEventManager<@NotNull Plugin> lifecycleManager = this.getLifecycleManager();
        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS, (event) -> {
            event.registrar().register(command().build().build(), "탈것 명령어", List.of("탈것", "xkfrjt"));
        });
    }

    private CommandNode command() {
        VehicleFactoryRegistry factoryRegistry = repository.getFactoryRegistry();
        return Commands.command("vehicle")
                .child(
                        Commands.command("spawn")
                                .argument(Arguments.string("type").suggests(factoryRegistry.names().toArray(new String[0])))
                                .argument(Arguments.string("name"))
                                .playerExecute(ctx -> {

                                    String type = ctx.get("type");
                                    String name = ctx.get("name");

                                    VehicleFactory factory = factoryRegistry.find(type);
                                    if (factory == null) return;
                                    Location location = ctx.player().getLocation();
                                    location.setPitch(0);
                                    repository.getSpawnService().spawn(factory, new VehicleFactorySender(location, name));
                                })
                );
    }
}
