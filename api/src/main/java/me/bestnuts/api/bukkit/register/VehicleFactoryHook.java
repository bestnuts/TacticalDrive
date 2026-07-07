package me.bestnuts.api.bukkit.register;

import me.bestnuts.api.manager.VehicleFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class VehicleFactoryHook {

    private static final Map<String, VehicleFactory> hook = new HashMap<>();

    private VehicleFactoryHook() {}

    public static void registerHooks(@NotNull VehicleFactory factory) {
        hook.put(factory.name(), factory);
    }

    @NotNull
    public static Collection<String> hookKeySet() {
        return hook.keySet();
    }

    @Nullable
    public static VehicleFactory getHooks(String name) {
        return hook.get(name);
    }
}
