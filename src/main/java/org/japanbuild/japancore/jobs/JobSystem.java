
/*
 * Copyright 2022 - Philippe Pflug (plocki)
 *
 * Weiterverwendung nur nach Genehmigung erlaubt.
 * Editieren, Weiterverbreiten, Verwenden für ungenehmigte Zwecke ist nicht gestattet.
 */

package org.japanbuild.japancore.jobs;

import org.japanbuild.japancore.JapanCore;
import org.japanbuild.japancore.util.JapanPlayer;

public record JobSystem(JapanPlayer player) {

    public enum Jobs {
        none,
        miner,
        lumberjack,
        fisher
    }

    public void reset(Jobs job) {
        JapanCore.getDatabase().update("UPDATE japanplayer SET job = ? WHERE uuid = ?", job.name(), player.getUUID());
        JapanCore.getDatabase().update("UPDATE japanplayer SET joblevel = ? WHERE uuid = ?", 0, player.getUUID());
        JapanCore.getDatabase().update("UPDATE japanplayer SET jobnextlevelcurrent = ? WHERE uuid = ?", 0, player.getUUID());
        JapanCore.getDatabase().update("UPDATE japanplayer SET jobnextlevelneed = ? WHERE uuid = ?", 10, player.getUUID());
    }

    public Jobs getJob() {
        String job = JapanCore.getDatabase().selectSingleRow("SELECT job FROM japanplayer WHERE uuid = ?", player.getUUID()).get("job");
        return Jobs.valueOf(job);
    }

    public Long getJobLevel() {
        return JapanCore.getDatabase().selectSingleRow("SELECT joblevel FROM japanplayer WHERE uuid = ?", player.getUUID()).getLong("joblevel");
    }

    public Long getNextLevelNeed() {
        return JapanCore.getDatabase().selectSingleRow("SELECT jobnextlevelneed FROM japanplayer WHERE uuid = ?", player.getUUID()).getLong("jobnextlevelneed");
    }

    public Long getNextLevelCurrent() {
        return JapanCore.getDatabase().selectSingleRow("SELECT jobnextlevelcurrent FROM japanplayer WHERE uuid = ?", player.getUUID()).getLong("jobnextlevelcurrent");
    }

    public double getMultiplier() {
        int fifty = Math.round(getJobLevel() / 50);
        return 1.0 + (0.05 * fifty);
    }

    public long updateLevel() {
        JapanCore.getDatabase().update("UPDATE japanplayer SET jobnextlevelcurrent = ? WHERE uuid = ?", getNextLevelCurrent() + 1, player.getUUID());
        if (getNextLevelCurrent() >= getNextLevelNeed()) {
            long money = 120 + (10 * getJobLevel());
            player.addMoney(money);
            JapanCore.getDatabase().update("UPDATE japanplayer SET joblevel = ? WHERE uuid = ?", getJobLevel() + 1, player.getUUID());
            long l = getNextLevelNeed() + (getNextLevelNeed() / 3);
            JapanCore.getDatabase().update("UPDATE japanplayer SET jobnextlevelneed = ? WHERE uuid = ?", l, player.getUUID());
            return money;
        }
        return 0L;
    }

}