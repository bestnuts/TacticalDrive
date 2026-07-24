package me.bestnuts.drive.api.manager;

import org.jetbrains.annotations.NotNull;

public interface RestoreFactory<T, R> {

    R regenerate(@NotNull T t);
}
