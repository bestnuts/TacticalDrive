package me.bestnuts.core.model.vehicle.component.function;

import me.bestnuts.api.bukkit.util.FunctionParamHelper;
import me.bestnuts.api.model.vehicle.Vehicle;
import me.bestnuts.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.api.model.vehicle.component.function.VehicleFunction;
import me.bestnuts.api.model.vehicle.configuration.PhysicsConfiguration;
import me.bestnuts.core.model.vehicle.VehicleCar;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static me.bestnuts.api.bukkit.util.Constant.FIXED_DELTA_TIME;

public class CarSuspensionFunction extends VehicleFunction {

    private final double stiffness;
    private final double damping;
    private final double restLength;
    private final Vector offset;

    private VehicleCar car;
    private PhysicsConfiguration physicsConfiguration;
    private double previousCompression;
    private boolean isLoad;

    public CarSuspensionFunction(@NotNull VehicleEntity parent, int delay, @NotNull Map<String, String> param) {
        super(parent, delay, param);
        this.stiffness = Double.parseDouble(param.getOrDefault("stiffness", "12000.0"));
        this.damping = Double.parseDouble(param.getOrDefault("damping", "1800.0"));
        this.restLength = Double.parseDouble(param.getOrDefault("length", "1.2"));
        this.offset = FunctionParamHelper.getVector(param.getOrDefault("offset", "0;0;0"), new Vector());
    }

    @Override
    public void execute(@NotNull Vehicle vehicle) {
        if (!isLoad) {
            if (vehicle instanceof VehicleCar vehicleCar) {
                car = vehicleCar;
                physicsConfiguration = car.configuration().getPhysics();
                isLoad = true;
            }
            return;
        }
        Location location = vehicle.entity().getLocation();
        World world = location.getWorld();

        Vector suspensionWorldPos = location.toVector().add(this.offset);
        Location rayStart = new Location(world, suspensionWorldPos.getX(), suspensionWorldPos.getY(), suspensionWorldPos.getZ());

        Vector downDirection = new Vector(0, -1, 0);
        RayTraceResult hit = world.rayTraceBlocks(rayStart, downDirection, this.restLength, org.bukkit.FluidCollisionMode.NEVER, true);

        double currentLength = this.restLength;
        double groundY = rayStart.getY() - this.restLength;

        if (hit != null && hit.getHitBlock() != null) {
            currentLength = rayStart.getY() - hit.getHitPosition().getY();
            groundY = hit.getHitPosition().getY();
        }

        double compression = this.restLength - currentLength;
        if (compression < 0) compression = 0;

        double springForce = compression * this.stiffness;

        double compressionVelocity = (compression - this.previousCompression) / FIXED_DELTA_TIME;
        this.previousCompression = compression;
        double dampingForce = compressionVelocity * this.damping;

        double totalUpwardForce = springForce + dampingForce;
        if (totalUpwardForce < 0) totalUpwardForce = 0;

        totalUpwardForce = Math.min(totalUpwardForce, (physicsConfiguration.getMass() * physicsConfiguration.getGravity()) * 5.0);

        double wheelWorldY = (compression > 0) ? groundY : (rayStart.getY() - this.restLength);

        car.getSuspensionOutputs().add(new SuspensionOutput(totalUpwardForce, wheelWorldY));

        Location originLocation = getParent().getLocation();
        originLocation.setY(wheelWorldY);
        getParent().getEntity().teleport(originLocation);
    }

    public record SuspensionOutput(double upwardForce, double wheelWorldY) {
    }
}
