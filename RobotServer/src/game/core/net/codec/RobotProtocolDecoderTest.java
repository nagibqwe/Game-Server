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
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * @author hewei@haowan123.com
 */
public class RobotProtocolDecoderTest extends CumulativeProtocolDecoder {

    private final static Logger logger = LogManager.getLogger("flow");
    @Override
    protected boolean doDecode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput out) throws Exception {
        if(ioBuffer.remaining() < Integer.SIZE / Byte.SIZE){
            return false;
        }
        ioBuffer.mark();
        int cryptLength = ioBuffer.getInt();
        int length = cryptLength & (0xFFFFFF);
        int isZip = cryptLength >> 24;

        if(length > ioBuffer.remaining()){
            ioBuffer.reset();
            return false;
        }

        if (isZip > 0) {
            int msgId = ioBuffer.getInt();
            byte[] bytes = new byte[length - 4];
            ioBuffer.get(bytes);
            byte[] bytess = ZLibUtils.decompress(bytes);
            byte[] bytessss = new byte[bytess.length + 4];
            bytessss[0] = (byte) ((0xff000000 & msgId) >> 24);
            bytessss[1] = (byte) ((0xff0000 & msgId) >> 16);
            bytessss[2] = (byte) ((0xff00 & msgId) >> 8);
            bytessss[3] = (byte) (0xff & msgId);
            int i = 4;
            for (byte b : bytess) {
                bytessss[i] = b;
                i++;
            }
            out.write(bytessss);
        } else {
            byte[] bytes = new byte[length];
            ioBuffer.get(bytes);
            out.write(bytes); // 输出一个消息包
        }

        return ioBuffer.remaining() > 0;
    }
}
