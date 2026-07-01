package common.copyMap.marry;

import com.data.*;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Clone_monster_Bean;
import com.data.struct.ReadArray;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.structs.Notify;
import com.game.cooldown.structs.CooldownTypes;
import com.game.count.structs.VariantType;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.marriage.script.IMarryCloneScript;
import com.game.marriage.struct.Marriage;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.robot.ai.RobotAi;
import com.game.robot.script.IRobotScript;
import com.game.robot.struct.Robot;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.game.utils.MessageUtils;
import game.core.map.Position;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.MarriageMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * @Desc TODO  情缘副本
 * @Date 2020/8/17 15:09
 * @Auth ZUncle
 */
public class MarryHoneyScript implements IMarryCloneScript {

    //TODO 情缘副本ID
    final int cloneModelId = 110001;
    //TODO 奖励选择时间
    final int selectTime = 30;
    //TODO 刷怪轮次
    final int loopKey = 1;
    //TODO 刷怪结束
    final int loopOverKey = 2;
    //TODO 副本怪物数据
    final int bossModelIdKey = 3;
    final int bossDieKey = 4;
    final int bossMaxKey = 5;
    final int monsterDieKey = 6;
    final int monsterMaxKey = 7;
    //TODO 默契大考验数据
    final int fate_dataKey = 8;
    //TODO 副本结束时间缓存
    final int endTimeKey = 10;
    //TODO 充气娃娃分身
    final int RobotKey = 11;
    //TODO 结束标志
    final int finishKey = 9;

    private static final Logger logger = LogManager.getLogger(MarryHoneyScript.class);

    /**
     * 地图创建初始化
     *
     * @param mapObject
     * @param objects
     */
    @Override
    public void onCreate(MapObject mapObject, Object... objects) {

        Player leader = (Player) objects[0];
        List<Player> team = (ArrayList<Player>) objects[1];

        // 创建充气娃娃
        if (team.size() == 1) {
            PlayerWorldInfo target = Manager.marriageManager.getMarryTarget(leader);
            IRobotScript is = (IRobotScript) Manager.scriptManager.GetScriptClass(ScriptEnum.RobotBaseScript);
            Robot robot = is.OnMake(target.getRoleid());
            if (robot != null) {
                robot.changeCurPos(mapObject.getBrithPos());
                robot.changeMapId(mapObject.getId());
                robot.changeLine(mapObject.getLineId());
                robot.changeMapModelId(mapObject.getMapModelId());

                Manager.mapManager.manager().onEnterMap(robot);

                mapObject.getParams().put(RobotKey, robot);
            }
        }

        mapObject.setAutoRemove(false);

        //region   初始化数据
        //TODO 副本怪物数据
        int modelId = mapObject.getZoneModelId();
        int boss, bossId = 0, monster = 0;
        for (int i = 1; ; i++) {
            boss = 0;
            Cfg_Clone_monster_Bean bean = CfgManager.getCfg_Clone_monster_Container().getValueByKey(modelId * 1000 + i);
            for (ReadArray<Integer> ll : bean.getMonster_information().getValuees()) {
                int monsterId = ll.get(0);
                int count = ll.get(1);
                monster += count;
                bossId = monsterId;
                boss += count;
            }
            if (bean.getIf_end() == 1) {
                break;
            }
        }
        mapObject.getParams().put(monsterMaxKey, monster);
        mapObject.getParams().put(bossMaxKey, boss);
        mapObject.getParams().put(bossModelIdKey, bossId);
        //endregion

        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(mapObject.getZoneModelId());
        mapObject.addMapOnceScriptEventTimer(getId(), "refreshMonster", bean.getEnter_time());

        long copyTime = bean.getEnter_time() + bean.getExist_time();
        mapObject.addMapOnceScriptEventTimer(getId(), "timeOut", copyTime);

        mapObject.getParams().put(endTimeKey, TimeUtils.Time() + copyTime);

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

        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(cloneModelId);
        if (bean.getManual_num() <= -1) {
            return false;
        }
        if (bean.getManual_num() == 0) {
            return true;
        }
        int buyTimes = (int) Manager.countManager.getVariant(player, VariantType.MarryCloneBuyTimes);
        int useTimes = (int) Manager.countManager.getVariant(player, VariantType.MarryCloneUseTimes);

        return buyTimes + bean.getManual_num() - useTimes > 0;
    }


    /**
     * 进入副本地图接口
     *
     * @param player
     * @param map
     * @param login
     */
    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {

        //TODO 同步一次
        sendCloneInfo(map);
        logger.info("玩家进入情缘副本 map={} player={}", map, player);
        Manager.controlManager.operate(player, FunctionVariable.Join_Marry_Copy, 1);

        if (map.getParams().containsKey(RobotKey)) {
            Robot robot = map.getParam(RobotKey);
            robot.setAi(RobotAi.Help);
            logger.warn("启动老婆分身 robot={}", robot);
        }
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
     *
     * @param map
     * @param monster
     * @param attacker
     */
    @Override
    public void onMonsterDie(MapObject map, Monster monster, Fighter attacker) {

        int bossId = (int) map.getParams().get(bossModelIdKey);

        int monsterDie = (int) map.getParams().getOrDefault(monsterDieKey, 0);
        map.getParams().put(monsterDieKey, monsterDie + 1);

        if (monster.getModelId() == bossId) {
            int bossDie = (int) map.getParams().getOrDefault(bossDieKey, 0);
            map.getParams().put(bossDieKey, bossDie + 1);
        }
    }

    /**
     * 怪物死亡后
     *
     * @param mapObject
     * @param monster
     * @param attacker
     */
    @Override
    public void onMonsterAfterDie(MapObject mapObject, Monster monster, Fighter attacker) {

        sendCloneInfo(mapObject);

        if (mapObject.getMonsters().size() <= 0) {
            //TODO 所有怪物死亡完成
            if ((boolean) mapObject.getParams().getOrDefault(loopOverKey, false)) {
                reward(mapObject);
                mapObject.getParams().put(endTimeKey, TimeUtils.Time() + selectTime * 1000);
                mapObject.addMapOnceScriptEventTimer(getId(), "selectTimeOut", selectTime * 1000);
                sendCloneInfo(mapObject);
                return;
            }

            int loop = (int) mapObject.getParams().getOrDefault(loopKey, 1);
            int modelId = mapObject.getZoneModelId();

            Cfg_Clone_monster_Bean bean = CfgManager.getCfg_Clone_monster_Container().getValueByKey(modelId * 1000 + loop);

            //TODO 刷新下一波怪物
            mapObject.getParams().put(loopKey, loop + 1);
            mapObject.addMapOnceScriptEventTimer(getId(), "refreshMonster", bean.getWaiting());
        }
    }

    @Override
    public void onLeaveBattle(MapObject map, Monster monster, Player attacker) {

    }

    /**
     * 玩家死亡接口
     *
     * @param map
     * @param attacker
     * @param player
     */
    @Override
    public void onPlayerDie(MapObject map, Fighter attacker, Player player) {

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
            case "timeOut":
                finish(map, false, false);
                break;
            case "tickOut":
                tickOut(map);
                break;
            case "refreshMonster":
                refreshMonster(map);
                break;
            case "selectTimeOut":
                finish(map, true, false);
                break;
        }
    }

    /**
     * 删除地图调用接口
     *
     * @param map
     */
    @Override
    public void removeMap(MapObject map) {

    }
    //region  ///////////////////////////副本logic///////////////////////////////////////////////


    //TODO 随机默契大考验
    void reward(MapObject map) {
        HashMap<Integer, Integer> fateMap = new HashMap<>();
        map.getParams().put(fate_dataKey, fateMap);

        List<ReadArray<Integer>> fateList = new ArrayList<>();
        for (int i = 0; i < Global.Marry_Copy_Fate_Test.size(); i++) {
            ReadArray<Integer> array = Global.Marry_Copy_Fate_Test.get(i);
            fateList.add(array);
        }
        //TODO 随机列表
        Collections.shuffle(fateList);
        ReadArray<Integer> array = fateList.get(0);
        for (int i = array.size() < 4 ? 0 : 1; i < array.size(); i++) {
            fateMap.put(array.get(i), 0);
        }
        List<Integer> imgs = new ArrayList<>(fateMap.keySet());
        //TODO 通知双方考研数据
        map.getPlayers().values().forEach(player -> {
            Collections.shuffle(imgs);
            MarriageMessage.ResMarryCloneSucInfo.Builder builder = MarriageMessage.ResMarryCloneSucInfo.newBuilder();
            builder.addAllImgList(imgs);
            MessageUtils.send_to_player(player, MarriageMessage.ResMarryCloneSucInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        });
    }


    /**
     * 同步副本即使数据
     *
     * @param map
     */
    void sendCloneInfo(MapObject map) {
        int bossId = (int) map.getParams().get(bossModelIdKey);
        int bossDie = (int) map.getParams().getOrDefault(bossDieKey, 0);
        int bossMax = (int) map.getParams().get(bossMaxKey);
        int monsterDie = (int) map.getParams().getOrDefault(monsterDieKey, 0);
        int monsterMax = (int) map.getParams().get(monsterMaxKey);
        long endTime = (long) map.getParams().get(endTimeKey);
        MarriageMessage.ResMarryCloneInfo.Builder builder = MarriageMessage.ResMarryCloneInfo.newBuilder();
        builder.setBossId(bossId);
        builder.setBossDie(bossDie);
        builder.setBossMax(bossMax);
        builder.setMonsterDie(monsterDie);
        builder.setMonsterMax(monsterMax);
        builder.setRemainTime((int) (endTime - TimeUtils.Time()));

        MessageUtils.send_to_map(map, MarriageMessage.ResMarryCloneInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 刷新怪物
     *
     * @param mapObject
     */
    void refreshMonster(MapObject mapObject) {

        int loop = (int) mapObject.getParams().getOrDefault(loopKey, 1);
        int modelId = mapObject.getZoneModelId();

        Cfg_Clone_monster_Bean bean = CfgManager.getCfg_Clone_monster_Container().getValueByKey(modelId * 1000 + loop);
        if (bean == null) {
            return;
        }
        if (bean.getIf_end() == 1) {
            mapObject.getParams().put(loopOverKey, true);
        }
        for (ReadArray<Integer> ll : bean.getMonster_information().getValuees()) {
            int x = ll.get(2);
            int y = ll.get(3);
            for (int i = 0; i < ll.get(1); i++) {
                Position pos = new Position(x, y);
                Manager.monsterManager.createMonster(mapObject, pos, ll.get(0));
            }
        }
        logger.info("情缘副本刷新怪物loop={} map={}", loop, mapObject);
    }

    /**
     * 副本结束
     *
     * @param map
     * @param success
     */
    void finish(MapObject map, boolean success, boolean isTrue) {

        logger.info("情缘副本结束 success={} isTrue={} map={}", success, isTrue, map);
        if (map.getParams().containsKey(finishKey)) {
            return;
        }
        map.getParams().put(finishKey, success);

        map.setAutoRemove(true);
        if (success) {
            Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(map.getZoneModelId());
            //TODO 发放奖励
            ReadArray<Integer> drops = isTrue ? bean.getSuccess_reward().get(0) : bean.getFail_reward().get(0);
            for (Player player : map.getPlayers().values()) {
                List<List<Integer>> reward = new ArrayList<>();
                for (int dropId : drops.getValue()) {
                    List<List<Integer>> dropExecute = Manager.dropManager.deal().getItemDrops(player, dropId);
                    reward.addAll(dropExecute);
                }
                List<Item> items = Item.createItems(reward, 1);
                if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.MarryCopyMapGet, IDConfigUtil.getLogId())) {
                    Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail
                            , MessageString.System, MessageString.BAGISSPACETOMAIL
                            , MessageString.GetAwardNotEnoughSpaceContent, items, ItemChangeReason.MarryCopyMapGet);
                }
                Manager.activityManager.cloneDropTrigger(player, bean.getId());
                Manager.countManager.addVariant(player, VariantType.MarryCloneUseTimes, 1);
                sendMarryCloneTimes(player);
                //记录BI数据
//                Manager.biManager.getScript().biActivity(player, ItemChangeReason.MarryCopyMap, BIActiityTypeEnum.LOVE_SHIP.getId(), map.getZoneModelId());
                Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.LOVE_SHIP, ItemChangeReason.MarryCopyMap, map.getZoneModelId());
            }
            map.addMapOnceScriptEventTimer(getId(), "tickOut", 3 * 1000);
        } else {
            for (Player player : map.getPlayers().values()) {
                MarriageMessage.ResMarryCloneFailInfo.Builder builder = MarriageMessage.ResMarryCloneFailInfo.newBuilder();
                MessageUtils.send_to_player(player, MarriageMessage.ResMarryCloneFailInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
            }
        }
    }

    void tickOut(MapObject map) {
        //TODO 踢出去
        for (Player player : map.getPlayers().values()) {
            Manager.copyMapManager.outZone(player);
        }
    }

    //endregion

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.MarryHoneyScript;
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
     * 请求 伴侣购买次数
     *
     * @param player
     */
    @Override
    public void reqCallMarryCloneBuy(Player player) {

        if (Manager.cooldownManager.isCooldowning(player, CooldownTypes.MARRY_BUY_CD, null)) {
            long cooldownTime = Manager.cooldownManager.getCooldownTime(player, CooldownTypes.MARRY_BUY_CD, null);
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CooldownIng, cooldownTime / 1000);
            return;
        }
        Marriage marriage = Manager.marriageManager.getMarriageList().get(player.getMarriageUid());
        if (marriage == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Marry_Box_No_Panter);
            return;
        }
        PlayerWorldInfo worldInfo = Manager.marriageManager.getMarryTarget(player);
        Player partner = Manager.playerManager.getPlayerOnline(worldInfo.getRoleid());
        if (partner == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.PlayerNotOnline, worldInfo.getRolename());
            return;
        }
        Manager.cooldownManager.addCooldown(player, CooldownTypes.MARRY_BUY_CD, null, 10 * 1000);

        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(cloneModelId);
        long variant = Manager.countManager.getVariant(partner, VariantType.MarryCloneBuy);

        if (variant >= bean.getBuy_need_gold().size()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.C_MARRY_BLESS_CANNOT_BUY_TIPS);
            return;
        }

        MessageUtils.notify_player(player, Notify.NORMAL, MessageString.C_MARRY_BUYTIPS);

        MarriageMessage.ResCallMarryCloneBuy.Builder builder = MarriageMessage.ResCallMarryCloneBuy.newBuilder();
        MessageUtils.send_to_player(partner, MarriageMessage.ResCallMarryCloneBuy.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        logger.info("请求购买 仙缘副本次数 player={}", player);
    }

    /**
     * 购买 仙缘副本次数
     *
     * @param player
     */
    @Override
    public void reqMarryCloneBuy(Player player) {

        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(cloneModelId);
        long variant = Manager.countManager.getVariant(player, VariantType.MarryCloneBuy);

        if (variant >= bean.getBuy_need_gold().size()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.NotHaveBuyCount);
            return;
        }
        int goldCount = bean.getBuy_need_gold().get(0);
        if (!Manager.currencyManager.manager().decBindGoldOrGold(player, goldCount, ItemChangeReason.MarryBoxBuyDec, IDConfigUtil.getLogId())) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Item_Not_Enough, Manager.backpackManager.manager().getName(ItemCoinType.GemCoin));
            return;
        }
        Manager.countManager.addVariant(player, VariantType.MarryCloneBuy, 1);
        Manager.countManager.addVariant(player, VariantType.MarryCloneBuyTimes, 1);
        sendMarryCloneTimes(player);
        MessageUtils.notify_player(player, Notify.NORMAL, MessageString.C_SHOP_TIPS_BUYSUXESSSSSS);

        logger.info("情缘副本次数购买 count={} player={}", Manager.countManager.getVariant(player, VariantType.MarryCloneBuy), player);

        //TODO 情缘副本购买，有伴侣的->伴侣也加次数
        Marriage marriage = Manager.marriageManager.getMarriageList().get(player.getMarriageUid());
        if (marriage == null) {
            return;
        }
        PlayerWorldInfo worldInfo = Manager.marriageManager.getMarryTarget(player);
        if (worldInfo == null) {
            return;
        }
        Player partner = Manager.playerManager.getPlayer(worldInfo.getRoleid());
        if (partner != null) {
            Manager.countManager.addVariant(partner, VariantType.MarryCloneBuyTimes, 1);
            sendMarryCloneTimes(partner);
            MessageUtils.notify_player(partner, Notify.NORMAL, MessageString.Marry_CloneBuy_Notice);
        }
    }

    /**
     * 拒绝购买
     *
     * @param player
     */
    @Override
    public void reqRefuseMarryClone(Player player) {
        Marriage marriage = Manager.marriageManager.getMarriageList().get(player.getMarriageUid());
        if (marriage == null) {
            return;
        }
        PlayerWorldInfo worldInfo = Manager.marriageManager.getMarryTarget(player);
        Player partner = Manager.playerManager.getPlayerOnline(worldInfo.getRoleid());
        if (partner == null) {
            return;
        }
        MessageUtils.notify_player(partner, Notify.ERROR, MessageString.MarryPrayFail_NotAgreeBuy);
        logger.info("拒绝购买仙缘副本 player={}", player);
    }

    /**
     * 同步情缘副本
     *
     * @param player
     */
    public void sendMarryCloneTimes(Player player) {
        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(cloneModelId);

        int buy = (int) Manager.countManager.getVariant(player, VariantType.MarryCloneBuy);
        int buyCloneTimes = (int) Manager.countManager.getVariant(player, VariantType.MarryCloneBuyTimes);
        int useTimes = (int) Manager.countManager.getVariant(player, VariantType.MarryCloneUseTimes);

        MarriageMessage.MarryClone.Builder mMarryClone = MarriageMessage.MarryClone.newBuilder();
        mMarryClone.setRemainBuy(bean.getBuy_need_gold().size() - buy);
        mMarryClone.setRemainTimes(bean.getManual_num() == 0 ? 999 : (buyCloneTimes + bean.getManual_num() - useTimes));

        MarriageMessage.ResMarryClone.Builder builder = MarriageMessage.ResMarryClone.newBuilder();
        builder.setClone(mMarryClone);
        MessageUtils.send_to_player(player, MarriageMessage.ResMarryClone.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 选择 默契大考验奖励
     *
     * @param player
     * @param img
     */
    @Override
    public void reqSelectMarryClone(Player player, Integer img) {

        MapObject curMap = Manager.mapManager.getMap(player.gainMapId());
        if (!curMap.getParams().containsKey(fate_dataKey)) {
            return;
        }
        HashMap<Integer, Integer> param = curMap.getParam(fate_dataKey);
        if (!param.containsKey(img)) {
            return;
        }
//        logger.info("默契选择 img={} player={}", img, player);
        param.put(img, param.get(img) + 1);

        if (curMap.getPlayers().size() == 1) {
            finish(curMap, true, false);
            return;
        }

        int count = 0;
        for (Map.Entry<Integer, Integer> set : param.entrySet()) {
            if (set.getValue() > 0) {
                count++;
            }
            if (set.getValue() >= curMap.getPlayers().size()) {
                finish(curMap, true, true);
                return;
            }
        }
        if (count >= curMap.getPlayers().size()) {
            finish(curMap, true, false);
        }
    }


}
