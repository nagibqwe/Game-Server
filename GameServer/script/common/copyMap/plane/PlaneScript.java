package common.copyMap.plane;

import com.data.CfgManager;
import com.data.MessageString;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Clone_monster_Bean;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.backpack.structs.Item;
import com.game.copymap.log.CopyMapLogRecord;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapParam;
import com.game.copymap.structs.PlaneMapData;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.map.structs.GroundBuff;
import com.game.map.structs.MapObject;
import com.game.monster.structs.Monster;
import com.game.npc.structs.Npc;
import com.game.npc.structs.Tombstone;
import com.game.pet.structs.Pet;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.skill.structs.SkillMagic;
import com.game.structs.Fighter;
import com.game.structs.Gather;
import com.data.ItemChangeReason;
import com.game.task.structs.Task;
import com.game.utils.MessageUtils;
import game.core.dblog.LogService;
import game.core.map.Position;
import game.core.util.IDConfigUtil;
import game.message.CopyMapMessage;
import game.message.MapMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * 位面脚本
 *
 * @author wzq
 */
public class PlaneScript implements IMapBaseScript {
    private final static Logger logger = LogManager.getLogger(PlaneScript.class);

    @Override
    public int getId() {
        return ScriptEnum.PlaneActivityScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void action(MapObject mapObject, String method, Object[] params) {
        switch (method) {
            case "timeOut":
                finish(mapObject, false);
                break;
            case "refreshMonster":
                refreshMonster(mapObject);
                break;
//            case "secondEnd":
//                secondEnd(mapObject);
//                break;
        }
    }

    /**
     * 结束流程
     *
     * @param mapObject
     */
//    private void secondEnd(MapObject mapObject) {
//        for (Player player : mapObject.getPlayers().values()) {
//            Manager.copyMapManager.outZone(player);
//        }
//        mapObject.setStop(true);
//    }
    @Override
    public void onCreate(MapObject mapObject, Object... objects) {
        //生成怪物
        refreshMonster(mapObject);
        //添加timer计时器
        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(mapObject.getZoneModelId());
        mapObject.addMapOnceScriptEventTimer(getId(), "timeOut", bean.getExist_time());
    }

    @Override
    public boolean canEnterMap(Player player, int model, int level) {
        return true;
    }

    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {
        cleanOldMapData(player, Manager.mapManager.getMap(player.getOld().getMapId()));
    }

    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {
        cleanOldMapData(player, map);
        if (map.getPlayers().isEmpty()) {
            //删除位面
            map.setStop(true);
        }
    }

    @Override
    public void onDamage(MapObject map, Monster monster, long damage, Fighter attacker) {

    }

    @Override
    public void onMonsterDie(MapObject mapObject, Monster monster, Fighter attacker) {
        for (Player p : mapObject.getPlayers().values()) {
            Manager.taskManager.deal().action(p, Task.ACTION_TYPE_PLANE, monster.getModelId(), 1);
        }
    }

    @Override
    public void onMonsterAfterDie(MapObject mapObject, Monster monster, Fighter attacker) {
        PlaneMapData mapData = MapParam.getPlaneMapData(mapObject);
        if (mapObject.getMonsters().size() <= 0) {
            //没有怪刷了
            if (mapData.isEnd()) {
                finish(mapObject, true);
            } else {
                int modelId = mapObject.getZoneModelId();
                if (modelId == 1027 || modelId == 19002) {
                    return;
                }

                int loop = mapData.getLoop();
                Cfg_Clone_monster_Bean bean = CfgManager.getCfg_Clone_monster_Container().getValueByKey(modelId * 1000 + loop);
                if (bean == null) {
                    logger.error("找不到怪物表 modeId = " + modelId + ", loop = " + loop);
                    return;
                }
                mapObject.addMapOnceScriptEventTimer(getId(), "refreshMonster", bean.getWaiting());
            }
        }
    }

    @Override
    public void onLeaveBattle(MapObject map, Monster monster, Player attacker) {

    }

    @Override
    public void onPlayerDie(MapObject mapObject, Fighter attacker, Player player) {
        Manager.copyMapManager.outZone(player);
    }

    @Override
    public void removeMap(MapObject mapObject) {

    }

    /**
     * 清理传送之前地图的数据
     *
     * @param player
     * @param map
     */
    private void cleanOldMapData(Player player, MapObject map) {
        try {
            //玩家、宠物删除
            MapMessage.ResPlayerDisappear.Builder playerMsg = MapMessage.ResPlayerDisappear.newBuilder();
            for (Player other : map.getPlayers().values()) {
                if (other.getId() == player.getId()) {
                    continue;
                }
                playerMsg.addPlayerIds(other.getId());
                Pet pet = Manager.petManager.getBattlePet(other);
                if (pet == null) {
                    continue;
                }
                MapMessage.ResPetDisappear.Builder petMsg = MapMessage.ResPetDisappear.newBuilder();
                petMsg.setId(pet.getId());
                MessageUtils.send_to_player(player, MapMessage.ResPetDisappear.MsgID.eMsgID_VALUE, petMsg.build().toByteArray());
            }
            MessageUtils.send_to_player(player, MapMessage.ResPlayerDisappear.MsgID.eMsgID_VALUE, playerMsg.build().toByteArray());

            //怪物删除
            for (Monster monster : map.getMonsters().values()) {
                MapMessage.ResMonsterDisappear.Builder monsterMsg = MapMessage.ResMonsterDisappear.newBuilder();
                monsterMsg.setMonsterId(monster.getId());
                MessageUtils.send_to_player(player, MapMessage.ResMonsterDisappear.MsgID.eMsgID_VALUE, monsterMsg.build().toByteArray());
            }

            //npc删除
            MapMessage.ResRoundNpcDisappear.Builder npcMsg = MapMessage.ResRoundNpcDisappear.newBuilder();
            for (Npc npc : map.getNpcs().values()) {
                npcMsg.addNpcIds(npc.getId());
            }
            MessageUtils.send_to_player(player, MapMessage.ResRoundNpcDisappear.MsgID.eMsgID_VALUE, npcMsg.build().toByteArray());

            //采集物删除
            for (Gather gather : map.getCollects().values()) {
                MapMessage.ResGatherDisappear.Builder gatherMsg = MapMessage.ResGatherDisappear.newBuilder();
                gatherMsg.setId(gather.getId());
                MessageUtils.send_to_player(player, MapMessage.ResGatherDisappear.MsgID.eMsgID_VALUE, gatherMsg.build().toByteArray());
            }

            //skillMagics删除
            for (SkillMagic magic : map.getMagics()) {
                MapMessage.ResMagicClean.Builder magicMsg = MapMessage.ResMagicClean.newBuilder();
                magicMsg.setId(magic.getId());
                MessageUtils.send_to_player(player, MapMessage.ResMagicClean.MsgID.eMsgID_VALUE, magicMsg.build().toByteArray());
            }

            //墓碑删除
            for (Tombstone tombstone : map.getTombstone().values()) {
                MapMessage.ResTombstoneClean.Builder stoneMsg = MapMessage.ResTombstoneClean.newBuilder();
                stoneMsg.setId(tombstone.getId());
                MessageUtils.send_to_player(player, MapMessage.ResTombstoneClean.MsgID.eMsgID_VALUE, stoneMsg.build().toByteArray());
            }

            //地图特效删除
            for (GroundBuff gb : map.getGroundBuffs().values()) {
                MapMessage.ResGroundBuffClean.Builder buffMsg = MapMessage.ResGroundBuffClean.newBuilder();
                buffMsg.setGbid(gb.getId());
                MessageUtils.send_to_player(player, MapMessage.ResGroundBuffClean.MsgID.eMsgID_VALUE, buffMsg.build().toByteArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 怪物死光的时候判断是否还有下一阶段
     *
     * @param mapObject
     */
    private void refreshMonster(MapObject mapObject) {
        if (mapObject.getZoneModelId() == 1027 || mapObject.getZoneModelId() == 19002) {
            //这个位面的怪物通过客户端通知来刷新
            return;
        }
        PlaneMapData mapData = MapParam.getPlaneMapData(mapObject);
        int modelId = mapObject.getZoneModelId();
        int loop = mapData.getLoop();

        Cfg_Clone_monster_Bean bean = CfgManager.getCfg_Clone_monster_Container().getValueByKey(modelId * 1000 + loop);
        if (bean == null) {
            logger.error("找不到怪物表 modeId = " + modelId + ", loop = " + loop);
            return;
        }

        //是不是最后一波怪了
        if (bean.getIf_end() == 1) {
            mapData.setEnd(true);
        }
        for (ReadArray<Integer> ll : bean.getMonster_information().getValuees()) {
            int x = ll.get(2);
            int y = ll.get(3);
            Position pos = new Position(x, y);
            for (int i = 0; i < ll.get(1); i++) {
                Manager.monsterManager.createMonster(mapObject, pos, ll.get(0));
            }
//            logger.error("位面刷怪，位面id = " + modelId +
//                    "，clone_monster = " + (modelId * 1000 + loop) + "，怪物id = " + ll.get(0) + "，数量 = " + ll.get(1) +
//                    "，位置（" + x + "，" + y + "）");
        }
        mapData.setLoop(++loop);
    }


    /**
     * 完成位面
     *
     * @param mapObject
     * @param isSuccess 成功还是失败
     */
    private void finish(MapObject mapObject, boolean isSuccess) {
        CopyMapMessage.ResCopyMapBitFinish.Builder builder = CopyMapMessage.ResCopyMapBitFinish.newBuilder();
        //奖励物品
        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(mapObject.getZoneModelId());
        if (bean == null) {
            logger.error("Cfg_Clone_mapBean无法找到数据，id = " + mapObject.getZoneModelId());
            return;
        }

        long  actionId = IDConfigUtil.getLogId();
        ReadIntegerArrayEs rewards = isSuccess? bean.getSuccess_reward(): bean.getFail_reward();
        //向位面所有玩家同步消息
        for (Player p : mapObject.getPlayers().values()) {
            List<Item> dropItems = Item.createItems(rewards, 1);
            //发送奖励
            if (!dropItems.isEmpty()) {
                if (!Manager.backpackManager.manager().addItems(p, dropItems, ItemChangeReason.PlaneRewardGet, actionId)) {
                    Manager.mailManager.sendMailToPlayer(p.getId(), MailType.SysCommonRewardMail, MessageString.System
                            , MessageString.System, MessageString.Task_Content, dropItems, ItemChangeReason.PlaneRewardGet, actionId);
                }
                Manager.activityManager.cloneDropTrigger(p, bean.getId());
            }
            //位面完成类任务完成
            if (isSuccess) {
                Manager.taskManager.deal().action(p, Task.ACTION_TYPE_PLANE, mapObject.getZoneModelId(), 1);
            }
            //记log
            MessageUtils.send_to_player(p, CopyMapMessage.ResCopyMapBitFinish.MsgID.eMsgID_VALUE, builder.build().toByteArray());
            writePlaneFinishLog(p, mapObject.getZoneModelId(), isSuccess);
            Manager.copyMapManager.logic().biInstance(p, mapObject.getZoneModelId(), 1, isSuccess ? 1 : 2, 0, true);
        }
    }

    /**
     * 位面完成记log
     *
     * @param player
     * @param zoneModelId
     * @param isSuccess
     */
    private void writePlaneFinishLog(Player player, int zoneModelId, boolean isSuccess) {
        CopyMapLogRecord log = new CopyMapLogRecord();
        log.setIsSuccess(isSuccess ? 1 : 2);
        log.setPlayerid(player.getId());
        log.setScore(0);
        log.setName(Manager.registerManager.getRoleName(player.getId()));
        log.setPlatform(player.getPlatformName());
        log.setType(0);
        log.setZonemodelid(zoneModelId);
        log.setCopyOverTime(0);
        log.setDcReward("");
        log.setReward("");
        log.setActionId(0);
        log.setSid(player.getCurServerId());
        log.setStar(0);
        log.setTeamId(0);
        LogService.getInstance().execute(log);
    }

}
