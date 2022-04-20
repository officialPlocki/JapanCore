
/*
 * Copyright 2022 - Philippe Pflug (plocki)
 *
 * Weiterverwendung nur nach Genehmigung erlaubt.
 * Editieren, Weiterverbreiten, Verwenden für ungenehmigte Zwecke ist nicht gestattet.
 */

package org.japanbuild.japancore.market;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.japanbuild.japancore.util.JapanPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GuiWrapper implements Listener {

    private static HashMap<Player, MarketCategory> players;
    private static HashMap<Player, Integer> page;

    public static void init() {
        players = new HashMap<>();
        page = new HashMap<>();
    }

    public void reload(Player player) {
        MarketCategory category = players.get(player);
        JapanPlayer japanPlayer = new JapanPlayer(player);
        Inventory inv = Bukkit.createInventory(null, 5*9);
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
        List<MarketItem> items = new ArrayList<>();
        new MarketItemManager().getMarketItems().forEach((item, category1) -> {
            if(category1.equals(category)) {
                items.add(item);
            }
        });
        int pageSize = inv.getSize()-2;
        List<List<MarketItem>> pages = new ArrayList<>();
        if(items.size() > pageSize) {
            AtomicInteger number = new AtomicInteger(0);
            for(int i = 0; i < Math.ceil(items.size() / pageSize); i++) {
                List<MarketItem> site = new ArrayList<>();
                for(int i2 = 0; i2 < pageSize; i2++) {
                    site.add(items.get(number.get()));
                    number.set(number.get() + 1);
                }
                pages.add(site);
            }
        } else {
            pages.add(items);
        }
        if(pages.size() > 1) {
            if(!(pages.size() == page.get(player))) {
                ItemStack stack = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
                ItemMeta meta = stack.getItemMeta();
                meta.setDisplayName("§a§l»");
                stack.setItemMeta(meta);
            }
        }
        if(page.get(player) != 0) {
            ItemStack stack = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName("§c§l«");
            stack.setItemMeta(meta);
        }
        AtomicInteger n = new AtomicInteger(0);
        List<MarketItem> pageList = pages.get(page.get(player));

        ItemStack reload = new ItemStack(Material.GREEN_DYE);
        ItemMeta reloadMeta = reload.getItemMeta();
        reloadMeta.setDisplayName("§a" + japanPlayer.getMessage("reload", false));
        reload.setItemMeta(reloadMeta);
        inv.setItem(8, reload);

        for(int i = 9; i < inv.getSize()-2; i++) {
            if(n.get() == pageList.size()) {
                n.set(n.get()-1);
            }
            if(pageList.get(n.get()) != null) {
                if(n.get() == pageList.size()) {
                    break;
                }
                MarketItem mItem = pageList.get(n.get());
                ItemStack item = new ItemStack(mItem.getItem());

                ItemMeta meta = item.getItemMeta();

                meta.setLore(List.of("§a",
                        "§a" + japanPlayer.getMessage("price_single", false) + " §8» §7" + mItem.getPrice() + " Yen",
                        "§c" + japanPlayer.getMessage("price_64", false) + " §8» §7" + (mItem.getPrice()*64) + " Yen",
                        "§e" + japanPlayer.getMessage("availableAmount", false) + " §8» §7" + mItem.getAvailableAmount(),
                        "§a"));
                item.setItemMeta(meta);
                inv.setItem(i, item);
                n.set(n.get()+1);
            } else {
                break;
            }
        }
        player.getOpenInventory().getTopInventory().setContents(inv.getStorageContents());
    }

    public void openGUI(Player player, MarketCategory category) {
        players.put(player, category);
        page.put(player, 0);
        JapanPlayer japanPlayer = new JapanPlayer(player);
        Inventory inv = Bukkit.createInventory(null, 5*9, Component.text(japanPlayer.getMessage("jobs_market_inventory_title", false)));
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
        List<MarketItem> items = new ArrayList<>();
        new MarketItemManager().getMarketItems().forEach((item, category1) -> {
            if(category1.equals(category)) {
                items.add(item);
            }
        });

        int pageSize = inv.getSize()-2;

        List<List<MarketItem>> pages = new ArrayList<>();
        if(items.size() > pageSize) {
            AtomicInteger number = new AtomicInteger(0);
            for(int i = 0; i < Math.ceil(items.size() / pageSize); i++) {
                List<MarketItem> site = new ArrayList<>();
                for(int i2 = 0; i2 < pageSize; i2++) {
                    site.add(items.get(number.get()));
                    number.set(number.get() + 1);
                }
                pages.add(site);
            }
        } else {
            pages.add(items);
        }
        if(pages.size() > 1) {
            if(!(pages.size() == page.get(player))) {
                ItemStack stack = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
                ItemMeta meta = stack.getItemMeta();
                meta.setDisplayName("§a§l»");
                stack.setItemMeta(meta);
            }
        }
        if(page.get(player) != 0) {
            ItemStack stack = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName("§c§l«");
            stack.setItemMeta(meta);
        }
        AtomicInteger n = new AtomicInteger(0);
        List<MarketItem> pageList = pages.get(page.get(player));

        ItemStack reload = new ItemStack(Material.GREEN_DYE);
        ItemMeta reloadMeta = reload.getItemMeta();
        reloadMeta.setDisplayName("§a" + japanPlayer.getMessage("reload", false));
        reload.setItemMeta(reloadMeta);
        inv.setItem(8, reload);

        for(int i = 9; i < inv.getSize()-2; i++) {
            if(n.get() == pageList.size()) {
                n.set(n.get()-1);
            }
            if(pageList.get(n.get()) != null) {
                if(n.get() == pageList.size()) {
                    break;
                }
                MarketItem mItem = pageList.get(n.get());
                ItemStack item = new ItemStack(mItem.getItem());

                ItemMeta meta = item.getItemMeta();

                meta.setLore(List.of("§a",
                        "§e §f|§7| §a" + japanPlayer.getMessage("price_single", false) + " §8» §7" + mItem.getPrice() + " Yen",
                        "§e §7|§f| §c" + japanPlayer.getMessage("price_64", false) + " §8» §7" + (mItem.getPrice()*64) + " Yen",
                        "§e  §a»  §e" + japanPlayer.getMessage("availableAmount", false) + " §8» §7" + mItem.getAvailableAmount(),
                        "§a"));
                item.setItemMeta(meta);
                inv.setItem(i, item);
                n.set(n.get()+1);
            } else {
                break;
            }
        }
        player.openInventory(inv);
    }

    public void changeSite(Player player, boolean up) {
        JapanPlayer japanPlayer = new JapanPlayer(player);
        MarketCategory category = players.get(player);
        if(up) {
            page.put(player, page.get(player) + 1);
        } else {
            page.put(player, page.get(player) - 1);
        }
        Inventory inv = Bukkit.createInventory(null, 5*9);
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
        List<MarketItem> items = new ArrayList<>();
        new MarketItemManager().getMarketItems().forEach((item, category1) -> {
            if(category1.equals(category)) {
                items.add(item);
            }
        });

        int pageSize = inv.getSize()-2;

        List<List<MarketItem>> pages = new ArrayList<>();
        if(items.size() > pageSize) {
            AtomicInteger number = new AtomicInteger(0);
            for(int i = 0; i < Math.ceil(items.size() / pageSize); i++) {
                List<MarketItem> site = new ArrayList<>();
                for(int i2 = 0; i2 < pageSize; i2++) {
                    site.add(items.get(number.get()));
                    number.set(number.get() + 1);
                }
                pages.add(site);
            }
        } else {
            pages.add(items);
        }
        if(pages.size() > 1) {
            if(!(pages.size() == page.get(player))) {
                ItemStack stack = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
                ItemMeta meta = stack.getItemMeta();
                meta.setDisplayName("§a§l»");
                stack.setItemMeta(meta);
            }
        }
        if(page.get(player) != 0) {
            ItemStack stack = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName("§c§l«");
            stack.setItemMeta(meta);
        }
        AtomicInteger n = new AtomicInteger(0);
        List<MarketItem> pageList = pages.get(page.get(player));

        ItemStack reload = new ItemStack(Material.GREEN_DYE);
        ItemMeta reloadMeta = reload.getItemMeta();
        reloadMeta.setDisplayName("§a" + japanPlayer.getMessage("reload", false));
        reload.setItemMeta(reloadMeta);
        inv.setItem(8, reload);
        List<MarketItem> used = new ArrayList<>();
        for(int i = 9; i < inv.getSize()-2; i++) {
            if(n.get() == pageList.size()) {
                n.set(n.get()-1);
            }
            if(pageList.get(n.get()) != null) {
                if(n.get() == pageList.size()) {
                    break;
                }
                MarketItem mItem = pageList.get(n.get());
                if(!used.contains(mItem)) {
                    ItemStack item = new ItemStack(mItem.getItem());

                    ItemMeta meta = item.getItemMeta();

                    meta.setLore(List.of("§a",
                            "§a" + japanPlayer.getMessage("price_single", false) + " §8» §7" + mItem.getPrice() + " Yen",
                            "§c" + japanPlayer.getMessage("price_64", false) + " §8» §7" + (mItem.getPrice()*64) + " Yen",
                            "§e" + japanPlayer.getMessage("availableAmount", false) + " §8» §7" + mItem.getAvailableAmount(),
                            "§a"));
                    item.setItemMeta(meta);
                    inv.setItem(i, item);
                    n.set(n.get()+1);
                    used.add(mItem);
                }
            } else {
                break;
            }
        }
        used.clear();
        player.getOpenInventory().getTopInventory().setStorageContents(inv.getStorageContents());
    }

    public void close(Player player) {
        players.remove(player);
        page.remove(player);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        JapanPlayer japanPlayer = new JapanPlayer((Player) event.getWhoClicked());
        if(event.getView().getTitle().equalsIgnoreCase(japanPlayer.getMessage("jobs_market_inventory_title", false))) {
            if(event.getCurrentItem() != null) {
                event.setCancelled(true);
                if(!event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§f ")) {
                    if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§a§l»")) {
                        changeSite(japanPlayer.player(), true);
                        return;
                    } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§c§l«")) {
                        changeSite(japanPlayer.player(), false);
                        return;
                    } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(japanPlayer.getMessage("reload", false))) {
                        reload(japanPlayer.player());
                        return;
                    }
                    MarketItemManager manager = new MarketItemManager();
                    MarketItem item = manager.getMarketItem(event.getCurrentItem().getType());
                    if(item != null) {
                        if(event.isLeftClick()) {
                            if(japanPlayer.hasMoney(item.getPrice())) {
                                if(manager.buyItem(item, 1)) {
                                    if(japanPlayer.isInventoryFull()) {
                                        japanPlayer.player().getLocation().getWorld().dropItem(japanPlayer.player().getLocation(), new ItemStack(item.getItem(), 1));
                                    } else {
                                        japanPlayer.player().getInventory().addItem(new ItemStack(item.getItem(), 1));
                                    }
                                } else {
                                    japanPlayer.player().sendMessage(japanPlayer.getMessage("jobs_market_not_enough", true));
                                }
                            } else {
                                japanPlayer.player().sendMessage(japanPlayer.getMessage("jobs_market_not_enough_money", true) + " " + (item.getPrice() - japanPlayer.getMoney()) + " Yen");
                            }
                        } else {
                            if(japanPlayer.hasMoney(item.getPrice()*64)) {
                                if(manager.buyItem(item, 64)) {
                                    japanPlayer.player().sendMessage(japanPlayer.getMessage("jobs_market_bought", true));
                                    if(japanPlayer.isInventoryFull()) {
                                        japanPlayer.player().getLocation().getWorld().dropItem(japanPlayer.player().getLocation(), new ItemStack(item.getItem(), 64));
                                    } else {
                                        japanPlayer.player().getInventory().addItem(new ItemStack(item.getItem(), 64));
                                    }
                                } else {
                                    japanPlayer.player().sendMessage(japanPlayer.getMessage("jobs_market_not_enough", true));
                                }
                            } else {
                                japanPlayer.player().sendMessage(japanPlayer.getMessage("jobs_market_not_enough_money", true) + " " + ((item.getPrice()*64) - japanPlayer.getMoney()) + " Yen");
                            }
                        }
                    }
                }
            }
        }
    }

}
