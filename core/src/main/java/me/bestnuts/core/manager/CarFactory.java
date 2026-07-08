package me.bestnuts.core.manager;

import me.bestnuts.api.manager.EntityFactory;
import me.bestnuts.api.manager.VehicleFactory;
import me.bestnuts.api.model.vehicle.Vehicle;
import me.bestnuts.api.model.vehicle.component.VehicleGroup;
import me.bestnuts.api.model.vehicle.dto.EntityFactorySender;
import me.bestnuts.api.model.vehicle.dto.GroupFactorySender;
import me.bestnuts.api.model.vehicle.dto.VehicleFactorySender;
import me.bestnuts.core.model.vehicle.VehicleCar;
import me.bestnuts.core.model.vehicle.component.VehicleRoot;
import me.bestnuts.core.model.vehicle.configuration.CarConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CarFactory extends VehicleFactory {

    private final EntityFactory entityFactory;
    private final CarGroupFactory groupFactory;
    private final CarConfigurationFactory configurationFactory;

    public CarFactory(EntityFactory entityFactory, CarGroupFactory groupFactory, CarConfigurationFactory configurationFactory) {
        this.entityFactory = entityFactory;
        this.groupFactory = groupFactory;
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
            EntityFactorySender entityFactorySender = new EntityFactorySender(sender.location(), configuration);
            VehicleGroup group = groupFactory.generate(new GroupFactorySender(vehicleConfiguration, entityFactorySender));
            if (group == null) return null;
            Entity entity = entityFactory.generate(entityFactorySender);
            VehicleRoot root = new VehicleRoot(entity);
            Vehicle vehicle = new VehicleCar(root, group, vehicleConfiguration);
            applyDataKey(vehicle);
            return vehicle;
        }
        return null;
    }
}
