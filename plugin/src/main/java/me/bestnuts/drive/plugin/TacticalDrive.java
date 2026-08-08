package me.bestnuts.drive.plugin;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.bestnuts.drive.api.bukkit.register.SurfaceFrictionRegistry;
import me.bestnuts.drive.api.bukkit.register.VehicleFunctionRegistry;
import me.bestnuts.drive.api.manager.VehicleFactory;
import me.bestnuts.drive.api.model.entity.Driver;
import me.bestnuts.drive.api.model.vehicle.Vehicle;
import me.bestnuts.drive.api.model.vehicle.data.DataKey;
import me.bestnuts.drive.api.model.vehicle.data.VehicleFactorySender;
import me.bestnuts.drive.core.model.vehicle.component.function.*;
import me.bestnuts.drive.core.repository.GlobalRepository;
import me.bestnuts.drive.core.service.VehicleSeatService;
import me.bestnuts.drive.plugin.command.Arguments;
import me.bestnuts.drive.plugin.command.CommandContext;
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
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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
        functionRegistry.register("heli-control", HeliControlFunction::new);
        functionRegistry.register("heli-body", HeliBodyFunction::new);
        functionRegistry.register("heli-rotor", HeliRotorFunction::new);
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
        return Commands.command("vehicle")
                .child(Commands.command("spawn")
                        .argument(Arguments.string("factory").suggests(this::suggestFactory))
                        .argument(Arguments.string("name").suggests(this::suggestVehicleName))
                        .playerExecute(this::spawnVehicle))
                .child(Commands.command("check")
                        .playerExecute(this::checkVehicle))
                .child(Commands.command("remove")
                        .argument(Arguments.string("uuid").suggests(this::suggestVehicleId))
                        .execute(this::removeVehicle));
    }

    private CompletableFuture<Suggestions> suggestFactory(CommandContext ctx, SuggestionsBuilder builder) {
        repository.getFactoryRegistry().names().forEach(builder::suggest);
        return builder.buildFuture();
    }

    private CompletableFuture<Suggestions> suggestVehicleName(CommandContext ctx, SuggestionsBuilder builder) {
        VehicleFactory factory = repository.getFactoryRegistry().find(ctx.get("factory"));
        if (factory != null) {
            factory.getConfigurationFactory().names().forEach(builder::suggest);
        }
        return builder.buildFuture();
    }

    private CompletableFuture<Suggestions> suggestVehicleId(CommandContext ctx, SuggestionsBuilder builder) {
        for (Vehicle vehicle : repository.getVehicleManager().getAll()) {
            builder.suggest(vehicle.entity().getUniqueId().toString());
        }
        return builder.buildFuture();
    }

    private void spawnVehicle(CommandContext ctx) {
        String factoryName = ctx.get("factory");
        String name = ctx.get("name");

        VehicleFactory factory = repository.getFactoryRegistry().find(factoryName);
        if (factory == null) {
            ctx.reply("알 수 없는 탈것 종류입니다. factory : " + factoryName);
            return;
        }

        Location location = ctx.player().getLocation();
        location.setPitch(0);

        Vehicle vehicle = repository.getSpawnService().spawn(factory, new VehicleFactorySender(location, name));
        if (vehicle == null) {
            ctx.reply("탈것 설정을 찾을 수 없습니다. name : " + name);
            return;
        }
        ctx.reply("탈것을 소환했습니다. uuid : " + vehicle.entity().getUniqueId());
    }

    private void checkVehicle(CommandContext ctx) {
        repository.getDriverManager().find(ctx.player().getUniqueId())
                .flatMap(Driver::getSeatedVehicle)
                .ifPresentOrElse(
                        vehicle -> ctx.reply("탑승 중인 탈것 uuid : " + vehicle.entity().getUniqueId()),
                        () -> ctx.reply("탑승 중인 탈것이 없습니다.")
                );
    }

    private void removeVehicle(CommandContext ctx) {
        String raw = ctx.get("uuid");
        UUID id;
        try {
            id = UUID.fromString(raw);
        } catch (IllegalArgumentException exception) {
            ctx.reply("uuid 형식이 올바르지 않습니다. uuid : " + raw);
            return;
        }

        repository.getVehicleManager().find(id).ifPresentOrElse(vehicle -> {
            repository.getSpawnService().despawn(vehicle);
            ctx.reply("탈것을 제거했습니다. uuid : " + id);
        }, () -> ctx.reply("해당 uuid의 탈것을 찾을 수 없습니다. uuid : " + id));
    }
}
