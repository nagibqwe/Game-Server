/**
 * @date 2014/5/22
 * @author ChenLong
 */
package game.core.command;

import java.util.concurrent.ThreadFactory;

/**
 *
 * @author ChenLong
 */
public class CommandThreadFactory implements ThreadFactory
{
    private final ThreadGroup group;
    private final String threadName;

    public CommandThreadFactory(String threadName)
    {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this.threadName = threadName;
    }

    @Override
    public Thread newThread(Runnable r)
    {
        Thread t = new Thread(group, r, threadName, 0);
        if (t.isDaemon())
            t.setDaemon(false);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}
