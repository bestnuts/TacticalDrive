package me.bestnuts.drive.core.repository;

import lombok.Getter;
import me.bestnuts.drive.api.manager.EntityFactory;
import me.bestnuts.drive.api.manager.FunctionFactory;

@Getter
public final class SharedRepository {

    private final EntityFactory entityFactory;
    private final FunctionFactory functionFactory;

    public SharedRepository() {
        entityFactory = new EntityFactory();
        functionFactory = new FunctionFactory();
    }
}
