package common.copyMap.guild;

import com.data.CfgManager;
import com.data.FunctionVariable;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.*;
import com.game.backpack.structs.Item;
import com.game.copymap.structs.GuildTaskData;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapParam;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.game.task.structs.ConquerTask;
import com.game.task.structs.Task;
import com.game.team.structs.TeamInfo;
import com.game.utils.MessageUtils;
import com.game.world_help.struct.WorldHelp;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.CopyMapMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 公会任务副本
 */
public class GuildTaskMapScript implements IMapBaseScript {

    private static final Logger logger = LogManager.getLogger(GuildTaskMapScript.class);
    //最大波数
    private static final int MaxWave = 4;

    @Override
    public int getId() {
        return ScriptEnum.GuildTaskMapScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void onCreate(MapObject mapObject, Object... objects) {
        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(mapObject.getZoneModelId());
        GuildTaskData data = MapParam.getGuildTaskData(mapObject);
        data.setStage(0);
        data.setRemainBossNum(mapObject.getMonsters().size());
        long now = TimeUtils.Time();
        data.setStartTime((int) ((now + bean.getEnter_time()) / 1000));
        data.setEndTime((int) ((now + bean.getExist_time()) / 1000));

        //准备时间过后开始刷怪
        mapObject.addMapOnceScriptEventTimer(getId(), "refreshMonster", bean.getEnter_time());

        //副本结束计时器
        mapObject.addMapOnceScriptEventTimer(getId(), "copyEnd", bean.getExist_time());

    }

    @Override
    public boolean canEnterMap(Player player, int model, int level) {
        return true;
    }

    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {
        GuildTaskData data = MapParam.getGuildTaskData(map);
        TeamInfo team = Manager.teamManager.getTeam(player.getTeamId());
        if(team == null||player.getId() == team.getLeaderId()){//没有队伍（单人）或者是队长
            data.setLeaderId(player.getId());
            data.setLeaderName(player.getName());
            ConquerTask task = player.getCurConquerTasks().get(2);
            if(task != null&&task.getModelId()>0){
                data.setTaskId(task.getModelId());
            }
        }else if(team!=null&&player.getId()!=team.getLeaderId()){//协助者
            data.setHelp(true);
        }else{
            logger.error(player.getInfo()+"不满足进入副本条件");
        }
        WorldHelp worldHelp = Manager.worldHelpManager.getTaskJoin().get(player.getId());
        if(worldHelp != null){
            data.setHelpId(worldHelp.getId());
        }
        data.getPlayerIds().add(player.getId());
        sendResGuildTaskCopyEnter(player, map, data);
    }

    private void sendResGuildTaskCopyEnter(Player player, MapObject map, GuildTaskData data) {
        CopyMapMessage.ResGuildTaskCopyEnter.Builder builder = CopyMapMessage.ResGuildTaskCopyEnter.newBuilder();
        builder.setStartTime(data.getStartTime());
        builder.setEndTime(data.getEndTime());
        builder.setStage(data.getStage());
        builder.setMonsterCount(data.getRemainBossNum());
        builder.setStageCount(MaxWave);
        MessageUtils.send_to_player(player, CopyMapMessage.ResGuildTaskCopyEnter.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    private void sendResGuildTaskCopyInfo(Player player, MapObject map, GuildTaskData data) {
        CopyMapMessage.ResGuildTaskCopyInfo.Builder builder = CopyMapMessage.ResGuildTaskCopyInfo.newBuilder();
        builder.setStage(data.getStage());
        builder.setMonsterCount(data.getRemainBossNum());
        MessageUtils.send_to_player(player, CopyMapMessage.ResGuildTaskCopyInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {

    }

    @Override
    public void onDamage(MapObject mapObject, Monster monster, long damage, Fighter attacker) {

    }

    @Override
    public void onMonsterDie(MapObject mapObject, Monster monster, Fighter attacker) {
        GuildTaskData data = MapParam.getGuildTaskData(mapObject);
        boolean nextStage= false;
        if(data.getRemainBossNum()<=1){
            nextStage=true;
        }
        data.setRemainBossNum(data.getRemainBossNum() - 1);

        if(nextStage){
            //是否最后一波
            Cfg_Clone_monster_Bean bean = CfgManager.getCfg_Clone_monster_Container().getValueByKey(mapObject.getZoneModelId() * 1000 + data.getStage());
            if (bean == null) {
                logger.error("Cfg_Clone_monster_Bean配置表不存在：" + mapObject.getZoneModelId() * 1000 + data.getStage());
                return;
            }
            if (bean.getIf_end() == 1) {
                copyEnd(mapObject, true);
                return;
            }

            if(data.getStage()<MaxWave){
                data.setStage(data.getStage() + 1);
                int monsterNum=refreshMonster(mapObject);
                data.setRemainBossNum(monsterNum);
            }
        }

        //同步怪物数量阶段
        synMonsterInfo(mapObject,data);
    }

    @Override
    public void onMonsterAfterDie(MapObject mapObject, Monster monster, Fighter attacker) {

    }

    @Override
    public void onLeaveBattle(MapObject map, Monster monster, Player attacker) {

    }

    private void synMonsterInfo(MapObject mapObject, GuildTaskData data){
        for (Player p:mapObject.getPlayers().values()) {
            sendResGuildTaskCopyInfo(p,mapObject,data);
        }
    }

    @Override
    public void onPlayerDie(MapObject mapObject, Fighter attacker, Player player) {

    }

    @Override
    public void action(MapObject mapObject, String method, Object[] params) {
        switch (method) {
            case "refreshMonster":
                GuildTaskData data = MapParam.getGuildTaskData(mapObject);
                data.setStage(data.getStage() + 1);
                int monsterNum = refreshMonster(mapObject);
                data.setRemainBossNum(monsterNum);
                synMonsterInfo(mapObject,data);
                break;
            case "copyEnd":
                copyEnd(mapObject, false);
                break;
        }
    }

    private int refreshMonster(MapObject mapObject) {
        GuildTaskData data = MapParam.getGuildTaskData(mapObject);
        List<Monster> list = Manager.copyMapManager.manager().copyMapRefreshMonster(mapObject, mapObject.getZoneModelId(), data.getStage());
        return list.size();
    }

    /**
     * 副本结束
     */
    private void copyEnd(MapObject mapObject, boolean success) {
        if (mapObject.isStop()) {
            return;
        }
        mapObject.setStop(true);
        //副本结算
        finish(mapObject, success);
    }

    private void finish(MapObject mapObject, boolean success) {
        GuildTaskData data = MapParam.getGuildTaskData(mapObject);
        for (Long pId:data.getPlayerIds()) {
            Player p = Manager.playerManager.getPlayer(pId);
            if(p == null){//玩家已经不在缓存中了
                continue;
            }
            sendReward(p, mapObject, data, success);

            Manager.copyMapManager.logic().biInstance(p, mapObject.getZoneModelId(), 1, success ? 1 : 2, 0, false);
        }
    }

    private void sendReward(Player player, MapObject mapObject, GuildTaskData data, boolean success){
        CopyMapMessage.ResGuildTaskCopyResult.Builder builder = CopyMapMessage.ResGuildTaskCopyResult.newBuilder();
        boolean isLeader = player.getId() == data.getLeaderId();
        if(success){
            builder.setRoleName(data.getLeaderName());
            List<Item> items;
            //协助成功，发放奖励
            int taskId=data.getTaskId();
            Cfg_Task_conquer_Bean bean = CfgManager.getCfg_Task_conquer_Container().getValueByKey(taskId);
            if (bean == null) {
                builder.setWinType(0);
                builder.addAllRewardlist(new ArrayList<>());
                MessageUtils.send_to_player(player, CopyMapMessage.ResGuildTaskCopyResult.MsgID.eMsgID_VALUE, builder.build().toByteArray());
                return;
            }
            if(isLeader){
                builder.setWinType(0);
                items = Item.createItems(bean.getRewards());
                for (Item item : items) {
                    CopyMapMessage.cardItemInfo.Builder itemInfo = CopyMapMessage.cardItemInfo.newBuilder();
                    itemInfo.setItemId(item.getItemModelId());
                    itemInfo.setNum(item.getNum());
                    itemInfo.setBind(item.isBind());
                    builder.addRewardlist(itemInfo);
                }
                //完成仙盟任务
                Manager.taskManager.deal().action(player, Task.ACTION_TYPE_GUILD_TASK_CLONE, mapObject.getZoneModelId(), 1);

                //发放感谢道具
                int helpId = data.getHelpId();
                boolean isHelp = data.isHelp();
                Manager.worldHelpManager.getScript().sendTaskHelpReward(player, mapObject, helpId, isHelp);

                //特殊情况处理：队长退出退伍（移除旧支援），然后发起新支援，上一个副本队友完成副本后队长任务完成，应该移除队长的新支援请求
                Manager.worldHelpManager.getScript().clearTask(player.getId());
            }else{//协助者
                builder.setWinType(2);
                items = Item.createItems(bean.getAssistrewards());
                if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.CopyMapGet, IDConfigUtil.getLogId())) {
                    Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail
                            , MessageString.System, MessageString.BAGISSPACETOMAIL
                            , MessageString.GetAwardNotEnoughSpaceContent, items, ItemChangeReason.CopyMapGet);
                }

                if (Manager.worldHelpManager.getTaskHistory().containsKey(data.getHelpId())){
                    Cfg_World_Support_Bean wsb = cfg(player.getLevel());
                    if (wsb != null) {
                        //增加一个展示道具给客户端
                        int itemId = wsb.getPic_res().get(0);
                        int num = wsb.getPic_res().get(1);
                        int maxNum = wsb.getPic_res().get(2);
                        int totalCount = (int) Manager.countManager.getCount(player, BaseCountType.WorldHelp, itemId);
                        int add = Math.min(num, maxNum - totalCount);
                        if (totalCount<maxNum) {
                            CopyMapMessage.cardItemInfo.Builder itemInfo = CopyMapMessage.cardItemInfo.newBuilder();
                            itemInfo.setItemId(itemId);
                            itemInfo.setNum(add);
                            itemInfo.setBind(true);
                            builder.addRewardlist(itemInfo);
                        }
                    }
                }

                for (Item item : items) {
                    CopyMapMessage.cardItemInfo.Builder itemInfo = CopyMapMessage.cardItemInfo.newBuilder();
                    itemInfo.setItemId(item.getItemModelId());
                    itemInfo.setNum(item.getNum());
                    itemInfo.setBind(item.isBind());
                    builder.addRewardlist(itemInfo);
                }

                Manager.countManager.addCount(player, BaseCountType.GuildTaskMapSupport, 0, Count.RefreshType.CountType_Forever,1);
                Manager.controlManager.operate(player, FunctionVariable.GuildCopySupport, 1);
            }
        }else{//失败
            if(mapObject.getPlayer(player.getId())==null){
                return;
            }
            builder.setWinType(1);
            builder.setRoleName(data.getLeaderName());
            builder.addAllRewardlist(new ArrayList<>());
        }
        MessageUtils.send_to_player(player, CopyMapMessage.ResGuildTaskCopyResult.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    private Cfg_World_Support_Bean cfg(int lvl) {
        for (Cfg_World_Support_Bean bean : CfgManager.getCfg_World_Support_Container().getValuees()) {
            if (bean.getLevel_rank().size() == 2
                    && bean.getLevel_rank().get(0) <= lvl && lvl <= bean.getLevel_rank().get(1))
                return bean;
        }
        return null;
    }

    @Override
    public void removeMap(MapObject mapObject){
    }
}
