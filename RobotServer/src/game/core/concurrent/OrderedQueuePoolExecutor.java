/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.concurrent;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 *
 * @author Administrator
 */
public class OrderedQueuePoolExecutor extends ThreadPoolExecutor
{
    public final static int DEFAULT_MAX_QUEUE_SIZE = 100000; // 10万
    protected static final Logger log = LogManager.getLogger(OrderedQueuePoolExecutor.class);
    private final OrderedQueuePool<Long, AbstractWork> pool = new OrderedQueuePool<>();
    private String name;
    private int corePoolSize;
    private int maxQueueSize;

    public OrderedQueuePoolExecutor(String name, int corePoolSize, int maxQueueSize)
    {
        super(corePoolSize, 2 * corePoolSize, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        this.name = name;
        this.corePoolSize = corePoolSize;
        this.maxQueueSize = maxQueueSize;
    }

    public OrderedQueuePoolExecutor(int corePoolSize)
    {
        this("queue-pool", corePoolSize, DEFAULT_MAX_QUEUE_SIZE);
    }

    public OrderedQueuePool<Long, AbstractWork> getPool(){
        return pool;
    }

    public int getPoolSize(){
        return pool.getTasksQueues().size();
    }

    /**
     * 根据key获取对应队列的元素数量
     * 如果key为-1表示枚举所有数量
     * @param key
     * @return 不会返回空
     */
    public int[] getQueueSize(long key){
        if(key == -1){
            int[] ret = new int[pool.getTasksQueues().size()];
            int idx = 0;
            for(CommandQueue<?> wq : pool.getTasksQueues().values()){
                ret[idx++] = wq.size();
            }
            return ret;
        }
        int[] ret = new int[1];
        ret[0] = pool.getTasksQueues().containsKey(key) ? pool.getTasksQueues().get(key).size() : 0;
        return ret;
    }

    /**
     * 增加执行任务
     *
     * @param key
     * @param work
     * @return
     */
    public boolean addTask(Long key, AbstractWork work)
    {
        key = key % corePoolSize;
        CommandQueue<AbstractWork> queue = pool.getTasksQueue(key);
        boolean run = false;
        boolean result = false;
        synchronized (queue)
        {
            if (maxQueueSize > 0 && queue.size() > maxQueueSize)
            {
                log.error("队列" + name + ", size = " + queue.size() + ", (" + key + ")" + "抛弃指令!");
                queue.clear();
            }
            result = queue.add(work);
            if (result)
            {
                work.setCommandQueue(queue);
                {
                    if (queue.isProcessingCompleted())
                    {
                        queue.setProcessingCompleted(false);
                        run = true;
                    }
                }
            }
            else
            {
                log.error(name + "队列添加任务失败");
            }
        }
        if (run)
        {
            execute(queue.poll());
        }
        return result;
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t)
    {
        super.afterExecute(r, t);

        AbstractWork work = (AbstractWork) r;
        CommandQueue<AbstractWork> queue = work.getCommandQueue();
        if (queue != null)
        {
            AbstractWork afterWork = null;
            synchronized (queue)
            {
                afterWork = queue.poll();
                if (afterWork == null)
                {
                    queue.setProcessingCompleted(true);
                }
            }
            if (afterWork != null)
            {
                execute(afterWork);
            }
            if(queue.size() > 10000){
                log.error(name + "队列似乎过长了 " + queue.size());
                //System.out.println("huhu test 队列似乎过长了 " + queue.size());
            }
        }
        else
        {
            log.error(name + "执行队列为空");
        }
    }

//	/**
//	 * 获取任务执行队列
//	 * @param key
//	 * @return
//	 */
//	public TasksQueue<AbstractWork> getTasksQueue(Long key){
//		key = key % corePoolSize;
//		return pool.getTasksQueue(key);
//	}
    /**
     * 获取剩余任务数量
     *
     * @return
     */
    /*
     public int getTaskCounts()
     {
     int count = super.getActiveCount();

     Iterator<Entry<Long, CommandQueue<AbstractWork>>> iter = pool.getAllCommandQueues().entrySet().iterator();
     while (iter.hasNext())
     {
     Entry<Long, CommandQueue<AbstractWork>> entry = (Entry<Long, CommandQueue<AbstractWork>>) iter.next();
     CommandQueue<AbstractWork> tasksQueue = entry.getValue();
     //			if(tasksQueue.size() > 0){
     //				log.info(entry.getKey() + " queue size:" + tasksQueue.size() + ", is run:" + tasksQueue.isProcessingCompleted());
     //			}
     count += tasksQueue.size();
     }
     return count;
     }
     */
    public void stopAndAwait()
    {
        try
        {
            // 等待将pool中的任务全部提交到executor
            while (pool.getTasksQueues().size() > 0)
                Thread.sleep(1000);

            // 等待executor执行完所有任务
            while (!this.awaitTermination(1, TimeUnit.SECONDS))
            {
            }
        }
        catch (InterruptedException ex)
        {
            log.error("InterruptedException", ex);
        }
    }
}
