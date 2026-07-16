package me.bestnuts.api.model.vehicle.component.function;

import me.bestnuts.api.bukkit.register.ManagerHook;
import me.bestnuts.api.bukkit.util.DataKeyHelper;
import me.bestnuts.api.bukkit.util.FunctionParamHelper;
import me.bestnuts.api.manager.DriverManager;
import me.bestnuts.api.model.vehicle.Vehicle;
import me.bestnuts.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.api.model.vehicle.component.bone.VehicleSeat;
import me.bestnuts.api.model.vehicle.data.DataKey;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public final class InteractionSeatFunction extends VehicleFunction {

    private final String link;

    private VehicleEntity target;
    private boolean isLoad;

    public InteractionSeatFunction(@NotNull VehicleEntity parent, int delay, @NotNull Map<String, String> param) {
        super(parent, delay, param);
        this.link = param.getOrDefault("link", "root");
    }

    @Override
    public void execute(@NotNull Vehicle vehicle) {
        if (!isLoad) {
            if (target == null) {
                target = FunctionParamHelper.getLink(link, vehicle);
                isLoad = true;
            }
            return;
        }

        if (canSeat()) {
            UUID id = UUID.fromString(DataKeyHelper.get(getParent().getEntity(), DataKey.VEHICLE_INTERACT_ID, String.class));
            DataKeyHelper.remove(getParent().getEntity(), DataKey.VEHICLE_INTERACT_ID);
            Entity entity = getParent().getLocation().getWorld().getEntity(id);
            if (entity == null) return;
            target.getEntity().addPassenger(entity);
            if (getParent() instanceof VehicleSeat seat && ManagerHook.getHook("driver") instanceof DriverManager driverManager) {
                driverManager.find(id).ifPresent(driver -> {
                    seat.setDriver(driver);
                    driver.setSeatedVehicle(vehicle);
                    driver.setSeatedVehicleSeat(seat);
                });
            }
        }
    }

    private boolean canSeat() {
        String id = DataKeyHelper.get(getParent().getEntity(), DataKey.VEHICLE_INTERACT_ID, String.class);
        if (id == null) return false;
        return target.getEntity().getPassengers().isEmpty();
    }
}
