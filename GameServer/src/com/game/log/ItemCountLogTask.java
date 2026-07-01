package com.game.log;

import com.game.timer.ScheduledTask;

public class ItemCountLogTask extends ScheduledTask {
    @Override
    public String getName() {
        return "ItemCountLogTask";
    }

    @Override
    public long getInitialDelay() {
        return 10000;
    }

    @Override
    public long getPeriod() {
        return 10000;
    }

    @Override
    public void run() {
        LogDataManager.instance.saveAllByInterval();
    }

    public boolean cancel(){
        this.future.cancel(false);
        super.cancel();
        return true;
    }
}
