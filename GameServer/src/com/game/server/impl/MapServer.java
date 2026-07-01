package com.game.server.impl;

import game.core.command.ICommand;
import game.core.command.ICommandFilter;
import game.core.script.ScriptManager;
import game.core.thread.ServerThread;
import game.core.thread.TimerThread;
import game.core.timer.TimerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * 类说明
 */
public abstract class MapServer
{
    protected static Logger LOG = LogManager.getLogger(MapServer.class);
    //服务线名字
    protected final String name;
    //序号
    protected final int serverIndex;
    //命令执行线程
    protected ServerThread thread;
    //删除停止标识
    protected boolean mstop = false;
    //防止线程挂掉了
    private final ThreadGroup myGroup;
    //防止线程挂掉了
    private final TimerThread timer;
    //数量
    protected int num;


    protected MapServer(ThreadGroup group, String name, TimerThread timerThread, int index)
    {
        this.name = name + "_" + index;
        this.serverIndex = index;
        this.myGroup = group;
        this.timer = timerThread;
        this.thread = new ServerThread(group, this.name, timerThread);
    }

    public void start()
    {
        init();
        this.thread.start();
    }

    protected abstract void init();

    public void stop(boolean flag) {
        LOG.info("地图服务器：" + name + "停止了！地图的数量：" + num);
        this.mstop = flag;
        this.thread.stop(flag);
    }

    /**
     * 对链表操作,少用
     *
     * @return
     */
    public int getCurCmdCount()
    {
        return thread.getCommandNumber();
    }

    public void addCommand(ICommand handler) {
        if (thread != null && !thread.isStop()) {
            //添加命令
            thread.addCommand(handler);
        } else {
            LOG.error("地图服务器：" + name + "添加Command时不存在或者停止了！");
        }
    }

    public void addTimerEvent(TimerEvent event)
    {
        bronThread();
        if (thread != null && !thread.isStop())
        {
            thread.addTimerEvent(event);
        }
        else
        {
            LOG.error("地图服务器：" + name + "添加定时事件时不存在或者停止了！");
            try
            {
                throw new Exception();
            }
            catch (Exception e)
            {
                LOG.error(e, e);
            }
        }
    }

    public String getName() {
        return name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isMstop()
    {
        return mstop;
    }

    /**
     * 添加消息过滤器
     *
     * @param filter
     */
    public void addCommandFilter(ICommandFilter filter)
    {
        if (this.thread != null)
        {
            thread.addCommandFitler(filter);
        }
    }

    public int getServerIndex()
    {
        return serverIndex;
    }

    private synchronized void bronThread()
    {
        if (isMstop())
            return;

        if (this.thread != null && !this.thread.isStop())
            return;

        LOG.error(name + " 的线程挂了， 这里要重新生成这个线程，以完成主要的功能， 但是副本的功能是无法恢复了");
        this.thread = new ServerThread(this.myGroup, name, this.timer);
        //补齐所有的事件， 但是还是会有一个事件是无法补齐的
        this.thread.start();
        //这里要写一个异常事件
        try
        {
            throw new Exception();
        }
        catch (Exception e)
        {
            LOG.error(e, e);
            ScriptManager.getInstance().error(name + "线程挂掉了，正在进行重生！", name + "的thread的线程挂了！");
        }
    }
}
