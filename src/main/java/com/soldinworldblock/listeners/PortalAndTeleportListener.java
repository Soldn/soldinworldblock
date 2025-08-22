package com.soldinworldblock.listeners;

import com.soldinworldblock.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class PortalAndTeleportListener implements Listener {

    private final Main plugin;

    public PortalAndTeleportListener(Main plugin) {
        this.plugin = plugin;
    }

    private void blockAccess(Player player) {
        Location spawn = Bukkit.getWorld("world").getSpawnLocation();
        player.teleport(spawn);
        player.sendMessage(ChatColor.RED + plugin.getConfig().getString("messages.blocked_access"));
    }

    @EventHandler
    public void onPortal(PlayerPortalEvent event) {
        if (event.getTo() == null) return;
        if (plugin.getWorldBlockManager().isBlocked(event.getTo().getWorld().getName())) {
            event.setCancelled(true);
            blockAccess(event.getPlayer());
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (event.getTo() == null) return;
        if (plugin.getWorldBlockManager().isBlocked(event.getTo().getWorld().getName())) {
            event.setCancelled(true);
            blockAccess(event.getPlayer());
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        if (plugin.getWorldBlockManager().isBlocked(event.getPlayer().getWorld().getName())) {
            blockAccess(event.getPlayer());
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
