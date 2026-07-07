package me.bestnuts.api.manager;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface Manager<K, V> {

    @NotNull Class<? extends Manager<?, ?>> type();

    @NotNull Optional<V> find(K k);

    void unregister(K k);

    void register(V v);
}
