/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.exception;

/**
 *
 * @author Administrator
 */
public class GameException extends RuntimeException
{
    public GameException()
    {
    }

    public GameException(String message)
    {
        super(message);
    }

    public GameException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public GameException(Throwable cause)
    {
        super(cause);
    }
}
