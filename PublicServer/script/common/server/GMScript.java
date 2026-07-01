/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.server;

import com.data.CfgManager;
import com.data.bean.Cfg_Cross_fudi_main_Bean;
import com.data.bean.Cfg_Daily_Bean;
import com.data.script.ScriptConfigManager;
import com.game.guildcrossfud.struct.FudCity;
import com.game.guildcrossfud.struct.FudGroup;
import com.game.manager.Manager;
import com.game.peak.timer.PeakGmEvent;
import com.game.script.ScriptEnum;
import com.game.server.filter.InnerMsgImpl;
import com.game.server.script.IGMScript;
import com.game.utils.MessageUtils;
import game.core.command.ICommand;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage;
import game.message.CrossServerMessage.P2GGMCMDResult;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * GM命令执行脚本
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class GMScript implements IGMScript {

    private static final Logger LOG = LogManager.getLogger(GMScript.class);

    @Override
    public int getId() {
        return ScriptEnum.GMScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void OnDeal(ChannelHandlerContext context, CrossServerMessage.G2PGMCMD messInfo) {
        try {
            long roleId = messInfo.getRoleId();
            String str = messInfo.getCmd();
            dealGm(context, roleId, str);
        } catch (Exception e) {
            LOG.error(e, e);
        }
    }

    @Override
    public void dealGm(ChannelHandlerContext context, long roleId, String str) {
        try {
            String[] cmd = str.trim().split(" ");
            int state = 1;
            String mesStr = "没找到匹配的GM命令";

            switch (cmd[0].toLowerCase()) {
                case "&dailybegin":
                    if (cmd.length < 2) {
                        break;
                    }
                    Cfg_Daily_Bean begin = CfgManager.getCfg_Daily_Container().getValueByKey(Integer.parseInt(cmd[1]));
                    if (begin == null) {
                        break;
                    }
                    Manager.dailyActiveManager.getGmControl().put(begin.getId(), true);
                    Manager.dailyActiveManager.deal().activeBegin(begin, 0);
                    break;
                case "&dailyend":
                    if (cmd.length < 2) {
                        break;
                    }
                    Cfg_Daily_Bean end = CfgManager.getCfg_Daily_Container().getValueByKey(Integer.parseInt(cmd[1]));
                    if (end == null) {
                        break;
                    }
                    Manager.dailyActiveManager.getGmControl().remove(end.getId());
                    Manager.dailyActiveManager.deal().activeEnd(end);
                    break;
                case "&setpublictime": {
                    state = 0;
                    long setTime = TimeUtils.Time();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
                    Date d = sdf.parse(cmd[1]);
                    if (setTime > d.getTime()) {
                        state = 1;
                        mesStr = str + "  时间设置错误";
                    } else {
                        TimeUtils.setTime(d.getTime());
                        mesStr = str + "  时间设置成功";
                    }
                    LOG.error(mesStr);
                    for (ChannelHandlerContext session : Manager.gameServerManager.getGameSessions().values()) {
                        if (session != null) {
                            Manager.zoneManager.noticeTime(session);
                        }
                    }
                }
                break;
                case "&crossfud": {
                    int type = Integer.parseInt(cmd[1]);
                    if (type == 0) {
                        Manager.fudManager.deal().allocCity(false);
                    }
                    if (type == 1) {
                        for (FudGroup group : Manager.fudManager.getGroups().values()) {
                            for (FudCity city : group.getCity().values()) {
                                Cfg_Cross_fudi_main_Bean bean = CfgManager.getCfg_Cross_fudi_main_Container().getValueByKey(city.getCityId());
                                if (bean.getPosition() == 0) {
                                    continue;
                                }
                                Manager.fudManager.deal().refreshCityBoss(group, city);
                            }
                        }
                    }
                }
                break;
                case "&addpeakscore":
                    InnerMsgImpl.getInstance().addCommand(new PeakGmEvent(roleId, Integer.parseInt(cmd[1])));
                    break;
                case "&getpublictime": {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
                    String tt = sdf.format(new Date(TimeUtils.Time()));
                    mesStr = "当前跨服的时间是:" + tt;
                    LOG.error(mesStr);
                    state = 0;
                }
                break;
                case "&publicscript":
                case "publicscript": {
                    state = 0;
                    try {
                        int scriptId = Integer.parseInt(cmd[1]);
                        boolean isload = Manager.scriptManager.reload(scriptId);
                        mesStr = "脚本ID" + scriptId + "加载" + (isload ? "成功！" : "失败！");
                        LOG.error(mesStr);
                    } catch (Exception e) {
                        LOG.error(e, e);
                        state = 1;
                        mesStr = " 参数指定脚本ID时不存在！";
                    }
                    if (cmd[0].equalsIgnoreCase("publicscript")) {
                        state = 3;
                    }
                }
                break;
//                case "&worldanswerready":
//                    mesStr = " 世界答题准备阶段！";
//                    Manager.worldAnswerManager.getIWorldAnswer().worldAnswerStageChange(1);
//                    break;
//
//                case "&worldanswerstart":
//                    mesStr = " 世界答题已经开启！";
//                    Manager.worldAnswerManager.getIWorldAnswer().worldAnswerStageChange(1);
//                    Manager.worldAnswerManager.getIWorldAnswer().worldAnswerStageChange(2);
//                    break;
//
//                case "&worldanswerover":
//                    mesStr = " 世界答题已结束！";
//                    Manager.worldAnswerManager.getIWorldAnswer().worldAnswerStageChange(3);
//                    break;

                case "&configreload":
                    configReload(cmd);
                    break;
//                case "&publicreload":
//                case "publicreload": {
//                    state = 0;
//                    String tablename = cmd[1];
//                    try {
//                        if (StringUtils.isBlank(tablename)) {
//                            mesStr = " 公共服加载数据表" + tablename + " 为空，有错哦！";
//                            state = 1;
//                        } else {
//                            Field declaredField;
//                            declaredField = Manager.gameDataManager.getClass().getDeclaredField("Cfg_" + tablename + "Container");
//                            int hashCode = declaredField.get(Manager.gameDataManager).hashCode();
//                            Class<?> cls = declaredField.getType();
//                            Object newInstance = cls.newInstance();
//                            Method method = cls.getMethod("load");
//                            method.invoke(newInstance);
//                            declaredField.set(Manager.gameDataManager, newInstance);
//
//                            if (tablename.equals("global")) {
//                                Manager.crossJJCmanager.init();
//                            }
//
//                            mesStr = "公共服加载数据表" + tablename + "成功！";
//                        }
//                    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
//                        LOG.error(e, e);
//                        state = 1;
//                        mesStr = " 公共服加载数据表" + tablename + " 出错了！";
//                    }
//
//                    if (cmd[0].equalsIgnoreCase("publicreload")) {
//                        state = 3;
//                    }
//                }
//                break;
                case "&couple":
                    couplefight(cmd);
                    break;
                default: {
                    if (str.startsWith("&")) {
                        state = 1;
                    } else {
                        state = 3;
                    }
                    mesStr = str + "在公共服的命令执行错误！";
                }
                break;
            }

            if (context == null) {
                LOG.error(str + " = " + mesStr);
                return;
            }

            if (roleId > 0) {
                P2GGMCMDResult.Builder msg = P2GGMCMDResult.newBuilder();
                msg.setRoleId(roleId);
                msg.setState(state);
                msg.setMessStr(mesStr);
                MessageUtils.send_to_game(context, P2GGMCMDResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }
        } catch (Exception e) {
            LOG.error(e, e);
        }
    }

    /**
     * 仙侣对决GM
     * @param cmd
     */
    private void couplefight(String[] cmd) {
        String type = cmd[1];
        if(type.equals("status")){
            String status = cmd[2];
            Manager.couplefightManager.addCommand(new ICommand() {
                @Override
                public void action() {
                    Manager.couplefightManager.getScript().statusChange(Integer.parseInt(status));
                }
            });
        }else if(type.equals("group")){
            Manager.couplefightManager.addCommand(new ICommand() {
                @Override
                public void action() {
                    Manager.couplefightManager.getScript().onGroupsNextRound();
                }
            });
        }else if(type.equals("champion")){
            Manager.couplefightManager.addCommand(new ICommand() {
                @Override
                public void action() {
                    Manager.couplefightManager.getScript().onChampionNextRound();
                }
            });
        }else if(type.equals("match")){
            Manager.couplefightManager.addCommand(new ICommand() {
                @Override
                public void action() {
                    Manager.couplefightManager.getScript().match();
                }
            });
        }
    }

    /**
     * 配置表重新加载
     *
     * @param command
     */
    private boolean configReload(String[] command) {
        try {
            if (command.length < 2) {
                return false;
            }
            String configName = command[1];
            LOG.info("GM 重新加载游戏数据配置:" + configName);
            String reloadName = getJavaClassReloadName(configName);
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
            }
            return isSuccess;
        } catch (Exception e) {
            LOG.error(e, e);
        }
        return false;
    }

    private String getJavaClassReloadName(String configName) {
        char oldChar = configName.charAt(0);
        char newChar = (oldChar + "").toUpperCase().charAt(0);
        String replace = configName.replaceFirst(oldChar + "", newChar + "");
        return "config.Cfg_" + replace + "_Load";
    }
}
