package game.core.net.thread.executor;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorTwoArg;
import com.lmax.disruptor.WorkHandler;
import game.core.disruptor.CommonQueue;
import game.core.disruptor.DisruptorOrderPoolExecutor;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Desc TODO
 * @Date 2020/9/30 17:42
 * @Auth ZUncle
 */
public class CmdWorkHandler implements EventFactory<CmdEvent>, WorkHandler<CmdEvent>, EventTranslatorTwoArg<CmdEvent, Long, Cmd> {

    static Logger logger = LogManager.getLogger(CmdWorkHandler.class);

    DisruptorOrderPoolExecutor<CmdEvent, Long, Cmd> executor;

    public void setExecutor(DisruptorOrderPoolExecutor<CmdEvent, Long, Cmd> executor) {
        this.executor = executor;
    }

    @Override
    public CmdEvent newInstance() {
        return new CmdEvent();
    }

    @Override
    public void translateTo(CmdEvent cmdEvent, long l, Long key, Cmd cmd) {
        cmdEvent.setKey(key);
        cmdEvent.setCmd(cmd);
    }

    @Override
    public void onEvent(CmdEvent event) throws Exception {

        Cmd cmd = event.getCmd();
        cmd.action();
        afterEvent(event.getKey());
    }

    //TODO 查找队列下一个事件
    private void afterEvent(long key) {
        CommonQueue<Cmd> queue = this.executor.getTasksQueue(key);
        if (queue == null)
            return;
        if (queue.size() > 1000) {
            logger.error("消息队列长度 len={}, queue={}", queue.size(), queue);
        }
        Cmd cmd = queue.getNewValue();
        if (cmd == null)
            return;
        //压入新的队列
        this.executor.publishNext(key, cmd);
    }
}
