package me.bestnuts.core.manager;

import me.bestnuts.api.manager.Manager;
import me.bestnuts.api.model.entity.Driver;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class DriverManager implements Manager<UUID, Driver> {

    private final Map<UUID, Driver> idMap = new HashMap<>();

    @Override
    public @NotNull Class<? extends Manager<?, ?>> type() {
        return DriverManager.class;
    }

    @Override
    public @NotNull Optional<Driver> find(UUID uuid) {
        return Optional.ofNullable(idMap.get(uuid));
    }

    @Override
    public void unregister(UUID uuid) {
        idMap.remove(uuid);
    }

    @Override
    public void register(Driver driver) {
        UUID id = driver.getUniqueId();
        idMap.put(id, driver);
    }
}
