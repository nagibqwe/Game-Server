/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.disruptor;

/**
 * 参数任务事件处理
 *
 * @author xuchangming <xysoko@qq.com>
 * @param <T> 任务事件
 */
public class TaskEvent<T>
{
    private int type;

    private T obj;

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public T getObj()
    {
        return obj;
    }

    public void setObj(T obj)
    {
        this.obj = obj;
    }
}
