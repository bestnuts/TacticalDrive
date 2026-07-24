package me.bestnuts.drive.plugin.listener;

import me.bestnuts.drive.api.bukkit.register.VehicleFactoryHook;
import me.bestnuts.drive.api.bukkit.util.DataKeyHelper;
import me.bestnuts.drive.api.manager.VehicleFactory;
import me.bestnuts.drive.api.manager.VehicleManager;
import me.bestnuts.drive.api.model.entity.Driver;
import me.bestnuts.drive.api.model.vehicle.Vehicle;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleBone;
import me.bestnuts.drive.api.model.vehicle.data.DataKey;
import me.bestnuts.drive.core.manager.DriverManager;
import me.bestnuts.drive.core.repository.GlobalRepository;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class PlayerInteractVehicle implements Listener {

    private final DriverManager driverManager;
    private final VehicleManager vehicleManager;

    public PlayerInteractVehicle(GlobalRepository repository) {
        driverManager = repository.getDriverManager();
        vehicleManager = repository.getVehicleManager();
    }

    @EventHandler
    public void dismountSeat(EntityDismountEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        Entity entity = event.getDismounted();
        consumeVehicle(player, entity, (driver) -> driver.getSeated().isPresent(), (driver, vehicle) -> {
            driver.getSeated().ifPresent(seat -> seat.setDriver(null));
            driver.setSeatedVehicle(null);
            driver.setSeatedVehicleSeat(null);
        });
    }

    @EventHandler
    public void interactSeat(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        consumeVehicle(player, entity, (driver) -> driver.getSeated().isEmpty(), (driver, vehicle) -> {
            DataKeyHelper.set(entity, DataKey.VEHICLE_INTERACT_ID, player.getUniqueId().toString());
        });
    }

    private void findBoneById(UUID id, Vehicle vehicle, Driver driver, BiConsumer<Driver, VehicleBone> consumer) {
        vehicle.findBoneById(id).ifPresent(bone -> consumer.accept(driver, bone));
    }

    private void consumeVehicle(Player player, Entity entity, Predicate<Driver> predicate, BiConsumer<Driver, Vehicle> consumer) {
        driverManager.find(player.getUniqueId()).ifPresent(driver -> {
            if (!predicate.test(driver)) return;
            if (!entity.getPersistentDataContainer().has(DataKey.VEHICLE_ROOT_ID.key())) return;
            String id = entity.getPersistentDataContainer().get(DataKey.VEHICLE_ROOT_ID.key(), PersistentDataType.STRING);
            if (id == null || id.isEmpty()) return;
            vehicleManager.find(UUID.fromString(id)).ifPresentOrElse(vehicle -> {
                consumer.accept(driver, vehicle);
            }, () -> {
                Entity root = entity.getWorld().getEntity(UUID.fromString(id));
                if (root == null) return;
                String type = DataKeyHelper.get(root, DataKey.VEHICLE_ROOT_TYPE, String.class);
                if (type == null) return;
                VehicleFactory factory = VehicleFactoryHook.getHook(type);
                if (factory == null) return;
                Vehicle vehicle = factory.regenerate(root);
                if (vehicle == null) return;
                vehicleManager.register(vehicle);
                consumer.accept(driver, vehicle);
            });
        });
    }
}
