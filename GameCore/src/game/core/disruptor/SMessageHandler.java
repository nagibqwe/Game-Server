	package game.core.disruptor;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import game.core.message.SMessage;
import game.core.util.SessionUtils;
import static game.core.util.SessionUtils.SEND_BUF;
import game.core.util.ZLibUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by soko(xysoko@qq.com) on 2017/10/14. copyright 巨匠@雨墨
 */
public class SMessageHandler implements WorkHandler<SMessageEvent>, EventHandler<SMessageEvent>
{
    private static final Logger logger = LogManager.getLogger(SMessageHandler.class);
    private static final int MAX_BUFFSIZE_SENDBUFF = 4 * 1024 * 1024;

    private DisruptorOrderPoolExecutor<SMessageEvent, ChannelHandlerContext, SMessage> disruptorPool;

    public void setDisruptorPool(DisruptorOrderPoolExecutor<SMessageEvent, ChannelHandlerContext, SMessage> disruptorPool)
    {
        this.disruptorPool = disruptorPool;
    }

    @Override
    public void onEvent(SMessageEvent sMessageEvent, long l, boolean b) throws Exception
    {
        onEvent(sMessageEvent);
    }

    @Override
    public void onEvent(SMessageEvent sMessageEvent) throws Exception
    {

        ChannelHandlerContext session = sMessageEvent.getSession();
        SMessage msg = sMessageEvent.getSmess();
        try
        {
//            logger.info("处理发送消息ID=" + msg.getId());
            if (session.channel() == null)
            {
                return;
            }
            Channel channel = session.channel();
            if (channel == null)
            {
                logger.info("Соединение разорвано, msgId=" + msg.getId() + "!");
                return;
            }
            if (channel.unsafe() == null)
            {
                logger.info("Сессия " + channel + " уже разорвана, отправка сообщения невозможна. msgId=" + msg.getId() + "!");
                return;
            }

            if (null != channel.unsafe().outboundBuffer())
            {
                long netsize = channel.unsafe().outboundBuffer().totalPendingWriteBytes();
                if (netsize > MAX_BUFFSIZE_SENDBUFF)
                {
                    logger.error("Сессия " + channel + ", очередь отправки переполнена!");
                    SessionUtils.closeSession(session, "Слишком много данных ожидает отправки (" + netsize + ")");
                    return;
                }
            }
            ByteBuf buf;
            int len = 0;
            // 是否压缩
            if (msg.getData().length > 512)
            {
                byte[] zipBytes = ZLibUtils.compress(msg.getData());
                len = zipBytes.length + Integer.SIZE / Byte.SIZE;
                int lenlen = (zipBytes.length + Integer.SIZE / Byte.SIZE) | (((int) 1) << 24);
                buf = Unpooled.compositeBuffer(lenlen + 16);
                buf.writeInt(lenlen);
                buf.writeInt(msg.getId());
                buf.writeBytes(zipBytes);
            }
            else
            {
                len = msg.getData().length + Integer.SIZE / Byte.SIZE;
                buf = Unpooled.compositeBuffer(len + 16);
                buf.writeInt(len);
                buf.writeInt(msg.getId());
                buf.writeBytes(msg.getData());
            }

            synchronized (channel)
            {
                ByteBuf out = channel.attr(SEND_BUF).get();
                if (out == null)
                {
                    channel.attr(SEND_BUF).set(buf);
                }
                else
                {
                    out.writeBytes(buf);
                    buf.release();
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e, e);
        }
        sMessageEvent.setSession(null);
        sMessageEvent.setSmess(null);

        if (null == disruptorPool)
            return;

        afterEvent(session);
    }

    //事件处理完成后
    private void afterEvent(ChannelHandlerContext session)
    {
        if (session.isRemoved())
        {
            this.disruptorPool.removeDataCache(session);
            return;
        }

        CommonQueue<SMessage> queue = this.disruptorPool.getTasksQueue(session);
        if (queue == null)
            return;

        SMessage sm = queue.getNewValue();
        if (sm == null)
            return;

//        logger.info("发送消息ID=" + sm.getId());
        //压入新的队列
        this.disruptorPool.publishNext(session, sm);
    }

}
