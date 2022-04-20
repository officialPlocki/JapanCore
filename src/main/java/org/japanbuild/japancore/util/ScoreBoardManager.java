
/*
 * Copyright 2022 - Philippe Pflug (plocki)
 *
 * Weiterverwendung nur nach Genehmigung erlaubt.
 * Editieren, Weiterverbreiten, Verwenden für ungenehmigte Zwecke ist nicht gestattet.
 */

package org.japanbuild.japancore.util;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.japanbuild.japancore.jobs.JobSystem;

import java.util.HashMap;
import java.util.Objects;

public class ScoreBoardManager {

    private static final HashMap<Scoreboard, Player> boards = new HashMap<>();

    public static void init() {
        new Thread(() -> {
            for(;;) {
                for(Scoreboard scoreboard : boards.keySet()) {
                    if(boards.get(scoreboard).isOnline()) {
                        Player player = boards.get(scoreboard);
                        //UPDATE
                        JapanPlayer japanPlayer = new JapanPlayer(player);
                        JobSystem jobSystem = new JobSystem(japanPlayer);

                        Objects.requireNonNull(scoreboard.getTeam("money")).suffix(Component.text("§8» §7" + japanPlayer.getMoney() + " Yen"));
                        Team job = scoreboard.getTeam("job");
                        assert job != null;
                        String s = "error";
                        if(jobSystem.getJob().equals(JobSystem.Jobs.none)) {
                            s = japanPlayer.getMessage("jobs_inventory_none_title");
                        } else if(jobSystem.getJob().equals(JobSystem.Jobs.fisher)) {
                            s = japanPlayer.getMessage("jobs_inventory_fisher_title");
                        } else if(jobSystem.getJob().equals(JobSystem.Jobs.miner)) {
                            s = japanPlayer.getMessage("jobs_inventory_miner_title");
                        } else if(jobSystem.getJob().equals(JobSystem.Jobs.lumberjack)) {
                            s = japanPlayer.getMessage("jobs_inventory_lumberjack_title");
                        }
                        job.prefix(Component.text(s.replaceAll(" ", "") + " "));
                        job.suffix(Component.text("§7(" + jobSystem.getJobLevel() + ")"));
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void send(Player player) {
        JapanPlayer japanPlayer = new JapanPlayer(player);
        JobSystem jobSystem = new JobSystem(japanPlayer);
        String server = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class).getOnlinePlayer(player.getUniqueId()).getConnectedService().getServerName();
        String job = "error";
        if(jobSystem.getJob().equals(JobSystem.Jobs.none)) {
            job = japanPlayer.getMessage("jobs_inventory_none_title");
        } else if(jobSystem.getJob().equals(JobSystem.Jobs.fisher)) {
            job = japanPlayer.getMessage("jobs_inventory_fisher_title");
        } else if(jobSystem.getJob().equals(JobSystem.Jobs.miner)) {
            job = japanPlayer.getMessage("jobs_inventory_miner_title");
        } else if(jobSystem.getJob().equals(JobSystem.Jobs.lumberjack)) {
            job = japanPlayer.getMessage("jobs_inventory_lumberjack_title");
        }
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective("aaa", "bbb", Component.text("§7" + server), RenderType.HEARTS);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.getScore("§a").setScore(9);

        Team money = scoreboard.registerNewTeam("money");
        money.prefix(Component.text("§k"));
        money.addEntry(ChatColor.AQUA.toString());

        if(japanPlayer.getLanguage().equals(Lang.DE)) {
            objective.getScore("§b§lName").setScore(8);
            objective.getScore("§8» §7" + japanPlayer.getName()).setScore(7);
            objective.getScore("§b§lGeld").setScore(5);
            money.suffix(Component.text("§8» §7" + japanPlayer.getMoney() + " Yen"));
            objective.getScore("§b§lBeruf").setScore(2);
        } else if(japanPlayer.getLanguage().equals(Lang.EN)) {
            objective.getScore("§b§lName").setScore(8);
            objective.getScore("§8» §7" + japanPlayer.getName()).setScore(7);
            objective.getScore("§b§lMoney").setScore(5);
            money.suffix(Component.text("§8» §7" + japanPlayer.getMoney() + " Yen"));
            objective.getScore("§b§lJob").setScore(2);
        } else if(japanPlayer.getLanguage().equals(Lang.JA)) {
            objective.getScore("§b§l姓").setScore(8);
            objective.getScore("§8» §7" + japanPlayer.getName()).setScore(7);
            objective.getScore("§b§lお金").setScore(5);
            money.suffix(Component.text("§8» §7" + japanPlayer.getMoney() + " Yen"));
            objective.getScore("§b§l職業").setScore(2);
        }
        objective.getScore("§e").setScore(6);
        objective.getScore(ChatColor.AQUA.toString()).setScore(4);
        objective.getScore("§l").setScore(3);

        Team secondJob = scoreboard.registerNewTeam("job");
        secondJob.prefix(Component.text(job.replaceAll(" ", "") + " "));
        secondJob.suffix(Component.text("§7(" + jobSystem.getJobLevel() + ")"));
        secondJob.addEntry(ChatColor.DARK_AQUA.toString());

        objective.getScore(ChatColor.DARK_AQUA.toString()).setScore(1);

        objective.getScore("§f").setScore(0);
        boards.put(scoreboard, player);
        player.setScoreboard(scoreboard);
    }

    public static void wipe(Player player) {
        boards.remove(player.getScoreboard());
        player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
    }

}
