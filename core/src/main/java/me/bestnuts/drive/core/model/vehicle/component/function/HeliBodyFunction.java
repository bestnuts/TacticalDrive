package me.bestnuts.drive.core.model.vehicle.component.function;

import me.bestnuts.drive.api.bukkit.util.RotationHelper;
import me.bestnuts.drive.api.model.vehicle.Vehicle;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.drive.api.model.vehicle.component.function.HitboxFunction;
import me.bestnuts.drive.api.model.vehicle.data.VehicleOutput;
import me.bestnuts.drive.core.model.vehicle.data.HeliBodyOutput;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class HeliBodyFunction extends HitboxFunction {

    public HeliBodyFunction(@NotNull VehicleEntity parent, int delay, @NotNull Map<String, String> param) {
        super(parent, delay, param);
    }

    @Override
    public @Nullable VehicleOutput execute(@NotNull Vehicle vehicle) {
        updateRotation((float) vehicle.motion().getPitch(), (float) vehicle.motion().getRoll());

        Location bodyLocation = vehicle.entity().getLocation();
        World world = bodyLocation.getWorld();
        if (world == null) return null;

        getHitbox().update(bodyLocation);
        Vector boxMin = getHitbox().getMin();
        Vector boxMax = getHitbox().getMax();

        boolean blocked = false;
        Vector offset = new Vector(0, 0, 0);

        for (int x = (int) Math.floor(boxMin.getX()); x <= (int) Math.ceil(boxMax.getX()); x++) {
            for (int y = (int) Math.floor(boxMin.getY()); y <= (int) Math.ceil(boxMax.getY()); y++) {
                for (int z = (int) Math.floor(boxMin.getZ()); z <= (int) Math.ceil(boxMax.getZ()); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (!block.getType().isSolid()) continue;

                    if (boxMin.getX() <= x + 1 && boxMax.getX() >= x &&
                            boxMin.getY() <= y + 1 && boxMax.getY() >= y &&
                            boxMin.getZ() <= z + 1 && boxMax.getZ() >= z) {

                        blocked = true;

                        Vector blockCenter = new Vector(x + 0.5, y + 0.5, z + 0.5);
                        Vector relative = blockCenter.subtract(bodyLocation.toVector());
                        offset.add(new Vector(relative.getX() > 0 ? 1 : -1, 0, relative.getZ() > 0 ? 1 : -1));
                    }
                }
            }
        }

        return new HeliBodyOutput(blocked, offset);
    }

    private void updateRotation(float pitch, float roll) {
        Entity entity = getParent().getEntity();
        if (!(entity instanceof Display display)) {
            return;
        }

        Transformation transformation = display.getTransformation();
        transformation.getLeftRotation().set(RotationHelper.tilt(pitch, roll));

        display.setTransformation(transformation);
    }
}
