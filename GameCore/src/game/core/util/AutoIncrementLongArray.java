/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.util;

import java.util.Arrays;

/**
 * 长整的数组处理逻辑
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class AutoIncrementLongArray
{
    private long[] array;

    public long[] getArray()
    {
        return array;
    }

    public void setArray(long[] array)
    {
        this.array = array;
    }

    public AutoIncrementLongArray(int size)
    {
        autoIncrement(size);
    }

    public AutoIncrementLongArray()
    {
        autoIncrement(1);
    }

    public long get(int index)
    {
        if (index >= length())
            return 0;
        return array[index];
    }

    public void set(int index, long value)
    {
        autoIncrement(index);
        array[index] = value;
    }

    public void add(int index, long value)
    {
        autoIncrement(index);
        array[index] += value;
    }

    public int length()
    {
        if (null == array)
            return -1;
        return array.length;
    }

    public void clean()
    {
        Arrays.fill(array, 0);
    }

    private void autoIncrement(int index)
    {
        if (null == array)
        {
            array = createArray(index + 1);
        }

        if (index < array.length)
            return;

        synchronized (this)
        {
            if (index >= array.length)
            {
                long[] old = array;
                array = createArray(index + 1);
                System.arraycopy(old, 0, array, 0, old.length);
            }
        }
    }

    private long[] createArray(int size)
    {
        return new long[size];
    }

    @Override
    public String toString()
    {
        if (null == array)
            return "";
        return "AL{" + "vs=" + Arrays.toString(array) + ", _s=" + array.length + '}';
    }

}
