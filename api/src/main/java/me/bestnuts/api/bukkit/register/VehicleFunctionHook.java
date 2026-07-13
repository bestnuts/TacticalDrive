package me.bestnuts.api.bukkit.register;

import me.bestnuts.api.manager.FunctionCreator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class VehicleFunctionHook {

    private static final Map<String, FunctionCreator> hook = new HashMap<>();

    private VehicleFunctionHook() {}

    public static void registerHook(@NotNull String name, @NotNull FunctionCreator creator) {
        hook.put(name, creator);
    }

    @NotNull
    public static Collection<String> hookKeySet() {
        return hook.keySet();
    }

    @Nullable
    public static FunctionCreator getHook(@NotNull String name) {
        return hook.get(name);
    }
}
