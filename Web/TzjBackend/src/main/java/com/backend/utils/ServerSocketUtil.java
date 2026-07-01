package com.backend.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.nutz.lang.Lang;
import org.nutz.lang.Streams;
import org.nutz.lang.util.NutMap;

import com.backend.bean.Server;

/**
 * 服务器连接请求字段信息
 *
 * @author Administrator
 */
public class ServerSocketUtil {
    private static final Logger log = Logger.getLogger(ServerSocketUtil.class);

    /**
     * 不对外提供接口了
     *
     * @param server
     * @param msg
     * @return
     */
    private static NutMap getResultByServer(Server server, String msg) {
        try {
            String ms = sendText(server.getWorldIP(), server.getWorldPort(), msg);

            HashMap<String, Object> map = JsonUtils.parseObject(ms, new TypeReference<HashMap<String, Object>>(){});
            NutMap re = new NutMap();
            if (map != null && map.size() > 0) {
                re.putAll(map);
                return re;
            }

            return re.setv("ok", false).setv("msg", "error bask msg =" + ms);
        } catch (Exception e) {
            log.error(e, e);
        }

        return new NutMap().setv("ok", false).setv("msg", server.getWorldIP() + ":" + server.getWorldPort() + " connect time out!");
    }

    /**
     * 不对外提供接口了
     *
     * @param server
     * @param msg
     * @return
     */
    private static NutMap getCompressResultByServer(Server server, String msg) {
        try {
            String ms = sendText(server.getWorldIP(), server.getWorldPort(), msg);
            @SuppressWarnings("unchecked")
            HashMap<String, Object> map = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(ms), new TypeReference<HashMap<String, Object>>(){});
            NutMap re = new NutMap();
            if (map != null && map.size() > 0) {
                re.putAll(map);
                return re;
            }

            return re.setv("ok", false).setv("msg", "error bask msg =" + ms);
        } catch (Exception e) {
            log.error(e, e);
        }

        return new NutMap().setv("ok", false).setv("msg", server.getWorldIP() + ":" + server.getWorldPort() + " connect time out!");
    }


    /**
     * 根据传入的指令信息，发送具体的消息内容，请求服务器处理相应逻辑
     *
     * @param server 指定服务器
     * @param msg    消息内容
     * @return 返回处理结果
     */
    public static NutMap getResultToServer(Server server, String msg) {
        return getResultByServer(server, msg);
    }

    /**
     * 根据传入的指令信息，发送具体的消息内容，请求服务器处理相应逻辑
     *
     * @param server 指定服务器
     * @param msg    消息内容
     * @return 返回处理结果
     */
    public static NutMap getResultByServer(Server server, Map<String, Object> msg) {
        try {
            String mess = JsonUtils.toJSONString(msg);
            return getResultByServer(server, mess);
        } catch (Exception e) {
            log.error(e, e);
        }
        return new NutMap().setv("ok", false).setv("msg", server.getWorldIP() + ":" + server.getWorldPort() + " connect time out!");
    }

    /**
     * 根据传入的指令信息，发送具体的消息内容，请求服务器处理相应逻辑
     *
     * @param server 指定服务器
     * @param cmd    接收指令
     * @param msg    消息内容
     * @return 返回处理结果
     */
    public static NutMap getCompressResultByServer(Server server, int cmd, Map<String, Object> msg) {
        try {
            msg.put("cmd", cmd);
            String mess = JsonUtils.toJSONString(msg);
            return getCompressResultByServer(server, mess);
        } catch (Exception e) {
            log.error(e, e);
        }

        return new NutMap().setv("ok", false).setv("msg", server.getWorldIP() + ":" + server.getWorldPort() + " connect time out!");
    }


    /**
     * 向某主机发送一段文本，并将主机的返回作为文本返回
     *
     * @param host 主机
     * @param port 端口
     * @param text 发送的内容
     * @return 主机返回的文本
     */
    public static String sendText(String host, int port, String text) {
        StringBuilder sb = new StringBuilder();
        int len = text.getBytes().length + Integer.SIZE / Byte.SIZE;
        ByteBuffer bb = ByteBuffer.allocate(len);
        bb.putInt(text.getBytes().length);
        bb.put(text.getBytes());
        bb.flip();

        // 读取指定长度的字节数
        byte[] bytes = new byte[len];
        bb.get(bytes);
        if (isValidIP(host)) {
            sendip(host, port, bytes, Lang.ops(sb), 60 * 1000);
        } else {
            send(host, port, bytes, Lang.ops(sb), 60 * 1000);
        }
        return sb.toString();
    }

    public static boolean isValidIP(String ipAddress) {
        String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }

    /**
     * 向某主机发送一些字节内容，并将返回写入输出流
     *
     * @param host 主机
     * @param port 端口
     * @param ins  发送的内容
     * @param ops  主机返回的输入流
     */
    public static void sendip(String host, int port, byte[] ins,
                              OutputStream ops, int timeout) {
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
            throw Lang.wrapThrow(e);
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
    public static void send(String host, int port, byte[] ins,
                            OutputStream ops, int timeout) {
        Socket socket = null;
        try {
            socket = new Socket();
            socket.connect(InetSocketAddress.createUnresolved(host, port),
                    timeout);
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
            throw Lang.wrapThrow(e);
        } finally {
            Streams.safeClose(ops);
            safeClose(socket);
        }
    }

    /**
     * 安全关闭套接层，容忍 null
     *
     * @param socket 套接层
     * @return 一定会返回 null
     */
    public static Socket safeClose(Socket socket) {
        if (null != socket)
            try {
                socket.close();
            } catch (IOException e) {
                throw Lang.wrapThrow(e);
            }
        return null;
    }

}
