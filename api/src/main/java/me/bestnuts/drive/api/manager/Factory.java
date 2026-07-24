package me.bestnuts.drive.api.manager;

import org.jetbrains.annotations.NotNull;

public interface Factory<T, R> {

    R generate(@NotNull T t);
}
