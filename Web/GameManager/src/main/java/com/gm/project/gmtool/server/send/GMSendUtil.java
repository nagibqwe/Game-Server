package com.gm.project.gmtool.server.send;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GMSendUtil {

    private static final int BUF_SIZE = 8192;
    private static Logger logger = LoggerFactory.getLogger(GMSendUtil.class);

    private static Socket createSocket(String host, int port) throws GMSendException {
        Socket socket;
        try {
            logger.info("Socket info: " + host + " : " + port);
            socket = new Socket(host, port);
            socket.setSoTimeout(60000);
            socket.setKeepAlive(true);
        } catch (Exception e) {
            throw new GMSendException(GMSendErrorType.CONNECT, e.getMessage(), e.getCause());
        }

        return socket;
    }

    private static boolean isValidIP(String ipAddress) {
        String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }

    public static String send(String text, String host, int port, int timeout) {
        StringBuilder sb = new StringBuilder();
        int len = text.getBytes(StandardCharsets.UTF_8).length + Integer.SIZE / Byte.SIZE;
        ByteBuffer bb = ByteBuffer.allocate(len);
        bb.putInt(text.getBytes(StandardCharsets.UTF_8).length);
        bb.put(text.getBytes(StandardCharsets.UTF_8));
        bb.flip();

        // 读取指定长度的字节数
        byte[] bytes = new byte[len];
        bb.get(bytes);
        if (isValidIP(host)) {
            sendIp(host, port, bytes, ops(sb), timeout);
        } else {
            send(host, port, bytes, ops(sb), timeout);
        }
        return sb.toString();
    }

    /**
     * 向某主机发送一些字节内容，并将返回写入输出流
     *  @param host 主机
     * @param port 端口
     * @param ins  发送的内容
     * @param ops  主机返回的输入流
     */
    private static void sendIp(String host, int port, byte[] ins, OutputStream ops, int timeout) {
        Socket socket = null;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), timeout);
            // 发送关闭命令
            OutputStream sOut = socket.getOutputStream();
            write(sOut, ins);
            sOut.flush();

            // 接收服务器的反馈
            if (!socket.isClosed()) {
                InputStream sReturn = socket.getInputStream();
                write(ops, sReturn);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            safeClose(ops);
            safeClose(socket);
        }
    }

    /**
     * 向某主机发送一些字节内容，并将返回写入输出流
     *
     * @param host 主机
     * @param port 端口
     * @param ins  发送的内容
     * @param ops  主机返回的输入流
     */
    public static void send(String host, int port, byte[] ins, OutputStream ops, int timeout) {
        Socket socket = null;
        try {
            socket = new Socket();
            socket.connect(InetSocketAddress.createUnresolved(host, port), timeout);
            // 发送关闭命令
            OutputStream sOut = socket.getOutputStream();
            write(sOut, ins);
            sOut.flush();

            // 接收服务器的反馈
            if (!socket.isClosed()) {
                InputStream sReturn = socket.getInputStream();
                write(ops, sReturn);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            safeClose(ops);
            safeClose(socket);
        }
    }

    /**
     * 安全关闭套接层，容忍null
     *
     * @param socket 套接层
     */
    private static void safeClose(Socket socket) {
        if (null != socket) {
            try {
                socket.close();
            } catch (IOException e) {
                throw wrapThrow(e);
            }
        }
    }

    public static StringOutputStream ops(StringBuilder sb) {
        return new StringOutputStream(sb);
    }

    public static RuntimeException wrapThrow(Throwable e) {
        if (e instanceof RuntimeException)
            return (RuntimeException) e;
        if (e instanceof InvocationTargetException)
            return wrapThrow(((InvocationTargetException) e).getTargetException());
        return new RuntimeException(e);
    }

    /**
     * 将输入流写入一个输出流。块大小为 8192
     * <p>
     * <b style=color:red>注意</b>，它并不会关闭输入/出流
     *
     * @param ops
     *            输出流
     * @param ins
     *            输入流
     *
     * @return 写入的字节数
     * @throws IOException
     */
    public static long write(OutputStream ops, InputStream ins) throws IOException {
        return write(ops, ins, BUF_SIZE);
    }

    /**
     * 将输入流写入一个输出流。
     * <p>
     * <b style=color:red>注意</b>，它并不会关闭输入/出流
     *
     * @param ops
     *            输出流
     * @param ins
     *            输入流
     * @param bufferSize
     *            缓冲块大小
     *
     * @return 写入的字节数
     *
     * @throws IOException
     */
    public static long write(OutputStream ops, InputStream ins, int bufferSize) throws IOException {
        if (null == ops || null == ins)
            return 0;

        byte[] buf = new byte[bufferSize];
        int len;
        long bytesCount = 0;
        while (-1 != (len = ins.read(buf))) {
            bytesCount += len;
            ops.write(buf, 0, len);
        }
        // 啥都没写，强制触发一下写
        // 这是考虑到 walnut 的输出流实现，比如你写一个空文件
        // 那么输入流就是空的，但是 walnut 的包裹输出流并不知道你写过了
        // 它人你就是打开一个输出流，然后再关上，所以自然不会对内容做改动
        // 所以这里触发一个写，它就知道，喔你要写个空喔。
        if (0 == bytesCount) {
            ops.write(buf, 0, 0);
        }
        ops.flush();
        return bytesCount;
    }
    /**
     * 关闭一个可关闭对象，可以接受 null。如果成功关闭，返回 true，发生异常 返回 false
     *
     * @param cb
     *            可关闭对象
     * @return 是否成功关闭
     */
    public static boolean safeClose(Closeable cb) {
        if (null != cb)
            try {
                cb.close();
            }
            catch (IOException e) {
                return false;
            }
        return true;
    }
    /**
     * 将一个字节数组写入一个输出流。
     * <p>
     * <b style=color:red>注意</b>，它并不会关闭输出流
     *
     * @param ops
     *            输出流
     * @param bytes
     *            字节数组
     * @throws IOException
     */
    public static void write(OutputStream ops, byte[] bytes) throws IOException {
        if (null == ops || null == bytes || bytes.length == 0)
            return;
        ops.write(bytes);
    }

}
