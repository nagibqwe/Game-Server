/**
 * @date 2014/5/12
 * @author ChenLong
 */

package game.core.exception;

/**
 *
 * @author ChenLong
 */
public class ReinitializedException extends GameException
{
    public ReinitializedException()
    {
    }

    public ReinitializedException(String message)
    {
        super(message);
    }

    public ReinitializedException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ReinitializedException(Throwable cause)
    {
        super(cause);
    }
}
