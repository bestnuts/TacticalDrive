package me.bestnuts.api.manager;

import me.bestnuts.api.bukkit.util.DataKeyRegister;
import me.bestnuts.api.model.vehicle.Vehicle;
import me.bestnuts.api.model.vehicle.component.VehicleBone;
import me.bestnuts.api.model.vehicle.dto.DataKey;
import me.bestnuts.api.model.vehicle.dto.VehicleFactorySender;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public abstract class VehicleFactory implements Factory<VehicleFactorySender, Vehicle> {

    public abstract @NotNull String name();

    protected void applyDataKey(Vehicle vehicle) {
        String id = vehicle.entity().getUniqueId().toString();
        DataKeyRegister.set(vehicle.entity().getEntity(), DataKey.VEHICLE_ROOT_TYPE, name());
        vehicle.group().consumerTransition(
                group -> group.bones().forEach(bone -> applyDataKey(bone, id))
        );
    }

    private void applyDataKey(VehicleBone bone, String rootId) {
        Entity entity = bone.getEntity();
        DataKeyRegister.set(entity, DataKey.VEHICLE_ROOT_ID, rootId);
        DataKeyRegister.set(entity, DataKey.VEHICLE_BONE_TYPE, bone.getType());
    }
}
