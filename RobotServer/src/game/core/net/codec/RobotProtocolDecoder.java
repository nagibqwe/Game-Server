/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.net.codec;

import com.game.server.RobotServer;
import game.core.net.server.ServerContext;
import game.core.util.SessionUtils;
import game.core.util.ZLibUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 *
 * @author hewei@haowan123.com
 */
public class RobotProtocolDecoder implements ProtocolDecoder
{
    private static final String CONTEXT = "CONTEXT";
    private final static Logger logger = LogManager.getLogger("flow");

    @Override
    public void decode(IoSession session, IoBuffer buff, ProtocolDecoderOutput out) throws Exception
    {
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
            // 获得信息长度
            int cryptLength = contextBuff.getInt();
            int length = cryptLength & (0xFFFFFF);
            int isZip = cryptLength >> 24;
            if (length <= 0)
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
                SessionUtils.closeSession(session, "客户端发送的消息字节错误("
                        + length + "bytes), 消息前64字节为(" + str.toString() + ")", true);
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
                if (isZip > 0)
                {
                    int msgId = contextBuff.getInt();
                    byte[] bytes = new byte[length - 4];
                    contextBuff.get(bytes);
                    byte[] bytess = ZLibUtils.decompress(bytes);
                    byte[] bytessss = new byte[bytess.length + 4];
                    bytessss[0] = (byte) ((0xff000000 & msgId) >> 24);
                    bytessss[1] = (byte) ((0xff0000 & msgId) >> 16);
                    bytessss[2] = (byte) ((0xff00 & msgId) >> 8);
                    bytessss[3] = (byte) (0xff & msgId);               
                    int i = 4;
                    for (byte b : bytess)
                    {
                        bytessss[i] = b;
                        i++;
                    }
                    out.write(bytessss);
                }
                else
                {
                    byte[] bytes = new byte[length];
                    contextBuff.get(bytes);
                    out.write(bytes); // 输出一个消息包
                }

                if(session.getId() == RobotServer.getInstance().debugSessionId){
                    logger.error(String.format("huhu debug %d decode isZip:%s", RobotServer.getInstance().debugRoleId, isZip + ""));
                }

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
}
