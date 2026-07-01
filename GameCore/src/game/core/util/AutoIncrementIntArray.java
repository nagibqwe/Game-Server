/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.util;

import java.util.Arrays;

/**
 * 整数型数组自增处理
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class AutoIncrementIntArray
{
    private int[] array;

    //用于修正数据不能用JSON传递的问题，请不要直接使用此两个接口
    public int[] getArray()
    {
        return array;
    }

    //用于修正数据不能用JSON传递的问题，请不要直接使用此两个接口
    public void setArray(int[] array)
    {
        this.array = array;
    }

    public AutoIncrementIntArray(int size)
    {
        autoIncrement(size);
    }

    public AutoIncrementIntArray()
    {
        autoIncrement(1);
    }

    public int get(int index)
    {
        if (index >= length())
            return 0;

        return array[index];
    }

    public void set(int index, int value)
    {
        autoIncrement(index);
        array[index] = value;
    }

    public void add(int index, int value)
    {
        autoIncrement(index);
        //超范围处理
        if (array[index] > 0){
            int shengNum = Integer.MAX_VALUE - 1 - array[index];
            if (value > shengNum){
                value = shengNum;
            }
        }

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
                int[] old = array;
                array = createArray(index + 1);
                System.arraycopy(old, 0, array, 0, old.length);
            }
        }
    }

    private int[] createArray(int size)
    {
        return new int[size];
    }

    @Override
    public String toString()
    {
        if (null == array)
            return "";
        return "AIA{" + "vs=" + Arrays.toString(array) + ", _s=" + array.length + '}';
    }

    public String ToString(String jian)
    {
        StringBuilder sb = new StringBuilder();
        int _size = array.length;
        for (int i = 0; i <= _size; ++i)
        {
            if (sb.length() > 1)
            {
                sb.append(jian);
            }
            sb.append(get(i));
        }
        return sb.toString();
    }

    public int sum()
    {
        int _size = array.length;
        int sum = 0;
        for (int i = 0; i <= _size; i++)
        {
            sum += get(i);
        }
        return sum;
    }

}
