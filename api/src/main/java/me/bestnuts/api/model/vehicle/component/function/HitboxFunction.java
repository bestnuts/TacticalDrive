package me.bestnuts.api.model.vehicle.component.function;

import lombok.Getter;
import me.bestnuts.api.bukkit.util.FunctionParamHelper;
import me.bestnuts.api.model.vehicle.VehicleHitbox;
import me.bestnuts.api.model.vehicle.component.bone.VehicleEntity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Getter
public abstract class HitboxFunction extends VehicleFunction {

    private final VehicleHitbox hitbox;

    public HitboxFunction(@NotNull VehicleEntity parent, int delay, @NotNull Map<String, String> param) {
        super(parent, delay, param);
        Vector size = FunctionParamHelper.getVector(param.getOrDefault("size", "1;1;1"), new Vector(1, 1, 1));
        this.hitbox = new VehicleHitbox(size);
    }

    public boolean containsHitbox(@NotNull Vector vector) {
        return hitbox.contains(vector);
    }
}
