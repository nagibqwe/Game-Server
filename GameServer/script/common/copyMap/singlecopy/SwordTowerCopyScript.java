package common.copyMap.singlecopy;

import com.data.CfgManager;
import com.data.Global;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Sword_soul_copy_Bean;
import com.data.struct.ReadArray;
import com.game.backpack.structs.Item;
import com.game.copymap.structs.SwordSoulTowerData;
import com.game.manager.Manager;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapParam;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import game.core.map.Position;
import game.core.util.TimeUtils;
import game.message.CopyMapMessage;
import game.message.HuaxinFlySwordMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gsj
 * @create 2020/7/15 13:49
 */
public class SwordTowerCopyScript implements IMapBaseScript {


    private static final Logger logger = LogManager.getLogger(SwordSoulCopyScript.class);

    private static final int SwordSoulCloneId = 500001;

    @Override
    public int getId() {
        return ScriptEnum.SwordSoulTowerMapScript;
    }

    @Override
    public Object call(Object... objects) {
        Player player = (Player) objects[0];
        String method = (String) objects[1];
        switch (method) {
            case "goOnChallenge":
                goOnChallenge(player);
                break;
        }
        return null;
    }

    /**
     * 继续挑战
     */
    private void goOnChallenge(Player player) {
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (map.getMonsters().size() > 0) {
            return;
        }
        if (map.getZoneModelId() != SwordSoulCloneId) {
            return;
        }
        if (!canEnterMap(player, map.getZoneModelId(), 0)) {
            return;
        }
        int layer = player.getFlyswordAllInfo().getSwordSoulLayer();
        Cfg_Sword_soul_copy_Bean bean = CfgManager.getCfg_Sword_soul_copy_Container().getValueByKey(layer);
        if (bean == null) {
            logger.error("Cfg_Sword_soul_copy_Bean配置表不存在：" + layer);
            return;
        }
        Position pos = new Position(bean.getMoster_id_pos().get(1), bean.getMoster_id_pos().get(2));
        Manager.monsterManager.createMonster(map, pos, bean.getMoster_id_pos().get(0));

        SwordSoulTowerData mapData = MapParam.getSwordSoulTowerData(map);
        mapData.setLayer(bean.getNum());
        mapData.setEndTime((int) ((TimeUtils.Time() / 1000 + Global.Sword_Soul_Cross_Time)));
        map.setStop(false);

        player.setCurHp(player.getAttribute().MaxHP());
        player.onHpChange(player);
        sendCopyInfo(player, mapData.getEndTime(), true);
        map.addMapOnceScriptEventTimer(getId(), "timeoutEnd", Global.Sword_Soul_Cross_Time * 1000, player, mapData.getLayer());
    }

    @Override
    public void onCreate(MapObject mapObject, Object... objects) {

    }

    @Override
    public boolean canEnterMap(Player player, int model, int level) {
        int layer = player.getFlyswordAllInfo().getSwordSoulLayer();
        int maxNum = CfgManager.getCfg_Sword_soul_copy_Container().getValuees().length;
        if (layer >= maxNum) {
            logger.error("已挑战完所有关卡");
            return false;
        }
        return true;
    }

    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {
        SwordSoulTowerData mapData = MapParam.getSwordSoulTowerData(map);
        sendCopyInfo(player, mapData.getEndTime(), map.getMonsters().size() > 0);
    }

    /**
     * 副本信息
     *
     * @param endTime       结束时间戳
     * @param hasRefresh    是否已刷怪
     */
    private void sendCopyInfo(Player player, int endTime, boolean hasRefresh) {
        HuaxinFlySwordMessage.ResSoulCopyChallengeInfo.Builder builder = HuaxinFlySwordMessage.ResSoulCopyChallengeInfo.newBuilder();
        builder.setChallengeLevel(player.getFlyswordAllInfo().getSwordSoulLayer());
        builder.setEndTime(endTime);
        builder.setRefresh(hasRefresh);
        MessageUtils.send_to_player(player, HuaxinFlySwordMessage.ResSoulCopyChallengeInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {

    }

    @Override
    public void onDamage(MapObject mapObject, Monster monster, long damage, Fighter attacker) {

    }

    @Override
    public void onMonsterDie(MapObject map, Monster monster, Fighter attacker) {

    }

    @Override
    public void onMonsterAfterDie(MapObject map, Monster monster, Fighter attacker) {
        if (map.getMonsters().size() <= 0) {
            if (attacker instanceof Player) {
                finish(map, (Player) attacker, true, 1);
            }
        }
    }

    @Override
    public void onLeaveBattle(MapObject map, Monster monster, Player attacker) {

    }

    @Override
    public void onPlayerDie(MapObject map, Fighter attacker, Player player) {
        finish(map, player, false, 2);
    }

    @Override
    public void action(MapObject map, String method, Object[] params) {
        switch (method) {
            case "timeoutEnd":
                timeoutEnd(map, params);
                break;
        }
    }

    /**
     * 超时，挑战失败处理
     */
    private void timeoutEnd(MapObject mapObject, Object[] params) {
        Player player = (Player)params[0];
        int layer = (int) params[1];
        SwordSoulTowerData mapData = MapParam.getSwordSoulTowerData(mapObject);
        if (mapData.getLayer() != layer) {
            return;
        }
        if (!mapObject.getPlayers().containsKey(player.getId())) {
            return;
        }
        finish(mapObject, player, false, 3);
    }

    private void finish(MapObject mapObject, Player player, boolean isWin, int type) {
        if (player == null) {
            return;
        }
        if (mapObject.isStop()) {
            return;
        }
        mapObject.setStop(true);

        SwordSoulTowerData mapData = MapParam.getSwordSoulTowerData(mapObject);
        Manager.copyMapManager.logic().biInstance(player, mapObject.getZoneModelId(), 1, type, mapData.getLayer(), false);

        List<Item> itemList = new ArrayList<>();
        if (isWin) {
            if (mapData.getLayer() > player.getFlyswordAllInfo().getSwordSoulLayer() + 1) {
                logger.error("层数异常！");
                return;
            }
            Cfg_Sword_soul_copy_Bean bean = CfgManager.getCfg_Sword_soul_copy_Container().getValueByKey(mapData.getLayer());
            if (bean == null) {
                logger.error("Cfg_Sword_soul_copy_Bean配置表不存在:" + mapData.getLayer());
                return;
            }
            Manager.huaxinFlySwordManager.swordSoulScript().finishChallengeTower(player, mapData.getLayer());
            itemList = Item.createItems(bean.getReward());
            itemList.addAll(getLayerReward(mapData.getLayer()));
            if (!Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.CopyMapGet, mapObject.getZoneModelId())) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System, MessageString.System, MessageString.BAG_FULL_MAIL_CONTENT, itemList, ItemChangeReason.CopyMapGet);
            }
        }

        HuaxinFlySwordMessage.ResSoulCopyResult.Builder builder = HuaxinFlySwordMessage.ResSoulCopyResult.newBuilder();
        builder.setSuccess(isWin);
        for (Item item : itemList) {
            CopyMapMessage.cardItemInfo.Builder itemInfo = CopyMapMessage.cardItemInfo.newBuilder();
            itemInfo.setItemId(item.getItemModelId());
            itemInfo.setNum(item.getNum());
            itemInfo.setBind(true);
            builder.addItemList(itemInfo);
        }
        MessageUtils.send_to_player(player, HuaxinFlySwordMessage.ResSoulCopyResult.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void removeMap(MapObject map) {

    }
    private List<Item> getLayerReward(int layer) {
        List<Item> immortalRewardList = new ArrayList<>();
        Cfg_Sword_soul_copy_Bean bean = CfgManager.getCfg_Sword_soul_copy_Container().getValueByKey(layer);
        if (bean == null) {
            logger.error("发送奖励，Cfg_Sword_soul_copy_Bean配置表存在：" + layer);
            return immortalRewardList;
        }
        for (int i = 0; i < bean.getImmortal_soul_reward().size(); i++) {
            List<Integer> subTypeList = new ArrayList<>();
            ReadArray<Integer> exArray = bean.getImmortal_soul_reward_exclusive().get(i);
            for (int j = 0; j < exArray.size(); j++) {
                int type = exArray.get(j);
                if (type <= bean.getReward_type()) {
                    subTypeList.add(type);
                }
            }
            ReadArray<Integer> array = bean.getImmortal_soul_reward().get(i);
            int num = array.get(0);
            int type = array.get(1);
            int quality = array.get(2);
            int subType = 0;
            for (int j = 0; j < num; j++) {
                if (type != 2) {
                    int random = RandomUtils.random(subTypeList.size());
                    subType = subTypeList.get(random);
                }
                int immortalCfgId = buildImmortalId(type, quality, subType);
                immortalRewardList.add(Item.createItem(immortalCfgId, 1, false));
            }
        }
        return immortalRewardList;
    }

    private int buildImmortalId(int type, int quality, int subType) {
        return 6000000 + type * 100000 + quality * 1000 + subType;
    }
}

