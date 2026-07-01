package game.core.command;

import game.core.message.RMessage;

/**
 *
 * <b>处理客户端消息的抽象基类.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public abstract class Handler<M>
{
    
    public abstract void action(RMessage session, M message);
    
//    public final static String PLAYER = "player";

//    private RMessage message; // 待处理的消息九零  一起玩 www.901  75.com

//    private final Map<String, Object> attributes = new HashMap<>(3); // 自定义属性值

//    private ICommandWithParam afterhandler;



//    public void setAfterHanler(ICommandWithParam cmd){
//        afterhandler = cmd;
//    }
//
//    abstract public void action();
//
//    @Override
//    final public void action(){
//        action();
//        afterAction();
//    }
//
//    public void afterAction(){
//        //
//        if(afterhandler != null){
//            afterhandler.action(this);
//        }
//    }

}
