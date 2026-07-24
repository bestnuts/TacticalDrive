package me.bestnuts.drive.api.bukkit.util;

import me.bestnuts.drive.api.model.vehicle.data.DataKey;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DataKeyHelper {

    public static boolean has(@NotNull PersistentDataHolder holder, @NotNull DataKey key) {
        return holder.getPersistentDataContainer().getKeys().contains(key.key());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> void set(@NotNull PersistentDataHolder holder, @NotNull DataKey key, @NotNull T value) {
        PersistentDataType type = getDataType(value.getClass());
        holder.getPersistentDataContainer().set(key.key(), type, value);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> @Nullable T get(@NotNull PersistentDataHolder holder, @NotNull DataKey key, @NotNull Class<T> typeClass) {
        PersistentDataType type = getDataType(typeClass);
        return (T) holder.getPersistentDataContainer().get(key.key(), type);
    }

    public static <T> @NotNull T getOrDefault(@NotNull PersistentDataHolder holder, @NotNull DataKey key, @NotNull Class<T> typeClass, @NotNull T defaultValue) {
        T value = get(holder, key, typeClass);
        return value != null ? value : defaultValue;
    }

    public static void remove(@NotNull PersistentDataHolder holder, @NotNull DataKey key) {
        holder.getPersistentDataContainer().remove(key.key());
    }

    private static @NotNull PersistentDataType<?, ?> getDataType(@NotNull Class<?> clazz) {
        if (clazz == Integer.class || clazz == int.class) return PersistentDataType.INTEGER;
        if (clazz == String.class) return PersistentDataType.STRING;
        if (clazz == Double.class || clazz == double.class) return PersistentDataType.DOUBLE;
        if (clazz == Boolean.class || clazz == boolean.class) return PersistentDataType.BOOLEAN;
        if (clazz == Long.class || clazz == long.class) return PersistentDataType.LONG;
        if (clazz == Float.class || clazz == float.class) return PersistentDataType.FLOAT;
        if (clazz == Byte.class || clazz == byte.class) return PersistentDataType.BYTE;
        if (clazz == Short.class || clazz == short.class) return PersistentDataType.SHORT;

        throw new IllegalArgumentException(clazz.getSimpleName() + " type is not supported.");
    }
}
