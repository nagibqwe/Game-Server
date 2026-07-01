package game.core.stream;



import java.io.*;

/**
 * @author gaozhaoguang
 * @desc BinaryWriter
 * @date Created on 2021/8/10 14:24
 **/
public class BinaryWriter {
    private final OutputStream stream;
    public BinaryWriter(OutputStream os) throws IOException {
        stream = os;
    }

    public void close() throws IOException {
        stream.close();
    }

    public void write(byte b) throws IOException {
        stream.write(b);
    }

    public void write(byte[] b) throws IOException {
        stream.write(b);
    }

    public void write(int value) throws IOException {
        byte[] b = new byte[4];
        for(int i = 0;i < 4;i++) {
            b[i] = (byte)((value >> (i*8)) & 0xFF);
        }
        stream.write(b);
    }

    public void write(long value) throws IOException {
        byte[] b = new byte[8];
        for(int i = 0;i < b.length;i++) {
            b[i] = (byte)((value >> (i*8)) & 0xFF);
        }
        stream.write(b);
    }

    public void write(short value) throws IOException {
        byte[] b = new byte[2];
        for(int i = 0;i < b.length;i++) {
            b[i] = (byte)((value >> (i*8)) & 0xFF);
        }
        stream.write(b);
    }

    public void write(float value) throws IOException {
        write(Float.floatToIntBits(value));
    }

    public void write(double value) throws IOException {
        write(Double.doubleToLongBits(value));
    }

}
