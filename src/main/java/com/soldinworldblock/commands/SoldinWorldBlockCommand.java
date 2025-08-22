package com.soldinworldblock.commands;

import com.soldinworldblock.Main;
import com.soldinworldblock.managers.WorldBlockManager;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class SoldinWorldBlockCommand implements CommandExecutor, TabCompleter {

    private final Main plugin;

    public SoldinWorldBlockCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        WorldBlockManager manager = plugin.getWorldBlockManager();
        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "Использование: /soldinworldblock <block|unblock|status|reload>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "block":
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Используйте: /soldinworldblock block <мир> <время>");
                    return true;
                }
                String world = args[1];
                if (!plugin.getConfig().getConfigurationSection("worlds").contains(world)) {
                    sender.sendMessage(ChatColor.RED + "Неверный мир!");
                    return true;
                }
                long duration = parseTime(args[2]);
                manager.blockWorld(world, duration);
                return true;

            case "unblock":
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Используйте: /soldinworldblock unblock <мир>");
                    return true;
                }
                manager.unblockWorld(args[1]);
                return true;

            case "status":
                sender.sendMessage(manager.getBlockedWorlds().toString());
                return true;

            case "reload":
                plugin.reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "Конфиг перезагружен.");
                return true;

            default:
                sender.sendMessage(ChatColor.RED + "Неизвестная команда!");
                return true;
        }
    }

    private long parseTime(String time) {
        long multiplier = 1000;
        if (time.endsWith("h")) return Long.parseLong(time.replace("h", "")) * 3600 * multiplier;
        if (time.endsWith("d")) return Long.parseLong(time.replace("d", "")) * 86400 * multiplier;
        if (time.endsWith("m")) return Long.parseLong(time.replace("m", "")) * 60 * multiplier;
        return Long.parseLong(time) * multiplier;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 2 && args[0].equalsIgnoreCase("block")) {
            plugin.getConfig().getConfigurationSection("worlds").getKeys(false).forEach(completions::add);
            StringUtil.copyPartialMatches(args[1], completions, completions);
        }
        return completions;
    }
}
