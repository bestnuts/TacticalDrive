package me.bestnuts.drive.core.model.vehicle.component.function;

import me.bestnuts.drive.api.bukkit.util.Constant;
import me.bestnuts.drive.api.bukkit.util.FunctionParamHelper;
import me.bestnuts.drive.api.model.vehicle.Vehicle;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.drive.api.model.vehicle.component.function.VehicleFunction;
import me.bestnuts.drive.api.model.vehicle.data.VehicleOutput;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class ItemDisplayFunction extends VehicleFunction {

    public ItemDisplayFunction(@NotNull VehicleEntity parent, int delay, @NotNull Map<String, String> param) {
        super(parent, delay, param);
        Material material = Material.getMaterial(param.getOrDefault("material", "AIR"));
        material = material == null ? Material.AIR : material;
        NamespacedKey model = FunctionParamHelper.getNamespacedKey(param.getOrDefault("model", "minecraft;air"));
        Vector size = FunctionParamHelper.getVector(param.getOrDefault("size", "1;1;1"), new Vector(1, 1, 1));
        Entity entity = parent.getEntity();
        if (!(entity instanceof ItemDisplay display)) return;
        ItemStack itemStack = ItemStack.of(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setItemModel(model);
        itemStack.setItemMeta(itemMeta);
        display.setItemStack(itemStack);
        Transformation transformation = display.getTransformation();
        transformation.getScale().set((float) size.getX(), (float) size.getY(), (float) size.getZ());
        display.setTransformation(transformation);

        display.setInterpolationDuration(Constant.INTERPOLATION_TICK);
        display.setInterpolationDelay(Constant.INTERPOLATION_TICK);
        display.setTeleportDuration(Constant.INTERPOLATION_TICK);
    }

    @Override
    public @Nullable VehicleOutput execute(@NotNull Vehicle vehicle) {
        return null;
    }
}
