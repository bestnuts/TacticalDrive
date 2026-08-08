package me.bestnuts.drive.core.manager;

import me.bestnuts.drive.api.manager.VehicleConfigurationFactory;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public abstract class DirectoryConfigurationFactory extends VehicleConfigurationFactory {

    private static final String EXTENSION = ".yml";

    private final File dir;

    protected DirectoryConfigurationFactory(@NotNull File root, @NotNull String path) {
        dir = new File(root, path);
        if (!dir.exists() || !dir.isDirectory()) dir.mkdirs();
    }

    @Override
    public @NotNull Collection<String> names() {
        File[] files = dir.listFiles((parent, fileName) -> fileName.endsWith(EXTENSION));
        if (files == null) return List.of();
        return Arrays.stream(files)
                .map(file -> file.getName().substring(0, file.getName().length() - EXTENSION.length()))
                .toList();
    }

    @Override
    public @NotNull Optional<FileConfiguration> parameter(@NotNull String name) {
        File file = new File(dir, name.concat(EXTENSION));
        if (!file.exists()) return Optional.empty();
        return Optional.of(YamlConfiguration.loadConfiguration(file));
    }
}
