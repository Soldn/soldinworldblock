package com.soldinworldblock.listeners;

import com.soldinworldblock.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.Location;

public class PortalAndTeleportListener implements Listener {

    private final Main plugin;

    public PortalAndTeleportListener(Main plugin) {
        this.plugin = plugin;
    }

    private void blockAccess(Player player, String world) {
        // Телепортируем игрока в безопасный мир
        Location spawn = Bukkit.getWorld("world").getSpawnLocation();
        player.teleport(spawn);
        player.sendMessage(ChatColor.RED + plugin.getConfig().getString("messages.blocked_access"));
    }

    @EventHandler
    public void onPortal(PlayerPortalEvent event) {
        if (event.getTo() == null) return;
        Player player = event.getPlayer();
        String world = event.getTo().getWorld().getName();
        if (plugin.getWorldBlockManager().isBlocked(world)) {
            event.setCancelled(true);
            blockAccess(player, world);
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (event.getTo() == null) return;
        String world = event.getTo().getWorld().getName();
        if (plugin.getWorldBlockManager().isBlocked(world)) {
            event.setCancelled(true);
            blockAccess(event.getPlayer(), world);
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        String world = event.getPlayer().getWorld().getName();
        if (plugin.getWorldBlockManager().isBlocked(world)) {
            blockAccess(event.getPlayer(), world);
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String msg = event.getMessage().toLowerCase();
        Player player = event.getPlayer();
        plugin.getWorldBlockManager().getBlockedWorlds().keySet().forEach(world -> {
            if (msg.contains(world)) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + plugin.getConfig().getString("messages.blocked_access"));
            }
        });
    }
}
