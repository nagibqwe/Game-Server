/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.concurrent;

/**
 *
 * @author Administrator
 */
public abstract class AbstractWork implements Runnable
{
    private CommandQueue<AbstractWork> commandQueue;

    public CommandQueue<AbstractWork> getCommandQueue()
    {
        return commandQueue;
    }

    public void setCommandQueue(CommandQueue<AbstractWork> commandQueue)
    {
        this.commandQueue = commandQueue;
    }

    public abstract String getKey();
}
