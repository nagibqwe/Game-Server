package common.server;

import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.server.script.IServerStart;
import com.game.structs.SessionKey;
import game.message.serverMessage.G2PReqFightServer;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 服务器的启动脚本
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class ServerStart implements IServerStart {

    private static final Logger log = LogManager.getLogger(ServerStart.class);

    @Override
    public int getId() {
        return ScriptEnum.ServerStart;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    //主逻辑函数，此函数运行过主serverThread线程中
    @Override
    public void tick(long curtime) {
    }

    /**
     * 请求战斗服务器 根据当前人数，及相关的分配规则进行战斗服的分配及处理
     *
     * @param context
     * @param mess
     */
    @Override
    public void OnG2PReqFightServer(ChannelHandlerContext context, G2PReqFightServer mess) {

    }

    @Override
    public void OnSessionOut(ChannelHandlerContext context, int type) {
        String sid = context.channel().attr(SessionKey.SERVERPLATID).get();
        if (sid == null) {
            return;
        }
        Manager.gameServerManager.removeServer(context, sid);
        log.info(context.channel() + " 注销的服务器ID：" + sid);
    }

}
