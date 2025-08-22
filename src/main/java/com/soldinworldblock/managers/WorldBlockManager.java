package com.soldinworldblock.managers;

import com.soldinworldblock.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public class WorldBlockManager {

    private final Main plugin;
    private final Map<String, Long> blockedWorlds = new HashMap<>();

    public WorldBlockManager(Main plugin) {
        this.plugin = plugin;
    }

    public boolean isBlocked(String world) {
        if (!blockedWorlds.containsKey(world)) return false;
        long endTime = blockedWorlds.get(world);
        if (System.currentTimeMillis() > endTime) {
            unblockWorld(world);
            return false;
        }
        return true;
    }

    public void blockWorld(String world, long duration) {
        blockedWorlds.put(world, System.currentTimeMillis() + duration);
        sendBroadcast("messages.block.start", world, duration);
        startCountdown(world);
    }

    public void unblockWorld(String world) {
        blockedWorlds.remove(world);
        sendBroadcast("messages.unblock", world, 0);
    }

    public Map<String, Long> getBlockedWorlds() {
        return blockedWorlds;
    }

    private void sendBroadcast(String path, String world, long duration) {
        String msg = plugin.getConfig().getString(path);
        if (msg != null) {
            msg = msg.replace("{world}", world).replace("{time}", formatDuration(duration));
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }
    }

    private void startCountdown(String world) {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (!isBlocked(world)) return;

            long remaining = (blockedWorlds.get(world) - System.currentTimeMillis()) / 1000;
            plugin.getConfig().getConfigurationSection("messages.block.countdown").getKeys(false).forEach(key -> {
                int sec = Integer.parseInt(key);
                if (remaining == sec) {
                    String msg = plugin.getConfig().getString("messages.block.countdown." + key).replace("{world}", world);
                    Bukkit.broadcastMessage(ChatColor.YELLOW + msg);
                }
            });

            if (remaining <= 0) {
                unblockWorld(world);
                String openedMsg = plugin.getConfig().getString("messages.block.opened").replace("{world}", world);
                Bukkit.broadcastMessage(ChatColor.GREEN + openedMsg);
            }
        }, 20L, 20L);
    }

    private String formatDuration(long millis) {
        long seconds = millis / 1000;
        if (seconds >= 86400) return (seconds / 86400) + "д";
        if (seconds >= 3600) return (seconds / 3600) + "ч";
        if (seconds >= 60) return (seconds / 60) + "м";
        return seconds + "с";
    }
}
