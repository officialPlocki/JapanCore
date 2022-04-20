
/*
 * Copyright 2022 - Philippe Pflug (plocki)
 *
 * Weiterverwendung nur nach Genehmigung erlaubt.
 * Editieren, Weiterverbreiten, Verwenden für ungenehmigte Zwecke ist nicht gestattet.
 */

package org.japanbuild.japancore.command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.japanbuild.japancore.jobs.JobSystem;
import org.japanbuild.japancore.market.GuiWrapper;
import org.japanbuild.japancore.market.MarketCategory;
import org.japanbuild.japancore.market.MarketItem;
import org.japanbuild.japancore.market.MarketItemManager;
import org.japanbuild.japancore.util.JapanPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MarketCommand implements CommandExecutor, Listener {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player) {
            JapanPlayer japanPlayer = new JapanPlayer(player);
            if(args.length != 1) {
                japanPlayer.player().sendMessage(japanPlayer.getMessage("jobs_market_message_arguments", true) + ": sellAll, MINER, LUMBERJACK, FISHER");
            } else {
                if(args[0].equalsIgnoreCase("sellAll")) {
                    sell(japanPlayer);
                } else if(args[0].equalsIgnoreCase("miner") || args[0].equalsIgnoreCase("lumberjack") || args[0].equalsIgnoreCase("fisher")) {
                    new GuiWrapper().openGUI(player, MarketCategory.valueOf(args[0].toUpperCase()));
                } else {
                    japanPlayer.player().sendMessage(japanPlayer.getMessage("jobs_market_message_arguments", true) + ": sellAll, MINER, LUMBERJACK, FISHER");
                }
            }
        }
        return false;
    }

    private void sell(JapanPlayer japanPlayer) {
        double earn = 0L;
        JobSystem jobSystem = new JobSystem(japanPlayer);
        JobSystem.Jobs job = jobSystem.getJob();
        MarketItemManager manager = new MarketItemManager();
        double bonus = jobSystem.getMultiplier();
        List<Material> materials = new ArrayList<>();
        manager.getMarketItems().forEach((item, category) -> {
            if(category.name().equalsIgnoreCase(job.name())) {
                materials.add(item.getItem());
            }
        });
        ItemStack[] items = japanPlayer.player().getInventory().getStorageContents();
        for (ItemStack item : items) {
            if (item != null) {
                if (materials.contains(item.getType())) {
                    int amount = item.getAmount();
                    MarketItem mItem = manager.getMarketItem(item.getType());
                    if (manager.sellItem(mItem, amount)) {
                        japanPlayer.player().getInventory().remove(item);
                        earn = earn + (((mItem.getPrice() / 2) * amount) * bonus);
                    }
                }
            }
        }
        if(earn > 200) {
            long amount = Math.round(Math.ceil(earn / 200));
            long up = jobSystem.updateLevel();
            if(up > 0) {
                japanPlayer.player().sendMessage(japanPlayer.getMessage("jobs_level_gift", true) + " " + amount + " Yen");
            }
        }
        long money = Math.round(Math.ceil(earn));
        japanPlayer.addMoney(money);
        japanPlayer.player().sendMessage(japanPlayer.getMessage("jobs_market_sold_all", true) + " " + money + " Yen");
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        new GuiWrapper().close((Player) event.getPlayer());
    }

}
