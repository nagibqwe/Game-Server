/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.disruptor;

import com.lmax.disruptor.ExceptionHandler;
import game.core.script.ScriptManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 异常收集处理
 *
 * @author xuchangming <xysoko@qq.com>
 * @param <T>
 */
public class EventExceptionHandlerWrapper<T> implements ExceptionHandler<T>
{
    private static final Logger log = LogManager.getLogger(EventExceptionHandlerWrapper.class);

    @Override
    public void handleEventException(Throwable thrwbl, long l, T t)
    {
        log.error("处理队列时失败了， l = " + l + " t=" + t.getClass().getSimpleName(), thrwbl);
        ScriptManager.getInstance().error(" l = " + l + " t=" + t.getClass().getSimpleName(), thrwbl.getMessage());
    }

    @Override
    public void handleOnStartException(Throwable thrwbl)
    {
        log.error("队列时申请开始时失败了", thrwbl);
    }

    @Override
    public void handleOnShutdownException(Throwable thrwbl)
    {
        log.error("队列时结束时失败了", thrwbl);
    }

}
