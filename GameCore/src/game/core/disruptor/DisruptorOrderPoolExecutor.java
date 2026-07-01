package game.core.disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Disruptor的队列模式
 *
 * RingBuffer 大小，必须是 2 的 N 次方
 *
 * @param <T> 环数据
 * @param <K> 第一个参数
 * @param <V> 第二个参数
 */
@SuppressWarnings("unchecked")
public class DisruptorOrderPoolExecutor<T, K, V>
{
    private final Disruptor<T> disruptor;

    private final EventTranslatorTwoArg<T, K, V> etl;

    private final ConcurrentHashMap<K, CommonQueue<V>> dataQueue = new ConcurrentHashMap<>();

    public DisruptorOrderPoolExecutor(String name, EventFactory<T> eventFactory, int size, ProducerType type, WorkHandler<T> handler, int handlerSize, EventTranslatorTwoArg<T, K, V> et)
    {
        this.disruptor = new Disruptor<>(eventFactory,
                size, new GameDaemonThreadFactory(name), type,
                new LiteTimeoutBlockingWaitStrategy(50, TimeUnit.MILLISECONDS));

        WorkHandler[] handlers = new WorkHandler[handlerSize];
        for (int i = 0; i < handlerSize; ++i)
        {
            handlers[i] = handler;
//            disruptor.handleEventsWith(handler).then(afterEvent);
        }
        EventExceptionHandlerWrapper<T> excpetionHandler = new EventExceptionHandlerWrapper<>();
        disruptor.setDefaultExceptionHandler(excpetionHandler);
        
        disruptor.handleEventsWithWorkerPool(handlers);
//        disruptor.after(afterEvent);
        this.etl = et;
    }

    /**
     * 获得任务队列
     *
     * @param key
     * @return
     */
    public CommonQueue<V> getTasksQueue(K key)
    {
        CommonQueue<V> queue = dataQueue.get(key);
        if (queue == null)
        {
            queue = new CommonQueue<>();
            CommonQueue<V> ls = dataQueue.putIfAbsent(key, queue);
            if (ls != null)
            {
                queue = ls;
            }
        }
        return queue;
    }

    public boolean publishEvent(K a, V b)
    {
        CommonQueue<V> queue = getTasksQueue(a);
        V v = queue.PutValue(b);
        if (v != null)
        {
            disruptor.getRingBuffer().publishEvent(etl, a, v);
        }

        return true;
    }

    //往环中有有压入数据
    public boolean publishNext(K a, V b)
    {
        disruptor.getRingBuffer().publishEvent(etl, a, b);
        return true;
    }

    //移除相应该的队列数据
    public void removeDataCache(K k)
    {
        dataQueue.remove(k);
    }

    /**
     * 组队加入队列， 要求两个数组长度一定是相同的
     *
     * @param a 第一个参数集
     * @param b 第二个参数集
     * @return 返回是否成功
     */
    public boolean publishEvents(K[] a, V[] b)
    {
        if (a.length != b.length)
            return false;
        disruptor.getRingBuffer().publishEvents(etl, a, b);
        return true;
    }

    public void start()
    {
        this.disruptor.start();
    }

    public void stop()
    {
        this.disruptor.shutdown();
    }

    public int getDataQueueSize()
    {
        return dataQueue.size();
    }

    public long getBuffSize()
    {
        return this.disruptor.getBufferSize();
    }

    public long getCursor()
    {
        return this.disruptor.getCursor();
    }
    
    //获得剩余的坑数量
    public long getRemainingCapacity(){
        return this.disruptor.getRingBuffer().remainingCapacity();
    }
    
    

}
