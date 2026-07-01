package common.server;
import com.game.connectfightserver.manager.ConnectFightManager;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.SessionAttribute;
import com.game.server.GameServer;
import com.game.server.impl.MapServer;
import com.game.server.script.IhandlerScript;
import com.game.script.structs.ScriptEnum;
import com.game.thread.FriendAndRankListProcessor;
import com.game.thread.GuildProcessor;
import com.game.thread.RegisterProcessor;
import com.game.utils.MessageEnum;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import game.core.message.MessageNumber;
import game.core.message.MsgSourceEnum;
import game.core.message.RMessage;
import game.core.net.Config.ServerConfig;
import game.core.script.IScript;
import game.message.*;
import game.message.heartMessage.ReqHeart;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author admin
 */
public class HandlerScript implements IScript, IhandlerScript {

    private static final Logger log = LogManager.getLogger(HandlerScript.class);

    final Integer[] IgnorePrintIDS = {
            PlayerMessage.ReqGetAccunonlinetime.MsgID.eMsgID_VALUE,
            ReqHeart.MsgID.eMsgID_VALUE,
            MapMessage.ReqDirMove.MsgID.eMsgID_VALUE,
            MapMessage.ReqStopMove.MsgID.eMsgID_VALUE,
            MapMessage.ReqPetMoveTo.MsgID.eMsgID_VALUE,
            MapMessage.ReqPetStopMove.MsgID.eMsgID_VALUE,
            FightMessage.ReqUseSkill.MsgID.eMsgID_VALUE,
            MapMessage.ReqMoveTo.MsgID.eMsgID_VALUE,
    };

    @Override
    public int getId() {
        return ScriptEnum.HandlerScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    //检查是否是需要发送战斗服的协议ID过滤
    @Override
    public boolean CheckFightServerId(int msgId) {
        if (msgId == RegisterMessage.ReqLoadFinish.MsgID.eMsgID_VALUE) {
            return true;
        }
        //跨服时调用副本主动退出命令
        if (msgId == CopyMapMessage.ReqCopyMapOut.MsgID.eMsgID_VALUE) {
            return true;
        }
        if (msgId == npcMessage.ReqClickNpc.MsgID.eMsgID_VALUE) {
            return true;
        }
        if (msgId == PlayerMessage.ReqUpdataPkState.MsgID.eMsgID_VALUE){
            return true;
        }
        if (msgId == ChatMessage.ChatReqCS.MsgID.eMsgID_VALUE) {
            if (ServerConfig.isTestServer()) {
                return true;
            }
        }


        int functionID = MessageNumber.getFunction(msgId);

        if (functionID == 999) {
            return true;
        }

        switch (functionID) {
            case MessageEnum.MSG_FIGHT:
            case MessageEnum.MSG_MAP:
                //复活消息本服处理
                if (msgId == MapMessage.ReqRelive.MsgID.eMsgID_VALUE) {
                    return false;
                }
            case MessageEnum.MSG_COMMAND:
                return true;

        }


        return false;
    }

    /**
     * 战斗服发送给玩家的协议过滤
     *
     * @param msgId
     * @return
     */
    @Override
    public boolean FightSendMsgID_Filter(int msgId) {
        int functionid = MessageNumber.getFunction(msgId);
        return functionid == MessageEnum.MSG_BACKAGE;
    }

    /**
     * 消息派发
     *
     * @param mess
     */
    @Override
    public void dispatch(RMessage mess) {
        try {
            if (null == mess) {
                return;
            }
            int msgid = mess.getId();

            int sourceId = MessageNumber.getSource(msgid);

            if (sourceId != MsgSourceEnum.ClientToGameServerr) {
                log.error("收到了不是gameServer应该处理 消息ID是：" + msgid);
                return;
            }
            //检查过渡服务器消息
            boolean isbl = false;
            try {
                isbl = CheckFilterMsgId(msgid, mess);
            } catch (Exception e) {
                log.error("过滤消息ID时，出错了", e);
            }
            if (isbl) {
                return;
            }

            ChannelHandlerContext iosession = mess.getContext();
            if (iosession == null) {
                log.info("连接已经空了！");
                return;
            }

            if (iosession.isRemoved()) {
                log.info("连接已经正在被移除！");
                return;
            }

            Player player = iosession.channel().attr(SessionAttribute.PLAYER).get();
            if (player != null) {
                if ( Utils.isMessagePrint() && Utils.findOne(IgnorePrintIDS, id -> id == msgid) == null) {
                    log.warn("玩家[{}]消息【ID={} name={}】 message={}", player.getName(), msgid, mess.getData().getClass().getSimpleName(), mess.getData());
                }
                mess.setExecutor(player);

                //如果玩家已经去了战斗服，那将玩家的所有协议都转发到战斗服去！
                if ( player.playerCrossData.isToFightServer() && player.playerCrossData.toFightSid > 0) {
                    isbl = false;
                    try {
                        isbl = CheckFightServerId(msgid);
                    } catch (Exception e) {
                        log.error("跨服过滤消息ID时，出错了", e);
                    }
                    if (isbl) {
                        ConnectFightManager.GetInstance().send_to_fight(player.playerCrossData.toFightSid, player.getId(), mess);
                        return;
                    }
                }
            }
            //进入跨服统一线程处理
            if (CrossFightMessage.G2FEnterCloneMap.MsgID.eMsgID_VALUE == msgid){
                GameServer.getInstance().getAssistThread().addCommand(mess);
                return;
            }

            int functionID = MessageNumber.getFunction(msgid);
            switch (functionID) {
                case MessageEnum.MSG_HEART:
                    GameServer.getInstance().getMainThread().addCommand(mess);
                    break;
                case MessageEnum.MSG_REGISTER: {
                    //只针对登陆消息进行异步处理(有一个向http请求的操作),其他消息直接执行
                    if(mess.getId() == RegisterMessage.ReqLoginGame.MsgID.eMsgID_VALUE){
                        RegisterProcessor.getInstance().addCommand(mess);
                    }
                    else{
                        mess.action();
                    }
                }
                break;
                case MessageEnum.MSG_GUILD:
                    GuildProcessor.getInstance().addCommand(mess);
                    break;
                case MessageEnum.MSG_FRIEND://好友
                case MessageEnum.MSG_RANKLIST://排行榜
                    FriendAndRankListProcessor.getInstance().addCommand(mess);
                    break;
                case MessageEnum.MSG_Peak: //巅峰竞技场
                case MessageEnum.MSG_JJC:  //竞技场
                    Manager.peakManager.addCommand(mess);
                    break;
                case MessageEnum.MSG_Activity: //运营活动
                    Manager.activityManager.addCommand(mess);
                    break;
//                case MessageEnum.MSG_Task:
//                    Manager.taskManager.addCommand(mess);
//                    break;
                case MessageEnum.MSG_LOGIN: {
                    log.error("收到了登录验证的数据， 消息ID是：" + msgid);
                }
                break;
                default: {
                    int state = LogicProcess(msgid, mess);
                    if (state > 0) {
                        //LOGGER.error("不支持的命令类型！command class: " + handler.getClass().getName() + " msgid :" + msgid + " state =" + state);
                        //丢到主线程去处理看看
                        if (state == 2) {
                            GameServer.getInstance().getMainThread().addCommand(mess);
                        } else {
                            //LOGGER.error("不支持的命令类型！command class: " + handler.getClass().getName() + " msgid :" + msgid + " 转交脚本去处理看看" + iosession.channel());

//                            //将无效连接踢掉，因断线重连引起客户端没有关闭反复再发消息
                            try {
                                OnOtherDealMsg(msgid, mess, iosession);
//                                manager.playerManager.sendQuitGameInfo(iosession, PlayerManager.QuitGame_GM);
//                                manager.registerManager.removeSession(iosession);
//                                iosession.channel().close();//断开网连接
//                                iosession.channel().unsafe().closeForcibly();

                                //接收到大于 20条无效消息，断开客户端
                                if (iosession.channel().attr(SessionAttribute.INVALIDMSGCOUNT).get() > 20){
                                    Manager.registerManager.deal().tickSession(iosession);
                                }
                            } catch (Exception e) {
                                log.error(e, e);
                            }
                        }
                    }
                }
                break;
            }

        } catch (Exception e) {
            log.error(e, e);
        }
    }

    int LogicProcess(int msgId, RMessage mess) {
        //查询玩家在那张地图上
        ChannelHandlerContext iosession = mess.getContext();
        if (iosession != null) {
            Player player = iosession.channel().attr(SessionAttribute.PLAYER).get();
            //Player player = PlayerManager.getInstance().getOnLinePlayer(roleId);
            if (player == null) {
                if (iosession.channel().attr(SessionAttribute.INVALIDMSGCOUNT).get() == null) {
                    iosession.channel().attr(SessionAttribute.INVALIDMSGCOUNT).set(1);
                }else {
                    int count =  iosession.channel().attr(SessionAttribute.INVALIDMSGCOUNT).get();
                    iosession.channel().attr(SessionAttribute.INVALIDMSGCOUNT).set(count + 1);
                }
                log.error("人不在，msgId =" + msgId + " ch=" + iosession.channel());
                return 1;
            }
            iosession.channel().attr(SessionAttribute.INVALIDMSGCOUNT).set(0);
            //获取玩家所在线程
            long mapId = player.gainMapId();
            MapServer map = GameServer.getInstance().getMServer(mapId);
            if (map == null) {
                log.error("消息ID:" + msgId + " , player =" + player + " 已经找不到可执行的线程了，地图：" + mapId + " line :" + player.gainLine() + " m:" + player.gainMapId());
                return 2;
            }

            map.addCommand(mess);
            return 0;
        } else {
            log.error("未知来源的：" + msgId);
        }
        return 3;
    }

    /**
     * 检查客户端传递给服务器的消息， 如果有问题，走新的流程处理
     *
     * @param msgId
     * @param mess
     * @return
     */
    @Override
    public boolean CheckFilterMsgId(int msgId, RMessage mess) {
        try {
            if (msgId == ReqHeart.MsgID.eMsgID_VALUE) {
                ChannelHandlerContext context = mess.getContext();
//                long roleId = context.channel().attr(SessionAttribute.ROLE_ID).get();
//                loger.error("roleId=" + roleId + " get=" + msgId);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
        return false;
    }

    //因玩家没有注册， 消息抛弃不处理
    @Override
    public boolean OnOtherDealMsg(int msgId, RMessage mess, ChannelHandlerContext session) {
        try {
//            int msgId = (int) arg.get(0);
//            Handler handle = (Handler) arg.get(1);
//            ChannelHandlerContext session = (ChannelHandlerContext) arg.get(2);

            log.info(session.channel() + " 收到会话非注册后的消息id:" + msgId + " , 处理的协议名是:" + mess.getClass().getSimpleName() + ", 消息抛弃");

        } catch (Exception e) {
            log.error(e, e);
        }
        return false;
    }

    /**
     * 过渡网络协议
     *
     * @param mess
     * @return 返回值将确定是否会往下走流程
     */
    @Override
    public boolean Filte_Handler(RMessage mess) {
        return true;
    }
}
