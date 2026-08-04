package me.bestnuts.drive.core.model.vehicle.component.function;

import me.bestnuts.drive.api.bukkit.util.DataKeyHelper;
import me.bestnuts.drive.api.bukkit.util.FunctionParamHelper;
import me.bestnuts.drive.api.model.vehicle.Vehicle;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleSeat;
import me.bestnuts.drive.api.model.vehicle.component.function.VehicleFunction;
import me.bestnuts.drive.api.model.vehicle.data.DataKey;
import me.bestnuts.drive.api.model.vehicle.data.VehicleOutput;
import me.bestnuts.drive.core.service.VehicleSeatService;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public final class InteractionSeatFunction extends VehicleFunction {

    private final VehicleSeatService seatService;
    private final String link;

    private VehicleEntity target;

    public InteractionSeatFunction(@NotNull VehicleSeatService seatService, @NotNull VehicleEntity parent, int delay, @NotNull Map<String, String> param) {
        super(parent, delay, param);
        this.seatService = seatService;
        this.link = param.getOrDefault("link", "root");
    }

    @Override
    public @Nullable VehicleOutput execute(@NotNull Vehicle vehicle) {
        if (target == null) {
            target = FunctionParamHelper.getLink(link, vehicle);
        }

        String interactId = DataKeyHelper.get(getParent().getEntity(), DataKey.VEHICLE_INTERACT_ID, String.class);
        if (interactId == null || !target.getEntity().getPassengers().isEmpty()) return null;

        UUID id = UUID.fromString(interactId);
        DataKeyHelper.remove(getParent().getEntity(), DataKey.VEHICLE_INTERACT_ID);
        Entity entity = getParent().getLocation().getWorld().getEntity(id);
        if (entity == null) return null;
        target.getEntity().addPassenger(entity);
        if (getParent() instanceof VehicleSeat seat) {
            seatService.mount(id, vehicle, seat);
        }
        return null;
    }
}
