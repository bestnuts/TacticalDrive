package me.bestnuts.drive.api.bukkit.register;

import me.bestnuts.drive.api.manager.VehicleFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class VehicleFactoryRegistry {

    private final Map<String, VehicleFactory> map = new HashMap<>();

    public void register(@NotNull VehicleFactory factory) {
        map.put(factory.name(), factory);
    }

    public @Nullable VehicleFactory find(@NotNull String name) {
        return map.get(name);
    }

    public @NotNull Collection<String> names() {
        return map.keySet();
    }

    public void clear() {
        map.clear();
    }
}
