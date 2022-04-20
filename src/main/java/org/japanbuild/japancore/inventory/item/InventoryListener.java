
/*
 * Copyright 2022 - Philippe Pflug (plocki)
 *
 * Weiterverwendung nur nach Genehmigung erlaubt.
 * Editieren, Weiterverbreiten, Verwenden für ungenehmigte Zwecke ist nicht gestattet.
 */

package org.japanbuild.japancore.inventory.item;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.japanbuild.japancore.JapanCore;

import java.util.List;

public class InventoryListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(event.getCurrentItem() != null) {
            if(event.getView().getTitle().startsWith("§8» §7")) {
                event.setCancelled(true);
                String dp = event.getCurrentItem().getItemMeta().getDisplayName();
                List<ItemBuilder> m = ItemBuilder.ib.get(((Player)event.getWhoClicked()));
                if(m.contains(ItemBuilder.dp.get(dp))) {
                    ItemBuilder builder = m.get(m.indexOf(ItemBuilder.dp.get(dp)));
                    builder.getAction().call(JapanCore.getPlugin(), event);
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        ItemBuilder.ib.get((Player) event.getPlayer()).clear();
    }

}
