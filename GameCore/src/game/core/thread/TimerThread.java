package game.core.thread;

import game.core.timer.ITimerEvent;
import game.core.timer.TimerEvent;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * 游戏服务器计时线程
 */
public class TimerThread extends Timer
{
    private static final Logger LOG = LogManager.getLogger("TimerThread");
    //事件集合
    private final LinkedBlockingQueue<ITimerEvent> events = new LinkedBlockingQueue<>();

    //定时任务
    private TimerTask task;

    //计时器时间
    private final int heartTime;


    public TimerThread(String threadName, int heart)
    {
        super(threadName + "-Timer");
        this.heartTime = heart;
    }

    public void start()
    {
        task = new TimerTask()
        {
            @Override
            public void run()
            {
                long ost = System.currentTimeMillis();
                //事件迭代器
                Iterator<ITimerEvent> it = events.iterator();
                //派发事件
                while (it.hasNext())
                {
                    ITimerEvent iter = it.next();
                    TimerEvent event = (TimerEvent) iter;

                    if (event.willRemove())
                    {
                        it.remove();
                        LOG.info("移除计时事件：" + event.getClass().getSimpleName() + " (" + event.getExecuter() + ")");
                        continue;
                    }

                    if (event.getExecuter() == null) {
                        it.remove();
                        continue;
                    }

                    if (event.isTimerEventTrigger(ost)) {
                        //需要放入执行的线程
                        event.getExecuter().addCommand(event);
                    }

                    //循环次数为0可以移除
                    if (event.getLoop() == 0) {
                        it.remove();
                    }
                }
                long offset = System.currentTimeMillis() - ost;
                if (offset > 10)
                {
                    LOG.error("TIMER事件的时间花费：" + offset + "数量：" + events.size());
                }
            }
        };
        this.schedule(task, 0, heartTime);
    }

    public void stop()
    {
        events.clear();
        if (task != null)
            task.cancel();
        this.cancel();
    }

    /**
     * 添加定时事件
     *
     * @param executor 执行管理器
     * @param event 定时事件
     */
    public void addTimerEvent(ServerThread executor, TimerEvent event)
    {
        if (null == event)
            return;
        event.setExecuter(executor);
        events.add(event);
        LOG.debug("添加计时事件：" + event.getClass().getSimpleName() + " (" + event.getExecuter() + ")" + " , 数量：" + events.size());

    }

    /**
     * 移除定时事件
     *
     * @param event 定时事件
     */
    public void removeTimerEvent(TimerEvent event)
    {
        if (null == event)
            return;
        events.remove(event);
        LOG.debug("移除计时事件：" + event.getClass().getSimpleName() + " (" + event.getExecuter() + ")" + " , 数量：" + events.size());
    }
}
