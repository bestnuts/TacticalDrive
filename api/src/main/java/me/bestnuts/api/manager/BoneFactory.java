package me.bestnuts.api.manager;

import lombok.Getter;
import me.bestnuts.api.model.vehicle.component.VehicleBone;
import me.bestnuts.api.model.vehicle.dto.BoneFactorySender;

import java.util.List;

public abstract class BoneFactory implements Factory<BoneFactorySender, List<VehicleBone>> {

    @Getter
    private final EntityFactory entityFactory;

    public BoneFactory(EntityFactory entityFactory) {
        this.entityFactory = entityFactory;
    }
}
