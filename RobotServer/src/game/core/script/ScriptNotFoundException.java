/**
 * @date 2014/5/12
 * @author ChenLong
 */
package game.core.script;

import game.core.exception.GameException;

/**
 * 找不到相应的脚本异常
 *
 * @author ChenLong
 */
public class ScriptNotFoundException extends GameException
{
    public ScriptNotFoundException()
    {
    }

    public ScriptNotFoundException(String message)
    {
        super(message);
    }

    public ScriptNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ScriptNotFoundException(Throwable cause)
    {
        super(cause);
    }
}
