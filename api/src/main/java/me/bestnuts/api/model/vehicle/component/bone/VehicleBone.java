package me.bestnuts.api.model.vehicle.component.bone;

import lombok.Getter;
import me.bestnuts.api.model.vehicle.component.function.VehicleFunction;
import me.bestnuts.api.model.vehicle.data.BoneData;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public abstract class VehicleBone extends VehicleEntity {

    @NotNull private final VehicleGroup group;
    @NotNull private final String type;
    @NotNull private final String name;
    @NotNull private final List<VehicleFunction> functions;

    public VehicleBone(@NotNull Entity entity, @NotNull BoneData data) {
        super(entity);
        this.group = data.group();
        this.type = data.type();
        this.name = data.name();
        this.functions = data.tick().apply(this);
        data.init().apply(this);
    }

    public @NotNull String getAbsolutePath() {
        return group.getAbsolutePath() + "." + name;
    }
}
