package com.soldinworldblock;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import com.soldinworldblock.commands.SoldinWorldBlockCommand;
import com.soldinworldblock.listeners.PortalListener;
import com.soldinworldblock.managers.WorldBlockManager;

public class Main extends JavaPlugin {

    private static Main instance;
    private WorldBlockManager worldBlockManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        worldBlockManager = new WorldBlockManager(this);
        getCommand("soldinworldblock").setExecutor(new SoldinWorldBlockCommand(this));
        Bukkit.getPluginManager().registerEvents(new PortalListener(this), this);
        getLogger().info("SoldinWorldBlock успешно запущен!");
    }

    @Override
    public void onDisable() {
        getLogger().info("SoldinWorldBlock отключен.");
    }

    public static Main getInstance() {
        return instance;
    }

    public WorldBlockManager getWorldBlockManager() {
        return worldBlockManager;
    }
}
