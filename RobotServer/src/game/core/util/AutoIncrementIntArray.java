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

    private int[] values;
    private int _size = 0;

    public AutoIncrementIntArray(int size)
    {
        values = new int[size + 1];
        _size = size;
    }

    public AutoIncrementIntArray()
    {
        values = new int[1];
    }

    public int get(int index)
    {
        if (index > _size)
        {
            return 0;
        }
        return values[index];
    }

    public void set(int index, int value)
    {
        if (index > _size)
        {
            int[] val = new int[index + 1];
            System.arraycopy(values, 0, val, 0, _size);
            values = val;
            _size = index;
        }
        values[index] = value;
    }

    public void add(int index, int value)
    {
        if (get(index) > 0)
        {
            int tmp = values[index];
            if (tmp + value >= Integer.MAX_VALUE)//如果超出来就不增加了
            {
                values[index] = Integer.MAX_VALUE;
            }
            else
            {
                values[index] += value;
            }
        }
        else
        {
            set(index, value);
        }
    }

    public int length()
    {
        return _size;
    }

    public void clean()
    {
        Arrays.fill(values, 0);
    }

    @Override
    public String toString()
    {
        return "AutoIncrementIntArray{" + "values=" + Arrays.toString(values) + ", _size=" + _size + '}';
    }

}
