
/*
 * Copyright 2022 - Philippe Pflug (plocki)
 *
 * Weiterverwendung nur nach Genehmigung erlaubt.
 * Editieren, Weiterverbreiten, Verwenden für ungenehmigte Zwecke ist nicht gestattet.
 */

package org.japanbuild.japancore.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.japanbuild.japancore.util.JapanPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PayCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player) {
            JapanPlayer japanPlayer = new JapanPlayer(player);
            if(args.length <= 1) {
                player.sendMessage(japanPlayer.getMessage("command_pay_help", true, "pay"));
            } else if(args.length == 2) {
                if(args[0].equalsIgnoreCase("*")) {
                    if(Long.parseLong(args[1]) > 1000L) {
                        long amount = Long.parseLong(args[1]);
                        if(!japanPlayer.hasMoney(amount * Bukkit.getOnlinePlayers().size())) {
                            player.sendMessage(japanPlayer.getMessage("command_pay_not_enough_money", true, "" + (Long.parseLong(args[1]) * Bukkit.getOnlinePlayers().size())));
                        } else {
                            japanPlayer.removeMoney(amount * Bukkit.getOnlinePlayers().size());
                            Bukkit.getOnlinePlayers().forEach(all -> {
                                JapanPlayer a = new JapanPlayer(all);
                                a.addMoney(amount);
                                all.sendMessage(a.getMessage("command_pay_money_received", true,japanPlayer.getName(), amount + ""));
                                player.sendMessage(japanPlayer.getMessage("command_pay_money_sent", true,a.getName(), amount + ""));
                            });
                        }
                    } else {
                        japanPlayer.player().sendMessage(japanPlayer.getMessage("command_pay_all_too_low", true));
                    }
                } else {
                    if(Objects.requireNonNull(Bukkit.getPlayer(args[0])).isOnline()) {
                        if(japanPlayer.hasMoney(Long.parseLong(args[1]))) {
                            japanPlayer.removeMoney(Long.parseLong(args[1]));
                            JapanPlayer target = new JapanPlayer(Bukkit.getPlayer(args[0]));
                            target.addMoney(Long.parseLong(args[1]));
                            target.player().sendMessage(target.getMessage("command_pay_money_received", true,player.getName(), Long.parseLong(args[1]) + ""));
                            player.sendMessage(japanPlayer.getMessage("command_pay_money_sent", true,target.getName(), Long.parseLong(args[1]) + ""));
                        } else {
                            player.sendMessage(japanPlayer.getMessage("command_pay_not_enough_money", true, "" + Long.parseLong(args[1])));
                        }
                    } else {
                        japanPlayer.player().sendMessage(japanPlayer.getMessage("command_teleport_from_to_error", true, args[0].toLowerCase()));
                    }
                }
            }
        }
        return false;
    }

}
