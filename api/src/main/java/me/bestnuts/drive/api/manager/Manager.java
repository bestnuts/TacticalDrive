package me.bestnuts.drive.api.manager;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface Manager<K, V> {

    @NotNull String type();

    @NotNull Optional<V> find(K k);

    void unregister(K k);

    void register(V v);

    void clear();
}
