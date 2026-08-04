package me.bestnuts.drive.core.repository;

import lombok.Getter;
import me.bestnuts.drive.api.bukkit.register.VehicleFunctionRegistry;
import me.bestnuts.drive.api.manager.EntityFactory;
import me.bestnuts.drive.api.manager.FunctionFactory;
import me.bestnuts.drive.core.manager.SharedEntityFactory;
import me.bestnuts.drive.core.manager.SharedFunctionFactory;
import org.jetbrains.annotations.NotNull;

@Getter
public final class SharedRepository {

    private final EntityFactory entityFactory;
    private final FunctionFactory functionFactory;

    public SharedRepository(@NotNull VehicleFunctionRegistry functionRegistry) {
        entityFactory = new SharedEntityFactory();
        functionFactory = new SharedFunctionFactory(functionRegistry);
    }
}
