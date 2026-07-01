package common.attribute;

import com.data.CfgManager;
import com.data.FunctionVariable;
import com.data.bean.Cfg_AttributeAdd_Bean;
import com.data.bean.Cfg_Item_warning_Bean;
import com.data.bean.Cfg_Occ_Skill_Bean;
import com.data.bean.Cfg_Skill_Bean;
import com.game.attribute.AttributeUtils;
import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseLongAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.manager.Manager;
import com.game.map.structs.MapUtils;
import com.game.player.log.PlayerAttChangeLog;
import com.game.player.log.PlayerFightPointChangeLog;
import com.game.player.script.IPlayerAttribute;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.robot.struct.Robot;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.skill.structs.MentalSkill;
import com.game.skill.structs.Skill;
import com.game.structs.AttributeType;
import com.game.utils.MessageUtils;
import game.core.dblog.LogService;
import game.core.util.JsonUtils;
import game.message.CrossFightMessage;
import game.message.MapMessage;
import game.message.PlayerMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Desc TODO
 * @Date 2020/11/2 17:21
 * @Auth ZUncle
 */
public class AttributeScript implements IPlayerAttribute {

    static final Logger logger = LogManager.getLogger("AttributeLog");
    static final Logger local = LogManager.getLogger(AttributeScript.class);

    //主-副属性加成计算
    HashMap<PlayerAttributeType, int[][]> calc = new HashMap<>();
    int[][] copyHorseAttribute = {
            //坐骑副属性
            {AttributeType.ATTR_Horse_Attack, AttributeType.ATTR_Atk},
            {AttributeType.ATTR_Horse_Hp, AttributeType.ATTR_MaxHp},
            {AttributeType.ATTR_Horse_DefBreak, AttributeType.ATTR_DefBreak},
            {AttributeType.ATTR_Horse_Defence, AttributeType.ATTR_Def},
    };
    int[][] copyWingAttribute = {
            //翅膀副属性
            {AttributeType.ATTR_Wing_Attack, AttributeType.ATTR_Atk},
            {AttributeType.ATTR_Wing_Hp, AttributeType.ATTR_MaxHp},
            {AttributeType.ATTR_Wing_DefBreak, AttributeType.ATTR_DefBreak},
            {AttributeType.ATTR_Wing_Defence, AttributeType.ATTR_Def},
    };
    int[][] copyTalismanAttribute = {
            //法器副属性
            {AttributeType.ATTR_Talisman_Attack, AttributeType.ATTR_Atk},
            {AttributeType.ATTR_Talisman_HP, AttributeType.ATTR_MaxHp},
            {AttributeType.ATTR_Talisman_DefBreak, AttributeType.ATTR_DefBreak},
            {AttributeType.ATTR_Talisman_Defence, AttributeType.ATTR_Def},
    };
    int[][] copyMagicAttribute = {
            //灵阵副属性
            {AttributeType.ATTR_Magic_Hit, AttributeType.ATTR_Hit},
            {AttributeType.ATTR_Magic_HitBreak, AttributeType.ATTR_HitBreak},
            {AttributeType.ATTR_Magic_Critical, AttributeType.ATTR_Critical},
            {AttributeType.ATTR_Magic_Resilience, AttributeType.ATTR_Resilience},
    };
    int[][] copyEquipAttribute = {
            //装备副属性
            {AttributeType.ATTR_EquipBase_Atk, AttributeType.ATTR_Atk},
            {AttributeType.ATTR_EquipBase_Hp, AttributeType.ATTR_MaxHp},
            {AttributeType.ATTR_EquipBase_DefBreak, AttributeType.ATTR_DefBreak},
            {AttributeType.ATTR_EquipBase_Def, AttributeType.ATTR_Def},

    };

    public AttributeScript() {
        calc.put(PlayerAttributeType.HORSE, copyHorseAttribute);
        calc.put(PlayerAttributeType.WING, copyWingAttribute);
        calc.put(PlayerAttributeType.Talisman, copyTalismanAttribute);
        calc.put(PlayerAttributeType.Magic, copyMagicAttribute);
        calc.put(PlayerAttributeType.EQUIP, copyEquipAttribute);
    }

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.AttributeScript;
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


    private void checkPlayerAttWarning(Player player) {
        for (Cfg_Item_warning_Bean bean : CfgManager.getCfg_Item_warning_Container().getValuees()) {
            if (bean.getItemId() > 0) {
                continue;
            }

            int key = 0 - bean.getItemId();

            if (key < 0) {
                continue;
            }
            if (bean.getPlayerLimit() < 1) {
                continue;
            }

            long value = player.getAttribute().getAdditionValue(key);
            if (value < 0) {
                logger.warn("playerAttMaxWarning11", player.nameIdString() + "的属性（" + key + "）的值是:" + value + "异常");
                continue;
            }

            if (value >= bean.getPlayerLimit()) {
                GameServer.getInstance().setErrorLog("playerAttMaxWarning", player.nameIdString() + "的属性（" + key + "）的值是:" + value + "异常");
            }
        }
    }

    /**
     * 计算战力
     *
     * @param att
     * @return
     */
    public int calcFightPower(BaseIntAttribute att) {
        double fighting = 0;
        for (int type : AttributeType.FIGHT_POWER) {
            if (att.getAdditionValue(type) < 1) {
                continue;
            }
            Cfg_AttributeAdd_Bean bean = CfgManager.getCfg_AttributeAdd_Container().getValueByKey(type);
            fighting += att.getAdditionValue(type) * (bean.getVariable() / 10000f);
        }
        Cfg_AttributeAdd_Bean bean = CfgManager.getCfg_AttributeAdd_Container().getValueByKey(AttributeType.ATTR_MaxHp);
        fighting += att.MaxHP() * (bean.getVariable() / 10000f);
        return (int) fighting;
    }

    /**
     * 计算战力
     *
     * @param att
     * @return
     */
    public long calcFightPower(BaseLongAttribute att) {
        double fighting = 0;
        for (int type : AttributeType.FIGHT_POWER) {
            if (att.getAdditionValue(type) < 1) {
                continue;
            }
            //血量的战力也是要计算的
            if (type == AttributeType.ATTR_MaxHp) {
                continue;
            }
            Cfg_AttributeAdd_Bean bean = CfgManager.getCfg_AttributeAdd_Container().getValueByKey(type);
            fighting += att.getAdditionValue(type) * (bean.getVariable() / 10000f);
        }
        Cfg_AttributeAdd_Bean bean = CfgManager.getCfg_AttributeAdd_Container().getValueByKey(AttributeType.ATTR_MaxHp);
        fighting += att.MaxHP() * (bean.getVariable() / 10000f);
        return (long) fighting;
    }

    @SuppressWarnings("deprecation")
    protected BaseLongAttribute sumAttribute(Player player) {
        BaseLongAttribute fightBaseAttribute = player.getAttribute();
        AttributeUtils.clean(fightBaseAttribute);

        sumAttribute(player, fightBaseAttribute);
        fightBaseAttribute.calFinalAttackSpeed();
        fightBaseAttribute.calFinalMoveSpeed();

        if (player.getCurHp() > fightBaseAttribute.MaxHP()) {
            player.setCurHp(fightBaseAttribute.MaxHP());
            player.onHpChange(null);
        }

        return fightBaseAttribute;
    }

    @SuppressWarnings("deprecation")
    protected BaseSystemIntAttribute sumSystemAttribute(Player player) {
        BaseSystemIntAttribute sysAttriBute = player.getSysAttriBute();
        AttributeUtils.clean(sysAttriBute);
        for (int type = 1; type < AttributeType.SystemAttr_Max; type++) {
            int base = 0;
            for (PlayerAttributeType attributeType : PlayerAttributeType.values()) {
                BaseSystemIntAttribute attr = getSystemAttribute(player, attributeType);
                int value = attr.getAttribute(type);
                base += value;
            }
            sysAttriBute.setAttribute(type, base);
        }
        return sysAttriBute;
    }

    /**
     * 获取单个
     *
     * @param player
     * @param type
     * @return
     */
    public BaseIntAttribute getAttribute(Player player, PlayerAttributeType type) {
        BaseIntAttribute sum = new BaseIntAttribute(AttributeType.ATTR_MAX);
        BaseIntAttribute attribute = player.PlayerCalculators().get(type);
        if (attribute == null) {
            return sum;
        }
        AttributeUtils.copyAttribute(sum, attribute);
        return sum;
    }

    /**
     * 获取单个系统属性
     * @param player
     * @param type
     * @return
     */
    public BaseSystemIntAttribute getSystemAttribute(Player player, PlayerAttributeType type) {
        if (!player.PlayerCalSystemCulators().containsKey(type)) {
            BaseSystemIntAttribute baseSystemIntAttribute = new BaseSystemIntAttribute(AttributeType.SystemAttr_Max);
            player.PlayerCalSystemCulators().put(type, baseSystemIntAttribute);
        }
        return player.PlayerCalSystemCulators().get(type);
    }

    /**
     * 计算玩家战斗力
     *
     * @param player
     * @param sendToClient 是否通知客户端
     * @return
     */
    private long calPlayerFighting(Player player, boolean sendToClient, boolean sycRank) {
        //TODO 开始计算玩家非战力
        BaseLongAttribute fightPower = player.getFightPowerAttribute();
        AttributeUtils.clean(fightPower);

        sumAttribute(player, fightPower, PlayerAttributeType.BUFF);

        long fighting = calcFightPower(fightPower);

        //计算技能战斗力
        List<Skill> skills = new ArrayList<>(player.getSkills().values());
        for (Skill skill : skills) {
            Cfg_Skill_Bean bean = CfgManager.getCfg_Skill_Container().getValueByKey(skill.getSkillId());
            if (bean == null) {
                continue;
            }
            fighting += bean.getFight_num() * skill.getLevel();
        }
        //心法技能战力计算
        for (MentalSkill mentalSkill : player.getMentalskills().values()) {
            Cfg_Occ_Skill_Bean occSkill_bean = CfgManager.getCfg_Occ_Skill_Container().getValueByKey(mentalSkill.getBaseSkillID());
            if (occSkill_bean != null) {
                fighting += occSkill_bean.getFight_num();
            }
            for (Integer occid : mentalSkill.getSkillID().values()) {
                occSkill_bean = CfgManager.getCfg_Occ_Skill_Container().getValueByKey(occid);
                if (occSkill_bean == null)
                    continue;
                fighting += occSkill_bean.getFight_num();
            }
        }

        if (sycRank) {
            player.setFightPoint(fighting);
            Manager.rankListManager.deal().setFightPower(player, fighting);
            Manager.playerManager.manager().syncPlayerWorldInfo(player, false);
        }

        Manager.controlManager.operate(player, FunctionVariable.PlayerPower, 0);
//        LOGGER.error(player.nameIdString() + " 属性：" + attpower + ", 技能：" + skillpower + ", total=" + player.getFightPoint());
        if (sendToClient) {
            sendResPlayerFightPointChange(player, player.getFightPoint());
        }
        return player.getFightPoint();
    }

    private void sendResPlayerFightPointChange(Player player, long fightPoint) {
        //如果是跨服则不重新计算属性
        if (GameServer.getInstance().IsFightServer()) {
            return;
        }
        PlayerMessage.ResPlayerFightPointChange.Builder msg = PlayerMessage.ResPlayerFightPointChange.newBuilder();
        msg.setFightPoint(fightPoint);
        msg.setType(0);
        MessageUtils.send_to_player(player, PlayerMessage.ResPlayerFightPointChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 计算机器人的属性
     *
     * @param robot
     * @param petFightPower
     */
    public void CountBaseRobot(Robot robot, long petFightPower) {
        BaseLongAttribute fightBaseAttribute = robot.getAttribute();
        fightBaseAttribute.clean();
        fightBaseAttribute.cleanMaxHP();
        for (int[] array : AttributeType.ATTR_FIX) {
            int type = array[0];
            int percentType = array[1];
            int base = 0;
            int basePercent = 0;
            // 基本值属性累加计算
            for (PlayerAttributeType attributeType : PlayerAttributeType.values()) {
                // 获取该资源线的属性
                BaseIntAttribute attr = robot.getPlayerCalculators().get(attributeType);
                if (attr == null) {
                    continue;
                }

                // 获取属性里具体的值
                int value = attr.getAdditionValue(type);
                int valuePercent = 0;
                if (percentType != 0) {
                    valuePercent = attr.getAdditionValue(percentType);
                }
                base += value;
                basePercent += valuePercent;
            }
            fightBaseAttribute.setAttribute(percentType, basePercent);
            //玩家的总血量处理
            if (type == AttributeType.ATTR_MaxHp) {
                fightBaseAttribute.addMaxHP((long) (base * (1 + basePercent / 10000.00d)));
            } else {
                fightBaseAttribute.setAttribute(type, (long) (base * (1 + basePercent / 10000.00d)));
            }
        }

        for (int type : AttributeType.ATTR_NO_FIX) {
            // 基本值属性累加计算
            int base = 0;
            for (PlayerAttributeType attributeType : PlayerAttributeType.values()) {
                BaseIntAttribute attr = robot.getPlayerCalculators().get(attributeType);
                if (attr == null) {
                    continue;
                }
                int value = attr.getAdditionValue(type);
                base += value;
            }
            fightBaseAttribute.setAttribute(type, base);
        }

        // 计算属性计算公式：当前角色属性*属性比例， 当前角色属性包含;characters中当前级别的属性+changejob中转职的本级属性
        countBaseAddPer(fightBaseAttribute, robot.getPlayerCalculators().get(PlayerAttributeType.BASE));

        long fighting = calcFightPower(fightBaseAttribute);
        //计算技能战斗力
        for (Skill skill : robot.getSkills().values()) {
            Cfg_Skill_Bean bean = CfgManager.getCfg_Skill_Container().getValueByKey(skill.getSkillId());
            if (bean == null) {
                continue;
            }
            fighting += bean.getFight_num() * skill.getLevel();
        }

        //计算玩家出战宠物的战斗力
        robot.setFightPower(fighting + petFightPower);
    }

    private void countBaseAddPer(BaseLongAttribute fightBaseAttribute, BaseIntAttribute attr) {
        // 计算属性计算公式：当前角色属性*属性比例， 当前角色属性包含;characters中当前级别的属性+changejob中转职的本级属性
        for (int[] array : AttributeType.ATTR_Add_FIX) {
            //加层类型
            int percentType = array[0];
            //增加的属性
            int type = array[1];
            if (type == AttributeType.ATTR_MaxHp) {
                //玩家的总血量处理
                fightBaseAttribute.addMaxHP((long) (attr.getAdditionValue(type) * (fightBaseAttribute.getAdditionValue(percentType) / 10000.00d)));
            } else {
                fightBaseAttribute.addAttribute(type, (long) (attr.getAdditionValue(type) * fightBaseAttribute.getAdditionValue(percentType) / 10000.00d));
            }
        }
    }

    boolean haveType(PlayerAttributeType check, PlayerAttributeType... ext) {
        for (PlayerAttributeType type : ext) {
            if (type == check) {
                return true;
            }
        }
        return false;
    }

    /**
     * 计算玩家属性值
     *
     * @param player
     * @param fightBaseAttribute
     * @see "这个方法是要被override的，需要对一些属性比如最大血量进行赋值"
     */
    @Override
    public void sumAttribute(Player player, BaseLongAttribute fightBaseAttribute, PlayerAttributeType... ext) {
        fightBaseAttribute.cleanMaxHP();
        for (int[] array : AttributeType.ATTR_FIX) {
            int type = array[0];
            int percentType = array[1];
            int base = 0;
            int basePercent = 0;
            // 基本值属性累加计算
            for (PlayerAttributeType attributeType : PlayerAttributeType.values()) {
                if (haveType(attributeType, ext)) {
                    continue;
                }
                BaseIntAttribute attr = getAttribute(player, attributeType);
                // 获取属性里具体的值
                int value = attr.getAdditionValue(type);
                int valuePercent = attr.getAdditionValue(percentType);
                base += value;
                basePercent += valuePercent;
            }
            fightBaseAttribute.setAttribute(percentType, basePercent);
            if (type == AttributeType.ATTR_MaxHp) {
                //玩家的总血量处理
                fightBaseAttribute.addMaxHP((long) (base * (1 + basePercent / 10000.00d)));
            } else {
                fightBaseAttribute.setAttribute(type, (long) (base * (1 + basePercent / 10000.00d)));
            }
//            local.info("type={} base={} last={}", type, base, (base * (1 + basePercent / 10000.00d)) );
        }

        for (int type : AttributeType.ATTR_NO_FIX) {
            // 基本值属性累加计算
            int base = 0;
            for (PlayerAttributeType attributeType : PlayerAttributeType.values()) {
                if (haveType(attributeType, ext)) {
                    continue;
                }
                BaseIntAttribute attr = getAttribute(player, attributeType);
                int value = attr.getAdditionValue(type);
                base += value;
            }
            fightBaseAttribute.setAttribute(type, base);
        }
        // 计算属性计算公式：当前角色属性*属性比例， 当前角色属性包含;characters中当前级别的属性+changejob中转职的本级属性
        BaseIntAttribute attr = getAttribute(player, PlayerAttributeType.BASE);
        for (int[] array : AttributeType.ATTR_Add_FIX) {
            //加层类型
            int percentType = array[0];
            //增加的属性
            int type = array[1];
            if (type == AttributeType.ATTR_MaxHp) {
                //玩家的总血量处理
                fightBaseAttribute.addMaxHP((long) (attr.getAdditionValue(type) * (fightBaseAttribute.getAdditionValue(percentType) / 10000.00d)));
            } else {
                fightBaseAttribute.addAttribute(type, (long) (attr.getAdditionValue(type) * fightBaseAttribute.getAdditionValue(percentType) / 10000.00d));
            }
        }
    }

    /**
     * 初始化玩家属性
     *
     * @param player
     * @param sycRank 初始化时是否影响排行榜
     */
    public void initPlayerAttribute(Player player, boolean sycRank) {
        //计算百分比属性
        for (PlayerAttributeType attributeType : PlayerAttributeType.values()) {
            Manager.playerAttAttributeManager.deal(attributeType).getPlayerSystemAttribute(player);
        }
        sumSystemAttribute(player);

        //计算基础属性
        for (PlayerAttributeType attributeType : PlayerAttributeType.values()) {
            Manager.playerAttAttributeManager.deal(attributeType).getPlayerAttribute(player, sycRank);
        }
        sumAttribute(player);

        //计算玩家最终战斗力
        calPlayerFighting(player, true, sycRank);

        //记录log
        PlayerAttChangeLog(player, player.getAttribute().toString(), player.getFightPoint());
        if (sycRank) {
            Manager.rankListManager.deal().syncRankPlayer(player);
        }
    }

    /**
     * 写属性日志
     *
     * @param player
     */
    public void writeAttlog(Player player) {
        for (PlayerAttributeType attributeType : PlayerAttributeType.values()) {
            BaseSystemIntAttribute sysAtt = getSystemAttribute(player, attributeType);
            logger.info("副属性 id={} type={} sysAtt={}", player.getId(), attributeType, sysAtt);
        }

        for (PlayerAttributeType attributeType : PlayerAttributeType.values()) {
            BaseIntAttribute at = getAttribute(player, attributeType);
            logger.info("主属性 id={} type={} att={}", player.getId(), attributeType, at);
        }

        logger.info("id:" + player.getId() + " fightPoint:" + player.getFightPoint());
        logger.info("c：" + player.getCurrencys().toString());
        logger.info("maxhp：" + player.getAttribute().MaxHP());
        logger.info("id:" + player.getId() + " att:" + player.getAttribute().toString());
    }

    /**
     * 发送玩家属性到客户端
     *
     * @param player
     */
    public void sendAttributeValueToPlayer(Player player) {
        PlayerMessage.ResPlayerOnLineAttribute.Builder b = PlayerMessage.ResPlayerOnLineAttribute.newBuilder();
        game.message.PlayerMessage.Attribute.Builder a = game.message.PlayerMessage.Attribute.newBuilder();
        for (int i = 1; i <= AttributeType.ATTR_MAX; i++) {
            if (i == AttributeType.ATTR_MaxHp) {
                continue;
            }
            a.setType(i);
            a.setValue(player.getAttribute().getAdditionValue(i));
            b.addAttributeList(a);
        }
        a.setType(AttributeType.AttackSpeedFinal);
        a.setValue(player.getAttribute().gainFinalAttackSpeed());
        b.addAttributeList(a);
        a.setType(AttributeType.MoveSpeedFinal);
        a.setValue(player.getAttribute().gainFinalMoveSpeed());
        b.addAttributeList(a);
        b.setMaxHp(player.getAttribute().MaxHP());
        MessageUtils.send_to_player(player, PlayerMessage.ResPlayerOnLineAttribute.MsgID.eMsgID_VALUE, b.build().toByteArray());
    }

    /**
     * 重新计算玩家属性，加上属性变化类型
     *
     * @param player
     * @param types
     */
    public void calcAttribute(Player player, PlayerAttributeType... types) {
        if (!player.isOnline()) {
            logger.error(player + "玩家不在线,计算属性模块:" + types);
            return;
        }
        BaseLongAttribute oldAttributesValue = new BaseLongAttribute(AttributeType.ATTR_MAX);
        AttributeUtils.addAttribute(oldAttributesValue, player.getAttribute());

        PlayerAttributeType[] calc = types.length == 0 ? PlayerAttributeType.values() : types;
        //计算基础属性
        for (PlayerAttributeType attributeType : calc) {
            Manager.playerAttAttributeManager.deal(attributeType).getPlayerSystemAttribute(player);
        }
        sumSystemAttribute(player);

        //将新属性设置
        for (PlayerAttributeType attributeType : calc) {
            Manager.playerAttAttributeManager.deal(attributeType).getPlayerAttribute(player, true);
        }
        sumAttribute(player);

        compareAttributsChange(oldAttributesValue, player);

        //TODO 开始计算玩家战力
        long oldFight = player.getFightPoint();
        if (types.length != 1 || PlayerAttributeType.BUFF != types[0]) {
            calPlayerFighting(player, true, true);
        }

        //logger.info("重id:" + player.getId() + " att:" + player.getAttribute().toString());
        //logger.info("id:" + player.getId() + " fightPoint:" + player.getFightPoint());
        //logger.info("type=" + type + ",玩家的总血量：" + player.getAttribute().MaxHP());
        //如果是跨服则不重新计算属性
        if (GameServer.getInstance().IsFightServer()) {
            return;
        }

        long newPoint = player.getFightPoint();
        //如果战力变化有上W的变化值， 则记录日志
        if (Math.abs(newPoint - oldFight) >= 10000) {
            PlayerFightPointChangeLog pfpc = new PlayerFightPointChangeLog();
            pfpc.setNewBuffFight(0);
            pfpc.setNewPower(newPoint);
            pfpc.setOldBuffFight(0);
            pfpc.setOldPower(oldFight);
            pfpc.setRoleId(player.getId());
            pfpc.setType(0);
            LogService.getInstance().execute(pfpc);
        }

        //报警，后台日志加入
        checkPlayerAttWarning(player);

        //如果玩家在跨服同步战斗力过去
        if (player.playerCrossData.isToFightServer()) {

            CrossFightMessage.G2FSynPowerAttAndFace.Builder msg = CrossFightMessage.G2FSynPowerAttAndFace.newBuilder();
            msg.setRoleId(player.getId());
            msg.setFightPower(player.getFightPoint());
            String att = JsonUtils.toJSONString(player.PlayerCalculators());
            msg.setAttlist(att);
            MessageUtils.send_to_fight(player, CrossFightMessage.G2FSynPowerAttAndFace.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    //比较属性变化
    private void compareAttributsChange(BaseLongAttribute oldAttributsVlaue, Player player) {
        //TODO 2020.7.22策划要求暂时取消战斗服属性同步屏蔽
        //如果是跨服则不重新计算属性,战斗只更新移动速度， 其它不管
//        if (GameServer.getInstance().IsFightServer()) {
//            ResPlayerAttributeChange.Builder b = ResPlayerAttributeChange.newBuilder();
//            game.message.PlayerMessage.Attribute.Builder a = game.message.PlayerMessage.Attribute.newBuilder();
//            b.setType(type);
//            a.setType(AttributeType.MoveSpeedFinal);
//            int off = player.getAttribute().gainFinalMoveSpeed();
//            a.setValue(off);
////                    LOGGER.info("移动速度是:" + off);
//            b.addChangeList(a);
//            broadCastMoveSpeedChange(player);
//            MessageUtils.send_to_player(player, ResPlayerAttributeChange.MsgID.eMsgID_VALUE, b.build().toByteArray());
//            return;
//        }
        PlayerMessage.ResPlayerAttributeChange.Builder b = PlayerMessage.ResPlayerAttributeChange.newBuilder();
        game.message.PlayerMessage.Attribute.Builder a = game.message.PlayerMessage.Attribute.newBuilder();
        b.setType(0);
        for (int i = 0; i <= AttributeType.ATTR_MAX; i++) {
            if (player.getAttribute().getAdditionValue(i) != oldAttributsVlaue.getAdditionValue(i)) {
                if (i == AttributeType.ATTR_AtkSpeed) {
                    a.setType(AttributeType.AttackSpeedFinal);
                    a.setValue(player.getAttribute().gainFinalAttackSpeed());//gainFinalAttackSpeed());
                    b.addChangeList(a);
                    sendAttackSpeedMessge(player);
                    continue;
                }
                if (i == AttributeType.ATTR_Speed) {
                    a.setType(AttributeType.MoveSpeedFinal);
                    int off = player.getAttribute().gainFinalMoveSpeed();
                    a.setValue(off);
                    b.addChangeList(a);
                    sendMoveSpeedMessage(player);
                }
                a.setType(i);
                a.setValue(player.getAttribute().getAdditionValue(i));
                b.addChangeList(a);
            }

            //重新发一下玩家的经验加成BUFF 变更
            if (i == AttributeType.ATTR_MonserExp) {
                MapUtils.sendExpRate(player);
            }
        }

        if (oldAttributsVlaue.MaxHP() != player.getAttribute().MaxHP()) {
            Manager.playerManager.managerExt().SendMaxHpChange(player);
        }
        MessageUtils.send_to_player(player, PlayerMessage.ResPlayerAttributeChange.MsgID.eMsgID_VALUE, b.build().toByteArray());
    }

    //移动速度广播
    public void sendMoveSpeedMessage(Player player) {
        if (!player.isOnline()) {
            return;
        }
        MapMessage.ResMoveSpeedChange.Builder msg = MapMessage.ResMoveSpeedChange.newBuilder();
        msg.setObjectId(player.getId());
        msg.setValue(player.getAttribute().gainFinalMoveSpeed());
        MessageUtils.send_to_roundPlayer(player, MapMessage.ResMoveSpeedChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        if (player.getActivePet().getFightPet() > 0) {
            MapMessage.ResMoveSpeedChange.Builder msg2 = MapMessage.ResMoveSpeedChange.newBuilder();
            msg2.setObjectId(player.getActivePet().getFightPet());
            msg2.setValue(player.getAttribute().gainFinalMoveSpeed());
            MessageUtils.send_to_roundPlayer(player, MapMessage.ResMoveSpeedChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    /**
     * 攻速改变
     *
     * @param player
     */
    public void sendAttackSpeedMessge(Player player) {
        if (!player.isOnline()) {
            return;
        }
        MapMessage.ResAttackspeedChange.Builder msg = MapMessage.ResAttackspeedChange.newBuilder();
        msg.setObjectId(player.getId());
        msg.setValue(player.getAttribute().gainFinalAttackSpeed());
        MessageUtils.send_to_roundPlayer(player, MapMessage.ResAttackspeedChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private void PlayerAttChangeLog(Player player, String end, long endf) {
        if (player.getId() <= 500) {
            return;
        }
        PlayerAttChangeLog pacl = new PlayerAttChangeLog();
        pacl.setRoleId(player.getId());
        pacl.setName(player.getName());
        pacl.setEndAtt(end);
        pacl.setEndFight(endf);
        LogService.getInstance().execute(pacl);
    }
}
