package game.core.message;

import com.google.protobuf.GeneratedMessage;
import java.lang.reflect.InvocationTargetException;
import org.apache.mina.core.session.IoSession;

/**
 *
 * <b>接收消息的结构体.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class RMessage
{

    private int id = 0; // 消息ID

    private Object data = null; // 消息内容

    private IoSession session = null; // 消息发送者的会话对象

    private int order = 0;      // 消息序号

    private int times = 0;      // 客户端发送消息的时间 = times + 登录时间
    
    private long srcId = 0;//来源ID值

    /**
     * 将收到的消息内容从字节数组转换成对象设置，此后，你可以安全的将其转为真实类型来使用.
     *
     * @param clazz 消息类型
     * @param msgData 收到的消息内容
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public void setData(Class<? extends GeneratedMessage> clazz, byte[] msgData)
            throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        // 获取并调用目标消息的parseFrom方法来将消息内容从字节数组转换成对象
        this.data = clazz.getDeclaredMethod("parseFrom", byte[].class).invoke(null, msgData);
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Object getData()
    {
        return data;
    }

    public IoSession getSession()
    {
        return session;
    }

    public void setSession(IoSession session)
    {
        this.session = session;
    }

    public int getOrder()
    {
        return order;
    }

    public void setOrder(int order)
    {
        this.order = order;
    }

    public int getTimes()
    {
        return times;
    }

    public void setTimes(int times)
    {
        this.times = times;
    }

    public long getSrcId()
    {
        return srcId;
    }

    public void setSrcId(long srcId)
    {
        this.srcId = srcId;
    }
    
}
