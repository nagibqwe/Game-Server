package com.game.log;

import com.game.timer.ScheduledTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.ZoneId;

public class ItemCrossDayTask extends ScheduledTask {

    Logger logger = LogManager.getLogger(ItemCrossDayTask.class);

    @Override
    public String getName() {
        return "ItemCrossDayTask";
    }

    @Override
    public long getInitialDelay() {
        return getPeriod() - (System.currentTimeMillis() - LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    @Override
    public long getPeriod() {
        return 24 * 60 * 60 * 1000;
    }

    @Override
    public void run() {
        try {
            LogDataManager.instance.crossDay();
        } catch (Exception e) {
            logger.error("ItemCrossDayTask error", e);
        }
    }

    public boolean cancel() {
        this.future.cancel(false);
        super.cancel();
        return true;
    }
}
