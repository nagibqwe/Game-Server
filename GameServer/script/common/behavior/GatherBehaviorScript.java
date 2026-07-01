/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.behavior;

import com.data.CfgManager;
import com.data.bean.Cfg_Gather_Bean;
import com.game.backpack.structs.Item;
import com.game.behavior.structs.BaseBehavior;
import com.game.behavior.structs.BehaviorStatus;
import com.game.behavior.structs.IBehavior;
import com.game.behavior.structs.type.GatherBehavior;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.copymap.scripts.ICopyGatherScript;
import com.game.fight.structs.FightEnum;
import com.game.manager.Manager;
import com.game.map.structs.Area;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.map.timer.GatherBrithTimer;
import  com.data.MessageString;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.structs.EntityState;
import com.game.structs.Gather;
import com.data.ItemChangeReason;
import com.game.task.structs.Task;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import game.core.map.IMapObject;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.MapMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author zenghai
 */
public class GatherBehaviorScript implements IScript, IBehavior {

    protected Logger log = LogManager.getLogger("BaseBehavior");

    @Override
    public int getId() {
        return ScriptEnum.GatherBehaviorCommonScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public boolean Cancel(BaseBehavior behavior) {
        IMapObject owner = behavior.getOwner();
        if (owner == null) {
            return behavior.Over();
        }
        if (!(owner instanceof Player)) {
            return behavior.Over();
        }
        Player mover = (Player) owner;

        GatherBehavior gb = (GatherBehavior) behavior;
        log.info(mover.getName() + "(" + mover.getId() + ") 被打断了采集 采集物=" + gb.getModelId() + "id=" + gb.getTargetId());
        mover.removeSate(EntityState.Pick);
        mover.addState(EntityState.Stand);

        behavior.Over();

        MapObject map = Manager.mapManager.getMap(mover.gainMapId());
        if (map == null) {
            return true;
        }

        if (map.getSetting().getIsscript()> 0){
            IScript is = Manager.scriptManager.GetScriptClass(map.getSetting().getIsscript());
            if( is instanceof ICopyGatherScript){
                ((ICopyGatherScript)is).onOutGather(mover, map.getGather(gb.getTargetId()));//此物品可能是空的哦
            }
        }
        return true;
    }

    @Override
    public void action(BaseBehavior behavior) {

        if (behavior.IsOver()) {
            return;
        }

        IMapObject owner = behavior.getOwner();
        if (owner == null) {
            return;
        }
        if (!(owner instanceof Player)) {
            return;
        }

        GatherBehavior gb = (GatherBehavior) behavior;

        Player player = (Player) owner;
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        Gather gather = map.getGather(gb.getTargetId());
        if (gather == null) {
            player.removeSate(EntityState.Pick);
            player.addState(EntityState.Stand);
            behavior.SetState(BehaviorStatus.ACTIVITYSTATUS_OVERED);
            log.info("已被其他人采了 采集物=" + gb.getModelId() + "id=" + gb.getTargetId() + player);
            return;
        }

        if (TimeUtils.Time() < gb.getEnd()) {

            /**
             * 采集只有在以下情况才能打断 1）玩家死亡 2）玩家受到眩晕、击飞、击退等效果影响 3）玩家距离采集物距离超过2米 4）玩家离线
             */
            boolean canBreak = player.isDie();
            canBreak = canBreak || (Utils.getDistance(gather.gainCurPos(), player.gainCurPos()) > 5.0f);
            canBreak = canBreak || !player.isOnline();
            canBreak = canBreak || EntityState.Fly.compare(player.getState());
            canBreak = canBreak || FightEnum.SkillFreeze.compare(player.getFightState());
            if (canBreak) {
                MapUtils.OnBreakPack(player);
                player.removeSate(EntityState.Pick);
                player.addState(EntityState.Stand);
                behavior.SetState(BehaviorStatus.ACTIVITYSTATUS_OVERED);
            }
            return;
        }
        log.info(player.nameIdString() + "采集[" + gather.getName() + "]完成");
        player.removeSate(EntityState.Pick);
        player.addState(EntityState.Stand);
        behavior.SetState(BehaviorStatus.ACTIVITYSTATUS_OVERED);

        MapMessage.ResEndGather.Builder msg = MapMessage.ResEndGather.newBuilder();
        msg.setRoleId(player.getId());
        MessageUtils.send_to_roundPlayer(player, MapMessage.ResEndGather.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        Cfg_Gather_Bean gatherCfg = CfgManager.getCfg_Gather_Container().getValueByKey(gather.getModelId());
        if (null == gatherCfg) {
            log.error("配置的没有，怎么初始化的！");
            return;
        }

        //如果要消失
        if (gatherCfg.getIsHide() == 1) {
            Manager.mapManager.manager().onQuitMap(map, gather, true);
            //添加刷新时间
            if (gatherCfg.getRefresh_time() > 0) {
                map.addTimerEvent(new GatherBrithTimer(gather, gatherCfg.getRefresh_time()));
            }
        }

        //修改没有配置掉落ID的情况
        if (gatherCfg.getDropId() > 0) {
            log.info(gather + "被采集了！dropid=" + gatherCfg.getDropId() + player);
            List<List<Integer>> itemDrops = Manager.dropManager.deal().getItemDrops(player, gatherCfg.getDropId());
            List<Item> list = Item.createItems(itemDrops, 1);



            int changeReason = 0;
           if (gatherCfg.getScriptId() == ScriptEnum.SoulAnimalForestCrossCloneCrossActivityScript){
               changeReason = ItemChangeReason.SoulAnimalGatherDropGet;
           }else {
               changeReason = ItemChangeReason.DropByGatherGet;
           }
            if (GameServer.getInstance().IsFightServer()) {
                //将采集物发回去
                sendRewardFtoGame(list,player,changeReason);
                if(changeReason == ItemChangeReason.SoulAnimalGatherDropGet){
                    Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.SoulAnimalIsland, changeReason, gatherCfg.getId());
                }
            }else {
                if (!Manager.backpackManager.manager().addItems(player, list, changeReason, IDConfigUtil.getLogId())) {
                    Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System,
                            MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, list, changeReason);
                }
            }
        } else {
            if (gatherCfg.getShare() > 0) {
                Set<Player> sharelist = new HashSet<>();
                List<Area> ll = Manager.mapManager.getRounds(map, player.gainCurPos());
                Iterator<Area> iter = ll.iterator();
                while (iter.hasNext()) {
                    Area area = iter.next();
                    sharelist.addAll(area.getPlayers());
                }
                for (Player player1 : sharelist) {
                    Manager.taskManager.deal().action(player1, Task.ACTION_TYPE_GATHER, gatherCfg.getId(), 1);
                }
            } else {
                Manager.taskManager.deal().action(player, Task.ACTION_TYPE_GATHER, gatherCfg.getId(), 1);
            }
        }

        //如果有执行脚本
        if (gatherCfg.getScriptId() > 0) {

            IScript is = Manager.scriptManager.GetScriptClass(gatherCfg.getScriptId());
            if (is instanceof ICopyGatherScript) {
                ICopyGatherScript icgs = (ICopyGatherScript) is;
                icgs.onGather(player, gather);
            } else {
                log.error("gatherCfg.getScriptId()" + gatherCfg.getScriptId() + "指定的脚本并不是采集脚本");
            }

        }
    }

    /**
     * 将掉落结果发送给游戏服
     *
     * @param itemDrops
     * @param player
     */
    private void sendRewardFtoGame(List<Item> itemDrops, Player player, int reasonId) {
        if (itemDrops == null || itemDrops.size() == 0) {
            return;
        }
        Manager.crossServerManager.getCrossServer().sendReward(player,itemDrops,reasonId);
    }


}
