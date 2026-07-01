package game.core.net.thread.executor;

import com.game.client.Client;
import game.core.command.Handler;
import game.core.message.Dictionary;
import game.core.message.MessageDictionary;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 * @Desc TODO
 * @Date 2020/9/30 18:12
 * @Auth ZUncle
 */
public class MessageCmd implements Cmd {

    static Logger logger = LogManager.getLogger(MessageCmd.class);

    RMessage message;

    public MessageCmd(RMessage message) {
        this.message = message;
    }

    @Override
    public void action() {
        //处理协议的问题
        try {
            long start = TimeUtils.Time();
            // 获取消息字典
            Dictionary dic = MessageDictionary.getInstance().getDictionary(message.getId());
            if (dic != null) {
                Handler handler = dic.getHandlerInstance();
                handler.action(message);
            }
            long offset = TimeUtils.Time() - start;
            if (offset > 200) {
                logger.error("接受消息 message={} id={} offset={}", message.getData().getClass().getSimpleName(), message.getId(), offset);
            }
        } catch (Exception e) {
            // do someting...
            logger.error("Error action msgId = " + message.getId(), e);
        }
    }
}
