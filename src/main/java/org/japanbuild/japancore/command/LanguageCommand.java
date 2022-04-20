
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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.japanbuild.japancore.util.JapanPlayer;
import org.japanbuild.japancore.util.Lang;
import org.japanbuild.japancore.util.ScoreBoardManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class LanguageCommand implements CommandExecutor, Listener {
    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player) {
            JapanPlayer japanPlayer = new JapanPlayer(player);
            Inventory inventory = Bukkit.createInventory(null, 3*9, Component.text(japanPlayer.getMessage("language_inventory_title")));
            // filler
            ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            ItemMeta fillerMeta = filler.getItemMeta();
            fillerMeta.setDisplayName("§f ");
            fillerMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            filler.setItemMeta(fillerMeta);
            for(int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, filler);
            }
            // german
            ItemStack german = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta germanMeta = german.getItemMeta();
            germanMeta.setDisplayName("§e§lDeutsch");
            germanMeta.setLore(List.of("§eEmpfohlen"));
            if(japanPlayer.getLanguage().equals(Lang.DE)) {
                germanMeta.addEnchant(Enchantment.KNOCKBACK, -1, true);
                germanMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            german.setItemMeta(germanMeta);
            inventory.setItem(11, german);
            // english
            ItemStack english = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta englishMeta = english.getItemMeta();
            englishMeta.setDisplayName("§c§lEnglish");
            englishMeta.setLore(List.of("§eRecommended, translated"));
            if(japanPlayer.getLanguage().equals(Lang.EN)) {
                englishMeta.addEnchant(Enchantment.KNOCKBACK, -1, true);
                englishMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            english.setItemMeta(englishMeta);
            inventory.setItem(13, english);
            // japanese
            ItemStack japanese = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta japaneseMeta = japanese.getItemMeta();
            japaneseMeta.setDisplayName("§a§l日本語");
            japaneseMeta.setLore(List.of("§e翻訳済み、文法エラーが含まれている可能性があります"));
            if(japanPlayer.getLanguage().equals(Lang.JA)) {
                japaneseMeta.addEnchant(Enchantment.KNOCKBACK, -1, true);
                japaneseMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            japanese.setItemMeta(japaneseMeta);
            inventory.setItem(15, japanese);
            player.openInventory(inventory);
        }
        return false;
    }
    
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        JapanPlayer japanPlayer = new JapanPlayer((Player) event.getWhoClicked());
        if(event.getView().getTitle().equalsIgnoreCase(japanPlayer.getMessage("language_inventory_title"))) {
            if(event.getCurrentItem() != null) {
                if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§e§lDeutsch")) {
                    japanPlayer.setLanguage(Lang.DE);
                    japanPlayer.player().closeInventory();
                    japanPlayer.player().sendMessage(japanPlayer.getMessage("changed_language", true));
                    ScoreBoardManager.wipe(Objects.requireNonNull(((Player) event.getWhoClicked()).getPlayer()));
                    ScoreBoardManager.send(((Player) event.getWhoClicked()).getPlayer());
                } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§c§lEnglish")) {
                    japanPlayer.setLanguage(Lang.EN);
                    japanPlayer.player().closeInventory();
                    japanPlayer.player().sendMessage(japanPlayer.getMessage("changed_language", true));
                    ScoreBoardManager.wipe(Objects.requireNonNull(((Player) event.getWhoClicked()).getPlayer()));
                    ScoreBoardManager.send(((Player) event.getWhoClicked()).getPlayer());
                } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§a§l日本語")) {
                    japanPlayer.setLanguage(Lang.JA);
                    japanPlayer.player().closeInventory();
                    ScoreBoardManager.wipe(Objects.requireNonNull(((Player) event.getWhoClicked()).getPlayer()));
                    ScoreBoardManager.send(((Player) event.getWhoClicked()).getPlayer());
                    japanPlayer.player().sendMessage(japanPlayer.getMessage("changed_language", true));
                }
            }
        }
    }
    
}
