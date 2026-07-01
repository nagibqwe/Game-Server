/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.disruptor;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 增加一个队列实现处理
 *
 * @author xuchangming <xysoko@qq.com>
 * @param <V> 存入值实例
 */
public class CommonQueue<V>
{
    private final LinkedBlockingQueue<V> cache = new LinkedBlockingQueue<>();
    private volatile boolean readComplete = false;

    public synchronized V PutValue(V v)
    {
        cache.add(v);
        if (!readComplete)
        {
            readComplete = true;
            return cache.poll();
        }
        else
        {
            return null;
        }
    }

    public synchronized V getNewValue()
    {
        if (cache.size() < 1)
        {
            readComplete = false;
            return null;
        }
        readComplete = true;
        return cache.poll();
    }

    public int size() {
        return this.cache.size();
    }

}
