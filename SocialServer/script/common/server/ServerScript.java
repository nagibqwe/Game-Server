package common.server;

import com.game.count.structs.Count;
import com.game.db.bean.ServerParamBean;
import com.game.db.dao.ServerParamDao;
import com.game.home.timer.HomeRankTimer;
import com.game.manager.Manager;
import com.game.player.structs.GlobalPlayerWorldInfo;
import com.game.player.timer.PlayerHeartTimer;
import com.game.script.struct.ScriptEnum;
import com.game.server.SocialServer;
import com.game.server.script.IServer;
import com.game.server.struct.ServerInfo;
import com.game.server.struct.ServerParams;
import com.game.utils.MessageUtils;
import game.core.json.TypeReference;
import game.core.message.Dictionary;
import game.core.message.MessageDictionary;
import game.core.message.RMessage;
import game.core.net.Config.ServerConfig;
import game.core.net.Config.ServerEnum;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import game.message.HomeMessage;
import game.message.serverMessage;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc TODO
 * @Date 2021/6/9 11:21
 * @Auth ZUncle
 */
public class ServerScript implements IServer {

    static Logger logger = LogManager.getLogger(ServerScript.class);

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.ServerScript;
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

    /**
     * 服务器心跳
     */
    @Override
    public void doHeart() {
        long curTime = TimeUtils.Time();
        long curSec = curTime / 1000L;
        long curMin = curSec / 60L;

        /**
         * 5秒执行一下玩家心跳
         */
        if (curSec % 5 == 0) {
            SocialServer.getInstance().getWorkThread().addCommand(new PlayerHeartTimer());
        }

        /**
         * 一个小时检测一次
         */
        if (curMin % 60 == 0) {
            SocialServer.getInstance().getWorkThread().addCommand(new HomeRankTimer());
        }

    }

    /**
     * 消息派发
     *
     * @param msg
     */
    @Override
    public void dispatch(RMessage msg) {

        GlobalPlayerWorldInfo player = Manager.globalPlayerManager.getPlayers().get(msg.getSrcId());
        if (player != null) {
            msg.setExecutor(player);
        }
        // 获取消息字典
        Dictionary dic = MessageDictionary.getInstance().getDictionary(msg.getId());
        if (dic == null) {
            // do someting...
            logger.error("Not found message dictionary! msgId = " + msg.getId());
            return;
        }
        //主线程
        if (msg.getExecutor() == null) {
            SocialServer.getInstance().addCommand(msg);
            return;
        }
        //工作线程
        if (isWorkerCmd(msg.getId())) {
            SocialServer.getInstance().getWorkThread().addCommand(msg);
            return;
        }
        //执行handler
        msg.action();
    }

    //工作线程执行
    boolean isWorkerCmd(int messId) {
        if (messId == HomeMessage.G2SHomeTrimVote.MsgID.eMsgID_VALUE) {
            return true;
        }
        if (messId == HomeMessage.G2SHomeLevelUp.MsgID.eMsgID_VALUE) {
            return true;
        }
        if (messId == HomeMessage.G2SHomeDecorate.MsgID.eMsgID_VALUE) {
            return true;
        }
        if (messId == HomeMessage.G2SHomeTrimRank.MsgID.eMsgID_VALUE) {
            return true;
        }
        return false;
    }

    /**
     * 连接注册
     *
     * @param context
     */
    @Override
    public void register(ChannelHandlerContext context, serverMessage.gameServerInfo mess) {

        String key = mess.getPlatformMark() + "_" + mess.getServerId();

        ServerInfo server = Manager.serverManager.getServers().get(key);
        if (server == null) {
            server = new ServerInfo();
            server.setPlat(mess.getPlatformMark());
            server.setServerId(mess.getServerId());
            server.setServerType(mess.getServerType());
            Manager.serverManager.getServers().put(server.uniqueKey(), server);
        }
        server.setServerType(mess.getServerType());
        server.setSession(context);

        serverMessage.S2GRegisterCallback.Builder msg = serverMessage.S2GRegisterCallback.newBuilder();
        msg.setServerId(ServerConfig.getServerId());
        MessageUtils.send_to_server(context, serverMessage.S2GRegisterCallback.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        logger.info("游戏服注册成功 server={}", mess);
    }

    /**
     * 连接关闭
     *
     * @param context
     * @param type
     */
    @Override
    public void close(ChannelHandlerContext context, int type) {

    }

    /**
     * 注册到公共服
     */
    @Override
    public void register2Public() {

        serverMessage.gameServerInfo.Builder sinfo = serverMessage.gameServerInfo.newBuilder();
        sinfo.setPlatformMark(ServerConfig.getServerPlatform());
        sinfo.setServerIP(ServerConfig.getGameServerIp());
        sinfo.setServerId(ServerConfig.getServerId());
        sinfo.setServerPort(ServerConfig.getServerPort());
        sinfo.setServerType(ServerEnum.SocialServer);
        sinfo.setVersion(ServerConfig.getVersion());
        sinfo.setServerOpentime(ServerConfig.getServerOpenTime());
        sinfo.setServerWorldlv(0);

        serverMessage.S2PRegisterServer.Builder msg = serverMessage.S2PRegisterServer.newBuilder();
        msg.setSinfo(sinfo);

        MessageUtils.send_to_public(serverMessage.S2PRegisterServer.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 加载服务器参数
     */
    @Override
    public void loadServerParams() {

        ServerParamDao serverParamDao = new ServerParamDao();

        List<ServerParamBean> serverParamBeans = serverParamDao.selectAll();
        for (ServerParamBean bean : serverParamBeans) {
            if (bean.getServerid() != ServerConfig.getServerId()) {
                continue;
            }
            //TODO 解析服务器参数
            if (bean.getParamkey().equals(ServerParams.HomeRankTurn.getKey())) {
                Manager.homeManager.setTurn(Integer.parseInt(bean.getParamvalue()));

            } else if (bean.getParamkey().equals(ServerParams.ServerCounts.getKey())) {
                ConcurrentHashMap<String, Count> counts = JsonUtils.parseObject(bean.getParamvalue(), new TypeReference<ConcurrentHashMap<String, Count>>() {
                });
                Manager.serverManager.setCounts(counts);
            }

        }

    }

    /**
     * 保存服务器参数
     *
     * @param key
     */
    @Override
    public void saveServerParams(ServerParams key) {

        ServerParamBean bean = new ServerParamBean();
        bean.setParamkey(key.getKey());
        bean.setServerid(ServerConfig.getServerId());

        //TODO 序列化服务器参数
        if (bean.getParamkey().equals(ServerParams.HomeRankTurn.getKey())) {

            bean.setParamvalue(Manager.homeManager.getTurn() + "");

        } else if (bean.getParamkey().equals(ServerParams.ServerCounts.getKey())) {

            bean.setParamvalue(JsonUtils.toJSONString(Manager.serverManager.getCounts()));

        }

        SocialServer.getInstance().serverSaveThread.dealSave(key, bean);
    }
}
