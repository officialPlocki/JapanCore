package org.japanbuild.japancore;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.japanbuild.japancore.command.JobCommand;
import org.japanbuild.japancore.command.LanguageCommand;
import org.japanbuild.japancore.command.PayCommand;
import org.japanbuild.japancore.command.admin.GameModeCommand;
import org.japanbuild.japancore.command.admin.MarketAdminCommand;
import org.japanbuild.japancore.command.MarketCommand;
import org.japanbuild.japancore.command.admin.TeleportCommand;
import org.japanbuild.japancore.inventory.item.InventoryListener;
import org.japanbuild.japancore.listener.PlayerListener;
import org.japanbuild.japancore.market.GuiWrapper;
import org.japanbuild.japancore.util.AzureTranslate;
import org.japanbuild.japancore.util.ScoreBoardManager;
import org.japanbuild.japancore.util.ez.DB;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class JapanCore extends JavaPlugin {

    private static DB db;
    private static YamlConfiguration config;
    private static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        File file = new File("plugins/JapanCore/config.yml");
        config = YamlConfiguration.loadConfiguration(file);
        if(!file.exists()) {
            config.set("mysql.host", "host");
            config.set("mysql.user", "user");
            config.set("mysql.password", "password");
            config.set("mysql.database", "database");
            config.set("translation.azure_translation_key", "key");
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        db = new DB(config.getString("mysql.host"), config.getString("mysql.user"), config.getString("mysql.password"), config.getString("mysql.database"));

        // translations

        getTranslationService().registerTranslation("jobs_inventory_title", "§a§l Jobs");
        getTranslationService().registerTranslation("jobs_inventory_info_lore", "§7 Du erhälst jede 50 Level einen %n §7Geld-Multiplikator bei verkäufen, %n §7 sowie bei jedem Level %n §7 einen kleines Geschenk.");
        getTranslationService().registerTranslation("jobs_inventory_miner_title", "§b§l Minenarbeiter");
        getTranslationService().registerTranslation("jobs_inventory_lumberjack_title", "§6§l Holzfäller");
        getTranslationService().registerTranslation("jobs_inventory_fisher_title", "§a§l Fischer");
        getTranslationService().registerTranslation("jobs_inventory_none_title", "§f§l Arbeitslos");
        getTranslationService().registerTranslation("jobs_level_inventory_title", "§a§l Job Level");
        getTranslationService().registerTranslation("jobs_level", "Aktuelles Level");
        getTranslationService().registerTranslation("jobs_points", "Punkte");
        getTranslationService().registerTranslation("jobs_level_inventory_leave_title", "§c§l Job Kündigen");
        getTranslationService().registerTranslation("jobs_level_inventory_leave_lore", "§7 Dein Level Fortschritt & Multiplikator geht verloren.");
        getTranslationService().registerTranslation("jobs_message_changed", "§7 Du hast deinen Job gewechselt!");
        getTranslationService().registerTranslation("jobs_message_leaved", "§7 Du hast deinen Job gekündigt.");
        getTranslationService().registerTranslation("first_join_notify_message", "§7 Willkommen! - Deine Sprache wurde auf Englisch eingestellt.");
        getTranslationService().registerTranslation("price_64", "Preis pro 64 Stück");
        getTranslationService().registerTranslation("price_single", "Preis pro Stück");
        getTranslationService().registerTranslation("availableAmount", "Verfügbarkeit");
        getTranslationService().registerTranslation("jobs_market_inventory_title", "§b§l Marktplatz");
        getTranslationService().registerTranslation("jobs_market_message_arguments", "Bitte gebe eines der folgenden Argumente an");
        getTranslationService().registerTranslation("jobs_market_not_enough_money", "Du hast nicht genügend Geld! Dir fehlen:");
        getTranslationService().registerTranslation("jobs_market_not_enough", "Aktuell sind nicht genügend Artikel auf lager.");
        getTranslationService().registerTranslation("jobs_market_bought", "Dein Einkauf war erfolgreich!");
        getTranslationService().registerTranslation("jobs_market_sold_all", "Du hast alle verkaufbaren Items verkauft für:");
        getTranslationService().registerTranslation("no_permission", "Du hast dazu keine Rechte.");
        getTranslationService().registerTranslation("reload", "Aktualisieren");
        getTranslationService().registerTranslation("command_error", "Dieser Befehl existiert nicht.");
        getTranslationService().registerTranslation("command_pay_help", "Bitte verwende: /%r < Spieler > < Anzahl >");
        getTranslationService().registerTranslation("language_inventory_title", "§b§l Sprache");
        getTranslationService().registerTranslation("command_teleport_to", "Du wurdest zu %r teleportiert.");
        getTranslationService().registerTranslation("command_teleport_from_to", "Der Spieler %r wurde zu %r teleportiert.");
        getTranslationService().registerTranslation("command_teleport_from_to_error", "Der Spieler %r konnte nicht gefunden werden.");
        getTranslationService().registerTranslation("command_teleport_error", "Der Spieler %r konnte nicht gefunden werden.");
        getTranslationService().registerTranslation("command_teleport_help", "Bitte verwende: /%r < Spieler > < Spieler >");
        getTranslationService().registerTranslation("command_gamemode_changed", "Du hast deinen Spielmodus auf %r gewechselt.");
        getTranslationService().registerTranslation("changed_language", "Du hast deine Sprache erfolgreich gewechselt.");
        getTranslationService().registerTranslation("jobs_level_gift", "Du bist im Job Level Aufgestiegen und hast eine Belohnung erhalten in höhe von:");
        getTranslationService().registerTranslation("gamemode_command_help", "Bitte verwende: /%r <%r (0), %r (1), %r (2), %r (3)>");
        getTranslationService().registerTranslation("command_pay_not_enough_money", "Du hast nicht genügend Geld! Dir fehlen %r Yen.");
        getTranslationService().registerTranslation("command_pay_money_received", "Du hast von %r %r Yen erhalten.");
        getTranslationService().registerTranslation("command_pay_money_sent", "Du hast %r %r Yen gegeben.");
        getTranslationService().registerTranslation("scoreboard_name", "Name");
        getTranslationService().registerTranslation("scoreboard_money", "Geld");
        getTranslationService().registerTranslation("scoreboard_job", "Beruf");
        getTranslationService().registerTranslation("command_pay_all_too_low", "Die Anzahl muss bei einer Zahlung an alle Spieler über 1000 Yen liegen.");

        // commands

        Objects.requireNonNull(this.getCommand("job")).setExecutor(new JobCommand());
        Objects.requireNonNull(this.getCommand("jobs")).setExecutor(new JobCommand());
        Objects.requireNonNull(this.getCommand("market")).setExecutor(new MarketCommand());
        Objects.requireNonNull(this.getCommand("marketplace")).setExecutor(new MarketCommand());
        Objects.requireNonNull(this.getCommand("ma")).setExecutor(new MarketAdminCommand());
        Objects.requireNonNull(this.getCommand("marketadmin")).setExecutor(new MarketAdminCommand());
        Objects.requireNonNull(this.getCommand("lang")).setExecutor(new LanguageCommand());
        Objects.requireNonNull(this.getCommand("language")).setExecutor(new LanguageCommand());
        Objects.requireNonNull(this.getCommand("gm")).setExecutor(new GameModeCommand());
        Objects.requireNonNull(this.getCommand("gamemode")).setExecutor(new GameModeCommand());
        Objects.requireNonNull(this.getCommand("tp")).setExecutor(new TeleportCommand());
        Objects.requireNonNull(this.getCommand("teleport")).setExecutor(new TeleportCommand());;
        Objects.requireNonNull(this.getCommand("pay")).setExecutor(new PayCommand());


        // listener

        Bukkit.getPluginManager().registerEvents(new JobCommand(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new MarketCommand(), this);
        Bukkit.getPluginManager().registerEvents(new GuiWrapper(), this);
        Bukkit.getPluginManager().registerEvents(new LanguageCommand(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);

        // internal system initialization

        GuiWrapper.init();
        ScoreBoardManager.init();

    }

    @Override
    public void onDisable() {

    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static DB getDatabase() {
        return db;
    }

    public static AzureTranslate getTranslationService() {
        return new AzureTranslate(config.getString("translation.azure_translation_key"));
    }

}
