package game.core.message;

/**
 * 
 * <b>发送消息的结构体.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 * 
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class SMessage
{
    
    private int id; // 消息ID
    
    private byte[] data; // 消息内容
    
    private long sender; // 发送者Id
    
    public SMessage(int id, byte[] data) {
        this.id = id;
        this.data = data;
    }
    
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public byte[] getData()
    {
        return data;
    }

    public void setData(byte[] data)
    {
        this.data = data;
    }

    public long getSender()
    {
        return sender;
    }

    public void setSender(long sender)
    {
        this.sender = sender;
    }
    
}
