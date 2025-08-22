package com.soldinworldblock.listeners;

import com.soldinworldblock.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PortalListener implements Listener {

    private final Main plugin;

    public PortalListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPortal(PlayerPortalEvent event) {
        Player player = event.getPlayer();
        String targetWorld = event.getTo().getWorld().getName();
        if (plugin.getWorldBlockManager().isBlocked(targetWorld)) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + plugin.getConfig().getString("messages.blocked_portal"));
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        String targetWorld = event.getTo().getWorld().getName();
        if (plugin.getWorldBlockManager().isBlocked(targetWorld)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + plugin.getConfig().getString("messages.blocked_portal"));
        }
    }
}
