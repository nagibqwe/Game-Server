/**
 * @date 2014/3/25 11:17
 * @author ChenLong
 */
package game.core.command;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.time.StopWatch;

/**
 * <b>处理各种命令的单线程基类</b>
 *
 * @author ChenLong
 */
public abstract class CommandProcessor
{
    protected final ExecutorService executor;
    private final StopWatch stopWatch;

    public CommandProcessor(String threadName)
    {
        this.executor = Executors.newSingleThreadExecutor(new CommandThreadFactory(threadName));
        this.stopWatch = new StopWatch();
    }

    /**
     * 停止Processor.
     *
     * @param immediately 是否立即停止, 立即停止将不处理已提交但还未被执行的命令
     */
    public void stop(boolean immediately)
    {
        if (immediately)
            executor.shutdownNow();
        else
            executor.shutdown();
    }

    /**
     * 是否已关闭, 如果isCompleteAllCommand为true, 则已经执行过stop, 并且已提交的命令全部执行完的情况下返回true
     *
     * @param isCompleteAllCommand 是否完成所有任务
     * @return
     */
    public boolean isStop(boolean isCompleteAllCommand)
    {
        if (isCompleteAllCommand)
            return executor.isTerminated();
        else
            return executor.isShutdown();
    }

    /**
     * 等待所有任务完成
     */
    public void awaitStop()
    {
        boolean loop = true;
        do
        {
            try
            {
                loop = !executor.awaitTermination(200, TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException ex)
            {
                writeError("InterruptedException", ex);
            }
        } while (loop);
    }

    public void stopAndAwaitStop(boolean immediately)
    {
        stop(immediately);
        awaitStop();
    }

    /**
     * 添加一个待处理的命令.
     *
     * @param command 待处理的命令
     */
    public void addCommand(ICommand command)
    {
        Validate.notNull(command, "command must not be null");
        executor.submit(new Runnable()
        {
            private ICommand command;

            public Runnable setCommand(ICommand command)
            {
                this.command = command;
                return this;
            }

            @Override
            public void run()
            {
                try
                {
                    stopWatch.reset();
                    stopWatch.start();
                    doCommand(command);
                    stopWatch.stop();
                    doCommandStatistic(stopWatch.getTime(), command);
                }
                catch (Throwable t)
                {
                    writeError("doCommand error! cause:", t);
                }
            }
        }.setCommand(command));
    }

    /**
     * 命令处理入口，默认调用ICommand的action方法来执行命令，子类可选择覆写来实现个性化需求.
     *
     * @param command 待处理的命令
     */
    protected void doCommand(ICommand command)
    {
        command.action();
    }

    /**
     * 统计每次doCommand消耗时间
     *
     * @param useTime 执行doCommand消耗时间(毫秒)
     * @param command
     */
    protected void doCommandStatistic(long useTime, ICommand command)
    {
    }

    /**
     * 如果处理过程中发生异常，记录错误日志信息.
     *
     * @param message 错误信息描述
     */
    public abstract void writeError(String message);

    /**
     * 如果处理过程中发生异常，记录错误日志信息.
     *
     * @param message 错误信息描述
     * @param t 产生错误的异常类
     */
    public abstract void writeError(String message, Throwable t);
}
