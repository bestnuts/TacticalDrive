package me.bestnuts.drive.api.model.vehicle.configuration;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface GroupConfiguration extends SharedConfiguration {

    @NotNull List<GroupConfiguration> children();

    @NotNull String groupName();

    @Nullable ConfigurationSection boneSection();
}
