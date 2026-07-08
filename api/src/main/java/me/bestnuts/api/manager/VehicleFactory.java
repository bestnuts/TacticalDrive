package me.bestnuts.api.manager;

import me.bestnuts.api.model.vehicle.Vehicle;
import me.bestnuts.api.model.vehicle.dto.DataKey;
import me.bestnuts.api.model.vehicle.dto.VehicleFactorySender;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public interface VehicleFactory extends Factory<VehicleFactorySender, Vehicle> {
    @NotNull String name();

    default void applyDataKey(Vehicle vehicle) {
        String id = vehicle.entity().getUniqueId().toString();
        vehicle.group().consumerTransition(
                group ->
                        group.bones().forEach(bone ->
                                bone.getEntity().getPersistentDataContainer().set(DataKey.VEHICLE_ROOT_ID.key(), PersistentDataType.STRING, id)
                    )
        );
    }
}
