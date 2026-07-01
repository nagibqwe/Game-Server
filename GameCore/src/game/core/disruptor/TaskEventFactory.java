/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 *
 * @author xuchangming <xysoko@qq.com>
 * @param <T>
 */
public class TaskEventFactory<T> implements EventFactory<TaskEvent<T>>
{
    
    @Override
    public TaskEvent<T> newInstance()
    {
        return new TaskEvent<>();
    }

}
