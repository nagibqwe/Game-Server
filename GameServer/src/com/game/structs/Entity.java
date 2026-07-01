package com.game.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.attribute.BaseLongAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.behavior.manager.BehaviorManager;
import com.game.behavior.structs.BaseBehavior;
import com.game.behavior.structs.BehaviorType;
import com.game.behavior.structs.type.AttackMoveBehavior;
import com.game.buff.structs.Buff;
import com.game.entity.script.IEntityScript;
import com.game.fight.state.FightState;
import com.game.fight.structs.FightEnum;
import com.game.fight.structs.FightSkillEvent;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapGps;
import com.game.map.structs.MapUtils;
import com.game.player.structs.Player;
import com.game.revive.structs.ReviveData;
import com.game.script.structs.ScriptEnum;
import com.game.skill.structs.Skill;
import com.game.skill.structs.SkillLockTrajectory;
import com.game.utils.RandomUtils;
import com.game.utils.Utils;
import com.game.yed.YedAI;
import game.core.map.Position;
import game.core.script.IScript;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基本实体类
 **/
public abstract class Entity extends GameObject implements Fighter {

    @JsonIgnore
    protected transient static final Logger logger = LogManager.getLogger(Entity.class);

    //基础属性
    @JsonIgnore
    protected transient BaseLongAttribute attribute = new BaseLongAttribute(AttributeType.ATTR_MAX);
    @JsonIgnore
    protected transient BaseSystemIntAttribute sysAttriBute = new BaseSystemIntAttribute(AttributeType.SystemAttr_Max);
    @JsonIgnore
    protected transient BaseLongAttribute fightPowerAttribute = new BaseLongAttribute(AttributeType.ATTR_MAX);
    //技能加成列表
    @JsonIgnore
    private transient ConcurrentHashMap<Integer, Integer> skilladds = new ConcurrentHashMap<>();

    //对象模板Id
    protected int modelId;
    @JsonIgnore
    protected transient int createTime; //创建时间
    //地图信息
    protected MapGps curGps = new MapGps();
    @JsonIgnore
    protected transient List<BaseBehavior> behaviorList = new ArrayList<>(); //行为列表
    //==============================移动相关=============================================
    @JsonIgnore
    protected transient Position dir = new Position(0, -1); //面向方向
    @JsonIgnore
    protected transient List<Position> roads = new ArrayList<>(); //移动路径
    @JsonIgnore
    protected transient HashMap<Integer, Long> dirs = new HashMap<>(); //8方向标志
    @JsonIgnore
    protected transient List<AttackMoveBehavior> ambs = new ArrayList<>(); //受击移动行为

    //==============================战斗相关=============================================
    //当前生命
    protected long curHp;
    //当前灵体
    protected long curWakan;
    protected List<Buff> buffs = new ArrayList<>(); //BUFF列表
    @JsonIgnore
    protected transient List<Hatred> hatreds = new ArrayList<>(); //仇恨列表
    @JsonIgnore
    private transient long curAttackTargetId; //当前攻击对象（亦宠物主攻击目标）
    @JsonIgnore
    protected transient int fightState; //战斗状态
    @JsonIgnore
    protected transient List<FightEnum> fightEnums = new ArrayList<>(); //战斗状态列表
    @JsonIgnore
    protected transient List<FightState> fightStates = new ArrayList<>(); //战斗状态列表
    @JsonIgnore
    protected transient Skill useSkill; //当前useSkill
    @JsonIgnore
    private transient Skill curSlowSkill; //当前使用的吟唱
    @JsonIgnore
    protected transient List<Long> magics = new ArrayList<>(); //召唤物
    @JsonIgnore
    protected transient final List<FightSkillEvent> willEvents = new ArrayList<>(); //等待需要执行技能事件
    @JsonIgnore
    protected transient final List<SkillLockTrajectory> lockTrajectories = new ArrayList<>(); //在正执行的弹道参数

    //==============================属性相关=============================================
    //等级
    protected int level;
    //模型半径
    protected float radius = 1.0f;
    //名字
    protected String name = "";
    //头像
    @JsonIgnore
    protected transient int icon;
    //阵营
    @JsonIgnore
    protected transient int camp;
    //生物状态
    protected int state;

    //是否在战斗状态中
    @JsonIgnore
    transient boolean isInBattle = false;

    @JsonIgnore
    private transient List<Skill> readyLists = new ArrayList<>();
    @JsonIgnore
    private final transient Queue<Skill> aipushLists = new LinkedList<>();
    @JsonIgnore
    protected transient boolean hideMe = false; //主角对于其他人是隐身的
    @JsonIgnore
    protected transient boolean hideOther = false; //其他人对于主角是隐身的
    @JsonIgnore
    private transient boolean show = true;
    @JsonIgnore
    private transient long lastFight = 0; //最后一次战斗
    @JsonIgnore
    private transient long breakWakanFight = 0;  //最后一次破盾时间
    @JsonIgnore
    private transient int skillTargetFilter = 0; // 技能目标筛选器,不同的对象在不同的场合会有不同的筛选器
    @JsonIgnore
    private transient YedAI runningAi = null; // 当前ai
    @JsonIgnore
    private transient int changeModelID =0; //变身ID
    @JsonIgnore
    private transient ReviveData reviveData = new ReviveData(); //复活数据

    public ReviveData getReviveData() {
        return reviveData;
    }

    public void setReviveData(ReviveData reviveData) {
        this.reviveData = reviveData;
    }

    public long getMakerId() {
        return 0;
    }

    public void stopMove() {
        removeSate(EntityState.Move);
        BehaviorManager.CancelBehaviorByType(this, BehaviorType.Move);
        BehaviorManager.CancelBehaviorByType(this, BehaviorType.Run);
        BehaviorManager.CancelBehaviorByType(this, BehaviorType.AttackMove);
        BehaviorManager.CancelBehaviorByType(this, BehaviorType.SkillMove);
        Manager.mapManager.synStopMove(this, false);
    }

    @Override
    public List<SkillLockTrajectory> getLockTrajectories() {
        return lockTrajectories;
    }

    public Queue<Skill> getAipushLists() {
        return aipushLists;
    }

    public boolean isNeedCheckCanMove() {
        return true;
    }

    public enum AiParam {

        entity(0),;

        public int value;

        AiParam(int v) {
            value = v;
        }
    }

    public BaseLongAttribute getFightPowerAttribute() {
        return fightPowerAttribute;
    }

    public void setFightPowerAttribute(BaseLongAttribute fightPowerAttribute) {
        this.fightPowerAttribute = fightPowerAttribute;
    }

    public List<BaseBehavior> BehaviorList() {
        return behaviorList;
    }

    @Override
    public List<FightSkillEvent> willEvents() {
        return willEvents;
    }

    @Override
    public List<Long> getMagics() {
        return magics;
    }

    @Override
    public BaseLongAttribute getAttribute() {
        return attribute;
    }

    public BaseSystemIntAttribute getSysAttriBute() {
        return sysAttriBute;
    }

    @Override
    public ConcurrentHashMap<Integer, Integer> getSkilladds() {
        return skilladds;
    }

    public void setSkilladds(ConcurrentHashMap<Integer, Integer> skilladds) {
        this.skilladds = skilladds;
    }

    @Override
    public Skill getUseSkill() {
        return useSkill;
    }

    @Override
    public void setUseSkill(Skill useSkill) {
        this.useSkill = useSkill;
    }

    @Override
    public Skill getCurSlowSkill() {
        return curSlowSkill;
    }

    @Override
    public void setCurSlowSkill(Skill curSlowSkill) {
        this.curSlowSkill = curSlowSkill;
    }

    public List<Skill> getReadyLists() {
        return readyLists;
    }

    @Override
    public List<AttackMoveBehavior> getAmbs() {
        return ambs;
    }


    @Override
    public int getState() {
        return state;
    }

    public boolean isInBattle() {
        return isInBattle;
    }

    public void setInBattle(boolean isin) {
        if (isInBattle == isin) {
            return;
        }
        boolean old = isInBattle;
        isInBattle = isin;
        OnInBattleChange(old);
    }

    /**
     * 当战斗状态改变时候的事件触发
     *
     * @param old
     */
    protected void OnInBattleChange(boolean old) {

    }
    
    public void setState(int state) {
        this.state = state;
    }

    @Override
    public Position getDir() {
        return dir;
    }

    public void setDir(Position dir) {
        this.dir = dir;
    }

    @Override
    public Position gainCurPos() {
        return curGps.getPos();
    }

    public boolean iShow() {
        return show;
    }

    public void seShow(boolean show) {
        this.show = show;
    }

    @Override
    public int getCamp() {
        return camp;
    }

    public void setCamp(int camp) {
        this.camp = camp;
    }

    public void setCamp(int camp, boolean isUpdata) {
        this.camp = camp;
        MapUtils.synUpdateCamp(this);
    }

    public HashMap<Integer, Long> getDirs() {
        return dirs;
    }

    @Override
    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public List<Hatred> getHatreds() {
        return hatreds;
    }

    /**
     * 检查是否包含一个战斗仇恨对象
     *
     * @param fighter
     * @return
     */
    public Hatred haveHatreds(Fighter fighter) {
        Iterator<Hatred> iter = this.getHatreds().iterator();
        while (iter.hasNext()) {
            Hatred hatred = iter.next();
            if (hatred.getTarget() == fighter) {
                return hatred;
            }
        }
        return null;
    }

    //清空仇恨对象
    public void clearHatred() {
        try {
            if(!Manager.worldHelpManager.getScript().canClear(this)) {
                return;
            }
            List<Hatred> temp = new ArrayList<>(this.getHatreds());
            this.getHatreds().clear();
            Iterator<Hatred> iter = temp.iterator();
            while (iter.hasNext()) {
                Hatred hatred = iter.next();
                if (hatred == null) {
                    continue;
                }
                Manager.hatredManager.clearHatred(hatred);
            }
            Manager.worldHelpManager.getScript().clearHatred(id);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

    //移除仇恨对象
    public boolean removeHatred(Fighter attacker) {
        //LOGGER.info(name + " (" + id + ") 清理仇恨 ＝" + attacker.getName());
        for (Hatred hatred : hatreds) {
            if (hatred.getTarget().getId() == attacker.getId()) {
//                if(this instanceof Player && hatred.getTarget() instanceof Player){
//                    LOGGER.error(String.format("ai debug removeHatred %s target:%s", this.getName(), attacker.getName()), new Throwable());
//                }
                removeHatred(hatred);
                return true;
            }
        }
        return false;
    }

    public void removeHatred(Hatred hatred) {
        if (null != hatred) {
//            if(this instanceof Player && hatred.getTarget() instanceof Player){
//                LOGGER.error(String.format("ai debug removeHatred %s target:%s", this.getName(), hatred.getTarget().getName()), new Throwable());
//            }
            //LOGGER.info(name + " (" + id + ") 清理仇恨 ＝" + hatred.getTarget().getName());
            hatreds.remove(hatred);
            Manager.hatredManager.clearHatred(hatred);

            if (hatred.getTarget() != null)
                Manager.worldHelpManager.getScript().removeHatred(id, hatred.getTarget().getId());
        }
    }

    //添加仇恨
    public boolean addHatred(Fighter attacker, long hate) {
        if (attacker == null) {
            return false;
        }
        Hatred hatred = null;
        boolean isFresh = false;
        if (attacker.getId() == this.getId()) {
            logger.error(getName() + "在增加自己为仇人哦！", new NullPointerException());
            return false;
        }

        //LOGGER.info("怪物" + getName() + "增加对" + attacker.getName() + "的仇恨" + hate);
        //查找仇恨对象
        Iterator<Hatred> iter = hatreds.iterator();
        while (iter.hasNext()) {
            Hatred hatered = iter.next();
            if (hatered == null) {
                iter.remove();
                continue;
            }

            if (hatered.getTarget() == null) {
                iter.remove();
                continue;
            }

            if (hatered.getTarget().isDie()) {
                if(hatered.getTarget() instanceof Player){
                    if(!Manager.worldHelpManager.getScript().canClear((Player)hatered.getTarget())){
                        if(!Manager.worldHelpManager.getScript().isHelp(hatered.getTarget().getId())){
                            iter.remove();
                            continue;
                        }
                    }
                }
                iter.remove();
                continue;
            }

            if (hatered.getTarget().gainMapId() != gainMapId()) {
                iter.remove();
                continue;
            }

            if (hatered.getTarget().getId() == attacker.getId()) {
                hatred = hatered;
                iter.remove();
            }
        }
        //如果没有新建一个
        if (null == hatred) {
            isFresh = true;
            hatred = Manager.hatredManager.getHatred();
            hatred.setTarget(attacker);
            hatred.setFirstAttackTime(TimeUtils.Time());
        }
        //增加仇恨值
        hatred.addHatred(hate);
        hatred.setLastAttackTime(TimeUtils.Time());
        //插入仇恨列表（按仇恨值大小排列）
        for (int i = 0; i < hatreds.size(); i++) {
            if (hatreds.get(i).getHatred() < hatred.getHatred()) {
                hatreds.add(i, hatred);
                return isFresh;
            }
        }
        hatreds.add(hatred);
        return isFresh;
    }

    public float gainX() {
        return curGps.getPos().getX();
    }

    public float gainY() {
        return curGps.getPos().getY();
    }

    //获取最近的方向
    public int getNearDir(float x, float y) {
        float value = y / x;
        // 妈蛋，没找到现成的三角函数
        // 8个角度 0：→， 1：↗， 2：↑， 3：↖， 4：←， 5：↙， 6：↓， 7：↘
        // tan22.5° == 0.55 tan45° == 1.61 tan67.5° == 22.58 tan90°== +∞
        if (x > 0) {
            if (value < 0.55 && value > -0.55) {
                return 4;
            }
            if (value < 22.58 && value >= 0.55) {
                return 5;
            }
            if (value >= 22.58) {
                return 6;
            }
            if (value < -0.55 && value >= -22.58) {
                return 3;
            }
            return 2;

        } else {
            if (value < 0.55 && value > -0.55) {
                return 0;
            }
            if (value < 22.58 && value >= 0.55) {
                return 1;
            }
            if (value >= 22.58) {
                return 2;
            }
            if (value < -0.55 && value >= -22.58) {
                return 7;
            }
            return 6;
        }

    }

    private int repairDir(int dir) {
        if (dir < 0) {
            return repairDir(dir + 8);
        }
        if (dir > 7) {
            return repairDir(dir - 8);
        }
        return dir;
    }

    //找空的车位
    public int repairDir(int dir, int r, int l) {
        if (r > 6 || l > 6) {
            return RandomUtils.random(0, 7);
        }
        int index = repairDir(dir);

        if (!dirs.containsKey(index)) {
            return index;
        }
        //找附近车位
        if (r > l) {
            r++;
            dir += (r + l);
        } else {
            l++;
            dir -= (r + l);
        }
        return repairDir(dir, r, l);
    }

    /**
     * curPos的8个角度 0：→， 1：↗， 2：↑， 3：↖， 4：←， 5：↙， 6：↓， 7：↘
     *
     * @param attacker 追击者
     * @param dis      距离
     * @return 获取追击点
     */
    @Override
    public Position getPursuePos(Fighter attacker, float dis) {
        float x = curGps.getPos().getX() - attacker.gainCurPos().getX();
        float y = curGps.getPos().getY() - attacker.gainCurPos().getY();

        int tempDir = -1;
        //有自己的方位找出来
        if (dirs.containsValue(attacker.getId())) {
            for (int i = 0; i < 8; i++) {
                if (dirs.get(i) == null) {
                    continue;
                }
                if (dirs.get(i) == attacker.getId()) {
                    tempDir = i;
                    break;
                }
            }
        }
        //没有方位，找一个最近的空位
        if (tempDir == -1) {
            tempDir = getNearDir(x, y);
            tempDir = repairDir(tempDir, 0, 0);
            dirs.put(tempDir, attacker.getId());
        }

        dis = RandomUtils.randomFloatValue(0.7f, 1.0f) * dis;
        switch (tempDir) {
            case 0:
                x = gainX() + dis;
                y = gainY();
                break;
            case 1:
                x = gainX() + dis * 0.7f;
                y = gainY() + dis * 0.7f;
                break;
            case 2:
                x = gainX();
                y = gainY() + dis;
                break;
            case 3:
                x = gainX() - dis * 0.7f;
                y = gainY() + dis * 0.7f;
                break;
            case 4:
                x = gainX() - dis;
                y = gainY();
                break;
            case 5:
                x = gainX() - dis * 0.7f;
                y = gainY() - dis * 0.7f;
                break;
            case 6:
                x = gainX();
                y = gainY() - dis;
                break;
            case 7:
                x = gainX() + dis * 0.7f;
                y = gainY() - dis * 0.7f;
                break;

            default:

                x = gainX();
                y = gainY();
        }
        return new Position(x, y);
    }

    //获取半包围据点
    @Override
    public Position getAiPos(Fighter attacker, float dis) {
        Position m_dir = Utils.getDir(this.gainCurPos(), attacker.gainCurPos());
        boolean isDirX = TimeUtils.Time() % 2 == 0;
        float dirx = isDirX ? -m_dir.getX() : m_dir.getX();
        float diry = isDirX ? m_dir.getY() : -m_dir.getY();
        m_dir.setX(dirx);
        m_dir.setY(diry);
        Position temp = Utils.getPosByDir(attacker.gainCurPos(), m_dir, dis * RandomUtils.randomFloatValue(100f, 150f) / 300f);
        m_dir = Utils.getDir(this.gainCurPos(), temp);
        return Utils.getPosByDir(this.gainCurPos(), m_dir, dis * 0.9f);

    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    @Override
    public int gainLine() {
        return curGps.getLine();
    }

    public void changeLine(int line) {
        this.curGps.setLine(line);
    }

    @Override
    public long gainMapId() {
        return curGps.getMapId();
    }

    public void changeMapId(long mapId) {
        curGps.setMapId(mapId);
    }

    @Override
    public int gainMapModelId() {
        return curGps.getModelId();
    }

    public void changeMapModelId(int mapModelId) {
        curGps.setModelId(mapModelId);
    }

    public List<Position> getRoads() {
        return roads;
    }

    public void clearRoads() {
        roads.clear();
    }

    public void setRoads(List<Position> roads) {
        this.roads = roads;
    }

    @Override
    public long getCurHp() {
        return curHp;
    }

    @Override
    public void setCurHp(long curHp) {
        this.curHp = curHp;
    }

    @Override
    public long getCurWakan() {
        return curWakan;
    }

    @Override
    public void setCurWakan(long curWakan) {
        this.curWakan = curWakan;
    }

    //获取血量百分比
    public int checkHpPercent() {
        if (this.getCurHp() <= 0) {
            return 0;
        }
        float fhp = 100.0f * this.getCurHp() / this.getAttribute().MaxHP();
        if (fhp > 99f) {
            return 100;
        }
        int ihp = (int) fhp;
        if (fhp - ihp > 0f) {
            return ihp + 1;
        }
        return ihp;
    }

    @Override
    public List<Buff> getBuffs() {
        return buffs;
    }

    public Buff getBuffbyId(int buffId) {
        if (buffs == null) {
            return null;
        }
        for (Buff b : buffs) {
            if (b.getBuffId() == buffId) {
                return b;
            }
        }
        return null;
    }

    public void setBuffs(List<Buff> buffs) {
        this.buffs = buffs;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    @Override
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int getFightState() {
        return fightState;
    }

    public void setFightState(int fightState) {
        this.fightState = fightState;
    }

    public List<FightEnum> getFightEnums() {
        return fightEnums;
    }

    //添加战斗事件
    @Override
    public void addFightState(FightState state, int time) {
        if (time < 1) {
            return;
        }
        state.setOverTime(TimeUtils.Time() + time);
        state.add(this);
        fightStates.add(state);
    }

    public List<FightState> getFightStates() {
        return fightStates;
    }

    /**
     * 增加战斗状态
     *
     * @param state
     */
    @Override
    public void addFightState(FightEnum state) {
        int old = this.fightState;
        this.fightState = 0;

        fightEnums.add(state);
        for (FightEnum fs : fightEnums) {
            this.fightState = this.fightState | fs.getState();
        }
        if (old != fightState) {
            MapUtils.sendFightState(this);
        }
        //LOGGER.info("[" + this.getName() + "]添加状态" + state);
    }

    /**
     * 移除战斗状态
     *
     * @param state
     */
    @Override
    public void removeFightState(FightEnum state) {

        int old = this.fightState;
        this.fightState = 0;
        Iterator<FightEnum> iter = fightEnums.iterator();
        while (iter.hasNext()) {
            FightEnum next = iter.next();
            if (next == state) {
                iter.remove();
                break;
            }
        }

        for (FightEnum fs : fightEnums) {
            this.fightState = this.fightState | fs.getState();
        }

        if (old != fightState) {
            MapUtils.sendFightState(this);
        }

        //LOGGER.info("[" + this.getName() + "]删除状态" + state);
    }

    @Override
    public boolean isDie() {
        return this.curHp <= 0;
    }

    public void setDie() {
        addState(EntityState.Dead);
        //死亡的时候要清理事件
        willEvents().clear();
        //死亡的时候要清理弹道
        getLockTrajectories().clear();
        //仇恨清除
        clearHatred();

        this.curHp = 0;
    }

    public boolean isHideMe() {
        return hideMe;
    }

    public void setHideMe(boolean hideMe) {
        this.hideMe = hideMe;
    }

    public boolean isHideOther() {
        return hideOther;
    }

    public void setHideOther(boolean hideOther) {
        this.hideOther = hideOther;
    }

    @Override
    public MapGps getCurGps() {
        return curGps;
    }

    public void setCurGps(MapGps curGps) {
        this.curGps = curGps;
    }

    @Override
    public boolean isEqualMap(Fighter comper) {

        return this.getCurGps().getMapId() == comper.getCurGps().getMapId()
                && this.getCurGps().getModelId() == comper.getCurGps().getModelId()
                && this.getCurGps().getLine() == comper.getCurGps().getLine();

    }

    /**
     * 返回当前实例名字与ID
     *
     * @return
     */
    @Override
    public String nameIdString() {
        return name + "(" + getId() + ")";
    }

    //重置状态
    public abstract void reset();


    //添加
    public final void addState(EntityState state) {
        int old = this.state;
        this.state = this.state | state.getValue();
        OnAddState(old);
    }

    protected void OnAddState(int old) {
        if (EntityState.Sitting.compare(old)) {
            removeSate(EntityState.Sitting);
        }
    }

    //移除状态
    public final void removeSate(EntityState state) {

        if (this instanceof Player) {
            if (EntityState.Sitting.compare(this.state) && state == EntityState.Sitting) {
                Manager.playerHookManager.deal().afterEndSitDown((Player) this);
            }
        }

        int old = this.state;
        this.state = ~state.getValue() & this.state;
        OnRemoveState(old);
    }

    protected void OnRemoveState(int old) {

    }


    //获取最终移动速度
    public abstract int gainFinalMoveSpeed();


    //重置状态到站立状态
    public final void resetState() {
        int old = state;
        this.state = EntityState.Stand.getValue();
        OnResetState(old);
    }

    protected void OnResetState(int old) {

    }

    public long getLastFight() {
        return lastFight;
    }

    public void setLastFight(long lastFight) {
//        if(this instanceof Player){
//            LOGGER.error(String.format("ai debug setLastFight %s dt:%d last:%d", getName(), lastFight - TimeUtils.Time(), lastFight), new Throwable());
//        }
        this.lastFight = lastFight;
    }

    public long getBreakWakanFight() {
        return breakWakanFight;
    }

    public void setBreakWakanFight(long breakWakanFight) {
        this.breakWakanFight = breakWakanFight;
    }

    public long gainCurAttackTargetId() {
        return curAttackTargetId;
    }

    public Fighter gainCurAttackTarget(MapObject map) {
        if (curAttackTargetId == 0) {
            return null;
        }
        Fighter ret = MapUtils.getFighter(map, curAttackTargetId);
        if (ret == null) {
            setCurAttackTargetId(0);
        }
        return ret;
    }

    public void setCurAttackTargetId(long curAttackTargetId) {
        this.curAttackTargetId = curAttackTargetId;
    }

    public ISkillTargetChecker gainSkillTargetFilter() {
        if (skillTargetFilter <= 0) {
            return null;
        }
        IScript is = Manager.scriptManager.GetScriptClass(skillTargetFilter);
        if (is == null || !(is instanceof ISkillTargetChecker)) {
            return null;
        }
        return (ISkillTargetChecker) is;
    }

    public void changeSkillTargetFilter(int skillTargetFilter) {
        this.skillTargetFilter = skillTargetFilter;
    }

    public Fighter gainFirstHatredTarget() {
        if (hatreds.isEmpty()) {
            return null;
        }
        Fighter ret = hatreds.get(0).getTarget();

        if (!isEqualMap(ret)) {
            // 删掉,递归再找
            if (removeHatred(ret)) {
                return gainFirstHatredTarget();
            }
            return null;
        }
        if (ret.isDie()) {
            // 删掉,递归再找
            if (removeHatred(ret)) {
                return gainFirstHatredTarget();
            }
            return null;
        }
        return ret;
    }

    public void changeRunningAi(YedAI ai) {
        runningAi = ai;
    }

    public YedAI gainRunningAi() {
        return runningAi;
    }

    public IEntityScript entityScript() throws Exception {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.EntityCommonScript);
        if (is instanceof IEntityScript) {
            return (IEntityScript) is;
        }
        throw new Exception("没有实现离线接口类IOfflinePlayerScript！");
    }

    @Override
    public void setChangeModelID(int modelID){
        changeModelID = modelID;
    }
    @Override
    public int  getChangeModleID(){
        return  changeModelID;
    }
    @Override
    public boolean getChangeModelState(){
        return changeModelID>0;
    }
}
