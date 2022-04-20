
/*
 * Copyright 2022 - Philippe Pflug (plocki)
 *
 * Weiterverwendung nur nach Genehmigung erlaubt.
 * Editieren, Weiterverbreiten, Verwenden für ungenehmigte Zwecke ist nicht gestattet.
 */

package org.japanbuild.japancore.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.TabCompleteEvent;
import org.japanbuild.japancore.util.JapanPlayer;
import org.japanbuild.japancore.util.ScoreBoardManager;

import java.util.ArrayList;
import java.util.List;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        ScoreBoardManager.send(event.getPlayer());
        event.joinMessage(null);
        Player player = (event.getPlayer());
        JapanPlayer japanPlayer = new JapanPlayer(player);
        if(japanPlayer.isFirstJoin()) {
            event.getPlayer().sendMessage(japanPlayer.getMessage("first_join_notify_message", true));
        }
        player.getInventory().clear();
        player.setExp(0);
        player.setHealth(20);
        player.setFoodLevel(20);
        //player.getActivePotionEffects().clear();
        japanPlayer.setOnline(true);
        japanPlayer.loadPlayer();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.deathMessage(null);
        event.setKeepInventory(true);
        event.setKeepLevel(true);
    }

    @EventHandler
    public void onHandSwitch(PlayerSwapHandItemsEvent event) {
        if(!event.getPlayer().getOpenInventory().getTopInventory().isEmpty()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if(event.getPlayer().getExp() > 0) {
            event.getPlayer().setExp(event.getPlayer().getExp() / 2);
        }
    }


    @EventHandler
    public void onProcess(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().replaceFirst("/", "").split(" ")[0];
        JapanPlayer japanPlayer = new JapanPlayer(event.getPlayer());
        if(command.contains("bukkit:") ||
                command.equalsIgnoreCase("?") ||
                command.equalsIgnoreCase("about") ||
                command.equalsIgnoreCase("help") ||
                command.equalsIgnoreCase("pl") ||
                command.equalsIgnoreCase("plugins") ||
                command.equalsIgnoreCase("reload") ||
                command.equalsIgnoreCase("rl") ||
                command.equalsIgnoreCase("ver") ||
                command.equalsIgnoreCase("version") ||
                command.equalsIgnoreCase("me") ||
                command.equalsIgnoreCase("pardon-ip") ||
                command.equalsIgnoreCase("recipe") ||
                command.equalsIgnoreCase("ban-ip") ||
                command.equalsIgnoreCase("say") ||
                command.equalsIgnoreCase("tell") ||
                command.equalsIgnoreCase("tellraw")) {
            if(!event.getPlayer().hasPermission("japanbuild.command.bypass")) {
                event.setCancelled(true);
            }
        }

        if(command.contains("op") || command.contains("rl") || command.contains("reload") || command.contains("execute") || command.contains("minecraft:") || command.contains("bukkit:")) {
            event.setCancelled(true);
        }

        if(!Bukkit.getServer().getCommandMap().getKnownCommands().containsKey(command)) {
            event.setCancelled(true);
        }
        if(command.contains("stop")) {
            if(event.getPlayer().hasPermission("japanbuild.command.bypass")) {
                event.setCancelled(false);
            }
        }
        if(event.isCancelled()) {
            event.getPlayer().sendMessage(japanPlayer.getMessage("command_error", true));
        }

    }

    @EventHandler
    public void onTabComplete(TabCompleteEvent event) {
        List<String> list = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(player -> list.add(player.getName()));
        event.setCompletions(list);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.quitMessage(null);
        if(event.getPlayer().isDead()) {
            event.getPlayer().setHealth(1);
        }
        Player player = (event.getPlayer());
        JapanPlayer japanPlayer = new JapanPlayer(player);
        japanPlayer.savePlayer();
        japanPlayer.setOnline(false);
        ScoreBoardManager.wipe(event.getPlayer());
    }

}
