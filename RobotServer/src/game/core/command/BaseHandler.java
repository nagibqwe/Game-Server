package game.core.command;

import game.core.message.RMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/10/18 10:31
 */
public abstract class BaseHandler extends Handler{

    protected Logger log = LoggerFactory.getLogger(PlayerHandler.class);

    @Override
    public void action(RMessage mess) {
        try{
            long start = System.currentTimeMillis();
            handler(mess);
            long time = System.currentTimeMillis() - start;
            if(time > 200){
                log.warn("execute time:{}ms", time);
            }
        }catch (Exception e){
            log.error("message handler error!", e);
        }
    }

    public abstract void handler(RMessage mess);
}
