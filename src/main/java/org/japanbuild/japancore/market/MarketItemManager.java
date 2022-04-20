
/*
 * Copyright 2022 - Philippe Pflug (plocki)
 *
 * Weiterverwendung nur nach Genehmigung erlaubt.
 * Editieren, Weiterverbreiten, Verwenden für ungenehmigte Zwecke ist nicht gestattet.
 */

package org.japanbuild.japancore.market;

import org.bukkit.Material;
import org.japanbuild.japancore.JapanCore;
import org.japanbuild.japancore.util.ez.Row;
import org.japanbuild.japancore.util.ez.Table;
import org.japanbuild.japancore.util.ox.x.XList;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class MarketItemManager {

    public MarketItemManager() {
        JapanCore.getDatabase().addTable(new Table("market")
                .idColumn()
                .column("material", String.class)
                .column("price", Long.class)
                .column("availableAmount", Long.class)
                .column("maxAmount", Long.class)
                .column("category", String.class));
    }

    public void registerItem(String material, long price, long maxAmount, String category) {
        JapanCore.getDatabase().insert("market", new Row()
                .with("material", Objects.requireNonNull(Material.getMaterial(material)).name())
                .with("price", price)
                .with("availableAmount", 50)
                .with("maxAmount", maxAmount)
                .with("category", MarketCategory.valueOf(category).name()));
    }

    public HashMap<MarketItem, MarketCategory> getMarketItems() {
        HashMap<MarketItem, MarketCategory> map = new HashMap<>();
        XList<Row> rows = JapanCore.getDatabase().select("SELECT * FROM market");
        rows.forEach(row -> {
            MarketCategory category = MarketCategory.valueOf(row.get("category"));
            Material item = Material.valueOf(row.get("material"));
            long price = row.getLong("price");
            long aAmount = row.getLong("availableAmount");
            long mAmount = row.getLong("maxAmount");
            map.put(new MarketItem() {
                @Override
                public Material getItem() {
                    return item;
                }

                @Override
                public long getPrice() {
                    return price;
                }

                @Override
                public long getAvailableAmount() {
                    return aAmount;
                }

                @Override
                public long getMaxAmount() {
                    return mAmount;
                }

                @Override
                public MarketCategory getCategory() {
                    return category;
                }
            }, category);
        });
        return map;
    }

    public MarketItem getMarketItem(Material material) {
        HashMap<MarketItem, MarketCategory> map = getMarketItems();
        AtomicReference<MarketItem> i = new AtomicReference<>(null);
        map.forEach((item, category) -> {
            if(item.getItem().equals(material)) {
                i.set(item);
            }
        });
        return i.get();
    }

    public boolean sellItem(MarketItem item, int amount) {
        if((item.getAvailableAmount() + amount) < item.getMaxAmount()) {
            JapanCore.getDatabase().update("UPDATE market SET availableAmount = ? WHERE material = ?", item.getAvailableAmount() + amount, item.getItem().name());
            return true;
        } else {
            return false;
        }
    }

    public boolean buyItem(MarketItem item, int amount) {
        if(item.getAvailableAmount() >= amount) {
            JapanCore.getDatabase().update("UPDATE market SET availableAmount = ? WHERE material = ?", item.getAvailableAmount() - amount, item.getItem().name());
            return true;
        } else {
            return false;
        }
    }

}
