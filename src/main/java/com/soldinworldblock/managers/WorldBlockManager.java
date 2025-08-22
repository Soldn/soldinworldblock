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

    public void blockWorld(String world, String time) {
        long duration = parseTime(time);
        blockedWorlds.put(world, System.currentTimeMillis() + duration);
        Bukkit.broadcastMessage(ChatColor.RED + "Мир " + world + " закрыт на " + time);
        startCountdown(world, duration);
    }

    public void unblockWorld(String world) {
        blockedWorlds.remove(world);
        Bukkit.broadcastMessage(ChatColor.GREEN + "Мир " + world + " теперь открыт!");
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

    public String getStatus() {
        StringBuilder sb = new StringBuilder(ChatColor.YELLOW + "Статус миров:
");
        for (Map.Entry<String, Long> entry : blockedWorlds.entrySet()) {
            long remaining = (entry.getValue() - System.currentTimeMillis()) / 1000;
            sb.append(ChatColor.RED).append(entry.getKey()).append(" - ").append(remaining).append(" сек
");
        }
        return sb.toString();
    }

    private long parseTime(String time) {
        long multiplier = 1000;
        if (time.endsWith("h")) return Long.parseLong(time.replace("h", "")) * 3600 * multiplier;
        if (time.endsWith("d")) return Long.parseLong(time.replace("d", "")) * 86400 * multiplier;
        if (time.endsWith("m")) return Long.parseLong(time.replace("m", "")) * 60 * multiplier;
        return Long.parseLong(time) * multiplier;
    }

    private void startCountdown(String world, long duration) {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (!isBlocked(world)) return;
            long remaining = (blockedWorlds.get(world) - System.currentTimeMillis()) / 1000;
            if (remaining == 3600 || remaining == 1800 || remaining == 60 || remaining == 10 || remaining == 5 || remaining == 1) {
                Bukkit.broadcastMessage(ChatColor.YELLOW + "Мир " + world + " будет открыт через " + remaining + " сек!");
            }
        }, 20L, 1200L);
    }
}
