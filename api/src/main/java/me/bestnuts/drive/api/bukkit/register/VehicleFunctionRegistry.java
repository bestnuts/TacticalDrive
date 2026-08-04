package me.bestnuts.drive.api.bukkit.register;

import me.bestnuts.drive.api.manager.FunctionCreator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class VehicleFunctionRegistry {

    private final Map<String, FunctionCreator> map = new HashMap<>();

    public void register(@NotNull String name, @NotNull FunctionCreator creator) {
        map.put(name, creator);
    }

    public @Nullable FunctionCreator find(@NotNull String name) {
        return map.get(name);
    }

    public void clear() {
        map.clear();
    }
}
