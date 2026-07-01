/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.gm.manager;

import com.game.db.bean.GameMaster;
import com.game.db.dao.GameMasterDao;
import com.game.gm.log.GmCommandLog;
import com.game.gm.script.IGmScript;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import game.core.dblog.LogService;
import game.core.net.Config.ServerConfig;
import game.core.script.IScript;
import game.message.CrossServerMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Administrator
 */
public class GmCommandManager {

    private static final Logger log = LogManager.getLogger(GmCommandManager.class);

    private final GameMasterDao gmDao;

    private List<String> commands = new ArrayList<>(16);

    private final String commandKey = "commandKey";

    GmCommandManager() {
        gmDao = new GameMasterDao();
    }

    private static final HashMap<String, Integer> commandLevelMap = new HashMap<>();

    static {
        commandLevelMap.put("&jinyan", 1);
        commandLevelMap.put("&kick", 1);
        commandLevelMap.put("&ts", 1);
        commandLevelMap.put("&totask", 1);
        commandLevelMap.put("&changemap", 1);
        commandLevelMap.put("&fangchenmi", 1);
        commandLevelMap.put("&goto", 1);
        commandLevelMap.put("&wudi", 1);
        commandLevelMap.put("&selectline", 1);
        commandLevelMap.put("&whomapid", 1);
        commandLevelMap.put("&dazuo", 1);
        commandLevelMap.put("&mapcount", 1);
        commandLevelMap.put("&script", 1);
        commandLevelMap.put("&worldscript", 1);
        commandLevelMap.put("&loadscript", 1);
        commandLevelMap.put("&reload", 1);
        commandLevelMap.put("&worldreload", 1);
        commandLevelMap.put("&date", 1);
        commandLevelMap.put("&maxlogin", 1);
        commandLevelMap.put("&sd", 1);
        commandLevelMap.put("&worldloadscript", 1);
        commandLevelMap.put("&inspectplayergold", 1);
    }

    //设置玩家GM Level
    public void setPlayerGmLevel(Player player) {
        try {
            GameMaster gm = null;
            gm = gmDao.selectByUserId(player.getUserId());
//            if (gm != null) {
//                player.setGmLevel(gm.getGmLevel());
//            } else {
//                player.setGmLevel(0);
//            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    public IGmScript getGM() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.GmComandBaseScript);
        if (is instanceof IGmScript) {
            return (IGmScript) is;
        }
        log.error("没有找到GM接口的实例！");
        return null;
    }

    /**
     * @Description 游戏内部即客户端发来的GM命令的处理
     *
     * @param player
     * @param command
     */
    public void clientGmDeal(Player player, String command) {
        if (command == null || command.equals("")) {
            log.error("GM命令是空的！");
            return;
        }

        String[] strCommand = command.toLowerCase().split(" "); //GM命令以空格分隔，全部转为小写字母方便进行switch

        //GM状态权限判断
//	int gmLevel = manager.playerManager.getPlayerGmlevel(player);
//	if (gmLevel <= 0) {
//	    if ("&zhimakaimen.".equalsIgnoreCase(strCommand[0])) {
//		//MessageUtils.notify_player(player, Notify.CHAT_ROLE, AmuletManager.status + ",VERSION=" + WServer.VERSION);
//	    }
//	    return;
//	}
//	if (gmLevel > 0) {
//	    player.setGmState(GmState.GM.getValue());
//	}
//	if (!commandLevelMap.containsKey(strCommand[0]) && gmLevel == 1) { //如果无此GM命令 或 GM对该指令的权限不够 则返回
//	    return;
//	}
//	LOGGER.info(player.getGmState() + "\t" + player.getUserId() + "\t" + player.getName() + "\t" + "\t" + command);
        //GM命令执行log记录
        if (player.getUserId() > 10000000000L) {
            try {
                GmCommandLog gmCommandLog = new GmCommandLog();
                gmCommandLog.setUserId(player.getUserId());
                gmCommandLog.setRoleName(player.getName());
                gmCommandLog.setRoleId(player.getId());
                gmCommandLog.setSid(ServerConfig.getServerId());
//            gmCommandLog.setGmLevel(player.getGmLevel());
                gmCommandLog.setCommand(command);
                LogService.getInstance().execute(gmCommandLog);
            } catch (Exception e) {
                log.error(e, e);
            }
        }

        switch (strCommand[0]) {   //JDK1.7 switch case 语句开始支持String类型
            case "&reloaddata": //刷表命令
                break;
            case "&maxcondition":
                // TODO: 2019/5/14  serverParam修改屏蔽换一种方式存储

//                if (commands.isEmpty()) {
//                    String commandString = ServerParamUtil.getServerParamMap().get(commandKey);
//                    if (commandString != null) {
//                        String[] temp = commandString.split(Symbol.FENHAO);
//                        for (String s : temp) {
//                            commands.add(s);
//                        }
//                    }
//                }
//                for (String tempCommand : commands) {
//                    Manager.gmCommandManager.getGM().RunGmCmd(player, tempCommand);
//                }
//                break;
            case "&clearhook":
                player.getHookInfo().setOnHook(false);
                player.getHookInfo().setHookTime(0);
                player.getHookInfo().getItemExpAddRateTime().clear();
                Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.MEDICINESATTRIBUTE);
                Manager.playerHookManager.deal().onReqHookSetInfoHandler(player);
            default:
                //调用GM脚本
//                HashMap<String, Object> args = new HashMap<>();
//                args.put("player", player);
//                args.put("command", command);
//                ScriptManager.getInstance().call(ScriptEnum.GmComandScript, args);
                Manager.gmCommandManager.getGM().RunGmCmd(player, command);
                break;
        }

    }

    //用枚举来实现单例
    private enum Singleton {

        INSTANCE;
        GmCommandManager manager;

        Singleton() {
            this.manager = new GmCommandManager();
        }

        GmCommandManager getProcessor() {
            return manager;
        }
    }

    //获取PetManager的实例对象
    public static GmCommandManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public void sendGMToPublic(long roleId, String str) {
        CrossServerMessage.G2PGMCMD.Builder scriptMsg = CrossServerMessage.G2PGMCMD.newBuilder();
        scriptMsg.setRoleId(roleId);
        scriptMsg.setCmd(str);
        MessageUtils.send_to_public(CrossServerMessage.G2PGMCMD.MsgID.eMsgID_VALUE, scriptMsg.build().toByteArray());
        //TODO 公共服命令同步一份到社交服务器
        MessageUtils.send_to_social(CrossServerMessage.G2PGMCMD.MsgID.eMsgID_VALUE, scriptMsg.build().toByteArray());
    }
}
