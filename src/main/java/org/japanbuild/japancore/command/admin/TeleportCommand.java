
/*
 * Copyright 2022 - Philippe Pflug (plocki)
 *
 * Weiterverwendung nur nach Genehmigung erlaubt.
 * Editieren, Weiterverbreiten, Verwenden für ungenehmigte Zwecke ist nicht gestattet.
 */

package org.japanbuild.japancore.command.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.japanbuild.japancore.util.JapanPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TeleportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player) {
            JapanPlayer japanPlayer = new JapanPlayer(player);
            if(player.hasPermission("japanbuild.command.teleport")) {
                if(args.length == 0) {
                    player.sendMessage(japanPlayer.getMessage("command_teleport_help", true, "teleport"));
                } else if(args.length == 1) {
                    if(Objects.requireNonNull(Bukkit.getPlayer(args[0])).isOnline()) {
                        player.sendMessage(japanPlayer.getMessage("command_teleport_to", true, args[0]));
                    } else {
                        player.sendMessage(japanPlayer.getMessage("command_teleport_to_error", true, args[0]));
                    }
                } else if(args.length == 2) {
                    if(Objects.requireNonNull(Bukkit.getPlayer(args[0])).isOnline()) {
                        if(Objects.requireNonNull(Bukkit.getPlayer(args[1])).isOnline()) {
                            player.sendMessage(japanPlayer.getMessage("command_teleport_from_to", true, args[0], args[1]));
                        } else {
                            player.sendMessage(japanPlayer.getMessage("command_teleport_from_to_error", true, args[1]));
                        }
                    } else {
                        player.sendMessage(japanPlayer.getMessage("command_teleport_from_to_error", true, args[0]));
                    }
                } else {
                    player.sendMessage(japanPlayer.getMessage("command_teleport_help", true, "teleport"));
                }
            } else {
                japanPlayer.player().sendMessage(japanPlayer.getMessage("no_permission", true));
            }
        }
        return false;
    }

}
