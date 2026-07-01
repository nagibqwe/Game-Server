package game.core.command;

import game.core.message.RMessage;

/**
 *
 * <b>处理客户端消息的抽象基类.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public abstract class Handler
{
   public abstract void action(RMessage mess);
}
