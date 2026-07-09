package me.bestnuts.api.manager;

import me.bestnuts.api.model.vehicle.Vehicle;
import me.bestnuts.api.model.vehicle.component.VehicleBone;
import me.bestnuts.api.model.vehicle.dto.DataKey;
import me.bestnuts.api.model.vehicle.dto.VehicleFactorySender;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public abstract class VehicleFactory implements Factory<VehicleFactorySender, Vehicle> {

    public abstract @NotNull String name();

    protected void applyDataKey(Vehicle vehicle) {
        String id = vehicle.entity().getUniqueId().toString();
        vehicle.entity().getEntity().getPersistentDataContainer().set(DataKey.VEHICLE_ROOT_TYPE.key(), PersistentDataType.STRING, name());
        vehicle.group().consumerTransition(
                group -> group.bones().forEach(bone -> applyDataKey(bone, id))
        );
    }

    private void applyDataKey(VehicleBone bone, String rootId) {
        bone.getEntity().getPersistentDataContainer().set(DataKey.VEHICLE_ROOT_ID.key(), PersistentDataType.STRING, rootId);
        bone.getEntity().getPersistentDataContainer().set(DataKey.VEHICLE_BONE_TYPE.key(), PersistentDataType.STRING, bone.getType());
    }
}
