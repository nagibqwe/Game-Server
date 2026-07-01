package game.core.message;

import com.google.protobuf.GeneratedMessage;
import game.core.command.Handler;

/**
 *
 * <b>消息字典.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class Dictionary
{

    private final Class<? extends GeneratedMessage> message;

    private final Handler handler;

    /**
     * 消息字典构造器.
     *
     * @param message 消息类型
     * @param handler 消息对应的处理函数类型
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public Dictionary(
            Class<? extends GeneratedMessage> message,
            Class<? extends Handler> handler) throws InstantiationException, IllegalAccessException
    {
        this.message = message;
        this.handler = handler.newInstance();
    }

    /**
     * 如果消息有对应的处理函数, 则返回一个该处理器的实例对象.
     *
     * @return 如果消息没有对应的处理函数则返回null
     */
    public Handler getHandlerInstance()
    {
        return handler;
    }

    /**
     * 获取消息类型.
     *
     * @return
     */
    public Class<? extends GeneratedMessage> getMessage()
    {
        return message;
    }

}
