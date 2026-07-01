
package common.buff;

import com.data.CfgManager;
import com.data.FunctionVariable;
import com.data.MessageString;
import com.data.bean.Cfg_Buff_Bean;
import com.data.bean.Cfg_Change_model_Bean;
import com.data.bean.Cfg_Monster_Bean;
import com.data.struct.ReadArray;
import com.game.attribute.AttributeUtils;
import com.game.attribute.BaseLongAttribute;
import com.game.buff.script.IBuffBehavior;
import com.game.buff.script.IBuffScript;
import com.game.buff.structs.Buff;
import com.game.buff.structs.BuffDefine;
import com.game.chat.structs.Notify;
import com.game.cooldown.structs.CooldownTypes;
import com.game.manager.Manager;
import com.game.monster.structs.Monster;
import com.game.pet.structs.Pet;
import com.game.player.structs.PlayerAttributeType;
import com.game.player.structs.Player;
import com.game.robot.manager.RobotManager;
import com.game.robot.struct.Robot;
import com.game.script.structs.ScriptEnum;
import com.game.structs.AttributeType;
import com.game.structs.Entity;
import com.game.structs.Fighter;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import game.core.script.IScript;
import game.core.util.TimeUtils;
import game.message.BuffMessage.ResAddBuff;
import game.message.BuffMessage.ResBuffs;
import game.message.BuffMessage.ResRemoveBuff;
import game.message.BuffMessage.ResUpdateBuff;
import game.message.CommonMessage;
import game.message.PlayerMessage.ResMaxHpChange;

import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author zenghai
 */
public class BuffScript implements IScript, IBuffScript {

    private static final Logger log = LogManager.getLogger(BuffScript.class);

    @Override
    public int getId() {
        return ScriptEnum.BuffScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void online(Player player) {

        //FIXBUG 修复缓存导致的状态叠加问题
        player.setMoneyRate(1f);

        ResBuffs.Builder msg = ResBuffs.newBuilder();
        Iterator<Buff> iter = player.getBuffs().iterator();
        while (iter.hasNext()) {

            Buff buff = iter.next();
            if (buff == null) {
                iter.remove();
                continue;
            }
            //登录初始化状态
//            buff.add(player, player);

            Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());
            if (config.getIf_send() == 1) {
                continue;
            }
            CommonMessage.Buff.Builder builder = Manager.buffManager.deal().build(buff);
            msg.addBuffs(builder);
        }
        MessageUtils.send_to_player(player, ResBuffs.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void offline(Player player) {
        List<Buff> buffs = player.getBuffs();
        synchronized (buffs) {
            Iterator<Buff> iter = player.getBuffs().iterator();
            while (iter.hasNext()) {
                Buff buff = iter.next();
                if (buff == null) {
                    iter.remove();
                    continue;
                }
                Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());
                //清空下线清除的buff
                if (config.getBuffstore() == BuffDefine.StoreType_UnStore) {
                    iter.remove();
                    IBuffBehavior script = Manager.buffManager.script(config.getType());
                    script.remove(buff, player);
                    continue;
                }
                //清空脱战清除的buff
                if (config.getLeaveClean() == BuffDefine.LeaveBattleClean) {
                    iter.remove();
                    IBuffBehavior script = Manager.buffManager.script(config.getType());
                    script.remove(buff, player);
                }
            }
        }
    }

    @Override
    public void onDie(Player player) {
        List<Buff> buffs = player.getBuffs();
        boolean isCom = false;
        synchronized (buffs) {
            Iterator<Buff> iter = player.getBuffs().iterator();
            while (iter.hasNext()) {
                Buff buff = iter.next();
                if (buff == null) {
                    iter.remove();
                    continue;
                }
                Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());
                boolean isAtt = config.getType() == BuffDefine.Type_Attribute;

                //清空死亡清除的buff
                if (config.getDieClean() == BuffDefine.DieClean) {
                    iter.remove();
                    IBuffBehavior script = Manager.buffManager.script(config.getType());
                    script.remove(buff, player);
                    sendBuffDec(player, buff);
                    if (isAtt) {
                        isCom = true;
                    }
                    continue;
                }
                //清空脱战清除的buff
                if (config.getLeaveClean() == BuffDefine.LeaveBattleClean) {
                    iter.remove();
                    IBuffBehavior script = Manager.buffManager.script(config.getType());
                    script.remove(buff, player);
                    sendBuffDec(player, buff);
                    if (isAtt) {
                        isCom = true;
                    }
                }
            }
        }
        if (isCom) {
            Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.BUFF);
        }
    }

    @Override
    public void onDie(Pet pet) {
        List<Buff> buffs = pet.getBuffs();
        synchronized (buffs) {
            Iterator<Buff> iter = pet.getBuffs().iterator();
            while (iter.hasNext()) {
                Buff buff = iter.next();
                if (buff == null) {
                    iter.remove();
                    continue;
                }
                Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());
                //清空死亡清除的buff
                if (config.getDieClean() == BuffDefine.DieClean) {
                    iter.remove();
                    IBuffBehavior script = Manager.buffManager.script(config.getType());
                    script.remove(buff, pet);
                    sendBuffDec(pet, buff);
                    continue;
                }
                //清空脱战清除的buff
                if (config.getLeaveClean() == BuffDefine.LeaveBattleClean) {
                    iter.remove();
                    IBuffBehavior script = Manager.buffManager.script(config.getType());
                    script.remove(buff, pet);
                    sendBuffDec(pet, buff);
                }
            }
        }
    }

    @Override
    public void changeMapClear(Player player) {
        List<Buff> buffs = player.getBuffs();
        boolean isCom = false;
        synchronized (buffs) {
            Iterator<Buff> iter = player.getBuffs().iterator();
            while (iter.hasNext()) {
                Buff buff = iter.next();
                if (buff == null) {
                    iter.remove();
                    continue;
                }
                Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());
                boolean isAtt = config.getType() == BuffDefine.Type_Attribute;

                //清空死亡清除的buff
                if (config.getIf_changemap() == BuffDefine.ChangeMapClean) {

                    //打印日志，测试一个记录性BUG，过地图不清除载具BUF
                    if (buff.getBuffId() == 1100001) {
                        log.info("owner.getName()  " + player.getName() + "  getBuffId:  " + buff.getBuffId());
                    }
                    iter.remove();
                    IBuffBehavior script = Manager.buffManager.script(config.getType());
                    script.remove(buff, player);
                    sendBuffDec(player, buff);
                    if (isAtt) {
                        isCom = true;
                    }
                }
            }
        }
        if (isCom) {
            Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.BUFF);
        }

        Pet pet = Manager.petManager.getBattlePet(player);
        if (pet == null) {
            return;
        }
        clearChangeMapPet(pet);
    }

    private void clearChangeMapPet(Pet pet) {
        List<Buff> buffs = pet.getBuffs();
        synchronized (buffs) {
            Iterator<Buff> iter = pet.getBuffs().iterator();
            while (iter.hasNext()) {
                Buff buff = iter.next();
                if (buff == null) {
                    iter.remove();
                    continue;
                }
                Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());
                //清空死亡清除的buff
                if (config.getIf_changemap() == BuffDefine.ChangeMapClean) {
                    iter.remove();
                    IBuffBehavior script = Manager.buffManager.script(config.getType());
                    script.remove(buff, pet);
                    sendBuffDec(pet, buff);
                }
            }
        }
    }

    @Override
    public void onLiveBattle(Entity entity) {
        boolean isCom = false;
        List<Buff> buffs = entity.getBuffs();
        synchronized (buffs) {
            Iterator<Buff> iter = entity.getBuffs().iterator();
            while (iter.hasNext()) {

                Buff buff = iter.next();
                if (buff == null) {
                    iter.remove();
                    continue;
                }
                Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());
                //清空脱战清除的buff
                if (config.getLeaveClean() == BuffDefine.LeaveBattleClean) {
                    iter.remove();
                    IBuffBehavior script = Manager.buffManager.script(config.getType());
                    script.remove(buff, entity);
                    sendBuffDec(entity, buff);
                    if (config.getType() == BuffDefine.Type_Attribute) {
                        isCom = true;
                    }
                }
            }
        }

        if (isCom) {
            if (entity instanceof Player) {
                Manager.playerAttAttributeManager.deal().calcAttribute((Player) entity, PlayerAttributeType.BUFF);
            }
        }
    }

    @Override
    public boolean onAddBuff(Fighter source, Fighter target, int buffId) {

        Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buffId);
        if (config == null) {
//            log.error("添加不存在的buffId=" + buffId + target);
            return false;
        }
        IBuffBehavior script = Manager.buffManager.script(config.getType());
        if (script == null) {
            return false;
        }
        List<Buff> buffs = target.getBuffs();
        synchronized (buffs) {
            if (config.getCDTime() > 0) {
                if (Manager.cooldownManager.isCooldowning(target, CooldownTypes.BUFFHIT_CD, String.valueOf(buffId), 0)) {
                    return false;
                }
                Manager.cooldownManager.addCooldown(target, CooldownTypes.BUFFHIT_CD, String.valueOf(buffId), config.getCDTime());
            }
            Buff buff = getBuff(target, buffId);
            if (buff != null) {
                //TODO 计算叠加
                if (config.getOverlap() > 0 && config.getOverlap() > buff.getOverlap()) {
                    buff.setOverlap(buff.getOverlap() + 1);
                    script.overlap(buff, target);
                }
                if (config.getType() == BuffDefine.Type_Attribute) {
                    calBuffAtt(target, buff);
                }
                sendBuffUpdate(target, buff);
                return true;
            }
            //TODO 同组buff检测
            buff = getBuffByGroup(target, config.getGroup());
            if (buff != null) {
                if (!isRepalce(target, buff, config)) {
                    return false;
                }
                script.remove(buff, target);
                target.getBuffs().remove(buff);

                sendBuffDec(target, buff);
            }
            //TODO 新增buff
            buff = new Buff();
            buff.setBuffId(buffId);
            buff.setSourceId(source.getId());
            buff.setStart(TimeUtils.Time());
            buff.setOverlap(1);

            target.getBuffs().add(buff);
            script.add(buff, source, target);
            sendBuffAdd(target, buff);

            if (config.getType() == BuffDefine.Type_Attribute) {
                calBuffAtt(target, buff);
            }
            return true;
        }
    }

    private void calBuffAtt(Fighter target, Buff buff) {

        Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());
        if (config.getType() != BuffDefine.Type_Attribute) {
            return;
        }

        if (target instanceof Player) {
            Manager.playerAttAttributeManager.deal().calcAttribute((Player) target, PlayerAttributeType.BUFF);
            return;
        }
        if (target instanceof Monster) {
            ComMonsterAtt((Monster) target);
            return;
        }

        if (target instanceof Pet) {
            Pet pet = (Pet) target;

            Player player = Manager.playerManager.getPlayerCache(pet.getOwnerId());
            if (player == null) {
                return;
            }
            Manager.petManager.deal().calPetAttribute(pet);
        }

        if (target instanceof Robot) {
            Robot robot = (Robot) target;
            RobotManager.getInstance().deal().OnCalcBuffAttribute(robot, robot.getPet(), PlayerAttributeType.BUFF);
        }
    }

    //同组能否顶替
    public boolean isRepalce(Fighter target, Buff buff, Cfg_Buff_Bean config) {
        Cfg_Buff_Bean check = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());
        return config.getLevel() > check.getLevel();
    }

    //获取同一组buff
    public Buff getBuffByGroup(Fighter entity, int groupId) {

        for (Buff buff : entity.getBuffs()) {
            Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());
            if (config == null) {
                continue;
            }
            if (config.getGroup() == groupId) {
                return buff;
            }
        }
        return null;
    }

    //获取buff
    @Override
    public Buff getBuff(Fighter fighter, int buffId) {
        for (Buff buff : fighter.getBuffs()) {
            if (buff.getBuffId() == buffId) {
                return buff;
            }
        }
        return null;
    }

    @Override
    public void tick(Pet fighter, Fighter acter) {
        if (fighter.getBuffs().isEmpty()) {
            return;
        }
        long curTime = TimeUtils.Time();
        List<Buff> bufs = fighter.getBuffs();
        boolean iscom = false;
        synchronized (bufs) {

            Iterator<Buff> iter = bufs.iterator();
            while (iter.hasNext()) {
                Buff buff = iter.next();
                if (buff == null) {
                    iter.remove();
                    continue;
                }
                Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());
                boolean isAtt = config.getType() == BuffDefine.Type_Attribute;
                IBuffBehavior script = Manager.buffManager.script(config.getType());
                if (checkCanDelete(buff)) {
                    script.timeout(buff, fighter);
                    iter.remove();
                    script.remove(buff, fighter);
                    sendBuffDec(fighter, buff);

                    isAtt = isAtt || config.getType() == BuffDefine.Type_Attribute;
                    continue;
                }
                long offset = curTime - buff.getStart();
                //TODO 无触发
                if (config.getTime() <= 0) {
                    buff.setPassTime(buff.getPassTime() + offset);
                    buff.setStart(curTime);
                } else {
                    //TODO 触发时间不够
                    if (offset < config.getTime()) {
                        continue;
                    }
                    buff.setPassTime(buff.getPassTime() + config.getTime());
                    buff.setStart(curTime);

                    buff.setTrigger(buff.getTrigger() + 1);
                    script.action(buff, fighter, fighter);
                }
            }
        }

        //计算属性
        if (iscom) {
            if (acter instanceof Robot) {
                RobotManager.getInstance().deal().OnCalcBuffAttribute((Robot) acter, fighter, PlayerAttributeType.BUFF);
                return;
            }

            Manager.petManager.deal().calPetAttribute(fighter);
        }
    }

    /**
     * 是否可以
     *
     * @param buff
     * @return
     */
    boolean checkCanDelete(Buff buff) {
        if (buff.isDelete()) {
            return true;
        }
        Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());
        //TODO 触发次数完成
        if (config.getTrigger() > 0) {
            return buff.getTrigger() >= config.getTrigger();
        }
        if (config.getAllTime() > 0) {
            return buff.getPassTime() > config.getAllTime() * buff.getOverlap();
        }
        return false;
    }

    @Override
    public void tick(Fighter fighter) {

        long curTime = TimeUtils.Time();

        List<Buff> bufs = fighter.getBuffs();
        boolean isAtt = false;
        synchronized (bufs) {
            for (Iterator<Buff> it = fighter.getBuffs().iterator(); it.hasNext(); ) {
                Buff buff = it.next();

                Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());
                IBuffBehavior script = Manager.buffManager.script(config.getType());
                if (checkCanDelete(buff)) {
                    script.timeout(buff, fighter);
                    it.remove();
                    script.remove(buff, fighter);
                    sendBuffDec(fighter, buff);

                    isAtt = isAtt || config.getType() == BuffDefine.Type_Attribute;
                    continue;
                }
                long offset = curTime - buff.getStart();
                //TODO 无触发
                if (config.getTime() <= 0) {
                    buff.setPassTime(buff.getPassTime() + offset);
                    buff.setStart(curTime);
                } else {
                    //TODO 触发时间不够
                    if (offset < config.getTime()) {
                        continue;
                    }
                    buff.setPassTime(buff.getPassTime() + config.getTime());
                    buff.setStart(curTime);

                    buff.setTrigger(buff.getTrigger() + 1);
                    script.action(buff, fighter, fighter);
                }
            }
        }
        if (!isAtt) {
            return;
        }
        if (fighter instanceof Player) {
            Manager.playerAttAttributeManager.deal().calcAttribute((Player) fighter, PlayerAttributeType.BUFF);
        }
        if (fighter instanceof Robot) {
            Robot robot = (Robot) fighter;
            RobotManager.getInstance().deal().OnCalcBuffAttribute(robot, robot.getPet(), PlayerAttributeType.BUFF);
        }
        if (fighter instanceof Monster) {
            Monster monster = (Monster) fighter;
            ComMonsterAtt(monster);
        }
    }

    @Override
    public boolean onRemoveBuff(Fighter fighter, int buffId) {
        List<Buff> bufs = fighter.getBuffs();
        synchronized (bufs) {
            Iterator<Buff> iter = fighter.getBuffs().iterator();
            while (iter.hasNext()) {
                Buff buff = iter.next();
                if (buff == null) {
                    iter.remove();
                    continue;
                }
                Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());
                IBuffBehavior script = Manager.buffManager.script(config.getType());

                if (buff.getBuffId() == buffId) {
                    iter.remove();
                    script.remove(buff, fighter);
                    sendBuffDec(fighter, buff);
                    if (config.getType() == BuffDefine.Type_Attribute) {
                        if (fighter instanceof Player) {
                            Manager.playerAttAttributeManager.deal().calcAttribute((Player) fighter, PlayerAttributeType.BUFF);
                        }
                        if (fighter instanceof Robot) {
                            Robot robot = (Robot) fighter;
                            RobotManager.getInstance().deal().OnCalcBuffAttribute(robot, robot.getPet(), PlayerAttributeType.BUFF);
                        }
                        if (fighter instanceof Pet) {
                            Pet pet = (Pet) fighter;
                            Player player = Manager.playerManager.getPlayerCache(pet.getOwnerId());
                            if (player != null) {
                                Manager.petManager.deal().calPetAttribute(pet);
                            }
                        }
                        //计算怪物BUFF 
                        if (fighter instanceof Monster) {
                            ComMonsterAtt((Monster) fighter);
                        }
                    }
                    return true;
                }
            }
            return true;
        }
    }

    @Override
    public boolean isCanAdd(Player player, int buffId) {
        Buff buff = getBuff(player, buffId);
        if (buff == null) {
            Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buffId);
            return config != null;
        }

        Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());

        return buff.getOverlap() < config.getOverlap();
    }

    private void ComMonsterAtt(Monster monster) {
        Cfg_Monster_Bean monsterCfg = CfgManager.getCfg_Monster_Container().getValueByKey(monster.getModelId());
        if (null == monsterCfg) {
            return;
        }
        BaseLongAttribute attribute = monster.getAttribute();
        BaseLongAttribute oldAttributsVlaue = new BaseLongAttribute(AttributeType.ATTR_MAX);
        AttributeUtils.clean(oldAttributsVlaue);
        AttributeUtils.addAttribute(oldAttributsVlaue, monster.getAttribute());

        attribute.clean();
        attribute.cleanMaxHP();
        for (ReadArray<Integer> aii : monsterCfg.getAttributeValue().getValuees()) {
            if (aii.get(0) == AttributeType.ATTR_Speed) {
                attribute.addAttribute(aii.get(0), aii.get(1));
                continue;
            }
            attribute.addAttribute(aii.get(0), (long) (aii.get(1) * (monster.getAttBei() / 10000.)));
        }
        attribute.addMaxHP((long) (monsterCfg.getMaxHp() * (monster.getAttBei() / 10000.)));

        //计算BUFF的属性
        for (Buff buff : monster.getBuffs()) {

            Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());
            if (config.getType() != BuffDefine.Type_Attribute) {
                continue;
            }
            for (ReadArray<Integer> porpert : config.getPorperty().getValuees()) {
                int type = porpert.get(0);
                int value = porpert.get(1);
                if (type < 1 || type > AttributeType.ATTR_MAX) {
                    continue;
                }
                long oldValue = monster.getAttribute().getAdditionValue(type);
                attribute.addAttribute(type, value * buff.getOverlap());
                if (type == AttributeType.ATTR_MaxHp) {
                    attribute.addMaxHP(value * buff.getOverlap());
                }
            }
        }

        long buffMaxpHp = monster.getAttribute().MaxHP();
        for (int[] types : AttributeType.ATTR_FIX) {
            int type = types[0];
            long valuePercent = 0;
            if (types[1] != 0) {
                valuePercent = attribute.getAdditionValue(types[1]);
            }
            //玩家的总血量处理
            if (type == AttributeType.ATTR_MaxHp) {
                attribute.cleanMaxHP();
                attribute.addMaxHP((long) (buffMaxpHp * (1 + valuePercent / 10000.00d)));
                continue;
            }
            // 获取属性里具体的值
            long base = attribute.getAdditionValue(type);
            attribute.setAttribute(type, (long) (base * (1 + valuePercent / 10000.00d)));
        }
        attribute.calFinalAttackSpeed();
        attribute.calFinalMoveSpeed();

        if (buffMaxpHp != attribute.MaxHP()) {
            ResMaxHpChange.Builder msg = ResMaxHpChange.newBuilder();
            msg.setRoleId(monster.getId());
            msg.setMaxHp(attribute.MaxHP());
            MessageUtils.send_to_roundPlayer(monster, ResMaxHpChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
        //如果去掉了BUFF，则血量变化处理
        if (monster.getCurHp() > attribute.MaxHP()) {
            monster.setCurHp(attribute.MaxHP());
            monster.onHpChange(monster);
        }

        //如果BUFF上限有变动，那么相应的血量变化也是需要变动的
        long offMaxHp = attribute.MaxHP() - buffMaxpHp;
        if (offMaxHp > 0 && !Manager.buffManager.deal().haveJinLiao(monster)) {
            monster.setCurHp(monster.getCurHp() + offMaxHp);
            monster.onHpChange(monster);
        }
        try {
            Manager.monsterManager.manager().monsterAttributeChange(oldAttributsVlaue, monster, PlayerAttributeType.BUFF);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    @Override
    public void removeAllHarmBuff(Fighter fighter) {
        for (Buff buff : fighter.getBuffs()) {
            Cfg_Buff_Bean buffBean = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());
            if (buffBean.getAddsub() == 1) {
                onRemoveBuff(fighter, buff.getBuffId());
            }
        }
    }

    @Override
    public boolean isExist(Fighter fighter, int buffId) {
        if (buffId == 0) {
            return false;
        }

        for (Buff buff : fighter.getBuffs()) {
            if (buff.getBuffId() == buffId) {
                return true;
            }
        }
        return false;

    }

    @Override
    public int getBuffNum(Fighter fighter, int type) {
        int num = 0;
        for (Buff buff : fighter.getBuffs()) {
            Cfg_Buff_Bean buffBean = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());
            if (buffBean.getAddsub() == type) {
                num++;
            }
        }
        return num;
    }

    private void sendBuffDec(Fighter fighter, Buff buff) {

        Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());
        if (config.getIf_send() == 1) {
            return;
        }
        ResRemoveBuff.Builder msg = ResRemoveBuff.newBuilder();
        msg.setOwnId(fighter.getId());
        msg.setId(buff.getBuffId());
        MessageUtils.send_to_roundPlayer(fighter, ResRemoveBuff.MsgID.eMsgID_VALUE, msg.build().toByteArray());

//        LOGGER.info("删除buff=" + buff.getBuffId() + fighter);
    }

    private void sendBuffAdd(Fighter target, Buff buff) {

        Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());
        if (config.getIf_send() == 1) {
            return;
        }
        ResAddBuff.Builder msg = ResAddBuff.newBuilder();
        msg.setOwnId(target.getId());
        CommonMessage.Buff.Builder builder = Manager.buffManager.deal().build(buff);
        msg.setBuff(builder);
        MessageUtils.send_to_roundPlayer(target, ResAddBuff.MsgID.eMsgID_VALUE, msg.build().toByteArray());

//        log.info("添加buff={} level={} player={}", buff.getBuffId(), buff.getOverlap(), target);
    }

    /**
     * buff 更新
     *
     * @param owner
     * @param buff
     */
    public void sendBuffUpdate(Fighter owner, Buff buff) {

        Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());
        if (config.getIf_send() == 1) {
            return;
        }

        ResUpdateBuff.Builder msg = ResUpdateBuff.newBuilder();
        msg.setOwnId(owner.getId());

        CommonMessage.Buff.Builder builder = Manager.buffManager.deal().build(buff);
        msg.setBuff(builder);
        MessageUtils.send_to_roundPlayer(owner, ResUpdateBuff.MsgID.eMsgID_VALUE, msg.build().toByteArray());

//        log.info("更新buff={} level={} player={}", buff.getBuffId(), buff.getOverlap(), owner);
    }

    @Override
    public void removeBuffInvisible(Entity player) {
        List<Buff> buffs = player.getBuffs();
        synchronized (buffs) {
            for (Iterator<Buff> it = buffs.iterator(); it.hasNext(); ) {
                Buff buff = it.next();
                Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());
                if (config == null) {
                    it.remove();
                    continue;
                }
                IBuffBehavior script = Manager.buffManager.script(config.getType());
                if (config.getType() == BuffDefine.Type_RoleInvisible) {
                    script.remove(buff, player);
                    it.remove();
                    sendBuffDec(player, buff);
                }
            }
        }
    }


    @Override
    public Buff isHaveBuff(Fighter fighter, int type, int groupId) {
        return Utils.findOne(fighter.getBuffs(), b -> {
            Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(b.getBuffId());
            return config.getType() == type && config.getGroup() == groupId;
        });
    }

    /**
     * 是否具体某个类型的BUFF
     *
     * @param fighter
     * @param type
     * @return
     */
    @Override
    public boolean isHaveBuff(Fighter fighter, int type) {
        Buff buff = Utils.findOne(fighter.getBuffs(), b -> {
            Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(b.getBuffId());
            return config.getType() == type;
        });
        return buff != null;
    }


    public void onReqAddChangeModeBuff(Player player, int id) {
        Cfg_Change_model_Bean change_model_bean = CfgManager.getCfg_Change_model_Container().getValueByKey(id);
        if (change_model_bean == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ConfigError_GetByKey, id + "");
            return;
        }
        if (change_model_bean.getMapLimit().size() > 0) {
            Integer[] maps = change_model_bean.getMapLimit().getValue();
            for (Integer mapid : maps) {
                if (mapid == player.getCurGps().getModelId()) {
                    onAddBuff(player, player, id);
                    Manager.biManager.getScript().biVehicle(player, player.getCurGps().getModelId(), id);
                    if (change_model_bean.getAttribute() > 0)
                        onAddBuff(player, player, change_model_bean.getAttribute());
                    break;
                }
            }
        } else {
            onAddBuff(player, player, id);
            Manager.biManager.getScript().biVehicle(player, player.getCurGps().getModelId(), id);
            if (change_model_bean.getAttribute() > 0)
                onAddBuff(player, player, change_model_bean.getAttribute());
        }
        //神骑
        if (id == 1100001) {
            Manager.controlManager.operate(player, FunctionVariable.GuildWar_special_skill, 1);
        }
    }

    public void onReqRemoveChangeModeBuff(Player player, int id) {
        Cfg_Change_model_Bean change_model_bean = CfgManager.getCfg_Change_model_Container().getValueByKey(id);
        if (change_model_bean == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ConfigError_GetByKey, id + "");
            return;
        }
        onRemoveBuff(player, id);
        if (change_model_bean.getAttribute() > 0)
            onRemoveBuff(player, change_model_bean.getAttribute());
    }

    public void addChangeModeSkill(Fighter source, int id) {
        if (!(source instanceof Player)) {
            return;
        }
        Player player = (Player) source;
        Cfg_Change_model_Bean change_model_bean = CfgManager.getCfg_Change_model_Container().getValueByKey(id);
        if (change_model_bean == null) {
            return;
        }

        for (Integer skillID : change_model_bean.getSkill().getValue()) {
            Manager.skillManager.addSkill(player, skillID);
        }
    }

    @Override
    public CommonMessage.Buff.Builder build(Buff buff) {
        Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());
        CommonMessage.Buff.Builder builder = CommonMessage.Buff.newBuilder();
        builder.setBuffId(buff.getBuffId());
        builder.setCurLevel(buff.getOverlap());
        builder.addArgs(buff.getPar1());
        builder.addArgs(buff.getPar2());
        builder.addArgs(buff.getPar3());
        builder.addArgs(buff.getPar4());
        if (config.getAllTime() < 0) {
            builder.setRemainTime(-1);
        } else {
            Long remain = config.getAllTime() * buff.getOverlap() - buff.getPassTime();
            if (remain < 0) {
                builder.setRemainTime(0);
            } else {
                builder.setRemainTime(remain.intValue() / 1000); //转换成秒
            }
        }
        return builder;
    }

    public void removeChangeModeSkill(Fighter source, int id) {

        if (!(source instanceof Player)) {
            return;
        }
        Player player = (Player) source;
        Cfg_Change_model_Bean change_model_bean = CfgManager.getCfg_Change_model_Container().getValueByKey(id);
        if (change_model_bean == null) {
            return;
        }
        for (Integer skillID : change_model_bean.getSkill().getValue()) {
            Manager.skillManager.removeSkill(player, skillID);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////
    //////////////////////是否具有某些buff
    //////////////////////////////////////////////////////////////////////////////////

    /**
     * 是否定身
     *
     * @param fighter
     * @return
     */
    @Override
    public boolean haveDing(Fighter fighter) {
        return isHaveBuff(fighter, BuffDefine.Type_DING);
    }

    /**
     * 是否眩晕
     *
     * @param fighter
     * @return
     */
    @Override
    public boolean haveDizziness(Fighter fighter) {
        return isHaveBuff(fighter, BuffDefine.Type_Dizziness);
    }

    /**
     * 免控BUFF
     *
     * @param fighter
     * @return
     */
    @Override
    public boolean haveMiaokong(Fighter fighter) {
        return isHaveBuff(fighter, BuffDefine.Type_MiaoKang);
    }

    /**
     * 免控BUFF
     *
     * @param fighter
     * @return
     */
    @Override
    public boolean haveJinLiao(Fighter fighter) {
        return isHaveBuff(fighter, BuffDefine.Type_jinliao);
    }

}
