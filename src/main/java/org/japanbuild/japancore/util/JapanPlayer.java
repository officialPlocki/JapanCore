package org.japanbuild.japancore.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.japanbuild.japancore.JapanCore;
import org.japanbuild.japancore.jobs.JobSystem;
import org.japanbuild.japancore.util.ez.Row;
import org.japanbuild.japancore.util.ez.Table;

import java.io.*;
import java.util.Base64;
import java.util.HashMap;

public class JapanPlayer {

    private static boolean firstJoin;
    private static Player player;

    public JapanPlayer(Player player) {
        firstJoin = false;
        JapanPlayer.player = player;
        JapanCore.getDatabase().addTable(new Table("japanplayer")
                .idColumn()
                .column("uuid", String.class)
                .column("inventory", String.class)

                .column("offhand", String.class)
                .column("helmet", String.class)
                .column("chestplate", String.class)
                .column("leggings", String.class)
                .column("boots", String.class)
                .column("slot", Integer.class)
                .column("effects", String.class)

                .column("money", Long.class)
                .column("health", Double.class)
                .column("hunger", Integer.class)
                .column("exp", Long.class)
                .column("job", String.class)
                .column("joblevel", Long.class)
                .column("jobnextlevelcurrent", Long.class)
                .column("jobnextlevelneed", Long.class)
                .column("playtime", Long.class)
                .column("lastseen", Long.class)
                .column("online", Boolean.class)
                .column("language", String.class));
        Row row = JapanCore.getDatabase().selectSingleRow("SELECT uuid FROM japanplayer WHERE uuid = ?", getUUID());
        if (row == null) {
            JapanCore.getDatabase().insert("japanplayer", new Row()
                    .with("uuid", player.getUniqueId().toString())
                    .with("inventory", "not set")

                    .with("offhand", "not set")
                    .with("helmet", "not set")
                    .with("chestplate", "not set")
                    .with("leggings", "not set")
                    .with("boots", "not set")
                    .with("slot", 0)
                    .with("effects", "not set")

                    .with("money", 0L)
                    .with("health", player.getHealth())
                    .with("hunger", player.getFoodLevel())
                    .with("exp", (long) player.getExp())
                    .with("job", JobSystem.Jobs.none.name())
                    .with("joblevel", 0)
                    .with("jobnextlevelcurrent", 0)
                    .with("jobnextlevelneed", 10)
                    .with("playtime", 0L)
                    .with("lastseen", System.currentTimeMillis())
                    .with("online", true)
                    .with("language", Lang.EN.name()));
            resetPlayer();
            firstJoin = true;
        }
        JapanCore.getDatabase().update("UPDATE japanplayer SET lastseen = ? WHERE uuid = ?", System.currentTimeMillis(), getUUID());
    }

    private static boolean setup = true;

    public JapanPlayer(Player player, boolean setup) {
        JapanPlayer.setup = setup;
        firstJoin = false;
        JapanPlayer.player = player;
        if(setup) {
            JapanCore.getDatabase().addTable(new Table("japanplayer")
                    .idColumn()
                    .column("uuid", String.class)
                    .column("inventory", String.class)

                    .column("offhand", String.class)
                    .column("helmet", String.class)
                    .column("chestplate", String.class)
                    .column("leggings", String.class)
                    .column("boots", String.class)
                    .column("slot", Integer.class)
                    .column("effects", String.class)

                    .column("money", Long.class)
                    .column("health", Double.class)
                    .column("hunger", Integer.class)
                    .column("exp", Long.class)
                    .column("job", String.class)
                    .column("joblevel", Long.class)
                    .column("jobnextlevelcurrent", Long.class)
                    .column("jobnextlevelneed", Long.class)
                    .column("playtime", Long.class)
                    .column("lastseen", Long.class)
                    .column("online", Boolean.class)
                    .column("language", String.class));
            Row row = JapanCore.getDatabase().selectSingleRow("SELECT uuid FROM japanplayer WHERE uuid = ?", getUUID());
            if (row == null) {
                JapanCore.getDatabase().insert("japanplayer", new Row()
                        .with("uuid", player.getUniqueId().toString())
                        .with("inventory", "not set")

                        .with("offhand", "not set")
                        .with("helmet", "not set")
                        .with("chestplate", "not set")
                        .with("leggings", "not set")
                        .with("boots", "not set")
                        .with("slot", 0)
                        .with("effects", "not set")

                        .with("money", 0L)
                        .with("health", player.getHealth())
                        .with("hunger", player.getFoodLevel())
                        .with("exp", (long) player.getExp())
                        .with("job", JobSystem.Jobs.none.name())
                        .with("joblevel", 0)
                        .with("jobnextlevelcurrent", 0)
                        .with("jobnextlevelneed", 10)
                        .with("playtime", 0L)
                        .with("lastseen", System.currentTimeMillis())
                        .with("online", true)
                        .with("language", Lang.EN.name()));
                resetPlayer();
                firstJoin = true;
            }
            JapanCore.getDatabase().update("UPDATE japanplayer SET lastseen = ? WHERE uuid = ?", System.currentTimeMillis(), getUUID());
        }
    }

    public String getMessage(String key, boolean prefix) {
        return prefix ? "§c§lJapan§a§lBuild §8» §7" + JapanCore.getTranslationService().getTranslation(getLanguage(), key) : JapanCore.getTranslationService().getTranslation(getLanguage(), key);
    }

    public void setOnline(boolean b) {
        if(!setup) {
            return;
        }
        JapanCore.getDatabase().update("UPDATE japanplayer SET online = ? WHERE uuid = ?", b, getUUID());
    }

    public String getMessage(String key, boolean prefix, String... replacements) {
        return prefix ? "§c§lJapan§a§lBuild §8» §7" + JapanCore.getTranslationService().getTranslation(getLanguage(), key, replacements) : JapanCore.getTranslationService().getTranslation(getLanguage(), key, replacements);
    }

    public String getMessage(String key, String... replacements) {
        return JapanCore.getTranslationService().getTranslation(getLanguage(), key, replacements);
    }

    public String getMessage(String key) {
        return JapanCore.getTranslationService().getTranslation(getLanguage(), key);
    }

    public void setLanguage(Lang lang) {
        if(!setup) {
            return;
        }
        JapanCore.getDatabase().update("UPDATE japanplayer SET language = ? WHERE uuid = ?", lang.name(), getUUID());
    }

    public Lang getLanguage() {
        return Lang.valueOf(JapanCore.getDatabase().selectSingleRow("SELECT language FROM japanplayer WHERE uuid = ?", getUUID()).get("language"));
    }

    public Player player() {
        return player;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isFirstJoin() {
        return firstJoin;
    }

    public void savePlayer() {
        if(!setup) {
            return;
        }
        /*StringBuilder potionString = new StringBuilder();
        for(PotionEffect effect : player.getActivePotionEffects()) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(baos);
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert oos != null;
            try {
                oos.writeObject(effect.serialize());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(potionString.length() > 0) {
                potionString.append("%3");
            }
            potionString.append(Base64.getEncoder().encodeToString(baos.toByteArray()));
        }*/
        //JapanCore.getDatabase().update("UPDATE japanplayer SET effects = ? WHERE uuid = ?", potionString, getUUID());

        String serializedStorage = SerializeInventory.itemStackArrayToBase64(player.getInventory().getStorageContents());
        JapanCore.getDatabase().update("UPDATE japanplayer SET inventory = ? WHERE uuid = ?", serializedStorage, getUUID());

        String serializedOffhand = SerializeInventory.itemStackToBase64(player.getInventory().getItemInOffHand());
        JapanCore.getDatabase().update("UPDATE japanplayer SET offhand = ? WHERE uuid = ?", serializedOffhand, getUUID());

        String serializedHelmet = SerializeInventory.itemStackToBase64(player.getInventory().getHelmet());
        JapanCore.getDatabase().update("UPDATE japanplayer SET helmet = ? WHERE uuid = ?", serializedHelmet, getUUID());

        String serializedChestplate = SerializeInventory.itemStackToBase64(player.getInventory().getChestplate());
        JapanCore.getDatabase().update("UPDATE japanplayer SET chestplate = ? WHERE uuid = ?", serializedChestplate, getUUID());

        String serializedLeggings = SerializeInventory.itemStackToBase64(player.getInventory().getLeggings());
        JapanCore.getDatabase().update("UPDATE japanplayer SET leggings = ? WHERE uuid = ?", serializedLeggings, getUUID());

        String serializedBoots = SerializeInventory.itemStackToBase64(player.getInventory().getBoots());
        JapanCore.getDatabase().update("UPDATE japanplayer SET boots = ? WHERE uuid = ?", serializedBoots, getUUID());

        JapanCore.getDatabase().update("UPDATE japanplayer SET slot = ? WHERE uuid = ?", player.getInventory().getHeldItemSlot(), getUUID());
        JapanCore.getDatabase().update("UPDATE japanplayer SET exp = ? WHERE uuid = ?", player.getExp(), getUUID());
        JapanCore.getDatabase().update("UPDATE japanplayer SET health = ? WHERE uuid = ?", player.getHealth(), getUUID());
        JapanCore.getDatabase().update("UPDATE japanplayer SET hunger = ? WHERE uuid = ?", player.getFoodLevel(), getUUID());
    }

    public String getUUID() {
        return player.getUniqueId().toString();
    }

    public String getName() {
        return player.getName();
    }

    public void resetPlayer() {
        if(!setup) {
            return;
        }
        player.getInventory().clear();
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setExp(0);
        //player.getActivePotionEffects().clear();
        savePlayer();
    }

    public void loadPlayer() {
        if(!setup) {
            return;
        }

        /*String[] data = JapanCore.getDatabase().selectSingleRow("SELECT effects FROM japanplayer WHERE uuid = ?", getUUID()).get("effects").split("%3");
        for(String string : data) {
            byte[] bytes = Base64.getDecoder().decode(string);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(
                        new ByteArrayInputStream(bytes));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Object o = null;
            try {
                assert ois != null;
                o = ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            HashMap<String, Object> map = (HashMap<String, Object>) o;
            assert map != null;
            PotionEffectType type = (PotionEffectType) map.get("effect");
            int duration = (int) map.get("duration");
            int amplifier = (int) map.get("amplifier");
            boolean ambient = (boolean) map.get("ambient");
            boolean particles = (boolean) map.get("has-particles");
            boolean icon = (boolean) map.get("has-icon");
            player.getActivePotionEffects().add(new PotionEffect(type, duration, amplifier, ambient, particles, icon));
        }*/

        ItemStack[] serializedStorage = null;
        try {
            serializedStorage = SerializeInventory.itemStackArrayFromBase64(JapanCore.getDatabase().selectSingleRow("SELECT inventory FROM japanplayer WHERE uuid = ?", getUUID()).get("inventory"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ItemStack serializedOffHand = null;
        try {
            String get = "offhand";
            serializedOffHand = SerializeInventory.itemStackFromBase64(JapanCore.getDatabase().selectSingleRow("SELECT " + get + " FROM japanplayer WHERE uuid = ?", getUUID()).get(get));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ItemStack serializedHelmet = null;
        try {
            String get = "helmet";
            serializedHelmet = SerializeInventory.itemStackFromBase64(JapanCore.getDatabase().selectSingleRow("SELECT " + get + " FROM japanplayer WHERE uuid = ?", getUUID()).get(get));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ItemStack serializedChestplate = null;
        try {
            String get = "chestplate";
            serializedChestplate = SerializeInventory.itemStackFromBase64(JapanCore.getDatabase().selectSingleRow("SELECT " + get + " FROM japanplayer WHERE uuid = ?", getUUID()).get(get));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ItemStack serializedLeggings = null;
        try {
            String get = "leggings";
            serializedLeggings = SerializeInventory.itemStackFromBase64(JapanCore.getDatabase().selectSingleRow("SELECT " + get + " FROM japanplayer WHERE uuid = ?", getUUID()).get(get));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ItemStack serializedBoots = null;
        try {
            String get = "boots";
            serializedBoots = SerializeInventory.itemStackFromBase64(JapanCore.getDatabase().selectSingleRow("SELECT " + get + " FROM japanplayer WHERE uuid = ?", getUUID()).get(get));
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.getInventory().setStorageContents(serializedStorage);
        player.getInventory().setItemInOffHand(serializedOffHand);
        player.getInventory().setHelmet(serializedHelmet);
        player.getInventory().setChestplate(serializedChestplate);
        player.getInventory().setLeggings(serializedLeggings);
        player.getInventory().setBoots(serializedBoots);

        player.getInventory().setHeldItemSlot(JapanCore.getDatabase().selectSingleRow("SELECT slot FROM japanplayer WHERE uuid = ?", getUUID()).getInt("slot"));
        player.setExp(JapanCore.getDatabase().selectSingleRow("SELECT exp FROM japanplayer WHERE uuid = ?", getUUID()).getLong("exp"));
        player.setHealth(JapanCore.getDatabase().selectSingleRow("SELECT health FROM japanplayer WHERE uuid = ?", getUUID()).getDouble("health"));
        player.setFoodLevel(JapanCore.getDatabase().selectSingleRow("SELECT hunger FROM japanplayer WHERE uuid = ?", getUUID()).getInt("hunger"));
    }

    public long getMoney() {
        return JapanCore.getDatabase().selectSingleRow("SELECT money FROM japanplayer WHERE uuid = ?", getUUID()).getLong("money");
    }

    public void setMoney(long money) {
        if(!setup) {
            return;
        }
        JapanCore.getDatabase().update("UPDATE japanplayer SET money = ? WHERE uuid = ?", money, getUUID());
    }

    public void resetMoney() {
        setMoney(0);
    }

    public void addMoney(long money) {
        if(!setup) {
            return;
        }
        setMoney(getMoney() + money);
    }

    public void removeMoney(long money) {
        setMoney(getMoney() - money);
    }

    public boolean hasMoney(long money) {
        return money > 0;
    }

    public boolean isInventoryFull(){
        Inventory inv = player.getInventory();
        for (ItemStack item : inv.getContents()) {
            if(item == null) {
                return false;
            }
        }
        return true;
    }

}
