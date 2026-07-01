package com.game.monster.structs;

import com.data.CfgManager;
import com.data.bean.Cfg_MonsterAi_Bean;
import com.data.bean.Cfg_Monster_Bean;
import com.data.bean.Cfg_Skill_Bean;
import com.data.struct.ReadArray;
import com.game.cooldown.structs.Cooldown;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.monster.script.IMonsterAction;
import com.game.pet.structs.Pet;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.skill.structs.Skill;
import com.game.structs.Entity;
import com.game.structs.EntityState;
import com.game.structs.Fighter;
import com.game.structs.ServerStr;
import com.game.utils.MessageUtils;
import game.core.map.IMapObject;
import game.core.map.Position;
import game.core.script.IScript;
import game.core.util.TimeUtils;
import game.message.FightMessage.ResMonsterHpChange;
import game.message.MapMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 怪物
 */
public class Monster extends Entity{

    //所在服务器
    private int serverId = GameServer.getInstance().getServerId();

    //怪物出生点
    private Position initPos;

    //伤害列表<attackId, damageAll>
    private ConcurrentHashMap<Long, Long> damages = new ConcurrentHashMap<>();

    //冷却列表
    protected ConcurrentHashMap<String, Cooldown> cooldowns = new ConcurrentHashMap<>();

    //怪物类型
    private int monsterType;

    //固定掉血
    private long fixDecHp = 0;

    //移动速度加成
    private transient float speedRate = 0.5f;

    private boolean userNormalSkill = true;

    //技能信息
    private final ConcurrentHashMap<Integer, Skill> skills = new ConcurrentHashMap<>();

    //怪物已执行的Ai
    private final List<Cfg_MonsterAi_Bean> actionAis = new ArrayList<>();

    //进入战斗时间
    private long enterFight = -1;

    //最后一次受击时间
    private long endHitTime = 0;

    //出生时间
    private long brithTime = TimeUtils.Time();

    //击杀玩家数量
    private int killcount;

    private transient long brithProtect = TimeUtils.Time();

    //护甲值
    private int armor = 0;

    //恢复值
    private long armorbegintime = 0;

    //死亡恢复
    private long armorzerotime = 0;

    //boss掉落排名
    private final List<Long> dropRoleIds = new ArrayList<>();

    //恢复状态 -1 （没有配置护甲功能） 0 （正常护甲） 1 （进入护甲5秒后恢复） 2 （1秒一次恢复） 3 （10秒后恢复满护甲值）
    private int armorState = -1;

    private boolean armorTrue = false;

    // ai中切换状态时候的状态信息
    private transient String stepState;
    private long makerId;
    private transient boolean trap;
    private boolean useNewAi;

    private long dieTime = 0;

    //设置共生怪物的同减血量
    private long lineOtherMonsterId = 0;

    //属性倍数，万分比
    private int attBei = 10000;

    //是否是TD的怪
    private boolean tdMonster = false;

    //记录打死的玩家时间
    private final ConcurrentHashMap<Long, Long> playerDieTime = new ConcurrentHashMap<>();

    //地图怪移到的步数
    private int moveStep = 0;

    //正在走的路
    private int doStep = 0;

    //增加需要移动到的点值
    private final List<Position> willMovePos = new ArrayList<>();

    /**
     * 怪物战力，用于一血怪战力压制。
     */
    private long score;

    /**
     * 上次被攻击的时间
     */
    private long lastAttackTime = 0;

    /**
     * 所有攻击者
     */
    private HashSet<Fighter> allFighter = new HashSet<>();

    /**
     *上次掉血时间
     */
    private long lastDropHpTime = 0;

    /**
     * 即将脱战时间
     */
    private long willLeaveBattleTime = 0;

    /**
     * 是不是召唤boss
     * @return
     */
    private boolean callBoss;

    /**
     * 一定时间内收到的伤害
     */
    private long cd_hurt;

    public boolean isCallBoss() {
        return callBoss;
    }

    public void setCallBoss(boolean callBoss) {
        this.callBoss = callBoss;
    }

    public long getWillLeaveBattleTime() {
        return willLeaveBattleTime;
    }

    public void setWillLeaveBattleTime(long willLeaveBattleTime) {
        this.willLeaveBattleTime = willLeaveBattleTime;
    }

    public long getLastDropHpTime() {
        return lastDropHpTime;
    }

    public void setLastDropHpTime(long lastDropHpTime) {
        this.lastDropHpTime = lastDropHpTime;
    }

    public long getLastAttackTime() {
        return lastAttackTime;
    }

    public void setLastAttackTime(long lastAttackTime) {
        this.lastAttackTime = lastAttackTime;
    }

    public HashSet<Fighter> getAllFighter() {
        return allFighter;
    }

    public void setAllFighter(HashSet<Fighter> allFighter) {
        this.allFighter = allFighter;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public int getDoStep() {
        return doStep;
    }

    public void setDoStep(int doStep) {
        this.doStep = doStep;
    }

    public long getLineOtherMonsterId() {
        return lineOtherMonsterId;
    }

    public void setLineOtherMonsterId(long lineOtherMonsterId) {
        this.lineOtherMonsterId = lineOtherMonsterId;
    }

    //是否使用普攻技能
    public boolean isUserNormalSkill() {
        return userNormalSkill;
    }

    public void setUserNormalSkill(boolean userNormalSkill) {
        this.userNormalSkill = userNormalSkill;
    }

    @Override
    public void setCurHp(long curHp) {
        super.setCurHp(curHp); //To change body of generated methods, choose Tools | Templates.
        if (lineOtherMonsterId > 0) {
            IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.MonsterBaseScript);
            if (is instanceof IMonsterAction) {
                ((IMonsterAction) is).changeLineOtherMonsterCurHp(this, lineOtherMonsterId, curHp);
            } else {
                logger.error("没有实现同步扣血的功能接口！");
            }
        }
    }

    /**
     * 双生的另一支怪调用扣血系统
     *
     * @param curHp
     */
    public void sameMonsterSetCurHp(long curHp) {
        super.setCurHp(curHp);

    }

    @Override
    public String nameIdString() {
        return super.nameIdString() + "-" + modelId;
    }

    public ConcurrentHashMap<Long, Long> getPlayerDieTime() {
        return playerDieTime;
    }

    public long getDieTime() {
        return dieTime;
    }

    public void setDieTime(long dieTime) {
        this.dieTime = dieTime;
    }

    public Monster(int modelid) {
        setModelId(modelid);
    }

    public int getKillcount() {
        return killcount;
    }

    public void setKillcount(int killcount) {
        this.killcount = killcount;
    }

    public long getBrithTime() {
        return brithTime;
    }

    public void setBrithTime(long brithTime) {
        this.brithTime = brithTime;
    }

    public long getEnterFight() {
        return enterFight;
    }

    public void setEnterFight(long enterFight) {
        this.enterFight = enterFight;
    }

    public List<Cfg_MonsterAi_Bean> getActionAis() {
        return actionAis;
    }

    @Override
    public long getBrithProtect() {
        return brithProtect;
    }

    public void setBrithProtect(long brithProtect) {
        this.brithProtect = brithProtect;
    }

    @Override
    public int getType() {
        return Fighter.MONSTER_TYPE;
    }

    public float getSpeedRate() {
        return speedRate;
    }

    public void setSpeedRate(float speedRate) {
        this.speedRate = speedRate;
    }

    //部分数据重置
    @Override
    public void reset() {
        this.state = EntityState.Stand.getValue();
        this.curGps.setPos(this.initPos);
        this.clearDropRoleIds();
        this.clearHatred();
        this.damages.clear();
        this.endHitTime = 0;
        this.killcount = 0;
        this.setEnterFight(-1);
        this.getActionAis().clear();
        this.setArmorState(0);
        this.setDieTime(0);
        this.lineOtherMonsterId = 0;
        Manager.monsterManager.addMonsterBaseInfo(this, false, getAttBei());
        this.setCurHp(getAttribute().MaxHP());
        Manager.cooldownManager.cleanAllCooldown(this);
    }

    public void clearDropRoleIds() {
        this.getDropRoleIds().clear();
        MapMessage.ResMonsterDropMark.Builder msg1 = MapMessage.ResMonsterDropMark.newBuilder();
        msg1.setMonsterId(this.getId());
        msg1.addAllDropUserIds(this.getDropRoleIds());
        MessageUtils.send_to_roundPlayer(this, MapMessage.ResMonsterDropMark.MsgID.eMsgID_VALUE, msg1.build().toByteArray());
    }

    public int getAttBei() {
        return attBei;
    }

    public void setAttBei(int attBei) {
        this.attBei = attBei;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    @Override
    public ConcurrentHashMap<String, Cooldown> getCooldowns() {
        return cooldowns;
    }

    public void setCooldowns(ConcurrentHashMap<String, Cooldown> cooldowns) {
        this.cooldowns = cooldowns;
    }

    public Position getInitPos() {
        return initPos;
    }

    public void setInitPos(Position initPos) {
        this.initPos = initPos;
        this.curGps.setPos(initPos);
    }
    public ConcurrentHashMap<Long, Long> getDamages() {
        return damages;
    }

    public void setDamages(ConcurrentHashMap<Long, Long> damages) {
        this.damages = damages;
    }

    @Override
    public int getState() {
        return state;
    }

    public int getMonsterType() {
        return monsterType;
    }

    public void setMonsterType(int monsterType) {
        this.monsterType = monsterType;
    }

    @Override
    public ConcurrentHashMap<Integer, Skill> getSkills() {
        return skills;
    }

    @Override
    public boolean canSee(IMapObject pp) {
        if (!iShow()) {
            return false;
        }

        Player player = (Player) pp;
        return Manager.monsterManager.taskIsShow().canSee(player, this);
    }

    /**
     * 获取任务隐藏id集合
     *
     * @return
     */
    @Override
    public HashMap<Integer, List<Integer>> gainHideTaskIds() {
        Cfg_Monster_Bean monsterCfg = CfgManager.getCfg_Monster_Container().getValueByKey(this.getModelId());
        if (null == monsterCfg) {
            logger.error("monsterCfg 没有这个怪物" + this.getModelId());
            return null;
        }
        if (monsterCfg.getTaskShow().size() == 0) {
            return null;
        }
        HashMap<Integer, List<Integer>> reulst = new HashMap<>(16);
        for (int i = 0; i < monsterCfg.getTaskShow().size(); i++) {
            ReadArray<Integer> next = monsterCfg.getTaskShow().get(i);
            if (reulst.containsKey(next.get(0))) {
                reulst.get(next.get(0)).add(next.get(1));
            } else {
                List<Integer> temp = new ArrayList<>();
                temp.add(next.get(1));
                reulst.put(next.get(0), temp);
            }
        }
        return reulst;
    }

    @Override
    public void changeCurPos(Position pos) {
        Position oldPos = this.gainCurPos();
        this.curGps.setPos(pos);
        Manager.mapManager.changeArea(this, oldPos, pos);
        Manager.mapManager.manager().OnChangePos(this, oldPos);
    }

    @Override
    public void changeCurPos(Position pos, boolean isBroadcast) {
        Position oldPos = this.gainCurPos();
        this.curGps.setPos(pos);
        boolean isChange = Manager.mapManager.changeArea(this, oldPos, pos);
        if (isChange) {
            Manager.mapManager.manager().OnChangePos(this, oldPos);
        }
    }

    @Override
    public void doDie(Fighter attacker) {
        setDieTime(TimeUtils.Time());
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.MonsterBaseScript);
        if (is instanceof IMonsterAction) {
            ((IMonsterAction) is).monsterDie(this, attacker);
        } else {
            logger.error("怪物死亡时没有找到处理死亡的实例！");
        }

        //如果有双生怪, 双生怪也死
        if (lineOtherMonsterId > 0) {
            MapObject map = Manager.mapManager.getMap(this.gainMapId());
            if (map == null) {
                return;
            }
            Monster mon = map.getMonster(lineOtherMonsterId);
            if (mon == null) {
                return;
            }
            mon.setDieTime(TimeUtils.Time());
            if (is instanceof IMonsterAction) {
                ((IMonsterAction) is).monsterDie(mon, attacker);
            } else {
                logger.error("怪物死亡时没有找到处理死亡的实例！");
            }
        }
    }

    @Override
    public void beAttack(Fighter attacker, Cfg_Skill_Bean skill, int skilllevel, long damage) {

        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.MonsterBaseScript);
        if (is instanceof IMonsterAction) {
            ((IMonsterAction) is).monsterBeAttack(this, attacker, damage);
        } else {
            logger.error("怪物受伤时没有找到处理受伤的实例！");
        }

        //如果有双生怪, 双生怪被攻击
        if (lineOtherMonsterId > 0) {
            MapObject map = Manager.mapManager.getMap(this.gainMapId());
            if (map == null) {
                return;
            }
            Monster mon = map.getMonster(lineOtherMonsterId);
            if (mon == null) {
                return;
            }
            if (is instanceof IMonsterAction) {
                ((IMonsterAction) is).monsterBeAttack(mon, attacker, damage);
            } else {
                logger.error("怪物受伤时没有找到处理受伤的实例！");
            }
        }
    }

    @Override
    public void onHpChange(Fighter attacker) {

        if (attacker instanceof Pet) {
            attacker = Manager.playerManager.getPlayerCache(((Pet) attacker).getOwnerId());
        }

        if (getCurHp() <= 0) {
            return;
        }

        sendSameHpChange(attacker);

        //如果有双生怪
        if (lineOtherMonsterId > 0) {
            MapObject map = Manager.mapManager.getMap(this.gainMapId());
            if (map == null) {
                return;
            }
            Monster mon = map.getMonster(lineOtherMonsterId);
            if (mon == null) {
                return;
            }
            mon.sendSameHpChange(attacker);
        }
    }

    public void sendSameHpChange(Fighter attacker) {
        ResMonsterHpChange.Builder msg = ResMonsterHpChange.newBuilder();
        msg.setOwnId(this.getId());
        msg.setCurHp(this.getCurHp());
        msg.setArmor(this.getArmor());
        if ((attacker instanceof Player)) {
            if (getMonsterType() == MonsterDefine.MonsterType_Normal) {
                MessageUtils.send_to_player((Player) attacker, ResMonsterHpChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            } else {
                MessageUtils.send_to_roundPlayer(this, ResMonsterHpChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }
        } else {
            if (getMonsterType() != MonsterDefine.MonsterType_Normal) {
                ;
                MessageUtils.send_to_roundPlayer(this, ResMonsterHpChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }

            if (attacker != null) {
                if (attacker.getId() == this.getId()) {
                    MessageUtils.send_to_roundPlayer(this, ResMonsterHpChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
                }
            }

            //如果是守卫怪打的血量是需要向玩家同步的
//            if (attacker instanceof Monster) {
//                if (((Monster) attacker).getMonsterType() == MonsterDefine.MonsterType_type10) {
//                    MessageUtils.send_to_roundPlayer(this, ResMonsterHpChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
//                }
//                if (((Monster) attacker).getMonsterType() == MonsterDefine.MonsterType_type11) {
//                    MessageUtils.send_to_roundPlayer(this, ResMonsterHpChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
//                }
//            }
        }
    }

    @Override
    public String getName() {
        return ServerStr.getChatTableName(super.getName());
    }

    @Override
    public String getSrcName() {
        return super.getName();
    }

    @Override
    public String toString() {
        return "怪物[" + getName() + "] modelID=" + this.getModelId() + " id=" + this.getId() + " pos=" + this.gainCurPos();
    }

    @Override
    public int gainFinalMoveSpeed() {
        return (int) (getAttribute().gainFinalMoveSpeed() * speedRate);
    }

    public void setFixDecHp(long fixDecHp) {
        this.fixDecHp = fixDecHp;
    }

    @Override
    public long getFixDecHp() {
        return fixDecHp;
    }

    @Override
    public long getFightPoint() {
        return Manager.playerAttAttributeManager.deal().calcFightPower(attribute);
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public long getArmorbegintime() {
        return armorbegintime;
    }

    public void setArmorbegintime(long armorbegintime) {
        this.armorbegintime = armorbegintime;
    }

    public long getArmorzerotime() {
        return armorzerotime;
    }

    public void setArmorzerotime(long armorzerotime) {
        this.armorzerotime = armorzerotime;
    }

    public int getArmorState() {
        return armorState;
    }

    public void setArmorState(int armorState) {
        this.armorState = armorState;
    }

    public List<Long> getDropRoleIds() {
        return dropRoleIds;
    }

    public boolean isArmorTrue() {
        return armorTrue;
    }

    public void setArmorTrue(boolean armorTrue) {
        this.armorTrue = armorTrue;
    }

    public long getEndHitTime() {
        return endHitTime;
    }

    public void setEndHitTime(long endHitTime) {
        this.endHitTime = endHitTime;
    }

    public void setChangeStepState(String state) {
        stepState = state;
    }

    public String getChangeStepState() {
        return stepState;
    }

    public boolean isChangingStep() {
        return stepState != null && !stepState.equals("");
    }

    public void setMakerId(long makerId) {
        this.makerId = makerId;
    }

    @Override
    public long getMakerId() {
        return makerId;
    }

    public void setTrap(boolean trap) {
        this.trap = trap;
    }

    public boolean isTrap() {
        return trap;
    }

    public boolean isUseNewAi() {
        return useNewAi;
    }

    public void setUseNewAi(boolean useNewAi) {
        this.useNewAi = useNewAi;
    }

    public boolean isTdMonster() {
        return tdMonster;
    }

    public void setTdMonster(boolean tdMonster) {
        this.tdMonster = tdMonster;
    }

    public int getMoveStep() {
        return moveStep;
    }

    public void setMoveStep(int moveStep) {
        this.moveStep = moveStep;
    }

    public List<Position> getWillMovePos() {
        return willMovePos;
    }

    public long getCd_hurt() {
        return cd_hurt;
    }

    public void setCd_hurt(long cd_hurt) {
        this.cd_hurt = cd_hurt;
    }

    @Override
    public void release() {

    }
}
