package me.bestnuts.drive.api.bukkit.register;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

public final class SurfaceFrictionRegistry {

    private final Map<Material, Double> map = new EnumMap<>(Material.class);

    @Getter
    @Setter
    private double defaultFriction = 1.0;

    public void register(@NotNull Material material, double friction) {
        map.put(material, friction);
    }

    public double find(@NotNull Material material) {
        return map.getOrDefault(material, defaultFriction);
    }

    public void clear() {
        map.clear();
        defaultFriction = 1.0;
    }
}
