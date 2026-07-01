package com.game.robot.struct;

import com.data.bean.Cfg_Skill_Bean;
import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseLongAttribute;
import com.game.behavior.manager.BehaviorManager;
import com.game.behavior.structs.BehaviorType;
import com.game.cooldown.structs.Cooldown;
import com.game.manager.Manager;
import com.game.map.structs.MapUtils;
import com.game.pet.structs.Pet;
import com.game.player.script.IPlayerBattle;
import com.game.player.structs.PlayerAttributeType;
import com.game.robot.ai.RobotAi;
import com.game.robot.manager.RobotManager;
import com.game.script.structs.ScriptEnum;
import com.game.skill.structs.Skill;
import com.game.structs.AttributeType;
import com.game.structs.Entity;
import com.game.structs.EntityState;
import com.game.structs.Fighter;
import com.game.yed.YedAI;
import game.core.map.IMapObject;
import game.core.map.Position;
import game.core.script.IScript;
import game.core.util.TimeUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.game.robot.manager.RobotManager.ROBOT_HELP_AI;

/**
 *
 * @author zenghai
 */
public class Robot extends Entity {

    private long makerId;  //制造者
    private RobotAi ai = RobotAi.None;
    private int career; // 职业
    private int weaponId; //神兵
    private int wingId; //翅膀
    private int mountId; //坐骑
    private int teamId; //组队Id
    private long guildId; //帮会ID
    private String guildName = ""; //帮会名字
    private int guildPost; //帮会职位
    private int title; //称号
    private float height; //高度
    private int picTitle; //图片称号
    private int fashionBodyId; //时装身体Id
    private int fashionWeaponId; //时装武器Id
    private int fashionHalo;
    private int fashionMatrix;
    private int grade; //转阶
    private long fightPower;//战斗力
    private int hairR = 0; //头发颜色红
    private int hairG = 0; //头发颜色绿
    private int hairB = 0; //头发颜色蓝
    private int hairId = 0; //头像ID
    private int hairFrameId = 0; //头框ID
    private int buchenVfxId = 0; //步尘ID
    private int stifleFabaoId = 0; //灵压法宝id
    private int godWeaponHead;//神兵头部
    private int godWeaponBody;//神兵身体部位

    private int godWeaponHeraldry;//神兵特效
    private int stateLv; //境界等级
    private int spiritId;//灵体外观
    private int soulArmorId; //魂甲外观

    /**
     * 主人ID
     */
    private long ownerId = 0;

    private long liveTime = 0;//机器人生命时间

    public Pet pet = null;

    private final Set<Integer> skillIds = new HashSet<>();

    //技能列表
    ConcurrentHashMap<Integer, Skill> skills = new ConcurrentHashMap<>();
    //技能加成列表
    private final ConcurrentHashMap<Integer, Integer> skilladds = new ConcurrentHashMap<>();
    //技能cd完成
    private transient List<Skill> readyLists = new ArrayList<>();

    //机器人苏醒时间
    public long BeginWork = -1;

    //翻滚等级
    private int rollLevel = 1; //翻滚等级
    //冷却列表
    protected ConcurrentHashMap<String, Cooldown> cooldowns = new ConcurrentHashMap<>();

    //BUFF 上一次的BUFF属性
    private final BaseIntAttribute buffAttr = new BaseIntAttribute(AttributeType.ATTR_MAX);

    //属性列表
    private final transient ConcurrentHashMap<PlayerAttributeType, BaseIntAttribute> playerCalculators = new ConcurrentHashMap<>();

    public int getStifleFabaoId() {
        return stifleFabaoId;
    }

    public void setStifleFabaoId(int stifleFabaoId) {
        this.stifleFabaoId = stifleFabaoId;
    }

    public int getGodWeaponHead() {
        return godWeaponHead;
    }

    public void setGodWeaponHead(int godWeaponHead) {
        this.godWeaponHead = godWeaponHead;
    }

    public int getGodWeaponBody() {
        return godWeaponBody;
    }

    public void setGodWeaponBody(int godWeaponBody) {
        this.godWeaponBody = godWeaponBody;
    }

    public int getGodWeaponHeraldry() {
        return godWeaponHeraldry;
    }

    public void setGodWeaponHeraldry(int godWeaponHeraldry) {
        this.godWeaponHeraldry = godWeaponHeraldry;
    }

    public int getStateLv() {
        return stateLv;
    }

    public void setStateLv(int stateLv) {
        this.stateLv = stateLv;
    }

    @Override
    public void release() {

    }

    public enum AiParam{
        robot(0),
        player(1),
        map(2),;

        public int value;
        AiParam(int v){
            value = v;
        }
    }

    @Override
    public int getType() {
        return Fighter.ROBOT_TYPE;
    }

    @Override
    public String getSrcName() {
        return null;
    }


    @Override
    public BaseLongAttribute getAttribute() {
        return attribute;
    }

    public void setAttribute(BaseLongAttribute attribute) {
        this.attribute = attribute;
    }

    @Override
    public ConcurrentHashMap<String, Cooldown> getCooldowns() {
        return cooldowns;
    }

    public int getRollLevel() {
        return rollLevel;
    }

    public void setRollLevel(int rollLevel) {
        this.rollLevel = rollLevel;
    }

    public void setCooldowns(ConcurrentHashMap<String, Cooldown> cooldowns) {
        this.cooldowns = cooldowns;
    }

    @Override
    public List<Skill> getReadyLists() {
        return readyLists;
    }

    public RobotAi getAi() {
        return ai;
    }

    public void setAi(RobotAi tai) {
        this.ai = tai;
        if (ai == RobotAi.Help) {
            YedAI yai = new YedAI(RobotManager.getInstance().yedAi());
            yai.Load(ROBOT_HELP_AI, false);
            changeRunningAi(yai);
        }
    }

    @Override
    public long getMakerId() {
        return makerId;
    }

    public void setMakerId(long makerId) {
        this.makerId = makerId;
    }

    public int getFashionBodyId() {
        return fashionBodyId;
    }

    public void setFashionBodyId(int fashionBodyId) {
        this.fashionBodyId = fashionBodyId;
    }

    public int getFashionWeaponId() {
        return fashionWeaponId;
    }

    public void setFashionWeaponId(int fashionWeaponId) {
        this.fashionWeaponId = fashionWeaponId;
    }

    public int getFashionHalo() {
        return fashionHalo;
    }

    public void setFashionHalo(int fashionHalo) {
        this.fashionHalo = fashionHalo;
    }

    public int getFashionMatrix() {
        return fashionMatrix;
    }

    public void setFashionMatrix(int fashionMatrix) {
        this.fashionMatrix = fashionMatrix;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getPicTitle() {
        return picTitle;
    }

    public void setPicTitle(int picTitle) {
        this.picTitle = picTitle;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    public int getGuildPost() {
        return guildPost;
    }

    public void setGuildPost(int guildPost) {
        this.guildPost = guildPost;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getWeaponId() {
        return weaponId;
    }

    public void setWeaponId(int weaponId) {
        this.weaponId = weaponId;
    }

    public int getWingId() {
        return wingId;
    }

    public void setWingId(int wingId) {
        this.wingId = wingId;
    }

    public int getMountId() {
        return mountId;
    }

    public void setMountId(int mountId) {
        this.mountId = mountId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getCareer() {
        return career;
    }

    public void setCareer(int career) {
        this.career = career;
    }

    public int getSpiritId() {
        return spiritId;
    }

    public void setSpiritId(int spiritId) {
        this.spiritId = spiritId;
    }

    @Override
    public ConcurrentHashMap<Integer, Skill> getSkills() {
        return skills;
    }

    public void setSkills(ConcurrentHashMap<Integer, Skill> skills) {
        this.skills = skills;
    }

    @Override
    public void reset() {
        Manager.cooldownManager.cleanAllCooldown(this);
        this.clearHatred();
        this.setCurHp(getAttribute().MaxHP());
        this.resetState();
    }

    @Override
    public int gainFinalMoveSpeed() {
        return attribute.gainFinalMoveSpeed();
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
            Manager.mapManager.manager().OnChangePos(this, oldPos);
        }
    }

    @Override
    public void doDie(Fighter attacker) {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.PlayerBattleBaseScript);
        if (is instanceof IPlayerBattle) {
            ((IPlayerBattle) is).doDie(this, attacker);
        } else {
            logger.error("没有找到玩家死亡的脚本实例！");
        }
    }

    @Override
    public void beAttack(Fighter attacker, Cfg_Skill_Bean skill, int skilllevel, long damage) {

        if (EntityState.Move.compare(this.getState())) {
            BehaviorManager.CancelBehaviorByType(this, BehaviorType.Move);
            BehaviorManager.CancelBehaviorByType(this, BehaviorType.Run);
        }
        this.setInBattle(true);
        this.setLastFight(TimeUtils.Time());
        this.addHatred(attacker, damage);
    }

    public int getSoulArmorId() {
        return soulArmorId;
    }

    public void setSoulArmorId(int soulArmorId) {
        this.soulArmorId = soulArmorId;
    }

    @Override
    public void onHpChange(Fighter attacker) {
        MapUtils.sendHpChange(this);
    }

    @Override
    public long getBrithProtect() {
        return 0;
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
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("robot=[");
        builder.append(name);
        builder.append("] robotId=");
        builder.append(id);
        builder.append(" maker=");
        builder.append(makerId);
        return builder.toString();
    }

    public ConcurrentHashMap<Integer, Integer> getSkilladds() {
        return skilladds;
    }

    @Override
    public long getFixDecHp() {
        return 0;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public long getFightPower() {
        return fightPower;
    }

    public void setFightPower(long fightPower) {
        this.fightPower = fightPower;
    }

    @Override
    public long getFightPoint() {
        return getFightPower();
    }

    private final ConcurrentHashMap<Integer, Pet> pets = new ConcurrentHashMap<>(); //玩家激活的宠物列表<宠物模型Id, Pet>

    public ConcurrentHashMap<Integer, Pet> getPets() {
        return pets;
    }

    public BaseIntAttribute getBuffAttr() {
        return buffAttr;
    }

    public ConcurrentHashMap<PlayerAttributeType, BaseIntAttribute> getPlayerCalculators() {
        return playerCalculators;
    }

    public Set<Integer> getSkillIds() {
        return skillIds;
    }

    public void setHairR(int hairR){this.hairR = hairR;}

    public int getHairR(){return hairR;}

    public void setHairB(int hairB){this.hairB = hairB;}

    public int getHairB(){return  hairB;}

    public void  setHairG(int hairG){this.hairG = hairG;}

    public int getHairG(){return hairG;}

    public void setHairId(int hairId){this.hairId = hairId;}

    public int getHairId(){return hairId;}

    public void setHairFrameId(int hairFrameId){this.hairFrameId = hairFrameId;}

    public int getHairFrameId(){return hairFrameId;}

    public void setBuchenVfxId(int buchenVfxId){this.buchenVfxId = buchenVfxId;}

    public int getBuchenVfxId(){return  buchenVfxId;}

    public void setLiveTime(long liveTime){this.liveTime = liveTime;}

    public long getLiveTime(){return liveTime;}

    /**
     * 获取 主人ID
     *
     * @return ownerId 主人ID
     */
    public long getOwnerId() {
        return this.ownerId;
    }

    /**
     * 设置 主人ID
     *
     * @param ownerId 主人ID
     */
    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }


}
