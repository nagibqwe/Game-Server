package game.core.cache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LRULinkedHashMap<K, V> extends LinkedHashMap<K, V>
{

    private static final long serialVersionUID = -3791412708654730531L;
    //最大数量
    private int max = 16;

    private static final int START_NUMBER = 16;

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Lock lock = new ReentrantLock();

    public LRULinkedHashMap(int max)
    {
        super(START_NUMBER, DEFAULT_LOAD_FACTOR, true);
        this.max = max;
    }

    @Override
    public boolean removeEldestEntry(Map.Entry<K, V> eldest)
    {
        return size() > max;
    }

    public List<V> getEldestEntry(int size)
    {
        try
        {
            lock.lock();
            List<V> result = new ArrayList<>();
            Iterator<java.util.Map.Entry<K, V>> iterator = this.entrySet().iterator();
            while (iterator.hasNext())
            {
                if (result.size() >= size)
                {
                    break;
                }
                Map.Entry<K, V> entry = (Map.Entry<K, V>) iterator.next();
                result.add(entry.getValue());
            }

            return result;
        }
        finally
        {
            lock.unlock();
        }
    }

    @Override
    public V get(Object k)
    {
        try
        {
            lock.lock();
            return super.get(k);
        }
        finally
        {
            lock.unlock();
        }
    }

    @Override
    public V put(K key, V value)
    {
        try
        {
            lock.lock();
            return super.put(key, value);
        }
        finally
        {
            lock.unlock();
        }
    }

    @Override
    public V remove(Object key)
    {
        try
        {
            lock.lock();
            return super.remove(key);
        }
        finally
        {
            lock.unlock();
        }
    }
}
