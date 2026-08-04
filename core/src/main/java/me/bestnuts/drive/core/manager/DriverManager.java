package me.bestnuts.drive.core.manager;

import me.bestnuts.drive.api.manager.AbstractDriverManager;
import me.bestnuts.drive.api.model.entity.Driver;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class DriverManager implements AbstractDriverManager {

    private final Map<UUID, Driver> idMap = new HashMap<>();

    @Override
    public @NotNull String type() {
        return "driver";
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

    @Override
    public void clear() {
        idMap.clear();
    }

    @Override
    public @NotNull Collection<Driver> getAll() {
        return idMap.values();
    }
}
