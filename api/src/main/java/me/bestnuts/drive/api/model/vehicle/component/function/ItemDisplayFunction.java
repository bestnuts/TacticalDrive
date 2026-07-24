package me.bestnuts.drive.api.model.vehicle.component.function;

import me.bestnuts.drive.api.bukkit.util.Constant;
import me.bestnuts.drive.api.bukkit.util.FunctionParamHelper;
import me.bestnuts.drive.api.model.vehicle.Vehicle;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleEntity;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class ItemDisplayFunction extends VehicleFunction {

    public ItemDisplayFunction(@NotNull VehicleEntity parent, int delay, @NotNull Map<String, String> param) {
        super(parent, delay, param);
        Material material = Material.getMaterial(param.getOrDefault("material", "AIR"));
        material = material == null ? Material.AIR : material;
        NamespacedKey model = FunctionParamHelper.getNamespacedKey(param.getOrDefault("model", "minecraft;air"));
        float size = Float.parseFloat(param.getOrDefault("size", "1"));
        Entity entity = parent.getEntity();
        if (!(entity instanceof ItemDisplay display)) return;
        ItemStack itemStack = ItemStack.of(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setItemModel(model);
        itemStack.setItemMeta(itemMeta);
        display.setItemStack(itemStack);
        Transformation transformation = display.getTransformation();
        transformation.getScale().set(size);
        display.setTransformation(transformation);

        display.setInterpolationDuration(Constant.INTERPOLATION_TICK);
        display.setInterpolationDelay(Constant.INTERPOLATION_TICK);
        display.setTeleportDuration(Constant.INTERPOLATION_TICK);
    }

    @Override
    public void execute(@NotNull Vehicle vehicle) {
    }
}
