package common.cross;

import com.data.CfgManager;
import com.data.FunctionVariable;
import com.data.MessageString;
import com.data.bean.*;
import com.data.struct.ReadIntegerArrayEs;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.boss.struct.BossTypeConst;
import com.game.bravepeak.struct.BravePeakDefine;
import com.game.chat.structs.Notify;
import com.game.connectfightserver.manager.ConnectFightManager;
import com.game.connectfightserver.struct.ConnectFightServer;
import com.game.copymap.log.CopyMapLogRecord;
import com.game.copymap.structs.CloneEndRewardInfo;
import com.game.count.structs.VariantType;
import com.game.crossserver.scripts.ICrossServerScript;
import com.game.dailyactive.manager.DailyActiveManager;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.fightserver.manager.FightClientManager;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.player.manager.PlayerManager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.player.structs.SavePlayerLevel;
import com.game.player.structs.SessionAttribute;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.structs.EntityState;
import com.data.ItemChangeReason;
import com.game.structs.ServerStr;
import com.game.task.structs.TaskHelp;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import game.core.dblog.LogService;
import game.core.script.IScript;
import game.core.util.*;
import game.message.*;
import game.message.CrossFightMessage.F2PFightRoomState;
import game.message.CrossServerMessage.*;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 游戏服与战斗服交互的公用脚本
 *
 * @author admin
 */
public class CrossServerScript implements IScript, ICrossServerScript {

    private final static Logger LOG = LogManager.getLogger("CrossServerScript");

    @Override
    public int getId() {
        return ScriptEnum.CrossServerBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    //跨服鼓舞
    @Override
    public void G2F_UpMorale(ChannelHandlerContext context, CrossServerMessage.G2F_UpMorale messInfo) {

        Player player = Manager.playerManager.getPlayerOnline(messInfo.getRoleId());
        if (player == null) {
            return;
        }

        F2G_UpMoraleRes.Builder msg = F2G_UpMoraleRes.newBuilder();
        msg.setRoleId(player.getId());
        msg.setDec(true);
        msg.setType(messInfo.getType());
        FightClientManager.GetInstance().send_to_game(context, F2G_UpMoraleRes.MsgID.eMsgID_VALUE, msg.build().toByteArray());

    }

    /**
     * 向战斗服告之当前有物品使用成功
     *
     * @param context
     * @param mess
     */
    @Override
    public void OnG2FReqCrossUseItem(ChannelHandlerContext context, G2FReqCrossUseItem mess) {
        long roleId = mess.getRoleId();
        long itemId = mess.getItemId();
        int num = mess.getNum();
        int modelId = mess.getModelId();

        Player player = Manager.playerManager.getPlayerOnline(roleId);

        if (player == null) {
            LOG.error("roleId=" + roleId + ", 使用物品时已经不在线了！");
            return;
        }

        //获得要移动物品
        Item item = Manager.backpackManager.manager().getItemById(player, itemId);
        if (item == null) {
            item = Item.createItem(modelId, num, true);
            Manager.backpackManager.manager().addItem(player, item, ItemChangeReason.DropGet, itemId);
        }

        if (item.getNum() < num) {
            item.setNum(num);
        }
        //使用物品
        long actionId = IDConfigUtil.getLogId();
        F2GResCrossUseItem.Builder msg = F2GResCrossUseItem.newBuilder();
        msg.setItemId(itemId);
        msg.setRoleId(roleId);
        msg.setModelId(modelId);
        msg.setNum(mess.getNum());
        msg.setCost(mess.getCost());
        if (item.use(player, num, 0, actionId)) {
            msg.setState(0);
        } else {
            msg.setState(-1);
        }
        FightClientManager.GetInstance().send_to_game(context, F2GResCrossUseItem.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 被告之在战斗服中使用了某个物品成功的返回 g
     *
     * @param context
     * @param mess
     */
    @Override
    public void OnF2GResCrossUseItem(ChannelHandlerContext context, F2GResCrossUseItem mess) {
        long roleId = mess.getRoleId();
        int modelId = mess.getModelId();

        Player player = Manager.playerManager.getPlayerOnline(roleId);
        LOG.error(roleId + "在战斗服使用了物品" + modelId + " 的状态值" + mess.getState());
        if (mess.getState() == -1) {
            return;
        }
        if (player == null) {
            LOG.error("使用物品，玩家不存在：" + roleId);
            return;
        }
        Cfg_Item_Bean bean = CfgManager.getCfg_Item_Container().getValueByKey(modelId);
        if (bean == null) {
            LOG.error("Cfg_Item_Bean配置表不存在：" + modelId);
            return;
        }
        if (mess.getCost() == 1) {
            if (!Manager.backpackManager.manager().onRemoveItem(player, bean.getId(), mess.getNum(), ItemChangeReason.OwnUseDec, 0L)) {
                LOG.error(String.format("玩家[%s]使用物品扣除失败: %s：", roleId, bean.getId()));
            }
        }
    }

    /**
     * 跨服掉落进入到游戏中
     *
     * @param context
     * @param mess
     */
    @Override
    public void OnF2GResCrossDropItem(ChannelHandlerContext context, F2GResCrossDropItem mess) {
        long roleId = mess.getRoleId();
        boolean isNotice = mess.getIsnotice();
        String sbStr, pinStr = "";

        Player player = Manager.playerManager.getPlayerCache(roleId);
        if (player == null) {
            return;
        }

        boolean specNotice = false;
        List<Item> rt = new ArrayList<>();
        for (dropItemInfo info : mess.getItemListList()) {
            boolean isBind = info.getIsBind();
            List<Item> titems = Item.createItems(info.getItemModelId(), info.getNum(), isBind, 0);
            if (titems != null && titems.size() > 0) {
                rt.addAll(titems);
            }

            if (info.getNotice()) {
                specNotice = true;
                pinStr = Utils.makeItemsStr(titems);
            }
        }

        if (isNotice) {
            sbStr = Utils.makeItemsStr(rt);
            if (sbStr.length() > 1) {
                G2FReqCrossDropItemString.Builder msg = G2FReqCrossDropItemString.newBuilder();
                msg.setRoleId(roleId);
                msg.setStrmess(sbStr);
                msg.setMonsterModelId(mess.getMonsterModelId());
                ConnectFightManager.GetInstance().send_to_fight(context, -1, G2FReqCrossDropItemString.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }
        }

        if (specNotice) {
            G2FReqCrossDropItemString.Builder msg = G2FReqCrossDropItemString.newBuilder();
            msg.setRoleId(roleId);
            msg.setStrmess(pinStr);
            msg.setMonsterModelId(mess.getMonsterModelId());
            ConnectFightManager.GetInstance().send_to_fight(context, -1, G2FReqCrossDropItemString.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }

        List<Item> result = new ArrayList<>();
        if (player.isOnline()) {
            for (Item item : rt) {
                Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
                if (model == null) {
                    LOG.error("玩家(" + player.getId() + ")模型:" + item.getItemModelId() + "任务物品加入包裹开始检查失败找不到物品的模型物品ID" + item.getId());
                    continue;
                }
                if (!Manager.backpackManager.manager().addItem(player, item, ItemChangeReason.DropByFightServerGet, mess.getActionId())) {
                    result.add(item);
                }
            }
        } else {
            result.addAll(rt);
        }

        if (result.size() > 0) {
            Manager.mailManager.sendMailToPlayer(roleId, 1, MessageString.System, MessageString.DropMailTitle
                    , MessageString.DropMailContent, result, ItemChangeReason.DropByFightServerGet, mess.getActionId());
        }

    }

    /**
     * 战斗服的掉落货币
     *
     * @param context
     * @param mess
     */
    @Override
    public void OnF2GResCrossDropCoin(ChannelHandlerContext context, F2GResCrossDropCoin mess) {

        long roleId = mess.getRoleId();
        long actionId = mess.getRoleId();

        Player player = Manager.playerManager.getPlayerOnline(roleId);
        if (player != null) {
            Manager.currencyManager.manager().onAddItemCoin(player, mess.getCoinType(), mess.getCoinNum(), mess.getReason(), actionId);
        }
    }

    private final Object endObj = new Object();

    /**
     * 战斗服通知游戏服，战场已经结束
     *
     * @param context
     * @param mess
     */
    @Override
    public void OnF2GFightEnd(ChannelHandlerContext context, F2GFightEnd mess) {
        synchronized (endObj) {

            long fid = mess.getFightId();
            int modelId = mess.getModelId();

            if (modelId < 1) {
                return;
            }
            Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(modelId);
//            //不是跨服
//            if (bean.getIskuafu() < 1) {
//                return;
//            }
            List<Long> rids = new ArrayList<>();
            List<Player> players = new ArrayList<>();

            for (fightEndScore fes : mess.getRoleInfoList()) {
                long roleId = fes.getRoleId();
                rids.add(roleId);
                Player player = Manager.playerManager.getPlayerCache(roleId);
                if (player != null) {
                    players.add(player);
                }
            }
            CloneEndRewardInfo ceri = new CloneEndRewardInfo();
            ceri.setModelId(modelId);
            ceri.setZoneId(fid);
            //处理每一个玩家的成功与失败
            for (fightEndScore fes : mess.getRoleInfoList()) {
                try {
                    sendReward(fid, modelId, fes, ceri, rids, bean);
                } catch (Exception e) {
                    LOG.error(e, e);
                }
            }
        }
    }

    /**
     * 给指定的玩家发奖
     *
     * @param fid
     * @param modelId
     * @param fes
     * @param ceri
     * @param rids
     * @param bean
     */
    private void sendReward(long fid, int modelId, fightEndScore fes, CloneEndRewardInfo ceri, List<Long> rids, Cfg_Clone_map_Bean bean) {
        long roleId = fes.getRoleId();
        boolean isSuccess = fes.getIsSuccess();
        long score = fes.getScore();
        int fightTime = fes.getTime();
        int rewardTime = fes.getRewardtime();
        LOG.info("fid =" + fid + "(" + modelId + ")收到玩家：" + roleId + " 的战斗结果：" + isSuccess + "  积分：" + score + " , 花的时间:" + fightTime);
        String key = fid + "_" + roleId + "_" + rewardTime;
        boolean isOver = Manager.crossServerManager.getFightEndCache().containsKey(key);
        if (isOver) {
            LOG.info("fid =" + fid + "收到玩家：" + roleId + " 已经处理了结果了");
            return;
        }
        Manager.crossServerManager.getFightEndCache().put(key, true);
        LOG.info("fid =" + fid + "收到玩家：" + roleId + " 发奖开始");
        Player player = Manager.playerManager.getPlayerCache(roleId);
        //发成功奖
        ReadIntegerArrayEs overReward = bean.getSuccess_reward();
        if (!isSuccess) {
            //发失败奖
            overReward = bean.getFail_reward();
        }
        String addString = "";

        if (player != null) {
            //如果玩家不在线, 10分钟后保存玩家的数据
            if (!player.isOnline()) {
                Manager.playerManager.savePlayer(player, SavePlayerLevel.TenMinLater);
            }
        }

        if (player != null && player.isOnline()) {
            MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, MessageString.CLONESUCCESSFAILTISHI, ServerStr.getChatTableName(bean.getDuplicate_name()), "1&_" + (isSuccess ? MessageString.TishiSuccess : MessageString.TishiFailure));
            ceri.setScore(score);
            ceri.setSort(fes.getSortIndex());
            ceri.setTime(fightTime);
            ceri.setSuccess(isSuccess);
            ceri.setStar(fes.getStarNum());
            ceri.getRoleIds().clear();
            ceri.getRoleIds().addAll(rids);
            ceri.getRoleIds().remove(player.getId());
        }
        CopyMapLogRecord log = new CopyMapLogRecord();
        log.setIsSuccess(isSuccess ? 1 : 2);
        log.setPlayerid(roleId);
        log.setScore(score);
        log.setName(Manager.registerManager.getRoleName(roleId));
        log.setPlatform(Manager.playerManager.getPlatformName(roleId));//ServerConfig.getServerPlatform());//记录平台用户来源
        log.setType(0);
        log.setZonemodelid(modelId);
        log.setCopyOverTime(fightTime);
        StringBuilder sb = new StringBuilder();

        sb.append(addString);
        log.setReward(sb.toString());
        log.setActionId(fid);
        log.setSid(Manager.playerManager.getCreateServeId(roleId));
        log.setStar(fes.getStarNum());
        log.setTeamId(0);
        LogService.getInstance().execute(log);
        LOG.info("fid =" + fid + "收到玩家：" + roleId + " 发奖结束, 获得：" + overReward);
    }

    /**
     * 跨服副本关闭通知
     *
     * @param context
     * @param mess
     */
    @Override
    public void OnF2GCloneClose(ChannelHandlerContext context, F2GCloneClose mess) {
        for (Long ll : mess.getRoleIdsList()) {
            Player player = Manager.playerManager.getPlayerCache(ll);
            if (player == null) {
                LOG.info(ll + "找不到玩家， 从副本 " + Manager.copyMapManager.getCopyMapName(mess.getModelId()) + " 出来了！");
                continue;
            }
            player.playerCrossData.isReqFight = false;
            if (!player.playerCrossData.isToFightServer()) {
                continue;
            }
            if (player.playerCrossData.toFightId == 0) {
                continue;
            }
            crossbackDeal(player, mess.getModelId());
        }
    }

    /**
     * 玩家跨服回来处理
     *
     * @param player
     */
    private void crossbackDeal(Player player, int copyModleId) {
        player.playerCrossData.setToFightServer(false);
        player.playerCrossData.toFightId = 0;
        player.playerCrossData.toFightSid = 0;
        player.playerCrossData.toZoneModelId = 0;
        player.playerCrossData.crossState = CrossState.PCS_LOCAL;

        //不在线
        if (player.getIsOnline() < 1) {
            MapObject unknowMap = Manager.mapManager.getMap(player.gainMapId());
            if (unknowMap == null) {
                LOG.info("跨服返回，跨服休息室世界地图不见了");
            } else {
                unknowMap.removePlayer(player.getId());
            }
            LOG.info(player.getName() + "(" + player.getId() + ") 从副本 " + Manager.copyMapManager.getCopyMapName(copyModleId) + " 出来了，玩家已经离线了！");
            player.changeMapId(player.getOld().getMapId());
            player.changeMapModelId(player.getOld().getModelId());
            player.changeLine(player.getOld().getLine());
            player.changeCurPos(player.getOld().getPos());
        } else {
            Manager.playerManager.manager().onUpdatePkState(player, player.getPkState(), true);
            int camp = (int) player.getCamp();
            player.setCamp(camp, true);
            OnQuitUnknowMap(player, copyModleId);
            player.setLastFight(0);
            player.setInBattle(false);
            //退出让玩家脱战
            MapUtils.synPlayerFightState(player);
            //广播血量
            FightMessage.ResHpChange.Builder msg = FightMessage.ResHpChange.newBuilder();
            msg.setOwnId(player.getId());
            msg.setCurHp(player.getCurHp());
            MessageUtils.send_to_player(player, FightMessage.ResHpChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            Manager.buffManager.deal().online(player);
            Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.BASE);
            long currencyNum = Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.GoodEvil);
            Manager.currencyManager.manager().sendItemCoinChange(player, ItemChangeReason.PkDec, ItemCoinType.GoodEvil, currencyNum);
            Manager.titleManager.deal().sendTitleInfo(player);
            LOG.info(player.getName() + "(" + player.getId() + ") 从副本 " + Manager.copyMapManager.getCopyMapName(copyModleId) + " 出来了， 也发送了最新的血量！");
        }
    }

    //退出跨服休息室
    @Override
    public void OnQuitUnknowMap(Player player, int copyModleId) {
        MapObject unknowMap = Manager.mapManager.getMap(player.gainMapId());
        //玩家恢复满血
        if (player.getCurHp() != player.getAttribute().MaxHP()) {
            player.setCurHp(player.getAttribute().MaxHP());
//                player.onHpChange(null);
        }
        if (unknowMap == null) {
            LOG.info("跨服返回，跨服休息室世界地图不见了");
        } else {
            Manager.mapManager.manager().onQuitMap(unknowMap, player, false);
        }

        //如果是登录状态返回失败要退出， 则是通知玩家使用老的地图返回哦
        if (EntityState.LoginGame.compare(player.getState())) {
            Manager.mapManager.changeMap(player, Manager.playerManager.getBornMapID(), null, -1, true);
            return;
        }
        if (copyModleId == Manager.peakManager.deal().getZoneModelId()) {
            MapObject waitMap = Manager.peakManager.deal().getWaitMap();
            if (waitMap != null) {
                Manager.mapManager.changeMap(player, waitMap.getId(), null, false);
                return;
            }
        }
        //如果两个的地图不相同才设置状态
        Manager.mapManager.changeMap(player, Manager.playerManager.getBornMapID(), null, -1, false);
        // MapUtils.sendChangeMap(player, oldMap.getMapModelId(), oldMap.getLineId(), player.gainCurPos(), MapDefine.CHANGE_MAP_RESULT_SUCCESS, -1, -1);
        LOG.info(player.getName() + "(" + player.getId() + ") 从副本 跨服休息室  出来了， 也发送了最新的血量！pos=" + player.gainCurPos());

    }

    /**
     * 游戏服收到公共服发回的心跳反馈
     *
     * @param context
     * @param mess
     */
    @Override
    public void OnF2GResHeart(ChannelHandlerContext context, F2GResHeart mess) {
        int id = context.channel().attr(SessionAttribute.CONNECT_SERVER_ID).get();//, id);
        String ip = context.channel().attr(SessionAttribute.CONNECT_SERVER_IP).get();//, ip);
        int sport = context.channel().attr(SessionAttribute.CONNECT_SERVER_PORT).get();//, port);

        ConnectFightServer cfs = ConnectFightManager.GetInstance().getConList().get(id);
        if (cfs == null) {
            LOG.error("战斗服的原始类不存在了， 请查看情况!");
            return;
        }
        long now = System.currentTimeMillis();
        LOG.info(" id = " + id + " ip= " + ip + " port=" + sport + " 的战斗心跳成功！hert =" + (now - cfs.getLastheartTime()));
        //设置新的心跳时间
        cfs.setLastheartTime(now);
    }

    /**
     * 收到游戏服发来的心跳
     *
     * @param context
     * @param mess
     */
    @Override
    public void OnG2FReqHeart(ChannelHandlerContext context, G2FReqHeart mess) {
        //如果还没有注册， 则返回吧
        if (!context.channel().hasAttr(SessionAttribute.SERVER_ID)) {
            return;
        }

        int id = context.channel().attr(SessionAttribute.SERVER_ID).get();//, id);
        String ip = context.channel().attr(SessionAttribute.PLATFORMNAME).get();//, ip);
//        LOG.error(" id = " + id + " plat= " + ip + " 的收到游戏服来了！");
        F2GResHeart.Builder msg = F2GResHeart.newBuilder();
        FightClientManager.GetInstance().send_to_game(context, F2GResHeart.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 游戏服的心跳处理逻辑
     *
     * @param now
     */
    @Override
    public void OnCrossHeartTick(long now) {
        G2FReqHeart.Builder msg = G2FReqHeart.newBuilder();
        byte[] mess = msg.build().toByteArray();
        List<ChannelHandlerContext> sess = new ArrayList<>(ConnectFightManager.GetInstance().getFightConnects().values());
        for (ChannelHandlerContext session : sess) {
            ConnectFightManager.GetInstance().send_to_fight(session, -1, G2FReqHeart.MsgID.eMsgID_VALUE, mess);
        }

        //检查服务器的连接
        List<ConnectFightServer> fslist = new ArrayList<>(ConnectFightManager.GetInstance().getConList().values());
        for (ConnectFightServer cfs : fslist) {
            long offtime = now - cfs.getLastheartTime();
            if (offtime > 2 * 60 * 1000) {
                ChannelHandlerContext session = ConnectFightManager.GetInstance().getFightConnects().get(cfs.getFid());
                if (session != null) {
                    LOG.error(cfs.getFid() + "因心跳超时120秒，连接中断进行重联,超时:" + offtime);
                    SessionUtils.closeSession(session, cfs.getFid() + "因心跳超时120秒，连接中断进行重联");
                    ConnectFightManager.GetInstance().getFightConnects().remove(cfs.getFid());//清理连接
                }
                new Thread(cfs).start();
                //设置新的心跳时间
                cfs.setLastheartTime(System.currentTimeMillis() + 60 * 1000);
            }
        }
    }

    @Override
    public void OnP2GNoticeEvent(CrossServerMessage.P2GNoticeEvent mess) {

        int modelId = mess.getCloneModelId();

        switch (mess.getCmd()) {
            case "sendnotice": {
                SendNotice(modelId, mess.getPara());
            }
            break;
        }
    }

    private void SendNotice(int modelId, String param) {
        switch (modelId) {

        }
    }

    //向public服发送战场的状态变更
    @Override
    public void SendFightStateToPublic(long roomid, int state) {
        F2PFightRoomState.Builder msg = F2PFightRoomState.newBuilder();
        msg.setFightId(roomid);
        msg.setState(state);
        MessageUtils.send_to_public(F2PFightRoomState.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        //游戏服缓存
        FightClientManager.GetInstance().getRoomStateCache().put(roomid, state);
    }

    @Override
    public void OnF2GTaskAction(ChannelHandlerContext context, CrossServerMessage.F2GTaskAction messInfo) {
        Player player = Manager.playerManager.getPlayerCache(messInfo.getRoleId());
        if (player == null) {
            LOG.error("任务数据过来的时候， 玩家已经不存在了！");
            return;
        }

        Manager.taskManager.deal().action(player, (short) messInfo.getType(), messInfo.getModelId(), messInfo.getNum(), messInfo.getSign());
    }

    @Override
    public void OnP2GPlayerStateChange(ChannelHandlerContext context, CrossServerMessage.P2GPlayerStateChange mess) {
        for (long roleId : mess.getRoleIdsList()) {
            Player player = Manager.playerManager.getPlayerCache(roleId);

            if (player == null) {
                continue;
            }
            LOG.error(player + "设置玩家的跨服状态为:" + player.playerCrossData.crossState + " 变更为" + mess.getState());
            player.playerCrossData.crossState = mess.getState();
            player.playerCrossData.isReqFight = false;
        }
    }

    @Override
    public void OnG2FReqCrossDropItemString(ChannelHandlerContext context, CrossServerMessage.G2FReqCrossDropItemString mess) {
        long roleId = mess.getRoleId();
        int modelId = mess.getMonsterModelId();
        String sb = mess.getStrmess();

        LOG.error("收到游戏服回来的准备信息：" + sb);
        Player player = Manager.playerManager.getPlayerCache(roleId);
        if (player == null) {
            return;
        }

        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (map == null) {
            return;
        }

//        MessageUtils.notify_Map(map, Notify.CHAT, MessageString.BOSSKILL_NOTICE_INZONE, player.getName(), manager.copyMapManager.getCopyMapName( map.getZoneModelId()), manager.monsterManager.GetMonsterName( modelId), sb);
        MessageUtils.notify_Map(map, Notify.CHAT_SYS_MARQUEE, MessageString.BOSSKILL_NOTICE_INZONE, player.getName(), Manager.copyMapManager.getCopyMapName(map.getZoneModelId()), Manager.monsterManager.GetMonsterName(modelId), sb);
    }

    @Override
    public void OnF2GTaskFresh(ChannelHandlerContext context, CrossServerMessage.F2GTaskRresh messInfo) {
        Player player = Manager.playerManager.getPlayerCache(messInfo.getRoleId());
        if (player == null) {
            LOG.info("任务数据过来的时候， 玩家已经不存在了！messInfo.getRoleId() = " + messInfo.getRoleId());
            return;
        }
//        Manager.taskManager.deal().freshTask(player, Task.BATTLEFIELDTASK, TaskConst.FRESH_SYSTEM);
    }

    @Override
    public void OnF2GSendReward(ChannelHandlerContext context, CrossServerMessage.F2GSendReward mess) {

        Player player = Manager.playerManager.getPlayerCache(mess.getRoleId());
        if (player == null) {
            return;
        }
        long actionId = mess.getActionId();
        List<Item> rt = new ArrayList<>();
        for (dropItemInfo info : mess.getItemsList()) {
            List<Item> items = Item.createItems(info.getItemModelId(), info.getNum(), info.getIsBind(), 0);
            if (items.size() > 0) {
                rt.addAll(items);
            }
        }
        List<Item> result = new ArrayList<>();
        if (player.isOnline()) {
            if (!Manager.backpackManager.manager().addItems(player, rt, mess.getReason(), actionId)) {
                result.addAll(rt);
            }
        } else {
            result.addAll(rt);
        }
        if (result.size() > 0) {
            Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.System
                    , MessageString.BAG_FULL_MAIL_CONTENT, result, mess.getReason(), actionId);
        }
        if (mess.getReason() == ItemChangeReason.CrossFudBossOwnGain) {
            Manager.countManager.addVariant(player, VariantType.CrossFudBossKill, 1);
            Manager.controlManager.operate(player, FunctionVariable.Kill_crossfudi_Boss, 1);
        }
    }

    @Override
    public void onF2GSendDropData(ChannelHandlerContext context, F2GDropData mess) {
        long roleId = mess.getRoleId();
        Player player = Manager.playerManager.getPlayerCache(roleId);
        if (player == null) {
            LOG.error("跨服掉落玩家(" + roleId + ")不存在");
            return;
        }
        int type = mess.getType();
        player.getDailyActiveData().getDailyBuyCount().put(type, mess.getBuyCount());
        int progress = player.getDailyActiveData().getDailyProgress().getOrDefault(mess.getType(), 0);
        if (mess.getJoinDropCount() > progress) {
            int count = mess.getJoinDropCount() - progress;
            DailyActiveDefine daily = DailyActiveDefine.find(type);
            Manager.dailyActiveManager.deal().addDailyProgress(player, daily, count);
        }
    }

    @Override
    public void OnF2GSendMailReward(ChannelHandlerContext context, CrossServerMessage.F2GSendMailReward mess) {
        long roleId = mess.getRoleId();
        List<Item> rt = new ArrayList<>();
        for (dropItemInfo info : mess.getItemsList()) {
            boolean isBind = info.getIsBind();
            List<Item> items = Item.createItems(info.getItemModelId(), info.getNum(), isBind, 0);
            if (items.size() > 0) {
                rt.addAll(items);
            }
        }
        Manager.mailManager.sendMailTo(roleId, MailType.SysCommonRewardMail, mess.getSender()
                , mess.getTitle(), mess.getContent(), rt, 1, mess.getReason(), 0);
    }

    @Override
    public void onP2GSendMailReward(P2GSendMailReward mess) {
        long roleId = mess.getRoleId();
        List<Item> rt = new ArrayList<>();
        for (dropItemInfo info : mess.getItemsList()) {
            List<Item> items = Item.createItems(info.getItemModelId(), info.getNum(), info.getIsBind());
            rt.addAll(items);
        }
        Manager.mailManager.sendMailToPlayer(roleId, mess.getMailType(), mess.getSender()
                , mess.getTitle(), mess.getContent(), rt, mess.getReason());
    }

    /**
     * 资源找回变化的通知
     *
     * @param context
     * @param mess
     */
    @Override
    public void OnF2GResouceFindChange(ChannelHandlerContext context, CrossServerMessage.F2GResourceFindChange mess) {
        Player player = Manager.playerManager.getPlayerCache(mess.getRoleId());
        if (player == null) {
            LOG.error("任务数据过来的时候， 玩家已经不存在了！");
            return;
        }
        Manager.retrieveResManager.getScript().onF2GResourceFindChange(mess.getRoleId(), mess.getType());

//        Manager.welfareManager.deal().OnCompletRRC(player, RRType.getTypeByValue(mess.getType()));
    }

    /**
     * 师徒任务变化的通知
     *
     * @param context
     * @param mess
     */
    @Override
    public void onF2GShituTaskChange(ChannelHandlerContext context, CrossServerMessage.F2GShituTaskChange0 mess) {

        Player player = Manager.playerManager.getPlayerCache(mess.getRoleId());
        if (player == null) {
            LOG.error("任务数据过来的时候， 玩家已经不存在了！");
            return;
        }
    }

    /**
     * 处理跨服勇者巅峰奖励
     *
     * @param context
     * @param mess
     */
    @Override
    public void onF2GSendBravePeakReward(ChannelHandlerContext context, BravePeakMessage.F2GSendBravePeakReward mess) {
        final long roleId = mess.getRoleId();
        Player player = Manager.playerManager.getPlayerCache(roleId);
        if (player == null) {
            LOG.error("数据过来的时候， 玩家已经不存在了！");
            return;
        }
        //状态清除
        player.playerCrossData.setToFightServer(false);
        player.playerCrossData.toFightId = 0;
        player.playerCrossData.toFightSid = 0;
        player.playerCrossData.toZoneModelId = 0;
        player.playerCrossData.crossState = CrossState.PCS_LOCAL;

        final int copyMapId = mess.getCopyMapId();
        //发送奖励
        if (!mess.getIsGetReward())
            sendBravePeakReward(player, copyMapId);
        //模拟进入勇者巅峰的下一层
        if (mess.hasNeedToNext()) {
            if (copyMapId % BravePeakDefine.BRAVE_PEAK_COPY_ID_BASE != 9) {
                Manager.copyMapManager.manager().onReqCopyMapEnter(player, copyMapId + 1, 0);
            }
        }
    }

    /**
     * 发送奖励
     *
     * @param player
     * @param idConfigId
     */
    public void sendBravePeakReward(Player player, int idConfigId) {
        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(idConfigId);
        if (bean == null) {
            LOG.error(TaskHelp.getPlayerInfo(player) + "在发送勇者巅峰奖励时找不到配置：" + idConfigId);
            return;
        }
        List<Item> createItems = Item.createItems(bean.getSuccess_reward());
        Manager.currencyManager.manager().addEXP(player, addExtraExp(player, bean), ItemChangeReason.BraveGloryRewardGet, 0);

        int floor = idConfigId % BravePeakDefine.BRAVE_PEAK_COPY_ID_BASE;
        if (floor <= BravePeakDefine.BRAVE_PEAK_MAX_FLOOR)
            DailyActiveManager.bravePeakMapInfo.getPlayerFloor().put(player.getId(), floor);
        // 是否有足够的空间加入多个物品
        List<Item> spilthGoods = new ArrayList<>();
        for (Item item : createItems) {
            if (Manager.backpackManager.manager().onHasAddSpace(player, item)) {
                Manager.backpackManager.manager().addItem(player, item, ItemChangeReason.BraveGloryRewardGet, idConfigId);
            } else {
                spilthGoods.add(item);
            }
        }
        //无空间则发邮件
        if (spilthGoods.size() > 0) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail,
                    MessageString.System, MessageString.Copymap_MailTitle
                    , MessageString.Task_Content, spilthGoods, ItemChangeReason.BraveGloryRewardGet, idConfigId);
        }
    }

    /**
     * 应策划要求每层结束了加一些额外的经验
     *
     * @param player
     * @param clone_bean
     */
    private long addExtraExp(Player player, Cfg_Clone_map_Bean clone_bean) {
        Cfg_Characters_Bean bean = CfgManager.getCfg_Characters_Container().getValueByKey(player.getLevel());
        if (bean == null) {
            return 0;
        }
        long expNum = bean.getYZZD_EXP_award().get(clone_bean.getMaterialLevel()) == null ? 0 : bean.getYZZD_EXP_award().get(clone_bean.getMaterialLevel());
        return expNum;
    }

    @Override
    public void OnP2GConnectHeartRes(ChannelHandlerContext context, CrossServerMessage.P2GConnectHeartRes mess) {
        if (mess.getReason() == 0) {
            long lastTime = Manager.publicServerManager.getHeartTime();
            long now = System.currentTimeMillis();
            LOG.info("公共心跳原因码返回是:" + mess.getReason() + ", 用时=" + (now - lastTime));
            Manager.publicServerManager.setHeartTime(now);
        }
    }

    @Override
    public void OnP2GBossRefreshTip(ChannelHandlerContext context, P2GBossRefreshTip mess) {
        switch (mess.getType()) {
            case BossTypeConst.UNIVERSE_WAR_BOSS:
                Manager.universeManager.deal().sendCareMonsterRefreshTip(mess.getBossID());
                break;
        }

    }

    @Override
    public void OnG2FReqCloneFightInfoHandler(ChannelHandlerContext context, CrossServerMessage.G2FReqCloneFightInfo mess) {
        long roleId = mess.getRoleId();
        Player player = Manager.playerManager.getPlayerCache(roleId);
        if (null == player) {
            return;
        }

        MapObject mapObject = Manager.mapManager.getMap(player.gainMapId());
        if (null == mapObject) {
            return;
        }

        if (mapObject.getZoneModelId() != mess.getModelId()) {
            return;
        }
        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(mess.getModelId());
        if (bean == null) {
            LOG.error("Cfg_Clone_map_Bean配置表找不到：" + mess.getModelId());
            return;
        }
        Cfg_Mapsetting_Bean mapBean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(bean.getMapid());
        if (mapBean == null) {
            return;
        }
        IScript script = Manager.scriptManager.GetScriptClass(mapBean.getIsscript());
        if (script == null) {
            LOG.error("没有副本实现的脚本：" + bean.getId());
            return;
        }
        script.call(player, "onReqCloneFightInfo");
    }

    public void OnF2GAddExp(ChannelHandlerContext context, CrossServerMessage.F2GAddExp mess) {
        long roleId = mess.getRoleId();
        Player player = Manager.playerManager.getPlayerCache(roleId);
        if (null == player) {
            return;
        }
        Manager.currencyManager.manager().addEXP(player, mess.getExp(), mess.getReason(), IDConfigUtil.getLogId());
    }


    public void sendReward(Player player, List<Item> items, int reason) {
        CrossServerMessage.F2GSendReward.Builder dropMsg = CrossServerMessage.F2GSendReward.newBuilder();
        for (Item item : items) {
            CrossServerMessage.dropItemInfo.Builder info = CrossServerMessage.dropItemInfo.newBuilder();
            info.setItemModelId(item.getItemModelId());
            info.setNum(item.getNum());
            info.setIsBind(item.isBind());
            info.setNotice(false);
            dropMsg.addItems(info);
        }
        dropMsg.setRoleId(player.getId());
        dropMsg.setReason(reason);
        dropMsg.setActionId(IDConfigUtil.getId());
        FightClientManager.GetInstance().send_to_game(player.getIosession(), player.getId(), CrossServerMessage.F2GSendReward.MsgID.eMsgID_VALUE, dropMsg.build().toByteArray());
    }

    @Override
    public void sendMailReward(Player player, String sender, String title, String content, List<Item> items, int reason) {
        CrossServerMessage.F2GSendMailReward.Builder msg = CrossServerMessage.F2GSendMailReward.newBuilder();
        msg.setRoleId(player.getId());
        msg.setSender(sender);
        msg.setTitle(title);
        msg.setContent(content);
        msg.setReason(reason);
        for (Item item : items) {
            CrossServerMessage.dropItemInfo.Builder info = CrossServerMessage.dropItemInfo.newBuilder();
            info.setItemModelId(item.getItemModelId());
            info.setNum(item.getNum());
            info.setIsBind(item.isBind());
            info.setNotice(false);
            msg.addItems(info);
        }
        FightClientManager.GetInstance().send_to_game(player.getIosession(), player.getId(), CrossServerMessage.F2GSendMailReward.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    public void onG2FSynPlayerOut(G2FSynPlayerOut mess) {
        Player player = PlayerManager.getInstance().getPlayerOnline(mess.getRoleId());
        if (player != null) {
            MapObject map = Manager.mapManager.getMap(player.gainMapId());
            player.dealOffLine();
            if (map != null) {
                Manager.mapManager.manager().onQuitMap(map, player, false);
            }
        }
    }

    @Override
    public void onP2GDailyData(ChannelHandlerContext context, P2GDailyData mess) {
        int dailyId = mess.getDailyId();
        if (dailyId == DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR.getValue()) {
            Manager.rankListManager.getUniverseRankScript().updateUniverseStage(Integer.parseInt(mess.getData()));
        }
    }
    @Override
    public void onF2GSendPersonalNotice(ChannelHandlerContext context, CrossServerMessage.F2GSendPersonalNotice mess) {
        ChatMessage.PersonalNotice.Builder msg = ChatMessage.PersonalNotice.newBuilder();
        msg.setType(mess.getType());
        msg.addAllChatChannelList(mess.getChatChannelListList());
     //  msg.addAllChatChannelList(mess.getChatChannelListList());
        msg.setContent(mess.getContent());
        msg.addAllValue(mess.getValueList());
        //发送到本服 所有玩家
        MessageUtils.send_to_all_player(ChatMessage.PersonalNotice.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

}