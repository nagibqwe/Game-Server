package game.core.timer;

import game.core.thread.ServerThread;
import game.core.util.TimeUtils;

/**
 * 定时计时抽象类
 */
public abstract class TimerEvent implements ITimerEvent
{

    //执行线程
    private ServerThread executer;

    //首次执行时间
    private final long firstTriggerTime;

    //剩余执行次数
    private volatile int loop;

    //间隔时间
    private final long period;

    //触发次数
    private volatile long triggerCount;

    /**
     * 默认1秒后执行1次的timer
     */
    public TimerEvent() {
        this.firstTriggerTime = System.currentTimeMillis() + 1000;
        this.loop = 1;
        this.period = -1;
    }

    /**
     * 定时事件
     *
     * @param delay 执行事件
     */
    protected TimerEvent(long delay) {
        this.firstTriggerTime = System.currentTimeMillis() + delay;
        this.loop = 1;
        this.period = -1;
    }

    /**
     * 循环事件
     *
     * @param loop 循环次数 -1表示无限次
     * @param delay 多久之后执行第一次
     * @param period 间隔时间
     */
    protected TimerEvent(int loop, long delay, long period) {
        this.loop = loop;
        this.period = period;
        this.firstTriggerTime = System.currentTimeMillis() + delay;
    }

    /**
     * timer事件触发判断
     *
     * @param time 当前时间
     * @return
     */
    @Override
    public boolean isTimerEventTrigger(long time) {
        if (loop == 0) {
            return false;
        }

        if (time < firstTriggerTime + triggerCount * period) {
            return false;
        }
        ++triggerCount;
        if (loop > 0) {
            loop -= 1;
        }
        return true;
    }

    /**
     * 返回剩余循环次数，等于0表示可以移除
     *
     * @return
     */
    public int getLoop() {
        return loop;
    }

    public void delete() {
        loop = 0;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    public ServerThread getExecuter()
    {
        return executer;
    }

    public void setExecuter(ServerThread executer)
    {
        this.executer = executer;
    }

    /**
     * 此函数主要是为了删除延时比较长的计时器因副本都关闭还不执行的情况下执行的
     *
     * @return
     */
    public boolean willRemove()
    {
        return false;
    }

}
