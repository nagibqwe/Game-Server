package common.server;

import com.data.MessageString;
import com.game.chat.structs.Notify;
import com.game.connectfightserver.manager.ConnectFightManager;
import com.game.connectfightserver.struct.ConnectFightServer;
import com.game.fightserver.manager.FightClientManager;
import com.game.fightserver.struct.FightClient;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.SessionAttribute;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.server.social.SocialServerClient;
import com.game.server.script.IServerScript;
import com.game.structs.GlobalType;
import com.game.structs.ServerStr;
import com.game.utils.MessageUtils;
import game.core.net.Config.ServerConfig;
import game.core.net.Config.ServerEnum;
import game.core.script.IScript;
import game.core.util.CrossState;
import game.core.util.HttpUtils;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import game.message.*;
import game.message.serverMessage.F2GResRegister;
import game.message.serverMessage.G2FReqRegister;
import game.message.serverMessage.P2GResFightServerList;
import game.message.serverMessage.gameServerInfo;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author admin
 */
public class ServerScript implements IScript, IServerScript {

    private static final Logger log = LogManager.getLogger(ServerScript.class);

    @Override
    public int getId() {
        return ScriptEnum.ServerBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public String getNotHaveMarkStr(String tableName, int id) {
        try {
            String key = tableName.toLowerCase() + "_" + id;

            if (ServerStr.getLanguageNo().containsKey(key)) {
                Integer no = ServerStr.getLanguageNo().get(key);
                String skey = "str" + no;
                if (ServerStr.getServerLang().containsKey(skey)) {
                    return ServerStr.getServerLang().get(skey);
                }
            }
            GameServer.getInstance().getErrorLogThread().pushErrorExcptionLog("ServerStr" + " tableName _id" + tableName.toLowerCase() + "”+" + id + " 找不到", "语言包中没有配置“" + tableName.toLowerCase() + "”+" + id + " 的语言包ID值！");
            return tableName + "(" + id + ")未知";
        } catch (Exception e) {
            return "未知" + tableName + ":" + id;
        }
    }

    /**
     * 服务器字符串语言包
     *
     * @param tableName
     * @param id
     * @return
     */
    @Override
    public String getStr(String tableName, int id) {
        try {
            String key = tableName.toLowerCase() + "_" + id;

            if (ServerStr.getLanguageNo().containsKey(key)) {
                Integer no = ServerStr.getLanguageNo().get(key);
                String skey = "str" + no;
                if (ServerStr.getServerLang().containsKey(skey)) {
                    return "2&_" + ServerStr.getServerLang().get(skey);
                }
            }
            GameServer.getInstance().getErrorLogThread().pushErrorExcptionLog("ServerStr" + " tableName _id" + tableName.toLowerCase() + "”+" + id + " 找不到", "语言包中没有配置“" + tableName.toLowerCase() + "”+" + id + " 的语言包ID值！");
            return "2&_" + tableName + "(" + id + ")未知";
        } catch (Exception e) {
            return "未知" + tableName + ":" + id;
        }

    }

    /**
     * 服务器神兵名称获取
     *
     * @param career
     * @param dragoonId
     * @return
     */
    @Override
    public String getDragoonName(int career, int dragoonId) {
        try {
            String key = career + "_" + dragoonId;
            if (ServerStr.getDragoon().containsKey(key)) {
                return ServerStr.getDragoon().get(key);
            }
            GameServer.getInstance().getErrorLogThread().pushErrorExcptionLog("ServerStr" + " career_dragoonId：" + career + "_" + dragoonId + " 找不到", "请检查语言包！");
            return key;
        } catch (Exception e) {
            return "未知" + career + ":" + dragoonId;
        }
    }

    /**
     * 多语言获取语言
     *
     * @param tableName
     * @param id
     * @param roleId
     * @return
     */
    @Override
    public String getLanguage(String tableName, int id, long roleId) {
        try {
            String key = tableName.toLowerCase() + "_" + id;

            int languageType = Manager.registerManager.getLanguageType(roleId);

            ConcurrentHashMap<String, String> languagePack = ServerStr.getLanguageMap().get(languageType);
            if (languagePack == null) {
                GameServer.getInstance().getErrorLogThread().pushErrorExcptionLog("找不到语言包", "找不到语言包，languageType=" + languageType);
                return null;
            }
            if (ServerStr.getLanguageNo().containsKey(key)) {
                int no = ServerStr.getLanguageNo().get(key);
                String skey = "str" + no;
                return languagePack.get(skey);
            }
            GameServer.getInstance().getErrorLogThread().pushErrorExcptionLog("ServerStr tableName_id 找不到", "语言包中没有配置“" + tableName.toLowerCase() + "”+" + id + " 的语言包ID值！");
            return null;
        } catch (Exception e) {
            log.error("getLanguage异常：" + e);
            return null;
        }
    }

    @Override
    public String getLanguage(String tableName, String id) {
        try {
            String key = tableName.toLowerCase() + "_" + id;
            if (ServerStr.getLanguageNo().containsKey(key)) {
                Integer no = ServerStr.getLanguageNo().get(key);
                String skey = "str" + no;
                if (ServerStr.getServerLang().containsKey(skey)) {
                    return ServerStr.getServerLang().get(skey);
                }
            }
            GameServer.getInstance().getErrorLogThread().pushErrorExcptionLog("ServerStr" + " tableName _id" + tableName.toLowerCase() + "”+" + id + " 找不到", "语言包中没有配置“" + tableName.toLowerCase() + "”+" + id + " 的语言包ID值！");
            return "" + tableName + "(" + id + ")未知";
        } catch (Exception e) {
            return "未知" + tableName + ":" + id;
        }
    }

    //连接公共服务器成功了！的通知函数
    @Override
    public void OnP2GResRegister(ChannelHandlerContext context, serverMessage.P2GResRegister messInfo) {

        log.info("游戏服务器" + GameServer.getInstance().getServerName() + "注册到" + messInfo.getPublicName() + "返回成功！");

        log.info("公共服的连接地址：" + context.channel() + "内容:" + messInfo.getPublicName() + "(" + messInfo.getPublicId() + ")");
        //战斗服请求跨魂兽森林的数据值
        if (GameServer.getInstance().IsFightServer()) {
            SoulAnimalForestMessage.F2PReqSoulAnimalForestBossInfo.Builder mess = SoulAnimalForestMessage.F2PReqSoulAnimalForestBossInfo.newBuilder();
            MessageUtils.send_to_public(SoulAnimalForestMessage.F2PReqSoulAnimalForestBossInfo.MsgID.eMsgID_VALUE, mess.build().toByteArray());
        }
        if (GameServer.getInstance().IsFightServer()) {
            GuildCrossFudMessage.G2PSyncRoomInfo.Builder mess = GuildCrossFudMessage.G2PSyncRoomInfo.newBuilder();
            MessageUtils.send_to_public(GuildCrossFudMessage.G2PSyncRoomInfo.MsgID.eMsgID_VALUE, mess.build().toByteArray());
        }
    }

    /**
     * 注册战斗服的返回
     *
     * @param context
     * @param mess
     */
    @Override
    public void OnF2GResRegister(ChannelHandlerContext context, F2GResRegister mess) {
        ConnectFightManager.GetInstance().Register(mess.getFsId(), context);
        log.info("收到战斗服“" + mess.getFsName() + "(" + mess.getFsId() + ")”的注册返回!");
        ConnectFightServer cfs = ConnectFightManager.GetInstance().getConList().get(mess.getFsId());
        if (cfs == null) {
            log.error("战斗服的原始类不存在了， 请查看情况!");
            return;
        }
        cfs.setFid(mess.getFsId());
        cfs.setLastheartTime(System.currentTimeMillis());
        int id = context.channel().attr(SessionAttribute.CONNECT_SERVER_ID).get();//, id);
        String ip = context.channel().attr(SessionAttribute.CONNECT_SERVER_IP).get();//, ip);
        int sport = context.channel().attr(SessionAttribute.CONNECT_SERVER_PORT).get();//, port);
        log.info(" id = " + id + " ip= " + ip + " port=" + sport + " 的战斗服务器连接成功了！");
    }

    /**
     * 战斗服注册的请求
     *
     * @param context
     * @param mess
     */
    @Override
    public void OnG2FReqRegister(ChannelHandlerContext context, G2FReqRegister mess) {
        log.error("收到游戏服“" + mess + "的连接开始了!");
        FightClient fc = new FightClient();
        gameServerInfo sInfo = mess.getSinfo();
        fc.setIp(sInfo.getServerIP());
        fc.setPlat(sInfo.getPlatformMark());
        fc.setPort(sInfo.getServerPort());
        fc.setSid(sInfo.getServerId());
        fc.setVersion(sInfo.getVersion());
        context.channel().attr(SessionAttribute.FIGHT_CLIENT_INFO).set(fc);
        int sid = sInfo.getServerId();
        String plat = sInfo.getPlatformMark();

        FightClientManager.GetInstance().add(sid, plat, context);
        F2GResRegister.Builder msg = F2GResRegister.newBuilder();
        msg.setFsId(ServerConfig.getServerId());
        msg.setFsName(ServerConfig.getServerName());
        FightClientManager.GetInstance().send_to_game(context, F2GResRegister.MsgID.eMsgID_VALUE, msg.build().toByteArray());

    }

    /**
     * 获得最新的战斗服列表
     *
     * @param context
     * @param messInfo
     */
    @Override
    public void OnP2GResFightServerList(ChannelHandlerContext context, P2GResFightServerList messInfo) {

        gameServerInfo social = messInfo.getSocial();
        if (SocialServerClient.getInstance().channel == null && social != null) {

            if (social.getServerId() > 0) {
                //更新ip端口
                SocialServerClient.getInstance().setServerIP(social.getServerIP()).setPort(social.getServerPort());
            }

            //启动社交服务器连接
            if (social.getServerId() > 0 && !SocialServerClient.getInstance().init) {
                SocialServerClient.getInstance().init(social.getServerIP(), social.getServerPort());
                new Thread(SocialServerClient.getInstance(), "社区服务器连接").start();
                log.info("公共服 同步 社区服务器={}", messInfo);
            }
        }

        if (GameServer.getInstance().IsFightServer()) {
            return;
        }

        for (gameServerInfo sinfo : messInfo.getInfoListList()) {
            int fid = sinfo.getServerId();
            String ip = sinfo.getServerIP();
            int port = sinfo.getServerPort();

            ConnectFightServer cfs = ConnectFightManager.GetInstance().getConList().get(fid);
            if (cfs != null) {
                cfs.getMapIds().clear();
                cfs.getMapIds().addAll(sinfo.getMapIdsList());
                ConnectFightManager.GetInstance().RegisterMapids(fid, sinfo.getMapIdsList());//注册地图
                if (cfs.isTrue(ip, port)) {
                    continue;
                }
                //设置新的
                ConnectFightManager.GetInstance().getConList().get(fid).setIPPort(ip, port);

                if (!cfs.isIsConnect() && !cfs.isConnectBool()) {
                    new Thread(cfs).start();//启动连接
                }
                continue;
            }
            log.info("公共服告诉了我新的战斗服ID=" + fid + " , ip=" + ip + ", port=" + port);
            cfs = new ConnectFightServer(fid, ip, port);
            cfs.getMapIds().addAll(sinfo.getMapIdsList());
            ConnectFightManager.GetInstance().getConList().put(fid, cfs);
            ConnectFightManager.GetInstance().RegisterMapids(fid, sinfo.getMapIdsList());
            cfs.setLastheartTime(System.currentTimeMillis() + 30 * 1000);//加上30秒的心跳， 免得重复去关闭
            new Thread(cfs).start();//启动连接
        }

        log.info("公共服同步了" + messInfo.getInfoListCount() + "个战斗服到游戏服");
    }

    /**
     * 当前与战斗服的连接断开了
     *
     * @param session
     * @param serverId
     */
    @Override
    public void OnFightSessionOut(ChannelHandlerContext session, int serverId) {
        int fitghSid = serverId;
        List<Player> list = new ArrayList<>(Manager.playerManager.getPlayersCache().values());
        for (Player player : list) {
            if (player.playerCrossData.toFightSid != fitghSid) {
                continue;
            }
            player.playerCrossData.setToFightServer(false);//清除跨服标识
            player.playerCrossData.toFightSid = 0;
            player.playerCrossData.toZoneModelId = 0;
            player.playerCrossData.toFightId = 0;
            player.playerCrossData.isReqFight = false;
            player.playerCrossData.crossState = CrossState.PCS_LOCAL;

            //如果玩家在线
            if (player.getIsOnline() != 0) {
                //切换地图
                MessageUtils.notify_player(player, Notify.SHOWBOX, MessageString.FightServerBreak);
                //切换回原来的地图
                Manager.copyMapManager.outZone(player);
            } else {
                //还原地图
                player.changeMapId(player.getOld().getMapId());
                player.changeMapModelId(player.getOld().getModelId());
                player.changeLine(player.getOld().getLine());
                player.changeCurPos(player.getOld().getPos());
            }
        }
    }

    /**
     * 获取服务器信息
     *
     * @return
     */
    gameServerInfo.Builder server() {
        gameServerInfo.Builder server = gameServerInfo.newBuilder();
        server.setPlatformMark(ServerConfig.getServerPlatform());
        server.setServerIP(ServerConfig.getGameServerIp());
        server.setServerId(ServerConfig.getServerId());
        server.setServerPort(ServerConfig.getServerPort());
        server.setServerType(ServerConfig.GetServerType());
        server.setVersion(GameServer.version);
        server.setServerOpentime(ServerConfig.getServerOpenTime());
        server.setServerWorldlv(GlobalType.getWorldLevel());
        return server;
    }

    /**
     * 注册到社交服务器
     *
     * @param context
     */
    @Override
    public void G2SRegisterServer(ChannelHandlerContext context) {

        serverMessage.G2SRegisterServer.Builder message = serverMessage.G2SRegisterServer.newBuilder();
        message.setServer(server());

        MessageUtils.send_to_social(serverMessage.G2SRegisterServer.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }


    @Override
    public void OnP2GResFightServer(ChannelHandlerContext context, serverMessage.P2GResFightServer messInfo) {

    }


    //"cn","en","kor"(韩国),"tg"(泰国),"tw"(台湾繁体),"yn"(越南)
    @Override
    public String getItemString(int modelId, String lang, String defineValue) {
        if (ServerStr.getLanguageMap().containsKey(modelId)) {
            ConcurrentHashMap<String, String> langMap = ServerStr.getLanguageMap().get(modelId);
            if (langMap.containsKey(lang)) {
                return langMap.get(lang);
            }
        }
        return defineValue;
    }


    @Override
    public void loadMutilLang() {
        //"cn","en","kor"(韩国),"th"(泰国),"tw"(台湾繁体),"yn"(越南)
        ServerStr.getLanguageMap().clear();
        HashMap<String, String> langMark = new HashMap<>();
        langMark.put("cn", "cn");
        langMark.put("ros", "en");
        langMark.put("kor", "kor");
        langMark.put("th", "th");
        langMark.put("tw", "tw");
        langMark.put("yn", "yn");
        loadMutilItemLang(langMark.get(ServerConfig.getLangType()));
    }

    private void loadMutilItemLang(String lang) {
        try {
            Properties prop = new Properties();
            String proPath = SystemUtils.USER_DIR + File.separator + "config" + File.separator + "lang" + File.separator + "ItemString_" + lang + ".properties";
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(proPath), "utf-8"));
//        InputStream in = new BufferedInputStream(new FileInputStream());
            prop.load(br);
            Set keyValue = prop.keySet();
            for (Iterator it = keyValue.iterator(); it.hasNext(); ) {
                String key = (String) it.next();
                String Property = prop.getProperty(key);
                String itemKey = key.substring(5).trim();
                try {
                    int itemId = Integer.parseInt(itemKey);
                    ConcurrentHashMap<String, String> langMap = null;
                    if (ServerStr.getLanguageMap().containsKey(itemId)) {
                        langMap = ServerStr.getLanguageMap().get(itemId);
                    } else {
                        langMap = new ConcurrentHashMap<>();
                        ServerStr.getLanguageMap().put(itemId, langMap);
                    }
                    langMap.put(lang, Property);
//                serverString.put(key.toLowerCase(), Property);
                } catch (NumberFormatException e) {
                    log.error("加载语言包ID为" + lang + "失败出错的语言=" + key, e);
                }
            }
            log.error("共加载了" + lang + "语言包条数为：" + prop.size());
        } catch (Exception e) {
            log.error("加载语言包ID为" + lang + "失败出错了", e);
        }
    }

    @Override
    public void loadServerIdList() {
//        GlobalType.HEART_WEB
        try {
            StringBuilder result = new StringBuilder();
            log.info("当前获取服务器列表HTTP为：" + GlobalType.HEART_WEB + String.format(GlobalType.HEART_PARA, "serverIdList", ServerConfig.getServerId(), 1, "serverIdList"));
            int code = HttpUtils.sendPost(GlobalType.HEART_WEB, String.format(GlobalType.HEART_PARA, "serverIdList", ServerConfig.getServerId(), 1, "serverIdList"), result);
            if (code == 200) {
                List<Integer> serverids = ServerConfig.getServerIdList();
                Map map = JsonUtils.parseObject(result.toString(), Map.class);
                String ids = (String) map.get("msg");
                log.info("获取服务器id列表成功：" + ids);
                String[] idArr = ids.split(",");
                for (String idStr : idArr) {
                    int id = Integer.parseInt(idStr);
                    if (!serverids.contains(id)) {
                        serverids.add(id);
                    }
                }
            } else {
                log.error("获取服务器id列表失败，code:" + code);
            }
        } catch (Exception e) {
            log.error("获取服务器id列表错误", e);
        }
    }
}
