package me.bestnuts.drive.api.model.vehicle.data;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

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

    public static void initialize(@NotNull JavaPlugin plugin) {
        for (DataKey value : values()) {
            value.namespacedKey = new NamespacedKey(plugin, value.key);
        }
    }

    public NamespacedKey key() {
        return namespacedKey;
    }
}
