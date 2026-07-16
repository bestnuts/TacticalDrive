package me.bestnuts.api.model.vehicle.data;

import me.bestnuts.api.bukkit.register.PluginProvider;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public enum DataKey {

    VEHICLE_ROOT_TYPE("vehicle_root_type"),
    VEHICLE_ROOT_NAME("vehicle_root_name"),
    VEHICLE_ROOT_ID("vehicle_root_id"),
    VEHICLE_BONE_TYPE("vehicle_bone_type"),
    VEHICLE_BONE_NAME("vehicle_bone_name"),
    VEHICLE_BONE_GROUP("vehicle_bone_group"),
    VEHICLE_INTERACT_ID("vehicle_interact_id");

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
