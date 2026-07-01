package game.core.thread;

import game.core.command.ICommand;
import game.core.command.ICommandFilter;
import game.core.message.RMessage;
import game.core.script.ScriptManager;
import game.core.timer.TimerEvent;
import game.core.util.TimeUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * 游戏服务器主线程
 */
public class ServerThread extends Thread
{
    private Logger log = LogManager.getLogger(ServerThread.class);

    //命令执行队列
    private LinkedBlockingQueue<ICommand> command_queue = new LinkedBlockingQueue<>();

    //过滤器
    private final ArrayList<ICommandFilter> filters = new ArrayList<>();

    //线程名称
    protected String threadName;

    //运行标志
    private boolean stop;

    //时间线程
    private final TimerThread timer;

    private boolean processingCompleted = false;

    public ServerThread(ThreadGroup group, String threadName, TimerThread timerThread) {
        super(group, threadName);
        this.threadName = threadName;
        this.timer = timerThread;
        this.setUncaughtExceptionHandler((t, e) -> {
            log.error(t.getName() + "不知道的异常进程异常挂掉" + e, e);
            ScriptManager.getInstance().error(t.getName() + "  serverThread error 不知道的异常进程异常挂掉了！", Arrays.toString(e.getStackTrace()));
            stop = true;
            command_queue.clear();
        });
    }

    @Override
    public void run() {
        stop = false;
        while (!stop) {
            ICommand command = command_queue.poll();
            if (command == null) {
                try {
                    synchronized (this) {
                        processingCompleted = true;
                        wait();
                    }
                } catch (Exception e) {
                    log.error(e, e);
                }
            } else {
                try {
                    processingCompleted = false;
                    long start = TimeUtils.Time();
                    boolean result = false;
                    for (ICommandFilter filter : filters) {
                        if (!filter.filter(command)) {
                            result = true;
                            break;
                        }
                    }
                    if (result)
                        continue;
                    command.action();
                    long end = TimeUtils.Time();
                    if (end - start > 200) {
                        if (command instanceof RMessage) {
                            RMessage rm = (RMessage) command;
                            log.error(this.getName() + "--> msgId :" + rm.getId() + " -> run:" + (end - start));
                        } else {
                            log.error(this.getName() + "-->" + command.getClass().getSimpleName() + " run:" + (end - start));
                        }
                    }
                } catch (Exception e) {
                    log.error("线程在处理消息是出异常" + command.getClass().getSimpleName() + e, e);
                    ScriptManager.getInstance().error("  serverThread 线程在处理消息是出异常！" + command.getClass().getSimpleName(), Arrays.toString(e.getStackTrace()));
                }
            }
        }
    }

    public void stop(boolean flag) {
        stop = flag;
        this.command_queue.clear();
        try {
            synchronized (this) {
                if (processingCompleted) {
                    processingCompleted = false;
                    notify();
                }
            }
        } catch (Exception e) {
            log.error("Main Thread " + threadName + " Notify Exception:" + e.getMessage());
        }
    }

    /**
     * 添加命令
     *
     * @param command 命令
     * @return
     */
    public boolean addCommand(ICommand command) {
        if (stop) {
            return false;
        }
        try {
            this.command_queue.add(command);
            if (!processingCompleted) {
                return true;
            }
            synchronized (this) {
                if (processingCompleted) {
                    processingCompleted = false;
                    notify();
                }
            }
            return true;
        } catch (Exception e) {
            log.error("Main Thread " + threadName + " Notify Exception:" + e.getMessage());
        }
        return false;
    }


    /**
     * 添加过滤器
     *
     * @param filter 过滤器
     */
    public void addCommandFitler(ICommandFilter filter)
    {
        this.filters.add(filter);
    }

    public String getThreadName()
    {
        return threadName;
    }

    public boolean isStop()
    {
        return stop;
    }

    public int getCommandNumber()
    {
        return command_queue.size();
    }

    /**
     * 添加定时事件
     *
     * @param event
     */
    public void addTimerEvent(TimerEvent event)
    {
        if (timer != null)
        {
            timer.addTimerEvent(this, event);
        }
    }

    /**
     * 移除定时事件
     *
     * @param event
     */
    public void removeTimerEvent(TimerEvent event)
    {
        if (timer != null)
        {
            timer.removeTimerEvent(event);
        }
    }
}
