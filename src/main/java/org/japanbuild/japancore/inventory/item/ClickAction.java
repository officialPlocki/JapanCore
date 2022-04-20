
/*
 * Copyright 2022 - Philippe Pflug (plocki)
 *
 * Weiterverwendung nur nach Genehmigung erlaubt.
 * Editieren, Weiterverbreiten, Verwenden für ungenehmigte Zwecke ist nicht gestattet.
 */

package org.japanbuild.japancore.inventory.item;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;

public record ClickAction() {

    public void call(Plugin plugin, InventoryClickEvent event) {

    }

}
