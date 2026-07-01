package com.game.skill.structs;

import com.data.Global;
import com.data.bean.Cfg_Skill_Bean;
import com.game.behavior.manager.BehaviorManager;
import com.game.behavior.structs.BaseBehavior;
import com.game.behavior.structs.type.MagicAiBehavior;
import com.game.cooldown.structs.Cooldown;
import com.game.manager.Manager;
import com.game.map.manager.MapManager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.skill.config.SkillEvent;
import com.game.skill.config.SkillEventContainer;
import com.game.skill.config.SkillVisual;
import com.game.structs.*;
import com.game.yed.YedAI;
import com.game.yed.YedMgr;
import com.game.yed.scripts.YedMethodScript;
import game.core.map.Position;
import game.core.map.IMapObject;
import game.core.util.AutoIncrementIntArray;
import game.core.util.StringUtils;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author zenghai <zenghai@haowan123.com>
 */
public class SkillMagic extends Entity implements YedMethodScript {

    protected Logger log = LogManager.getLogger("SkillMagic");

    private String magicIndex; //魔法配置项
    private List<Integer> serials = new ArrayList<>(); //攻击序列
    private long ownerId; //释放者ID
    private int skillId; //从属技能ID
    private long start; //出生时间
    private long initTime; //进入地图时间
    private int intType; //初始位置类型
    private Skill skillinfo;

    private final List<AutoIncrementIntArray> listArray = new ArrayList<>();

    //冷却列表
    protected ConcurrentHashMap<String, Cooldown> cooldowns = new ConcurrentHashMap<>();

    public int getIntType() {
        return intType;
    }

    public void setIntType(int intType) {
        this.intType = intType;
    }

    public long getInitTime() {
        return initTime;
    }

    public void setInitTime(long initTime) {
        this.initTime = initTime;
    }

    @Override
    public float getRadius() {
        SkillVisual sv = SkillEventContainer.getInstance().getVisuals().get(magicIndex);
        if (sv == null) {
            return 0;
        }

        if (serials.size() == 0) {
            return 0;
        }
        SkillEvent event = sv.getEvents().get(serials.get(0));
        return event.getHitDis(event.getHitType());
    }

    @Override
    public int getType() {
        return Fighter.SKILLMAGIC_TYPE;
    }

    @Override
    public String getSrcName() {
        return null;
    }

    public int getDelay() {
        long delay = start - TimeUtils.Time();
        if (delay < 0) {
            return 0;
        }
        return (int) (delay);
    }

    public List<Integer> getSerials() {
        return serials;
    }

    public void setSerials(List<Integer> serials) {
        this.serials = serials;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public String getMagicIndex() {
        return magicIndex;
    }

    public void setMagicIndex(String magicIndex) {
        this.magicIndex = magicIndex;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    @Override
    public void reset() {

    }

    @Override
    public int gainFinalMoveSpeed() {
        return getAttribute().gainFinalMoveSpeed();
    }

    @Override
    public Fighter gainCurAttackTarget(MapObject map) {
        Entity owner = gainOwner(map);
        if(owner == null){
            return null;
        }
        return owner.gainCurAttackTarget(map);
    }

    @Override
    public Fighter gainFirstHatredTarget() {
        Entity owner = gainOwner();
        if(owner == null){
            return null;
        }
        return owner.gainFirstHatredTarget();
    }

    @Override
    public Position gainCurPos() {
        return getCurGps().getPos();
    }

    @Override
    public void changeCurPos(Position pos) {
        curGps.setPos(pos);
    }

    @Override
    public void changeCurPos(Position pos, boolean isBroadcast) {
        Position oldPos = this.gainCurPos();
        curGps.setPos(pos);
        boolean isChange = Manager.mapManager.changeArea(this, oldPos, pos);
        if (isChange) {
            // manager.mapManager.manager().OnChangePos(this, oldPos);
        }
    }

    @Override
    public void doDie(Fighter attacker) {

    }

    @Override
    public void beAttack(Fighter attacker, Cfg_Skill_Bean skill, int skilllevel, long damage) {

    }

    @Override
    public void onHpChange(Fighter attacker) {

    }

    @Override
    public boolean canSee(IMapObject player) {
        return true;
    }

    /**
     * 获取任务隐藏id集合
     *
     * @return
     */
    @Override
    public HashMap<Integer, List<Integer>> gainHideTaskIds() {
        return null;
    }

    @Override
    public long getBrithProtect() {
        return 0;
    }

    @Override
    public long getFixDecHp() {
        return 0;
    }

    @Override
    public ConcurrentHashMap<String, Cooldown> getCooldowns() {
        return cooldowns;
    }

    @Override
    public ConcurrentHashMap<Integer, Skill> getSkills() {
        return new ConcurrentHashMap<>();
    }

    @Override
    public long getFightPoint() {
        return Manager.playerAttAttributeManager.deal().calcFightPower(attribute);
    }

    public Skill getSkillinfo() {
        return skillinfo;
    }

    public void setSkillinfo(Skill skillinfo) {
        this.skillinfo = skillinfo;
    }

    public void UpdateBehavior(long offset) {
        Iterator<BaseBehavior> it = BehaviorList().iterator();
        while (it.hasNext()) {
            BaseBehavior bb = it.next();

            if (bb == null) {
                log.error("行为数据为空， 则清除！");
                it.remove();
                continue;
            }

            bb.Update(offset);
            if (bb.IsOver()) {
//                log.info(this.getName() + "(" + this.getId() + ") 行为=" + bb.getType());
                it.remove();
            }
        }
    }

    @Override
    public boolean isNeedCheckCanMove(){
        return false;
    }

    public void onCreate() {
        // 看看有没有合适的ai
        if(YedMgr.getInstance().IsExist(getMagicIndex())){
            YedAI ai = new YedAI(this);
            ai.Load(getMagicIndex(), false);
            changeRunningAi(ai);
            getAttribute().setAttribute(AttributeType.ATTR_Speed, Global.MoveSpeed);
            getAttribute().calFinalMoveSpeed();
            BehaviorManager.InsertBehavior(this, new MagicAiBehavior(this));
        }
    }

    public Entity gainOwner() {
        if(getOwnerId() <= 0){
            log.error("gainFirstHatredTarget getOwnerId() <= 0");
            return null;
        }
        MapObject map = MapManager.getInstance().getMap(this.gainMapId());
        if(map == null){
            log.error("gainFirstHatredTarget map == null");
            return null;
        }
        Entity owner = (Entity) MapUtils.getFighter(map, getOwnerId());
        if(owner == null){
            log.error("gainFirstHatredTarget owner == null");
            return null;
        }
        return owner;
    }

    public Entity gainOwner(MapObject map){
        if(getOwnerId() <= 0){
            log.error("gainFirstHatredTarget getOwnerId() <= 0");
            return null;
        }
        Entity owner = (Entity) MapUtils.getFighter(map, getOwnerId());
        if(owner == null){
            log.error("gainFirstHatredTarget owner == null");
            return null;
        }
        return owner;
    }

    public void setActionParam(String param) {
        listArray.clear();
        if(StringUtils.isEmpty(param)){
            return;
        }

        String[] strArray = param.split(";");
        for(int i = 0; i<strArray.length;i++){
            String[] valueArray = strArray[i].split("_");
            AutoIncrementIntArray value = new AutoIncrementIntArray(valueArray.length);
            for(int j = 0;j<valueArray.length;j++){
                if(StringUtils.isNumber(valueArray[j],'-')){
                    value.add(j,  Integer.parseInt(valueArray[j]));
                }else{
                    value.add(j,  0);
                }
            }
            listArray.add(value);
        }
    }

    final public List<AutoIncrementIntArray> getActionParam(){
        return listArray;
    }

    @Override
    public boolean execAiMethod(YedAI ai, String methodName, Object[] args) {
        return false;
    }


    @Override
    public void release() {

    }
// ai 接口实现
}
