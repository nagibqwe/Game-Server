package common.http;

import com.data.CfgManager;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.script.ScriptConfigManager;
import com.game.gameserver.structs.ServerInfo;
import com.game.http.SendBackFuture;
import com.game.http.script.IHttpScript;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.servermatch.manager.ServerMatchManager;
import com.game.utils.MessageUtils;
import com.game.utils.ServerParamUtil;
import game.core.json.TypeReference;
import game.core.net.Config.ServerConfig;
import game.core.script.ScriptManager;
import game.core.util.JsonUtils;
import game.core.util.StringUtils;
import game.core.util.TimeUtils;
import game.message.BackendMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpHeaderValues.CLOSE;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;

/**
 * http协议处理脚本实现
 */
public class HttpBackScript implements IHttpScript {

    private static final Logger log = LogManager.getLogger(HttpBackScript.class);

    @Override
    public int getId() {
        return ScriptEnum.HttpBackScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void onHttp(ChannelHandlerContext session, HttpRequest httpRequest) {
        QueryStringDecoder qsd = new QueryStringDecoder(httpRequest.uri());
        String result;
        switch (qsd.path().toLowerCase()) {
            case "/test":
                writeResponse(session.channel(), httpRequest, "连接成功");
                break;
            case "/script":
                result = reloadScript(httpRequest);
                writeResponse(session.channel(), httpRequest, result);
                break;
            case "/getservergrouplist":
                result =  getServerGroupList();
                writeResponse(session.channel(), httpRequest, result);
                break;
            case "/setservergrouplist":
                result = setServerGroupList(httpRequest);
                writeResponse(session.channel(), httpRequest, result);
                break;
            case "/cgm":
                String res = dealGM(httpRequest);
                writeResponse(session.channel(), httpRequest, res);
                break;
            case "/getservergroup":
                writeResponse(session.channel(), httpRequest, ServerParamUtil.getServerParamMap().get(ServerParamUtil.serverGroupStr));
                break;
            case "/sendservergroup":
                receiveServerGroup(httpRequest);
                writeResponse(session.channel(), httpRequest, "成功");
                break;
            case "/forbiddenuser": //屏蔽、解封账号
                result = forbiddenUser(httpRequest);
                writeResponse(session.channel(), httpRequest, result);
                break;
            case "/cancelforbiddenuser": //解封账号
                result = cancelForbiddenUser(httpRequest);
                writeResponse(session.channel(), httpRequest, result);
                break;
            case "/whiteadd": //加白名单账号
                result = whiteAdd(httpRequest);
                writeResponse(session.channel(), httpRequest, result);
                break;
            case "/whitecancel": //取消白名单账号
                result = whiteCancel(httpRequest);
                writeResponse(session.channel(), httpRequest, result);
                break;
            case "/config":
                result = reloadConfig(httpRequest);
                writeResponse(session.channel(), httpRequest, result);
            default:
                log.error("unknown getRequestPath: [" + qsd.path() + "]");
                writeResponse(session.channel(), httpRequest, "404");
                break;
        }
    }

    /**
     * 处理发来的GM命令
     */
    private String dealGM(HttpRequest req) {
        QueryStringDecoder qsd = new QueryStringDecoder(req.uri());
        List<String> param = qsd.parameters().get("secret_key");
        if (!param.get(0).equals(ServerConfig.getPrivateKey())) {
            log.error("后台刷新脚本失败 私密key错误");
            return "failed";
        }

        List<String> cmd = qsd.parameters().get("cmd");
        List<String> params = qsd.parameters().get("params");
        try {
            String cmdStr = cmd.get(0).toLowerCase();
            switch (cmdStr) {
                case "showclone_map":
                    StringBuilder sb = new StringBuilder();
                    for (Cfg_Clone_map_Bean bean : CfgManager.getCfg_Clone_map_Container().getValuees()) {
                        sb.append(bean.getId()).append("(").append(bean.getDuplicate_name()).append(")的跨服类型是：").append(bean.getType());
                        sb.append(",");
                    }
                    return "当前加载了副本内容：" + sb.toString();
                case "reloadsoulanimalforest":
                    Manager.soulAnimalForestManager.manager().reloadBossData();
                    return "重新加载了魂兽森林的BOSS刷新事件";
                case "servergroup":
                    ServerParamUtil.immediateSave(ServerParamUtil.serverGroupStr, params.get(0));
                    return "servergroup参数：" + params.get(0);
                case "refreshsoulboss":
                    Manager.soulAnimalForestManager.manager().gmRefreshSoulBoss(Integer.parseInt(params.get(0)),
                            Integer.parseInt(params.get(1)),
                            Integer.parseInt(params.get(2))
                            ,Integer.parseInt(params.get(3)));
                    return "刷新魂兽森林BOSS "  + params;
                default:
                    return cmd.get(0) + " 参数：" + params.get(0) + "命令执行时错误！";
            }
        } catch (Exception e) {
            log.error(e, e);
            return cmd.get(0) + " 参数：" + params.get(0) + "命令执行时出现异常错误！";
        }
    }

    @Override
    public void loadDatas() {

    }

    /**
     * 加载单位脚本类
     */
    private String reloadScript(HttpRequest req) {
        QueryStringDecoder qsd = new QueryStringDecoder(req.uri());
        List<String> param = qsd.parameters().get("secret_key");
        if (!param.get(0).equals(ServerConfig.getPrivateKey())) {
            log.error("后台刷新脚本失败 私密key错误");
            return "failed";
        }

        List<String> scriptIds = qsd.parameters().get("scriptId");
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

    private String reloadConfig(HttpRequest req) {
        QueryStringDecoder qsd = new QueryStringDecoder(req.uri());
        List<String> param = qsd.parameters().get("secret_key");
        if (!param.get(0).equals(ServerConfig.getPrivateKey())) {
            log.error("后台刷新脚本失败 私密key错误");
            return "failed";
        }
        List<String> configNames = qsd.parameters().get("scriptId");
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
        char oldChar = configName.charAt(0);
        char newChar = (oldChar + "").toUpperCase().charAt(0);
        String replace = configName.replaceFirst(oldChar + "", newChar + "");
        return "config.Cfg_" + replace + "_Load";
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
     * 收到分出信息
     */
    private void receiveServerGroup(HttpRequest req) {
        QueryStringDecoder qsd = new QueryStringDecoder(req.uri());
        List<String> param = qsd.parameters().get("secret_key");
        if (!param.get(0).equals(ServerConfig.getPrivateKey())) {
            log.error("后台刷新脚本失败 私密key错误");
            return;
        }
        List<String> groups = qsd.parameters().get("group");

        StringBuilder sb = new StringBuilder();
        for (String s : groups) {
            String[] str = s.split(" ");
            for (String groupStr : str) {
                String[] group = groupStr.split(",");
                String plat = group[0];
                int groupId = Integer.parseInt(group[1]);
                for (int i = 2; i < group.length; i++) {
                    String key = groupId + "&" + plat + "_" + Integer.parseInt(group[i]) + ";";
                    sb.append(key);
                }
            }
        }
        //保存
        if (sb.length() > 0) {
            ServerParamUtil.immediateSave(ServerParamUtil.operatingGroup, sb.toString());
        }
    }

    //格式http://ip:port/forbiddenuser?secret_key=12&whiteStr=214&forbiddenTime=1990943(unix时间，单位s)
    private String forbiddenUser(HttpRequest httpRequest) {
        try {
            QueryStringDecoder qsd = new QueryStringDecoder(httpRequest.uri());
            Map<String, List<String>> pMap = qsd.parameters();
            List<String> param = qsd.parameters().get("secret_key");
            if (!param.get(0).equals(ServerConfig.getPrivateKey())) {
                log.error("后台屏蔽账号失败 私密key错误");
                return "failed";
            }
            String forbidStr = pMap.get("forbidStr").get(0);
            String forbiddenTime = pMap.get("forbiddenTime").get(0);
            if (StringUtils.isEmpty(forbidStr) || StringUtils.isEmpty(forbiddenTime)) {
                log.error("后台屏蔽账号失败 传入forbidStr为空");
                return "failed";
            }
            sendP2GNoticeSynData(0,forbidStr+"&&"+forbiddenTime);
            log.error("后台屏蔽账号 传入forbidStr:" + forbidStr + "成功， 封号的结束日期是：" + TimeUtils.format2string(Integer.parseInt(forbiddenTime) * 1000L));
            return "ok";
        } catch (Exception e) {
            log.error("后台屏蔽账号失败 参数错误", e);
            return "failed";
        }
    }

    //格式http://ip:port/cancelforbiddenuser?secret_key=12&forbidStr=214
    private String cancelForbiddenUser(HttpRequest httpRequest) {
        try {
            QueryStringDecoder qsd = new QueryStringDecoder(httpRequest.uri());
            Map<String, List<String>> pMap = qsd.parameters();
            List<String> param = qsd.parameters().get("secret_key");
            if (!param.get(0).equals(ServerConfig.getPrivateKey())) {
                log.error("后台取消屏蔽失败 私密key错误");
                return "failed";
            }
            String forbidStr = pMap.get("forbidStr").get(0);
            if (StringUtils.isEmpty(forbidStr)) {
                log.error("后台取消屏蔽失败 传入forbidStr为空");
                return "failed";
            }
            sendP2GNoticeSynData(1,forbidStr);
            return "ok";
        } catch (Exception e) {
            log.error("后台取消屏蔽失败 参数错误", e);
            return "failed";
        }
    }

    //格式http://ip:port/whiteadd?secret_key=12&whiteStr=214
    private String whiteAdd(HttpRequest httpRequest) {
        try {
            QueryStringDecoder qsd = new QueryStringDecoder(httpRequest.uri());
            Map<String, List<String>> pMap = qsd.parameters();
            List<String> param = qsd.parameters().get("secret_key");
            if (!param.get(0).equals(ServerConfig.getPrivateKey())) {
                log.error("后台设置白名单账号失败 私密key错误");
                return "failed";
            }
            String whiteStr = pMap.get("whiteStr").get(0);
            if (StringUtils.isEmpty(whiteStr)) {
                log.error("后台设置白名单账号失败 传入whiteStr为空");
                return "falied";
            }
            sendP2GNoticeSynData(2,whiteStr);
        } catch (Exception e) {
            log.error("后台设置白名单账号失败 传入参数错误", e);
            return "falied";
        }
        return "ok";
    }

    //格式http://ip:port/whitecancel?secret_key=12&whiteStr=214
    private String whiteCancel(HttpRequest httpRequest) {
        try {
            QueryStringDecoder qsd = new QueryStringDecoder(httpRequest.uri());
            Map<String, List<String>> pMap = qsd.parameters();
            List<String> param = qsd.parameters().get("secret_key");
            if (!param.get(0).equals(ServerConfig.getPrivateKey())) {
                log.error("后台取消白名单账号失败 私密key错误");
                return "failed";
            }
            String whiteStr = pMap.get("whiteStr").get(0);
            if (StringUtils.isEmpty(whiteStr)) {
                log.error("后台取消白名单账号失败 传入whiteStr为空");
                return "falied";
            }
            sendP2GNoticeSynData(3,whiteStr);
        } catch (Exception e) {
            log.error("后台取消白名单账号失败 传入参数错误", e);
            return "falied";
        }
        return "ok";
    }

    private void sendP2GNoticeSynData(int type, String data){
        log.info("P2GNoticeSynData:" + data + " type  "   +type);
        BackendMessage.P2GNoticeSynData.Builder msg = BackendMessage.P2GNoticeSynData.newBuilder();
        msg.setType(type);
        msg.setData(data);
        for (ServerInfo serverInfo : Manager.gameServerManager.getServerCache().values())
        {
            MessageUtils.send_to_game(serverInfo.getSession(), BackendMessage.P2GNoticeSynData.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    private String getServerGroupList(){
        String result =  ServerMatchManager.deal().onSendServerGroupToBackground();
        log.info("sendBackGroupServerlist ：{}" ,result);
        return result;
    }

    private String setServerGroupList(HttpRequest httpRequest){
        try {
            QueryStringDecoder qsd = new QueryStringDecoder(httpRequest.uri());
           // Map<String, List<String>> pMap = qsd.parameters();
            List<String> param = qsd.parameters().get("secret_key");
            if (!param.get(0).equals(ServerConfig.getPrivateKey())) {
                log.error("私密key错误");
                return "failed";
            }
            String dataStr = qsd.parameters().get("serverGroup").get(0);
            log.info("ReceiveOperatingServerGroup:  {}",dataStr);
            ServerMatchManager.deal().onReceiveOperatingServerGroup(dataStr);
        } catch (Exception e) {
        log.error("后台传入分组数据错误", e);
            return "falied";
        }
        return "ok";
    }

}
