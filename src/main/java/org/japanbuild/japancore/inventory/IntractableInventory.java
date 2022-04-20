
/*
 * Copyright 2022 - Philippe Pflug (plocki)
 *
 * Weiterverwendung nur nach Genehmigung erlaubt.
 * Editieren, Weiterverbreiten, Verwenden für ungenehmigte Zwecke ist nicht gestattet.
 */

package org.japanbuild.japancore.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.japanbuild.japancore.inventory.item.ItemBuilder;
import org.japanbuild.japancore.inventory.selective.SelectiveInventory;
import org.japanbuild.japancore.util.JapanPlayer;

public class IntractableInventory {

    private final SelectiveInventory selectiveInventory;
    private final Inventory inventory;
    private int i = 0;

    public IntractableInventory(SelectiveInventory selectiveInventory, String displayName) {
        this.selectiveInventory = selectiveInventory;
        this.inventory = Bukkit.createInventory(null, selectiveInventory.rows(), "§8» §7" + displayName);
        if(selectiveInventory.filler()) {
            ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta fillerMeta = filler.getItemMeta();
            fillerMeta.setDisplayName("§f ");
            fillerMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            filler.setItemMeta(fillerMeta);
            for(int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, filler);
            }
        }

    }

    public void addItem(int slot, ItemBuilder itemBuilder) {
        inventory.setItem(slot, itemBuilder.getItem());
    }

    public void addItem(ItemBuilder itemBuilder) {
        if(!(selectiveInventory.selectiveSlots().length == i)) {
            inventory.setItem(selectiveInventory.selectiveSlots()[i], itemBuilder.getItem());
            i = i +1;
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

}
