package common.marriage;

import com.data.*;
import com.data.bean.*;
import com.data.struct.ReadArray;
import com.game.backpack.structs.Item;
import com.game.bi.manager.BIDefine;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.structs.Notify;
import com.game.cooldown.structs.CooldownTypes;
import com.game.count.structs.*;
import com.game.friend.struct.Friend;
import com.game.friend.struct.PlayerRelation;
import com.game.friend.struct.Relation;
import com.game.guild.structs.Guild;
import com.game.log.grow.GrowType;
import com.game.mail.manager.MailManager;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.marriage.command.WeddingBulletScreenCommand;
import com.game.marriage.command.WeddingBuyHotCommand;
import com.game.marriage.command.WeddingGiftCommand;
import com.game.marriage.command.WeddingUseGiftCommand;
import com.game.marriage.log.MarryBoxLog;
import com.game.marriage.log.MarryChildLog;
import com.game.marriage.log.MarryLog;
import com.game.marriage.log.MarryLoveLockLog;
import com.game.marriage.script.IMarriageScript;
import com.game.marriage.script.IMarryWallScript;
import com.game.marriage.struct.*;
import com.game.newfashion.manager.NewFashionManager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.player.structs.PlayerWorldInfo;
import com.game.player.structs.SavePlayerLevel;
import com.game.script.structs.ScriptEnum;
import com.game.skill.structs.Skill;
import com.game.server.DbSqlName;
import com.game.server.thread.SaveServer;
import com.game.structs.ServerStr;
import com.game.utils.*;
import game.core.dblog.LogService;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.MapMessage;
import game.message.MarriageMessage;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description
 * @auther admin
 * @create 2020-06-02 16:13
 */
public class MarriageScript implements IMarriageScript, IMarryWallScript {

    final int WEDDING_COPY_MAP_ID = 3001;//婚礼副本地图ID
    final long DeclarationTimeout = 12 * 3600 * 1000L;  //爱情宣言过期时间

    final int PreWeddingState_Suc = 0;   //成功
    final int PreWeddingState_Limit = 1; //已经预约婚宴
    final int PreWeddingState_UnNull = 2; //已经被别人预约
    final int PreWeddingState_TimeOut = 3;  //婚宴已过期

    private static final int marryType_general = 1;//普通婚姻
    private static final int marryType_higher = 2;//高级婚姻
    private static final int marryType_luxury = 3;//豪华婚姻

    private static final Logger log = LogManager.getLogger(MarriageScript.class);

    @Override
    public int getId() {
        return ScriptEnum.MarriageScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }


    /**
     * 获取婚宴场景
     *
     * @return
     */
    @Override
    public MapObject getWeddingScene() {

        WeddingMapInfo wedding = Manager.marriageManager.getWedding();
        if (wedding == null) {
            return null;
        }
        if (wedding.getWeddingScene().isEmpty()) {
            return Manager.mapManager.createCopyMap(WEDDING_COPY_MAP_ID, 1, WEDDING_COPY_MAP_ID, wedding.getWeddingScene().size() + 1);
        }
        List<Map.Entry<Long, Integer>> rank = wedding.getWeddingScene().entrySet().stream().sorted(Map.Entry.comparingByValue((v1, v2) -> v2 - v1)).collect(Collectors.toList());

        Cfg_Clone_map_Bean zone = CfgManager.getCfg_Clone_map_Container().getValueByKey(WEDDING_COPY_MAP_ID);
        Cfg_Mapsetting_Bean bean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(zone.getMapid());
        for (Map.Entry<Long, Integer> entry : rank) {
            if (entry.getValue() < bean.getOnline()) {
                return Manager.mapManager.getMap(entry.getKey());
            }
        }
        if (wedding.getWeddingScene().size() < bean.getLines()) {
            return Manager.mapManager.createCopyMap(WEDDING_COPY_MAP_ID, 1, WEDDING_COPY_MAP_ID, wedding.getWeddingScene().size() + 1);
        }
        Map.Entry<Long, Integer> entry = rank.get(rank.size() - 1);

        return Manager.mapManager.getMap(entry.getKey());
    }

    /**
     * 是否夫妻
     *
     * @param player
     * @param target
     * @return
     */
    @Override
    public boolean isCouple(Player player, Player target) {
        if (player.getId() == target.getId()) {
            return false;
        }
        return player.getMarriageUid() > 0 && player.getMarriageUid() == target.getMarriageUid();
    }

    /**
     * 获取仙娃技能
     *
     * @param player
     * @return
     */
    @Override
    public List<Skill> sumAllChildSkill(Player player) {
        List<Skill> skills = new ArrayList<>();
        for (MarryChild child : player.getChilds().values()) {
            Cfg_Marry_child_Bean bean = CfgManager.getCfg_Marry_child_Container().getValueByKey(child.getId());
            for (int i = 0; i < bean.getSkillId().size(); i++) {
                ReadArray<Integer> array = bean.getSkillId().get(i);
                int skillId = array.get(0);
                int openLevel = array.get(1);
                if (child.getLevel() >= openLevel) {
                    Skill skill = new Skill();
                    skill.setSkillId(skillId);
                    skills.add(skill);
                }
            }
        }
        return skills;
    }

    /**
     * 仙娃改名
     *
     * @param player
     * @param name
     */
    @Override
    public void reqMarryChildChangeName(Player player, int childId, String name) {
        MarryChild child = player.getChilds().get(childId);
        if (child == null) {
            return;
        }
        if (!isNameOK(player, name)){
            return;
        }
        if (!Manager.currencyManager.manager().onDecItemCoin(player, Global.Marry_child_name.get(1), ItemChangeReason.MarryBoxBuyDec, IDConfigUtil.getLogId(), Global.Marry_child_name.get(0))) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Item_Not_Enough, Manager.backpackManager.manager().getName(Global.Marry_box_cost.get(0)));
            return;
        }
        child.setName(name);
        Manager.playerManager.savePlayer(player, SavePlayerLevel.FiveMinLater);
        log.info("仙娃改名id={} name={} player={}", child.getId(), child.getName(), player);
        sendMarryChild(player, child, false);

        sendChildMap(player, child);

        writeMarryChildLog(player, child, 3);
    }
    //检查新角色名是否正确合法
    private boolean isNameOK(Player player, String name) {
        if (name == null || StringUtils.isBlank(name)) { //检查是否为空(客户端判断给予提示)
            return false;
        }
        int length = StringUtils.length(name);
        if (length < Global.PlayerNameLimit.get(1) || length > Global.PlayerNameLimit.get(0)) { //检查名字长度(客户端判断给予提示)
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.NameLength_error, Global.PlayerNameLimit.get(1) + "", Global.PlayerNameLimit.get(0) + ""); //“对不起，您的名字不符合取名规则，请重新输入。”
            return false;
        }
        if (Utils.isContainsShielding_symbol(name)) { //屏蔽标点符号检查
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.SensitiveNewName); //“对不起，您的名字中包含敏感字，改名失败！”
            return false;
        }
        if (Utils.isForbiddenStr(name)) { //屏蔽字检查
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.SensitiveNewName); //“对不起，您的名字中包含敏感字，改名失败！”
            return false;
        }
        if (name.contains("?")) { //屏蔽字检查
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.SensitiveNewName); //“对不起，您的名字中包含敏感字，改名失败！”
            return false;
        }
        return true;
    }

    void sendChildMap(Player player, MarryChild child) {
        MapMessage.ResChildCallInfo.Builder message = MapMessage.ResChildCallInfo.newBuilder();
        message.setPlayerId(player.getId());
        message.setChildId(child.getId());
        message.setOpt(child.getShow());
        if (child.getName() != null) {
            message.setChildName(child.getName());
        }
        MessageUtils.send_to_roundPlayer(player, MapMessage.ResChildCallInfo.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    public int calcChildLevelId(MarryChild child) {
        return calcChildCfgId(child.getId(), child.getLevel());
    }

    int calcChildCfgId(int id, int level) {
        //TODO 获取策划等级配置表ID
        return id * 100 + level;
    }

    //TODO 获取道具祝福值
    int calcChildCostExp(Cfg_Marry_childAtt_Bean config, int item) {
        for (int i = 0; i < config.getConsume().size(); i++) {
            ReadArray<Integer> cos = config.getConsume().get(i);
            if (cos.get(0) == item) {
                return cos.get(1);
            }
        }
        return -1;
    }

    /**
     * 升级仙娃
     *
     * @param player
     * @param itemId
     * @param oneKey
     */
    @Override
    public void reqUpgradeMarryChild(Player player, int childId, int itemId, boolean oneKey) {
        MarryChild child = player.getChilds().get(childId);
        if (child == null) {
            return;
        }
        //TODO 是否满级
        int nextId = calcChildCfgId(child.getId(), child.getLevel() + 1);
        Cfg_Marry_childAtt_Bean next = CfgManager.getCfg_Marry_childAtt_Container().getValueByKey(nextId);
        if (next == null) {
            return;
        }
        int cfgId = calcChildLevelId(child);
        Cfg_Marry_childAtt_Bean config = CfgManager.getCfg_Marry_childAtt_Container().getValueByKey(cfgId);

        int exp = calcChildCostExp(config, itemId);
        if (exp <= 0) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Item_Not_Enough, Manager.backpackManager.manager().getName(itemId));
            return;
        }
        int needCount = oneKey ? (config.getBlessingValue() - child.getExp() + exp - 1) / exp : 1;

        int itemNum = Manager.backpackManager.manager().getItemNum(player, itemId);
        if (itemNum <= 0) {
            if (!oneKey) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Item_Not_Enough, Manager.backpackManager.manager().getName(itemId));
            }
            return;
        }
        int min = Math.min(needCount, itemNum);
        //TODO 原因码 功能不匹配
        if (Manager.backpackManager.manager().onRemoveItem(player, itemId, min, ItemChangeReason.MarryChildLevelDec, IDConfigUtil.getLogId())) {
            child.setExp(child.getExp() + exp * min);
            marryChildUpLevel(player, child);
            if (oneKey) {
                reqUpgradeMarryChild(player, childId, itemId, true);
            }
//            log.info("仙娃强化id={} level={} exp={} player={}", child.getId(), child.getLevel(), child.getExp(), player);
            Manager.playerManager.savePlayer(player, SavePlayerLevel.FiveMinLater);
            sendMarryChild(player, child, false);
            Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.MARRIAGE, PlayerAttributeType.Skill);

            writeMarryChildLog(player, child, 2);
        }
    }

    void marryChildUpLevel(Player player, MarryChild child) {
        int nextId = calcChildCfgId(child.getId(), child.getLevel() + 1);
        Cfg_Marry_childAtt_Bean next = CfgManager.getCfg_Marry_childAtt_Container().getValueByKey(nextId);
        if (next == null) {
            return;
        }
        int cfgId = calcChildLevelId(child);
        Cfg_Marry_childAtt_Bean config = CfgManager.getCfg_Marry_childAtt_Container().getValueByKey(cfgId);
        if (child.getExp() >= config.getBlessingValue()) {
            int oldLv = child.getLevel();
            child.setLevel(child.getLevel() + 1);
            child.setExp(child.getExp() - config.getBlessingValue());
            log.info("仙娃升级id={} level={} exp={} player={}", child.getId(), child.getLevel(), child.getExp(), player);
            Manager.biManager.getScript().biGrow(player, GrowType.marry_child_levelUp.getType(), 0, BIDefine.GrowLevelUp, oldLv, child.getLevel(), child.getId());
            marryChildUpLevel(player, child);
        }
    }

    /**
     * 请求激活仙娃
     *
     * @param player
     */
    @Override
    public void reqOpenMarryChild(Player player, int childId) {
        if (player.getChilds().get(childId) != null) {
            return;
        }
        Cfg_Marry_child_Bean config = CfgManager.getCfg_Marry_child_Container().getValueByKey(childId);
        if (config == null) {
            return;
        }
        boolean condition;
        if (config.getActivation() == 1) {
            //TODO 只要一个条件满足就激活
            condition = openChildCondition(player, config) || openChildConditionItem(player, config);
        } else {
            //TODO 两个条件都满足才激活
            condition = openChildCondition(player, config) && openChildConditionItem(player, config);
        }
        if (!condition) {
            if (!openChildCondition(player, config)) {
                Cfg_Marry_lock_Bean bean = CfgManager.getCfg_Marry_lock_Container().getValueByKey(config.getCondition());
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.MarriageLockLevelLow, bean.getStage(), bean.getGrade());
            }
            return;
        }
        //TODO 激活条件满足
        MarryChild child = new MarryChild();
        child.setId(config.getId());
        child.setLevel(1);
        player.getChilds().put(child.getId(), child);
        Manager.playerManager.savePlayer(player, SavePlayerLevel.RightNow);

        log.info("激活仙娃成功child={} player={}", child.getId(), player);
        sendMarryChild(player, child, true);
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.MARRIAGE);

        //仙娃任务检测
        Manager.controlManager.operate(player, FunctionVariable.Marry_childNum, 1);

        writeMarryChildLog(player, child, 1);
        Manager.biManager.getScript().biGrow(player, GrowType.marry_child_active.getType(), 0, BIDefine.GrowActive, 0, child.getId(), child.getId());
    }

    MarriageMessage.MarryChild.Builder pack(MarryChild child, boolean active) {
        MarriageMessage.MarryChild.Builder mChild = MarriageMessage.MarryChild.newBuilder();
        mChild.setId(child.getId());
        mChild.setLevel(child.getLevel());
        mChild.setExp(child.getExp());
        mChild.setIsActive(active);
        mChild.setBattle(child.getShow());
        if (child.getName() != null) {
            mChild.setName(child.getName());
        }
        return mChild;
    }

    void sendMarryChild(Player player, MarryChild child, boolean active) {
        MarriageMessage.ResMarryChildInfo.Builder builder = MarriageMessage.ResMarryChildInfo.newBuilder();
        builder.addChilds(pack(child, active));
        MessageUtils.send_to_player(player, MarriageMessage.ResMarryChildInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    void sendMarryChild(Player player, Collection<MarryChild> childs) {
        MarriageMessage.ResMarryChildInfo.Builder builder = MarriageMessage.ResMarryChildInfo.newBuilder();
        for (MarryChild child : childs) {
            builder.addChilds(pack(child, false));
        }
        MessageUtils.send_to_player(player, MarriageMessage.ResMarryChildInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    //TODO 道具是否满足
    boolean openChildConditionItem(Player player, Cfg_Marry_child_Bean config) {
        if (config.getItemCondition().size() <= 0) {
            return true;
        }
        ReadArray<Integer> cost = config.getItemCondition().get(0);
        if (Manager.backpackManager.manager().onRemoveItem(player, cost.get(0), cost.get(1), ItemChangeReason.MarryChildActiveDec, IDConfigUtil.getLogId())) {
            return true;
        }
        MessageUtils.notify_player(player, Notify.ERROR, MessageString.Item_Not_Enough, Manager.backpackManager.manager().getName(cost.get(0)));
        return false;
    }

    //TODO 心锁等级是否满足
    boolean openChildCondition(Player player, Cfg_Marry_child_Bean config) {
        return calcMarryLockLevelId(player) >= config.getCondition();
    }

    /**
     * 拒绝购买仙匣
     *
     * @param player
     */
    @Override
    public void reqRefuseBuyMarryBox(Player player) {
        PlayerWorldInfo marryTarget = Manager.marriageManager.getMarryTarget(player);
        if (marryTarget == null) {
            return;
        }
        Player partner = Manager.playerManager.getPlayerOnline(marryTarget.getRoleid());
        if (partner == null) {
            return;
        }
        MessageUtils.notify_player(partner, Notify.ERROR, MessageString.MarryPrayFail_NotAgreeBuy);
        log.info("玩家拒绝购买仙匣 player={}", partner);
    }

    /**
     * 领取仙匣奖励
     *
     * @param player
     */
    @Override
    public void reqMarryBoxReward(Player player, int type) {

        if (player.getMarryBox().getTime() < TimeUtils.Time()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.MarryBoxFail_NotBuyBox);
            return;
        }
        if (type == 2) {
            //TODO 每日奖励
            boolean hasGet = Manager.countManager.getBooleanCountValue(player, BooleanDay.MarryBoxDailyReward);
            if (hasGet) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.MarryIntimacyFail_HasGet);
                return;
            }

            List<Item> list = Item.createItems(Global.Marry_box_sepcialrew, 1);
            if (!Manager.backpackManager.manager().addItems(player, list, ItemChangeReason.MarryDailyBoxRewardGet, IDConfigUtil.getLogId())) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.MainTaskNoBagCell);
                return;
            }
            Manager.countManager.setBooleanCountValue(player, BooleanDay.MarryBoxDailyReward, true);
            log.info("领取仙匣每日奖励 player={}", player);
        } else {
            //TODO 购买奖励
            if (Manager.countManager.getVariant(player, VariantType.MarryBoxBuyOnceReward) > 0) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.MarryIntimacyFail_HasGet);
                return;
            }

            List<Item> list = Item.createItems(Global.Marry_box_rew, 1);
            if (!Manager.backpackManager.manager().addItems(player, list, ItemChangeReason.MarryRebateBoxRewardGet, IDConfigUtil.getLogId())) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.MainTaskNoBagCell);
                return;
            }
            Manager.countManager.addVariant(player, VariantType.MarryBoxBuyOnceReward, 1);
            log.info("领取仙匣一次性奖励 player={}", player);
        }
        MarriageMessage.ResMarryBox.Builder builder = MarriageMessage.ResMarryBox.newBuilder();
        builder.addBox(pack(player));
        MessageUtils.send_to_player(player, MarriageMessage.ResMarryBox.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 请求 伴侣购买仙匣
     *
     * @param player
     */
    @Override
    public void reqCallBuyMarryBox(Player player) {

        if (Manager.cooldownManager.isCooldowning(player, CooldownTypes.MARRY_BUY_CD, null)) {
            long cooldownTime = Manager.cooldownManager.getCooldownTime(player, CooldownTypes.MARRY_BUY_CD, null);
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CooldownIng, cooldownTime / 1000);
            return;
        }
        //TODO 已有仙匣
        if (player.getMarryBox().getTime() > TimeUtils.Time()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.MarryBoxFail_HasBuy);
            return;
        }
        PlayerWorldInfo marryTarget = Manager.marriageManager.getMarryTarget(player);
        if (marryTarget == null) {
            return;
        }
        Player partner = Manager.playerManager.getPlayerOnline(marryTarget.getRoleid());
        if (partner == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.PlayerNotOnline, marryTarget.getRolename());
            return;
        }

        MessageUtils.notify_player(player, Notify.SUCCESS, MessageString.MarryBoxSuccess_RequseCoupleBuy);

        Manager.cooldownManager.addCooldown(player, CooldownTypes.MARRY_BUY_CD, null, 10 * 1000);
        //TODO 请求Cp 购买仙匣
        MarriageMessage.ResCallBuyMarryBox.Builder builder = MarriageMessage.ResCallBuyMarryBox.newBuilder();
        MessageUtils.send_to_player(partner, MarriageMessage.ResCallBuyMarryBox.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        log.info("玩家请求购买仙匣 player={}", player);
    }

    MarriageMessage.MarryBox.Builder pack(Player player) {
        MarriageMessage.MarryBox.Builder mBox = MarriageMessage.MarryBox.newBuilder();
        mBox.setRole(player.getId());
        mBox.setReward(Manager.countManager.getBooleanCountValue(player, BooleanDay.MarryBoxDailyReward) ? 1 : 0);
        mBox.setOnceReward(Manager.countManager.getVariant(player, VariantType.MarryBoxBuyOnceReward) > 0 ? 1 : 0);
        if (player.getMarryBox().getTime() > TimeUtils.Time()) {
            Long remainTime = player.getMarryBox().getTime() - TimeUtils.Time();
            mBox.setRemainTime((int) (remainTime / 1000));
        } else {
            mBox.setRemainTime(0);
        }
        return mBox;
    }

    /**
     * 购买仙匣
     *
     * @param player
     */
    @Override
    public void reqBuyMarryBox(Player player) {

        PlayerWorldInfo marryTarget = Manager.marriageManager.getMarryTarget(player);
        if (marryTarget == null) {
            return;
        }
        Player partner = Manager.playerManager.getPlayerOnline(marryTarget.getRoleid());
        if (partner == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.PlayerNotOnline, marryTarget.getRolename());
            return;
        }
        //TODO 已经有仙匣
        if (partner.getMarryBox().getTime() > TimeUtils.Time()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.MarryBoxFail_HasBuy);
            return;
        }
        if (!Manager.currencyManager.manager().onDecItemCoin(player, Global.Marry_box_cost.get(1), ItemChangeReason.MarryBoxBuyDec, IDConfigUtil.getLogId(), Global.Marry_box_cost.get(0))) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Item_Not_Enough, Manager.backpackManager.manager().getName(Global.Marry_box_cost.get(0)));
            return;
        }
        //TODO 购买成功
        Manager.countManager.setVariant(player, VariantType.MarryBoxBuyOnceReward, 0);
        long time = TimeUtils.Time();
        partner.getMarryBox().setTime(time + Global.Marry_box_day * 24 * 3600 * 1000l);

        MarriageMessage.ResMarryBox.Builder builder = MarriageMessage.ResMarryBox.newBuilder();
        builder.addBox(pack(partner));

        //TODO 通知双方
        MessageUtils.send_to_player(player, MarriageMessage.ResMarryBox.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        MessageUtils.send_to_player(partner, MarriageMessage.ResMarryBox.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        MessageUtils.notify_player(player, Notify.ERROR, MessageString.Buy_Marry_Box_Notice);
        MessageUtils.notify_player(partner, Notify.ERROR, MessageString.MarryBoxSuccess_CoupleBuy);

        //TODO 给对方发送一封邮件
        String content = MailManager.linkContext(MessageString.Buy_Marry_Box_Mail, player.getName());
        Manager.mailManager.sendMailToPlayer(partner.getId(), 1, MessageString.System, MessageString.Buy_Marry_Box_Mail_Title, content);

        Manager.countManager.addVariant(player, VariantType.MarryBoxNum, 1);
        Manager.controlManager.operate(player, FunctionVariable.Marry_boxNum, 1);


        Manager.playerManager.savePlayer(player, SavePlayerLevel.FiveMinLater);

        log.info("购买仙匣成功 player={} =====> partner={}", player, partner);

        writeMarryBoxLog(partner, player);
    }

    @Override
    public void sendMarryBox(Player player) {
        MarriageMessage.ResMarryBox.Builder mbox = MarriageMessage.ResMarryBox.newBuilder();
        mbox.addBox(pack(player));
        Player partner = getPartner(player);
        if (partner != null) {
            mbox.addBox(pack(partner));
        }
        MessageUtils.send_to_player(player, MarriageMessage.ResMarryBox.MsgID.eMsgID_VALUE, mbox.build().toByteArray());
    }

    /**
     * 获取心锁等级
     *
     * @param player
     * @return
     */
    @Override
    public int calcMarryLockLevelId(Player player) {
        return calcMarryLockLevel(player.getMarryLock().getStage(), player.getMarryLock().getGrade());
    }

    /**
     * 获取心锁等级
     *
     * @param stage
     * @param grade
     * @return
     */
    int calcMarryLockLevel(int stage, int grade) {
        //TODO 策划配置表等级 == 阶级 (转换关系)
        return (stage + 1) * 100 + grade;
    }

    /**
     * 激活仙缘心锁
     *
     * @param player
     */
    public void reqOpenMarryLock(Player player) {
        if (player.getMarryLock().isOpen()) {
            return;
        }
        if (Manager.backpackManager.manager().onRemoveItem(player, Global.Marry_Lock_Unlock_Item, 1, ItemChangeReason.MarryLocalUpLevelDec, IDConfigUtil.getLogId())) {
            player.getMarryLock().setOpen(true);
            player.getMarryLock().setGrade(1);
            log.info("仙缘心锁解锁 player={}", player);
            sendMarryLock(player);
            Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.MARRIAGE);
            writeMarryLockLog(player, 1);
            Manager.biManager.getScript().biGrow(player, GrowType.marry_lock_active.getType(), 0, BIDefine.GrowActive, 0, calcMarryLockLevelId(player), calcMarryLockLevelId(player));
            return;
        }
        MessageUtils.notify_player(player, Notify.ERROR, MessageString.Item_Not_Enough, Manager.backpackManager.manager().getName(Global.Marry_Lock_Unlock_Item));
    }

    /**
     * 获取心锁道具经验
     *
     * @param config
     * @param itemId
     * @return
     */
    int calcExp(Cfg_Marry_lock_Bean config, int itemId) {
        for (int i = 0; i < config.getCostItem().size(); i++) {
            if (config.getCostItem().get(i) == itemId) {
                return config.getSinglexp().get(i);
            }
        }
        return -1;
    }

    void sendMarryLock(Player player) {
        if (player.getMarryLock().isOpen()) {
            MarriageMessage.ResUpgradeMarryLockInfo.Builder builder = MarriageMessage.ResUpgradeMarryLockInfo.newBuilder();
            builder.setLevel(calcMarryLockLevelId(player));
            builder.setExp(player.getMarryLock().getExp());
            MessageUtils.send_to_player(player, MarriageMessage.ResUpgradeMarryLockInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        }
    }

    /**
     * 仙缘心锁
     *
     * @param player
     * @param oneKey
     */
    @Override
    public void reqUpGradeMarryLock(Player player, int itemId, boolean oneKey) {

        if (!player.getMarryLock().isOpen()) {
            reqOpenMarryLock(player);
            return;
        }
        int lockLevel = calcMarryLockLevelId(player);
        Cfg_Marry_lock_Bean config = CfgManager.getCfg_Marry_lock_Container().getValueByKey(lockLevel);
        if (config == null) {
            return;
        }
        if (oneKey) {
            for (int i = 0; i < config.getCostItem().size(); i++) {
                itemId = config.getCostItem().get(i);
                upGradeMarryLock(player, itemId, oneKey);
            }
        } else {
            upGradeMarryLock(player, itemId, oneKey);
        }
    }

    private void upGradeMarryLock(Player player, int itemId, boolean oneKey) {
        int lockLevel = calcMarryLockLevelId(player);
        Cfg_Marry_lock_Bean config = CfgManager.getCfg_Marry_lock_Container().getValueByKey(lockLevel);
        if (config == null) {
            return;
        }
        int nextGrade = calcMarryLockLevel(player.getMarryLock().getStage(), player.getMarryLock().getGrade() + 1);
        int nextStage = calcMarryLockLevel(player.getMarryLock().getStage() + 1, 1);
        Cfg_Marry_lock_Bean nextGradeCfg = CfgManager.getCfg_Marry_lock_Container().getValueByKey(nextGrade);
        Cfg_Marry_lock_Bean nextStageCfg = CfgManager.getCfg_Marry_lock_Container().getValueByKey(nextStage);
        //TODO 获取下一阶级配置
        if (nextGradeCfg == null && nextStageCfg == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.MarryLockLevelLimit);
            return;
        }
        int singleExp = calcExp(config, itemId);
        if (singleExp <= 0) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Item_Not_Enough, Manager.backpackManager.manager().getName(itemId));
            return;
        }
        int needCount = oneKey ? (config.getExp() - player.getMarryLock().getExp() + singleExp - 1) / singleExp : 1;
        int itemNum = Manager.backpackManager.manager().getItemNum(player, itemId);
        if (itemNum <= 0) {
            if (!oneKey) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Item_Not_Enough, Manager.backpackManager.manager().getName(itemId));
            }
            return;
        }
        int min = Math.min(needCount, itemNum);
        if (Manager.backpackManager.manager().onRemoveItem(player, itemId, min, ItemChangeReason.MarryLocalUpLevelDec, IDConfigUtil.getLogId())) {
            player.getMarryLock().setExp(player.getMarryLock().getExp() + min * singleExp);
            marryLockUpLevel(player);
            if (oneKey) {
                upGradeMarryLock(player, itemId, true);
            }
//            log.info("仙缘心锁经验level={} exp={} player={}", Manager.marriageManager.manager().calcMarryLockLevelId(player), player.getMarryLock().getExp(), player);
            sendMarryLock(player);
            Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.MARRIAGE);

            writeMarryLockLog(player, 2);
        }
    }

    void marryLockUpLevel(Player player) {
        int lockLevel = calcMarryLockLevelId(player);
        Cfg_Marry_lock_Bean config = CfgManager.getCfg_Marry_lock_Container().getValueByKey(lockLevel);
        if (config == null) {
            return;
        }
        int nextGrade = calcMarryLockLevel(player.getMarryLock().getStage(), player.getMarryLock().getGrade() + 1);
        int nextStage = calcMarryLockLevel(player.getMarryLock().getStage() + 1, 1);
        Cfg_Marry_lock_Bean nextGradeCfg = CfgManager.getCfg_Marry_lock_Container().getValueByKey(nextGrade);
        Cfg_Marry_lock_Bean nextStageCfg = CfgManager.getCfg_Marry_lock_Container().getValueByKey(nextStage);
        //TODO 获取下一阶级配置
        if (nextGradeCfg == null && nextStageCfg == null) {
            return;
        }
        if (player.getMarryLock().getExp() >= config.getExp()) {
            if (nextGradeCfg != null) {
                player.getMarryLock().setStage(nextGradeCfg.getStage());
                player.getMarryLock().setGrade(nextGradeCfg.getGrade());
            } else {
                player.getMarryLock().setStage(nextStageCfg.getStage());
                player.getMarryLock().setGrade(nextStageCfg.getGrade());
            }
            player.getMarryLock().setExp(player.getMarryLock().getExp() - config.getExp());
            log.info("仙缘心锁升级{}->{}.{} player={}", config.getLevel(), Manager.marriageManager.manager().calcMarryLockLevelId(player), player.getMarryLock().getExp(), player);
            Manager.biManager.getScript().biGrow(player, GrowType.marry_lock_levelUp.getType(), 0, BIDefine.GrowLevelUp, lockLevel, calcMarryLockLevelId(player), lockLevel);
            marryLockUpLevel(player);
        }
    }

    /**
     * 获取婚礼称号
     *
     * @param player
     * @param id
     */
    @Override
    public void reqRewardTitle(Player player, int id) {
        if (player.getMarriageUid() == 0) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.MARRY_NOTMARRIED);
            return;
        }
        Marriage marriage = Manager.marriageManager.getMarriageList().get(player.getMarriageUid());
        if (marriage == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.MARRY_NOTMARRIED);
            return;
        }
        Cfg_Marry_title_Bean config = CfgManager.getCfg_Marry_title_Container().getValueByKey(id);
        if (config == null) {
            return;
        }
        if (calcMarryLockLevelId(player) < config.getLock()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.MarriageLockLevelLow, String.valueOf(config.getLock() / 10), String.valueOf(config.getLock() % 10));
            return;
        }
        //TODO 获取仙缘对象
        PlayerWorldInfo some = getMarraige1(player, marriage);
        if (some == null) {
            return;
        }
        if (Manager.friendManager.getFriendIntimacy(player, some.getRoleid()) < config.getNeedValue()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.MarryIntimacyFail_NotIntimacy, config.getNeedValue() + "");
            return;
        }
        Cfg_Title_Bean title = CfgManager.getCfg_Title_Container().getValueByKey(config.getTitleId());
        if (title == null) {
            log.error("称号未找到 id={}", config.getTitleId());
            return;
        }
        //TODO 激活称号
        Manager.titleManager.deal().useTitleItem(player, title.getId(), 1, ItemChangeReason.MarryTitleGet);

        //TODO 公告
        MessageUtils.notify_allOnlinePlayer(Notify.CHAT_SYS_MARQUEE, MessageString.Marry_Title_Reward_Note, player.getName(),
                ServerStr.getChatTableName(title.getName()));
        log.warn("仙缘称号领取成功 player={} id={} title={}[{}]", player, id, title.getId(), title.getName());
    }

    @Override
    public void reqMarriage(Player player, long roleId, int type, boolean isNotice, String notice) {

        Cfg_Marry_dinner_Bean bean = CfgManager.getCfg_Marry_dinner_Container().getValueByKey(type);
        if (bean == null || bean.getNeed_type().size() < 2) {
            return;
        }
        Player bePlayer = Manager.playerManager.getPlayerCache(roleId);
        if (bePlayer == null || !bePlayer.isOnline()) {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.Marry_System_Tips5);
            return;
        }
        if (!Manager.currencyManager.manager().canDecItemCoin(player, bean.getNeed_type().get(1), bean.getNeed_type().get(0))) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Marry_Money_Not_Enough);
            return;
        }
        if (Manager.cooldownManager.isCooldowning(player, CooldownTypes.MARRY_CD, null)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.GetMarriageAndNoticeMate, Global.Marry_other_cd);
            return;
        }
        if (player.getMarriageUid() != 0) {
            Marriage marriage = Manager.marriageManager.getMarriageList().get(player.getMarriageUid());
            if (marriage != null) {
                if (marriage.gettList().contains(type)) {
                    MessageUtils.notify_player(player, Notify.NORMAL, MessageString.Marry_Order_Repeat_Tips);
                    return;
                }

                if (marriage.getBeMarriageId() != roleId && marriage.getMarriageId() != roleId) {
                    return;
                }

            } else {
                player.setMarriageUid(0);
                log.error(player.nameIdString() + "婚姻数据错误:" + player.getMarriageUid());
            }
        } else {
            if (bePlayer.getMarriageUid() != 0) {
                return;
            }
        }
        if (Global.MarryNeedSex == 1) {
            //性别判断
            if (player.getSex() == bePlayer.getSex()) {
                return;
            }
        }
        if (Manager.friendManager.getFriendRelation(player, roleId) != Relation.RelationType_Friend) {
            return;
        }
        if (Manager.friendManager.getFriendRelation(bePlayer, player.getId()) != Relation.RelationType_Friend) {
            return;
        }
        if (Manager.friendManager.getFriendIntimacy(player, roleId) < Global.Marry_Need_Love_Point) {
            return;
        }
        Cfg_FunctionStart_Bean fbean = CfgManager.getCfg_FunctionStart_Container().getValueByKey(FunctionStart.Marry);
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Marry)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.FUNC_UN_OPEN, player.getName(), ServerStr.getChatTableName(fbean.getFunction_name()));
            return;
        }
        if (!Manager.controlManager.deal().isOpenFunction(bePlayer, FunctionStart.Marry)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.FUNC_UN_OPEN, bePlayer.getName(), ServerStr.getChatTableName(fbean.getFunction_name()));
            return;
        }
        if (isNotice) {
            if (notice.length() > Global.Marry_Information_Chat.get(2)) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Marry_Information_Word_Limit);
                return;
            }
//            if (Utils.isForbiddenStr(notice)) {
//                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Marry_System_Tips12);
//                return;
//            }
            if (!Manager.currencyManager.manager().onDecItemCoin(player, Global.Marry_Information_Chat.get(1), ItemChangeReason.MarryNoticeDec, player.getId(), Global.Marry_Information_Chat.get(0))) {
                return;
            }
            MessageUtils.notify_allOnlinePlayer(Notify.CHAT_SYS_MARQUEE, MessageString.Marry_Information_World_Chat, player.getName(), bePlayer.getName(),
                    ServerStr.getChatTableName(bean.getName()), notice);
        }
        Manager.cooldownManager.addCooldown(player, CooldownTypes.MARRY_CD, null, Global.Marry_other_cd * 1000);
        Wedding wedding = new Wedding();
        wedding.setTargetId(roleId);
        wedding.setType(type);
        player.getWedding().add(wedding);

        MarriageMessage.ResMarryPropose.Builder builder = MarriageMessage.ResMarryPropose.newBuilder();
        builder.setCareer(player.getCareer());
        builder.setMarrayId(player.getId());
        builder.setType(type);
        builder.setName(player.getName());
        MessageUtils.send_to_player(bePlayer, MarriageMessage.ResMarryPropose.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        MapUtils.notifyEffect(1);

        if (bean.getLevel() >= 2) {
            actionTask(player, MarryTask.ReachMarry, 0);
            actionTask(bePlayer, MarryTask.ReachMarry, 0);
        }

    }

    @Override
    public void reqBeMarriage(Player player, long roleId, boolean isAgree) {
        Player targetPlayer = Manager.playerManager.getPlayerCache(roleId);
        if (targetPlayer == null) {
            return;
        }
        if (!isAgree) {
            if (targetPlayer.isOnline()) {
                MessageUtils.notify_player(targetPlayer, Notify.NORMAL, MessageString.Marry_Refuse_Title);
            }
            return;
        }
        //获取婚宴
        Wedding wedding = Utils.findOne(targetPlayer.getWedding(), w -> w.getTargetId() == player.getId());
        if (wedding == null) {
            MessageUtils.notify_player(targetPlayer, Notify.NORMAL, MessageString.GetMarriedRefuseOpp, targetPlayer.getName());
            return;
        }
        targetPlayer.getWedding().clear();

        int type = wedding.getType();
        int reason = ItemChangeReason.MarryDec;
        int getReason = ItemChangeReason.MarrySuccessGet;
        if (type == marryType_general) {
            reason = ItemChangeReason.MarryGeneralDec;
            getReason = ItemChangeReason.MarryGeneralGet;
        } else if (type == marryType_higher) {
            reason = ItemChangeReason.MarryHigherDec;
            getReason = ItemChangeReason.MarryGeneralGet;
        } else if (type == marryType_luxury) {
            reason = ItemChangeReason.MarryLuxuryDec;
            getReason = ItemChangeReason.MarryGeneralGet;
        }

        //todo 这里可能会异步行为
        Cfg_Marry_dinner_Bean bean = CfgManager.getCfg_Marry_dinner_Container().getValueByKey(wedding.getType());
        if (!Manager.currencyManager.manager().onDecItemCoin(targetPlayer, bean.getNeed_type().get(1), reason, player.getId(), bean.getNeed_type().get(0))) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Marry_Money_Not_Enough_Other);
            return;
        }

        Marriage marriage = Manager.marriageManager.getMarriageList().get(player.getMarriageUid());

        Marriage targetMarriage = Manager.marriageManager.getMarriageList().get(targetPlayer.getMarriageUid());
        //再婚
        if (marriage != null && marriage.getBeMarriageId() != roleId && marriage.getMarriageId() != roleId) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.OwnHaveMarriaged);
            return;
        }
        if (targetMarriage != null && targetMarriage.getBeMarriageId() != player.getId() && targetMarriage.getMarriageId() != player.getId()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.GetMarriedFailed_Married, targetPlayer.getName());
            return;
        }
        //初婚
        if (marriage == null && targetMarriage == null) {
            marriage = new Marriage();
            marriage.setId(IDConfigUtil.getId());
            marriage.setBeMarriageId(targetPlayer.getId());
            marriage.setMarriageId(player.getId());
            marriage.setMarriageTime(TimeUtils.Time());

            ServerParamUtil.weddingNum = ServerParamUtil.weddingNum + 1;
            ServerParamUtil.saveWeddingNum();
            marriage.setOrder(ServerParamUtil.weddingNum);

            Manager.marriageManager.getMarriageList().put(marriage.getId(), marriage);
            player.setMarriageUid(marriage.getId());
            targetPlayer.setMarriageUid(marriage.getId());
            player.setSaveToDB(true);
            targetPlayer.setSaveToDB(true);
            Manager.saveThreadManager.getOtherServerSave().deal(marriage.toMarrayBean(), DbSqlName.MARRIAGE_INSERT, SaveServer.INSERT);
        }
        if (marriage == null) {
            return;
        }
        if (!marriage.gettList().contains(wedding.getType())) {
            marriage.gettList().add(wedding.getType());
        }
        marriage.getBlessList().clear();

        Manager.saveThreadManager.getOtherServerSave().deal(marriage.toMarrayBean(), DbSqlName.MARRIAGE_UPDATE, SaveServer.UPDATE);

        //发送邮件
        String content = MessageString.Marry_Success_Mail + "@_@" + targetPlayer.getName() + "@_@" + player.getName() + "@_@" + marriage.getOrder();
        MessageUtils.notify_allOnlinePlayer(Notify.CHAT_SYS_MARQUEE, MessageString.Marry_Success_Mail, targetPlayer.getName(), player.getName(), marriage.getOrder() + "");

        List<Item> list = Item.createItems(player.getCareer(), bean.getRewardItemList(), 1);
        Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.Marry_Success_Mail_Title, content, list, getReason);

        List<Item> list2 = Item.createItems(targetPlayer.getCareer(), bean.getRewardItemList(), 1);
        Manager.mailManager.sendMailToPlayer(targetPlayer.getId(), 1, MessageString.System, MessageString.Marry_Success_Mail_Title, content, list2, getReason);

        Manager.countManager.addCount(player, BaseCountType.FinishWedding, wedding.getType(), Count.RefreshType.CountType_Forever, 1);
        Manager.countManager.addCount(targetPlayer, BaseCountType.FinishWedding, wedding.getType(), Count.RefreshType.CountType_Forever, 1);
        Manager.controlManager.operate(player, FunctionVariable.MarryTotal, 0);
        Manager.controlManager.operate(targetPlayer, FunctionVariable.MarryTotal, 0);

        //返回成功消息
        MarriageMessage.ResDealMarryPropose.Builder builder = MarriageMessage.ResDealMarryPropose.newBuilder();
        builder.setMarrayName(player.getName());
        builder.setMarraycareer(player.getCareer());
        builder.setBemarraycareer(targetPlayer.getCareer());
        builder.setBemarrayName(targetPlayer.getName());

        if (targetPlayer.isOnline()) {
            MessageUtils.send_to_player(targetPlayer, MarriageMessage.ResDealMarryPropose.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        }
        MessageUtils.send_to_player(player, MarriageMessage.ResDealMarryPropose.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        //广播给周边玩家
        MarriageMessage.ResMarryBroadcastName.Builder br = MarriageMessage.ResMarryBroadcastName.newBuilder();
        br.setPlayerId(player.getId());
        br.setName(targetPlayer.getName());
        MessageUtils.send_to_roundPlayer(player, MarriageMessage.ResMarryBroadcastName.MsgID.eMsgID_VALUE, br.build().toByteArray());

        //广播给周边玩家
        MarriageMessage.ResMarryBroadcastName.Builder br2 = MarriageMessage.ResMarryBroadcastName.newBuilder();
        br2.setPlayerId(targetPlayer.getId());
        br2.setName(player.getName());
        MessageUtils.send_to_roundPlayer(targetPlayer, MarriageMessage.ResMarryBroadcastName.MsgID.eMsgID_VALUE, br2.build().toByteArray());

        //广播祝福界面
        MarriageMessage.ResMarryPosterShow.Builder poster = MarriageMessage.ResMarryPosterShow.newBuilder();
        poster.setMan(packRole(player));
        poster.setWoman(packRole(targetPlayer));
        poster.setMarryId(marriage.getId());
        poster.setOrder(marriage.getOrder());

        for (Player isOnline : Manager.playerManager.getPlayersCache().values()) {
            if (isOnline.getId() == player.getId() || isOnline.getId() == targetPlayer.getId()) {
                continue;
            }
            if (isOnline.getLevel() >= Global.Marry_pray_level_limit && isOnline.isOnline()) {
                MessageUtils.send_to_player(isOnline, MarriageMessage.ResMarryPosterShow.MsgID.eMsgID_VALUE, poster.build().toByteArray());
            }
        }

        MapUtils.notifyEffect(2);

        writeMarryLog(marriage, 1);

        actionTask(player, MarryTask.Marry, 0);
        actionTask(targetPlayer, MarryTask.Marry, 0);
        //刷新 亲密点任务 进度
        Manager.controlManager.operate(player, FunctionVariable.Marry_intimacy, 0);
        if(targetPlayer.isOnline()){
            Manager.controlManager.operate(targetPlayer, FunctionVariable.Marry_intimacy, 0);
        }

//        Manager.biManager.getScript().biMarry(player, targetPlayer.getId(), targetPlayer.getName(), bean.getLevel());
//        Manager.biManager.getScript().biMarry(targetPlayer, player.getId(), player.getName(), bean.getLevel());

        //BI
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.Marriage, bean.getLevel(), bean.getName(), reason, 0, 0);
    }

    MarriageMessage.MarryRole.Builder packRole(Player player) {
        MarriageMessage.MarryRole.Builder man = MarriageMessage.MarryRole.newBuilder();
        man.setId(player.getId());
        man.setName(player.getName());
        man.setCareer(player.getCareer());
//        if (player.getNewFashionData().getWearDatas().containsKey(NewFashionManager.HEAD_TYPE)) {
//            int headId = player.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_TYPE).getFashionID();
//            man.setHeadId(headId);
//        }
//        if (player.getNewFashionData().getWearDatas().containsKey(NewFashionManager.HEAD_FRAME_TYPE)) {
//            int headFrameId = player.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_FRAME_TYPE).getFashionID();
//            man.setHeadFrameId(headFrameId);
//        }

        man.setHead(MapUtils.getHead(player));

        return man;
    }

    /**
     * 预约婚礼状态
     *
     * @param player
     * @param bePlayer
     * @param res
     */
    void sendMentWedding(Player player, Player bePlayer, int res, int weddingId) {
        MarriageMessage.ResSelectWedding.Builder builder = MarriageMessage.ResSelectWedding.newBuilder();
        builder.setRes(res);
        builder.setWeddingId(weddingId);


        MessageUtils.send_to_player(bePlayer, MarriageMessage.ResSelectWedding.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        MessageUtils.send_to_player(player, MarriageMessage.ResSelectWedding.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }


    /**
     * 自动预约
     *
     * @param player
     * @param id
     */
    @Override
    public void reqSelectMarriage(Player player, int id) {
        if (player.getMarriageUid() == 0) {
            return;
        }
        Marriage marriage = Manager.marriageManager.getMarriageList().get(player.getMarriageUid());
        if (marriage == null) {
            return;
        }
        Player bePlayer = getMarraige(player, marriage);
        if (bePlayer == null) {
            return;
        }
        //TODO 婚宴次数
        if (marriage.getNum() >= getMaxNum(marriage)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CountNotEnough);
            return;
        }
        if (Manager.marriageManager.getCurMarriageId() == marriage.getId()) {
            return;
        }
        Cfg_Marry_order_Bean bean = null;
        //自动最近一场预约
        if (id == 0) {
            //TODO 已预约
            if (ServerParamUtil.weddingList.containsKey(marriage.getId())) {
                sendMentWedding(player, bePlayer, PreWeddingState_Limit, id);
                return;
            }
            Cfg_Marry_order_Bean[] cfg_Marry_order_Bean_List = CfgManager.getCfg_Marry_order_Container().getValuees();
            for (int i = 0; i < cfg_Marry_order_Bean_List.length; i++) {
                if (TimeUtils.Time() > TimeUtils.getTodayBeginTime() + cfg_Marry_order_Bean_List[i].getTime() * 60 * 1000) {
                    continue;
                }
                int curBeginMin = (int) (TimeUtils.getTodayBeginTime() / 60000);
                int key = curBeginMin + cfg_Marry_order_Bean_List[i].getTime();
                //TODO 已被预约
                Map.Entry<Long, Integer> wedding = Utils.findOne(ServerParamUtil.weddingList.entrySet(), e -> e.getValue() == key);
                if (wedding != null) {
                    continue;
                }
                bean = cfg_Marry_order_Bean_List[i];
                break;
            }
            if (bean == null) {
                //   没有预约时间提示
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.marry_order_time_out_notice);
                return;
            }
        } else {
            bean = CfgManager.getCfg_Marry_order_Container().getValueByKey(id);
            if (bean == null) {
                //没有预约时间提示
                // MessageUtils.notify_player(player, Notify.ERROR, MessageString.marry_order_time_out_notice);
                return;
            }
            //TODO 已预约
            if (ServerParamUtil.weddingList.containsKey(marriage.getId())) {
                sendMentWedding(player, bePlayer, PreWeddingState_Limit, bean.getId());
                return;
            }
        }
        //TODO 超时
        if (TimeUtils.Time() > TimeUtils.getTodayBeginTime() + bean.getTime() * 60 * 1000) {
            sendMentWedding(player, bePlayer, PreWeddingState_TimeOut, bean.getId());
            return;
        }
        int curBeginMin = (int) (TimeUtils.getTodayBeginTime() / 60000);
        int key = curBeginMin + bean.getTime();
        //TODO 已被预约
        Map.Entry<Long, Integer> wedding = Utils.findOne(ServerParamUtil.weddingList.entrySet(), e -> e.getValue() == key);
        if (wedding != null) {
            if (wedding.getKey() == marriage.getId()) {
                sendMentWedding(player, bePlayer, PreWeddingState_Limit, bean.getId());
            } else {
                sendMentWedding(player, bePlayer, PreWeddingState_UnNull, bean.getId());
            }
            return;
        }

        marriage.setNum(marriage.getNum() + 1);

        ServerParamUtil.weddingList.put(marriage.getId(), key);
        ServerParamUtil.saveWeddingData();

        Manager.saveThreadManager.getOtherServerSave().deal(marriage.toMarrayBean(), DbSqlName.MARRIAGE_UPDATE, SaveServer.UPDATE);

        MarriageMessage.ResUpdateWedding.Builder bb = MarriageMessage.ResUpdateWedding.newBuilder();
        MarriageMessage.WeddingData.Builder wd = MarriageMessage.WeddingData.newBuilder();
        wd.setTimeStart(key);

        wd.setMarryId(player.getId());
        wd.setMarrayName(player.getName());
        wd.setMarrayCareer(player.getCareer());
        wd.setMarryHead(MapUtils.getHead(player));

        wd.setBemMarryId(bePlayer.getId());
        wd.setBeMarrayName(bePlayer.getName());
        wd.setBeMarrayCareer(bePlayer.getCareer());
        wd.setBeMarryHead(MapUtils.getHead(bePlayer));

        bb.setWeddingData(wd);
        MessageUtils.send_to_all_player(MarriageMessage.ResUpdateWedding.MsgID.eMsgID_VALUE, bb.build().toByteArray());

        sendMentWedding(player, bePlayer, PreWeddingState_Suc, bean.getId());

        String content = MessageString.Marry_Order_Success_Mail_Title + "@_@" + player.getName() + "@_@" + bePlayer.getName() + "@_@" + TimeUtils.format2string(key * 60 * 1000L, "HH:mm");
        Manager.mailManager.sendMailToPlayer(marriage.getMarriageId(), 1, MessageString.System, MessageString.Marry_Divorce_Mail_Title, content);
        Manager.mailManager.sendMailToPlayer(marriage.getBeMarriageId(), 1, MessageString.System, MessageString.Marry_Divorce_Mail_Title, content);

        writeMarryLog(marriage, 2);

        actionTask(player, MarryTask.OrderWedding, 0);
        actionTask(bePlayer, MarryTask.OrderWedding, 0);
    }

    @Override
    public void reqDivorce(Player player, int type) {

        if (Manager.cooldownManager.isCooldowning(player, CooldownTypes.MARRY_DIVORCE_CD, null)) {
            long cooldownTime = Manager.cooldownManager.getCooldownTime(player, CooldownTypes.MARRY_DIVORCE_CD, null);
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CooldownIng, cooldownTime / 1000);
            return;
        }
        if (player.getMarriageUid() == 0) {
            return;
        }
        Marriage marriage = Manager.marriageManager.getMarriageList().get(player.getMarriageUid());
        if (marriage.getId() == Manager.marriageManager.getCurMarriageId()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Marry_Divorce_Note);
            return;
        }
        Map.Entry<Long, Integer> wedding = Utils.findOne(ServerParamUtil.weddingList.entrySet(), e -> e.getKey() == marriage.getId());
        if (wedding != null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Marry_Divorce_Note);
            return;
        }
        Player bePlayer = getMarraige(player, marriage);
        //TODO 强制离婚
        if (type == 1) {
            if (!Manager.currencyManager.manager().onDecItemCoin(player, Global.Force_Divorce_Cost.get(1), ItemChangeReason.Force_Divorce_Dec, player.getId(), Global.Force_Divorce_Cost.get(0))) {
                return;
            }
            divorce(player, bePlayer, marriage, 0);
            Manager.biManager.getScript().biMarry(player, bePlayer.getId(), bePlayer.getName(), 5);
            return;
        }

        //TODO 申述离婚
        if (!bePlayer.isOnline() && TimeUtils.Time() - bePlayer.getOffLineTime() >= Global.Divorce_free_Time * 60 * 60 * 1000L) {
            divorce(player, bePlayer, marriage, 0);
            Manager.biManager.getScript().biMarry(player, bePlayer.getId(), bePlayer.getName(), 4);
            return;
        }

        //TODO 协议离婚
        if (!bePlayer.isOnline()) {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.Divorce_Online_Limit);
            return;
        }
        marriage.setDivorceID(player.getId());
        Manager.saveThreadManager.getOtherServerSave().deal(marriage.toMarrayBean(), DbSqlName.MARRIAGE_UPDATE, SaveServer.UPDATE);

        Manager.cooldownManager.addCooldown(player, CooldownTypes.MARRY_DIVORCE_CD, null, Global.Divorce_Beg_CD * 1000);

        MarriageMessage.ResDivorceID.Builder builder = MarriageMessage.ResDivorceID.newBuilder();
        builder.setRoleId(player.getId());
        MessageUtils.send_to_player(player, MarriageMessage.ResDivorceID.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        MessageUtils.send_to_player(bePlayer, MarriageMessage.ResDivorceID.MsgID.eMsgID_VALUE, builder.build().toByteArray());

    }

    @Override
    public void reqAffirmDivorce(Player player, int opt) {
        if (player.getMarriageUid() == 0) {
            return;
        }
        Marriage marriage = Manager.marriageManager.getMarriageList().get(player.getMarriageUid());
        Player bePlayer = getMarraige(player, marriage);
        //TODO 拒绝
        if (opt == 0) {
            MessageUtils.notify_player(bePlayer, Notify.NORMAL, MessageString.PeaceDivorceRefruse);
        } else {
            //TODO 同意
            if (marriage.getDivorceID() == bePlayer.getId()) {
                divorce(player, bePlayer, marriage, 0);
            }
        }
    }

    @Override
    public void divorce(Player player, Player bePlayer, Marriage marriage, int type) {
        player.setMarriageUid(0);
        bePlayer.setMarriageUid(0);
        player.setSaveToDB(true);
        bePlayer.setSaveToDB(true);
        Manager.marriageManager.getMarriageList().remove(marriage.getId());
        Manager.saveThreadManager.getOtherServerSave().deal(marriage.toMarrayBean(), DbSqlName.MARRIAGE_DELETE, SaveServer.DELETE);

        if (Manager.marriageManager.getCurMarriageId() == marriage.getId()) {
            Manager.marriageManager.setCurMarriageId(0);
        }

        ServerParamUtil.weddingList.remove(marriage.getId());

        ServerParamUtil.saveWeddingData();

        MarriageMessage.ResDivorce.Builder builder = MarriageMessage.ResDivorce.newBuilder();
        MessageUtils.send_to_player(player, MarriageMessage.ResDivorce.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        if (bePlayer.isOnline()) {
            MessageUtils.send_to_player(bePlayer, MarriageMessage.ResDivorce.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        }

        if (type == 0) {
            String content = MessageString.Divorce_Success_Mail + "@_@" + player.getName() + "@_@" + bePlayer.getName();
            Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.Divorce_Success_Mail_Title, content);
            Manager.mailManager.sendMailToPlayer(bePlayer.getId(), 1, MessageString.System, MessageString.Divorce_Success_Mail_Title, content);
        } else {
            String content = MessageString.Delete_Role_Divorce_Mail + "@_@" + player.getName();
            Manager.mailManager.sendMailToPlayer(bePlayer.getId(), 1, MessageString.System, MessageString.Delete_Role_Divorce_Mail_Title, content);
        }

        MarriageMessage.ResMarryBroadcastName.Builder br = MarriageMessage.ResMarryBroadcastName.newBuilder();
        br.setPlayerId(player.getId());
        MessageUtils.send_to_roundPlayer(player, MarriageMessage.ResMarryBroadcastName.MsgID.eMsgID_VALUE, br.build().toByteArray());

        MarriageMessage.ResMarryBroadcastName.Builder br2 = MarriageMessage.ResMarryBroadcastName.newBuilder();
        br2.setPlayerId(bePlayer.getId());
        MessageUtils.send_to_roundPlayer(bePlayer, MarriageMessage.ResMarryBroadcastName.MsgID.eMsgID_VALUE, br2.build().toByteArray());

        writeMarryLog(marriage, 3);
    }

    public Player getPartner(Player player) {
        Marriage marriage = Manager.marriageManager.getMarriageList().get(player.getMarriageUid());
        return getMarraige(player, marriage);
    }

    @Override
    public Player getMarraige(Player player, Marriage marriage) {
        if (marriage == null) {
            return null;
        }
        long roleId = 0;

        if (marriage.getMarriageId() == player.getId()) {
            roleId = marriage.getBeMarriageId();
        }

        if (marriage.getBeMarriageId() == player.getId()) {
            roleId = marriage.getMarriageId();
        }

        return Manager.playerManager.getPlayer(roleId);
    }

    @Override
    public void reqMarriageData(Player player) {
        if (player.getMarriageUid() == 0) {
            return;
        }

        Marriage marriage = Manager.marriageManager.getMarriageList().get(player.getMarriageUid());
        if (marriage == null) {
            log.error(player.nameIdString() + "婚姻数据不存在:" + player.getMarriageUid());
            player.setMarriageUid(0);
            return;
        }

        PlayerWorldInfo info = getMarraige1(player, marriage);
        if (info == null) {
            //todo 角色信息错误处理
            return;
        }
        Player parter = getMarraige(player, marriage);
        if (parter == null) {
            return;
        }
        PlayerRelation relation = Manager.friendManager.getPlayerRelation(player.getId());

        Friend friend = relation.getFriends().get(parter.getId());

        MarriageMessage.ResMarryData.Builder builder = MarriageMessage.ResMarryData.newBuilder();
        builder.setCareer(info.getCareer());
        builder.setName(info.getRolename());
        builder.setStateLv(info.getStateVip());
        builder.setIntimacy(friend == null ? 0 : friend.getIntimacy());
        builder.setPlayerId(info.getRoleid());
        builder.setWeddingNum(getMaxNum(marriage) - marriage.getNum());
        builder.setMarryDay(getMarrigeDay(marriage));
        builder.setDivorceId(marriage.getDivorceID());
        builder.addAllTList(marriage.gettList());
        builder.setPurNum(marriage.getPurNum());
        builder.setFacade(MapUtils.getFacade(info));

        Iterator<Map.Entry<Long, String>> iter = marriage.getInviteList().entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Long, String> entry = iter.next();
            MarriageMessage.WeddingMember.Builder wm = MarriageMessage.WeddingMember.newBuilder();
            wm.setRoleId(entry.getKey());
            wm.setName(entry.getValue());
            builder.addWeddingMembersList(wm);
        }
        MessageUtils.send_to_player(player, MarriageMessage.ResMarryData.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void reqDemandInvit(Player player, int key) {
        Map.Entry<Long, Integer> wedding = Utils.findOne(ServerParamUtil.weddingList.entrySet(), e -> e.getValue() == key);
        if (wedding == null) {
            sendDemandInvit(player, 0);
            return;
        }
        Marriage marriage = Manager.marriageManager.getMarriageList().get(wedding.getKey());
        if (marriage.getApplyList().containsKey(player.getId())) {
            sendDemandInvit(player, 2);
            return;
        }
        if (marriage.getInviteList().containsKey(player.getId())) {
            sendDemandInvit(player, 3);
            return;
        }
        if (marriage.getInviteList().size() >= Global.Marry_Guest_Limit + marriage.getPurNum()) {
            sendDemandInvit(player, 4);
            return;
        }
        if (marriage.getMarriageId() == player.getId() || marriage.getBeMarriageId() == player.getId()) {
            sendDemandInvit(player, 5);
            return;
        }
        marriage.getApplyList().put(player.getId(), player.getName());

        MarriageMessage.ResUpdateDemandInvit.Builder invit = MarriageMessage.ResUpdateDemandInvit.newBuilder();
        MarriageMessage.WeddingMember.Builder mm = MarriageMessage.WeddingMember.newBuilder();
        mm.setRoleId(player.getId());
        mm.setName(player.getName());
        invit.setMember(mm);
        Player p1 = Manager.playerManager.getPlayer(marriage.getMarriageId());
        if (p1.isOnline()) {
            MessageUtils.send_to_player(p1, MarriageMessage.ResUpdateDemandInvit.MsgID.eMsgID_VALUE, invit.build().toByteArray());
        }

        Player p2 = Manager.playerManager.getPlayer(marriage.getBeMarriageId());
        if (p2.isOnline()) {
            MessageUtils.send_to_player(p2, MarriageMessage.ResUpdateDemandInvit.MsgID.eMsgID_VALUE, invit.build().toByteArray());
        }
    }

    void sendDemandInvit(Player player, int res) {
        MarriageMessage.ResDemandInvit.Builder builder = MarriageMessage.ResDemandInvit.newBuilder();
        builder.setRes(res);
        MessageUtils.send_to_player(player, MarriageMessage.ResDemandInvit.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void reqInvit(Player player, long roleId, int type) {
        Marriage marriage = Manager.marriageManager.getMarriageList().get(player.getMarriageUid());
        if (marriage == null) {
            return;
        }
        Integer weddingTime = ServerParamUtil.weddingList.get(marriage.getId());
        if (weddingTime == null) {
            return;
        }
        if (marriage.getInviteList().size() >= Global.Marry_Guest_Limit + marriage.getPurNum()) {
            return;
        }
        Player p1 = getMarraige(player, marriage);
        MarriageMessage.ResUpdateInvit.Builder builder = MarriageMessage.ResUpdateInvit.newBuilder();
        //自动邀请所有在线的
        if (roleId == 0) {
            List<Player> onlineList = Manager.playerManager.getOnLines();
            if (onlineList != null && onlineList.size() > 0) {
                for (int i = 0; i < onlineList.size(); i++) {
                    //排除自己 和 结婚对象
                    if (marriage.getBeMarriageId() == onlineList.get(i).getId() || marriage.getMarriageId() == onlineList.get(i).getId()) {
                        continue;
                    }
                    //已经邀请了
                    if (marriage.getInviteList().containsKey(onlineList.get(i).getId())) {
                        continue;
                    }
                    MarriageMessage.WeddingMember.Builder wm = MarriageMessage.WeddingMember.newBuilder();
                    wm.setRoleId(onlineList.get(i).getId());
                    wm.setName(onlineList.get(i).getName());
                    marriage.getInviteList().put(onlineList.get(i).getId(), onlineList.get(i).getName());
                    builder.addMemberList(wm);

                    //发送邮件邀请
                    String content = MessageString.Marry_Invite_Guest_Mail + "@_@" + player.getName() + "@_@" + p1.getName() + "@_@" + TimeUtils.format2string(weddingTime * 60 * 1000L, "HH:mm");
                    Manager.mailManager.sendMailToPlayer(onlineList.get(i).getId(), 1, MessageString.System, MessageString.Marry_Invite_Guest_Mail_Title, content);
                }
            }
        } else {
            if (marriage.getBeMarriageId() == roleId || marriage.getMarriageId() == roleId) {
                return;
            }
            if (type == 1 && Manager.friendManager.getFriendRelation(player, roleId) != Relation.RelationType_Friend) {
                return;
            }
            if (type == 2) {
                if (player.getGuildId() == 0) {
                    return;
                }

                Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());
                if (!guild.getMembers().containsKey(roleId)) {
                    return;
                }
            }

            Player invitPlayer = Manager.playerManager.getPlayer(roleId);
            if (invitPlayer == null) {
                return;
            }

            //已经邀请了
            if (marriage.getInviteList().containsKey(invitPlayer.getId())) {
                return;
            }

            MarriageMessage.WeddingMember.Builder wm = MarriageMessage.WeddingMember.newBuilder();
            wm.setRoleId(roleId);
            wm.setName(invitPlayer.getName());
            marriage.getInviteList().put(roleId, invitPlayer.getName());
            builder.addMemberList(wm);
            //发送邮件邀请
            String content = MessageString.Marry_Invite_Guest_Mail + "@_@" + player.getName() + "@_@" + p1.getName() + "@_@" + TimeUtils.format2string(weddingTime * 60 * 1000L, "HH:mm");
            Manager.mailManager.sendMailToPlayer(roleId, 1, MessageString.System, MessageString.Marry_Invite_Guest_Mail_Title, content);

        }

        Manager.saveThreadManager.getOtherServerSave().deal(marriage.toMarrayBean(), DbSqlName.MARRIAGE_UPDATE, SaveServer.UPDATE);
        MessageUtils.send_to_player(p1, MarriageMessage.ResUpdateInvit.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        MessageUtils.send_to_player(player, MarriageMessage.ResUpdateInvit.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void reqAgreeInvit(Player player, long roleId, boolean isAgree) {

        Marriage marriage = Manager.marriageManager.getMarriageList().get(player.getMarriageUid());
        if (marriage == null) {
            return;
        }
        Integer weddingTime = ServerParamUtil.weddingList.get(marriage.getId());
        if (weddingTime == null) {
            return;
        }
        if (!marriage.getApplyList().containsKey(roleId)) {
            return;
        }
        String inviteName = marriage.getApplyList().get(roleId);
        marriage.getApplyList().remove(roleId);
        MarriageMessage.ResDeleteDemandInvit.Builder dInvit = MarriageMessage.ResDeleteDemandInvit.newBuilder();
        dInvit.setRoleId(roleId);

        Player p1 = getMarraige(player, marriage);
        if (p1.isOnline()) {
            MessageUtils.send_to_player(p1, MarriageMessage.ResDeleteDemandInvit.MsgID.eMsgID_VALUE, dInvit.build().toByteArray());
        }

        MessageUtils.send_to_player(player, MarriageMessage.ResDeleteDemandInvit.MsgID.eMsgID_VALUE, dInvit.build().toByteArray());

        if (isAgree) {
            marriage.getInviteList().put(roleId, inviteName);
            Manager.saveThreadManager.getOtherServerSave().deal(marriage.toMarrayBean(), DbSqlName.MARRIAGE_UPDATE, SaveServer.UPDATE);

            String content = MessageString.Marry_Invite_Guest_Mail + "@_@" + player.getName() + "@_@" + p1.getName() + "@_@" + TimeUtils.format2string(weddingTime * 60 * 1000L, "HH:mm");
            Manager.mailManager.sendMailToPlayer(roleId, 1, MessageString.System, MessageString.Marry_Invite_Guest_Mail_Title, content);

            MarriageMessage.ResUpdateInvit.Builder builder = MarriageMessage.ResUpdateInvit.newBuilder();
            MarriageMessage.WeddingMember.Builder wm = MarriageMessage.WeddingMember.newBuilder();
            wm.setRoleId(roleId);
            wm.setName(inviteName);
            builder.addMemberList(wm);

            MessageUtils.send_to_player(p1, MarriageMessage.ResUpdateInvit.MsgID.eMsgID_VALUE, builder.build().toByteArray());
            MessageUtils.send_to_player(player, MarriageMessage.ResUpdateInvit.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        }
    }

    /**
     * 购买邀请上限
     *
     * @param player
     */
    @Override
    public void reqPurInvitNum(Player player) {
        Marriage marriage = Manager.marriageManager.getMarriageList().get(player.getMarriageUid());
        if (marriage == null) {
            return;
        }
        Integer weddingTime = ServerParamUtil.weddingList.get(marriage.getId());
        if (weddingTime == null) {
            return;
        }
        if (marriage.getPurNum() >= Global.Marry_Buy_Guest_Cost.get(2)) {
            return;
        }
        if (!Manager.currencyManager.manager().onDecItemCoin(player, Global.Marry_Buy_Guest_Cost.get(1), ItemChangeReason.PurInviteNum, player.getId(), Global.Marry_Buy_Guest_Cost.get(0))) {
            return;
        }
        marriage.setPurNum(marriage.getPurNum() + 1);

        MarriageMessage.ResPurInvitNum.Builder builder = MarriageMessage.ResPurInvitNum.newBuilder();
        MessageUtils.send_to_player(player, MarriageMessage.ResPurInvitNum.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        Player bePlayer = getMarraige(player, marriage);
        if (bePlayer != null)
            MessageUtils.send_to_player(bePlayer, MarriageMessage.ResPurInvitNum.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void online(Player player) {
        MarriageMessage.ResMarryOnline.Builder builder = MarriageMessage.ResMarryOnline.newBuilder();
        Iterator<Map.Entry<Long, Integer>> iter = ServerParamUtil.weddingList.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Long, Integer> wedding = iter.next();
            Marriage marriage = Manager.marriageManager.getMarriageList().get(wedding.getKey());
            if (marriage == null) {
                iter.remove();
                log.error("婚宴数据不存在:" + wedding.getKey());
                continue;
            }

            PlayerWorldInfo info1 = getMarraige(marriage.getMarriageId());
            PlayerWorldInfo info2 = getMarraige(marriage.getBeMarriageId());

            if (info1 == null || info2 == null) {
                iter.remove();
                log.error("婚宴角色数据不存在:" + wedding.getKey());
                continue;
            }

            MarriageMessage.WeddingData.Builder wd = MarriageMessage.WeddingData.newBuilder();
            wd.setMarrayCareer(info2.getCareer());
            wd.setMarrayName(info2.getRolename());
            wd.setMarryId(info2.getRoleid());
            wd.setBeMarryHead(MapUtils.getHead(info2));

            wd.setBeMarrayCareer(info1.getCareer());
            wd.setBeMarrayName(info1.getRolename());
            wd.setBemMarryId(info1.getRoleid());
            wd.setMarryHead(MapUtils.getHead(info1));

            wd.setTimeStart(wedding.getValue());
            builder.addWeddingDataList(wd);
        }

        Marriage marriage = Manager.marriageManager.getMarriageList().get(player.getMarriageUid());
        if (marriage != null) {
            Iterator<Map.Entry<Long, String>> iter1 = marriage.getApplyList().entrySet().iterator();
            while (iter1.hasNext()) {
                Map.Entry<Long, String> entry1 = iter1.next();
                MarriageMessage.WeddingMember.Builder wm = MarriageMessage.WeddingMember.newBuilder();
                wm.setRoleId(entry1.getKey());
                wm.setName(entry1.getValue());
                builder.addWeddingMembersList(wm);
            }
        }

        MessageUtils.send_to_player(player, MarriageMessage.ResMarryOnline.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        //TODO 通知心锁
        sendMarryLock(player);
        //TODO 通知仙娃
        sendMarryChild(player, player.getChilds().values());
        //TODO 通知仙匣
        sendMarryBox(player);
        //TODO 同步情缘副本
        Manager.marriageManager.clone().sendMarryCloneTimes(player);
        //TODO 通知相亲墙数据
        sendMarryWallRewardState(player);
    }

    @Override
    public void tick() {
        int nowMin = (int) (TimeUtils.TimeSec() / 60);
        Iterator<Map.Entry<Long, Integer>> iter = ServerParamUtil.weddingList.entrySet().iterator();
        while (iter.hasNext()) {
            if (Manager.marriageManager.getCurMarriageId() > 0) {
                break;
            }
            Map.Entry<Long, Integer> wedding = iter.next();
            Marriage marriage = Manager.marriageManager.getMarriageList().get(wedding.getKey());
            if (Manager.marriageManager.getCurMarriageId() == marriage.getId()) {
                continue;
            }
            //TODO fixbug GM 设置时间 或者服务器卡顿超过一分钟，导致的婚宴无法进行， 策划说直接清掉
            if (wedding.getValue() < nowMin) {
                iter.remove();
                marriage.setPurNum(0);
                marriage.getInviteList().clear();
                marriage.getApplyList().clear();
                log.info(wedding.getKey() + "婚宴过期:" + wedding.getValue() + "新婚人:" + marriage.getMarriageId() + "和" + marriage.getBeMarriageId());
                continue;
            }
            if (wedding.getValue() == nowMin) {
                log.info(wedding.getKey() + "婚宴即将开始:" + wedding.getValue() + "新婚人:" + marriage.getMarriageId() + "和" + marriage.getBeMarriageId());

                Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(WEDDING_COPY_MAP_ID);
                WeddingMapInfo weddingMapInfo = new WeddingMapInfo();

                weddingMapInfo.setWeddingID(marriage.getId());
                weddingMapInfo.setHubbyID(marriage.getMarriageId());
                weddingMapInfo.setWifeID(marriage.getBeMarriageId());
                long weddingTime = TimeUtils.Time() + 300000; //策划欧阳帆偷懒不配置，喊写死
                long overTime = TimeUtils.Time() + bean.getExist_time();
                weddingMapInfo.setWeddingTime(weddingTime);
                weddingMapInfo.setOverTime(overTime);

                Manager.marriageManager.setCurMarriageId(marriage.getId());
                Manager.marriageManager.setWedding(weddingMapInfo);

                Manager.mapManager.createCopyMap(WEDDING_COPY_MAP_ID, 1, WEDDING_COPY_MAP_ID, weddingMapInfo.getWeddingScene().size() + 1);

                PlayerWorldInfo p1 = Manager.playerManager.getPlayerWorldInfo(marriage.getMarriageId());
                PlayerWorldInfo p2 = Manager.playerManager.getPlayerWorldInfo(marriage.getBeMarriageId());
                //TODO 婚宴开启
                MarriageMessage.ResWeddingStart.Builder ws = MarriageMessage.ResWeddingStart.newBuilder();
                ws.setCopyMapId(WEDDING_COPY_MAP_ID);

                for (long applyId : marriage.getInviteList().keySet()) {
                    Player apply = Manager.playerManager.getPlayerOnline(applyId);
                    if (apply == null) {
                        continue;
                    }
                    MessageUtils.send_to_player(apply, MarriageMessage.ResWeddingStart.MsgID.eMsgID_VALUE, ws.build().toByteArray());
                    MessageUtils.notify_player(apply, Notify.CHAT_SYS_MARQUEE, MessageString.Marry_Star_Notice, p1.getRolename(), p2.getRolename());
                }
                MessageUtils.send_to_player(marriage.getMarriageId(), MarriageMessage.ResWeddingStart.MsgID.eMsgID_VALUE, ws.build().toByteArray());
                MessageUtils.send_to_player(marriage.getBeMarriageId(), MarriageMessage.ResWeddingStart.MsgID.eMsgID_VALUE, ws.build().toByteArray());

                Player player = Manager.playerManager.getPlayer(marriage.getMarriageId());
                Player bePlayer = Manager.playerManager.getPlayer(marriage.getBeMarriageId());
                actionTask(player, MarryTask.Wedding, 0);
                actionTask(bePlayer, MarryTask.Wedding, 0);
                break;
            }
        }
        //TODO 处理过期宣言
        Iterator<MarryDeclaration> iterator = Manager.marriageManager.getDeclarations().values().iterator();
        while (iterator.hasNext()) {
            MarryDeclaration declaration = iterator.next();
            if (declaration.getTimeout() < TimeUtils.Time()) {
                iterator.remove();
                Manager.saveThreadManager.getOtherServerSave().deal(declaration, DbSqlName.MARRY_DECLARATION_DELETE, SaveServer.DELETE);
            }
        }
    }

    @Override
    public void clear() {
        long mId = Manager.marriageManager.getCurMarriageId();
        if (mId == 0) {
            return;
        }
        ServerParamUtil.weddingList.remove(mId);
        Marriage marriage = Manager.marriageManager.getMarriageList().get(mId);
        log.info(mId + "婚宴即将结束,新婚人:" + marriage.getMarriageId() + "和" + marriage.getBeMarriageId());

        Manager.marriageManager.setCurMarriageId(0);
        Manager.marriageManager.setWedding(null);
        marriage.setPurNum(0);
        marriage.getInviteList().clear();
        marriage.getApplyList().clear();
    }

    @Override
    public void reqMarrySendBulletScreen(Player player, String context) {

        WeddingMapInfo wedding = Manager.marriageManager.getWedding();
        if (wedding == null)
            return;

        for (Map.Entry<Long, Integer> entry : wedding.getWeddingScene().entrySet()) {
            MapObject map = Manager.mapManager.getMap(entry.getKey());
            if (map == null) {
                continue;
            }
            Manager.mapManager.addCommand(new WeddingBulletScreenCommand(map, player, context));
        }
    }

    @Override
    public void reqMarrySendItem(Player player, int index, long roleID) {
        Player target = Manager.playerManager.getPlayer(roleID);
        if (target == null) {
            return;
        }
        int size = Global.Marry_Send_Items.size();
        if (index >= size || index < 0)
            return;
        WeddingMapInfo wedding = Manager.marriageManager.getWedding();
        if (wedding == null) {
            return;
        }
        WeddingOperation weddingOperation = wedding.getOperation().get(player.getId());
        if (weddingOperation == null) {
            return;
        }
        ReadArray<Integer> items = Global.Marry_Send_Items.get(index);

        int id = items.get(0);
        int num = items.get(1);
        int addhot = items.get(2);
        if (!Manager.backpackManager.manager().onRemoveItem(player, id, num, ItemChangeReason.WeddingSendDec, id)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.MyMoneyNotEnough);
            return;
        }
        MessageUtils.notify_player(player, Notify.ERROR, MessageString.Marry_Bless_Succ, Manager.backpackManager.manager().getName(id), num + "");

        wedding.setHot(wedding.getHot() + addhot);

        int len = Global.Marry_Souger_MaxCount.size();
        for (int i = len - 1; i >= 0; i--) {
            ReadArray<Integer> array = Global.Marry_Souger_MaxCount.get(i);
            if (wedding.getHot() >= array.get(0)) {
                if (array.get(1) > wedding.getGatherMax()) {
                    wedding.setGatherMax(array.get(1));
                    break;
                }
            }
        }

        WeddingSendData weddingSendData = new WeddingSendData();
        weddingSendData.setSendName(player.getName());
        weddingSendData.setBeSendName(target.getName());
        weddingSendData.setItemID(id);
        weddingSendData.setNum(num);
        wedding.getWeddingSendDatas().add(weddingSendData);

        for (Map.Entry<Long, Integer> entry : wedding.getWeddingScene().entrySet()) {
            MapObject map = Manager.mapManager.getMap(entry.getKey());
            if (map == null) {
                continue;
            }
            Manager.mapManager.addCommand(new WeddingGiftCommand(map, player, target, id, num));
        }
    }

    public void reqMarryBlessList(Player player) {

        WeddingMapInfo wedding = Manager.marriageManager.getWedding();

        List<WeddingSendData> weddingSendDatas = wedding.getWeddingSendDatas();

        MarriageMessage.ResMarryBlessList.Builder msg = MarriageMessage.ResMarryBlessList.newBuilder();
        MarriageMessage.BlessData.Builder blessdata = null;
        if (weddingSendDatas != null && weddingSendDatas.size() > 0) {
            for (WeddingSendData weddingSendData : weddingSendDatas) {
                blessdata = MarriageMessage.BlessData.newBuilder();
                blessdata.setSendName(weddingSendData.getSendName());
                blessdata.setReceiveName(weddingSendData.getBeSendName());
                blessdata.setItemID(weddingSendData.getItemID());
                blessdata.setNum(weddingSendData.getNum());
                msg.addBlessDataList(blessdata.build());
            }
            MessageUtils.send_to_player(player, MarriageMessage.ResMarryBlessList.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    /**
     * 仙娃出战，召回
     *
     * @param player
     * @param childId
     * @param opt
     */
    @Override
    public void reqChildCall(Player player, int childId, int opt) {

        MarryChild child = player.getChilds().get(childId);
        if (child == null) {
            return;
        }
        if (opt == 0) {
            child.setShow(0);
        } else {
            MarryChild one = Utils.findOne(player.getChilds().values(), o -> o.getShow() == 1);
            if (one != null && !one.equals(child)) {
                one.setShow(0);
                sendMarryChild(player, one, false);
            }
            child.setShow(1);
            player.setChildId(childId);
        }
        sendMarryChild(player, child, false);

        sendChildMap(player, child);
    }

    /**
     * 任务
     *
     * @param player
     * @param task
     * @param param
     */
    @Override
    public void actionTask(Player player, MarryTask task, int param) {
        if (Manager.countManager.getBooleanCountValue(player, task)) {
            return;
        }
        Manager.countManager.setBooleanCountValue(player, task, true);
        sendMarryTask(player);

        log.info("完成仙缘任务 id={} player={}", task.getKey(), player);

        for (MarryTask check : MarryTask.values()) {
            if (check == MarryTask.All || check == MarryTask.None) {
                continue;
            }
            if (!Manager.countManager.getBooleanCountValue(player, check)) {
                return;
            }
        }

        Manager.countManager.setBooleanCountValue(player, MarryTask.All, true);
        sendMarryTask(player);
        log.info("完成仙缘任务 id={} player={}", MarryTask.All.getKey(), player);
    }

    /**
     * 请求仙缘任务列表
     *
     * @param player
     */
    @Override
    public void sendMarryTask(Player player) {

        MarriageMessage.ResMarryTask.Builder message = MarriageMessage.ResMarryTask.newBuilder();
        for (MarryTask check : MarryTask.values()) {
            if (Manager.countManager.getBooleanCountValue(player, check)) {
                message.addTaskId((int) check.getKey());
            }
        }
        for (Cfg_Marry_show_Bean bean : CfgManager.getCfg_Marry_show_Container().getValuees()) {
            boolean state = Manager.countManager.getBooleanCountValue(player, new BooleanCount() {
                @Override
                public long getKey() {
                    return bean.getId();
                }

                @Override
                public VariantType getVariantType() {
                    return VariantType.MarryTaskReward;
                }
            });
            if (state) {
                message.addOverId(bean.getId());
            }
        }
        MessageUtils.send_to_player(player, MarriageMessage.ResMarryTask.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 请求领取任务奖励
     *
     * @param player
     * @param taskId
     */
    @Override
    public void reqMarryTaskReward(Player player, int taskId) {

        Cfg_Marry_show_Bean bean = CfgManager.getCfg_Marry_show_Container().getValueByKey(taskId);

        MarryTask task = MarryTask.find(taskId);

        if (Manager.countManager.getBooleanCountValue(player, new BooleanCount() {
            @Override
            public long getKey() {
                return taskId;
            }

            @Override
            public VariantType getVariantType() {
                return VariantType.MarryTaskReward;
            }
        })) {
            return;
        }
        if (Manager.countManager.getBooleanCountValue(player, task)) {
            Manager.countManager.setBooleanCountValue(player, new BooleanCount() {
                @Override
                public long getKey() {
                    return taskId;
                }

                @Override
                public VariantType getVariantType() {
                    return VariantType.MarryTaskReward;
                }
            }, true);

            List<Item> items = Item.createItems(bean.getReward());
            if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.MarryTaskGet, IDConfigUtil.getLogId())) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                        MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.MarryTaskGet);
            }
            sendMarryTask(player);
        }
    }

    /**
     * 使用道具
     *
     * @param player
     * @param id
     */
    public void reqMarryUseItem(Player player, int id) {
        Cfg_Marry_shop_Bean bean = CfgManager.getCfg_Marry_shop_Container().getValueByKey(id);
        if (bean == null || bean.getHot() <= 0) {
            return;
        }
        Cfg_Item_Bean item_bean = CfgManager.getCfg_Item_Container().getValueByKey(id);
        if (item_bean == null) {
            return;
        }
        WeddingMapInfo wedding = Manager.marriageManager.getWedding();
        if (wedding == null) {
            return;
        }
        WeddingOperation weddingOperation = wedding.getOperation().get(player.getId());
        if (weddingOperation == null) {
            return;
        }
        boolean isBigFire = bean.getExp_index() == 1;
        int useCount = isBigFire ? weddingOperation.getUseBigFireCount() : weddingOperation.getUseSamllFireCount();
        if (useCount >= bean.getUse_max()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Marry_Fire_Little_Limit, Manager.backpackManager.manager().getName(id));
            return;
        }
        if (!Manager.backpackManager.manager().onRemoveItem(player, id, 1, ItemChangeReason.WeddingUseDec, id)) {
            log.error("使用失败道具不存在  " + id);
            return;
        }
        doItemEffect(player, id);
    }

    /**
     * 使用道具
     *
     * @param player
     * @param id
     */
    public void doItemEffect(Player player, int id) {

        WeddingMapInfo wedding = Manager.marriageManager.getWedding();

        WeddingOperation weddingOperation = wedding.getOperation().get(player.getId());

        Cfg_Marry_shop_Bean bean = CfgManager.getCfg_Marry_shop_Container().getValueByKey(id);
        //掉落
        if (bean.getDrop_item() > 0) {
            List<List<Integer>> itemDrops = Manager.dropManager.deal().getItemDrops(player, bean.getDrop_item());
            List<Item> list = Item.createItems(itemDrops, 1);
            if (!Manager.backpackManager.manager().addItems(player, list, ItemChangeReason.WeddingUseDec, IDConfigUtil.getLogId())) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail,
                        MessageString.System, MessageString.BAGISSPACETOMAIL, MessageString.GetAwardNotEnoughSpaceContent, list, ItemChangeReason.WeddingUseDec);
            }
        }

        //加经验
        Cfg_Characters_Bean characters_bean = CfgManager.getCfg_Characters_Container().getValueByKey(player.getLevel());
        if (characters_bean != null) {
            ReadArray<Long>[] readArray = characters_bean.getMarry_use_item_add_exp().getValuees();
            for (ReadArray<Long> arr : readArray) {
                if (arr.get(0) == id) {
                    Manager.currencyManager.manager().addEXP(player, arr.get(1), ItemChangeReason.WeddingUseDec, id);
                    break;
                }
            }
        }
        //加热度
        if (bean.getExp_index() == 1) {
            weddingOperation.setUseBigFireCount(weddingOperation.getUseBigFireCount() + 1);
        } else {
            weddingOperation.setUseSamllFireCount(weddingOperation.getUseSamllFireCount() + 1);
        }

        wedding.setHot(wedding.getHot() + bean.getHot());

        int size = Global.Marry_Souger_MaxCount.size();
        for (int i = size - 1; i >= 0; i--) {
            ReadArray<Integer> array = Global.Marry_Souger_MaxCount.get(i);
            if (wedding.getHot() >= array.get(0)) {
                if (array.get(1) > wedding.getGatherMax()) {
                    wedding.setGatherMax(array.get(1));
                    break;
                }
            }
        }

        for (Map.Entry<Long, Integer> entry : wedding.getWeddingScene().entrySet()) {
            MapObject map = Manager.mapManager.getMap(entry.getKey());
            if (map == null) {
                continue;
            }
            Manager.mapManager.addCommand(new WeddingUseGiftCommand(map, player, id));
        }
    }

    public void reqMarryCopyBuy(Player player, int id, int num) {

        Cfg_Marry_shop_Bean bean = CfgManager.getCfg_Marry_shop_Container().getValueByKey(id);
        if (bean == null)
            return;

        Item item = Item.createItem(id, num, true);
        if (!Manager.backpackManager.manager().onHasAddSpace(player, item)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.NoBagCell);
            return;
        }

        if (!Manager.currencyManager.manager().onDecItemCoin(player, bean.getPrice().get(1) * num, ItemChangeReason.WeddingBuyDec, bean.getId(), bean.getPrice().get(0))) {
            return;
        }


        Cfg_Item_Bean item_bean = CfgManager.getCfg_Item_Container().getValueByKey(id);
        if (item_bean != null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Marry_Shop_Buy_Notice, Manager.backpackManager.manager().getName(id), num + "");
        }
        Manager.backpackManager.manager().addItem(player, item, ItemChangeReason.WeddingBuyGet, id);
        //情缘商城BI
        Manager.biManager.getScript().biMall(player, 0, 9, id, num,
                bean.getPrice().get(0), bean.getPrice().get(1) * num, bean.getPrice().get(1), 0);
    }

    private int getMaxNum(Marriage marriage) {
        int num = 0;
        for (Integer i : marriage.gettList()) {
            Cfg_Marry_dinner_Bean bean = CfgManager.getCfg_Marry_dinner_Container().getValueByKey(i);
            num = num + bean.getOrderTime();
        }
        return num;
    }

    public PlayerWorldInfo getMarraige1(Player player, Marriage marriage) {
        long roleId = 0;

        if (marriage.getMarriageId() == player.getId()) {
            roleId = marriage.getBeMarriageId();
        }

        if (marriage.getBeMarriageId() == player.getId()) {
            roleId = marriage.getMarriageId();
        }

        return Manager.playerManager.getPlayerWorldInfo(roleId);
    }

    public PlayerWorldInfo getMarraige(long roleId) {
        return Manager.playerManager.getPlayerWorldInfo(roleId);
    }

    public int getMarrigeDay(Marriage marriage) {
        int day = TimeUtils.getBetweenDays(TimeUtils.Time(), marriage.getMarriageTime());
        if (day == 0) {
            return 1;
        }
        return day;
    }

    //////////////////////////仙缘日志////////////////////////////

    /**
     * TODO 写入婚姻日志
     *
     * @param marriage
     * @param opt      //1:结婚  2：预定婚宴 3:离婚
     */
    void writeMarryLog(Marriage marriage, int opt) {
        MarryLog log = new MarryLog();
        log.setMarryId(marriage.getId());
        log.setPlayerId(marriage.getMarriageId());
        log.setPartnerId(marriage.getBeMarriageId());
        log.setOpt(opt);

        LogService.getInstance().execute(log);
    }

    /**
     * TODO 写入心锁日志
     *
     * @param player
     * @param opt    1= 激活 2=强化
     */
    void writeMarryLockLog(Player player, int opt) {
        MarryLoveLockLog log = new MarryLoveLockLog();
        log.setPlayerId(player.getId());
        log.setOpt(opt);
        log.setGrade(player.getMarryLock().getGrade());
        log.setStage(player.getMarryLock().getStage());
        log.setExp(player.getMarryLock().getExp());
        LogService.getInstance().execute(log);
    }

    /**
     * TODO 写入仙娃日志
     *
     * @param player
     * @param opt    1= 激活 2=强化 3=改名
     */
    void writeMarryChildLog(Player player, MarryChild child, int opt) {
        MarryChildLog log = new MarryChildLog();
        log.setPlayerId(player.getId());
        log.setChildId(child.getId());
        log.setName(child.getName());
        log.setLevel(child.getLevel());
        log.setExp(child.getExp());
        log.setOpt(opt);
        LogService.getInstance().execute(log);
    }

    /**
     * TODO 写入 仙匣日志
     *
     * @param player
     */
    void writeMarryBoxLog(Player player, Player parter) {
        MarryBoxLog log = new MarryBoxLog();
        log.setPlayerId(player.getId());
        log.setBuyId(parter.getId());
        log.setTimeout(player.getMarryBox().getTime());
        LogService.getInstance().execute(log);
    }

    /**
     * 发展关系
     *
     * @param player
     * @param roleId
     */
    @Override
    public void reqMarryAddFriend(Player player, long roleId) {
        if (player.getId() == roleId) {
            return;
        }
        Player target = Manager.playerManager.getPlayerOnline(roleId);
        if (target == null) {
            PlayerWorldInfo worldInfo = Manager.playerManager.getPlayerWorldInfo(roleId);
            if (worldInfo == null) {
                return;
            }
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.PlayerNotOnline, worldInfo.getRolename());
            return;
        }

        PlayerRelation selfRelation = Manager.friendManager.getPlayerRelation(player.getId());
        PlayerRelation targetRelation = Manager.friendManager.getPlayerRelation(target.getId());

        if (selfRelation.getFriends().containsKey(target.getId()) && targetRelation.getFriends().containsKey(player.getId())) {
            MessageUtils.notify_player(target, Notify.ERROR, MessageString.ERROR_PLAYERRELATION_MESS_FRIEND, target.getName());
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.Marry_System_Tips7);
            return;
        }

        MessageUtils.notify_player(player, Notify.NORMAL, MessageString.Marry_Friend_Success);

        MarriageMessage.ResMarryAddFriendNotify.Builder builder = MarriageMessage.ResMarryAddFriendNotify.newBuilder();
        builder.setRoleId(player.getId());
        builder.setRoleName(player.getName());
        MessageUtils.send_to_player(target, MarriageMessage.ResMarryAddFriendNotify.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 发展关系 回复
     *
     * @param player
     * @param roleId
     * @param opt
     */
    @Override
    public void reqMarryAddFriendOpt(Player player, long roleId, int opt) {
        if (player.getId() == roleId) {
            return;
        }
        Player target = Manager.playerManager.getPlayer(roleId);
        if (target == null) {
            return;
        }
        if (opt == 0) {
            //TODO 拒绝添加
            MessageUtils.notify_player(target, Notify.NORMAL, MessageString.Marry_Friend_Refuse, player.getName());
            return;
        }
        //TODO 同意添加
        PlayerRelation selfRelation = Manager.friendManager.getPlayerRelation(player.getId());
        PlayerRelation targetRelation = Manager.friendManager.getPlayerRelation(target.getId());

        if (selfRelation.getFriends().size() >= Global.FriendMax) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.FriendMaxSize);
            return;
        }
        if (targetRelation.getFriends().size() >= Global.FriendMax) {
            MessageUtils.notify_player(target, Notify.ERROR, MessageString.FriendMaxSize);
            return;
        }
        //TODO 检测敌对
        if (selfRelation.getEnemies().containsKey(target.getId())) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.FRIENDNOTSAMEGROUP);
            return;
        }
        if (targetRelation.getEnemies().containsKey(player.getId())) {
            MessageUtils.notify_player(target, Notify.ERROR, MessageString.FRIENDNOTSAMEGROUP);
            return;
        }

        //TODO 开始添加好友
        if (selfRelation.getFriends().containsKey(target.getId())) {
            MessageUtils.notify_player(target, Notify.ERROR, MessageString.ERROR_PLAYERRELATION_MESS_FRIEND, target.getName());
        } else {
            Manager.friendManager.deal().addFriend(player, target.getId());
        }
        if (targetRelation.getFriends().containsKey(player.getId())) {
            MessageUtils.notify_player(target, Notify.ERROR, MessageString.ERROR_PLAYERRELATION_MESS_FRIEND, player.getName());
        } else {
            Manager.friendManager.deal().addFriend(target, player.getId());
        }
    }

    /**
     * 请求相亲墙数据
     *
     * @param player
     */
    @Override
    public void reqMarryWallDeclaration(Player player) {

        MarriageMessage.ResMarryWallInfo.Builder builder = MarriageMessage.ResMarryWallInfo.newBuilder();
        MarriageMessage.MarryDeclaration.Builder mDec = MarriageMessage.MarryDeclaration.newBuilder();

        for (MarryDeclaration dec : Manager.marriageManager.getDeclarations().values()) {
            PlayerWorldInfo worldInfo = Manager.playerManager.getPlayerWorldInfo(dec.getRoleId());
            if (worldInfo == null) {
                continue;
            }
            boolean friend = Manager.friendManager.deal().isRealFriend(player, worldInfo.getRoleid());
            mDec.setRoleId(dec.getRoleId());
            mDec.setDeclarationId(dec.getDeclarationId());
            mDec.setCareer(worldInfo.getCareer());
            mDec.setLevel(worldInfo.getLevel());
            mDec.setName(worldInfo.getRolename());
            mDec.setGuildId(worldInfo.getGuildId());
            mDec.setVip(worldInfo.getPlayerVip());
            mDec.setFriend(friend);
            mDec.setHead(MapUtils.getHead(worldInfo));
            mDec.setOnline(Manager.playerManager.isOnline(dec.getRoleId()));
            builder.addDeclarations(mDec);
        }

        MessageUtils.send_to_player(player, MarriageMessage.ResMarryWallInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 领取 缘定三生礼包
     *
     * @param player
     */
    @Override
    public void reqMarryWallReward(Player player) {
        if (player.getLevel() < Global.Marry_Wall_Award_Level) {
            return;
        }
        if (Manager.countManager.getVariant(player, VariantType.MarryWallReward) > 0) {
            return;
        }
        //TODO 发放奖励
        List<Item> list = Item.createItems(Global.Marry_Wall_Award_Items, 1);
        if (!Manager.backpackManager.manager().addItems(player, list, ItemChangeReason.MarryWallGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail,
                    MessageString.System, MessageString.BAGISSPACETOMAIL, MessageString.GetAwardNotEnoughSpaceContent, list, ItemChangeReason.MarryWallGet);
        }
        Manager.countManager.addVariant(player, VariantType.MarryWallReward, 1);

        sendMarryWallRewardState(player);
        //记录BI数据 1894是Marry_Wall_Award_Items在global表里面的id
//        Manager.biManager.getScript().biActivity(player, ItemChangeReason.MarryWall, BIActiityTypeEnum.WEDDING.getId(), 1894);
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.XFHD_JOTC, ItemChangeReason.MarryWallGet);
    }

    //TODO 发送奖励状态
    void sendMarryWallRewardState(Player player) {
        MarriageMessage.ResMarryWallRewardInfo.Builder builder = MarriageMessage.ResMarryWallRewardInfo.newBuilder();
        long variant = Manager.countManager.getVariant(player, VariantType.MarryWallReward);
        builder.setHaveReward(variant == 0 ? 1 : 0);
        long cd = Manager.countManager.getVariant(player, VariantType.MarryDeclarationCD);
        if (cd > TimeUtils.Time()) {
            builder.setCd((int) ((cd - TimeUtils.Time() + 999) / 1000));
        } else {
            builder.setCd(0);
        }
        MessageUtils.send_to_player(player, MarriageMessage.ResMarryWallRewardInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 发布爱情宣言
     *
     * @param player
     * @param declarationId
     */
    @Override
    public void reqPushMarryDeclaration(Player player, int declarationId) {

        long cd = Manager.countManager.getVariant(player, VariantType.MarryDeclarationCD);
        if (cd > TimeUtils.Time()) {
            return;
        }
        Cfg_Marry_wall_Bean bean = CfgManager.getCfg_Marry_wall_Container().getValueByKey(declarationId);
        if (bean == null) {
            return;
        }
        long logId = IDConfigUtil.getLogId();
        //TODO 豪华宣言
        if (bean.getCost_item().size() >= 2) {
            Integer costId = bean.getCost_item().get(0);
            Integer count = bean.getCost_item().get(1);
            if (!Manager.backpackManager.manager().onRemoveItem(player, costId, count, ItemChangeReason.MarryWallDec, logId)) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Item_Not_Enough, Manager.backpackManager.manager().getName(costId));
                return;
            }

            MessageUtils.notify_allOnlinePlayer(Notify.CHAT, MessageString.Marry_Declaration_Notice, player.getName());
        }

        Manager.countManager.setVariant(player, VariantType.MarryDeclarationCD, TimeUtils.Time() + Global.Marry_Wall_Send_CDTime * 60 * 1000l);
        //TODO 发放奖励
        List<Item> list = Item.createItems(Global.Marry_Wall_Send_Award, 1);
        if (!Manager.backpackManager.manager().addItems(player, list, ItemChangeReason.PushMarryDeclarationGet, logId)) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail,
                    MessageString.System, MessageString.BAGISSPACETOMAIL, MessageString.GetAwardNotEnoughSpaceContent, list, ItemChangeReason.PushMarryDeclarationGet);
        }

        MarryDeclaration declaration = new MarryDeclaration();
        declaration.setRoleId(player.getId());
        declaration.setTimeout(TimeUtils.Time() + DeclarationTimeout);
        declaration.setDeclarationId(declarationId);

        Manager.marriageManager.getDeclarations().put(declaration.getRoleId(), declaration);
        sendMarryWallRewardState(player);
        Manager.saveThreadManager.getOtherServerSave().deal(declaration, DbSqlName.MARRY_DECLARATION_INSERT_OR_UPDATE, SaveServer.INSERT);

        log.info("发布爱情宣言 decId={} player={}", declarationId, player);
        //TODO 发布成功， 重新push
        reqMarryWallDeclaration(player);

        Manager.biManager.getInstance().getScript().biActivity(player, BIActiityTypeEnum.XFHD_JOTC, ItemChangeReason.PushMarryDeclarationGet);
    }

    /**
     * 热度礼包购买
     *
     * @param player
     */
    public void reqMarryCopyBuyHot(Player player) {

        MapObject mapObject = Manager.mapManager.getMap(player.gainMapId());
        //玩家不在地图中
        if (mapObject == null) {
            return;
        }

        WeddingMapInfo wedding = Manager.marriageManager.getWedding();
        //玩家结婚信息为空
        if (wedding == null) {
            return;
        }
        //
        if (player.getId() == wedding.getWifeID()) {
            if (wedding.getWifeIsBuyHot()) {
                return;
            }
        } else if (player.getId() == wedding.getHubbyID()) {
            if (wedding.getHubbyIsBuyHot()) {
                return;
            }
        }
        int modelId = Global.Marry_copy_buy_hot.get(0);
        int num = Global.Marry_copy_buy_hot.get(1);
        if (!Manager.backpackManager.manager().removeItemOrCurrency(player, modelId, num, IDConfigUtil.getLogId(), ItemChangeReason.MarryCopyBuyHotDes)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ItemNotEnough);
            return;
        }

        if (player.getId() == wedding.getWifeID()) {
            wedding.setWifeIsBuyHot(true);
        } else if (player.getId() == wedding.getHubbyID()) {
            wedding.setHubbyIsBuyHot(true);
        }
        //获得热度
        int newHot = wedding.getHot() + Global.Marry_copy_buy_hot.get(2);
        wedding.setHot(newHot);
        int size = Global.Marry_Souger_MaxCount.size();
        for (int i = size - 1; i >= 0; i--) {
            ReadArray<Integer> array = Global.Marry_Souger_MaxCount.get(i);
            if (wedding.getHot() >= array.get(0)) {
                if (array.get(1) > wedding.getGatherMax()) {
                    wedding.setGatherMax(array.get(1));
                    break;
                }
            }
        }

        for (Map.Entry<Long, Integer> entry : wedding.getWeddingScene().entrySet()) {
            MapObject map = Manager.mapManager.getMap(entry.getKey());
            if (map == null) {
                continue;
            }
            Manager.mapManager.addCommand(new WeddingBuyHotCommand(map, player));
        }

    }

    /**
     * 婚姻签到请求
     *
     * @param player
     */
    public void reqMarryCopySign(Player player) {
        MapObject mapObject = Manager.mapManager.getMap(player.gainMapId());
        //玩家不在地图中
        if (mapObject == null) {
            return;
        }
        WeddingMapInfo wedding = Manager.marriageManager.getWedding();
        //玩家结婚信息为空
        if (wedding == null) {
            return;
        }
        WeddingOperation operation = wedding.getOperation().get(player.getId());
        if (operation == null) {
            operation = new WeddingOperation();
            wedding.getOperation().put(player.getId(), operation);
        }
        //已经签到了
        if (operation.isMarryCopySign()) {
            return;
        }
        //TODO 发放奖励
        List<Item> list = Item.createItems(Global.Marry_copy_sign_award, 1);
        if (!Manager.backpackManager.manager().addItems(player, list, ItemChangeReason.MarryCopySigRewardGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail,
                    MessageString.System, MessageString.BAGISSPACETOMAIL, MessageString.GetAwardNotEnoughSpaceContent, list, ItemChangeReason.MarryCopySigRewardGet);
        }
        operation.setMarryCopySign(true);
    }

    /**
     * 送祝福
     *
     * @param player
     * @param marryId
     */
    @Override
    public void reqMarryBless(Player player, long marryId) {

        Marriage marriage = Manager.marriageManager.getMarriageList().get(marryId);
        if (marriage == null) {
            return;
        }
        if (marriage.getBlessList().contains(player.getId())) {
            return;
        }
        marriage.getBlessList().add(player.getId());

        //TODO 发放奖励
        List<Item> list = Item.createItems(Global.Marry_pray_reward, 1);
        if (!Manager.backpackManager.manager().addItems(player, list, ItemChangeReason.MarryBlessGiftGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail,
                    MessageString.System, MessageString.BAGISSPACETOMAIL, MessageString.GetAwardNotEnoughSpaceContent, list, ItemChangeReason.MarryBlessGiftGet);
        }

    }
}
