package common.command;

import com.data.CfgManager;
import com.data.MessageString;
import com.data.bean.Cfg_Universe_boss_Bean;
import com.game.command.scripts.ICommandScript;
import com.game.command.structs.CommandData;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import game.core.map.Position;
import game.core.util.TimeUtils;
import game.message.CommandMessage;
import game.message.CommonMessage;
import game.message.mailMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommandScript implements ICommandScript {

    private static final Logger logger = LogManager.getLogger(CommandScript.class);

    @Override
    public int getId() {
        return ScriptEnum.CommandScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void onReqJoinCommand(Player player, CommandMessage.ReqJoinCommand messInfo) {
        CommandData cmd = getCommandData(player);
        if (cmd == null) return;

        if (cmd.getCommanderId() == player.getId()) {
            logger.info(player.getInfo() + " является командиром, невозможно присоединиться");
            return;
        }

        if (cmd.getRoleList().contains(player.getId())) {
            logger.info(player.getInfo() + " уже в командной очереди");
            return;
        }

        cmd.getRoleList().add(player.getId());

        Manager.universeManager.deal().updateCmdBuff(player, cmd);

        noticeTeam(Manager.mapManager.getMap(player.getCurGps().getMapId()), cmd, player.playerCrossData.platSid, true, 0L);
    }

    @Override
    public void onReqExitCommand(Player player, CommandMessage.ReqExitCommand messInfo) {
        CommandData cmd = getCommandData(player);
        if (cmd == null) return;

        if (cmd.getCommanderId() == player.getId()) {
            logger.info(player.getInfo() + " является командиром, невозможно выйти");
            return;
        }

        if (!cmd.getRoleList().contains(player.getId())) {
            logger.info(player.getInfo() + " уже не в командной очереди");
            return;
        }

        cmd.getRoleList().remove(player.getId());
        Manager.buffManager.deal().onRemoveBuff(player, cmd.getBuffId());

        Manager.universeManager.deal().updateCmdBuff(player, cmd);

        //Клиент сам обрабатывает отмену следования
        sendCommandInfo(player, cmd.getCommanderId(), 0, 0);

        noticeTeam(Manager.mapManager.getMap(player.getCurGps().getMapId()), cmd, player.playerCrossData.platSid, true, player.getId());
    }

    @Override
    public void onReqFocusTarget(Player player, CommandMessage.ReqFocusTarget messInfo) {
        CommandData cmd = getCommandData(player);
        if (cmd == null) return;

        if (cmd.getCommanderId() != player.getId()) {
            logger.info(player.getInfo() + " не является командиром");
            return;
        }

        cmd.setTargetId(messInfo.getTargetId());

        noticeTeam(player, Manager.mapManager.getMap(player.getCurGps().getMapId()), cmd, player.playerCrossData.platSid, true, 0L, messInfo.getTargetId());
    }

    @Override
    public void onReqTargetPos(Player player, CommandMessage.ReqTargetPos messInfo) {
        CommandData cmd = getCommandData(player);
        if (cmd == null) return;

        if (cmd.getCommanderId() <= 0) {
            logger.info("Командир не найден");
            return;
        }

        MapObject mapObject = Manager.mapManager.getMap(player.getCurGps().getMapId());
        if (cmd.getTargetId() > 0) {
            Cfg_Universe_boss_Bean bean = CfgManager.getCfg_Universe_boss_Container().getValueByKey((int) cmd.getTargetId());
            if (bean != null) {
                for (Monster monster : mapObject.getMonsters().values()) {
                    if (bean.getId() == monster.getModelId()) {
                        sendResTargetPos(player, monster.getCurGps().getPos());
                        return;
                    }
                }
            }
        } else {//Если цели нет, берём координаты командира
            Player target = mapObject.getPlayer(cmd.getCommanderId());
            if (target != null) {
                sendResTargetPos(player, target.getCurGps().getPos());
                return;
            }
        }
    }

    @Override
    public void onReqCommandBulletScreen(Player player, CommandMessage.ReqCommandBulletScreen messInfo) {
        MapObject map = Manager.mapManager.getMap(player.getCurGps().getMapId());
        if (map == null) {
            return;
        }

        CommandMessage.ResCommandBulletScreen.Builder msg = CommandMessage.ResCommandBulletScreen.newBuilder();
        for (Player p : map.getPlayers().values()) {
            if (!p.playerCrossData.platSid.equals(player.playerCrossData.platSid)) {
                continue;
            }

            msg.setContext(messInfo.getContext());
            msg.setRoleId(player.getId());
            msg.setRoleName(player.getName());
            msg.setRoleCareer(player.getCareer());
            MessageUtils.send_to_player(p, CommandMessage.ResCommandBulletScreen.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    private CommandData getCommandData(Player player) {
        long mapId = player.getCurGps().getMapId();
        MapObject mapObject = Manager.mapManager.getMap(mapId);
        if (mapObject == null) {
            logger.info("Карта не существует");
            return null;
        }
        if (mapObject.getMapModelId() != 71001) {
            logger.info("Неверная карта");
            return null;
        }

        ConcurrentHashMap<String, CommandData> commandDataMap = Manager.commandManager.getCommandDataMap().get(mapId);
        if (commandDataMap == null) {
            logger.info("На текущей карте нет информации о командире");
            return null;
        }

        CommandData cmd = commandDataMap.get(player.playerCrossData.platSid);
        if (cmd == null) {
            logger.info(player.playerCrossData.platSid + " Нет командной очереди для этой фракции");
            return null;
        }
        return cmd;
    }

    private void noticeTeam(MapObject map, CommandData cmd, String platSid, boolean synCMD, long exId) {
        if (map == null || map.getPlayers().size() == 0) {
            return;
        }
        CommandMessage.ResCommandInfo.Builder msg = buildCommandInfo(map, cmd);
        //Отправка членам команды
        for (Player p : map.getPlayers().values()) {
            if (!p.playerCrossData.platSid.equals(platSid)) {
                continue;
            }

            //Исключаем указанный ID
            if (p.getId() == exId) {
                continue;
            }

            //Отправка командиру
            if (synCMD && p.getId() == cmd.getCommanderId()) {
                MessageUtils.send_to_player(p, CommandMessage.ResCommandInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
                continue;
            }

            if (!cmd.getRoleList().contains(p.getId())) {
                continue;
            }

            MessageUtils.send_to_player(p, CommandMessage.ResCommandInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    private void noticeTeam(Player commander, MapObject map, CommandData cmd, String platSid, boolean synCMD, long exId, long targetId) {
        if (map == null || map.getPlayers().size() == 0) {
            return;
        }

        String monsterName = "";
        for (Monster monster : map.getMonsters().values()) {
            if (monster.getModelId() == targetId) {
                monsterName = monster.getName();
                break;
            }
        }

        StringBuilder context = new StringBuilder(String.valueOf(MessageString.Universe_Command_BOSS_Notice));
        context.append("@_@").append(commander.getName()).append("@_@").append(monsterName);

        long nowTime = TimeUtils.Time();
        boolean canSendBulletScreen = false;
        if(nowTime-cmd.getBulletScreenTime()>5*1000){
            cmd.setBulletScreenTime(nowTime);
            canSendBulletScreen = true;
        }

        CommandMessage.ResCommandInfo.Builder msg = buildCommandInfo(map, cmd);
        //Отправка членам команды
        for (Player p : map.getPlayers().values()) {
            if (!p.playerCrossData.platSid.equals(platSid)) {
                continue;
            }

            //Исключаем указанный ID
            if (p.getId() == exId) {
                continue;
            }

            if(canSendBulletScreen){
                sendCommandBulletScreen(p, context.toString(), true);
            }

            //Отправка командиру
            if (synCMD && p.getId() == cmd.getCommanderId()) {
                MessageUtils.send_to_player(p, CommandMessage.ResCommandInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
                continue;
            }

            if (!cmd.getRoleList().contains(p.getId())) {
                continue;
            }

            MessageUtils.send_to_player(p, CommandMessage.ResCommandInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    private CommandMessage.ResCommandInfo.Builder buildCommandInfo(MapObject map, CommandData cmd) {
        CommandMessage.ResCommandInfo.Builder msg = CommandMessage.ResCommandInfo.newBuilder();
        CommandMessage.CommandInfo.Builder cmdBuilder = CommandMessage.CommandInfo.newBuilder();
        cmdBuilder.setRoleId(cmd.getCommanderId());
        cmdBuilder.setTargetId(cmd.getTargetId());
        cmdBuilder.setNum(cmd.getRoleList().size() + 1);

        Player player = map.getPlayer(cmd.getCommanderId());
        if (player != null) {
            cmdBuilder.setRoleName(player.getName());
            cmdBuilder.setRoleCareer(player.getCareer());
            cmdBuilder.setFightPower(player.getFightPoint());
            cmdBuilder.setGuildName(player.getGuildName());
            cmdBuilder.setHead(MapUtils.getHead(player));
            CommonMessage.FacadeAttribute.Builder fa = MapUtils.getFacade(player);
            cmdBuilder.setFacade(fa);
        } else {
            cmdBuilder.setRoleName("");
            cmdBuilder.setRoleCareer(0);
            cmdBuilder.setHead(MapUtils.getDefaultHead());
            cmdBuilder.setFightPower(0);
            cmdBuilder.setGuildName("");
            CommonMessage.FacadeAttribute.Builder fa = MapUtils.getFacade(
                    0,
                    0,
                    0,
                    0,
                    0,
                    0, 0);
            cmdBuilder.setFacade(fa);
        }

        msg.setInfo(cmdBuilder);
        return msg;
    }

    private void sendCommandInfo(Player player, long commanderId, long targetId, int num) {
        CommandMessage.ResCommandInfo.Builder msg = CommandMessage.ResCommandInfo.newBuilder();
        CommandMessage.CommandInfo.Builder cmdBuilder = getCMDBuilder(player, commanderId, targetId, num);

        msg.setInfo(cmdBuilder);
        MessageUtils.send_to_player(player, CommandMessage.ResCommandInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private CommandMessage.CommandInfo.Builder getCMDBuilder(Player player, long commanderId, long targetId, int num) {
        CommandMessage.CommandInfo.Builder cmdBuilder = CommandMessage.CommandInfo.newBuilder();
        cmdBuilder.setRoleId(commanderId);
        cmdBuilder.setTargetId(targetId);
        cmdBuilder.setNum(num);
        cmdBuilder.setRoleName(player.getName());
        cmdBuilder.setRoleCareer(player.getCareer());
        cmdBuilder.setFightPower(player.getFightPoint());
        cmdBuilder.setGuildName(player.getGuildName());

        cmdBuilder.setHead(MapUtils.getHead(player));
        CommonMessage.FacadeAttribute.Builder fa = MapUtils.getFacade(player);
        cmdBuilder.setFacade(fa);
        return cmdBuilder;
    }

    private void sendResTargetPos(Player player, Position pos) {
        CommandMessage.ResTargetPos.Builder msg = CommandMessage.ResTargetPos.newBuilder();
        msg.setX(pos.getX());
        msg.setY(pos.getY());
        MessageUtils.send_to_player(player, CommandMessage.ResTargetPos.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private void sendCommandBulletScreen(Player player, String context, boolean isSystem){
        CommandMessage.ResCommandBulletScreen.Builder msg = CommandMessage.ResCommandBulletScreen.newBuilder();
        if(isSystem){
            msg.setRoleId(0);
            msg.setRoleName("");
            msg.setRoleCareer(0);

            String[] tmp = context.split("@_@");
            msg.setContext(tmp[0]);
            if (tmp.length > 1) {
                for (int i = 1; i < tmp.length; ++i) {
                    msg.addParamLists(getParamStructBuilder(tmp[i]));
                }
            }
        }else{
            msg.setRoleId(player.getId());
            msg.setRoleName(player.getName());
            msg.setRoleCareer(player.getCareer());
            msg.setContext(context);
        }
        MessageUtils.send_to_player(player, CommandMessage.ResCommandBulletScreen.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private mailMessage.paramStruct.Builder getParamStructBuilder(String value) {
        mailMessage.paramStruct.Builder info = mailMessage.paramStruct.newBuilder();
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
}