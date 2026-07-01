package common.copyMap.guild;

import com.data.CfgManager;
import com.data.FunctionVariable;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Guild_battle_final_add_Bean;
import com.data.bean.Cfg_Guild_battle_final_reward_Bean;
import com.game.backpack.structs.Item;
import com.game.bi.enums.ResourceType;
import com.game.chat.structs.Notify;
import com.game.copymap.scripts.ICopyReliveScript;
import com.game.dailyactive.manager.DailyActiveManager;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.guild.structs.GuildMember;
import com.game.guildactivity.script.IGuildLastBattle;
import com.game.guildactivity.struct.GuildFud;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.monster.structs.Monster;
import com.game.newfashion.structs.FashionData;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerDefine;
import com.game.player.structs.PlayerWorldInfo;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.game.structs.Hatred;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import game.core.map.Position;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.CommonMessage;
import game.message.GuildActivityMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc TODO
 * @Date 2021/1/20 20:12
 * @Auth ZUncle
 */

public class GuildLastBattleScript implements ICopyReliveScript, IGuildLastBattle {

    final Logger logger = LogManager.getLogger(GuildLastBattleScript.class);
    final int NotifyMailDay = 2;    //广播邮件开服天数
    final String[] door = {"DynamicBlocker", "DynamicBlocker1", "DynamicBlocker2"};   //空气墙

    final int ScoreKey = 1;          //积分数据
    final int KillKey = 2;           //击杀key
    final int FudKey = 3;            //福地Key
    final int buffKey = 7;           //buff加成
    final int StartKey = 4;          //准备倒计时
    final int NeedSynRankKey = 5;    //是否需要同步排行数据
    final int MaxKillBeanKey = 6;    //最大击杀次数特效
    final int MaxBeKillBeanKey = 8;    //最大被击杀次数特效


    /**
     * 活动开始
     *
     * @param mapObject
     */
    void startActive(MapObject mapObject) {
        for (String key : door) {
            Manager.mapManager.setBlockDoor(mapObject, key, true);
        }
    }

    /**
     * 进入副本
     *
     * @param player
     */
    @Override
    public void enterMap(Player player) {
        ConcurrentHashMap<Integer, Long> dailyMapList = DailyActiveManager.dailyMap.get(DailyActiveDefine.GUILD_LAST_BATTLE.getValue());
        if (dailyMapList == null) {
            logger.error("论剑地图未找到1111");
            return;
        }
        Long mapId = Utils.findOne(dailyMapList.values(), m -> true);
        if (mapId == null) {
            logger.error("论剑地图未找到2222");
            return;
        }
        MapObject map = Manager.mapManager.getMap(mapId);
        if (map == null) {
            logger.error("论剑地图未找到3333");
            return;
        }
        //计算出生点
        HashMap<Long, Integer> fud = map.getParam(FudKey);
        int fudId = fud.get(player.getGuildId());
        Position brith = map.getBriths().get(fudId - 1);
        Manager.mapManager.changeMap(player, mapId, brith, false);
    }

    /**
     * 结束活动
     */
    @Override
    public void endActive() {
        ConcurrentHashMap<Integer, Long> dailyMapList = DailyActiveManager.dailyMap.get(DailyActiveDefine.GUILD_LAST_BATTLE.getValue());
        if (dailyMapList == null) {
            logger.error("论剑地图未找到1111");
            return;
        }
        Long mapId = Utils.findOne(dailyMapList.values(), m -> true);
        if (mapId == null) {
            logger.error("论剑地图未找到2222");
            return;
        }
        MapObject map = Manager.mapManager.getMap(mapId);
        if (map == null) {
            logger.error("论剑地图未找到3333");
            return;
        }
        map.addMapOnceScriptEventTimer(getId(), "endActive", 0);
        map.addMapOnceScriptEventTimer(getId(), "tickOutAll", 60 * 1000);
    }

    /**
     * 获取个人连胜击杀
     *
     * @param player
     */
    @Override
    public void reqGuildLastBattleRoleKill(Player player) {
        MapObject map = Manager.mapManager.getMap(player.getCurGps().getMapId());
        if (map == null) {
            return;
        }
        if (!map.getParams().containsKey(KillKey)) {
            return;
        }
        HashMap<Long, Integer> kill = map.getParam(KillKey);
        sendSelfReport(player, kill);
    }

    void sendSelfReport(Player player, HashMap<Long, Integer> kill) {
        int count = kill.getOrDefault(player.getId(), 0);
        GuildActivityMessage.ResGuildLastBattleRoleKill.Builder message = GuildActivityMessage.ResGuildLastBattleRoleKill.newBuilder();
        message.setKill(Math.abs(count));
        message.setType(count < 0 ? 1 : 0);
        MessageUtils.send_to_player(player, GuildActivityMessage.ResGuildLastBattleRoleKill.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 地图创建初始化
     *
     * @param mapObject
     * @param objects
     */
    @Override
    public void onCreate(MapObject mapObject, Object... objects) {

        mapObject.setDelTime(0);
        mapObject.setAutoRemove(false);

        for (String key : door) {
            Manager.mapManager.setBlockDoor(mapObject, key, false);
        }

        Cfg_Guild_battle_final_add_Bean maxLimit = null;
        Cfg_Guild_battle_final_add_Bean maxBeLimit = null;
        for (Cfg_Guild_battle_final_add_Bean bean : CfgManager.getCfg_Guild_battle_final_add_Container().getValuees()) {
            if (bean.getType() == 0) {
                if (maxLimit == null) {
                    maxLimit = bean;
                    continue;
                }
                if (bean.getNum() > maxLimit.getNum()) {
                    maxLimit = bean;
                }
            } else {
                if (maxBeLimit == null) {
                    maxBeLimit = bean;
                    continue;
                }
                if (bean.getNum() > maxBeLimit.getNum()) {
                    maxBeLimit = bean;
                }
            }
        }
        mapObject.getParams().put(MaxKillBeanKey, maxLimit);
        mapObject.getParams().put(MaxBeKillBeanKey, maxBeLimit);

        HashMap<Long, Integer> fudH = new HashMap<>();
        HashMap<Long, HashMap<Long, Integer>> guild = new HashMap<>();
        for (GuildFud fud : Manager.guildActivityManager.getFud().values()) {
            if (fud.getGuild() == null) {
                continue;
            }
            guild.put(fud.getGuild().getId(), new HashMap<>());
            fudH.put(fud.getGuild().getId(), fud.getRank());
        }

        mapObject.getParams().put(NeedSynRankKey, false);
        mapObject.getParams().put(FudKey, fudH);
        mapObject.getParams().put(ScoreKey, guild);
        mapObject.getParams().put(buffKey, new HashMap<Long, Integer>());
        mapObject.getParams().put(KillKey, new HashMap<Long, Integer>());
        mapObject.getParams().put(StartKey, TimeUtils.Time() + 60 * 1000);

        mapObject.addMapOnceScriptEventTimer(getId(), "startActive", 60 * 1000);
        mapObject.addMapLoopScriptEventTimer(getId(), "tick", -1, 0, 3000);

        logger.info("福地论剑副本开启！！！");

    }

    /**
     * 活动开启广播通知
     */
    @Override
    public void notifyMail() {
        if (NotifyMailDay != TimeUtils.getOpenServerDay()){
            return;
        }

        for (GuildFud fud : Manager.guildActivityManager.getFud().values()) {
            if (fud.getGuild() == null) {
                continue;
            }
            for (GuildMember member : fud.getGuild().getMembers().values()) {
                Manager.mailManager.sendMailToPlayer(
                        member.getId(),
                        MailType.SysCommonRewardMail,
                        MessageString.System,
                        MessageString.Fud_Activity_Open_Title,
                        MessageString.Fud_Activity_Open_Content);
            }
        }
    }

    /**
     * 是否满足进入条件
     * <p>
     * 若不满足，实现脚本给出提示或错误日志
     *
     * @param player
     * @param model  副本zoneId
     * @param level
     * @return 是否满足条件
     */
    @Override
    public boolean canEnterMap(Player player, int model, int level) {

        for (GuildFud fud : Manager.guildActivityManager.getFud().values()) {
            if (fud.getGuild() == null) {
                continue;
            }
            if (fud.getGuild().getId() == player.getGuildId()) {
                return true;
            }
        }
        MessageUtils.notify_player(player, Notify.ERROR, MessageString.GuildNotTopThree);
        return false;
    }

    /**
     * 进入副本地图接口
     *  @param player
     * @param map
     * @param login
     */
    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {

        if (!map.getParams().containsKey(FudKey)) {
            return;
        }
        HashMap<Long, Integer> fud = map.getParam(FudKey);
        addScore(map, player, 0);

        Integer fudId = fud.getOrDefault(player.getGuildId(), 99);
        player.setCamp(fudId + 99, true);
        Manager.playerManager.manager().onUpdatePkState(player, PlayerDefine.PkStateGuild, true);//阵营模式

        long startTime = (long) map.getParam(StartKey) - TimeUtils.Time();
        //发送倒计时
        GuildActivityMessage.ResGuildLastBattleTimeCalc.Builder message = GuildActivityMessage.ResGuildLastBattleTimeCalc.newBuilder();
        message.setTime(startTime < 0 ? 0 : startTime);
        MessageUtils.send_to_player(player, GuildActivityMessage.ResGuildLastBattleTimeCalc.MsgID.eMsgID_VALUE, message.build().toByteArray());

        Manager.controlManager.operate(player, FunctionVariable.Guild_battle_final, 1);
    }

    /**
     * 离开副本地图接口
     *
     * @param player
     * @param map
     * @param isQuit
     */
    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {
        HashMap<Long, Integer> buffCache = map.getParam(buffKey);
        if (buffCache == null) {
            return;
        }
        int old = buffCache.getOrDefault(player.getId(), 0);
        if (old > 0) {
            Manager.buffManager.deal().onRemoveBuff(player, old);
        }
    }

    /**
     * 伤害接口
     *
     * @param mapObject
     * @param monster
     * @param damage
     * @param attacker
     */
    @Override
    public void onDamage(MapObject mapObject, Monster monster, long damage, Fighter attacker) {

    }

    /**
     * 怪物死亡接口
     * b)	击杀地图中的怪物时，击杀该怪物时最后一刀的玩家所属仙盟的所有对该怪物造成过伤害的玩家增加3分。
     *
     * @param map
     * @param monster
     * @param attacker
     */
    @Override
    public void onMonsterDie(MapObject map, Monster monster, Fighter attacker) {

        if (!map.getParams().containsKey(ScoreKey)) {
            return;
        }
        if (!(attacker instanceof Player)) {
            return;
        }
        Player player = (Player) attacker;
        addScore(map, player, 3);
        logger.info("击杀怪物+3 player={}", player);
    }

    /**
     * 怪物死亡后
     *
     * @param map
     * @param monster
     * @param attacker
     */
    @Override
    public void onMonsterAfterDie(MapObject map, Monster monster, Fighter attacker) {

    }

    /**
     * 怪物脱离战斗
     *
     * @param map
     * @param monster
     * @param attacker
     */
    @Override
    public void onLeaveBattle(MapObject map, Monster monster, Player attacker) {

    }

    /**
     * 玩家死亡接口
     * a)	击杀其他仙盟玩家增加10分，助攻击杀其他仙盟玩家增加3分。
     *
     * @param map
     * @param attacker
     * @param die
     */
    @Override
    public void onPlayerDie(MapObject map, Fighter attacker, Player die) {
        if (!map.getParams().containsKey(ScoreKey)) {
            return;
        }
        if (!map.getParams().containsKey(KillKey)) {
            return;
        }

        HashMap<Long, Integer> killInfo = map.getParam(KillKey);

        Integer beKillCount = killInfo.getOrDefault(die.getId(), 0);
        if (beKillCount < 0) {
            killInfo.put(die.getId(), beKillCount - 1);
        } else {
            killInfo.put(die.getId(), -1);
        }
        sendSelfReport(die, killInfo);

        if (attacker instanceof Player) {
            Player killer = (Player) attacker;
            addScore(map, killer, 10);
            logger.info("击杀玩家+10 player={}", killer);
            for (Hatred hatred : die.getHatreds()) {
                if (hatred.getTarget() instanceof Player) {
                    Player helper = (Player) hatred.getTarget();
                    if (!helper.equals(killer)) {
                        addScore(map, helper, 3);
                        logger.info("助攻玩家+3 player={}", helper);
                    }
                }
            }
            //计算连胜
            Integer killCount = killInfo.getOrDefault(killer.getId(), 0);
            if (killCount < 0) {
                killInfo.put(killer.getId(), 1);
                //计算连败转胜积分
                calcExtScore(map, killer, killCount);
            } else {
                killInfo.put(killer.getId(), killCount + 1);
                //计算连胜积分
                calcExtScore(map, killer, killCount + 1);
                //广播连胜
                sendWinNotice(map, killer, die, killCount + 1);
            }
            sendSelfReport(killer, killInfo);
        }
    }

    /**
     * 定时执行的函数
     *
     * @param map
     * @param method
     * @param params
     */
    @Override
    public void action(MapObject map, String method, Object[] params) {

        switch (method) {
            case "startActive":
                startActive(map);
                break;
            case "tick":
                tick(map);
                break;
            case "endActive":
                doGameOver(map);
                break;
            case "tickOutAll":
                tickOutAll(map);
                break;
            default:
        }
    }

    /**
     * 踢出所有玩家
     *
     * @param map
     */
    void tickOutAll(MapObject map) {
        for (Player player : map.getPlayers().values()) {
            Manager.copyMapManager.outZone(player);
        }
        map.setStop(true);
        logger.info("福地论剑踢出所有玩家！！");
    }

    /**
     * 活动结束
     *
     * @param map
     */
    void doGameOver(MapObject map) {

        map.setAutoRemove(false);

        HashMap<Integer, Cfg_Guild_battle_final_reward_Bean> GuildReward = new HashMap<>();
        HashMap<Integer, Cfg_Guild_battle_final_reward_Bean> RoleReward = new HashMap<>();

        for (Cfg_Guild_battle_final_reward_Bean bean : CfgManager.getCfg_Guild_battle_final_reward_Container().getValuees()) {
            for (int i = bean.getRank().get(0); i <= bean.getRank().get(1); i++) {
                if (bean.getType() == 0) {
                    GuildReward.put(i, bean);
                } else {
                    RoleReward.put(i, bean);
                }
            }
        }

        HashMap<Long, GuildActivityMessage.GuildLastBattleInfo.Builder> fudMap = new HashMap<>();
        List<GuildActivityMessage.GuildLastBattleInfo.Builder> fudList = new ArrayList<>();

        HashMap<Long, GuildActivityMessage.GuildLastBattleRoleInfo.Builder> scoreMap = new HashMap<>();
        List<GuildActivityMessage.GuildLastBattleRoleInfo.Builder> scoreList = new ArrayList<>();

        HashMap<Long, HashMap<Long, Integer>> guilds = map.getParam(ScoreKey);
        HashMap<Long, Integer> fud = map.getParam(FudKey);

        for (Map.Entry<Long, HashMap<Long, Integer>> guild : guilds.entrySet()) {
            HashMap<Long, Integer> member = guild.getValue();
            long id = guild.getKey();
            int score = sum(member.values());

            GuildActivityMessage.GuildLastBattleInfo.Builder fudMember = GuildActivityMessage.GuildLastBattleInfo.newBuilder();
            fudMember.setRank(0);
            fudMember.setId(fud.get(id));
            fudMember.setScore(score);
            fudList.add(fudMember);
            fudMap.put(id, fudMember);

            for (Map.Entry<Long, Integer> role : member.entrySet()) {
                PlayerWorldInfo player = Manager.playerManager.getPlayerWorldInfo(role.getKey());
                if (player == null) {
                    continue;
                }
                GuildActivityMessage.GuildLastBattleRoleInfo.Builder mRole = GuildActivityMessage.GuildLastBattleRoleInfo.newBuilder();
                mRole.setPlayerId(player.getRoleid());
                mRole.setName(player.getRolename());
                mRole.setCareer(player.getCareer());
                mRole.setScore(role.getValue());
                scoreList.add(mRole);
                scoreMap.put(player.getRoleid(), mRole);
            }
        }
        if (scoreList.isEmpty()) {
            return;
        }

        //福地积分排名
        fudList.sort(Comparator.comparingInt(GuildActivityMessage.GuildLastBattleInfo.Builder::getScore).reversed());
        scoreList.sort(Comparator.comparingInt(GuildActivityMessage.GuildLastBattleRoleInfo.Builder::getScore).reversed());
        int r = 1, rr = 1;
        for (GuildActivityMessage.GuildLastBattleInfo.Builder f : fudList) {
            f.setRank(r++);
        }
        for (GuildActivityMessage.GuildLastBattleRoleInfo.Builder s : scoreList) {
            s.setRank(rr++);
        }

        GuildActivityMessage.GuildLastBattleRoleInfo.Builder first = scoreList.get(0);
        PlayerWorldInfo pw = Manager.playerManager.getAllPlayerWorldInfo().get(first.getPlayerId());
        first.setFacade(MapUtils.getFacade(pw));

        long log = IDConfigUtil.getLogId();

        for (Player player : map.getPlayers().values()) {
            player.setCamp(3);


            GuildActivityMessage.GuildLastBattleRoleInfo.Builder mRole = scoreMap.get(player.getId());
            Cfg_Guild_battle_final_reward_Bean role_reward_bean = RoleReward.get(mRole.getRank());
            List<Item> items2 = Item.createItemsByCareer(player.getCareer(), role_reward_bean.getReward(), 1);
            //个人奖励版面
            GuildActivityMessage.ResGuildLastBattleGameOver.Builder message = GuildActivityMessage.ResGuildLastBattleGameOver.newBuilder();
            for (Item reward : items2) {
                CommonMessage.ShowItemInfo.Builder mItem = CommonMessage.ShowItemInfo.newBuilder();
                mItem.setModelId(reward.getItemModelId());
                mItem.setCount(reward.getNum());
                message.addMyReward(mItem);
            }

            GuildActivityMessage.GuildLastBattleInfo.Builder guild = fudMap.get(player.getGuildId());
            Cfg_Guild_battle_final_reward_Bean guild_reward_bean = GuildReward.get(guild.getRank());
            List<Item> items1 = Item.createItemsByCareer(player.getCareer(), guild_reward_bean.getReward(), 1);
            //福地结算版面
            for (Item reward : items1) {
                CommonMessage.ShowItemInfo.Builder mItem = CommonMessage.ShowItemInfo.newBuilder();
                mItem.setModelId(reward.getItemModelId());
                mItem.setCount(reward.getNum());
                message.addFudReward(mItem);
            }
            items1.addAll(items2);
            if (!Manager.backpackManager.manager().addItems(player, items1, ItemChangeReason.GuildFudGain, log)) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                        MessageString.System, MessageString.NoBagCell, items1, ItemChangeReason.GuildFudGain, log);
            }

            message.setFirst(first);
            message.setFud(guild);
            message.setMy(mRole);
            MessageUtils.send_to_player(player, GuildActivityMessage.ResGuildLastBattleGameOver.MsgID.eMsgID_VALUE, message.build().toByteArray());
        }
    }

    Cfg_Guild_battle_final_add_Bean gainBean(MapObject map, int count) {
        Cfg_Guild_battle_final_add_Bean maxBeLimit = map.getParam(MaxBeKillBeanKey);
        Cfg_Guild_battle_final_add_Bean maxLimit = map.getParam(MaxKillBeanKey);
        if (count < -maxBeLimit.getNum()) {
            return maxBeLimit;
        }
        if (count > maxLimit.getNum()) {
            return maxLimit;
        }
        int key = (count > 0 ? 0 : 1) * 1000 + Math.abs(count);
        return CfgManager.getCfg_Guild_battle_final_add_Container().getValueByKey(key);
    }

    /**
     * 计算连胜连败积分
     *
     * @param player
     * @param count
     */
    void calcExtScore(MapObject map, Player player, int count) {

        HashMap<Long, Integer> buffCache = map.getParam(buffKey);
        int old = buffCache.getOrDefault(player.getId(), 0);
        if (old > 0) {
            Manager.buffManager.deal().onRemoveBuff(player, old);
        }
        Cfg_Guild_battle_final_add_Bean bean = gainBean(map, count);
        if (bean == null) {
            return;
        }
        if (bean.getBuff() > 0) {
            Manager.buffManager.deal().onAddBuff(player, player, bean.getBuff());
            buffCache.put(player.getId(), bean.getBuff());
        }
        addScore(map, player, bean.getScore());
        logger.info("连胜|连败玩家+{} player={}", bean.getScore(), player);

    }

    /**
     * 添加积分
     *
     * @param map
     * @param player
     * @param score
     */
    void addScore(MapObject map, Player player, int score) {
        HashMap<Long, HashMap<Long, Integer>> guildList = map.getParam(ScoreKey);
        HashMap<Long, Integer> guild = guildList.getOrDefault(player.getGuildId(), new HashMap<>());
        Integer old = guild.getOrDefault(player.getId(), 0);
        int newScore = old + score;
        guild.put(player.getId(), newScore);
        map.getParams().put(NeedSynRankKey, true);

        BigInteger bigChange = BigInteger.valueOf(score);
        BigInteger beforeNum = BigInteger.valueOf(old);
        BigInteger afterNum = BigInteger.valueOf(newScore);
        Manager.biManager.getScript().biResource(player, 1, ResourceType.GuildLastBattleScore.getId(),bigChange,beforeNum,afterNum,0,0, ItemChangeReason.GuildFudGain,IDConfigUtil.getLogId());
    }

    /**
     * 心跳执行
     *
     * @param map
     */
    void tick(MapObject map) {

        if (map.getParam(NeedSynRankKey)) {
            sendAllReport(map);
        }
        map.getParams().put(NeedSynRankKey, false);
    }

    /**
     * 击杀广播
     */
    void sendWinNotice(MapObject map, Player killer, Player beKill, int count) {

        Cfg_Guild_battle_final_add_Bean noticeBean = gainBean(map, count);

        if (noticeBean == null || noticeBean.getSpecial_tex() <= 0) {
            return;
        }
        if (count > noticeBean.getNum() && noticeBean.getSpecial_add_num() > 0) {
            if ((count - noticeBean.getNum()) % noticeBean.getSpecial_add_num() != 0) {
                return;
            }
        }

        GuildActivityMessage.GuildLastBattleRoleInfo.Builder mKiller = GuildActivityMessage.GuildLastBattleRoleInfo.newBuilder();
        mKiller.setPlayerId(killer.getId());
        mKiller.setName(killer.getName());
        mKiller.setCareer(killer.getCareer());
        for (FashionData fashionData : killer.getNewFashionData().getWearDatas().values()) {
            mKiller.addFashion(Manager.newFashionManager.deal().buildFashionData(fashionData));
        }

        GuildActivityMessage.GuildLastBattleRoleInfo.Builder mb = GuildActivityMessage.GuildLastBattleRoleInfo.newBuilder();
        mb.setPlayerId(beKill.getId());
        mb.setName(beKill.getName());
        mb.setCareer(beKill.getCareer());
        for (FashionData fashionData : beKill.getNewFashionData().getWearDatas().values()) {
            mb.addFashion(Manager.newFashionManager.deal().buildFashionData(fashionData));
        }

        GuildActivityMessage.ResGuildLastBattleKill.Builder message = GuildActivityMessage.ResGuildLastBattleKill.newBuilder();
        message.setKill(count);
        message.setKiller(mKiller);
        message.setBeKill(mb);
        MessageUtils.send_to_map(map, GuildActivityMessage.ResGuildLastBattleKill.MsgID.eMsgID_VALUE, message.build().toByteArray());
        logger.info("广播连胜 count={} player={}", count, killer);
    }

    /**
     * 发送战报消息
     *
     * @param map
     */
    void sendAllReport(MapObject map) {

        List<GuildActivityMessage.GuildLastBattleInfo.Builder> fudList = new ArrayList<>();
        List<GuildActivityMessage.GuildLastBattleRoleInfo.Builder> scoreList = new ArrayList<>();

        HashMap<Long, HashMap<Long, Integer>> guilds = map.getParam(ScoreKey);
        HashMap<Long, Integer> fud = map.getParam(FudKey);

        for (Map.Entry<Long, HashMap<Long, Integer>> guild : guilds.entrySet()) {
            HashMap<Long, Integer> member = guild.getValue();
            long id = guild.getKey();
            int score = sum(member.values());

            GuildActivityMessage.GuildLastBattleInfo.Builder fudMember = GuildActivityMessage.GuildLastBattleInfo.newBuilder();
            fudMember.setRank(0);
            fudMember.setId(fud.get(id));
            fudMember.setScore(score);
            fudList.add(fudMember);

            for (Map.Entry<Long, Integer> role : member.entrySet()) {
                PlayerWorldInfo player = Manager.playerManager.getPlayerWorldInfo(role.getKey());
                if (player == null) {
                    continue;
                }
                GuildActivityMessage.GuildLastBattleRoleInfo.Builder mRole = GuildActivityMessage.GuildLastBattleRoleInfo.newBuilder();
                mRole.setPlayerId(player.getRoleid());
                mRole.setName(player.getRolename());
                mRole.setScore(role.getValue());
                scoreList.add(mRole);
            }
        }
        //福地积分排名
        fudList.sort(Comparator.comparingInt(GuildActivityMessage.GuildLastBattleInfo.Builder::getScore).reversed());
        scoreList.sort(Comparator.comparingInt(GuildActivityMessage.GuildLastBattleRoleInfo.Builder::getScore).reversed());
        int r = 1, rr = 1;
        GuildActivityMessage.ResGuildLastBattleReport.Builder message = GuildActivityMessage.ResGuildLastBattleReport.newBuilder();
        for (GuildActivityMessage.GuildLastBattleInfo.Builder f : fudList) {
            f.setRank(r++);
            message.addFud(f);
        }
        for (GuildActivityMessage.GuildLastBattleRoleInfo.Builder s : scoreList) {
            s.setRank(rr++);
            message.addRoles(s);
        }
        for (Map.Entry<Long, HashMap<Long, Integer>> guild : guilds.entrySet()) {
            HashMap<Long, Integer> member = guild.getValue();
            for (Long roleId : member.keySet()) {
                Player player = map.getPlayer(roleId);
                if (player == null) {
                    continue;
                }
                MessageUtils.send_to_player(player, GuildActivityMessage.ResGuildLastBattleReport.MsgID.eMsgID_VALUE, message.build().toByteArray());
            }
        }
    }

    int sum(Collection<Integer> c) {
        int sum = 0;
        for (int m : c) {
            sum += m;
        }
        return sum;
    }

    /**
     * 删除地图调用接口
     *
     * @param map
     */
    @Override
    public void removeMap(MapObject map) {

    }

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.GuildLastBattleScript;
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

    /**
     * 计算副本复活点
     *
     * @param map
     * @param player
     * @return
     */
    @Override
    public Position doCreateRelivePosition(MapObject map, Player player) {
        //计算复活点
        HashMap<Long, Integer> fud = map.getParam(FudKey);
        int fudId = fud.get(player.getGuildId());
        return map.getRelives().get(fudId - 1);
    }
}
