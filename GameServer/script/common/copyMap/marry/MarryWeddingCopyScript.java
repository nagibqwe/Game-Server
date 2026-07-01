package common.copyMap.marry;

import com.data.CfgManager;
import com.data.Global;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Characters_Bean;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Gather_Bean;
import com.data.struct.ReadArray;
import com.game.backpack.structs.Item;
import com.game.chat.structs.Notify;
import com.game.copymap.scripts.ICopyGatherScript;
import com.game.map.manager.MapManager;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.marriage.script.IMarryWeddingScript;
import com.game.marriage.struct.Marriage;
import com.game.marriage.struct.WeddingMapInfo;
import com.game.marriage.struct.WeddingOperation;
import com.game.monster.structs.Monster;
import com.game.player.manager.PlayerManager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.game.structs.Gather;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import game.core.map.Position;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.MarriageMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class MarryWeddingCopyScript implements IMarryWeddingScript, ICopyGatherScript {

    final static Logger logger = LogManager.getLogger(MarryWeddingCopyScript.class);

    final int GatherPosKey = 1;
    final int WeddingStateKey = 2;

    @Override
    public int getId() {
        return ScriptEnum.MarryWeddingActivityScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    public boolean onBeginGather(Player player, Gather gather) {

        MapObject map = Manager.mapManager.getMap(gather.gainMapId());
        if (map == null) {
            logger.info("所在地图已经被销毁!!!" + gather.gainMapModelId());
            return false;
        }
        Cfg_Gather_Bean gatherCfg = CfgManager.getCfg_Gather_Container().getValueByKey(gather.getModelId());
        if (null == gatherCfg) {
            logger.error("配置的没有，怎么初始化！");
            return false;
        }
        WeddingMapInfo wedding = Manager.marriageManager.getWedding();
        if (wedding == null)
            return false;
        WeddingOperation weddingOperation = wedding.getOperation().get(player.getId());
        if (gatherCfg.getType() == 3) {
            if (weddingOperation.getGatherNum() >= wedding.getGatherMax()) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Marry_Gift_Limit_Notice);
                return false;
            }

        } else {
            if (weddingOperation.getEatFoodNum() >= Global.Marry_Food_MaxCount) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Marry_Gather_Limit_Notice);
                return false;
            }
        }
        return true;
    }

    public void onGather(Player player, Gather gather) {
        MapObject map = Manager.mapManager.getMap(gather.gainMapId());
        if (map == null) {
            logger.info("所在地图已经被销毁!!!" + gather.gainMapModelId());
            return;
        }
        Cfg_Gather_Bean gatherCfg = CfgManager.getCfg_Gather_Container().getValueByKey(gather.getModelId());
        if (null == gatherCfg) {
            logger.error("配置的没有，怎么初始化！");
            return;
        }
        WeddingMapInfo wedding = Manager.marriageManager.getWedding();
        if (wedding == null)
            return;
        WeddingOperation weddingOperation = wedding.getOperation().get(player.getId());
        if (gatherCfg.getType() == 3) {
            weddingOperation.setGatherNum(weddingOperation.getGatherNum() + 1);
        } else {
            weddingOperation.setEatFoodNum(weddingOperation.getEatFoodNum() + 1);
        }

        resMarryCopyInfo(player, wedding);
    }

    public void onOutGather(Player player, Gather gather) {

    }

    @Override
    public void onCreate(MapObject mapObject, Object... objects) {

        WeddingMapInfo wedding = Manager.marriageManager.getWedding();

        mapObject.setAutoRemove(false);
        mapObject.getParams().put(GatherPosKey, new HashSet<String>());
        mapObject.getParams().put(WeddingStateKey, wedding.getStage());

        mapObject.setOwnId(wedding.getWeddingID());
        //添加timer计时器
        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(mapObject.getZoneModelId());

        mapObject.addMapLoopScriptEventTimer(getId(), "addexp", -1, 0, 10000);//策划欧阳帆偷懒不配置，喊写死

        mapObject.addMapLoopScriptEventTimer(getId(), "weddingTick", -1, 0, 2000);

        wedding.getWeddingScene().put(mapObject.getId(), 0);

        refreshGather(wedding, mapObject);

        logger.info("创建婚姻副本 wedding={}  分宴={}", wedding.getWeddingID(), objects[0]);
    }

    @Override
    public boolean canEnterMap(Player player, int model, int level) {

        WeddingMapInfo wedding = Manager.marriageManager.getWedding();
        if (wedding == null) {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.Marry_Copy_Exist);
            Manager.marriageManager.manager().tick();
            return false;
        }
        Marriage marriage = Manager.marriageManager.getMarriageList().get(wedding.getWeddingID());

        if (marriage.getInviteList().containsKey(player.getId()) || marriage.getBeMarriageId() == player.getId() || marriage.getMarriageId() == player.getId()) {
            return true;
        }
        MessageUtils.notify_player(player, Notify.NORMAL, MessageString.Marry_Copy_Invite_Exist);
        return false;
    }

    @Override
    public void onEnterMap(Player player, MapObject mapObject, boolean login) {
        WeddingMapInfo wedding = Manager.marriageManager.getWedding();

        if (!wedding.getAddhotList().contains(player.getId())) {
            wedding.setHot(wedding.getHot() + Global.Marry_Player_EnterHot);
            wedding.getAddhotList().add(player.getId());
        }

        Player hubby = PlayerManager.getInstance().getPlayer(wedding.getHubbyID());
        Player wife = PlayerManager.getInstance().getPlayer(wedding.getWifeID());

        MarriageMessage.ResMarryCopyEnter.Builder msg = MarriageMessage.ResMarryCopyEnter.newBuilder();
        msg.setRemianTime((int) (wedding.getOverTime() / 1000L));
        if (hubby != null) {
            msg.setRoleId1(hubby.getId());
            msg.setRoleCareer1(hubby.getCareer());
            msg.setRoleName1(hubby.getName());
            msg.setMarryCopyHotIsBuy1(wedding.getHubbyIsBuyHot());
        }
        if (wife != null) {
            msg.setRoleId2(wife.getId());
            msg.setRoleCareer2(wife.getCareer());
            msg.setRoleName2(wife.getName());
            msg.setMarryCopyHotIsBuy2(wedding.getWifeIsBuyHot());
        }

        WeddingOperation operation = wedding.getOperation().get(player.getId());
        if (operation == null) {
            operation = new WeddingOperation();
            wedding.getOperation().put(player.getId(), operation);
        }
        int signCount = 0;
        if (wedding.getOperation() != null && wedding.getOperation().size() > 0) {
            for (WeddingOperation operation1 : wedding.getOperation().values()) {
                if (operation1.isMarryCopySign()) {
                    signCount += 1;
                }
            }
        }
        msg.setSignCount(signCount);
        //是否签到
        msg.setIsSign(operation.isMarryCopySign());
        MessageUtils.send_to_player(player, MarriageMessage.ResMarryCopyEnter.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        resMarryCopyInfo(player, wedding);

        sendBossState(player, wedding.isBossIsDie());

        wedding.getWeddingScene().put(mapObject.getId(), mapObject.getPlayers().size());
    }

    @Override
    public void onQuitMap(Player player, MapObject mapObject, boolean isQuit) {

        WeddingMapInfo wedding = Manager.marriageManager.getWedding();
        wedding.getWeddingScene().put(mapObject.getId(), mapObject.getPlayers().size());
    }

    @Override
    public void onDamage(MapObject mapObject, Monster monster, long damage, Fighter attacker) {

    }

    @Override
    public void onMonsterDie(MapObject mapObject, Monster monster, Fighter attackerp) {

    }

    @Override
    public void onMonsterAfterDie(MapObject mapObject, Monster monster, Fighter attacker) {

        for (Player player : mapObject.getPlayers().values()) {
            sendBossState(player, true);
        }

        WeddingMapInfo wedding = Manager.marriageManager.getWedding();
        if (wedding.isBossIsDie()) {
            return;
        }
        wedding.setBossIsDie(true);

        Marriage marriage = Manager.marriageManager.getMarriageList().get(wedding.getWeddingID());
        //给夫妻双方发参与奖
        List<Item> items = Item.createItems(Global.Marry_Boss_Reward);
        String huabandName = PlayerManager.getInstance().getPlayer(marriage.getMarriageId()).getName();
        String wifeName = PlayerManager.getInstance().getPlayer(marriage.getBeMarriageId()).getName();
        String context = MessageString.Marry_Boss_Reward_Mail + "@_@" + huabandName + "@_@" + wifeName;
        long actionId = IDConfigUtil.getLogId();
        Manager.mailManager.sendMailToPlayer(marriage.getMarriageId()
                , MailType.SysCommonRewardMail
                , MessageString.System
                , MessageString.Marry_Boss_Reward_Mail_Titel
                , context
                , items
                , ItemChangeReason.MarryBossRewardGet, actionId);

        Manager.mailManager.sendMailToPlayer(marriage.getBeMarriageId()
                , MailType.SysCommonRewardMail
                , MessageString.System
                , MessageString.Marry_Boss_Reward_Mail_Titel
                , context
                , items
                , ItemChangeReason.MarryBossRewardGet, actionId);
    }

    @Override
    public void onLeaveBattle(MapObject map, Monster monster, Player attacker) {

    }

    private void sendBossState(Player player, boolean isDie) {
        MarriageMessage.ResMarryCopyBossState.Builder builder = MarriageMessage.ResMarryCopyBossState.newBuilder();
        builder.setBossIsDie(isDie);
        MessageUtils.send_to_player(player, MarriageMessage.ResMarryCopyBossState.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void onPlayerDie(MapObject mapObject, Fighter attacker, Player player) {

    }

    @Override
    public void action(MapObject mapObject, String method, Object[] params) {
        switch (method) {
            case "weddingTick":
                weddingTick(mapObject);
                break;
            case "addexp":
                addexp(mapObject);
                break;
        }
    }

    private void addexp(MapObject mapObject) {

        WeddingMapInfo wedding = Manager.marriageManager.getWedding();
        for (Map.Entry<Long, Player> playerEntry : mapObject.getPlayers().entrySet()) {
            Player player = playerEntry.getValue();
            Cfg_Characters_Bean bean = CfgManager.getCfg_Characters_Container().getValueByKey(player.getLevel());
            if (bean == null)
                continue;
            WeddingOperation weddingOperation = wedding.getOperation().get(player.getId());
            if (weddingOperation != null) {
                long exp = (long) (bean.getMarry_single_add_exp() * player.gainExpRate());
                weddingOperation.setAddExp(weddingOperation.getAddExp() + exp);
                Manager.currencyManager.manager().addEXP(player, exp, ItemChangeReason.MarrigeCopyExpGet, IDConfigUtil.getLogId());
                resMarryCopyInfo(player, wedding);
            }
        }
    }

    private void wedding(MapObject mapObject) {
        WeddingMapInfo wedding = Manager.marriageManager.getWedding();
        wedding.setStage(1);
        mapObject.getParams().put(WeddingStateKey, 1);
        MarriageMessage.ResMarryCopyPlayViedo.Builder msg = MarriageMessage.ResMarryCopyPlayViedo.newBuilder();
        for (Map.Entry<Long, Player> playerEntry : mapObject.getPlayers().entrySet()) {
            MessageUtils.send_to_player(playerEntry.getValue(), MarriageMessage.ResMarryCopyPlayViedo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    private void weddingTick(MapObject mapObject) {

        long time = TimeUtils.Time();
        WeddingMapInfo wedding = Manager.marriageManager.getWedding();

        //婚宴开席
        if ((int) mapObject.getParam(WeddingStateKey) == 0 && wedding.getWeddingTime() < time) {
            wedding(mapObject);
        }
        //副本结束
        if (wedding.getOverTime() < time) {
            for (Map.Entry<Long, Player> playerEntry : mapObject.getPlayers().entrySet()) {
                Manager.mapManager.changeMap(playerEntry.getValue(), Manager.playerManager.getBornMapID(), null, -1, false);
            }
            mapObject.setStop(true);
        }
    }

    private void resMarryCopyInfo(Player player, WeddingMapInfo weddingMapInfo) {

        WeddingOperation weddingOperation = weddingMapInfo.getOperation().get(player.getId());
        if (weddingOperation == null) {
            logger.error("WeddingOperation  is Null  " + player.getId());
            return;
        }
        MarriageMessage.ResMarryCopyInfo.Builder msg = MarriageMessage.ResMarryCopyInfo.newBuilder();
        msg.setStage(weddingMapInfo.getStage());
        msg.setHot(weddingMapInfo.getHot());
        msg.setEatCount(weddingOperation.getEatFoodNum());
        msg.setGatherCount(weddingOperation.getGatherNum());
        msg.setExp(weddingOperation.getAddExp());
        MessageUtils.send_to_player(player, MarriageMessage.ResMarryCopyInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void removeMap(MapObject mapObject) {
        Manager.marriageManager.manager().clear();
    }

    /**
     * 发送滚屏
     *
     * @param map
     * @param player
     * @param context
     */
    @Override
    public void reqMarrySendBulletScreen(MapObject map, Player player, String context) {

        MarriageMessage.ResMarrySendBulletScreen.Builder msg = MarriageMessage.ResMarrySendBulletScreen.newBuilder();
        msg.setContext(context);
        msg.setRoleCareer(player.getCareer());
        msg.setRoleId(player.getId());
        msg.setRoleName(player.getName());

        for (Player role : map.getPlayers().values()) {
            MessageUtils.send_to_player(role, MarriageMessage.ResMarrySendBulletScreen.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    /**
     * 婚宴送礼
     *
     * @param map
     * @param player
     * @param target
     * @param gift
     */
    @Override
    public void weddingGift(MapObject map, Player player, Player target, int gift, int count) {

//        logger.info("婚宴送礼 gift={} player={}", gift, player);

        WeddingMapInfo wedding = Manager.marriageManager.getWedding();

        int type = refreshGather(wedding, map);

        for (Player player1 : map.getPlayers().values()) {
            MessageUtils.notify_player(player1, Notify.CHAT_SYS_MARQUEE, MessageString.Marry_Blessing_Title,
                    player.getName(), target.getName(), Manager.backpackManager.manager().getName(gift), count);
            resMarryCopyInfo(player1, wedding);
            if (type == 1) {
                MessageUtils.notify_player(player1, Notify.CHAT_SYS_MARQUEE, MessageString.Marry_Gift_System_Give);
            } else if (type == 2) {
                MessageUtils.notify_player(player1, Notify.CHAT_SYS_MARQUEE, MessageString.Marry_Boss_Turn_on);
            }
        }
    }

    /**
     * 放烟花
     *
     * @param map
     * @param player
     * @param gift
     */
    @Override
    public void weddingUseGift(MapObject map, Player player, int gift) {

//        logger.info("婚宴使用道具 gift={} player={}", gift, player);

        WeddingMapInfo wedding = Manager.marriageManager.getWedding();

        int type = refreshGather(wedding, map);

        MarriageMessage.ResMarryUseItemBroadcast.Builder msg = MarriageMessage.ResMarryUseItemBroadcast.newBuilder();
        msg.setRoleId(player.getId());
        msg.setRoleName(player.getName());
        msg.setItemID(gift);

        for (Player player1 : map.getPlayers().values()) {
            MessageUtils.send_to_player(player1, MarriageMessage.ResMarryUseItemBroadcast.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            resMarryCopyInfo(player1, wedding);

            if (type == 1) {
                MessageUtils.notify_player(player1, Notify.CHAT_SYS_MARQUEE, MessageString.Marry_Gift_System_Give);
            } else if (type == 2) {
                MessageUtils.notify_player(player1, Notify.CHAT_SYS_MARQUEE, MessageString.Marry_Boss_Turn_on);
            }
        }
    }

    /**
     * 购买热度
     *
     * @param map
     * @param player
     */
    @Override
    public void weddingBuyHot(MapObject map, Player player) {

        WeddingMapInfo wedding = Manager.marriageManager.getWedding();
        //全地图广播
        MarriageMessage.ResMarryCopyBuyHot.Builder msg = MarriageMessage.ResMarryCopyBuyHot.newBuilder();
        msg.setMarryCopyHotIsBuy1(wedding.getHubbyIsBuyHot());
        msg.setMarryCopyHotIsBuy2(wedding.getWifeIsBuyHot());
        msg.setHotValue(wedding.getHot());

        int type = refreshGather(wedding, map);

        for (Player player1 : map.getPlayers().values()) {
            MessageUtils.send_to_player(player1, MarriageMessage.ResMarryCopyBuyHot.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            resMarryCopyInfo(player1, wedding);

            if (type == 1) {
                MessageUtils.notify_player(player1, Notify.CHAT_SYS_MARQUEE, MessageString.Marry_Gift_System_Give);
            } else if (type == 2) {
                MessageUtils.notify_player(player1, Notify.CHAT_SYS_MARQUEE, MessageString.Marry_Boss_Turn_on);
            }
        }
    }

    private int refreshGather(WeddingMapInfo weddingMapInfo, MapObject map) {

        int proSize = Global.Marry_Hot_Progress.size();

        for (int i = proSize - 1; i >= 0; i--) {
            ReadArray<Integer> array = Global.Marry_Hot_Progress.get(i);
            int needHot = array.get(0);
            if (weddingMapInfo.getHot() < needHot) {
                continue;
            }
            if (needHot <= weddingMapInfo.getCurRefreshHot().getOrDefault(map.getId(), 0)) {
                continue;
            }
            weddingMapInfo.getCurRefreshHot().put(map.getId(), needHot);
            if (array.get(1) > 0) {
                Position pos = new Position(array.get(2), array.get(3));
                logger.info(" 婚宴刷新怪物 boss={} map={}", array.get(1), map.getId());
                Manager.monsterManager.createMonster(map, pos, array.get(1));
                return 2;
            } else {
                int gatherID = array.get(2);
                int num = array.get(3);
                HashSet<String> history = map.getParam(GatherPosKey);
                Cfg_Gather_Bean gatherCfg = CfgManager.getCfg_Gather_Container().getValueByKey(gatherID);
                if (null == gatherCfg) {
                    logger.error("Cfg_Gather_Bean无法找到指定数据，婚宴生成采集物失败，id = " + gatherID);
                    continue;
                }
                List<Position> poslist = new ArrayList<>();
                for (ReadArray<Integer> aii : Global.Marry_Souger_Pos.getValuees()) {
                    if (history.contains(aii.get(0) + "_" + aii.get(1))) {
                        continue;
                    }
                    poslist.add(MapManager.getPos(aii.get(0), aii.get(1)));
                }
                for (int k = 0; k < num; ++k) {
                    if (poslist.size() <= 0) {
                        logger.error("婚宴 策划配置的 坐标 长度不够");
                        break;
                    }
                    Position pos = poslist.get(0);
                    int randIndex = -1;
                    if (poslist.size() > 1) {
                        randIndex = RandomUtils.random(poslist.size());
                        pos = poslist.get(randIndex);
                        poslist.remove(randIndex);
                    }
                    Gather gather = Manager.gatherManager.deal().createGather(map, gatherCfg, pos);
                    if (gather == null) {
                        logger.error(map.getName() + " 创建采集物时出错了", new NullPointerException());
                        if (randIndex >= 0) {
                            poslist.add(pos);
                        }
                        continue;
                    }
                    history.add(pos.getX() + "_" + pos.getY());
                }
            }
            return 1;
        }
        return -1;
    }

}
