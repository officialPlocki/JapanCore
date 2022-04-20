
/*
 * Copyright 2022 - Philippe Pflug (plocki)
 *
 * Weiterverwendung nur nach Genehmigung erlaubt.
 * Editieren, Weiterverbreiten, Verwenden für ungenehmigte Zwecke ist nicht gestattet.
 */

package org.japanbuild.japancore.market;

import org.bukkit.Material;

public interface MarketItem {

    Material getItem();

    long getPrice();

    long getAvailableAmount();

    long getMaxAmount();

    MarketCategory getCategory();

}
