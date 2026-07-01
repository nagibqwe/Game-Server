package game.core.net.codec;

import game.core.net.server.ServerContext;
import game.core.util.SessionUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * 
 * <b>通信解码器.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 * 
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class ServerProtocolDecoder implements ProtocolDecoder
{

    static final Logger LOG = LogManager.getLogger(ServerProtocolDecoder.class);

    private static final String CONTEXT = "CONTEXT";

    private static final String START_TIME = "START_TIME";

    private static final String RECEIVE_COUNT = "RECEIVE_COUNT";
    
    private static final List<Integer> ignoreMessage = new ArrayList<>();
    
    private final int maxSize, maxCount;
   
    /**
     * 通信解码器的构造函数.
     *
     * @param maxSize 设置每次从一个Session接收的最大字节数，超过将拒绝服务并断开连接
     * @param maxCount 设置每秒最多处理同一个Session发送的多少条消息，超过将拒绝服务并断开连接
     */
    public ServerProtocolDecoder(int maxSize, int maxCount)
    {
       this.maxSize = maxSize;
       this.maxCount = maxCount;
    }

    @Override
    public void decode(IoSession session, IoBuffer buff,
            ProtocolDecoderOutput out) throws Exception
    {
        long startTime = 0;
        if (session.containsAttribute(START_TIME))
        {
            startTime = (Long) session.getAttribute(START_TIME);
        }

        int count = 0;
        if (session.containsAttribute(RECEIVE_COUNT))
        {
            count = (Integer) session.getAttribute(RECEIVE_COUNT);
        }

        if (System.currentTimeMillis() - startTime > 1000)
        {
            if (count > 10)
            {
                LOG.error(session + " --> sendmsg:" + count);
            }
            startTime = System.currentTimeMillis();
            count = 0;
        }

        count++;

        if (count > maxCount)
        {
            // 如果客户端在1秒之内发送了超过MAX_COUNT条消息, 则认为客户端在恶意刷包, 断开其连接
            LOG.error(session + " --> sendmsg:" + count + "-->close-->buffer:"
                    + buff.remaining() + "(" + buff + ")");
            SessionUtils.closeSession(session, "客户端发送的消息次数太频繁(" + count + ")", true);
            return;
        } else
        {
            session.setAttribute(START_TIME, startTime);
            session.setAttribute(RECEIVE_COUNT, count);
        }

        // 初始化服务器上下文
        ServerContext context = (ServerContext) session.getAttribute(CONTEXT);
        if (context == null)
        {
            context = new ServerContext();
            session.setAttribute(CONTEXT, context);
        }

        // 读取信息
        IoBuffer contextBuff = context.getBuffer();
        // 将接收到的信息添加到Context
        contextBuff.put(buff);

        do
        {
            contextBuff.flip();
            if (contextBuff.remaining() < Integer.SIZE / Byte.SIZE)
            {
                // 剩余字节长度不足，等待下次信息
                contextBuff.compact();
                break;
            }

            // 获得信息长度
            int length = contextBuff.getInt();
//            LOG.error(session + ":message length " + length + " bytes, buff remain " + contextBuff.remaining() + " bytes，max bytes " + maxSize);
            if (length > maxSize || length <= 0)
            {
                contextBuff.rewind();

                int remain = contextBuff.remaining();
                if (remain > 64)
                {
                    remain = 64;
                }
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < remain / 4; i++)
                {
                    str.append(" ").append(Integer.toHexString(contextBuff.getInt()));
                }
                SessionUtils.closeSession(session, "客户端发送的消息字节过长(" 
                        + length + "bytes), 消息前64字节为(" + str.toString() + ")", true);
                break;
            }

            if (contextBuff.remaining() < length)
            {
                contextBuff.rewind();
                // 剩余字节长度不足，等待下次信息
                contextBuff.compact();
                break;
            }
            else
            {
                // 读取指定长度的字节数
                byte[] bytes = new byte[length];
                contextBuff.get(bytes);
   
                // 包序号 
                int order = peekInt32(0, bytes);
                order = order ^ (0x77 << 10);
                order = order ^ length;
                     
                // 时间
                int curTimes = peekInt32(4, bytes); //contextBuff.getInt();
                
                // 消息ID
                int messageId = peekInt32(8, bytes);
                
                int lastTimes = 0;
                if (session.containsAttribute("LAST_RECEIVED_TIME"))
                {
                    lastTimes = (int) session.getAttribute("LAST_RECEIVED_TIME");
                    if (lastTimes > curTimes)
                    {
                        // 是否可以忽略的消息
                        if (!ignoreMessage.contains(messageId))
                        {
                            LOG.error(session + "时间戳出错: message id = " + messageId + ", lastTimes = " + lastTimes + ", curTimes = " + curTimes);
                            break;
                        }
                        else
                        {
                            LOG.info(session + "未丢弃的过期包： message id = " + messageId + ", lastTimes = " + lastTimes + ", curTimes = " + curTimes);
                        }
                    }
                }               
          
                session.setAttribute("LAST_RECEIVED_TIME", curTimes);
          
                int preOrder = 0;
                if (session.containsAttribute("PRE_ORDER"))
                {
                    preOrder = (Integer) session.getAttribute("PRE_ORDER");
                }
                
                if (order != preOrder)
                {
                    // 是否可以忽略的消息
                    if (!ignoreMessage.contains(messageId))
                    {
                        LOG.error(session + "包序列出错: message id = " + messageId + ", order = " + order + ", preOrder = " + preOrder);
                        SessionUtils.closeSession(session, "包序列出错", true);
                        break;
                    }
                    else
                    {
                        LOG.info(session + "未丢弃的错误序列包：message id = " + messageId + ", order = " + order + ", preOrder = " + preOrder);
                    }
                }

                session.setAttribute("PRE_ORDER", (preOrder + 1));
                
                // 将包的序号解码后写回去
                bytes[0] = (byte)((order & (0xFF << 24)) >> 24);
                bytes[1] = (byte)((order & (0xFF << 16)) >> 16);
                bytes[2] = (byte)((order & (0xFF <<  8)) >> 8);
                bytes[3] = (byte)((order & (0xFF)));
                
                //写入指定长度的字节数
                out.write(bytes);

                if (contextBuff.remaining() == 0)
                {
                    contextBuff.clear();
                    break;
                } 
                else
                {
                    contextBuff.compact();
                }
            }
        } while (true);

    }

    @Override
    public void dispose(IoSession session) throws Exception
    {
        if (session.getAttribute(CONTEXT) != null)
        {
            session.removeAttribute(CONTEXT);
        }
    }

    @Override
    public void finishDecode(IoSession session, ProtocolDecoderOutput out)
            throws Exception
    {

    }

    private int peekInt32(int beg, byte[] bytes)
    {
        return (int)((bytes[0 + beg] & 0xFF) << 24)   | 
               (int)((bytes[beg + 1] & 0xFF) << 16)   |
               (int)((bytes[beg + 2] & 0xFF) <<  8)   |
               (int)((bytes[beg + 3] & 0xFF));
    }

}
