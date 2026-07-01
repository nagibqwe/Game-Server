package game.core.dblog.task;

import game.core.dblog.bean.BaseLogBean;

public class FileLogTask implements Runnable
{
    BaseLogBean log;

    public FileLogTask(BaseLogBean log)
    {
        this.log = log;
    }

    @Override
    public void run()
    {
        log.logToFile();
    }
}
