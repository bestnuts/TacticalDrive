package me.bestnuts.core.repository;

import lombok.Getter;
import me.bestnuts.api.manager.EntityFactory;

@Getter
public final class SharedRepository {

    private final EntityFactory entityFactory;

    public SharedRepository() {
        entityFactory = new EntityFactory();
    }
}
