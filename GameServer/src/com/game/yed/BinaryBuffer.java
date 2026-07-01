package com.game.yed;

public class BinaryBuffer {

    public Endian bufferEndian = Endian.Little;
    public Endian readEndian = Endian.Little;
    public Endian writeEndian = Endian.Little;
    //字节缓存区
    private byte[] buf;
    //读取索引
    private int readIndex = 0;
    //写入索引
    private int writeIndex = 0;
    //读取索引标记
    private int markReadIndex = 0;
    //写入索引标记
    private int markWirteIndex = 0;
    //缓存区字节数组的长度
    private int capacity;

    /**
     * 构造方法
     */
    private BinaryBuffer(int capacity, Endian _e) {
        this.capacity = capacity;
        buf = new byte[capacity];
    }

    /**
     * 构造方法
     */
    private BinaryBuffer(byte[] data, Endian _e) {
        this.capacity = data.length;
        buf = data;
        writeIndex = data.length;
    }


    /**
     * 构建一个capacity长度的字节缓存区ByteBuffer对象
     */
    public static BinaryBuffer Allocate(int capacity, Endian _e) {
        return new BinaryBuffer(capacity, _e);
    }


    /**
     * 构建一个以bytes为字节缓存区的ByteBuffer对象，一般不推荐使用
     */
    public static BinaryBuffer Allocate(byte[] bytes, Endian _e) {
        return new BinaryBuffer(bytes, _e);
    }

    public static int FindFirst(byte[] bytes, byte v) {
        if (bytes != null) {
            int idx = 0;
            while (idx < bytes.length) {
                if (bytes[idx] == v) {
                    return idx;
                }
                idx++;
            }
            return idx;
        }
        return -1;
    }

    public static String Bytes2String(byte[] bytes, boolean skip_end_zero) {
        if (bytes != null) {
            int rlen = bytes.length;
            if (skip_end_zero) {
                rlen = FindFirst(bytes, (byte) 0);
            }
            return new String(bytes, 0, rlen);
        }
        return "";
    }

    public static byte[] String2Bytes(String str) {
        return str.getBytes();
    }

    public void reverse(byte[] a) {
        for (int i = 0; i < a.length >> 1; ++i) {
            byte t = a[i];
            a[i] = a[a.length - i];
            a[a.length - i] = t;
        }
    }

    /**
     * 根据length长度，确定大于此leng的最近的2次方数，如length=7，则返回值为8
     */
    private int FixLength(int length) {
        int n = 2;
        int b = 2;
        while (b < length) {
            b = 2 << n;
            n++;
        }
        return b;
    }

    /**
     * 翻转字节数组，如果本地字节序列为低字节序列，则进行翻转以转换为高字节序列
     */
    private byte[] WriteFlip(byte[] src) {
        if (bufferEndian != writeEndian) {
            reverse(src);
        }
        return src;
    }

    private byte[] ReadFlip(byte[] bytes) {
        if (bufferEndian != readEndian) {
            reverse(bytes);
        }
        return bytes;
    }

    /**
     * 确定内部字节缓存数组的大小
     */
    private int FixSizeAndReset(int currLen, int futureLen) {
        if (futureLen > currLen) {
            //以原大小的2次方数的两倍确定内部字节缓存区大小
            int size = FixLength(currLen) * 2;
            if (futureLen > size) {
                //以将来的大小的2次方的两倍确定内部字节缓存区大小
                size = FixLength(futureLen) * 2;
            }
            byte[] newbuf = new byte[size];
            System.arraycopy(buf, 0, newbuf, 0, currLen);
            buf = newbuf;
            capacity = newbuf.length;
        }
        return futureLen;
    }

    /**
     * 将bytes字节数组从startIndex开始的length字节写入到此缓存区
     */
    public void WriteBytes(byte[] bytes, int startIndex, int length) {
        synchronized (this) {
            int offset = length - startIndex;
            if (offset <= 0) return;
            int total = offset + writeIndex;
            int len = buf.length;
            FixSizeAndReset(len, total);
            for (int i = writeIndex, j = startIndex; i < total; i++, j++) {
                buf[i] = bytes[j];
            }
            writeIndex = total;
        }
    }

    /**
     * 将字节数组中从0到length的元素写入缓存区
     */
    public void WriteBytes(byte[] bytes, int length) {
        WriteBytes(bytes, 0, length);
    }

    /**
     * 将字节数组全部写入缓存区
     */
    public void WriteBytes(byte[] bytes) {
        WriteBytes(bytes, bytes.length);
    }

    /**
     * 将一个ByteBuffer的有效字节区写入此缓存区中
     */
    public void Write(BinaryBuffer buffer) {
        if (buffer == null) return;
        if (buffer.ReadableBytes() <= 0) return;
        WriteBytes(buffer.ToArray());
    }

    /**
     * 写入一个int16数据
     */
    public void WriteShort(short value) {
        WriteBytes(WriteFlip(BitConverter.GetBytes(value)));
    }

    /**
     * 写入一个int32数据
     */
    public void WriteInt(int value) {
        WriteBytes(WriteFlip(BitConverter.GetBytes(value)));
    }

    /**
     * 写入一个int64数据
     */
    public void WriteLong(long value) {
        WriteBytes(WriteFlip(BitConverter.GetBytes(value)));
    }

    /**
     * 写入一个float数据
     */
    public void WriteFloat(float value) {
        WriteBytes(WriteFlip(BitConverter.GetBytes(value)));
    }

    /**
     * 写入一个byte数据
     */
    public void WriteByte(byte value) {
        synchronized (this) {
            int afterLen = writeIndex + 1;
            int len = buf.length;
            FixSizeAndReset(len, afterLen);
            buf[writeIndex] = value;
            writeIndex = afterLen;
        }
    }

    /**
     * 写入一个double类型数据
     */
    public void WriteDouble(double value) {
        WriteBytes(WriteFlip(BitConverter.GetBytes(value)));
    }

    /**
     * 读取一个字节
     */
    public byte ReadByte() {
        byte b = buf[readIndex];
        readIndex++;
        return b;
    }

    public int NReadInt() {
        int idx = readIndex;
        readIndex += 4;
        return BitConverter.ToInt32(buf, idx);
    }

    public float NReadFloat() {
        int idx = readIndex;
        readIndex += 4;
        return BitConverter.ToSingle(buf, idx);
    }

    public String NReadString() {
        int len = NReadInt();
        int idx = readIndex;
        readIndex += len;
        return new String(buf, idx, len).replace("\0", "");
    }

    /**
     * 从读取索引位置开始读取len长度的字节数组
     */
    public byte[] Read(int len) {
        byte[] bytes = new byte[len];
        System.arraycopy(buf, readIndex, bytes, 0, len);
        readIndex += len;
        return bytes;
    }

    /**
     * 读取一个int16数据
     */
    public short ReadShort() {
        return BitConverter.ToInt16(ReadFlip(Read(2)), 0);
    }

    /**
     * 读取一个int32数据
     */
    public int ReadInt() {
        return BitConverter.ToInt32(ReadFlip(Read(4)), 0);
    }

    /**
     * 读取一个long数据
     */
    public long ReadLong() {
        return BitConverter.ToInt64(Read(8), 0);
    }

    /**
     * 读取一个float数据
     */
    public float ReadFloat() {
        return BitConverter.ToSingle(ReadFlip(Read(4)), 0);
    }

    /**
     * 读取一个double数据
     */
    public double ReadDouble() {
        return BitConverter.ToDouble(ReadFlip(Read(8)), 0);
    }

    /**
     * 从读取索引位置开始读取len长度的字节到disbytes目标字节数组中
     *
     * @params disstart 目标字节数组的写入索引
     */
    public void ReadBytes(byte[] disbytes, int disstart, int len) {
        int size = disstart + len;
        for (int i = disstart; i < size; i++) {
            disbytes[i] = this.ReadByte();
        }
    }

    /**
     * 清除已读字节并重建缓存区
     */
    public void DiscardReadBytes() {
        if (readIndex <= 0) return;
        int len = buf.length - readIndex;
        byte[] newbuf = new byte[len];
        System.arraycopy(buf, readIndex, newbuf, 0, len);
        buf = newbuf;
        writeIndex -= readIndex;
        markReadIndex -= readIndex;
        if (markReadIndex < 0) {
            markReadIndex = readIndex;
        }
        markWirteIndex -= readIndex;
        if (markWirteIndex < 0 || markWirteIndex < readIndex || markWirteIndex < markReadIndex) {
            markWirteIndex = writeIndex;
        }
        readIndex = 0;
    }

    /**
     * 清空此对象
     */
    public void Clear() {
        buf = new byte[buf.length];
        readIndex = 0;
        writeIndex = 0;
        markReadIndex = 0;
        markWirteIndex = 0;
    }

    /**
     * 设置开始读取的索引
     */
    public void SetReaderIndex(int index) {
        if (index < 0) return;
        readIndex = index;
    }

    /**
     * 标记读取的索引位置
     */
    public void MarkReaderIndex() {
        markReadIndex = readIndex;
    }

    /**
     * 标记写入的索引位置
     */
    public void MarkWriterIndex() {
        markWirteIndex = writeIndex;
    }

    /**
     * 将读取的索引位置重置为标记的读取索引位置
     */
    public void ResetReaderIndex() {
        readIndex = markReadIndex;
    }

    /**
     * 将写入的索引位置重置为标记的写入索引位置
     */
    public void ResetWriterIndex() {
        writeIndex = markWirteIndex;
    }

    /**
     * 可读的有效字节数
     */
    public int ReadableBytes() {
        return writeIndex - readIndex;
    }

    /**
     * 获取可读的字节数组
     */
    public byte[] ToArray() {
        byte[] bytes = new byte[writeIndex];
        System.arraycopy(buf, 0, bytes, 0, bytes.length);
        return bytes;
    }

    /**
     * 获取缓存区大小
     */
    public int GetCapacity() {
        return this.capacity;
    }
}
