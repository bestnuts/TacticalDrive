package me.bestnuts.drive.api.manager;

import me.bestnuts.drive.api.model.entity.Driver;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;

public interface DriverManager extends Manager<UUID, Driver> {

    @NotNull Collection<Driver> getAll();
}
