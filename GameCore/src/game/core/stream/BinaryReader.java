package game.core.stream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * @author gaozhaoguang
 * @desc BinaryReader
 * @date Created on 2021/8/10 14:24
 **/
public class BinaryReader {

    private final ByteArrayInputStream stream;

    public BinaryReader(InputStream is) throws IOException {
        byte[] buff = new byte[is.available()];
        is.read(buff);
        stream = new ByteArrayInputStream(buff);
        is.close();
    }

    public void close() throws IOException {
        stream.close();
    }

    public void skip(long s) {
        stream.skip(s);
    }

    public int limit() {
        return stream.available();
    }

    public void mark(int readAheadLimit) {
        stream.reset();
        stream.skip(readAheadLimit);
    }

    public void reset() {
        stream.reset();
    }

    public byte read() {
        return (byte) stream.read();
    }

    public void read(byte[] b) throws IOException {
        stream.read(b);
    }

    public int readInt() throws IOException {
        byte[] b = new byte[4];
        stream.read(b, 0, 4);
        int value = 0;
        for (int i = 3; i > -1; i--) {
            int shift = i * 8;
            value += (b[i] & 0x000000FF) << shift;
        }
        return value;
    }

    public long readLong() throws IOException {
        byte[] b = new byte[8];
        stream.read(b, 0, 8);
        long value = 0L;
        for (int i = 7; i > -1; i--) {
            int shift = i * 8;
            value += (long)(b[i] & 0x000000FF) << shift;
        }
        return value;
    }

    public short readShort() throws IOException {
        byte[] b = new byte[2];
        stream.read(b, 0, 2);
        return (short) (((b[1] << 8) | b[0] & 0xff));
    }

    public int readUShort() throws IOException {
        byte[] b = new byte[2];
        stream.read(b, 0, 2);
        return (b[1] & 0x000000ff) << 8 | b[0] & 0x000000ff;
    }

    public float readFloat() throws IOException {
        byte[] b = new byte[4];
        stream.read(b, 0, 4);
        int f;
        f = b[0];
        f &= 0xff;
        f |= ((long) b[1] << 8);
        f &= 0xffff;
        f |= ((long) b[2] << 16);
        f &= 0xffffff;
        f |= ((long) b[3] << 24);
        return Float.intBitsToFloat(f);
    }

    public double readDouble() throws IOException {
        byte[] b = new byte[8];
        stream.read(b, 0, 8);
        long d;
        d = b[0];
        d &= 0xff;
        d |= ((long) b[1] << 8);
        d &= 0xffff;
        d |= ((long) b[2] << 16);
        d &= 0xffffff;
        d |= ((long) b[3] << 24);
        d &= 0xffffffffl;
        d |= ((long) b[4] << 32);
        d &= 0xffffffffffl;
        d |= ((long) b[5] << 40);
        d &= 0xffffffffffffl;
        d |= ((long) b[6] << 48);
        d &= 0xffffffffffffffl;
        d |= ((long) b[7] << 56);
        return Double.longBitsToDouble(d);
    }
}
