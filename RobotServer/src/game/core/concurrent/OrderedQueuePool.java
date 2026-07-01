package game.core.concurrent;

import java.util.concurrent.ConcurrentHashMap;

public class OrderedQueuePool<K, V> {

    ConcurrentHashMap<K, CommandQueue<V>> map = new ConcurrentHashMap<>();

    /**
     * 获得任务队列
     *
     * @param key
     * @return
     */
    public synchronized CommandQueue<V> getTasksQueue(K key) {
//        synchronized (map)
//        {
        CommandQueue<V> queue = map.get(key);
        if (queue == null) {
            queue = new CommandQueue<>();
            CommandQueue<V> ls = map.putIfAbsent(key, queue);
            if (ls != null) {
                queue = ls;
            }
        }
        return queue;
//        }
    }

    /**
     * 获得全部任务队列
     *
     * @return
     */
    public ConcurrentHashMap<K, CommandQueue<V>> getTasksQueues() {
        return map;
    }

    /**
     * 移除任务队列
     *
     * @param key
     */
    public void removeTasksQueue(K key) {
        map.remove(key);
    }
}
