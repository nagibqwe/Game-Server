package common.monster;

import com.data.CfgManager;
import com.data.FunctionVariable;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Monster_Bean;
import com.game.attribute.BaseLongAttribute;
import com.game.behavior.manager.BehaviorManager;
import com.game.behavior.structs.BehaviorType;
import com.game.behavior.structs.type.ReviveBehavior;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.fightserver.manager.FightClientManager;
import com.game.guild.manager.GuildsManager;
import com.game.guild.structs.Guild;
import com.game.manager.Manager;
import com.game.map.structs.Area;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.monster.script.IMonsterAction;
import com.game.monster.script.ITaskEntityIsShow;
import com.game.monster.structs.Monster;
import com.game.monster.structs.MonsterDefine;
import com.game.nature.structs.HuaxinEntity;
import com.game.pet.structs.Pet;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.skill.structs.SkillMagic;
import com.game.structs.*;
import com.game.task.structs.*;
import com.game.utils.MessageUtils;
import com.game.world_help.struct.WorldHelp;
import game.core.map.IMapObject;
import game.core.map.Position;
import game.core.script.IScript;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage;
import game.message.MapMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.Map.Entry;

import static com.game.monster.structs.MonsterDefine.MonsterType_Boss;

/**
 * 怪物的逻辑脚本实现， 主要实现一个怪物会产生的通用逻辑
 *
 * @author admin
 */
public class MonsterScript implements IScript, IMonsterAction, ITaskEntityIsShow {

    private static final Logger log = LogManager.getLogger(MonsterScript.class);

    @Override
    public int getId() {
        return ScriptEnum.MonsterBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    /**
     * 怪物的死亡事件
     *
     * @param attacker 攻击对象
     * @param monster  被攻击的怪物
     */
    @Override
    public void monsterDie(Monster monster, Fighter attacker) {
        if (monster == null) {
            return;
        }
        //9.被救活了?? 这个是一个预防措施?
        if (monster.getCurHp() > 0) {
            return;
        }
        //0.记录怪物的基础信息
        int monsterModelId = monster.getModelId();
        int monsterLevel = monster.getLevel();
        Position monsterPos = monster.gainCurPos();
        Cfg_Monster_Bean bean = CfgManager.getCfg_Monster_Container().getValueByKey(monsterModelId);

        //1.获取怪物所在地图
        MapObject map = Manager.mapManager.getMap(attacker.gainMapId());

        //2.获取击杀怪物的玩家
        attacker = MapUtils.getOwnFighter(map, attacker);
        if (!(attacker instanceof Player)) {
            attacker = getFirstOne(monster);
        }

        //3 处理怪物的仇恨玩家
        List<Player> hatredPlayers = new ArrayList<>();
        List<Player> noHelpers = new ArrayList<>();
        for (Hatred ht : monster.getHatreds()) {
            if ((ht.getTarget() instanceof Player)) {
                Player player = (Player) ht.getTarget();
                //3.1记录怪物的所有仇恨玩家列表
                hatredPlayers.add(player);
                //3.2 找到怪物仇恨中所有不是参与帮助的玩家
                WorldHelp help = Manager.worldHelpManager.getJoin().get(player.getId());
                if (help == null || help.isInitiate()) {
                    noHelpers.add(player);
                }
            }
        }

        //4.世界支援的相关信息,判断最后击杀怪物的玩家是否在支援
        boolean isWorldHelp = false;

        //5.处理掉落
        if (attacker != null) {
            isWorldHelp = Manager.worldHelpManager.getScript().isHelpAndMapBoss((Player) attacker, monster.getId());
            //怪物死亡掉落
            Manager.dropManager.deal().doDeadDrop(map, monster, (Player) attacker);
        }

        //6.调用场景的脚本回调死亡前处理.
        if (map.getSetting().getIsscript() > 0) {
            try {
                Manager.mapManager.base(map.getSetting().getIsscript()).onMonsterDie(map, monster, attacker);
            } catch (Exception e) {
                log.error(e, e);
            }
        }

        //7.记录怪物死亡的BI日志
        bossDieBI(monster, map, bean);

        //8.处理世界救援的处理,此句放在onMonsterDie回调之后
        Manager.worldHelpManager.getScript().bossDie(map, monster);

        //10. 清理怪物标识,并从地图中清理掉这个怪物
        monster.setDie();
        monster.clearDropRoleIds();
        monster.getDamages().clear();
        monster.setCurSlowSkill(null);
        BehaviorManager.CancelAllBehavior(monster);
        Manager.mapManager.manager().onQuitMap(map, monster, false);

        //11.调用场景的脚本回调死亡后处理.
        if (map.getSetting().getIsscript() > 0) {
            try {
                Manager.mapManager.base(map.getSetting().getIsscript()).onMonsterAfterDie(map, monster, attacker);
            } catch (Exception e) {
                log.error(e, e);
            }
        }
        //12.怪物死亡增加墓碑
        Manager.mapManager.createTombstone(map, monsterModelId);

        //13.清理召唤技能产生的物件
        clearSkillMagic(map, monster);

        //14.处理怪物复活逻辑
        if (bean.getRevive_time() > 0) {
            ReviveBehavior rb = new ReviveBehavior(monster);
            rb.setReviveTime(TimeUtils.Time() + bean.getRevive_time());
            BehaviorManager.InsertBehavior(monster, rb);
            map.getMonsters().put(monster.getId(), monster);
        }

        //15.怪物死亡之后的各种标记的计数处理

        //15.1 记录哪些玩家能够被记录次数
        Set<Player> haveCountKillNumPlayers = new HashSet<>();

        //15.2 击杀者,如果不是救援,才会处理的一些计数标记
        if (attacker != null && !isWorldHelp) {
            Manager.taskManager.deal().action((Player) attacker, Task.ACTION_TYPE_KILL_MONSTER, monsterModelId, 1);
            haveCountKillNumPlayers.add((Player) attacker);

            //检查BOSS死亡
            if (bean.getMonster_type() == MonsterType_Boss) {
                Manager.countManager.addCount((Player) attacker, BaseCountType.Event_TIMES, FunctionVariable.KillBOSS, Count.RefreshType.CountType_Forever, 1);
                Manager.controlManager.operate((Player) attacker, FunctionVariable.KillBOSS, 1);

                Manager.countManager.addCount((Player) attacker, BaseCountType.KillBoss_Times, monsterModelId, Count.RefreshType.CountType_Forever, 1);
                Manager.controlManager.operate((Player) attacker, FunctionVariable.KillSpecialBoss, 1);

                //法宝战斗器灵击杀boss数更新
                Manager.stateStifleManager.deal().addSoulSpiritProgress((Player) attacker, 2, 1, monsterLevel);
            }
        }

        //15.3.如果当前怪物是共享怪,那么一些任务标记也可以设置了.
        if(bean.getShare() == 1) {
            //15.3.1 大怪摸一下就算完成
            boolean isTaskBigMonster = bean.getMonster_type() == MonsterDefine.MonsterType_Best || bean.getMonster_type() == MonsterType_Boss;
            if (isTaskBigMonster) {
                //找出仇恨列表中的玩家
                for (Player tempPlayer : hatredPlayers) {
                    //在已经击杀的列表中没有找到
                    if (!haveCountKillNumPlayers.contains(tempPlayer)) {
                        Manager.taskManager.deal().action(tempPlayer, Task.ACTION_TYPE_KILL_MONSTER, monsterModelId, 1, TaskConst.KILL_MONSTER_SHARE);
                        haveCountKillNumPlayers.add(tempPlayer);
                    }
                }
            }
            //15.3.2 即使没有参与工具,只要在某个区域内就共享次数
            if (bean.getMonster_type() == MonsterDefine.MonsterType_Normal) {
                Set<Player> sharelist = new HashSet<>();
                List<Area> ll = Manager.mapManager.getRounds(map, monsterPos);
                for (Area area : ll) {
                    sharelist.addAll(area.getPlayers());
                }

                for (Player player : sharelist) {
                    //在已经击杀的列表中没有找到
                    if (!haveCountKillNumPlayers.contains(player)) {
                        Manager.taskManager.deal().action(player, Task.ACTION_TYPE_KILL_MONSTER, monsterModelId, 1, TaskConst.KILL_MONSTER_SHARE);
                        haveCountKillNumPlayers.add(player);
                    }
                }
                sharelist.clear();
            }
        }
        //清空方便回收
        haveCountKillNumPlayers.clear();

        //15.4 这里的计数, 要使用排除参与世界救援的人
        List<Long> topThreeId = new ArrayList<>();
        List<String> topThreeName = new ArrayList<>();
        List<Player> topThree = new ArrayList<>();
        for (Player player : noHelpers) {

            //记录仇恨的前三名
            if (topThreeId.size() < 3) {
                topThreeId.add(player.getId());
                topThreeName.add(player.getName());
                topThree.add(player);
            }

            Manager.countManager.addCount(player, BaseCountType.KillSpecBoss_Times, bean.getBOSS_type(), Count.RefreshType.CountType_Forever, 1);
            Manager.controlManager.operate(player, FunctionVariable.TypeBoss, bean.getBOSS_type(), 1);


            Manager.countManager.addCount(player, BaseCountType.MapKillMonsterNum, map.getMapModelId(), Count.RefreshType.CountType_Forever, 1);
            Manager.controlManager.operate(player, FunctionVariable.SceneBoss, map.getMapModelId(), 1);

            if (GameServer.getInstance().IsFightServer()) {
                Manager.crossServerManager.crossFightdeal().sendF2GSynRoleFVInfo(player, FunctionVariable.CrossSceneBoss, 1, Integer.parseInt(BaseCountType.MapKillMonsterNum.getValue()), map.getMapModelId(), Count.RefreshType.CountType_Forever.getValue(), 1);
                Manager.crossServerManager.crossFightdeal().sendF2GSynRoleFVInfo(player, FunctionVariable.TypeBoss, 1, Integer.parseInt(BaseCountType.KillSpecBoss_Times.getValue()), bean.getBOSS_type(), Count.RefreshType.CountType_Forever.getValue(), 1);
            }

            if (bean.getBOSS_type() == 1) {
                Manager.controlManager.operate(player, FunctionVariable.KillWorldBossNum, 1);
                Manager.controlManager.operate(player, FunctionVariable.Kill_World_Boss_Num, 1);
            }
            if (bean.getBOSS_type() == 16) {
                Manager.controlManager.operate(player, FunctionVariable.Kill_Vip_Boss_Num, 1);
            }
            if (bean.getBOSS_type() == 3) {
                Manager.controlManager.operate(player, FunctionVariable.KillSuitBossNum, 1);
                Manager.controlManager.operate(player, FunctionVariable.Kill_Suit_Boss_Num, 1);

            }

            if (bean.getBOSS_type() == 4) {
                Manager.controlManager.operate(player, FunctionVariable.KillGemBossNum, 1);
            }

            if (bean.getBOSS_type() == 8) {
                Manager.controlManager.operate(player, FunctionVariable.KillStateBossNum, 1);
                Manager.controlManager.operate(player, FunctionVariable.Kill_Private_Boss_Num, 1);
            }
        }
        //15.5 如果top3内有数据,就发送给其他系统
        if (topThreeId.size() > 0) {
            if (GameServer.getInstance().IsFightServer()) {
                syncFirstKillInfoToGame(topThree, monsterModelId);
            } else {
                Manager.openServerAcManager.deal().onKillMonster(monsterModelId, topThreeId, topThreeName);
            }
        }

    }

    private void syncFirstKillInfoToGame(List<Player> players, int monsterModelId) {
        CrossServerMessage.F2GFirstKillInfo.Builder builder = CrossServerMessage.F2GFirstKillInfo.newBuilder();
        builder.setModelId(monsterModelId);
        for (Player player : players) {
            builder.addRoleName(player.getName());
            builder.addRoleId(player.getId());
        }
        FightClientManager.GetInstance().send_to_game(players, CrossServerMessage.F2GFirstKillInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 记录boss击杀记录
     *
     * @param player
     * @param bean
     */
    private void bossKillRecord(Player player, Cfg_Monster_Bean bean) {
//        if (bean.getMonster_type() == MonsterDefine.MonsterType_WBest || bean.getMonster_type() == MonsterDefine.MonsterType_WBoss) {
//
//            int bossId = 0;
//            int modelId = bean.getId();
//            //添加boss击杀记录(不以尾刀击杀者作为判断记录，而是以掉落归属的玩家为记录)
//            int killedTime = (int) (TimeUtils.Time() / 1000);
//            KilledRecord killRe = new KilledRecord(killedTime, player.getId());
//            if (bean.getMonster_type() == MonsterDefine.MonsterType_WBest) {
//                //世界boss
//                for (Cfg_Bossnew_worldBean cfg_bossnew_worldBean : Manager.gameDataManager.Cfg_Bossnew_worldContainer.getList()) {
//                    if (cfg_bossnew_worldBean.getID() == modelId) {
//                        bossId = cfg_bossnew_worldBean.getID();
//                    }
//                }
//            } else {
//                //boss之家boss
//                for (Cfg_BossHomeBean bossHomeBean : Manager.gameDataManager.Cfg_BossHomeContainer.getList()) {
//                    if (bossHomeBean.getMonsterid() == modelId) {
//                        bossId = bossHomeBean.getID();
//                    }
//                }
//                //永夜幻境boss
//                for (Cfg_YyHuanJingBean yyHuanJingBean : Manager.gameDataManager.Cfg_YyHuanJingContainer.getList()) {
//                    if (yyHuanJingBean.getMonsterid() == modelId) {
//                        bossId = yyHuanJingBean.getID();
//                    }
//                }
//                //个人boss
//                for (Cfg_BossPersonalBean cfg_bossPersonalBean : Manager.gameDataManager.Cfg_BossPersonalContainer.getList()) {
//                    if (cfg_bossPersonalBean.getMonsterid() == modelId) {
//                        bossId = cfg_bossPersonalBean.getID();
//                    }
//                }
//
//                for (Cfg_BossGodRuinsBean godRuinsBean : Manager.gameDataManager.Cfg_BossGodRuinsContainer.getList()) {
//                    if (godRuinsBean.getMonsterid() == modelId) {
//                        bossId = godRuinsBean.getID();
//                    }
//                }
//                //添加死亡与击杀记录
//                for (Cfg_BossSoulBeastsBean bossSoulBeastsBean : Manager.gameDataManager.Cfg_BossSoulBeastsContainer.getList()) {
//                    if (bossSoulBeastsBean.getMonsterid() == modelId) {
//                        bossId = bossSoulBeastsBean.getID();
//                    }
//                }
//                //洪荒神迹
//                for (Cfg_BossgodcloneBean bossgodcloneBean : Manager.gameDataManager.Cfg_BossgodcloneContainer.getList()) {
//                    //必须是boss
//                    if (bossgodcloneBean.getType() != EmperorShipConst.EMPEROR_SHIP_CLONE_OBJECT_TYPE_Boss){
//                        continue;
//                    }
//                    if (bossgodcloneBean.getMonsterid() == modelId){
//                        bossId = bossgodcloneBean.getID();
//                    }
//                }
//
//            }
//            if (bossId > 0) {
//                Manager.bossManager.addBossKilledRecord(bossId, killRe);
//            } else {
//                log.error(TaskHelp.getPlayerInfo(player) + "在boss死亡时，所以的boss表中均为找到：" + bean.getId());
//            }
//        }
    }

    //获取仇恨最高的玩家(伤害最高的玩家)
    private Player getFirstOne(Monster monster) {
        long maxDamage = 0L;
        Player player = null;
        for (Entry<Long, Long> en : monster.getDamages().entrySet()) {
            Player pp = Manager.playerManager.getPlayerCache(en.getKey());
            if (pp == null) {
                continue;
            }
            if (en.getValue() > maxDamage) {
                maxDamage = en.getValue();
                player = pp;
            }
        }
        return player;
    }

    Fighter findOwner(Fighter attacker, MapObject map) {
        if (attacker instanceof Player) {
            return attacker;
        }
        if (attacker instanceof Pet) {
            Pet pet = (Pet) attacker;
            attacker = map.getPlayers().get(pet.getOwnerId());
            if (attacker == null) {
                return null;
            }
            return attacker;
        } else if (attacker instanceof HuaxinEntity) {
            HuaxinEntity entity = (HuaxinEntity) attacker;
            attacker = map.getPlayers().get(entity.getOwnerId());
            if (attacker == null) {
                return null;
            }
            return attacker;
        } else if (attacker instanceof SkillMagic) {
            SkillMagic magic = (SkillMagic) attacker;
            attacker = MapUtils.getFighter(map, magic.getOwnerId());
            if (attacker == null) {
                return null;
            }
            return findOwner(attacker, map);
        }
        return null;
    }

    /**
     * 怪物攻击
     *
     * @param monster
     * @param attacker
     * @param damage
     */
    @Override
    public void monsterBeAttack(Monster monster, Fighter attacker, long damage) {
        if (attacker == null || attacker.getId() == monster.getId()) {
            return;
        }

        MapObject map = Manager.mapManager.getMap(attacker.gainMapId());
        Fighter owner = findOwner(attacker, map);
        if (owner == null) {
            return;
        }
        //如果不是TD，　才会去掉其移动行为
        if (!monster.isTdMonster()) {
            //如果不是追击则不取消移动
            if (EntityState.Move.compare(monster.getState())) {
                if (!EntityState.Run.compare(monster.getState())) {
                    BehaviorManager.CancelBehaviorByType(monster, BehaviorType.Move);
                    BehaviorManager.CancelBehaviorByType(monster, BehaviorType.Run);
                }
            } else {//如果有单独跑的行为，则取消此行为
                if (EntityState.Run.compare(monster.getState())) {
                    BehaviorManager.CancelBehaviorByType(monster, BehaviorType.Run);
                }
            }
        }
        // 怪物 仇恨和 伤害只添加玩家的
        monster.setInBattle(true);
        monster.addHatred(owner, damage);
        if (monster.getEnterFight() <= 0) {
            monster.setEnterFight(TimeUtils.Time());
        }
        monster.setEndHitTime(TimeUtils.Time());
        long historyDamage = monster.getDamages().getOrDefault(owner.getId(), 0L);
        monster.getDamages().put(owner.getId(), historyDamage + damage);

        if (map.getSetting().getIsscript() > 0) {
            Manager.mapManager.base(map.getSetting().getIsscript()).onDamage(map, monster, damage, attacker);
        }
    }

    @Override
    public boolean canSee(Player player, IMapObject entity) {
        if (entity.gainHideTaskIds() == null) {
            return true;
        } else {
            //主线任务
            if (entity.gainHideTaskIds().containsKey(Task.MAIN_TASK)) {
                if (player.getCurMainTasks().size() > 0) {
                    if (entity.gainHideTaskIds().get(Task.MAIN_TASK).contains(player.getCurMainTasks().get(0).getModelId())) {
                        if (!player.getCurMainTasks().get(0).checkFinish(false, player)) {
                            return true;
                        }
                    }
                }
            }
            //日常任务
            if (entity.gainHideTaskIds().containsKey(Task.DAILY_TASK)) {
                for (DailyTask dailyTask : player.getCurDailyTasks().values()) {
                    if (entity.gainHideTaskIds().get(Task.DAILY_TASK).contains(dailyTask.getModelId())) {
                        return true;
                    }
                }
            }
            //支线任务
            if (entity.gainHideTaskIds().containsKey(Task.BRANCH_TASK)) {
                for (BranchTask branchTask : player.getCurBranchTask()) {
                    if (entity.gainHideTaskIds().get(Task.BRANCH_TASK).contains(branchTask.getModelId())) {
                        return true;
                    }
                }
            }
            //帮会任务
            if (entity.gainHideTaskIds().containsKey(Task.GUILD_TASK)) {
                for (ConquerTask conquerTask : player.getCurConquerTasks().values()) {
                    if (conquerTask.getModelId() == 0) {
                        continue;
                    }
                    if (entity.gainHideTaskIds().get(Task.GUILD_TASK).contains(conquerTask.getModelId())) {
                        return true;
                    }
                }
            }
            //转职任务
            if (entity.gainHideTaskIds().containsKey(Task.GENDER_TASK)) {
                GenderTask genderTask = player.getCurGenderTask();
                if (entity.gainHideTaskIds().get(Task.GENDER_TASK).contains(genderTask.getModelId())) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public void changeLineOtherMonsterCurHp(Monster monster, long otherMonster, long curHp) {

        MapObject map = Manager.mapManager.getMap(monster.gainMapId());
        if (map == null) {
            return;
        }

        Monster other = map.getMonster(otherMonster);
        if (other == null) {
            return;
        }

        //扣血错误的问题，死亡循环
        other.sameMonsterSetCurHp(curHp);
    }

    @Override
    public void clearSkillMagic(MapObject map, Monster monster) {
        if (map == null) {
            return;
        }

        if (monster == null) {
            return;
        }

        if (monster.getMagics().size() < 1) {
            return;
        }

        Iterator<Long> iter = monster.getMagics().iterator();
        while (iter.hasNext()) {
            long magicId = iter.next();
            SkillMagic magic = map.getMagic(magicId);
            if (magic == null) {
                continue;
            }
            Manager.mapManager.manager().onQuitMap(map, magic, true);
        }
        monster.getMagics().clear();
    }

    @Override
    public void monsterAttributeChange(BaseLongAttribute oldAttributsVlaue, Entity monster, PlayerAttributeType type) {
        if (monster.getAttribute().getAdditionValue(AttributeType.ATTR_AtkSpeed) != oldAttributsVlaue.getAdditionValue(AttributeType.ATTR_AtkSpeed)) {
            broadCastMonsterAttackspeedChange(monster);
        }

        if (monster.getAttribute().getAdditionValue(AttributeType.ATTR_Speed) != oldAttributsVlaue.getAdditionValue(AttributeType.ATTR_Speed)) {
            broadCastMoveSpeedChange(monster);
        }
    }

    private void broadCastMoveSpeedChange(Entity entity) {
        MapMessage.ResMoveSpeedChange.Builder msg = MapMessage.ResMoveSpeedChange.newBuilder();
        msg.setObjectId(entity.getId());
        msg.setValue(entity.gainFinalMoveSpeed());
        MessageUtils.send_to_roundPlayer(entity, MapMessage.ResMoveSpeedChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private void broadCastMonsterAttackspeedChange(Entity entity) {
        MapMessage.ResAttackspeedChange.Builder msg = MapMessage.ResAttackspeedChange.newBuilder();
        msg.setObjectId(entity.getId());
        msg.setValue(entity.getAttribute().gainFinalAttackSpeed());
        MessageUtils.send_to_roundPlayer(entity, MapMessage.ResAttackspeedChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private void bossDieBI(Monster monster, MapObject map, Cfg_Monster_Bean bean) {
        if (!(bean.getMonster_type() == MonsterDefine.MonsterType_Best || bean.getMonster_type() == MonsterType_Boss)) {
            return;
        }
        Cfg_Clone_map_Bean mapBean = CfgManager.getCfg_Clone_map_Container().getValueByKey(map.getMapModelId());

        //仙盟boss单独处理
        if (map.getSetting().getIsscript() == ScriptEnum.GuildBaseMapScript) {
            return;
        }

        int rank = 0;
        long guildId = 0;
        for (Hatred ht : monster.getHatreds()) {
            rank++;
            Fighter ft = ht.getTarget();
            if (ft instanceof Player) {
                Player tempPlayer = (Player) ft;
                //BOSS击杀BI

                Guild guild = GuildsManager.getInstance().getGuildById(tempPlayer.getGuildId());
                if (guild != null) {
                    guildId = guild.getId();
                }
                Manager.biManager.getScript().biBoss(tempPlayer, map.getMapModelId(), map.getName(), map.getType(), mapBean == null ? null : mapBean.getMin_lv(), bean.getMonster_type(), monster.getModelId(), monster.getName(), monster.getLevel(), ht.getHatred(), rank, guildId, bean.getBOSS_type(), 0);
            }
        }
    }
}
