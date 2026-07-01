package common.statestifle;

import com.data.CfgManager;
import com.data.FunctionVariable;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Huaxingfabao_Bean;
import com.data.bean.Cfg_State_stifle_Bean;
import com.data.bean.Cfg_State_stifle_add_Bean;
import com.data.bean.Cfg_State_stifle_add_level_Bean;
import com.data.container.Cfg_Huaxingfabao_Container;
import com.data.container.Cfg_State_stifle_Container;
import com.data.struct.ReadIntegerArray;
import com.game.backpack.structs.Item;
import com.game.bi.manager.BIDefine;
import com.game.chat.structs.Notify;
import com.game.log.db.RoleGrowLog;
import com.game.log.grow.GrowType;
import com.game.manager.Manager;
import com.game.nature.structs.Huaxin;
import com.game.nature.structs.Nature;
import com.game.nature.structs.NatureType;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.skill.structs.Skill;
import com.game.statestifle.script.IStateStifleScript;
import com.game.statestifle.structs.PlayerSateStifleData;
import com.game.statestifle.structs.SoulSpiritInfo;
import com.game.structs.ServerStr;
import com.game.task.structs.TaskHelp;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.utils.Utils;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;
import game.message.MapMessage;
import game.message.StateStifleMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 瞿冰冰
 * 2019/9/4
 */
public class StateStifleScript implements IStateStifleScript, IScript {

    private static final Logger logger = LogManager.getLogger(StateStifleScript.class);

    private static final int soulSpiritType1 = 1;       //经验器灵
    private static final int soulSpiritType2 = 2;       //战斗器灵
    private static final int soulSpiritType3 = 3;       //追击器灵

    @Override
    public int getId() {
        return ScriptEnum.StateStifleBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public int getConfigIdByLevelAndStar(int level, int star) {
        return level * 100 + star;
    }

    @Override
    public void online(Player player) {
        sendPlayerStifleDataInfo(player);
    }

    @Override
    public void openPanel(Player player) {
        sendPlayerStifleDataInfo(player);
    }

    private void sendPlayerStifleDataInfo(Player player) {
        StateStifleMessage.ResStateStifleBase.Builder message = StateStifleMessage.ResStateStifleBase.newBuilder();
        PlayerSateStifleData stifleData = player.getStifleData();
        int cofigId = getConfigIdByLevelAndStar(stifleData.getLevel(), stifleData.getStar());
        Cfg_State_stifle_Bean bean = Cfg_State_stifle_Container.GetInstance().getValueByKey(cofigId);
        if (bean == null) {
            logger.error(String.format("发送玩家的境界灵压数据失败!!!id{%s}在表Cfg_State_stifle_Bean中不存在！！！", cofigId));
            return;
        }
        boolean reach = true;
        ReadIntegerArray active_tasks = bean.getCondition();
        if (active_tasks != null && active_tasks.size() != 0) {
            StateStifleMessage.LevelCondition.Builder condition = StateStifleMessage.LevelCondition.newBuilder();
            reach = Manager.controlManager.deal().checkFuncProgress(player, active_tasks);
            int progress = Manager.controlManager.deal().getFuncProgress(player, active_tasks);
            condition.setProgress(progress);
            int total = active_tasks.getValue()[1];
            condition.setTotal(total);
            int conditionId = active_tasks.getValue()[0];
            condition.setConditionId(conditionId);
            message.addConditionValue(condition);
        }
        message.setConditionReach(reach);
        StateStifleMessage.StateStifleLevel.Builder level = StateStifleMessage.StateStifleLevel.newBuilder();
        level.setLevel(stifleData.getLevel());
        level.setStar(stifleData.getStar());
        message.setLevel(level);

        Map<Integer, SoulSpiritInfo> spiritMap = player.getStifleData().getSpiritMap();
        for (Cfg_State_stifle_add_Bean tempBean : CfgManager.getCfg_State_stifle_add_Container().getValuees()) {
            if (tempBean.getId() % 100 != 0) {
                continue;
            }
            SoulSpiritInfo info = spiritMap.get(tempBean.getType());
            StateStifleMessage.soulSpirit.Builder spiritInfo = StateStifleMessage.soulSpirit.newBuilder();
            spiritInfo.setId(tempBean.getType());
            if (info == null) {
                spiritInfo.setEvolveLv(0);
                spiritInfo.setPromoteLv(0);
                spiritInfo.setPromotePorgress(0);
                boolean canActive = stifleData.getLevel() >= tempBean.getNeed_level();
                spiritInfo.setState(canActive ? 1 : 0);
            } else {
                spiritInfo.setEvolveLv(info.getEvolveLv());
                spiritInfo.setPromoteLv(info.getPromoteLv());
                spiritInfo.setPromotePorgress(info.getPromoteProgress());
                spiritInfo.setState(2);
            }
            message.addSoulSpiritList(spiritInfo);
        }
        MessageUtils.send_to_player(player, StateStifleMessage.ResStateStifleBase.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    @Override
    public void levelUp(Player player, boolean oneKey) {
        PlayerSateStifleData stifleData = player.getStifleData();
        int configId = getConfigIdByLevelAndStar(stifleData.getLevel(), stifleData.getStar());

        Cfg_State_stifle_Bean bean = null;
        boolean reach = true;

        int cfgId= configId;
        while (cfgId != 0) {
            bean = Cfg_State_stifle_Container.GetInstance().getValueByKey(cfgId);
            if (cfgId != configId) {
                stifleData.setLevel(cfgId / 100);
                stifleData.setStar(cfgId % 100);
                if (bean.getModelID() != 0) {
                    Manager.natureManager.deal().onReqNatureModelSet(player, NatureType.STIFLEFFABAO, bean.getModelID());
                }
                if (!oneKey) {
                    break;
                }
            }
            ReadIntegerArray active_tasks = bean.getCondition();
            if (active_tasks != null && active_tasks.size() != 0) {
                reach = Manager.controlManager.deal().checkFuncProgress(player, active_tasks);
                if (!reach) {
                    logger.info(String.format("玩家{%s}升级灵压失败！！玩家条件未达成{%s}", TaskHelp.getPlayerInfo(player), active_tasks));
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.UpLevelConditionLess);
                    break;
                }
            }
            if (bean.getNeed_item().size() >=2 && !Manager.backpackManager.manager().onRemoveItem(player,
                    bean.getNeed_item().get(0), bean.getNeed_item().get(1), ItemChangeReason.UpLevelStateStifleDec, IDConfigUtil.getId())) {
                if (!oneKey || cfgId == configId) {
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.LIANQI_GEM_NEEDITEMNOTENOUGH);
                    Manager.backpackManager.manager().sendItemNotEnough(player, bean.getNeed_item().get(0));
                }
                break;
            }
            cfgId = getNextCfgId(cfgId);
        }
        int nowCfgId = getConfigIdByLevelAndStar(stifleData.getLevel(), stifleData.getStar());
        if (nowCfgId == configId) {
            return;
        }
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.StifleFabao);

        StateStifleMessage.ResUpLevel.Builder builder = StateStifleMessage.ResUpLevel.newBuilder();
        builder.setOneKey(oneKey);
        StateStifleMessage.StateStifleLevel.Builder levelInfo = StateStifleMessage.StateStifleLevel.newBuilder();
        levelInfo.setLevel(stifleData.getLevel());
        levelInfo.setStar(stifleData.getStar());
        builder.setLevel(levelInfo);
        builder.setConditionReach(reach);
        builder.setFight(player.getStifleData().getNature().getPower());

        if (bean != null && bean.getCondition().size() != 0) {
            StateStifleMessage.LevelCondition.Builder condition = StateStifleMessage.LevelCondition.newBuilder();
            int progress = Manager.controlManager.deal().getFuncProgress(player, bean.getCondition());
            condition.setProgress(progress);
            condition.setConditionId(bean.getCondition().getValue()[0]);
            condition.setTotal(bean.getCondition().getValue()[1]);
            builder.addConditionValue(condition);
        }
        MessageUtils.send_to_player(player, StateStifleMessage.ResUpLevel.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        Manager.controlManager.operate(player, FunctionVariable.MagicWeapon, 0);
        Manager.controlManager.operate(player, FunctionVariable.MagicWeaponRank, 0);
        //成长BI
        Manager.biManager.getScript().biGrow(player, GrowType.stifle_level_up.getType(), 0, BIDefine.GrowLevelUp, configId, cfgId, cfgId);
    }

    /**
     * 检查该升星还是升级 或者都不能再升（1 表示升星 2表示升级 0表示 最大星级了无法再升了）
     */
    private int checkIsLastLevel(Player player) {
        PlayerSateStifleData stifleData = player.getStifleData();
        //假设是升星
        int upStar = stifleData.getStar() + 1;
        int upStarConfigId = getConfigIdByLevelAndStar(stifleData.getLevel(), upStar);
        Cfg_State_stifle_Bean upStarBean = Cfg_State_stifle_Container.GetInstance().getValueByKey(upStarConfigId);
        if (upStarBean != null) {
            return 1;
        }
        //假设升级
        int upLevel = stifleData.getLevel() + 1;
        int upLevelConfigId = getConfigIdByLevelAndStar(upLevel, 0);
        Cfg_State_stifle_Bean upLevelBean = Cfg_State_stifle_Container.GetInstance().getValueByKey(upLevelConfigId);
        if (upLevelBean != null) {
            return 2;
        }
        return 0;
    }

    private int getNextCfgId(int configId) {
        Cfg_State_stifle_Bean nextBean = Cfg_State_stifle_Container.GetInstance().getValueByKey(configId + 1);
        if (nextBean == null) {
            configId = (configId / 100 + 1) * 100;
            nextBean = Cfg_State_stifle_Container.GetInstance().getValueByKey(configId);
            if (nextBean != null) {
                return configId;
            }
            return 0;
        }
        return configId + 1;
    }

    @Override
    public void conditionUpdate(Player player, int type) {
        PlayerSateStifleData stifleData = player.getStifleData();
        int cofigId = getConfigIdByLevelAndStar(stifleData.getLevel(), stifleData.getStar());
        Cfg_State_stifle_Bean bean = Cfg_State_stifle_Container.GetInstance().getValueByKey(cofigId);
        if (bean == null) {
            logger.error(String.format("发送玩家的境界灵压数据失败!!!id{%s}在表Cfg_State_stifle_Bean中不存在！！！", cofigId));
            return;
        }
        //任务有更新
        ReadIntegerArray active_tasks = bean.getCondition();
        if (active_tasks != null && active_tasks.size() != 0) {
            boolean reach = Manager.controlManager.deal().checkFuncProgress(player, active_tasks);
            if (reach) {
                sendPlayerStifleDataInfo(player);
            }
        }
    }

    @Override
    public boolean addSkillToPlayer(Player player, int oldModelId, int newModelId) {
        Nature data = player.getStifleData().getNature();
        Cfg_Huaxingfabao_Bean oldBean = Cfg_Huaxingfabao_Container.GetInstance().getValueByKey(oldModelId);
        if (oldBean != null) {
            Manager.skillManager.removeSkill(player, oldBean.getUse_skill());
            data.getSkills().clear();
        }
        Cfg_Huaxingfabao_Bean newBean = Cfg_Huaxingfabao_Container.GetInstance().getValueByKey(newModelId);
        if (newBean != null) {
            Manager.skillManager.deal().addSkill(player, newBean.getUse_skill());
            Skill skill = new Skill();
            skill.setSkillId(newBean.getUse_skill());
            data.getSkills().put(newBean.getUse_skill(), skill);
        }
        return true;
    }

    @Override
    public int getAttValue(Player player) {
        int level = player.getStifleData().getLevel();
        int star = player.getStifleData().getStar();
        int cofigId = getConfigIdByLevelAndStar(level, star);
        Cfg_State_stifle_Bean bean = Cfg_State_stifle_Container.GetInstance().getValueByKey(cofigId);
        if (bean == null) {
            logger.error(String.format("发送玩家的境界灵压数据失败!!!id{%s}在表Cfg_State_stifle_Bean中不存在！！！", cofigId));
            return -1;
        }

        ConcurrentHashMap<Integer, Huaxin> huaxins = player.getStifleData().getNature().getHuaxins();
        int huaxinDamage = 0;
        for (Huaxin huaxin : huaxins.values()) {
            int excelId = huaxin.getExcelId();
            int huaxinLevel = huaxin.getLevel();
            Cfg_Huaxingfabao_Bean huaxinBean = Cfg_Huaxingfabao_Container.GetInstance().getValueByKey(excelId);
            if (huaxinBean == null) {
                logger.info(String.format("玩家身上的灵压法宝化形模板{%s}在表中_Huaxingfabao不存在!!!", excelId));
                continue;
            }
            if (huaxinBean.getFabaohit()==null ||huaxinBean.getFabaohit().size() <=0){
                continue;
            }
            Integer base = huaxinBean.getFabaohit().getValue()[0];
            Integer starAdd = huaxinBean.getFabaohit().getValue()[1];
            int add = base + starAdd * huaxinLevel;
            huaxinDamage += add;
        }
        return bean.getEx_damage() + huaxinDamage;
    }

    @Override
    public void onReqActiveSoulSpirit(Player player, int id) {
        PlayerSateStifleData stifleData = player.getStifleData();
        if (stifleData.getSpiritMap().containsKey(id)) {
            return;
        }
        Cfg_State_stifle_add_Bean bean = CfgManager.getCfg_State_stifle_add_Container().getValueByKey(id * 100);
        if (bean == null) {
            return;
        }
        if (stifleData.getLevel() < bean.getNeed_level()) {
            return;
        }
        if (!Manager.backpackManager.manager().onRemoveItem(player, bean.getNeed_item().get(0), bean.getNeed_item().get(1), ItemChangeReason.SoulSpiritDec, IDConfigUtil.getLogId())) {
            return;
        }
        if (bean.getSkill().size() > 0) {
            for (int i = 0; i < bean.getSkill().size(); i++) {
                Manager.skillManager.addSkill(player, bean.getSkill().get(i));
            }
        }
        SoulSpiritInfo info = new SoulSpiritInfo(id);
        stifleData.getSpiritMap().put(id, info);
        if (info.getId() == soulSpiritType3) {
            info.setPromoteProgress(player.getStifleData().getNature().getHuaxins().size());
        }
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.StifleFabao);

        sendSoulSpiritInfo(player, info, 0, true);
        boradFabaoInfo(player);
        Manager.controlManager.operate(player, FunctionVariable.StatestifleAddActivated, 1);
        Manager.biManager.getScript().biGrow(player,GrowType.stifle_spirit_active.getType(), 0, BIDefine.GrowActive,0,id * 100,id * 100);
    }

    @Override
    public void onReqUpPromoteLevel(Player player, int id) {
        PlayerSateStifleData stifleData = player.getStifleData();
        SoulSpiritInfo info = stifleData.getSpiritMap().get(id);
        if (info == null) {
            return;
        }
        int configId = id * 100 + info.getPromoteLv();
        Cfg_State_stifle_add_level_Bean bean = CfgManager.getCfg_State_stifle_add_level_Container().getValueByKey(configId);
        if (bean == null) {
            return;
        }
        if (bean.getIf_max() == 1) {
            return;
        }
        if (stifleData.getLevel() < bean.getNeed_level()) {
            return;
        }
        boolean reach = false;
        switch (id) {
            //获得经验X点
            case soulSpiritType1:
            //激活法宝化形X个
            case soulSpiritType3:
                reach = info.getPromoteProgress() >= bean.getNeed_item().get(0);
                break;
            //击杀x级以上bossX只
            case soulSpiritType2:
                reach = info.getPromoteProgress() >= bean.getNeed_item().get(1);
                break;
        }
        if (!reach) {
            return;
        }
        //替换技能
        for (int i = 0; i < bean.getSkill().size(); i++) {
            Manager.skillManager.removeSkill(player, bean.getSkill().get(i));
        }
        int old = info.getPromoteLv();
        info.setPromoteLv(info.getPromoteLv() + 1);
        if (id != soulSpiritType3) {
            info.setPromoteProgress(0);
        }
        stifleData.getSpiritMap().put(info.getId(), info);
        bean = CfgManager.getCfg_State_stifle_add_level_Container().getValueByKey(id * 100 + info.getPromoteLv());
        if (bean != null) {
            for (int i = 0; i < bean.getSkill().size(); i++) {
                Manager.skillManager.addSkill(player, bean.getSkill().get(i));
            }
        }
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.StifleFabao);
        sendSoulSpiritInfo(player, info, 1, true);
        Manager.biManager.getScript().biGrow(player,15, 0, BIDefine.GrowLevelUp, old, info.getPromoteLv(), configId);
    }

    @Override
    public void onReqUpEvolveLevel(Player player, int id) {
        PlayerSateStifleData stifleData = player.getStifleData();
        SoulSpiritInfo info = stifleData.getSpiritMap().get(id);
        if (info == null) {
            return;
        }
        int configId = id * 100 + info.getEvolveLv();
        Cfg_State_stifle_add_Bean bean = CfgManager.getCfg_State_stifle_add_Container().getValueByKey(configId);
        if (bean == null) {
            return;
        }
        if (bean.getIf_max() == 1) {
            return;
        }
        if (stifleData.getLevel() < bean.getNeed_level()) {
            return;
        }
        if (bean.getJinhua_need_money() != null && bean.getJinhua_need_money().size() >= 2) {
            if (!Manager.currencyManager.manager().canDecItemCoin(player, bean.getJinhua_need_money().get(1), bean.getJinhua_need_money().get(0))) {
                return;
            }
        }
        long actionId = IDConfigUtil.getLogId();
        if (!Manager.backpackManager.manager().removeItemOrCurrencies(player, bean.getJinhua_need_item(), actionId, ItemChangeReason.SoulSpiritDec)) {
            return;
        }
        if (bean.getJinhua_need_money() != null && bean.getJinhua_need_money().size() >= 2) {
            Manager.currencyManager.manager().onDecItemCoin(player, bean.getJinhua_need_money().get(1), ItemChangeReason.SoulSpiritDec, actionId, bean.getJinhua_need_money().get(0));
        }
        if (RandomUtils.random(10000) > bean.getJinghua_succes()) {
            sendSoulSpiritInfo(player, info, 2, false);
            return;
        }
        //替换技能
        for (int i = 0; i < bean.getSkill().size(); i++) {
            Manager.skillManager.removeSkill(player, bean.getSkill().get(i));
        }
        int old = info.getEvolveLv();
        info.setEvolveLv(info.getEvolveLv() + 1);

        Cfg_State_stifle_add_Bean oldBean = bean;

        bean = CfgManager.getCfg_State_stifle_add_Container().getValueByKey(id * 100 + info.getEvolveLv());
        if (bean != null) {
            for (int i = 0; i < bean.getSkill().size(); i++) {
                Manager.skillManager.addSkill(player, bean.getSkill().get(i));
            }
        }
        Manager.controlManager.operate(player, FunctionVariable.StateStifleAddTotal, 0);
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.StifleFabao);
        sendSoulSpiritInfo(player, info, 2, true);
        boradFabaoInfo(player);
        Manager.biManager.getScript().biGrow(player,GrowType.stifle_spirit_evolution.getType(), 0, BIDefine.GrowEvolution, old, info.getEvolveLv(), configId);

        //发送公告
        if (oldBean != null && oldBean.getNotice() != 0 || oldBean.getChatchannel() != null) {
            int itemid = oldBean.getJinhua_need_item().get(1).get(0);
            MessageUtils.notify_allOnlinePlayer(oldBean.getNotice() , oldBean.getChatchannel(), MessageString.STATE_STIFLE_ADD_NOTICE1,
                    player.getId()+"", player.getName(), Manager.backpackManager.manager().getChatInfo(Item.createItem(itemid, 1, false)),
                    Utils.makeUrlStr(MessageString.STATE_STIFLE_ADD_NOTICE1));
        }
    }

    @Override
    public void boradFabaoInfo(Player player) {
        MapMessage.ResFabaoInfoBroadCast.Builder builder = MapMessage.ResFabaoInfoBroadCast.newBuilder();
        builder.setPlayerId(player.getId());
        builder.setCfgId(player.getStifleData().getNature().getCurrentModelId());
        builder.setId(player.getStifleData().getNature().getId());
        builder.setSoulSpirte1(0);
        builder.setSoulSpirte2(0);
        builder.setSoulSpirte3(0);
        Map<Integer, SoulSpiritInfo> spiritMap = player.getStifleData().getSpiritMap();
        if (spiritMap.containsKey(1)) {
            builder.setSoulSpirte1(100 + spiritMap.get(1).getEvolveLv());
        }
        if (spiritMap.containsKey(2)) {
            builder.setSoulSpirte2(200 + spiritMap.get(2).getEvolveLv());
        }
        if (spiritMap.containsKey(3)) {
            builder.setSoulSpirte3(300 + spiritMap.get(3).getEvolveLv());
        }
        MessageUtils.send_to_roundPlayer(player, MapMessage.ResFabaoInfoBroadCast.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void addSoulSpiritProgress(Player player, int type, long num, int param) {
        if (num <= 0) {
            return;
        }
        SoulSpiritInfo info = player.getStifleData().getSpiritMap().get(type);
        if (info == null) {
            return;
        }
        int configId = info.getId() * 100 + info.getPromoteLv();
        Cfg_State_stifle_add_level_Bean bean = CfgManager.getCfg_State_stifle_add_level_Container().getValueByKey(configId);
        if (bean.getIf_max() == 1) {
            return;
        }
        switch (type) {
            case soulSpiritType1:
                info.setPromoteProgress(info.getPromoteProgress() + num);
                break;
            case soulSpiritType3:
                info.setPromoteProgress(player.getStifleData().getNature().getHuaxins().size());
                break;
            case soulSpiritType2:
                if (param >= bean.getNeed_item().get(0)) {
                    info.setPromoteProgress(info.getPromoteProgress() + num);
                }
                break;
        }
    }

    /**
     * 器灵返回消息
     *
     * @param info      器灵信息
     * @param type      0:激活，1晋升，2进化
     * @param success   是否成功
     */
    private void sendSoulSpiritInfo(Player player, SoulSpiritInfo info, int type, boolean success) {
        StateStifleMessage.ResSoulSpiritInfo.Builder builder = StateStifleMessage.ResSoulSpiritInfo.newBuilder();
        StateStifleMessage.soulSpirit.Builder spiritInfo = StateStifleMessage.soulSpirit.newBuilder();
        spiritInfo.setId(info.getId());
        spiritInfo.setState(2);
        spiritInfo.setPromoteLv(info.getPromoteLv());
        spiritInfo.setEvolveLv(info.getEvolveLv());
        spiritInfo.setPromotePorgress(info.getPromoteProgress());
        builder.setSoulSpiritList(spiritInfo);
        builder.setType(type);
        builder.setIsSuc(success);
        builder.setFight(player.getStifleData().getNature().getPower());
        MessageUtils.send_to_player(player, StateStifleMessage.ResSoulSpiritInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }
}
