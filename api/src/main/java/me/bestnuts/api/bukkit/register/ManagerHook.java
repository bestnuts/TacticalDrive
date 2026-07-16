package me.bestnuts.api.bukkit.register;

import me.bestnuts.api.manager.Manager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class ManagerHook {

    private final static Map<String, Manager<?, ?>> hook = new HashMap<>();

    private ManagerHook() {}

    public static void register(@NotNull Manager<?, ?> manager) {
        hook.put(manager.type(), manager);
    }

    public static @Nullable Manager<?, ?> getHook(@NotNull String name) {
        return hook.get(name);
    }
}
