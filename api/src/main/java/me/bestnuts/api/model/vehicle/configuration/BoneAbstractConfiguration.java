package me.bestnuts.api.model.vehicle.configuration;

import lombok.Getter;
import me.bestnuts.api.manager.EntityFactory;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class BoneAbstractConfiguration implements BoneConfiguration {

    private final EntityFactory entityFactory;
    private final VehicleConfiguration parent;

    public BoneAbstractConfiguration(@NotNull EntityFactory entityFactory, @NotNull VehicleConfiguration parent) {
        this.entityFactory = entityFactory;
        this.parent = parent;
    }

    @Override
    public @NotNull String name() {
        return parent.name();
    }
}
