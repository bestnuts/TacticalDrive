package me.bestnuts.drive.core.model.vehicle.component.function;

import me.bestnuts.drive.api.bukkit.util.RotationHelper;
import me.bestnuts.drive.api.manager.AbstractVehicleManager;
import me.bestnuts.drive.api.model.vehicle.Vehicle;
import me.bestnuts.drive.api.model.vehicle.VehicleHitbox;
import me.bestnuts.drive.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.drive.api.model.vehicle.component.function.HitboxFunction;
import me.bestnuts.drive.api.model.vehicle.data.VehicleOutput;
import me.bestnuts.drive.core.model.vehicle.configuration.CarPhysicsConfiguration;
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

    private static final int CHUNK_RANGE = 1;
    private static final double SEPARATION_STEP = 0.05;
    private static final double CONTACT_EPSILON = 0.001;

    private final AbstractVehicleManager vehicleManager;

    private CarPhysicsConfiguration physics;

    public CarBodyFunction(@NotNull AbstractVehicleManager vehicleManager,
                           @NotNull VehicleEntity parent, int delay, @NotNull Map<String, String> param) {
        super(parent, delay, param);
        this.vehicleManager = vehicleManager;
    }

    @Override
    public @Nullable VehicleOutput execute(@NotNull Vehicle vehicle) {
        if (physics == null) {
            physics = (CarPhysicsConfiguration) vehicle.configuration().getPhysics();
        }

        updateRotation((float) vehicle.motion().getPitch(), (float) vehicle.motion().getRoll());

        Location bodyLocation = vehicle.entity().getLocation();
        World world = bodyLocation.getWorld();
        if (world == null) return null;

        getHitbox().update(bodyLocation);

        Vector offset = new Vector(0, 0, 0);
        Vector impactVelocity = new Vector(0, 0, 0);
        Vector separation = new Vector(0, 0, 0);

        boolean locked = scanBlocks(world, bodyLocation, vehicle.motion().getClimbLimitY(), offset);
        scanVehicles(vehicle, bodyLocation, impactVelocity, separation);

        return new BodyOutput(locked, offset, getHitbox(), impactVelocity, separation);
    }

    private boolean scanBlocks(@NotNull World world, @NotNull Location bodyLocation, double climbLimitY, @NotNull Vector offset) {
        Vector boxMin = getHitbox().getMin();
        Vector boxMax = getHitbox().getMax();

        int minX = (int) Math.floor(boxMin.getX());
        int minY = (int) Math.floor(boxMin.getY());
        int minZ = (int) Math.floor(boxMin.getZ());
        int maxX = (int) Math.ceil(boxMax.getX());
        int maxY = (int) Math.ceil(boxMax.getY());
        int maxZ = (int) Math.ceil(boxMax.getZ());

        boolean collided = false;

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

                        collided = true;

                        Vector blockCenter = new Vector(x + 0.5, y + 0.5, z + 0.5);
                        Vector relativeDir = blockCenter.subtract(bodyLocation.toVector());
                        offset.add(new Vector(relativeDir.getX() > 0 ? 1 : -1, 0, relativeDir.getZ() > 0 ? 1 : -1));
                    }
                }
            }
        }

        return collided;
    }

    private void scanVehicles(@NotNull Vehicle vehicle, @NotNull Location bodyLocation,
                              @NotNull Vector impactVelocity, @NotNull Vector separation) {
        for (Vehicle other : vehicleManager.getAll()) {
            if (other.entity().getUniqueId().equals(vehicle.entity().getUniqueId())) continue;

            Location otherLocation = other.entity().getLocation();
            if (!withinChunkRange(bodyLocation, otherLocation)) continue;

            VehicleHitbox otherHitbox = other.hitbox();
            if (otherHitbox == null || !getHitbox().intersects(otherHitbox)) continue;

            Vector normal = bodyLocation.toVector().subtract(otherLocation.toVector()).setY(0);
            if (normal.lengthSquared() < CONTACT_EPSILON) continue;
            normal.normalize();

            separation.add(normal.clone().multiply(SEPARATION_STEP));

            Vector relative = worldVelocity(vehicle, bodyLocation).subtract(worldVelocity(other, otherLocation));
            double approach = relative.dot(normal);
            if (approach >= 0.0) continue;

            double selfMass = physics.getMass();
            double otherMass = other.configuration().getPhysics().getMass();
            double impulse = -(1.0 + physics.getCollisionRestitution()) * approach / ((1.0 / selfMass) + (1.0 / otherMass));

            impactVelocity.add(normal.clone().multiply(impulse / selfMass));
        }
    }

    private boolean withinChunkRange(@NotNull Location self, @NotNull Location other) {
        if (self.getWorld() == null || !self.getWorld().equals(other.getWorld())) return false;
        int dx = Math.abs((self.getBlockX() >> 4) - (other.getBlockX() >> 4));
        int dz = Math.abs((self.getBlockZ() >> 4) - (other.getBlockZ() >> 4));
        return dx <= CHUNK_RANGE && dz <= CHUNK_RANGE;
    }

    private @NotNull Vector worldVelocity(@NotNull Vehicle vehicle, @NotNull Location location) {
        Vector forward = location.getDirection().setY(0).normalize();
        Vector lateral = RotationHelper.rotateByYaw(location, new Vector(1, 0, 0)).setY(0).normalize();
        return forward.multiply(vehicle.motion().getSpeed())
                .add(lateral.multiply(vehicle.motion().getLateralSpeed()));
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
