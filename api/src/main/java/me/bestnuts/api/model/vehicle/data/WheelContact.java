package me.bestnuts.api.model.vehicle.data;

import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public interface WheelContact {

    boolean grounded();

    @NotNull
    Vector normal();

    double friction();

    double compression();

    @Nullable
    Block block();
}
