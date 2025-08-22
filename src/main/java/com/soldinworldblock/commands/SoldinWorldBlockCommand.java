package com.soldinworldblock.commands;

import com.soldinworldblock.Main;
import com.soldinworldblock.managers.WorldBlockManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SoldinWorldBlockCommand implements CommandExecutor {

    private final Main plugin;

    public SoldinWorldBlockCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "Использование: /soldinworldblock <block|unblock|status|reload>");
            return true;
        }

        WorldBlockManager manager = plugin.getWorldBlockManager();

        if (args[0].equalsIgnoreCase("block")) {
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "Используйте: /soldinworldblock block <world> <time>");
                return true;
            }
            String world = args[1];
            String time = args[2];
            manager.blockWorld(world, time);
            sender.sendMessage(ChatColor.GREEN + "Мир " + world + " закрыт на " + time);
            return true;
        }

        if (args[0].equalsIgnoreCase("unblock")) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Используйте: /soldinworldblock unblock <world>");
                return true;
            }
            String world = args[1];
            manager.unblockWorld(world);
            sender.sendMessage(ChatColor.GREEN + "Мир " + world + " открыт.");
            return true;
        }

        if (args[0].equalsIgnoreCase("status")) {
            sender.sendMessage(manager.getStatus());
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "Конфиг перезагружен.");
            return true;
        }

        return false;
    }
}
