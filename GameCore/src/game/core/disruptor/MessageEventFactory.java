package game.core.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * Created by soko(xysoko@qq.com) on 2017/10/14. copyright 巨匠@雨墨
 */
public class MessageEventFactory implements EventFactory<MessageEvent> {
    @Override
    public MessageEvent newInstance() {
        return new MessageEvent();
    }
}
