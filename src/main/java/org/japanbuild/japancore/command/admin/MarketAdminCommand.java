
/*
 * Copyright 2022 - Philippe Pflug (plocki)
 *
 * Weiterverwendung nur nach Genehmigung erlaubt.
 * Editieren, Weiterverbreiten, Verwenden für ungenehmigte Zwecke ist nicht gestattet.
 */

package org.japanbuild.japancore.command.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.japanbuild.japancore.market.MarketItemManager;
import org.japanbuild.japancore.util.JapanPlayer;
import org.jetbrains.annotations.NotNull;

public class MarketAdminCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player) {
            JapanPlayer japanPlayer = new JapanPlayer(player);
            if(sender.hasPermission("japanbuild.market.admin")) {
                if(args.length == 4) {
                    new MarketItemManager().registerItem(args[0].toUpperCase(), Long.parseLong(args[1]), Long.parseLong(args[2]), args[3].toUpperCase());
                    player.sendMessage("registered");
                } else {
                    player.sendMessage("material, price, maxamount, category (FISHER, MINER, LUMBERJACK)");
                }
            } else {
                japanPlayer.player().sendMessage(japanPlayer.getMessage("no_permission", true));
            }
        }
        return false;
    }

}
