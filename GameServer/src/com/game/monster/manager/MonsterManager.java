package com.game.monster.manager;

import com.data.CfgManager;
import com.data.bean.Cfg_Monster_Bean;
import com.data.bean.Cfg_Skill_Bean;
import com.data.struct.ReadArray;
import com.game.attribute.BaseLongAttribute;
import com.game.cooldown.structs.CooldownTypes;
import com.game.manager.Manager;
import com.game.map.structs.ByteMapCfg;
import com.game.map.structs.ByteMapItem;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.monster.script.IMonsterAction;
import com.game.monster.script.IMonsterAi;
import com.game.monster.script.ITaskEntityIsShow;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.skill.structs.Skill;
import com.game.structs.AttributeType;
import com.game.structs.EntityState;
import com.game.structs.ServerStr;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import game.core.map.Position;
import game.core.script.IScript;
import game.core.util.TimeUtils;
import game.message.MapMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;

/**
 * @author
 */
public class MonsterManager {

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        INSTANCE;
        MonsterManager manager;

        Singleton() {
            this.manager = new MonsterManager();
        }

        MonsterManager getProcessor() {
            return manager;
        }
    }

    public static MonsterManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    private final static Logger log = LogManager.getLogger(MonsterManager.class);

    public boolean addMonsterBaseInfo(Monster monster, boolean init, int attBei) {
        if (attBei <= 0) {
            attBei = 10000;
        }

        Cfg_Monster_Bean monsterCfg = CfgManager.getCfg_Monster_Container().getValueByKey(monster.getModelId());
        if (null == monsterCfg) {
            log.error("monsterCfg 没有这个怪物" + monster.getModelId());
            return false;
        }
        monster.setName(monsterCfg.getName());
        monster.setIcon(monsterCfg.getIcon());
        monster.setLevel(monsterCfg.getLevel());
        monster.setFixDecHp(monsterCfg.getFixDecHp());
        monster.setMonsterType(monsterCfg.getMonster_type());
        monster.setRadius((monsterCfg.getSize_scale() * monsterCfg.getStrike_distance()) / 10000.0f);
        monster.setBrithProtect(TimeUtils.Time() + monsterCfg.getBrithProtect());
        monster.setScore(monsterCfg.getScore());

        //是初始化时， 护甲数据处理
        if (init) {
            monster.setCamp(monsterCfg.getCamp());
            monster.setCurHp(monsterCfg.getMaxHp());
            monster.setAttBei(attBei);
            //没有护甲
            monster.setArmorState(-1);
            if (monsterCfg.getArmor() > 0) {
                monster.setArmorState(0);
                monster.setArmor(monsterCfg.getArmor());
                //设置是否掉破甲
                monster.setArmorTrue(monsterCfg.getArmor_if() == 1);
                long now = TimeUtils.Time();
                monster.setArmorbegintime(now);
                monster.setArmorzerotime(now);
            }
        }
        monster.getPlayerDieTime().clear();
        //如果没有恢复到初始化状态， 则执行此数据初始化
        if (monster.getArmorState() == 0) {
            if (monster.getArmor() != monsterCfg.getArmor()) {
                monster.setArmor(monsterCfg.getArmor());
                monster.setArmorTrue(monsterCfg.getArmor_if() == 1);
                long now = TimeUtils.Time();
                monster.setArmorbegintime(now);
                monster.setArmorzerotime(now);
            }
        }

        //解析技能
        splitSkill(monster, monsterCfg);

        BaseLongAttribute attribute = monster.getAttribute();
        attribute.clean();
        attribute.cleanMaxHP();
        for (int i = 0; i < monsterCfg.getAttributeValue().size(); ++i) {
            ReadArray<Integer> next = monsterCfg.getAttributeValue().get(i);
            //怪物移速不翻倍
            if (next.get(0) == AttributeType.ATTR_Speed) {
                attribute.addAttribute(next.get(0), next.get(1));
                continue;
            }
            attribute.addAttribute(next.get(0), (long) (next.get(1) * (attBei / 10000.)));
        }
        if (monsterCfg.getBeHurtType() == 0) {
            attribute.addMaxHP((long) (monsterCfg.getMaxHp() * (monster.getAttBei() / 10000.)));
        } else {
            attribute.addMaxHP(monsterCfg.getMaxHp());
        }
        attribute.calFinalAttackSpeed();
        attribute.calFinalMoveSpeed();
        //增加出生buff
        if (monsterCfg.getBrithBUFF() != 0) {
            Manager.buffManager.deal().onAddBuff(monster, monster, monsterCfg.getBrithBUFF());
        }
        return true;
    }

    //解析技能
    private void splitSkill(Monster monster, Cfg_Monster_Bean monsterCfg) {
        monster.getSkills().clear();
        if (monsterCfg.getUse_skills().size() < 1) {
            log.error("技能列表为空了 ：" + monster.nameIdString());
            return;
        }
        for (int i = 0; i < monsterCfg.getUse_skills().size(); i++) {
            Cfg_Skill_Bean skillCfg = CfgManager.getCfg_Skill_Container().getValueByKey(monsterCfg.getUse_skills().get(i));
            if (null == skillCfg) {
                log.error("[" + monster.getName() + " id=" + monster.getModelId() + "]怪物初始化 填写了不存在的技能 skillID" + monsterCfg.getUse_skills().get(i));
                continue;
            }
            Skill skill = new Skill();
            skill.setSkillId(monsterCfg.getUse_skills().get(i));
            skill.setLevel(1);
            skill.setNormal(true);
            monster.getSkills().put(skill.getSkillId(), skill);
        }
    }

    //获取怪物名字
    public String GetMonsterName(int modelId) {
        Cfg_Monster_Bean bean = CfgManager.getCfg_Monster_Container().getValueByKey(modelId);
        if (bean == null) {
            return "monster" + modelId;
        }
        return ServerStr.getChatTableName(bean.getName());
    }

    private boolean isShow(int modelId) {
        Cfg_Monster_Bean bean = CfgManager.getCfg_Monster_Container().getValueByKey(modelId);
        if (bean == null) {
            return true;
        }
//        return bean.getTaskShowArray().isEmpty();
//        return taskIsShow().
        return true;
    }

    // 在地图map的 pos 位置 创建一个怪物
    public Monster createMonster(MapObject map, Position pos, int modelId) {
        return createMonster(map, pos, modelId, new Position(0, 0), -3);
    }

    public Monster createMonster(MapObject map, Position pos, int modelId, int camp) {

        return createMonster(map, pos, modelId, new Position(0, 0), camp);
    }

    public Monster createMonster(MapObject map, Position pos, int modelId, int camp, int attBei) {
        return createMonster(map, pos, modelId, new Position(0, 0), camp, attBei);
    }

    public Monster createMonster(MapObject map, Position pos, int modelId, Position dir, int camp) {
        return createMonster(map, pos, modelId, new Position(0, 0), camp, 10000);
    }

    // 在地图map的 pos 位置 创建一个怪物
    public Monster createMonster(MapObject map, Position pos, int modelId, Position dir, int camp, int attBei) {
        Monster monster = new Monster(modelId);
        //添加地图相关
        monster.setServerId(GameServer.getInstance().getServerId());
        monster.changeLine(map.getLineId());
        monster.changeMapId(map.getId());
        monster.changeMapModelId(map.getMapModelId());
        monster.setModelId(modelId);
        monster.addState(EntityState.Stand);
        monster.seShow(isShow(modelId));
        //给怪物添加一个随机
        Manager.cooldownManager.addCooldown(monster, CooldownTypes.MonsterPatrol, null, RandomUtils.random(10) * 1000);
        //添加基本数据
        if (addMonsterBaseInfo(monster, true, attBei)) {
            monster.changeCurPos(pos, false);
            monster.setInitPos(pos);
            monster.setCurHp(monster.getAttribute().MaxHP());
            monster.setDir(dir);
            //如果阵营值是-3 表示使用的表里面的
            if (camp != -3) {
                monster.setCamp(camp);
            }
            Manager.mapManager.manager().onEnterMap(monster);
            return monster;
        }
        return null;
    }

    public Monster createMonster(int modelId) {

        Monster monster = new Monster(modelId);
        //添加地图相关
        monster.setServerId(GameServer.getInstance().getServerId());
        monster.setModelId(modelId);
        monster.addState(EntityState.Stand);
        monster.seShow(isShow(modelId));
        //添加基本数据
        if (addMonsterBaseInfo(monster, true, 10000)) {
            monster.setCurHp(monster.getAttribute().MaxHP());
            return monster;
        }
        return null;
    }

    /**
     * 创建怪物
     *
     * @param modelId 怪物配置表id
     * @param attBei  属性倍率，万分比
     */
    public Monster createMonster(int modelId, int attBei) {

        Monster monster = new Monster(modelId);
        //添加地图相关
        monster.setServerId(GameServer.getInstance().getServerId());
        monster.setModelId(modelId);
        monster.addState(EntityState.Stand);
        monster.seShow(isShow(modelId));
        //添加基本数据
        if (addMonsterBaseInfo(monster, true, attBei)) {
            monster.setCurHp(monster.getAttribute().MaxHP());
            return monster;
        }
        return null;
    }

    public ITaskEntityIsShow taskIsShow() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.MonsterBaseScript);
        if (is instanceof ITaskEntityIsShow) {
            return (ITaskEntityIsShow) is;
        } else {
            return null;
        }
    }

    public IMonsterAi ai() {
        IMonsterAi ai = (IMonsterAi) Manager.scriptManager.GetScriptClass(ScriptEnum.MonsterAiCommonScript);
        return ai;
    }

    public IMonsterAction manager() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.MonsterBaseScript);
        if (is instanceof IMonsterAction) {
            return (IMonsterAction) is;
        }
        throw new NullPointerException("没有实现怪物的管理函数！");
    }
}
