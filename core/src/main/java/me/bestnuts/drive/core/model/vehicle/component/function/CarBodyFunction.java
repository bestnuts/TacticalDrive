package me.bestnuts.drive.core.model.vehicle.component.function;

import me.bestnuts.drive.api.model.vehicle.Vehicle;
import me.bestnuts.drive.api.model.vehicle.VehicleHitbox;
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
import org.joml.Quaternionf;

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

        double carSpeed = vehicle.motion().getSpeed();
        Vector forwardVector = bodyLocation.getDirection().setY(0).normalize();

        double currentMoveDistance = carSpeed * 0.05;
        double minMarginDistance = 0.2;
        double finalSearchDistance = Math.max(Math.abs(currentMoveDistance), minMarginDistance);

        Vector searchVelocity = forwardVector.multiply(carSpeed >= 0.0 ? finalSearchDistance : -finalSearchDistance);

        VehicleHitbox expandedBox = getHitbox().calculateExpandedBox(searchVelocity);

        int minX = (int) Math.floor(expandedBox.getMin().getX());
        int minY = (int) Math.floor(expandedBox.getMin().getY());
        int minZ = (int) Math.floor(expandedBox.getMin().getZ());
        int maxX = (int) Math.ceil(expandedBox.getMax().getX());
        int maxY = (int) Math.ceil(expandedBox.getMax().getY());
        int maxZ = (int) Math.ceil(expandedBox.getMax().getZ());

        boolean isBodyCollided = false;
        Vector bodyCollisionOffset = new Vector(0, 0, 0);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);

                    if (!block.getType().isSolid()) {
                        continue;
                    }

                    Vector blockMin = new Vector(x, y, z);
                    Vector blockMax = new Vector(x + 1, y + 1, z + 1);

                    if (expandedBox.getMin().getX() <= blockMax.getX() && expandedBox.getMax().getX() >= blockMin.getX() &&
                            expandedBox.getMin().getY() <= blockMax.getY() && expandedBox.getMax().getY() >= blockMin.getY() &&
                            expandedBox.getMin().getZ() <= blockMax.getZ() && expandedBox.getMax().getZ() >= blockMin.getZ()) {

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

        float pitchRad = (float) Math.toRadians(-pitch);
        float rollRad = (float) Math.toRadians(-roll);

        Quaternionf pitchQuaternion = new Quaternionf().rotationX(pitchRad);
        Quaternionf rollQuaternion = new Quaternionf().rotationZ(rollRad);
        Quaternionf finalRotation = pitchQuaternion.mul(rollQuaternion);

        Transformation transformation = display.getTransformation();
        transformation.getLeftRotation().set(finalRotation);

        display.setTransformation(transformation);
    }
}
