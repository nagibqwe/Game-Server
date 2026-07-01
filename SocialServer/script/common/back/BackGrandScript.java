package common.back;

import com.data.script.ScriptConfigManager;
import com.game.backgrand.script.IBackGrand;
import com.game.backgrand.struct.SendBackFuture;
import com.game.script.struct.ScriptEnum;
import game.core.net.Config.ServerConfig;
import game.core.script.ScriptManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpHeaderValues.CLOSE;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;

/**
 * @Desc TODO
 * @Date 2021/6/9 15:51
 * @Auth ZUncle
 */
public class BackGrandScript implements IBackGrand {

    static final Logger logger = LogManager.getLogger(BackGrandScript.class);

    /**
     * 处理消息
     *
     * @param session
     * @param httpRequest
     */
    @Override
    public void cmd(ChannelHandlerContext session, HttpRequest httpRequest) {

        QueryStringDecoder query = new QueryStringDecoder(httpRequest.uri());
        String result;
        switch (query.path().toLowerCase()) {
            case "/test":
                writeResponse(session.channel(), httpRequest, "连接成功");
                break;
            case "/script":
                result = reloadScript(query);
                writeResponse(session.channel(), httpRequest, result);
                break;
            case "/config":
                if (checkSecretKey(query)) {
                    result = reloadConfig(query);
                } else {
                    result = "秘钥key 错误";
                }
                writeResponse(session.channel(), httpRequest, result);
                break;
            case "/cgm":
                if (checkSecretKey(query)) {
                    result = dealGM(query);
                } else {
                    result = "秘钥key 错误";
                }
                writeResponse(session.channel(), httpRequest, result);
                break;
            default:
                logger.error("unknown getRequestPath: [" + query.path() + "]");
                writeResponse(session.channel(), httpRequest, "404");
                break;
        }
    }

    /**
     * 处理发来的GM命令
     */
    private String dealGM(QueryStringDecoder query) {

        List<String> cmd = query.parameters().get("cmd");
        List<String> params = query.parameters().get("params");
        try {
            String cmdStr = cmd.get(0).toLowerCase();
            switch (cmdStr) {
                case "addfuju":

                    return "添加成功";
                default:
                    return cmd.get(0) + " 参数：" + params.get(0) + "命令执行时错误！";
            }
        } catch (Exception e) {
            logger.error(e, e);
            return cmd.get(0) + " 参数：" + params.get(0) + "命令执行时出现异常错误！";
        }
    }

    private String reloadConfig(QueryStringDecoder query) {

        List<String> configNames = query.parameters().get("scriptId");
        StringBuilder sb = new StringBuilder();
        for (String configName : configNames) {
            String reloadName = getJavaClassReloadName(configName);
            try {
                boolean isSuccess = ScriptConfigManager.GetInstance().reloadConfigScript(reloadName);
                if (isSuccess) {
                    switch (configName.toLowerCase()) {
                        case "item":
                            ScriptConfigManager.GetInstance().reloadCofigItem();
                            break;
                        case "equip":
                            ScriptConfigManager.GetInstance().reloadCofigItem();
                            break;
                        default:
                            break;
                    }
                    sb.append(configName).append(" == 配置表加载成功!");
                } else {
                    sb.append(configName).append(" == 配置表加载失败!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                sb.append(configName).append(" == 配置表加载失败!");
            }
        }
        return sb.toString();
    }

    private String getJavaClassReloadName(String configName) {

        configName = configName.replace("Cfg_", "").replace("_Load", "");

        char[] chars = configName.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        configName = new String(chars);
        return "config.Cfg_" + configName + "_Load";
    }

    /**
     * 加载单位脚本类
     */
    private String reloadScript(QueryStringDecoder query) {
        List<String> scriptIds = query.parameters().get("scriptId");
        StringBuilder sb = new StringBuilder();
        for (String sid : scriptIds) {
            try {
                int scriptId = Integer.parseInt(sid);
                if (ScriptManager.getInstance().reload(scriptId)) {
                    sb.append(scriptId).append(" == 重新加载成功!");
                } else {
                    sb.append(scriptId).append(" == 加载失败了!");
                }
            } catch (Exception e) {
                sb.append(sid).append(" == 加载失败了!");
            }
        }
        return sb.toString();
    }

    /**
     * 检测秘钥
     *
     * @param query
     * @return
     */
    boolean checkSecretKey(QueryStringDecoder query) {
        List<String> param = query.parameters().get("secret_key");
        if (!param.get(0).equals(ServerConfig.getPrivateKey())) {
            logger.error("后台刷新脚本失败 私密key错误");
            return false;
        }
        return true;
    }

    /**
     * http返回响应数据
     */
    private void writeResponse(Channel channel, HttpRequest httpRequest, String responseMsg) {

        // Convert the response content to a ChannelBuffer.
        ByteBuf buf = copiedBuffer(responseMsg, CharsetUtil.UTF_8);

        // Decide whether to close the connection or not.
        boolean close = httpRequest.headers().contains(CONNECTION, CLOSE, true)
                || httpRequest.protocolVersion().equals(HttpVersion.HTTP_1_0)
                && !httpRequest.headers().contains(CONNECTION, KEEP_ALIVE, true);

        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

        if (!close) {
            // There's no need to add 'Content-Length' header
            // if this is the last response.
            response.headers().set(CONTENT_LENGTH, buf.readableBytes());
        }
        // Write the response.
        ChannelFuture future = channel.writeAndFlush(response);
        future.addListener(new SendBackFuture());
        // Close the connection after the write operation is done if necessary.
        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.BackGrandScript;
    }

    /**
     * 调用脚本
     *
     * @param args 参数
     * @return
     */
    @Override
    public Object call(Object... args) {
        return null;
    }
}
