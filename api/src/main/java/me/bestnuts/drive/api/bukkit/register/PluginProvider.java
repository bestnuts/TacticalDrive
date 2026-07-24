package me.bestnuts.drive.api.bukkit.register;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class PluginProvider {

    @Getter
    private static JavaPlugin plugin;

    private PluginProvider() {}

    public static void initialize(@NotNull JavaPlugin plugin) {
        PluginProvider.plugin = plugin;
    }
}
