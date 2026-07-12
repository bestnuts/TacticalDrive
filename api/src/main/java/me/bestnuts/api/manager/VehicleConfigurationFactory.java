package me.bestnuts.api.manager;

import lombok.Getter;
import me.bestnuts.api.model.vehicle.configuration.SharedConfiguration;
import me.bestnuts.api.model.vehicle.configuration.VehicleConfiguration;
import me.bestnuts.api.model.vehicle.data.ConfigurationFactorySender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public abstract class VehicleConfigurationFactory implements ConfigurationFactory {

    @FunctionalInterface
    public interface ConfigCreator {
        SharedConfiguration create(VehicleConfiguration parent, FileConfiguration configuration);
    }

    private final Map<Class<?>, ConfigCreator> function = new HashMap<>();

    @Getter
    private final EntityFactory entityFactory;

    public VehicleConfigurationFactory(EntityFactory entityFactory) {
        this.entityFactory = entityFactory;
    }

    @Override
    public abstract @NotNull VehicleConfiguration generate(@NotNull ConfigurationFactorySender sender);

    public abstract @Nullable VehicleConfiguration generate(@NotNull String name);

    @NotNull
    public <C extends SharedConfiguration> VehicleConfigurationFactory register(
            @NotNull Class<C> type,
            @NotNull BiFunction<VehicleConfiguration, FileConfiguration, ? extends C> creator) {
        this.function.put(type, creator::apply);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nullable
    public <C extends SharedConfiguration> C getConfiguration(@NotNull Class<C> type, @NotNull VehicleConfiguration parent, @NotNull FileConfiguration configuration) {
        ConfigCreator func = this.function.get(type);
        if (func == null) return null;
        return (C) func.create(parent, configuration);
    }
}
