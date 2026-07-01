package com.game.yed;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by huhu on 2017/7/27.
 */
public class BitConverter {
    public static boolean bDebug = false;

    public static byte[] GetBytes(short value) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (value >> 8);
        bytes[1] = (byte) (value);
        return bytes;
    }

    public static byte[] GetBytes(int value) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (value >> 24);
        bytes[1] = (byte) (value >> 16);
        bytes[2] = (byte) (value >> 8);
        bytes[3] = (byte) (value);
        return bytes;
    }

    public static byte[] GetBytes(long value) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (value >> 24);
        bytes[1] = (byte) (value >> 16);
        bytes[2] = (byte) (value >> 8);
        bytes[3] = (byte) (value);
        return bytes;
    }

    public static byte[] GetBytes(float value) {
        ByteBuffer buf = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        buf.putFloat(value);
        return buf.array();
    }

    public static byte[] GetBytes(double value) {
        ByteBuffer buf = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
        buf.putDouble(value);
        return buf.array();
    }

    public static float ToSingle(byte[] bytes, int offset) {
        ByteBuffer buf = ByteBuffer.wrap(bytes, (int) offset, 4).order(ByteOrder.LITTLE_ENDIAN);
        float outp = buf.getFloat();
        if (bDebug)
            System.out.println(outp);
        return outp;
    }

    public static short ToInt16(byte[] bytes, int offset) {
        short result = (short) ((int) bytes[offset] & 0xff);
        result |= ((int) bytes[offset + 1] & 0xff) << 8;
        if (bDebug)
            System.out.println(result & 0xffff);
        return (short) (result & 0xffff);
    }

    public static int ToUInt16(byte[] bytes, int offset) {
        int result = (int) bytes[offset + 1] & 0xff;
        result |= ((int) bytes[offset] & 0xff) << 8;
        if (bDebug)
            System.out.println(result & 0xffff);
        return result & 0xffff;
    }

    public static int ToInt32(byte[] bytes, int offset) {
        int result = (int) bytes[offset] & 0xff;
        result |= ((int) bytes[offset + 1] & 0xff) << 8;
        result |= ((int) bytes[offset + 2] & 0xff) << 16;
        result |= ((int) bytes[offset + 3] & 0xff) << 24;
        if (bDebug)
            System.out.println(result);
        return result;
    }

    public static long ToUInt32(byte[] bytes, int offset) {
        long result = (int) bytes[offset] & 0xff;
        result |= ((int) bytes[offset + 1] & 0xff) << 8;
        result |= ((int) bytes[offset + 2] & 0xff) << 16;
        result |= ((int) bytes[offset + 3] & 0xff) << 24;
        if (bDebug)
            System.out.println(result & 0xFFFFFFFFL);
        return result & 0xFFFFFFFFL;
    }

    public static long ToInt64(byte[] bytes, int offset) {
        return ToUInt64(bytes, offset);
    }

    public static long ToUInt64(byte[] bytes, int offset) {
        long result = 0;
        for (int i = 0; i <= 56; i += 8) {
            result |= ((int) bytes[offset++] & 0xff) << i;
        }
        if (bDebug)
            System.out.println(result);
        return result;
    }

    public static double ToDouble(byte[] bytes, int offset) {
        ByteBuffer buf = ByteBuffer.wrap(bytes, offset, 8).order(ByteOrder.LITTLE_ENDIAN);
        double outp = buf.getDouble();
        if (bDebug)
            System.out.println(outp);
        return outp;
    }
}
