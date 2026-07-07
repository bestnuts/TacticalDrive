package me.bestnuts.api.manager;

import me.bestnuts.api.model.vehicle.component.VehicleBone;
import me.bestnuts.api.model.vehicle.component.VehicleGroup;
import org.bukkit.entity.Entity;

@FunctionalInterface
public interface BoneCreator {
    VehicleBone create(VehicleGroup group, Entity entity);
}
