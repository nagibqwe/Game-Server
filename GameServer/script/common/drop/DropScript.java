package common.drop;

import com.data.*;
import com.data.bean.*;
import com.data.container.Cfg_Bossnew_drop_Container;
import com.data.container.Cfg_Drop_item_Container;
import com.data.container.Cfg_Drop_package_Container;
import com.data.container.Cfg_Monster_Container;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.backpack.structs.*;
import com.game.chat.structs.Notify;
import com.game.copymap.structs.StateBossCopyData;
import com.game.copymap.structs.ZoneCache;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.count.structs.VariantType;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.drop.log.SpecialDropLog;
import com.game.drop.script.IDropScript;
import com.game.drop.structs.BossRelationDropLimit;
import com.game.drop.structs.SpecialDropDefine;
import com.game.fightserver.manager.FightClientManager;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapParam;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.structs.ServerStr;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.utils.Utils;
import com.game.welfare.struct.RetrieveType;
import com.game.world_help.struct.SpecialHatred;
import game.core.dblog.LogService;
import game.core.util.IDConfigUtil;
import game.message.BossMessage;
import game.message.CopyMapMessage;
import game.message.CrossServerMessage.F2GResCrossDropCoin;
import game.message.MapMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * @author lw
 */
public class DropScript implements IDropScript {
    private static final Logger log = LogManager.getLogger("DropScript");
    private final int MAX_DROP_TIMES = 20;

    HashMap<Integer, ReadIntegerArrayEs> drops = new HashMap<>();

    public DropScript() {
        drops.put(1541, Global.Freeboss_drop_3);
        drops.put(1542, Global.Freeboss_drop_4);
    }

    @Override
    public int getId() {
        return ScriptEnum.DropBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }


    @Override
    public void doDeadDrop(MapObject map, Monster monster, Player player) {
        Cfg_Monster_Bean bean = CfgManager.getCfg_Monster_Container().getValueByKey(monster.getModelId());
        if (bean == null) {
            return;
        }

        List<Player> plist = getOwner(player, monster, monster.gainMapId(), bean.getDrop_belong());
        for (Player p : plist) {
            //活动掉落
            Manager.activityManager.deal().bossDrop(p, monster.getModelId());
            //普通掉落
            for (Integer dropId : bean.getNormaldrop().getValue()) {
                if (dropId <= 0) {
                    continue;
                }
                dropExecute(map, p, dropId, monster, ItemChangeReason.DropGet);
            }

            //职业掉落
            for (ReadArray<Integer> proDrop : bean.getProfessional_drop().getValuees()) {
                if (proDrop.size() < 2) {
                    log.error("职业掉落数据有误，Cfg_Monster_Bean无法找到数据，id = " + monster.getModelId());
                    continue;
                }
                if (proDrop.get(0) != p.getCareer()) {
                    continue;
                }
                dropExecute(map, p, proDrop.get(1), monster, ItemChangeReason.ProDropGet);
            }

            //经验掉落
            dropExp(p, monster, bean.getExp());
        }

        //共享掉落，掉一份，所有人平分
        List<Player> shareList = bean.getDrop_belong() == 0 ? plist : getOwner(player, monster, monster.gainMapId(), 0);
        HashMap<Integer, List<Item>> dropList = new HashMap<>();
        for (int i = 0; i < bean.getShareDrop().size(); i++) {
            List<List<Integer>> lists = getItemDrops(player, bean.getShareDrop().get(i));
            List<List<Integer>> items1 = Manager.backpackManager.manager().getAfterMergeItemList(lists);
            List<Item> itemList = Item.createItems(items1, 1);
            for (int j = 0; j < itemList.size(); j++) {
                int index = j % shareList.size();
                if (!dropList.containsKey(index)) {
                    dropList.put(index, new ArrayList<>());
                }
                dropList.get(index).add(itemList.get(j));
            }
        }
        for (Map.Entry<Integer, List<Item>> dropInfo : dropList.entrySet()) {
            int index = dropInfo.getKey();
            List<Item> itemList = dropInfo.getValue();
            Player tempPlayer = shareList.get(index);
            syncDropItemToRoundPlayer(tempPlayer, monster, itemList, false);

            if (!Manager.backpackManager.manager().addItems(shareList.get(index), itemList, ItemChangeReason.ShareDrop, monster.getModelId())) {
                Manager.mailManager.sendMailToPlayer(shareList.get(index).getId(), 1, MessageString.System,
                        MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, itemList, ItemChangeReason.ShareDrop, monster.getModelId());
            }
        }
    }

    @Override
    public List<List<Integer>> getItemDrops(Player player, int itemDropId) {
        List<List<Integer>> list = new ArrayList<>();
        dropIDDrops(list, player, itemDropId, MAX_DROP_TIMES,0);
        return list;
    }


    public  List<List<Integer>> getItemDrops(Player player, int itemDropId,int isBind){
        List<List<Integer>> list = new ArrayList<>();
        dropIDDrops(list, player, itemDropId, MAX_DROP_TIMES,isBind);
        return list;
    }

    /**
     * 掉落物品
     *
     * @param itemDropId
     */
    @Override
    public List<List<Integer>> dropExecute(int itemDropId) {
        return dropExecute(new ArrayList<>(), itemDropId);
    }

    /**
     * 掉落物品
     *
     * @param itemDropId
     */
    public List<List<Integer>> dropExecute(List<List<Integer>> drops, int itemDropId) {

        Cfg_Drop_item_Bean bean = Cfg_Drop_item_Container.GetInstance().getValueByKey(itemDropId);
        if (bean == null) {
            return drops;
        }
        //判断全服掉落次数已达上限
        int serverdaily_control = bean.getServerdaily_control();
        if (serverdaily_control > 0) {
            int haveDrop = Manager.countManager.getServerCount(BaseCountType.DROP_ITEM_LIMIT, itemDropId);
            if (haveDrop >= serverdaily_control) {
                //今日全服掉落次数已满
                return drops;
            }
        }
        boolean dropSuccess = false;
        for (ReadArray<Integer> drop : bean.getDropItems().getValuees()) {
            //计算概率
            if (!RandomUtils.defaultIsGenerate(drop.get(2))) {
                continue;
            }
            //看是物品掉落还是包掉落
            int itemID = drop.get(1);
            int maxNum = RandomUtils.random(drop.get(3), drop.get(4));
            int bind = drop.get(5);
            if (drop.get(0) == 0) {
                Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(itemID);
                if (itemBean == null) {
                    continue;
                }
                List<Integer> dropitem = new ArrayList<>();
                dropitem.add(itemID);
                dropitem.add(maxNum);
                dropitem.add(bind);
                drops.add(dropitem);
                dropSuccess = true;
            } else {
                //包掉落
                //掉落包最多20次，防止策划配置错误，引起性能问题
                if (maxNum > MAX_DROP_TIMES) {
                    maxNum = MAX_DROP_TIMES;
                }
                for (int i = 1; i <= maxNum; ++i) {
                    if (dropPackageItem(drops, itemID, drop.get(5))) {
                        dropSuccess = true;
                    }
                }
            }
        }
        if (dropSuccess) {
            if (bean.getServerdaily_control() > 0) {
                Manager.countManager.addServerCount(BaseCountType.DROP_ITEM_LIMIT, Count.RefreshType.CountType_Day, bean.getDropId(), 1);
            }
        }
        return drops;
    }

    private void dropIDDrops(List<List<Integer>> dropReturns, Player player, int itemDropId, int times,int isBind) {
        if (times <= 0) {
            printDropError(dropReturns, player, "掉落ID掉落：" + itemDropId);
            return;
        }

        if (player == null || itemDropId <= 0) {
            return;
        }
        Cfg_Drop_item_Bean bean = Cfg_Drop_item_Container.GetInstance().getValueByKey(itemDropId);
        if (bean == null) {
            return;
        }
        //判断自己掉落次数是否已达上限
        int localplayer_control = bean.getLocalplayer_control();
        if (localplayer_control > 0) {
            int haveDrop = (int) Manager.countManager.getCount(player, BaseCountType.DROP_ITEM_LIMIT, itemDropId);
            if (haveDrop >= localplayer_control) {
                return;
            }
        }

        //判断全服掉落次数已达上限
        int serverdaily_control = bean.getServerdaily_control();
        if (serverdaily_control > 0) {
            int haveDrop = Manager.countManager.getServerCount(BaseCountType.DROP_ITEM_LIMIT, itemDropId);
            if (haveDrop >= serverdaily_control) {
                //今日全服掉落次数已满
                return;
            }
        }

        boolean dropSuccess = false;
        for (ReadArray<Integer> drop : bean.getDropItems().getValuees()) {
            //计算概率
            if (!RandomUtils.defaultIsGenerate(drop.get(2))) {
                continue;
            }
            if (drop.get(6) > player.getLevel()) {
                continue;
            }
            if (drop.get(7) < player.getLevel()) {
                continue;
            }

            //看是物品掉落还是包掉落
            int itemID = drop.get(1);
            int maxNum = RandomUtils.random(drop.get(3), drop.get(4));
            if (drop.get(0) == 0) {
                if (!itemDrop(dropReturns, player, times, itemID, maxNum, isBind>0?1:drop.get(5)))
                    return;
                dropSuccess = true;
            } else {
                //包掉落
                //掉落包最多20次，防止策划配置错误，引起性能问题
                if (maxNum > MAX_DROP_TIMES) maxNum = MAX_DROP_TIMES;
                for (int i = 1; i <= maxNum; ++i) {
                    if (getDropPackageItem(dropReturns, player, itemID,  isBind>0?1:drop.get(5)))
                        dropSuccess = true;
                }
            }
        }

        if (dropSuccess) {
            if (bean.getLocalplayer_control() > 0) {
                Manager.countManager.addCount(player, BaseCountType.DROP_ITEM_LIMIT, bean.getDropId(), Count.RefreshType.CountType_Day, 1);
            }
            if (bean.getServerdaily_control() > 0) {
                Manager.countManager.addServerCount(BaseCountType.DROP_ITEM_LIMIT, Count.RefreshType.CountType_Day, bean.getDropId(), 1);
            }
        }
    }

    private boolean itemDrop(List<List<Integer>> dropReturns, Player player, int times, int itemID, int maxNum, int bind) {
        if (times <= 0) {
            printDropError(dropReturns, player, "道具ID掉落：" + itemID);
            return false;
        }

        if (player == null || itemID <= 0) {
            return false;
        }

        Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(itemID);
        if (itemBean == null)
            return false;

        if (itemBean.getType() == ItemTypeConst.XiSuiNormal && !Manager.playerManager.xiSuiScript().isNeed(player, itemID))
            return false;
        else if (itemBean.getType() == ItemTypeConst.XiSui) {
            // 如果是洗髓掉落包，直接给他掉出来
            for (int i = 0; i < maxNum; i++) {
                dropIDDrops(dropReturns, player, itemBean.getUes_gift(), times - 1,bind);
            }
            return false;
        }
        if (maxNum > 0) {
            List<Integer> dropitem = new ArrayList<>();
            dropitem.add(itemID);
            dropitem.add(maxNum);
            dropitem.add(bind);
            dropReturns.add(dropitem);
        }
        return true;
    }

    /**
     * 掉落包id获取掉落物品
     */
    boolean dropPackageItem(List<List<Integer>> drops, int packageId, int bind) {
        Cfg_Drop_package_Bean bean = Cfg_Drop_package_Container.GetInstance().getValueByKey(packageId);
        if (bean == null) {
            return false;
        }
        int randValue = RandomUtils.random(bean.getSumWeight());
        int curRate = 0;
        for (ReadArray<Integer> drop : bean.getDropItems().getValuees()) {
            if (randValue > curRate && randValue <= curRate + drop.get(3)) {
                int itemID = drop.get(0);
                int maxNum = RandomUtils.random(drop.get(1), drop.get(2));
                Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(itemID);
                if (itemBean == null) {
                    continue;
                }
                List<Integer> dropitem = new ArrayList<>();
                dropitem.add(itemID);
                dropitem.add(maxNum);
                dropitem.add(bind);
                drops.add(dropitem);
                return true;
            }
            curRate += drop.get(3);
        }
        return false;
    }

    /**
     * 掉落包id获取掉落物品
     */
    private boolean getDropPackageItem(List<List<Integer>> dropReturns, Player player, int packageId, int bind) {
        Cfg_Drop_package_Bean bean = Cfg_Drop_package_Container.GetInstance().getValueByKey(packageId);
        if (bean == null) {
            return false;
        }
        int randValue = RandomUtils.random(bean.getSumWeight());
        int curRate = 0;
        for (ReadArray<Integer> drop : bean.getDropItems().getValuees()) {
            if (randValue > curRate && randValue <= curRate + drop.get(3)) {
                int itemID = drop.get(0);
                Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(itemID);
                if (itemBean == null)
                    continue;

                if (itemDrop(dropReturns, player, MAX_DROP_TIMES, itemID, RandomUtils.random(drop.get(1), drop.get(2)), bind))
                    return true;
            }
            curRate += drop.get(3);
        }
        return false;
    }

    /**
     * 打印超限掉落日志
     *
     * @param dropReturns
     * @param player
     * @param title
     */
    private void printDropError(List<List<Integer>> dropReturns, Player player, String title) {
        long id = IDConfigUtil.getLogId();
        log.error(player.getInfo() + " 掉落循环次数超过上限: " + id + " -> " + title);
        if (dropReturns != null)
            for (List<Integer> integerList : dropReturns)
                for (int p : integerList)
                    log.error(player.getInfo() + " 掉落循环次数超过上限: " + id + " => " + p);
    }

    @Override
    public void dropExecute(MapObject map, Player player, int dropID, Monster monster, int reason) {

        if (Manager.worldHelpManager.getScript().isHelpBoss(player.getId(), monster.getId())) {
            return;
        }

        List<List<Integer>> lists = getItemDrops(player, dropID);
        List<Item> dropItems = Item.createItems(lists, 1);
        if (dropItems.isEmpty()) {
            return;
        }

        //需要广播筛选
        needDropNotice(player,dropItems,map,monster);

        writeDropHistory(map, player.getId(), dropItems);
        syncDropItemToRoundPlayer(player, monster, dropItems, true);

        if (GameServer.getInstance().IsFightServer()) {
            Manager.crossServerManager.getCrossServer().sendReward(player, dropItems, reason);
        } else {
            if (!Manager.backpackManager.manager().addItems(player, dropItems, reason, dropID)) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail,
                        MessageString.System, MessageString.System, MessageString.GetAwardNotEnoughSpaceContent, dropItems, reason, dropID);
            }
        }
    }

    @Override
    public void dropExp(Player player, Monster monster, long exp) {
        if (exp <= 0) {
            return;
        }

        MapObject map = Manager.mapManager.getMap(monster.gainMapId());
        if (map == null) {
            return;
        }

        int monsterModelId = monster.getModelId();
        //跨服掉落
        if (GameServer.getInstance().IsFightServer()) {
            F2GResCrossDropCoin.Builder msg = F2GResCrossDropCoin.newBuilder();
            msg.setCoinType(ItemCoinType.EXP);
            long num = (int) (exp * (player.gainExpRate() + 1));
            msg.setCoinNum(num);
            msg.setActionId(monsterModelId);
            msg.setRoleId(player.getId());
            msg.setCloneModelId(map.getZoneModelId() != 0 ? map.getZoneModelId() : map.getMapModelId());
            msg.setReason(ItemChangeReason.DropByFightServerGet);
            FightClientManager.GetInstance().send_to_game(player.getIosession(), F2GResCrossDropCoin.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            return;
        }

        if (Manager.worldHelpManager.getScript().isHelpBoss(player.getId(), monster.getId())) {
            return;
        }

        int expMultiple = 1;
        exp = (long) (exp * player.gainExpRate() * expMultiple);
        Manager.currencyManager.manager().onChangeFinalExp(player, exp, ItemChangeReason.DropGet, monsterModelId);
    }

    //计算玩家列表
    private List<Player> getOwner(Player killer, Monster monster, long mapId, int belongType) {
        List<Player> players = new ArrayList<>();
        List<SpecialHatred> playerHatredList = Manager.worldHelpManager.getScript().getSpecialHatredRes(monster);
        switch (belongType) {
            case 0: //所有人
                players.add(killer);
                for (SpecialHatred specialHatred : playerHatredList) {
                    Player pl = specialHatred.getPlayer();
                    if (pl.gainMapId() != mapId) {
                        continue;
                    }
                    if (specialHatred.getDamage() <= 0) {
                        continue;
                    }
                    if (!players.contains(pl)) {
                        players.add(pl);
                    }
                }
                break;
            case 1: //伤害最高的玩家
                if (playerHatredList.size() > 0) {
                    players.add(playerHatredList.get(0).getPlayer());
                } else {
                    players.add(killer);
                }
                break;
            case 2: //伤害最高的队伍
                long maxDamage = 0L, maxTeamId = 0L;
                HashMap<Long, Long> teamDamage = new HashMap<>();
                for (SpecialHatred specialHatred : playerHatredList) {
                    Player pl = specialHatred.getPlayer();
                    if (pl.gainMapId() != mapId) {
                        continue;
                    }
                    if (specialHatred.getDamage() <= 0) {
                        continue;
                    }
                    if (pl.getTeamId() <= 0) {
                        continue;
                    }
                    long damage = teamDamage.getOrDefault(pl.getTeamId(), 0L) + specialHatred.getDamage();
                    if (damage > maxDamage) {
                        maxDamage = damage;
                        maxTeamId = pl.getTeamId();
                    }
                    teamDamage.put(pl.getTeamId(), damage);
                }
                for (SpecialHatred specialHatred : playerHatredList) {
                    Player pl = specialHatred.getPlayer();
                    if (pl.gainMapId() != mapId) {
                        continue;
                    }
                    if (specialHatred.getDamage() <= 0) {
                        continue;
                    }
                    if (pl.getTeamId() <= 0) {
                        continue;
                    }
                    if (pl.getTeamId() == maxTeamId) {
                        players.add(pl);
                    }
                }
                break;
        }
        if (players.isEmpty()) {
            players.add(killer);
        }
        return players;
    }

    @Override
    public HashMap<Long, Player> specialDropReward(Monster monster, Player player, int type, boolean record, int replaceDropId) {

        HashMap<Long, Player> dropRoles = new HashMap<>();
        Cfg_Monster_Bean monsterBean = Cfg_Monster_Container.GetInstance().getValueByKey(monster.getModelId());
        if (monsterBean == null) {
            log.error("击杀boss获得掉落物品失败，没有在Cfg_MonsterBean中找到相应数据，bossID = " + monster.getModelId());
            return dropRoles;
        }

        int dropId = replaceDropId > 0 ? replaceDropId : monsterBean.getSpecialdrop();

        MapObject map = Manager.mapManager.getMap(monster.gainMapId());
        if (map == null) {
            return dropRoles;
        }

        int top = 0;
        int dailyId = getSpecialBossDailyId(type);
        List<SpecialHatred> playerHatredList = Manager.worldHelpManager.getScript().getSpecialHatredRes(monster);
        for (SpecialHatred hatred : playerHatredList) {
            Player p = hatred.getPlayer();
            if (hatred.getDamage() <= 0) {
                continue;
            }

            //改为全部受排名奖励次数限制
            int remainCount;
            if (dailyId != 0 && record) {
                remainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(p, dailyId);
                if (remainCount == 0) {
                    continue;
                }
            }

            //新手层特殊掉落
            if (type == SpecialDropDefine.NoobBoss) {
                int noob = calcDropId(map, p);
                if (noob > 0) {
                    dropId = noob;
                }
            }

            Cfg_Bossnew_drop_Bean bean = Cfg_Bossnew_drop_Container.GetInstance().getValueByKey(dropId);
            if (bean == null) {
                log.error("击杀boss获得掉落物品失败，没有在Cfg_Bossnew_dropBean中找到相应数据，dropId = " + dropId);
                continue;
            }

            boolean hasReward = false;
            if (p.getId() == player.getId()) {
                //击杀归属掉落
                if (bean.getDrop_Ordinary() > 0) {
                    hasReward = true;
                    dropRoles.put(p.getId(), p);
                    getReward(player, type, bean.getDrop_Ordinary(), ItemChangeReason.BossOrdinaryGet, monster, false);
                }
            }
            //参与奖励，不计数以及计数但没超过上限的情况才发奖励
            int dropItemId = bean.getPer_Capita_Dorp();
            if (bean.getState() > 0 && p.getStateVip().getLv() >= bean.getState()) {
                dropItemId = bean.getPer_Capita_Dorp_2();
            }
            if (dropItemId > 0) {
                hasReward = true;
                dropRoles.put(p.getId(), p);
                getReward(p, type, dropItemId, ItemChangeReason.BossCapitaGet, monster, false);
            }

            //次数特殊掉落
            BossRelationDropLimit bossRelationDropLimit = p.getRelationDropLimits().get(monster.getModelId());
            if (bossRelationDropLimit == null) {
                bossRelationDropLimit = new BossRelationDropLimit();
                p.getRelationDropLimits().put(monster.getModelId(), bossRelationDropLimit);
            }
            if (bean.getRelation_Dorp() > 0) {
                int randomCount = bossRelationDropLimit.getRandomKillCount();
                if (randomCount == 0) {
                    randomCount = RandomUtils.random(bean.getRelation_Num_min(), bean.getRelation_Num_max());
                    bossRelationDropLimit.setRandomKillCount(randomCount);
                }
                int rankCount = bossRelationDropLimit.getRankDropCount();
                bossRelationDropLimit.setRankDropCount(++rankCount);
                if (randomCount == rankCount) {
                    randomCount = RandomUtils.random(bean.getRelation_Num_min(), bean.getRelation_Num_max());
                    rankCount = 0;
                    bossRelationDropLimit.setRandomKillCount(randomCount);
                    bossRelationDropLimit.setRankDropCount(rankCount);
                    hasReward = true;
                    dropRoles.put(p.getId(), p);
                    getReward(p, type, bean.getRelation_Dorp(), ItemChangeReason.BossRelationGet, monster, false);
                }
            }

            //排名奖励, 不计数以及计数但没超过上限的情况才发奖励
            ++top;
            if (!bean.getRank_num().isEmpty() && top <= bean.getRank_num().get(bean.getRank_num().size() - 1)) {
                dropItemId = selectDropItem(top, bean.getRank_num(), bean.getRanking_Drop());
                if (dropItemId > 0) {
                    hasReward = true;
                    dropRoles.put(p.getId(), p);
                    //掉落原因码特殊处理，策划喊写死
                    int changeReason = bean.getDrop_type() == 8 ? ItemChangeReason.HorseBossDropGet : ItemChangeReason.BossRankGet;
                    getReward(p, type, dropItemId, changeReason, monster, true);
                }
            }

            //改为全部受排名奖励次数限制
            if (record && hasReward) {
                if (type == SpecialDropDefine.WORLD_BOSS) {
                    Manager.dailyActiveManager.deal().addDailyProgress(p, DailyActiveDefine.WORLD_BOSS, 1);
                    Manager.retrieveResManager.getScript().count(p, RetrieveType.WorldBoss);
                }
                if (type == SpecialDropDefine.BOSS_HOME) {
                    Manager.dailyActiveManager.deal().addDailyProgress(p, DailyActiveDefine.HOME_BOSS, 1);
                    Manager.countManager.addVariant(p, VariantType.Daily_Kill_VIP_Boss_Times, 1);
                    Manager.controlManager.operate(p, FunctionVariable.Daily_Kill_VIP_Boss_Times, 1);
                    Manager.retrieveResManager.getScript().count(p, RetrieveType.VipBoss);
                }
                if (type == SpecialDropDefine.SuitBoss) {
                   Manager.countManager.addVariant(p, VariantType.Daily_Kill_JingJia_Boss_Times, 1);
                   Manager.controlManager.operate(p, FunctionVariable.Daily_Kill_JingJia_Boss_Times, 1);
                   //Manager.retrieveResManager.getScript().count(p, RetrieveType.SuitBoss);
                }
                if (type == SpecialDropDefine.SOULANIMALISLAND_BOSS) {
                    Manager.dailyActiveManager.deal().addDailyProgress(p, DailyActiveDefine.SOUL_ANIMAL_ISLAND_BOSS, 1);
                }
                //跨服坐骑BOSS
                if (type == SpecialDropDefine.CrossHorseBoss) {
                    Manager.dailyActiveManager.deal().addDailyProgress(p, DailyActiveDefine.CrossHosreBoss, 1);
                }
                writeSpecialDropLog(monster.getModelId(), p, dailyId, bossRelationDropLimit);
            }

            remainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(p, dailyId);
            if (remainCount == 0) {
                Cfg_Mapsetting_Bean mapBean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(map.getMapModelId());
                int camp;
                if (mapBean.getScene_came_match_type() == 1) {
                    camp = p.getCamp() | 1;
                } else {
                    camp = map.getMapModelId();
                }
                //套装不计击杀次数
                if (type == SpecialDropDefine.SuitBoss){
                    camp = 0;
                }
                p.setCamp(camp, true);
                if (type == SpecialDropDefine.WORLD_BOSS || type == SpecialDropDefine.SuitBoss) {
                    BossMessage.ResRankCountTips.Builder msg = BossMessage.ResRankCountTips.newBuilder();
                    MessageUtils.send_to_player(p, BossMessage.ResRankCountTips.MsgID.eMsgID_VALUE, msg.build().toByteArray());
                }
            }
        }
        return dropRoles;
    }

    int calcDropId(MapObject map, Player player) {
        ReadIntegerArrayEs es = drops.get(map.getZoneModelId());
        if (es == null) {
            return 0;
        }
        ReadArray<Integer> array = Utils.findOne(es.getValuees(), a -> a.get(0) == player.getCareer());
        for (int i = 1; i < array.size(); i++) {
            int dropId = array.get(i);
            if (player.getNoodDrop().contains(dropId)) {
                continue;
            }
            player.getNoodDrop().add(dropId);
            return dropId;
        }
        player.getNoodDrop().clear();
        int dropId = array.get(0);
        player.getNoodDrop().add(dropId);
        return dropId;
    }

    private int getSpecialBossDailyId(int type) {
        switch (type) {
            case SpecialDropDefine.BOSS_HOME:
                return DailyActiveDefine.HOME_BOSS.getValue();
            case SpecialDropDefine.WORLD_BOSS:
                return DailyActiveDefine.WORLD_BOSS.getValue();
            case SpecialDropDefine.SuitBoss:
                return DailyActiveDefine.SUIT_BOSS.getValue();
            case SpecialDropDefine.SOULANIMALISLAND_BOSS:
                return DailyActiveDefine.SOUL_ANIMAL_ISLAND_BOSS.getValue();
        }
        return 0;
    }

    /**
     * 排名奖励掉落
     */
    private int selectDropItem(int top, ReadIntegerArray rank, ReadIntegerArray drop) {
        for (int i = 0; i < rank.size(); i++) {
            if (top <= rank.get(i)) {
                return drop.get(i);
            }
        }
        return 0;
    }

    private void getReward(Player player, int type, int dropId, int reason, Monster monster, boolean triggerMarryActivity) {
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (map == null) {
            return;
        }
        int modelId = monster.getModelId();
        List<List<Integer>> drops = Manager.dropManager.deal().getItemDrops(player, dropId);
        if (drops == null) {
            return;
        }
        List<Item> dropItems = Item.createItems(drops, 1);
        if (triggerMarryActivity) {
            List<Item> items = Manager.marriageManager.activity().triggerDrop(player.getCareer(), monster.getModelId(), -1, 1);
            dropItems.addAll(items);
        }
        if (dropItems.isEmpty()) {
            return;
        }
        //需要广播筛选
        needDropNotice(player,dropItems,map,monster);

        //记录玩家获得的物品
        writeDropHistory(map, player.getId(), dropItems);
        syncDropItemToRoundPlayer(player, monster, dropItems, true);

        if (GameServer.getInstance().IsFightServer()) {
            Manager.crossServerManager.getCrossServer().sendReward(player, dropItems, reason);
        } else {
            if (!Manager.backpackManager.manager().addItems(player, dropItems, reason, modelId)) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail, MessageString.System, MessageString.System, MessageString.Task_Content, dropItems, reason, modelId);
            }
        }
    }

    public void needDropNotice(Player player,List<Item> dropItems,MapObject map,Monster monster){
        for (Item item : dropItems) {

            if (item instanceof Equip) {
                Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(item.getItemModelId());
                if (bean != null) {
//                    MessageUtils.notify_allOnlinePlayer(Notify.CHAT_SYS_MARQUEE, MessageString.DropNotice, player.getName(),
//                            ServerStr.getChatTableName(map.getName()), ServerStr.getChatTableName(monster.getName()), ServerStr.getChatTableName(bean.getName()));

                    if (bean.getDrop_notice() != 0 || bean.getChatchannel() != null) {
                        //TODO:公告 只能发本服
                        MessageUtils.notify_allOnlinePlayer(player,bean.getDrop_notice() , bean.getChatchannel(), MessageString.DropNotice,
                                player.getId()+"", player.getName(),ServerStr.getChatTableName(map.getName()),ServerStr.getChatTableName(monster.getName()),
                                Manager.backpackManager.manager().getChatInfo(item),
                                Utils.makeUrlStr(MessageString.DropNotice));

                    }



                }
            } else {
                Cfg_Item_Bean bean = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
                if (bean != null) {
                    //@todo 修改公告
                    if (bean.getDrop_notice() != 0 || bean.getChatchannel() != null) {
                        //TODO:公告
                        MessageUtils.notify_allOnlinePlayer(player,bean.getDrop_notice() , bean.getChatchannel(), MessageString.DropNotice,
                                player.getId()+"", player.getName(),ServerStr.getChatTableName(map.getName()),ServerStr.getChatTableName(monster.getName()),
                                Manager.backpackManager.manager().getChatInfo(item),
                                Utils.makeUrlStr(MessageString.DropNotice));
                    }
                }
            }
        }
    }

    @Override
    public void syncDropItemToRoundPlayer(Player player, Monster monster, List<Item> items, boolean filter) {
        MapMessage.ResMonsterDieGetItem.Builder msg = MapMessage.ResMonsterDieGetItem.newBuilder();
        msg.setInstanceId(monster.getId());
        msg.setRoleId(player.getId());
        for (Item item : items) {
            CopyMapMessage.cardItemInfo.Builder itemInfo = CopyMapMessage.cardItemInfo.newBuilder();
            itemInfo.setItemId(item.getItemModelId());
            itemInfo.setNum(item.getNum());
            itemInfo.setBind(item.isBind());
            msg.addList(itemInfo);
        }
        MessageUtils.send_to_player(player, MapMessage.ResMonsterDieGetItem.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        MapMessage.ResMonsterDieGetItem.Builder builder = MapMessage.ResMonsterDieGetItem.newBuilder();
        builder.setInstanceId(monster.getId());
        builder.setRoleId(player.getId());
        for (Item item : items) {
            Cfg_Item_Bean bean = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
            //红色品质才同步给周围玩家
            if (filter && (bean == null || bean.getColor() < 7)) {
                continue;
            }
            CopyMapMessage.cardItemInfo.Builder itemInfo = CopyMapMessage.cardItemInfo.newBuilder();
            itemInfo.setItemId(item.getItemModelId());
            itemInfo.setNum(item.getNum());
            itemInfo.setBind(item.isBind());
            builder.addList(itemInfo);
        }
        if (builder.getListList().isEmpty()) {
            return;
        }
        MessageUtils.send_to_roundPlayer(player, MapMessage.ResMonsterDieGetItem.MsgID.eMsgID_VALUE, builder.build().toByteArray(), false);
    }

    /**
     * 记录玩家掉落情况
     *
     * @param mapObject 副本
     * @param roleId    玩家唯一id
     * @param dropItems 新增掉落
     */
    private void writeDropHistory(MapObject mapObject, long roleId, List<Item> dropItems) {

        ZoneCache zone = mapObject.getZone();
        HashMap<Integer, Item> items = zone.getDropItemsHistory(roleId);

        for (Item item : dropItems) {
            Item cache = items.get(item.getItemModelId());
            if (cache != null) {
                if (cache instanceof ExpValueItem) {
                    ExpValueItem exp = (ExpValueItem) cache;
                    ExpValueItem itemExp = (ExpValueItem) cache;
                    exp.setExpNum(exp.getExpNum() + itemExp.getExpNum());
                }
                cache.setNum(cache.getNum() + item.getNum());
            } else {
                try {
                    items.put(item.getItemModelId(), item.clone());
                } catch (CloneNotSupportedException e) {
                    log.error("物品克隆出错 " + item, e);
                }
            }
        }
    }

    private void writeSpecialDropLog(int bossId, Player player, int dailyId, BossRelationDropLimit bossRelationDropLimit) {
        SpecialDropLog log = new SpecialDropLog();
        log.setBossId(bossId);
        log.setPlayerInfo(player.getPlatformName(), player.getCreateServerId(), player.getUserId(), player.getId(), player.getName());
        log.setRankDropCount(player.getDailyActiveData().getDailyProgress().getOrDefault(dailyId, 0));
        if (bossRelationDropLimit != null) {
            log.setRandomCount(bossRelationDropLimit.getRandomKillCount());
            log.setHasRankCount(bossRelationDropLimit.getRankDropCount());
        }
        LogService.getInstance().execute(log);
    }
}
