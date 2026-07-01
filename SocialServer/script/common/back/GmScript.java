package common.back;

import com.data.ItemChangeReason;
import com.data.MessageString;
import com.game.chat.structs.Notify;
import com.game.gm.script.IGmScript;
import com.game.manager.Manager;
import com.game.player.structs.GlobalPlayerWorldInfo;
import com.game.script.struct.ScriptEnum;
import com.game.server.struct.ServerInfo;
import com.game.utils.MessageUtils;
import game.core.util.TimeUtils;
import game.message.ChatMessage;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Desc TODO
 * @Date 2021/6/24 15:06
 * @Auth ZUncle
 */
public class GmScript implements IGmScript {

    final Logger logger = LogManager.getLogger(GmScript.class);

    /**
     * 处理命令
     *
     * @param cmd
     */
    @Override
    public void cmd(String cmd) {
        cmd(null, 0L, cmd);
    }

    /**
     * 处理命令
     *
     * @param context
     * @param roleId
     * @param cmd
     */
    @Override
    public void cmd(ChannelHandlerContext context, long roleId, String cmd) {
        logger.info("cmd={}", cmd);
        String[] params = cmd.split(" ");
        try {
            String type = params[0].toLowerCase();
            switch (type) {
                case "&setpublictime": {
                    long setTime = TimeUtils.Time();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
                    Date d = sdf.parse(params[1]);
                    if (d.getTime() > setTime) {
                        TimeUtils.setTime(d.getTime());
                    }
                    logger.error("设置时间={}", params[1]);
                }
                break;
                case "&addfurniture":
                    GlobalPlayerWorldInfo player = Manager.globalPlayerManager.getPlayers().get(roleId);
                    int modelId = Integer.parseInt(params[1]);
                    int count = Integer.parseInt(params[2]);
                    Manager.homeManager.deal().addFurniture(player, modelId, count, ItemChangeReason.GM);
                    break;
                case "&test":
                    ChatMessage.PersonalNotice.Builder msg = ChatMessage.PersonalNotice.newBuilder();
                    msg.setType(Notify.CHAT.getValue());
                    msg.setContent(MessageString.WANGNENGTISHI);
                    msg.addValue(parseParam("hello world"));
                    ServerInfo server = Manager.serverManager.getServers().get("cn_" + 20022);
                    MessageUtils.send_to_player(server.getSession(), 5635798436698340213L, ChatMessage.PersonalNotice.MsgID.eMsgID_VALUE, msg.build().toByteArray());
                    break;
                default:
                    logger.info("未知命令 cmd={}", cmd);
            }
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

    private static ChatMessage.paramStruct.Builder parseParam(String value) {
        ChatMessage.paramStruct.Builder info = ChatMessage.paramStruct.newBuilder();
        if (value.length() < 3) {
            info.setMark(0);
            info.setParamsValue(value);
            return info;
        }
        char sr = value.charAt(1);
        char srs = value.charAt(2);

        if (sr == '&' && srs == '_') {
            String[] tt = value.split("&_");
            info.setMark(Integer.parseInt(tt[0]));
            info.setParamsValue(value.substring(3));
        } else {
            info.setMark(0);
            info.setParamsValue(value);
        }
        return info;
    }

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.GmScript;
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
