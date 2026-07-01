package game.core.net.thread.config;

/**
 *
 * 服务器线程配置类信息
 */
public class ThreadConfig
{
    //线程名称
    private String threadName;
    //心跳间隔
    private int heart;

    public String getThreadName()
    {
        return threadName;
    }

    public void setThreadName(String threadName)
    {
        this.threadName = threadName;
    }

    public int getHeart()
    {
        return heart;
    }

    public void setHeart(int heart)
    {
        this.heart = heart;
    }

}
