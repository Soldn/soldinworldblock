package com.soldinworldblock;

import com.soldinworldblock.commands.SoldinWorldBlockCommand;
import com.soldinworldblock.listeners.PortalAndTeleportListener;
import com.soldinworldblock.managers.WorldBlockManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private WorldBlockManager worldBlockManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        worldBlockManager = new WorldBlockManager(this);

        // Команда
        getCommand("soldinworldblock").setExecutor(new SoldinWorldBlockCommand(this));
        getCommand("soldinworldblock").setTabCompleter(new SoldinWorldBlockCommand(this));

        // Слушатели
        getServer().getPluginManager().registerEvents(new PortalAndTeleportListener(this), this);
    }

    public WorldBlockManager getWorldBlockManager() {
        return worldBlockManager;
    }
}
