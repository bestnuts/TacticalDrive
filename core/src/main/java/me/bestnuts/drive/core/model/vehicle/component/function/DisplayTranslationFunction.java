package me.bestnuts.drive.core.model.vehicle.component.function;

import me.bestnuts.drive.api.bukkit.util.FunctionParamHelper;
import me.bestnuts.drive.api.model.vehicle.Vehicle;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.drive.api.model.vehicle.component.function.VehicleFunction;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class DisplayTranslationFunction extends VehicleFunction {

    public DisplayTranslationFunction(@NotNull VehicleEntity parent, int delay, @NotNull Map<String, String> param) {
        super(parent, delay, param);
        Vector offset = FunctionParamHelper.getVector(param.getOrDefault("offset", "0;0;0"), new Vector());
        Entity entity = parent.getEntity();
        if (!(entity instanceof Display display)) return;
        Transformation transformation = display.getTransformation();
        transformation.getTranslation().set(offset.getX(), offset.getY(), offset.getZ());
        display.setTransformation(transformation);
    }

    @Override
    public void execute(@NotNull Vehicle vehicle) {
    }
}
