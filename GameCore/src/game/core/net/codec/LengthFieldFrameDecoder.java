/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.net.codec;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import java.nio.ByteOrder;

/**
 * 需要支持有解压缩包的能力
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class LengthFieldFrameDecoder extends LengthFieldBasedFrameDecoder
{

    public LengthFieldFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength)
    {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }

    /**
     * 此函数的意义是把解包的长度中是否包含有压缩的算法长度值处理
     *
     * @param buf
     * @param offset
     * @param length
     * @param order
     * @return
     */
    @Override
//    @SuppressWarnings(value={"deprecation"}) 
    protected long getUnadjustedFrameLength(ByteBuf buf, int offset, int length, ByteOrder order)
    {
//        buf = buf.order(order);
//        int frameLength;
//        if (length == 4)
//        {
//            frameLength = (int) buf.getUnsignedInt(offset);
//            return frameLength & (0xFFFFFF);
//        }
        return super.getUnadjustedFrameLength(buf, offset, length, order); //To change body of generated methods, choose Tools | Templates.
    }

}
