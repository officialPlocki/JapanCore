
/*
 * Copyright 2022 - Philippe Pflug (plocki)
 *
 * Weiterverwendung nur nach Genehmigung erlaubt.
 * Editieren, Weiterverbreiten, Verwenden für ungenehmigte Zwecke ist nicht gestattet.
 */

package org.japanbuild.japancore.command.admin;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.japanbuild.japancore.util.JapanPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class GameModeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player) {
            JapanPlayer japanPlayer = new JapanPlayer(player);
            if(player.hasPermission("japanbuild.command.gamemode")) {
                if(args.length == 1) {
                    if(List.of("0", "survival").contains(args[0].toLowerCase())) {
                        if(player.hasPermission("japanbuild.command.gamemode.survival")) {
                            player.setGameMode(GameMode.SURVIVAL);
                            japanPlayer.player().sendMessage(japanPlayer.getMessage("command_gamemode_changed", true, "SURVIVAL"));
                        } else {
                            japanPlayer.player().sendMessage(japanPlayer.getMessage("no_permission", true));
                        }
                    } else if(List.of("1", "creative").contains(args[0].toLowerCase())){
                        if(player.hasPermission("japanbuild.command.gamemode.creative")) {
                            player.setGameMode(GameMode.CREATIVE);
                            japanPlayer.player().sendMessage(japanPlayer.getMessage("command_gamemode_changed", true, "CREATIVE"));
                        } else {
                            japanPlayer.player().sendMessage(japanPlayer.getMessage("no_permission", true));
                        }
                    } else if(List.of("2", "adventure").contains(args[0].toLowerCase())){
                        if(player.hasPermission("japanbuild.command.gamemode.adventure")) {
                            player.setGameMode(GameMode.ADVENTURE);
                            japanPlayer.player().sendMessage(japanPlayer.getMessage("command_gamemode_changed", true, "ADVENTURE"));
                        } else {
                            japanPlayer.player().sendMessage(japanPlayer.getMessage("no_permission", true));
                        }
                    } else if(List.of("3", "spectator").contains(args[0].toLowerCase())){
                        if(player.hasPermission("japanbuild.command.gamemode.spectator")) {
                            player.setGameMode(GameMode.SPECTATOR);
                            japanPlayer.player().sendMessage(japanPlayer.getMessage("command_gamemode_changed", true, "SPECTATOR"));
                        } else {
                            japanPlayer.player().sendMessage(japanPlayer.getMessage("no_permission", true));
                        }
                    } else {
                        player.sendMessage(japanPlayer.getMessage("gamemode_command_help", true,"gamemode", "survival", "creative", "adventure", "spectator"));
                    }
                } else {
                    player.sendMessage(japanPlayer.getMessage("gamemode_command_help", true,"gamemode", "survival", "creative", "adventure", "spectator"));
                }
            } else {
                japanPlayer.player().sendMessage(japanPlayer.getMessage("no_permission", true));
            }
        }
        return false;
    }

}
