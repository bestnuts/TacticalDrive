package me.bestnuts.api.model.vehicle.component;

import lombok.Getter;
import me.bestnuts.api.model.vehicle.dto.BoneData;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class VehicleBone extends VehicleEntity {

    @NotNull private final VehicleGroup group;
    @NotNull private final String type;
    @NotNull private final String name;

    public VehicleBone(@NotNull Entity entity, @NotNull BoneData data) {
        super(entity);
        this.group = data.group();
        this.type = data.type();
        this.name = data.name();
    }
}
