package me.bestnuts.drive.core.model.vehicle.component.function;

import me.bestnuts.drive.api.bukkit.util.RotationHelper;
import me.bestnuts.drive.api.model.vehicle.Vehicle;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.drive.api.model.vehicle.component.function.HitboxFunction;
import me.bestnuts.drive.api.model.vehicle.data.VehicleOutput;
import me.bestnuts.drive.core.model.vehicle.data.BodyOutput;
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

public final class CarBodyFunction extends HitboxFunction {

    public CarBodyFunction(@NotNull VehicleEntity parent, int delay, @NotNull Map<String, String> param) {
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
        double climbLimitY = vehicle.motion().getClimbLimitY();

        int minX = (int) Math.floor(boxMin.getX());
        int minY = (int) Math.floor(boxMin.getY());
        int minZ = (int) Math.floor(boxMin.getZ());
        int maxX = (int) Math.ceil(boxMax.getX());
        int maxY = (int) Math.ceil(boxMax.getY());
        int maxZ = (int) Math.ceil(boxMax.getZ());

        boolean isBodyCollided = false;
        Vector bodyCollisionOffset = new Vector(0, 0, 0);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                if (y + 1 <= climbLimitY) {
                    continue;
                }
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);

                    if (!block.getType().isSolid()) {
                        continue;
                    }

                    if (boxMin.getX() <= x + 1 && boxMax.getX() >= x &&
                            boxMin.getY() <= y + 1 && boxMax.getY() >= y &&
                            boxMin.getZ() <= z + 1 && boxMax.getZ() >= z) {

                        isBodyCollided = true;

                        Vector blockCenter = new Vector(x + 0.5, y + 0.5, z + 0.5);
                        Vector relativeDir = blockCenter.subtract(bodyLocation.toVector());
                        bodyCollisionOffset.add(new Vector(relativeDir.getX() > 0 ? 1 : -1, 0, relativeDir.getZ() > 0 ? 1 : -1));
                    }
                }
            }
        }

        if (!isBodyCollided) return null;
        return new BodyOutput(true, bodyCollisionOffset);
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
