package me.bestnuts.drive.plugin;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.bestnuts.drive.api.bukkit.register.ManagerHook;
import me.bestnuts.drive.api.bukkit.register.PluginProvider;
import me.bestnuts.drive.api.bukkit.register.VehicleFactoryHook;
import me.bestnuts.drive.api.bukkit.register.VehicleFunctionHook;
import me.bestnuts.drive.api.manager.VehicleFactory;
import me.bestnuts.drive.api.model.vehicle.component.function.DisplayTranslationFunction;
import me.bestnuts.drive.api.model.vehicle.component.function.InteractionSeatFunction;
import me.bestnuts.drive.api.model.vehicle.component.function.ItemDisplayFunction;
import me.bestnuts.drive.api.model.vehicle.data.VehicleFactorySender;
import me.bestnuts.drive.core.manager.VehicleService;
import me.bestnuts.drive.core.model.vehicle.component.function.*;
import me.bestnuts.drive.core.repository.GlobalRepository;
import me.bestnuts.drive.plugin.command.Arguments;
import me.bestnuts.drive.plugin.command.CommandNode;
import me.bestnuts.drive.plugin.command.Commands;
import me.bestnuts.drive.plugin.listener.PlayerInteractVehicle;
import me.bestnuts.drive.plugin.listener.PlayerLifecycle;
import me.bestnuts.drive.plugin.scheduler.GlobalScheduler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class TacticalDrive extends JavaPlugin {

    private GlobalRepository repository;
    private VehicleService service;
    private GlobalScheduler scheduler;

    @Override
    public void onEnable() {
        PluginProvider.initialize(this);
        repository = new GlobalRepository(this);
        service = new VehicleService(repository.getVehicleManager());
        register();
        scheduler = new GlobalScheduler(this, repository);
    }

    @Override
    public void onDisable() {
        scheduler.disable();
        scheduler = null;
        service = null;
        repository = null;
    }

    private void register() {
        VehicleFunctionHook.registerHook("item-display", ItemDisplayFunction::new);
        VehicleFunctionHook.registerHook("display-translation", DisplayTranslationFunction::new);
        VehicleFunctionHook.registerHook("position", PositionFunction::new);
        VehicleFunctionHook.registerHook("rotation", RotationFunction::new);
        VehicleFunctionHook.registerHook("interaction-seat", InteractionSeatFunction::new);
        VehicleFunctionHook.registerHook("car-wheel", CarWheelFunction::new);
        VehicleFunctionHook.registerHook("car-suspension", CarSuspensionFunction::new);
        VehicleFunctionHook.registerHook("car-body", CarBodyFunction::new);

        ManagerHook.register(repository.getVehicleManager());
        ManagerHook.register(repository.getDriverManager());

        Bukkit.getPluginManager().registerEvents(new PlayerInteractVehicle(repository), this);
        Bukkit.getPluginManager().registerEvents(new PlayerLifecycle(repository), this);

        final LifecycleEventManager<@NotNull Plugin> lifecycleManager = this.getLifecycleManager();
        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS, (event) -> {
            event.registrar().register(command().build().build(), "탈것 명령어", List.of("탈것", "xkfrjt"));
        });
    }

    private CommandNode command() {
        return Commands.command("vehicle")
                .child(
                        Commands.command("spawn")
                                .argument(Arguments.string("type").suggests(VehicleFactoryHook.hookKeySet().toArray(new String[0])))
                                .argument(Arguments.string("name"))
                                .playerExecute(ctx -> {

                                    String type = ctx.get("type");
                                    String name = ctx.get("name");

                                    VehicleFactory factory = VehicleFactoryHook.getHook(type);
                                    if (factory == null) return;
                                    Location location = ctx.player().getLocation();
                                    location.setPitch(0);
                                    this.service.spawn(factory, new VehicleFactorySender(location, name));
                                })
                );
    }
}
