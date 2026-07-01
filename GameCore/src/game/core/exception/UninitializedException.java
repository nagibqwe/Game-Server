/**
 * @date 2014/5/8
 * @author ChenLong
 */
package game.core.exception;

/**
 * 未初始化异常
 * 九  零一起玩 www.901  75.com
 * @author ChenLong
 */
public class UninitializedException extends GameException
{
    public UninitializedException()
    {
    }

    public UninitializedException(String message)
    {
        super(message);
    }

    public UninitializedException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public UninitializedException(Throwable cause)
    {
        super(cause);
    }
}
