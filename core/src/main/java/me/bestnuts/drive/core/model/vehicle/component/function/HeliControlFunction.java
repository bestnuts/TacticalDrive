package me.bestnuts.drive.core.model.vehicle.component.function;

import me.bestnuts.drive.api.bukkit.util.FunctionParamHelper;
import me.bestnuts.drive.api.model.entity.Driver;
import me.bestnuts.drive.api.model.vehicle.Vehicle;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleSeat;
import me.bestnuts.drive.api.model.vehicle.component.function.VehicleFunction;
import me.bestnuts.drive.api.model.vehicle.data.VehicleOutput;
import me.bestnuts.drive.core.model.vehicle.data.ControlOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class HeliControlFunction extends VehicleFunction {

    private final String link;

    private VehicleSeat seat;

    public HeliControlFunction(@NotNull VehicleEntity parent, int delay, @NotNull Map<String, String> param) {
        super(parent, delay, param);
        this.link = param.getOrDefault("link", "root");
    }

    @Override
    public @Nullable VehicleOutput execute(@NotNull Vehicle vehicle) {
        if (seat == null) {
            if (!(FunctionParamHelper.getLink(link, vehicle) instanceof VehicleSeat target)) return null;
            seat = target;
        }

        Driver driver = seat.getDriver();
        if (driver == null) {
            return new ControlOutput(0.0, 0.0, false, vehicle.entity().getLocation().getYaw(), false);
        }

        return new ControlOutput(
                driver.getInput().getForward(),
                driver.getInput().getSideway(),
                driver.getInput().isJump(),
                driver.getLocation().getYaw(),
                true
        );
    }
}
