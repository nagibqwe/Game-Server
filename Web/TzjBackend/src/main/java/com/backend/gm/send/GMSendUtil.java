package com.backend.gm.send;


import org.apache.log4j.Logger;
import org.nutz.lang.Lang;
import org.nutz.lang.Streams;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GMSendUtil {

    private static Logger logger = Logger.getLogger(GMSendUtil.class);

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
            sendIp(host, port, bytes, Lang.ops(sb), timeout);
        } else {
            send(host, port, bytes, Lang.ops(sb), timeout);
        }
        return sb.toString();
    }

    /**
     * 向某主机发送一些字节内容，并将返回写入输出流
     *  @param host 主机九零  一起玩 www.9017  5.com
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
            Streams.write(sOut, ins);
            sOut.flush();

            // 接收服务器的反馈
            if (!socket.isClosed()) {
                InputStream sReturn = socket.getInputStream();
                Streams.write(ops, sReturn);
            }
        } catch (IOException e) {
            logger.error(e, e);
        } finally {
            Streams.safeClose(ops);
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
            Streams.write(sOut, ins);
            sOut.flush();

            // 接收服务器的反馈
            if (!socket.isClosed()) {
                InputStream sReturn = socket.getInputStream();
                Streams.write(ops, sReturn);
            }
        } catch (IOException e) {
            logger.error(e, e);
        } finally {
            Streams.safeClose(ops);
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
                throw Lang.wrapThrow(e);
            }
        }
    }

}
