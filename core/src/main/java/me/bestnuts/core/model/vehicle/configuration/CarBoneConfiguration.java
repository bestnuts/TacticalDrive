package me.bestnuts.core.model.vehicle.configuration;

import me.bestnuts.api.model.vehicle.component.VehicleBone;
import me.bestnuts.api.model.vehicle.component.VehicleGroup;
import me.bestnuts.api.model.vehicle.configuration.BoneAbstractConfiguration;
import me.bestnuts.api.model.vehicle.configuration.VehicleConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CarBoneConfiguration extends BoneAbstractConfiguration {

    public CarBoneConfiguration(@NotNull VehicleConfiguration parent, @NotNull ConfigurationSection section) {
        super(parent, section);
    }

    @Override
    public @NotNull List<VehicleBone> create(@NotNull VehicleGroup group) {
        List<VehicleBone> bones = new ArrayList<>();
        return bones;
    }
}
