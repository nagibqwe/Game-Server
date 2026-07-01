/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.disruptor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class GameDaemonThreadFactory implements ThreadFactory
{
    final ThreadGroup group;
    final AtomicInteger threadNumber = new AtomicInteger(1);
    final String threadNamePrefix;

    public GameDaemonThreadFactory(String threadNamePrefix)
    {
        this.threadNamePrefix = threadNamePrefix;
        final SecurityManager securityManager = System.getSecurityManager();
        this.group = (securityManager != null) ? securityManager.getThreadGroup()
                : Thread.currentThread().getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable r)
    {
        final Thread thread = new Thread(group, r, threadNamePrefix + "-" + threadNumber.getAndIncrement(), 0);
        if (!thread.isDaemon())
        {
            thread.setDaemon(true);
        }
        if (thread.getPriority() != Thread.NORM_PRIORITY)
        {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;
    }

}
