package me.bestnuts.core.repository;

import lombok.Getter;
import me.bestnuts.api.manager.EntityFactory;
import me.bestnuts.api.manager.FunctionFactory;

@Getter
public final class SharedRepository {

    private final EntityFactory entityFactory;
    private final FunctionFactory functionFactory;

    public SharedRepository() {
        entityFactory = new EntityFactory();
        functionFactory = new FunctionFactory();
    }
}
