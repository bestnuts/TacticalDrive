package me.bestnuts.api.manager;

import me.bestnuts.api.model.vehicle.component.VehicleBone;
import me.bestnuts.api.model.vehicle.dto.BoneData;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface BoneCreator {
    VehicleBone create(@NotNull Entity entity, @NotNull BoneData data);
}
