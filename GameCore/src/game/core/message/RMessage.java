package game.core.message;

import com.google.protobuf.GeneratedMessage;
import game.core.command.ICommand;
import io.netty.channel.ChannelHandlerContext;
import java.lang.reflect.InvocationTargetException;

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
public class RMessage implements ICommand
{

    private int id = 0; // 消息ID

    private Object data = null; // 消息内容

    private ChannelHandlerContext context = null; // 消息发送者的会话对象

    private byte[] byteData;

    private int order = 0;      // 消息序号

    private int times = 0;      // 客户端发送消息的时间 = times + 登录时间

    private long srcId = 0;//来源ID值

    //执行者
    private Object executor;

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

    public <T> T  getData()
    {
        return (T)data;
    }

    public ChannelHandlerContext getContext()
    {
        return context;
    }

    public void setContext(ChannelHandlerContext context)
    {
        this.context = context;
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

    public byte[] getByteData()
    {
        return byteData;
    }

    public void setByteData(byte[] byteData)
    {
        this.byteData = byteData;
    }

    @Override
    public void action()
    {
        Dictionary dic = MessageDictionary.getInstance().getDictionary(id);
        if (dic != null)
        {
            dic.getHandlerInstance().action(this, data);
        }
    }

    public <T> T getExecutor()
    {
        return (T)executor;
    }

    public void setExecutor(Object executor)
    {
        this.executor = executor;
    }

    @Override
    public String toString()
    {
        return "RMessage{" + "id=" + id + ", times=" + times + '}';
    }

}
