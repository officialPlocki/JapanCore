
/*
 * Copyright 2022 - Philippe Pflug (plocki)
 *
 * Weiterverwendung nur nach Genehmigung erlaubt.
 * Editieren, Weiterverbreiten, Verwenden für ungenehmigte Zwecke ist nicht gestattet.
 */

package org.japanbuild.japancore.command;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.japanbuild.japancore.JapanCore;
import org.japanbuild.japancore.jobs.JobSystem;
import org.japanbuild.japancore.util.JapanPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JobCommand implements CommandExecutor, Listener {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player) {
            JapanPlayer japanPlayer = new JapanPlayer(player);
            JobSystem jobSystem = new JobSystem(japanPlayer);
            if(jobSystem.getJob().equals(JobSystem.Jobs.none)) {
                Inventory inv = Bukkit.createInventory(null, 3*9, Component.text(japanPlayer.getMessage("jobs_inventory_title")));
                //filler
                ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
                ItemMeta fillerMeta = filler.getItemMeta();
                fillerMeta.setDisplayName("§f ");
                fillerMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                filler.setItemMeta(fillerMeta);
                for(int i = 0; i < inv.getSize(); i++) {
                    inv.setItem(i, filler);
                }
                //items
                /*
                11, 13, 15
                */
                ItemStack info = new ItemStack(Material.PAPER);
                ItemMeta infoMeta = info.getItemMeta();
                infoMeta.setDisplayName("§f ");
                infoMeta.setLore(Arrays.stream(japanPlayer.getMessage("jobs_inventory_info_lore").split("%n")).collect(Collectors.toList()));
                info.setItemMeta(infoMeta);
                inv.setItem(0, info);
                
                ItemStack miner = new ItemStack(Material.DIAMOND_PICKAXE);
                ItemMeta minerMeta = miner.getItemMeta();
                minerMeta.setDisplayName(japanPlayer.getMessage("jobs_inventory_miner_title"));
                miner.setItemMeta(minerMeta);
                inv.setItem(11, miner);

                ItemStack lumberjack = new ItemStack(Material.WOODEN_AXE);
                ItemMeta lumberjackMeta = lumberjack.getItemMeta();
                lumberjackMeta.setDisplayName(japanPlayer.getMessage("jobs_inventory_lumberjack_title"));
                lumberjack.setItemMeta(lumberjackMeta);
                inv.setItem(13, lumberjack);

                ItemStack fisher = new ItemStack(Material.FISHING_ROD);
                ItemMeta fisherMeta = fisher.getItemMeta();
                fisherMeta.setDisplayName(japanPlayer.getMessage("jobs_inventory_fisher_title"));
                fisher.setItemMeta(fisherMeta);
                inv.setItem(15, fisher);
                player.openInventory(inv);
            } else {
                Inventory inv = Bukkit.createInventory(null, 5*9, Component.text(japanPlayer.getMessage("jobs_level_inventory_title")));
                //filler
                ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
                ItemMeta fillerMeta = filler.getItemMeta();
                fillerMeta.setDisplayName("§f ");
                fillerMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                filler.setItemMeta(fillerMeta);
                for(int i = 0; i < inv.getSize(); i++) {
                    inv.setItem(i, filler);
                }

                ItemStack info = new ItemStack(Material.PAPER);
                ItemMeta infoMeta = info.getItemMeta();
                infoMeta.setDisplayName("§f ");
                infoMeta.setLore(List.of("",
                        "§7" + japanPlayer.getMessage("jobs_level") + ": " + jobSystem.getJobLevel(),
                        "§7" + japanPlayer.getMessage("jobs_points") + ": §a" + jobSystem.getNextLevelCurrent() + "§7 / §c" + jobSystem.getNextLevelNeed(), ""));
                info.setItemMeta(infoMeta);
                inv.setItem(4, info);

                //level
                for(int i = 18; i < 27; i++) {
                    if(jobSystem.getJobLevel() >= (jobSystem.getJobLevel() + (i - 18))) {
                        ItemStack level = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
                        ItemMeta levelMeta = level.getItemMeta();
                        levelMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        levelMeta.setDisplayName("§a" + JapanCore.getTranslationService().translateSingleTime("Level ", japanPlayer.getLanguage()) + (jobSystem.getJobLevel() + (i - 18)));
                        level.setItemMeta(levelMeta);
                        inv.setItem(i, level);
                    } else {
                        ItemStack level = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                        ItemMeta levelMeta = level.getItemMeta();
                        levelMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        levelMeta.setDisplayName("§c" + JapanCore.getTranslationService().translateSingleTime("Level ", japanPlayer.getLanguage()) + (jobSystem.getJobLevel() + (i - 18)));
                        level.setItemMeta(levelMeta);
                        inv.setItem(i, level);
                    }
                }
                ItemStack leave = new ItemStack(Material.BARRIER);
                ItemMeta leaveMeta = leave.getItemMeta();
                leaveMeta.setDisplayName(japanPlayer.getMessage("jobs_level_inventory_leave_title"));
                leaveMeta.setLore(Arrays.stream(japanPlayer.getMessage("jobs_level_inventory_leave_lore").split("%n")).collect(Collectors.toList()));
                leave.setItemMeta(leaveMeta);
                inv.setItem(44, leave);
                player.openInventory(inv);
            }
        }
        return false;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(event.getCurrentItem() != null) {
            JapanPlayer japanPlayer = new JapanPlayer((Player) event.getWhoClicked());
            JobSystem jobSystem = new JobSystem(japanPlayer);
            if(event.getView().getTitle().equalsIgnoreCase(japanPlayer.getMessage("jobs_inventory_title"))) {
                event.setCancelled(true);
                if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(japanPlayer.getMessage("jobs_inventory_miner_title"))) {
                    jobSystem.reset(JobSystem.Jobs.miner);
                    japanPlayer.player().closeInventory();
                    japanPlayer.player().sendMessage(japanPlayer.getMessage("jobs_message_changed", true));
                } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(japanPlayer.getMessage("jobs_inventory_lumberjack_title"))) {
                    jobSystem.reset(JobSystem.Jobs.lumberjack);
                    japanPlayer.player().closeInventory();
                    japanPlayer.player().sendMessage(japanPlayer.getMessage("jobs_message_changed", true));
                } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(japanPlayer.getMessage("jobs_inventory_fisher_title"))) {
                    jobSystem.reset(JobSystem.Jobs.fisher);
                    japanPlayer.player().closeInventory();
                    japanPlayer.player().sendMessage(japanPlayer.getMessage("jobs_message_changed", true));
                }
            } else if(event.getView().getTitle().equalsIgnoreCase(japanPlayer.getMessage("jobs_level_inventory_title"))) {
                event.setCancelled(true);
                if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(japanPlayer.getMessage("jobs_level_inventory_leave_title"))) {
                    jobSystem.reset(JobSystem.Jobs.none);
                    japanPlayer.player().closeInventory();
                    japanPlayer.player().sendMessage(japanPlayer.getMessage("jobs_message_leaved", true));
                }
            }
        }
    }

}
