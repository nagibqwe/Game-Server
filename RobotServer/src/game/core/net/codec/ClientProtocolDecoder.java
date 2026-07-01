/**
 * @date 2014/7/2
 * @author ChenLong
 */
package game.core.net.codec;

import game.core.net.server.ServerContext;
import game.core.util.SessionUtils;
import game.core.util.ZipUtil;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * 客户端Decoder
 *
 * @author ChenLong
 */
public class ClientProtocolDecoder implements ProtocolDecoder {

    private static final Logger LOG = LogManager.getLogger(ClientProtocolDecoder.class);
    private static final String CONTEXT = "CONTEXT";

    @Override
    public void decode(IoSession session, IoBuffer buff, ProtocolDecoderOutput out) throws Exception {
        // 初始化服务器上下文
        ServerContext context = (ServerContext) session.getAttribute(CONTEXT);
        if (context == null) {
            context = new ServerContext();
            session.setAttribute(CONTEXT, context);
        }

        IoBuffer contextBuff = context.getBuffer(); // 读取信息
        contextBuff.put(buff); // 将接收到的信息添加到Content

        do {
            contextBuff.flip();
            if (contextBuff.remaining() < Integer.SIZE / Byte.SIZE) {
                // 剩余字节长度不足，等待下次信息
                contextBuff.compact();
                break;
            }

            // 获得信息长度
            int cryptLength = contextBuff.getInt();
            int length = cryptLength & (0xFFFFFF);
            int isCompressed = cryptLength >> 24;

            if (length > 16 * 1024 || length <= 0) {
                contextBuff.rewind();

                int remain = contextBuff.remaining();
                if (remain > 64) {
                    remain = 64;
                }
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < remain / 4; i++) {
                    str.append(" ").append(Integer.toHexString(contextBuff.getInt()));
                }
                SessionUtils.closeSession(session, "客户端发送的消息字节过长("
                        + length + "bytes), 消息前64字节为(" + str.toString() + ")", true);
                break;
            }

            if (contextBuff.remaining() < length) {
                contextBuff.rewind();
                // 剩余字节长度不足，等待下次信息
                contextBuff.compact();
                break;
            } else {
                // 读取指定长度的字节数
                byte[] bytes = new byte[length];
                contextBuff.get(bytes);
                String mess = new String(bytes);
                //写入指定长度的字节数
                if (isCompressed > 0) {
                    out.write(ZipUtil.uncompress(mess));
                } else {
                    out.write(bytes);
                }

                if (contextBuff.remaining() == 0) {
                    contextBuff.clear();
                    break;
                } else {
                    contextBuff.compact();
                }
            }
        } while (true);
    }

    @Override
    public void dispose(IoSession session) throws Exception {
        if (session.getAttribute(CONTEXT) != null) {
            session.removeAttribute(CONTEXT);
        }
    }

    @Override
    public void finishDecode(IoSession session, ProtocolDecoderOutput out)
            throws Exception {
    }
}
