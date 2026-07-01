package game.core.disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Disruptor的队列模式 Created by soko(xysoko@qq.com) on 2017/10/14. copyright 巨匠@雨墨
 *
 * @param <T> 环数据
 * @param <A> 第一个参数
 * @param <B> 第二个参数
 */
@SuppressWarnings("unchecked")
public class DisruptorNoOrderPoolExecutor<T, A, B>
{
    private static final Logger log = LogManager.getLogger(DisruptorNoOrderPoolExecutor.class);
//    private final ExecutorService executor = Executors.newCachedThreadPool();
    private static final int DEFAULTBUFFERSIZE = 1024 * 1024; // RingBuffer 大小，必须是 2 的 N 次方；

    private final Disruptor<T> disruptor;

    private final EventTranslatorTwoArg<T, A, B> etl;

    public DisruptorNoOrderPoolExecutor(EventFactory<T> eventFactory, ProducerType type, WorkHandler<T> handler, EventTranslatorTwoArg<T, A, B> et)
    {
        this.disruptor = new Disruptor<>(eventFactory,
                DEFAULTBUFFERSIZE, DaemonThreadFactory.INSTANCE, type,
                new LiteTimeoutBlockingWaitStrategy(50, TimeUnit.MILLISECONDS));
        EventExceptionHandlerWrapper<T> excpetionHandler = new EventExceptionHandlerWrapper<>();
        disruptor.setDefaultExceptionHandler(excpetionHandler);
        disruptor.handleEventsWithWorkerPool(new WorkHandler[]
        {
            handler, handler, handler, handler, handler, handler, handler, handler
        });
        this.etl = et;
    }

    public DisruptorNoOrderPoolExecutor(EventFactory<T> eventFactory, int size, ProducerType type, WorkHandler<T> handler, int handlerSize, EventTranslatorTwoArg<T, A, B> et)
    {
        this.disruptor = new Disruptor<>(eventFactory,
                size, DaemonThreadFactory.INSTANCE, type,
                new LiteTimeoutBlockingWaitStrategy(50, TimeUnit.MILLISECONDS));

        EventExceptionHandlerWrapper<T> excpetionHandler = new EventExceptionHandlerWrapper<>();
        disruptor.setDefaultExceptionHandler(excpetionHandler);

        WorkHandler[] handlers = new WorkHandler[handlerSize];
        for (int i = 0; i < handlerSize; ++i)
        {
            handlers[i] = handler;
        }
        disruptor.handleEventsWithWorkerPool(handlers);
        this.etl = et;
    }

    public boolean publishEvent(A a, B b)
    {
//        log.info("压入A=" + a + " , B=" + b);
        disruptor.getRingBuffer().publishEvent(etl, a, b);
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

}
