package me.bestnuts.plugin;

import me.bestnuts.api.bukkit.register.PluginProvider;
import me.bestnuts.core.repository.GlobalRepository;
import me.bestnuts.plugin.listener.PlayerLifecycle;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class TacticalDrive extends JavaPlugin {

    private GlobalRepository repository;

    @Override
    public void onEnable() {
        PluginProvider.initialize(this);
        repository = new GlobalRepository(this);
        register();
    }

    @Override
    public void onDisable() {
        repository = null;
    }

    private void register() {
        Bukkit.getPluginManager().registerEvents(new PlayerLifecycle(repository), this);
    }
}
