package me.bestnuts.plugin;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.bestnuts.api.bukkit.register.PluginProvider;
import me.bestnuts.api.bukkit.register.VehicleFactoryHook;
import me.bestnuts.api.manager.VehicleFactory;
import me.bestnuts.api.model.vehicle.data.VehicleFactorySender;
import me.bestnuts.core.manager.VehicleService;
import me.bestnuts.core.repository.GlobalRepository;
import me.bestnuts.plugin.command.Arguments;
import me.bestnuts.plugin.command.CommandNode;
import me.bestnuts.plugin.command.Commands;
import me.bestnuts.plugin.listener.PlayerInteractVehicle;
import me.bestnuts.plugin.listener.PlayerLifecycle;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class TacticalDrive extends JavaPlugin {

    private GlobalRepository repository;
    private VehicleService service;

    @Override
    public void onEnable() {
        PluginProvider.initialize(this);
        repository = new GlobalRepository(this);
        service = new VehicleService(repository.getVehicleManager());
        register();
    }

    @Override
    public void onDisable() {
        service = null;
        repository = null;
    }

    private void register() {
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

                                    VehicleFactory factory = VehicleFactoryHook.getHooks(type);
                                    if (factory == null) return;
                                    this.service.spawn(factory, new VehicleFactorySender(ctx.player().getLocation(), name));
                                })
                );
    }
}
