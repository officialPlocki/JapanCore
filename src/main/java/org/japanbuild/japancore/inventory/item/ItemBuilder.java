
/*
 * Copyright 2022 - Philippe Pflug (plocki)
 *
 * Weiterverwendung nur nach Genehmigung erlaubt.
 * Editieren, Weiterverbreiten, Verwenden für ungenehmigte Zwecke ist nicht gestattet.
 */

package org.japanbuild.japancore.inventory.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ItemBuilder {

    public static HashMap<Player, List<ItemBuilder>> ib = new HashMap<>();
    public static HashMap<String, ItemBuilder> dp = new HashMap<>();

    private final ItemStack itemStack;
    private ClickAction action;

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        action = null;
    }

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        action = null;
    }

    public void setAmount(int amount) {
        itemStack.setAmount(amount);
    }

    public void setDisplayName(String displayName) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(displayName);
        itemStack.setItemMeta(meta);
    }

    public void addItemFlag(ItemFlag flag) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.addItemFlags(flag);
        itemStack.setItemMeta(meta);
    }

    public void addEnchantment(Enchantment enchantment, int level) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.addEnchant(enchantment, level, true);
        itemStack.setItemMeta(meta);
    }

    public void setLore(String... lore) {
        ItemMeta meta = itemStack.getItemMeta();
        String itemID = Objects.requireNonNullElse(meta.getLore().get(meta.getLore().size()-1), UUID.randomUUID().toString());
        List<String> l = Arrays.stream(lore).toList();
        l.add("");
        l.add("");
        l.add("§0" + itemID);
        meta.setLore(l);
        itemStack.setItemMeta(meta);
    }
    /***

     Please use only after setting the DisplayName!

     */
    public void addClickAction(Player player, ClickAction action) {
        this.action = action;
        List<ItemBuilder> list = ib.get(player);
        list.add(this);
        ib.put(player, list);
        dp.put(itemStack.getItemMeta().getDisplayName(), this);
    }

    public ClickAction getAction() {
        return action;
    }

    public ItemStack getItem() {
        return itemStack;
    }

}
