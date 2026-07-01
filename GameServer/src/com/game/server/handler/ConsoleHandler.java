package com.game.server.handler;

import com.game.gm.manager.GmCommandManager;
import com.game.manager.Manager;
import com.game.player.manager.PlayerAttributeManager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.soulanimalforest.manager.SoulAnimalForestCrossManager;
import com.game.structs.ServerStr;
import com.game.yed.YedMgr;
import game.core.command.ICommand;
import game.core.net.Config.ServerConfig;
import game.core.script.ScriptManager;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 *
 * @author Administrator
 */
public class ConsoleHandler implements ICommand {

    private String cmdStr = "";

    public String getCmdStr() {
        return cmdStr;
    }

    public void setCmdStr(String cmdStr) {
        this.cmdStr = cmdStr;
    }

    @Override
    public void action() {
        int scriptId = ScriptEnum.GmComandBaseScript;
        HashMap<String, Object> cmd = new HashMap<>();
        cmd.put("func", "cmd");
        cmd.put("params", cmdStr);

//        if (cmdStr.startsWith("script")) {
//            String[] cmd = cmdStr.split(" ");
//
//            int id = Integer.parseInt(cmd[1]);
//
//            if (id > 0) {
//                ScriptManager.getInstance().reload(id);
//                YedMgr.getInstance().cleanFuncsIndex++;
//            }
//            return;
//        }

//        if (cmdStr.startsWith("reload")) {
//            String[] cmd = cmdStr.split(" ");
//
//            String tablename = cmd[1];
//
////            if (tablename != null && tablename.length() > 0) {
////                Field declaredField;
////                try {
////                    declaredField = Manager.gameDataManager.getClass().getDeclaredField("Cfg_" + tablename + "Container");
////                    int hashCode = declaredField.get(Manager.gameDataManager).hashCode();
////                    Class<?> cls = declaredField.getType();
////                    Object newInstance = cls.newInstance();
////                    Method method = cls.getMethod("load");
////                    method.invoke(newInstance);
////                    declaredField.set(Manager.gameDataManager, newInstance);
////
////                    if (tablename.equalsIgnoreCase("global")) {
////                        GameServer.getInstance().initGlobal();
////                    }
////
////                    if (tablename.equalsIgnoreCase("special_monster")) {
////                        Manager.bossManager.reloadBossConfig();
////                    }
////                    if (tablename.equalsIgnoreCase("card")) {
////                        Manager.cardManager.reload();
////                    }
////                    if (tablename.equalsIgnoreCase("attributeAdd")) {
////                        PlayerAttributeManager.InitFightPercent();
////                    }
////                    if (tablename.equalsIgnoreCase("serverstr")) {
////                        try {
////                            ServerStr.load(ServerConfig.getLangType());
////                        } catch (IOException ex) {
////                        }
////                    }
////
////                    //导表后需要执行的函数
////                    if (tablename.equalsIgnoreCase("shop_Maket")) {
////                        Manager.shopManager.updateSublist();
////                    } else if (tablename.equalsIgnoreCase("equip")) {
//////                        GameServer.getInstance().equipToItem(false);
////                    }
////
////                    //加载物品表时， 也需要重置一下武器列表数据
////                    if (tablename.equalsIgnoreCase("item")) {
//////                        GameServer.getInstance().equipToItem(false);
////                    }
////                    //加载题库时，从新排序下
////                    if (tablename.equalsIgnoreCase("question_warehouse")) {
//////                        GameServer.getInstance().initQuestion();
////                    }
////
////
////                } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException ex) {
////
////                }
////            }
//            return;
//        }

        if (cmdStr.equalsIgnoreCase("quit")) {
              GameServer.getInstance().stop();
            System.out.print("有人输入了QUIT退出命令异常退出了！");
            System.exit(0);
        }
        Player player = new Player();
        player.setId(11);
        player.setName("GM");
        GmCommandManager.getInstance().clientGmDeal(player, cmdStr);
    }

}
