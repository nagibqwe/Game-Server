package common.register;

import com.game.register.script.IRegisterScript;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.ScriptEnum;
import com.game.structs.Config;
import com.game.structs.SessionAttribute;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import game.core.util.TimeUtils;
import game.message.BIMessage;
import game.message.RegisterMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.session.IoSession;

import java.util.List;
import java.util.Map;

public class RegisterScript implements IRegisterScript {

    private static final Logger log = LogManager.getLogger(RegisterScript.class);

    @Override
    public int getId() {
        return ScriptEnum.RegisterScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void loginGame(Player player) {
        RegisterMessage.ReqLoginGame.Builder msg = RegisterMessage.ReqLoginGame.newBuilder();
        long time = TimeUtils.Time();
        String sign = com.game.register.client.LoginVerifySignCal.calSign(player.getUserId(), player.getAccessToken(), player.getMachineCode(), time, player.getPlatformName());
        msg.setAccessToken(player.getAccessToken());
        msg.setMachineCode(player.getMachineCode());
        msg.setPlatformName(player.getPlatformName());
        msg.setServerId(Config.getServerId());
        msg.setSign(sign);
        msg.setUserId(player.getUserId());
        msg.setTime(time);
        msg.setIsCertify(false);
        if(player.getFuncellUUid().isEmpty()){
            msg.setFuncelUUid("test_" + player.getUserId());
        }else{
            msg.setFuncelUUid(player.getFuncellUUid());
        }
        msg.setLanguageType(0);
        msg.setRoleId(player.getId());
        msg.setIsWhite(true);
        MessageUtils.sendMsg(player, RegisterMessage.ReqLoginGame.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void loginGameSuccess(Player player, List<RegisterMessage.RoleBaseInfo> infoList) {
        player.setRoleInfoList(infoList);
        if(infoList.size() >= 1) {
            sendSelectRoleMsg(player);
        }else {
            sendCreatePlayerMsg(player);
        }
    }

    @Override
    public void reqLoadFinish(Player player) {
        RegisterMessage.ReqLoadFinish.Builder msg = RegisterMessage.ReqLoadFinish.newBuilder();
        msg.setHeight(0);
        msg.setType(0);
        msg.setWidth(0);
        MessageUtils.sendMsg(player, RegisterMessage.ReqLoadFinish.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 创建角色
     */
    private void sendCreatePlayerMsg(Player player) {
        RegisterMessage.ReqCreateCharacter.Builder msg = RegisterMessage.ReqCreateCharacter.newBuilder();
//        msg.setPlayerName(Manager.playerManager.deal().getRandomName());
        msg.setPlayerName(Manager.playerManager.deal().getRobotName(player.getrUserId()));
        if (player.getCareer() == -1) {
            msg.setCareer(RandomUtils.random(0, 1));
            player.setCareer(msg.getCareer());
        }
        BIMessage.Device.Builder device = BIMessage.Device.newBuilder();
        device.setAppId(0);
        device.setRoleId(0);
        device.setChannelId("");
        device.setSourceId("");
        device.setDeviceId("c1cbc0cb-b6a9-4905-a875-8b7b33e4e40c");
        device.setPlatform(3);
        device.setAppVersion("");
        device.setMerchant("");
        device.setNetType("WIFI");
        msg.setDevice(device);
        msg.setCareer(player.getCareer());
        MessageUtils.sendMsg(player, RegisterMessage.ReqCreateCharacter.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        log.info(player.getInfo() + " 请求创建角色,职业："+player.getCareer());
    }

    /**
     * 选择角色
     */
    private void sendSelectRoleMsg(Player player) {
        long roleId = player.getRoleInfoList().get(RandomUtils.random(0, player.getRoleInfoList().size() - 1)).getRoleId();
//        long roleId = roleInfoList.get(0).getRoleId();
        player.setId(roleId);
        RegisterMessage.ReqSelectCharacter.Builder msg = RegisterMessage.ReqSelectCharacter.newBuilder();
        msg.setPlayerId(player.getId());
        MessageUtils.sendMsg(player, RegisterMessage.ReqSelectCharacter.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        log.info(player.getInfo() + "请求选择角色,ID="+player.getId());
    }

    @Override
    public void allQuitGame(long except) {
        IoSession se = null;
        try {
            for (Player p : Manager.playerManager.getPlayers().values()) {
                if (except == p.getId())
                    continue;
                se = p.getSession();
                quitGame(p, false);
                CloseFuture f = se.closeNow();
                se.removeAttribute(SessionAttribute.PLAYER.getValue());
                f.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void quitGame(Player player, boolean isFromServer) {
        IoSession se = player.getSession();
        if (se != null) {
            RegisterMessage.ReqQuit.Builder msg = RegisterMessage.ReqQuit.newBuilder();
            player.sendMsg(RegisterMessage.ReqQuit.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            player.setSession(null);
            if (isFromServer) {
                log.error(player.getId() + "因网络断开而掉线了！");
            } else {
                log.error(player.getId() + "退出游戏,非服务器导致", new Throwable());
            }
        }
        String str = String.format("robot debug 被断开连接 ip:%s 角色唯一ID:%d 账号:%d 是否网络断开掉线:%s 角色名:%s", se.getLocalAddress(), player.getId(), player.getUserId(), isFromServer, player.getName());
        log.error(str);
        System.out.println(str);
        Manager.playerManager.deal().removePlayer(player.getId());
    }

    @Override
    public void quitGame(long userId) {
        for (Map.Entry<Long, Player> entry : Manager.playerManager.getPlayers().entrySet()) {
            if (entry.getValue().getUserId() == userId) {
                quitGame(entry.getValue(), false);
                break;
            }
        }
    }
}
