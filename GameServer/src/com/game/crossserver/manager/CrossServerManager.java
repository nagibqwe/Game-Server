
package com.game.crossserver.manager;

import com.game.attribute.BaseIntAttribute;
import com.game.crossfight.script.ICrossFightScript;
import com.game.crossserver.scripts.ICrossChangeMapScript;
import com.game.crossserver.scripts.ICrossServerScript;
import com.game.fightserver.manager.FightClientManager;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.player.structs.PlayerAttributeType;
import com.game.player.structs.Player;
import com.game.robot.manager.RobotManager;
import com.game.robot.struct.Robot;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import game.core.json.TypeReference;
import game.core.script.IScript;
import game.core.util.JsonUtils;
import game.message.CrossServerMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 跨服处理结果
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class CrossServerManager {

    private static final Logger log = LogManager.getLogger(CrossServerManager.class);

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        CrossServerManager processor;

        Singleton() {
            this.processor = new CrossServerManager();
        }

        CrossServerManager getProcessor() {
            return processor;
        }
    }

    public ICrossFightScript crossFightdeal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.CrossFightBaseScript);
        if (is instanceof ICrossFightScript) {
            return (ICrossFightScript) is;
        }
        log.error("找不到跨服战的实例！");
        return null;
    }


    public static CrossServerManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    private ConcurrentHashMap<String, Boolean> fightEndCache = new ConcurrentHashMap<>();

    public ConcurrentHashMap<String, Boolean> getFightEndCache() {
        return fightEndCache;
    }

    public ICrossServerScript getCrossServer() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.CrossServerBaseScript);
        if (is instanceof ICrossServerScript) {
            return (ICrossServerScript) is;
        }
        log.error("跨服的脚本系统实例没有找到===================");
        return null;

    }

    public ICrossChangeMapScript GetCrossChangeMap() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.CrossChangeMapBaseScript);
        if (is instanceof ICrossChangeMapScript) {
            return (ICrossChangeMapScript) is;
        }
        log.error("跨服的切换地图脚本系统实例没有找到===================");
        return null;
    }

    public void ParseAttCals(Player player, String attlist) {
        try {
            ConcurrentHashMap<PlayerAttributeType, BaseIntAttribute> cals = JsonUtils.parseObject(attlist,
                    new TypeReference<ConcurrentHashMap<PlayerAttributeType, BaseIntAttribute>>() {
                    });
            if (cals != null) {
                player.PlayerCalculators().clear();
                player.PlayerCalculators().putAll(cals);
                //重新计算BUFF的属性
                Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.BUFF);
            }
        } catch (Exception e) {
            log.error(e, e);
            log.error(player + "转换玩家的属性数据时出错了！");
        }
    }

    public void ParseAttCals(Robot robot, String attlist) {
        try {
            ConcurrentHashMap<PlayerAttributeType, BaseIntAttribute> cals = JsonUtils.parseObject(attlist,
                    new TypeReference<ConcurrentHashMap<PlayerAttributeType, BaseIntAttribute>>() {
                    });
            if (cals != null) {
                robot.getPlayerCalculators().clear();
                robot.getPlayerCalculators().putAll(cals);
                //重新计算BUFF的属性
                RobotManager.getInstance().deal().OnCalcBuffAttribute(robot, robot.getPet(), PlayerAttributeType.BASE);
            }
        } catch (Exception e) {
            log.error(e, e);
            log.error(robot + "转换机器人的属性数据时出错了！");
        }
    }

    public void NoticeCrossCloneOutToPlayer(Player player, MapObject mapObject) {
        //设置下线
        player.dealOffLine();
        CrossServerMessage.F2GCloneClose.Builder msg = CrossServerMessage.F2GCloneClose.newBuilder();
        msg.setFightId(mapObject.getId());
        msg.setModelId(mapObject.getZoneModelId());
        msg.addRoleIds(player.getId());
        if (FightClientManager.GetInstance().send_to_game(player.getIosession(), CrossServerMessage.F2GCloneClose.MsgID.eMsgID_VALUE, msg.build().toByteArray())) {
            log.error("游戏服:" + player.getIosession() + "的离开消息发送成功！");
        } else {
            log.error("游戏服:" + player.getIosession() + "的离开消息发送失败了！");
        }
        //退出离开
        player.setIosession(null);
        //设置下线
        player.setIsOnline((byte) 0);
    }

    /**
     * 通知公共服， 某个玩家不想参加某个活动了
     */
    public void sendToPublicPlayerOutCrossFight(Player player, long fightId, int modelId) {
        CrossServerMessage.F2PPlayerOutFightRoom.Builder msg = CrossServerMessage.F2PPlayerOutFightRoom.newBuilder();
        msg.setFightId(fightId);
        msg.setModelId(modelId);
        msg.setRoleId(player.getId());
        MessageUtils.send_to_public(CrossServerMessage.F2PPlayerOutFightRoom.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }
}
