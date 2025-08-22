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
        String msg = plugin.getConfig().getString("messages.block.start").replace("{world}", world).replace("{time}", formatDuration(duration));
        Bukkit.broadcastMessage(ChatColor.RED + msg);
        startCountdown(world, duration);
    }

    public void unblockWorld(String world) {
        blockedWorlds.remove(world);
        String msg = plugin.getConfig().getString("messages.unblock").replace("{world}", world);
        Bukkit.broadcastMessage(ChatColor.GREEN + msg);
    }

    public Map<String, Long> getBlockedWorlds() {
        return blockedWorlds;
    }

    private void startCountdown(String world, long duration) {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (!isBlocked(world)) return;

            long remaining = (blockedWorlds.get(world) - System.currentTimeMillis()) / 1000;
            Map<String, Object> countdown = plugin.getConfig().getConfigurationSection("messages.block.countdown").getValues(false);

            for (String key : countdown.keySet()) {
                if (remaining == Integer.parseInt(key)) {
                    String msg = countdown.get(key).toString().replace("{world}", world);
                    Bukkit.broadcastMessage(ChatColor.YELLOW + msg);
                }
            }

            if (remaining <= 0) {
                unblockWorld(world);
                String openedMsg = plugin.getConfig().getString("messages.block.opened").replace("{world}", world);
                Bukkit.broadcastMessage(ChatColor.GREEN + openedMsg);
            }

        }, 20L, 20L); // проверка каждую секунду
    }

    private String formatDuration(long millis) {
        long seconds = millis / 1000;
        if (seconds >= 86400) return (seconds / 86400) + "д";
        if (seconds >= 3600) return (seconds / 3600) + "ч";
        if (seconds >= 60) return (seconds / 60) + "м";
        return seconds + "с";
    }
}
