package me.bestnuts.api.model.vehicle.dto;

import me.bestnuts.api.bukkit.register.PluginProvider;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public enum DataKey {

    VEHICLE_ROOT_TYPE("vehicle_root_type"),
    VEHICLE_ROOT_ID("vehicle_root_id"),
    VEHICLE_BONE_TYPE("vehicle_bone_type");

    private final String key;
    private NamespacedKey namespacedKey;

    DataKey(String key) {
        this.key = key;
    }

    static {
        JavaPlugin plugin = PluginProvider.getPlugin();
        for (DataKey value : values()) {
            value.namespacedKey = new NamespacedKey(plugin, value.key);
        }
    }

    public NamespacedKey key() {
        return namespacedKey;
    }
}
