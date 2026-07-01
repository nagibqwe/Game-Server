package common.copyMap.singlecopy;

import com.data.CfgManager;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_FlySword_Grave_Bean;
import com.game.backpack.structs.Item;
import com.game.cooldown.structs.CooldownTypes;
import com.game.copymap.structs.CopyBaseData;
import com.game.huaxinflysword.structs.FlyswordAllInfo;
import com.game.manager.Manager;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapParam;
import com.game.monster.structs.Monster;
import com.game.nature.structs.HuaxinEntity;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.skill.structs.Skill;
import com.game.structs.Fighter;
import com.game.utils.MessageUtils;
import game.core.util.TimeUtils;
import game.message.CopyMapMessage;
import game.message.HuaxinFlySwordMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * @author gsj
 * @create 2020/7/15 13:50
 */
public class SwordSoulCopyScript implements IMapBaseScript {

    private static final Logger logger = LogManager.getLogger(SwordSoulCopyScript.class);

    @Override
    public int getId() {
        return ScriptEnum.SwordSoulCopyMapScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void onCreate(MapObject mapObject, Object... objects) {
    }

    @Override
    public boolean canEnterMap(Player player, int model, int level) {
        FlyswordAllInfo info = player.getFlyswordAllInfo();
        Cfg_FlySword_Grave_Bean bean = null;
        for (Cfg_FlySword_Grave_Bean temp : CfgManager.getCfg_FlySword_Grave_Container().getValuees()) {
            if (temp.getClone_id() == model) {
                bean = temp;
                break;
            }
        }
        if (bean == null) {
            logger.info("未找到剑冢副本对应的配置表：" + model);
            return false;
        }
        int state = info.getSwaordTombInfo().getOrDefault(bean.getId(), 0);
        if (state >= 1) {
            logger.info("该剑冢已完成或已觉醒");
            return false;
        }
        return Manager.controlManager.deal().checkFuncProgress(player, bean.getCondition());
    }

    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {
        CopyBaseData data = MapParam.getSwordSoulCopyData(map);
        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(map.getZoneModelId());
        if (bean == null) {
            return;
        }
        if (data.getId() == 0) {
            Cfg_FlySword_Grave_Bean flySwordGraveBean = null;
            for (Cfg_FlySword_Grave_Bean temp : CfgManager.getCfg_FlySword_Grave_Container().getValuees()) {
                if (temp.getClone_id() == map.getZoneModelId()) {
                    flySwordGraveBean = temp;
                    break;
                }
            }
            if (flySwordGraveBean == null) {
                return;
            }
            data.setId(flySwordGraveBean.getId());
            data.setStartTime((int) (TimeUtils.Time() / 1000 + 3));
            data.setEndTime(data.getStartTime() + bean.getExist_time() / 1000);
            map.addMapOnceScriptEventTimer(getId(), "refreshMonster", 3000);
            map.addMapOnceScriptEventTimer(getId(), "timeoutEnd", bean.getExist_time() + 3000);
        }
        int now = (int) (TimeUtils.Time() / 1000);
        HuaxinFlySwordMessage.ResSwordTombCopyInfo.Builder builder = HuaxinFlySwordMessage.ResSwordTombCopyInfo.newBuilder();
        builder.setId(data.getId());
        builder.setStartTime(Math.max(data.getStartTime() - now, 0));
        builder.setEndTime(data.getEndTime() - now - 3);
        MessageUtils.send_to_player(player, HuaxinFlySwordMessage.ResSwordTombCopyInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        HuaxinEntity entity = player.getCurHuaxinEntity();
        if (entity == null) {
            return;
        }
        for (Skill skill : entity.getBaseSkills().values()) {
            Manager.cooldownManager.addCooldown(player, CooldownTypes.Player_FlySowrd_CD_Skill, String.valueOf(skill.getSkillId()), 0);
        }
    }

    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {
        map.setStop(true);
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
            case "refreshMonster":
                refreshMonster(map);
                break;
            case "timeoutEnd":
                timeoutEnd(map);
                break;
        }
    }

    private void refreshMonster(MapObject mapObject) {
        Manager.copyMapManager.manager().copyMapRefreshMonster(mapObject, mapObject.getZoneModelId(), 1);
    }

    private void timeoutEnd(MapObject map) {
        for (Player player : map.getPlayers().values()) {
            finish(map, player, false, 3);
        }
    }

    private void finish(MapObject map, Player player, boolean isWin, int type) {
        if (map.isStop()) {
            return;
        }
        map.setStop(true);

        List<Item> itemList = new ArrayList<>();
        if (isWin) {
            Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(map.getZoneModelId());
            if (bean == null) {
                return;
            }
            itemList = Item.createItems(bean.getSuccess_reward());
            if (!Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.CopyMapGet, map.getZoneModelId())) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System, MessageString.System, MessageString.BAG_FULL_MAIL_CONTENT, itemList, ItemChangeReason.CopyMapGet);
            }
            Manager.activityManager.cloneDropTrigger(player, bean.getId());

            FlyswordAllInfo info = player.getFlyswordAllInfo();
            CopyBaseData data = MapParam.getSwordSoulCopyData(map);
            Integer state = info.getSwaordTombInfo().getOrDefault(data.getId(), 0);
            if (state != 0) {
                logger.error("剑冢副本完成状态错误");
            }
            info.getSwaordTombInfo().put(data.getId(), 1);

            HuaxinFlySwordMessage.ResSwordTombChange.Builder builder = HuaxinFlySwordMessage.ResSwordTombChange.newBuilder();
            builder.setId(data.getId());
            builder.setState(1);
            MessageUtils.send_to_player(player, HuaxinFlySwordMessage.ResSwordTombChange.MsgID.eMsgID_VALUE, builder.build().toByteArray());


            Cfg_FlySword_Grave_Bean grave_bean = CfgManager.getCfg_FlySword_Grave_Container().getValueByKey(data.getId());
            if (grave_bean == null){
                logger.error("Cfg_FlySword_Grave_Bean is null");
                return;
            }
            //TODO  剑冢激活全部通过副本来激活 策划强行要求
            //if (grave_bean.getType() != 1){
            //    Manager.huaxinFlySwordManager.swordSoulScript().onReqSwordTombWakeUp(player, data.getId());
            //}
            Manager.huaxinFlySwordManager.swordSoulScript().onReqSwordTombWakeUp(player, data.getId());
        }

        Manager.copyMapManager.logic().biInstance(player, map.getZoneModelId(), 1, type, 0, false);
        HuaxinFlySwordMessage.ResSwordTombResult.Builder builder = HuaxinFlySwordMessage.ResSwordTombResult.newBuilder();
        builder.setSuccess(isWin);
        for (Item item : itemList) {
            CopyMapMessage.cardItemInfo.Builder itemInfo = CopyMapMessage.cardItemInfo.newBuilder();
            itemInfo.setItemId(item.getItemModelId());
            itemInfo.setNum(item.getNum());
            itemInfo.setBind(true);
            builder.addItemList(itemInfo);
        }
        MessageUtils.send_to_player(player, HuaxinFlySwordMessage.ResSwordTombResult.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void removeMap(MapObject map) {

    }
}