package common.leaderpreach;

import com.data.*;
import com.data.bean.Cfg_Characters_Bean;
import com.data.bean.Cfg_Leader_Preach_Bean;
import com.data.bean.Cfg_Leader_Preach_value_Bean;
import com.data.bean.Cfg_Vip_Bean;
import com.data.struct.ReadArray;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.structs.Notify;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.count.structs.VariantType;
import com.game.leaderpreach.inter.ILeaderPreachScript;
import com.game.leaderpreach.struct.LPPlayerData;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.GlobalType;
import com.game.team.structs.TeamInfo;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import com.game.vip.manager.VipManager;
import com.game.vip.structs.VipPower;
import game.core.map.Position;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.DailyactiveMessage;
import game.message.HookMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

/**
 * @explain: desc
 * @time Created on 2019/11/8 15:25.
 * @author: tc
 */
public class LeaderPreachScript implements ILeaderPreachScript {
    private static final Logger log = LogManager.getLogger(LeaderPreachScript.class);

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.LeaderPreachScript;
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


    /**
     * 请求进入掌门传道副本
     *
     * @param player
     * @param
     */
    @Override
    public void onReqLeaderPreachEnter(Player player) {

        long count = Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.ActivePoint);
        if (count <= 0) {
            log.info("没有活跃点");
            return ;
        }
        //策划喊地图ID写死不会变,他觉得配置global麻烦
        if (player.getCurGps().getModelId() != 102599) {
            log.info("地图不匹配");
            return;
        }
        //策划喊坐标也写死不会变,他觉得配置global麻烦
        Position targetPos = new Position(161, 225);
        if (Utils.getDistance(player.getCurGps().getPos(), targetPos) > 10) {
            log.info("不在范围内");
            return;
        }

        int buffID = Global.Leader_Preach_Buff.get(0);
        if (!canEnter(player))
            return;

        Manager.buffManager.deal().onAddBuff(player, player, buffID);
        LPPlayerData data = null;
        for (int i = 0; i < Manager.leaderPreachManager.getWaitPIds().size(); i++) {
            if (Manager.leaderPreachManager.getWaitPIds().get(i).getPlayerID() == player.getId()) {
                data = Manager.leaderPreachManager.getWaitPIds().get(i);
                break;
            }
        }
        if(data !=null){
            log.info("已在传道中");
            return;
        }
        Manager.leaderPreachManager.getWaitPIds().add(new LPPlayerData(player, buffID,count));

        //进入传道成功返回
        DailyactiveMessage.SyncLeaderPowerStage.Builder msg = DailyactiveMessage.SyncLeaderPowerStage.newBuilder();
        MessageUtils.send_to_player(player, DailyactiveMessage.SyncLeaderPowerStage.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        checkFunctionVariable(player);
    }

    /**
     * 打坐姿势状态
     *
     * @param player
     * @param isSit
     */
    public void onLeaderSitDown(Player player, boolean isSit) {
        HookMessage.ResLeaderSitDown.Builder msgSit = HookMessage.ResLeaderSitDown.newBuilder();
        msgSit.setRoleId(player.getId());
        msgSit.setIsTrue(isSit);
        MessageUtils.send_to_roundPlayer(player, HookMessage.ResLeaderSitDown.MsgID.eMsgID_VALUE, msgSit.build().toByteArray());
    }

    public void onLeaveLeaderPreach(Player player) {

        LPPlayerData playerData = null;
        for (int i = 0; i < Manager.leaderPreachManager.getWaitPIds().size(); i++) {
            if (Manager.leaderPreachManager.getWaitPIds().get(i).getPlayerID() == player.getId()) {
                playerData = Manager.leaderPreachManager.getWaitPIds().get(i);
                break;
            }
        }
        if (playerData != null) {
            sendLeaderOverMsg(player, playerData, true);
            playerData.setExit(true);
            Manager.leaderPreachManager.getWaitPIds().remove(playerData);
            checkFunctionVariable(player);
        }
    }

    private void sendPlayerOut(Player player, LPPlayerData playerData){

        Manager.buffManager.deal().onRemoveBuff(player, playerData.getBuffID());
        //TODO 策划暂时喊写死，后面他配置了我在改
        Manager.buffManager.deal().onRemoveBuff(player, 90003);
        DailyactiveMessage.ResLeaderReward.Builder msg = DailyactiveMessage.ResLeaderReward.newBuilder();
        msg.setAddExp(0);
        msg.setChangeLevel(0);
        msg.setDecActivePoint(0);
        MessageUtils.send_to_player(player, DailyactiveMessage.ResLeaderReward.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 心跳
     */
    @Override
    public void action() {

        if (checkResetMap())
            return;
        if (Manager.leaderPreachManager.getWaitPIds().size() == 0)
            return;
        Iterator<LPPlayerData> iterator = Manager.leaderPreachManager.getWaitPIds().iterator();

        while (iterator.hasNext()) {
           LPPlayerData playerData = iterator.next();

           Player player = Manager.playerManager.getPlayerCache(playerData.getPlayerID());
             if (player == null) {
                 iterator.remove();
                 continue;
             }
             if (player.getIsOnline() < 1){
                 iterator.remove();
                 continue;
             }
             if (playerData.isExit()) {
                 iterator.remove();
                 continue;
             }
             if (player.getCurGps().getModelId() !=102599) {
                 playerData.setExit(true);
                 sendPlayerOut(player,playerData);
                 continue;
             }
             Position targetPos = new Position(161, 225);
             if (Utils.getDistance(player.getCurGps().getPos(), targetPos) >10) {
                 log.info("不在范围内");
                 playerData.setExit(true);
                 sendPlayerOut(player,playerData);
                 continue;
             }
        }
    }


    /**
     * 下线踢出副本
     *
     * @param player
     */
    @Override
    public void offline(Player player) {
        //MapObject map = Manager.mapManager.getMap(player.gainMapId());
        //if (map == null)
        //	return;
        //
        //if (map.getZoneModelId() != Manager.leaderPreachManager.getMapId())
        //	return;
        //Manager.copyMapManager.outZone(player);
    }

    private boolean checkResetMap() {
        // 检查是否跨天
        long now = TimeUtils.Time();
        //long zeroOffset = Global.Daily_times_reset_time * GlobalType.MILLIS_PER_HOUR; 策划要求改为0点
        if (TimeUtils.isSameDay(now , Manager.leaderPreachManager.getLastResetDataTime()))
            return false;
        Manager.leaderPreachManager.setLastResetDataTime(now);
       //for (LPPlayerData playerData : Manager.leaderPreachManager.getWaitPIds()) {
       //    Player player = Manager.playerManager.getPlayerCache(playerData.getPlayerID());
       //    if (player == null)
       //        continue;
       //    sendLeaderOverMsg(player, playerData, true);
       //}
       //Manager.leaderPreachManager.getWaitPIds().clear();
        return true;
    }

    private boolean canEnter(Player player) {
        if (Manager.currencyManager.manager().getCurrencyIntNum(player, ItemCoinType.ActivePoint) <= 0)
            return false;
        return true;
    }

    private void checkFunctionVariable(Player player) {
        if (player.getTeamId() != 0) {
            TeamInfo teamInfo = Manager.teamManager.getTeam(player.getTeamId());
            if (teamInfo != null && teamInfo.getMembers().size() >= 2 && player.getGuildId() > 0) {
                for (long roleId : teamInfo.getMembers()) {
                    if (roleId == player.getId()) {
                        continue;
                    }

                    Player p = Manager.playerManager.getPlayer(roleId);
                    if (p.getGuildId() != player.getGuildId()) {
                        return;
                    }
                }
                Manager.countManager.addVariant(player, VariantType.GroupLeaderpreach, 1);
                Manager.controlManager.operate(player, FunctionVariable.GroupLeaderpreach, 1);
            }
        }
    }

    private void sendLeaderOverMsg(Player player, LPPlayerData playerData, boolean isSendRewad) {
        int addTime = (int) Math.ceil((TimeUtils.Time() - playerData.getStartTime()) / 1000f);

        // n = 0; 扣点数
        long count = playerData.getDecActivePoint();
        if (count <=0){
            log.error("活跃点小于 0 ");
            return;
        }


        //VIP加成
        boolean iscan = Manager.vipManager.power().canFree(player, VipPower.POWER_39);
        int addpre = 0;
        if (iscan){
            addpre = Manager.vipManager.power().getVipPowerValue(player, VipPower.POWER_39);
        }
        float pre =  (addpre/10000f)+1;
        //根据狂欢周,来处理经验,这里是 基础经验*2
        if (Manager.controlManager.deal().isOpenFunction(player, FunctionStart.CrazySat)) {
            pre += 1;
        }
        long addallexp = 0;
        for (int i = 0 ;i < count; i++){
            Cfg_Characters_Bean characters_bean = CfgManager.getCfg_Characters_Container().getValueByKey(player.getLevel());
            if (characters_bean == null){
                log.error("characters_bean == null  {} " ,player.getLevel());
                continue;
            }
            long nowExp = player.getCurrencys().get(ItemCoinType.EXP);
            Manager.controlManager.operate(player, FunctionVariable.CurDayActiveValueCos, 1);
            long baseExp = characters_bean.getLeader_Preach_award();
            baseExp = (long) (baseExp *pre);
            addallexp +=baseExp;
            if (addallexp + nowExp >  characters_bean.getExp()){
                Manager.currencyManager.manager().onChangeFinalExp(player, addallexp, ItemChangeReason.LeaderPreachAddExpGet, IDConfigUtil.getLogId());
                addallexp = 0;

            }else {
                if (i == count-1){
                    Manager.currencyManager.manager().onChangeFinalExp(player, addallexp, ItemChangeReason.LeaderPreachAddExpGet, IDConfigUtil.getLogId());
                    addallexp = 0;
                }
            }
            playerData.setAddExp(playerData.getAddExp() + baseExp);
            Manager.countManager.addVariant(player, VariantType.Daily_LeaderPreach_Time, 1);
        }
        int addlevel = player.getLevel() - playerData.getStartLevel();
        Manager.currencyManager.manager().onDecItemCoin(player, count, ItemChangeReason.LeaderPreachDec, IDConfigUtil.getLogId(), ItemCoinType.ActivePoint);
        Manager.countManager.addVariant(player, VariantType.DailyActivePointCost, count);

        Manager.buffManager.deal().onRemoveBuff(player, playerData.getBuffID());
        //TODO 策划暂时喊写死，后面他配置了我在改
        Manager.buffManager.deal().onRemoveBuff(player, 90003);

        if (isSendRewad) {
            DailyactiveMessage.ResLeaderReward.Builder msg = DailyactiveMessage.ResLeaderReward.newBuilder();
            msg.setAddExp(playerData.getAddExp());
            msg.setChangeLevel(addlevel);
            msg.setDecActivePoint((int) count);
            MessageUtils.send_to_player(player, DailyactiveMessage.ResLeaderReward.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }

        HookMessage.ResLeaderSitDown.Builder msgSit = HookMessage.ResLeaderSitDown.newBuilder();
        msgSit.setRoleId(player.getId());
        msgSit.setIsTrue(false);
        MessageUtils.send_to_roundPlayer(player, HookMessage.ResLeaderSitDown.MsgID.eMsgID_VALUE, msgSit.build().toByteArray());

        //if (playerData.getRewardList())
        //Manager.backpackManager.manager().getName(items.get(i).getItemModelId())
        int h =  addTime/3600;
        int m =  addTime%3600/60;
        int s =  addTime%3600%60;
        StringBuffer sb=new StringBuffer();
        sb.append((h<10)?"0"+h+":":""+h+":");
        sb.append((m<10)?"0"+m+":":""+m+":");
        sb.append((s<10)?"0"+s:""+s);
        MessageUtils.notify_player(player, Notify.CHAT, MessageString.LeaderPreach_Tips, sb.toString(),playerData.getAddExp());
        //BI
//        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.LeaderPreach, ItemChangeReason.LeaderPreachDec, (int)Manager.countManager.getVariant(player, VariantType.DailyActivePointCost));
        //
        long changeExp = playerData.getAddExp();
        Manager.biManager.getScript().biResource(player, changeExp>0?1:0, ItemCoinType.EXP, BigInteger.valueOf(changeExp), playerData.getBeforeExe(), player.getTotalExp(), playerData.getStartLevel(), player.getLevel(), ItemChangeReason.LeaderPreachAddExpGet, IDConfigUtil.getLogId());
    }
}
