package me.bestnuts.core.manager;

import me.bestnuts.api.manager.EntityFactory;
import me.bestnuts.api.manager.VehicleFactory;
import me.bestnuts.api.model.vehicle.Vehicle;
import me.bestnuts.api.model.vehicle.component.VehicleGroup;
import me.bestnuts.api.model.vehicle.configuration.GroupImplConfiguration;
import me.bestnuts.api.model.vehicle.dto.EntityFactorySender;
import me.bestnuts.api.model.vehicle.dto.VehicleFactorySender;
import me.bestnuts.core.model.vehicle.VehicleCar;
import me.bestnuts.core.model.vehicle.component.VehicleRoot;
import me.bestnuts.core.model.vehicle.configuration.CarConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CarFactory implements VehicleFactory {

    private final EntityFactory entityFactory;
    private final CarBoneFactory boneFactory;
    private final CarConfigurationFactory configurationFactory;

    public CarFactory(EntityFactory entityFactory, CarBoneFactory boneFactory, CarConfigurationFactory configurationFactory) {
        this.entityFactory = entityFactory;
        this.boneFactory = boneFactory;
        this.configurationFactory = configurationFactory;
    }

    @Override
    public @NotNull String name() {
        return "car";
    }

    @Override
    public @Nullable Vehicle generate(@NotNull VehicleFactorySender sender) {
        Optional<FileConfiguration> optional = configurationFactory.parameter(sender.name());
        if (optional.isPresent()) {
            FileConfiguration configuration = optional.get();
            CarConfiguration vehicleConfiguration = configurationFactory.generate(configuration);
            GroupImplConfiguration group = boneFactory.generate(vehicleConfiguration);
            if (group == null) return null;
            EntityFactorySender entityFactorySender = new EntityFactorySender(sender.location(), configuration);
            VehicleGroup vehicleGroup = group.create(entityFactorySender);
            Entity entity = entityFactory.generate(entityFactorySender);
            VehicleRoot root = new VehicleRoot(entity);
            return new VehicleCar(root, vehicleGroup, vehicleConfiguration);
        }
        return null;
    }
}
