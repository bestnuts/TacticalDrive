package me.bestnuts.api.manager;

import lombok.Getter;
import me.bestnuts.api.model.vehicle.component.VehicleBone;
import me.bestnuts.api.model.vehicle.dto.BoneFactorySender;
import me.bestnuts.api.model.vehicle.dto.BoneRestoreFactorySender;

import java.util.Map;

public abstract class BoneFactory implements Factory<BoneFactorySender, Map<String, VehicleBone>>, RestoreFactory<BoneRestoreFactorySender, Map<String, VehicleBone>> {

    @Getter
    private final EntityFactory entityFactory;

    public BoneFactory(EntityFactory entityFactory) {
        this.entityFactory = entityFactory;
    }
}
