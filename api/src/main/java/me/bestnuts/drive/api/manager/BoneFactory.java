package me.bestnuts.drive.api.manager;

import lombok.Getter;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleBone;
import me.bestnuts.drive.api.model.vehicle.data.BoneFactorySender;
import me.bestnuts.drive.api.model.vehicle.data.BoneRestoreFactorySender;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Getter
public abstract class BoneFactory implements Factory<BoneFactorySender, Map<String, VehicleBone>>, RestoreFactory<BoneRestoreFactorySender, Map<String, VehicleBone>> {

    private final EntityFactory entityFactory;
    private final FunctionFactory functionFactory;

    public BoneFactory(@NotNull EntityFactory entityFactory, @NotNull FunctionFactory functionFactory) {
        this.entityFactory = entityFactory;
        this.functionFactory = functionFactory;
    }
}
