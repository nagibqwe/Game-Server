/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.concurrent;

import java.util.ArrayDeque;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Administrator
 * @param <T>
 */
public class CommandQueue<T>
{
    private final Lock lock = new ReentrantLock();
    private final ArrayDeque<T> queue = new ArrayDeque<>();
    private volatile boolean processingCompleted = true;

    /**
     * 获取并移除队列的第一个命令.
     *
     * @return
     */
    public T poll()
    {
        try
        {
            lock.lock();
            return queue.poll();
        }
        finally
        {
            lock.unlock();
        }
    }

    /**
     * 将指定命令添加到队列的末尾.
     *
     * @param command 指定命令
     * @return
     */
    public boolean add(T command)
    {
        try
        {
            lock.lock();
            return queue.add(command);
        }
        finally
        {
            lock.unlock();
        }
    }

    /**
     * 清除队列中的所有命令.
     */
    public void clear()
    {
        try
        {
            lock.lock();
            queue.clear();
        }
        finally
        {
            lock.unlock();
        }
    }

    /**
     * 获取队列中的命令总数.
     *
     * @return
     */
    public int size()
    {
        try
        {
            lock.lock();
            return queue.size();
        }
        finally
        {
            lock.unlock();
        }
    }

    public ArrayDeque<T> getQueue(){
        return queue;
    }

    public boolean isProcessingCompleted()
    {
        return processingCompleted;
    }

    public void setProcessingCompleted(boolean processingCompleted)
    {
        this.processingCompleted = processingCompleted;
    }
}
