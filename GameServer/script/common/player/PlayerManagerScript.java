
package common.player;

import com.data.*;
import com.data.bean.Cfg_Equip_Bean;
import com.data.bean.Cfg_Item_Bean;
import com.game.backpack.structs.Equip;
import com.game.backpack.structs.ItemCoinType;
import com.game.chat.structs.Notify;
import com.game.count.structs.VariantType;
import com.game.db.DBErrorToFile;
import com.game.db.bean.RoleLoginBean;
import com.game.db.bean.roleBean;
import com.game.db.dao.RoleLoginDao;
import com.game.equip.struct.EquipDefine;
import com.game.jjc.command.JJCChangeCareerCommand;
import com.game.manager.Manager;
import com.game.map.structs.MapDefine;
import com.game.map.structs.MapObject;
import com.game.newfashion.manager.NewFashionManager;
import com.game.player.log.LevelChangeLog;
import com.game.player.log.PlayerChangeCareerLog;
import com.game.player.script.IEnterGameFinish;
import com.game.player.script.IPlayerLoginFixBug;
import com.game.player.script.IPlayerManagerScript;
import com.game.player.structs.*;
import com.game.roleLog.RoleUpdateLogService;
import com.game.script.structs.ScriptEnum;
import com.game.server.DbSqlName;
import com.game.server.GameServer;
import com.game.server.script.IErrorReportScript;
import com.game.server.thread.SaveServer;
import com.game.structs.EntityState;
import com.game.structs.Fighter;
import com.game.structs.GlobalType;
import com.game.structs.ItemChangeAction;
import com.game.utils.MessageUtils;
import game.core.dblog.LogService;
import game.core.map.Position;
import game.core.net.Config.ServerConfig;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import game.core.util.VersionUpdateUtil;
import game.message.CrossServerMessage;
import game.message.CrossServerMessage.G2PPlayerCareerChange;
import game.message.HomeMessage;
import game.message.PlayerMessage;
import game.message.PlayerMessage.ResPlayerCareerChange;
import game.message.RegisterMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * @author admin
 */
public class PlayerManagerScript implements IPlayerManagerScript {
    private final Logger log = LogManager.getLogger("PlayerManager");

    private final int OffTimeToRemoveCache = 2 * 60 * 60 * 1000;//2小时未登录删除缓存数据
    private final int FightServerOffTimeToRemoveCache = 30 * 60 * 1000;//战斗服只维持30分钟就足未登录删除缓存数据

    private List<Long> repairRoleInfos = new ArrayList<>();//修复修仙宝剑 消费排行

    @Override
    public int getId() {
        return ScriptEnum.PlayerManagerBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    /**
     * 非主动动加载玩家
     *
     * @param roleId
     * @return
     */
    @Override
    public Player LoadPlayerFromDB(long roleId) {
        // 从数据库取数据
        roleBean role = Manager.registerManager.getDao().select(roleId);
        if (role == null) {
            log.error("登录选择角色失败，未找到该角色 roleId" + roleId);
            return null;
        }

        try {
            Player player = roleBeanToPlayer(role);
            long vipExp = Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.VipExp);
            player.setVipLv(Manager.vipManager.deal().getVipLvByExp(vipExp));
            Manager.goldManager.playerOnLine(player);
            return player;
        } catch (Exception e) {
            log.error(e, e);
            return null;
        }
    }

    /**
     * 玩家的数据保存
     *
     * @param player
     */
    @Override
    public void TickSavePlayer(Player player) {
        if (player == null)
            return;

        if (!player.isSaveToDB() || TimeUtils.Time() < player.getSaveToDBTime())
            return;

        SavePlayer(player);
    }

    /**
     * 定时保存玩家数据
     */
    @Override
    public void TickSavePlayer() {
        final long now = TimeUtils.Time();
        final boolean isFight = GameServer.getInstance().IsFightServer();
        final long offTime = isFight ? FightServerOffTimeToRemoveCache : OffTimeToRemoveCache;

        Iterator<Map.Entry<Long, Player>> iterator = Manager.playerManager.getPlayersCache().entrySet().iterator();
        while (iterator.hasNext()) {
            Player player = iterator.next().getValue();
            if (!checkOffPlayer(player, now, offTime)) {
                continue;
            }

            if (!isFight) SavePlayer(player);

            iterator.remove();
            //需要释放玩家
        }

        long cd = TimeUtils.Time() - now;
        if (cd > 1000) {
            log.error("遍历保存玩家数据所用时间过长：" + cd);
        }
    }

    /**
     * 立即保存玩家数据
     *
     * @param player
     * @return
     */
    @Override
    public boolean SavePlayer(Player player) {
        try {
            Manager.saveThreadManager.getSavePlayerThread().addRole(makeRoleBeanByPlayer(player));
        } catch (Exception e) {
            log.error(player.getInfo() + " 进行了一次保存的时候，玩家数据异常了！");
            return false;
        }

        player.setSaveToDB(false);
        syncPlayerWorldInfo(player, true);

        // 保存物品数据到表中
        RoleUpdateLogService.getInstance().updateRoleItemData(player.getId());

        // 保存玩家的最新数据到roleState
        RoleUpdateLogService.getInstance().updateRoleDate(player.getId());

        Manager.biManager.get4399Script().updatePlayer(player);
        return true;
    }

    /**
     * 保存玩家,不同步数据到世界服
     *
     * @param player
     * @param level
     */
    @Override
    public void SavePlayer(Player player, SavePlayerLevel level) {
        long nowTime = TimeUtils.Time();
        long nextTime = nowTime + level.getValue();
        // 如果比最近一次保存时间还小，则进行保存
        if (nowTime >= player.getSaveToDBTime() || nextTime <= player.getSaveToDBTime()) {
            if (!player.isSaveToDB()) {
                player.setSaveToDB(true);
                player.setSaveToDBTime(nextTime);
            }
        }

        syncPlayerWorldInfo(player, false);
    }

    @Override
    public void SaveAllPlayer() {
        List<Player> players = new ArrayList<>(Manager.playerManager.getPlayersCache().values());
        log.info("保存玩家开始，保存数量：" + players.size());
        int count = 0;
        int totalCount = 0;
        boolean isFightServer = GameServer.getInstance().IsFightServer();

        for (Map.Entry<Long, Player> entry : Manager.playerManager.getPlayersCache().entrySet()) {
            Player player = entry.getValue();
            if (isFightServer) {
                if (!player.isOnline()) continue;

                player.dealOffLine();
                MapObject map = Manager.mapManager.getMap(player.gainMapId());
                if (map == null) continue;
                Manager.crossServerManager.NoticeCrossCloneOutToPlayer(player, map);
                Manager.crossServerManager.sendToPublicPlayerOutCrossFight(player, map.getId(), map.getZoneModelId());
                continue;
            }
            totalCount += 1;
            // 玩家在线，走下线逻辑
            if (player.isOnline()) {
                try {
                    sendQuitGameInfo(player, QuitGameDefine.Normal);
                    Manager.playerManager.iQuitGame().QuitGame(player.getIosession(), QuitGameDefine.Normal, false, true);//踢玩家下线
                } catch (Exception e) {
                    log.error(e, e);
                }
            }

            //都是需要保存一次的
            try {
                if (SavePlayer(player)) count++;
                else DBErrorToFile.error("停服保存玩家数据出错，" + player.toString());

                if (count % 100 == 0) log.info("已经保存数量：" + count);

                if (player.playerCrossData.isReqFight) {
                    //向战斗服发送玩家离线
                    CrossServerMessage.G2FSynPlayerOut.Builder msg = CrossServerMessage.G2FSynPlayerOut.newBuilder();
                    msg.setRoleId(player.getId());
                    MessageUtils.send_to_public(CrossServerMessage.G2FSynPlayerOut.MsgID.eMsgID_VALUE, msg.build().toByteArray());
                }
            } catch (Exception e) {
                log.error(e, e);
            }
        }
        log.info("保存玩家数据成功, 保存的条数为：" + count + ", 总共玩家有:" + totalCount);
    }

    /**
     * 玩家登录完成同步数据
     *
     * @param player
     * @param isReConnect
     */
    @Override
    public void OnSendPlayerAllInfo(Player player, boolean isReConnect) {
        player.setSendTaskInfo(false);
        //功能开关系统
        Manager.controlManager.deal().login(player);
        //设置玩家元宝数据
        Manager.goldManager.playerOnLine(player);
        //处理玩家自身数据(放在前面执行，保证部分数据提前被初始化)
        Manager.dailyActiveManager.deal().checkDailyReset(player);
        //登录发送邮件信息(往前提一些，先发邮件再发其他系统)
        Manager.mailManager.loginLoadMail(player);
        //发送玩家累计登录天数
        sendResPlayerTodayData(player);

        //魂兽上线处理
        Manager.soulBeastManager.deal().online(player);
        //境界boss
        Manager.bossManager.stateBoss().doBossInfo(player);
        //个人世界boss
        Manager.bossManager.manager().loadPersonalWorldBoss(player);
        //化形上线初始化
        Manager.huaxinFlySwordManager.deal().onlineInit(player);
        //八级阵图
        Manager.eightDiagramsManager.deal().online(player);

        if (player.gainCurPos() == null || player.getCurGps().getPos().equals(Position.ZEROPOS)) {
            MapObject map = Manager.mapManager.getMap(player.gainMapId());
            if (map != null) {
                Position pos = map.getBrithPos();
                if (pos == null) {
                    log.error("地图没有出生点" + player);
                    return;
                }
                player.changeCurPos(pos);
            } else {
                player.changeCurPos(Position.ZEROPOS);
            }
        }
        //上线初始设置玩家未请求公会邀请信息
//        player.getGuild().setGotInviteList(false);
        Manager.playerManager.managerExt().synSelfPlayerInfo(player);

        //生成离线数据
        syncPlayerWorldInfo(player, false);

        //发送公会的数据
        if (!isReConnect) {
            Manager.guildsManager.playerOnLine(player);
        }

        Manager.guildBattleManager.manager().sendGuildBattleRed(player);

        //发送开服7天活动信息
//        Manager.newServerActivityManager.deal().online(player);

        //上线设置组队信息
        Manager.teamManager.playerOnLine(player);

        //灵体信息
        Manager.equipManager.deal().sendSpiritInfo(player);
        Manager.backpackManager.manager().sendItemInfos(player);
        Manager.currencyManager.manager().sendItemCoinInfos(player);
        Manager.storeManager.sendStoreItemInfos(player);

        //寻宝系统,在货币同步消息之后
        Manager.treasureHuntManager.deal().treasureHuntBaseInfo(player);
        /**
         * 发送装备部位信息
         * */
        Manager.equipManager.sendEquipPartInfoToClient(player);
        /**
         * 发送竞技场首次达到排名奖励的信息给客户端
         * */
        Manager.jjcManager.sendFirstRewardInfoToClient(player);

        //发送断线重联验证数据
        Manager.heartManager.sendReconnectSignInfo(player);
//        log.info(player + " 断线重联信息发送完毕！");

//        log.info(player + " 技能列表发送完毕！");

        //发送留言数据
        Manager.chatManager.deal().sendLevelMsg(player);
        Manager.chatManager.deal().sendWorldLevelMsg(player);

        //发送图鉴数据
        //Manager.cardManager.online(player);

        //宠物出征
        Manager.petManager.online(player);

        //坐骑
        Manager.horseManager.online(player);

        //发送排行榜红点提示信息
        Manager.rankListManager.deal().online(player);

        //计算buff,并同步buff
        Manager.buffManager.deal().online(player);
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.PlayerEnterGameFinishBaseScript);
        if (is instanceof IEnterGameFinish) {
            ((IEnterGameFinish) is).EnterGameFinish(player);
        } else {
            log.error("登录时没有找到登录完成的脚本实例！");
        }


        //登录
        Manager.commercializeManager.playerOnline(player);

        //登陆、处理福利模块数据
        Manager.welfareManager.playerOnline(player);

        // 资源找回
        Manager.retrieveResManager.playerOnline(player);

        //情缘活动回收
        Manager.marriageManager.activity().recoveryMarryActivityItem(player);

        //登录发送仙魄
        Manager.immortalSoulManager.online(player);

        //竞技场上线初始化奖励数据
        Manager.jjcManager.deal().Online(player);

        // 挚友
        Manager.chumManager.playerOnline(player);

        //圣装
        Manager.holyEquipManager.deal().onLine(player);

        // 世界支援
        Manager.worldHelpManager.playerOnline(player);

        //技能
        Manager.skillManager.deal().online(player);

        //任务数据检查并发送任务列表给客户端
        Manager.taskManager.deal().loginCheckTask(player);

        //任务目标信息
        Manager.taskManager.deal().sendResTargetInfo(player);

        // 神秘限购
        Manager.shopManager.limitShop().online(player);

        //vip
        Manager.vipManager.deal().online(player);

        //vip充值金额
        Manager.vipManager.deal().onlineVipRechage(player);

        //vip充值奖励
        Manager.vipManager.deal().onlineVipRechageRewardList(player);

        //vip宝珠信息
        Manager.vipManager.pearl().onlineVipPearlInfo(player);

        //自动熔炼
        Manager.recycleManager.deal().online(player);

        //仙甲上线
        Manager.immortalEquipManager.manager().onLineInitPart(player);

        //仙甲寻宝
        Manager.treasureHuntXianjiaManager.deal().onLineInit(player);

        //无忧宝库
        Manager.treasureHuntWuyouManager.getScript().online(player);

        //魂甲
        Manager.soulArmorManager.script().sendSoulArmorInfo(player);
        Manager.soulArmorManager.script().reqSoulArmorBag(player);

        //魔魂
        Manager.devilSeriesManager.getScript().online(player);

        //新时装
        Manager.newFashionManager.deal().onlineInit(player);

        //天墟战场怒气同步
        Manager.universeManager.deal().synAnger(player);

        // 神秘商店
        Manager.shopManager.mysteryShop().online(player);

        //0元购买
        Manager.shopManager.freeShop().onlineInit(player);

        Manager.marriageManager.manager().online(player);

        //藏宝阁
        Manager.cangbaogeManager.deal().playerOnline(player);

        //SDK评价
        Manager.platformevaluateManager.deal().playerOnline(player);

        //推送开服狂欢幸运翻牌数据
        Manager.openServerAcManager.deal().playerOnline(player);


        //天禁令
        Manager.fallingSkyManager.deal().online(player);

        //犒赏令
        Manager.kaoShangLingManager.deal().playerOnline(player);

        //初始化属性
        Manager.playerAttAttributeManager.deal().initPlayerAttribute(player, true);
        Manager.playerAttAttributeManager.deal().sendAttributeValueToPlayer(player);

        //情缘活动
        Manager.marriageManager.activity().online(player);


        //记录玩家的属性
        Manager.playerAttAttributeManager.deal().writeAttlog(player);


        //好友模块上线检测
        Manager.friendManager.deal().online(player);

        //新零元购
        Manager.shopManager.newFreeShop().onlineInit(player);

        //功能任务
        Manager.functionTaskManager.getScript().online(player);

        //仙侣对决
        Manager.couplefightManager.getScript().online(player);

        //幻装
        Manager.unrealEquipManager.deal().onLine(player);

        //v4助力上线推送数据
        Manager.v4HelpManager.deal().playerOnline(player);

        //同步社交服务器
        PlayerWorldInfo playerWorldInfo = Manager.playerManager.getPlayerWorldInfo(player.getId());
        PlayerMessage.G2SSynPlayerSocialInfo.Builder mPlayer = PlayerMessage.G2SSynPlayerSocialInfo.newBuilder();
        mPlayer.setGlobalPlayerWorldInfo(playerWorldInfo.toGlobalPlayerWorldInfo());
        mPlayer.setType(0);
        MessageUtils.send_to_social(PlayerMessage.G2SSynPlayerSocialInfo.MsgID.eMsgID_VALUE, mPlayer.build().toByteArray());

        //TODO 登录临时修复BUG
        IScript pLoginFixBug = Manager.scriptManager.GetScriptClass(ScriptEnum.PlayerLoginFixBugScript);
        if (pLoginFixBug instanceof IPlayerLoginFixBug) {
            ((IPlayerLoginFixBug) pLoginFixBug).fixBug(player);
        } else {
            log.error("登录时没有找到登录修复脚本实例！");
        }

        //bi 放在最下面
        Manager.biManager.playerOnline(player);
    }

    /**
     * 同步离线玩家的数据
     *
     * @param player
     * @param isSave
     */
    @Override
    public void syncPlayerWorldInfo(Player player, boolean isSave) {
        if (player.getGold() == null) {
            log.error(player.nameIdString() + "还没有初始化完！", new NullPointerException());
            return;
        }

//		if (player.getDeleteTime() != 0) {
//		    return;
//        }

        PlayerWorldInfo pwi = Manager.playerManager.getPlayerWorldInfo(player.getId());
        if (pwi == null) {
            pwi = new PlayerWorldInfo();
            pwi.setRoleid(player.getId());
            Manager.playerManager.getAllPlayerWorldInfo().put(player.getId(), pwi);
            isSave = true;
        }

        pwi.setStateVip(player.getStateVip().getLv());
        pwi.setPlayerVip(player.getVipLv());
        pwi.setShiHaiLevel(player.getShiHaiData().getCfgId());
        pwi.setLevel(player.getLevel());
        pwi.setCareer(player.getCareer());
        pwi.setCsid(player.getCreateServerId());
        pwi.setUserId(player.getUserId());
        pwi.setHorseId(player.getHorse().getHorseModelId());
        pwi.setWingId(player.getNewFashionData().getWingId());
        if (player.getFightPoint() > 0) {
            pwi.setFightPower(player.getFightPoint());
        }
        pwi.setGuildId(player.getGuildId());
        if (player.getNewFashionData().getWearDatas().containsKey(NewFashionManager.HEAD_TYPE)) {
            int headId = player.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_TYPE).getFashionID();
            pwi.setFashionHeadId(headId);
        }
        if (player.getNewFashionData().getWearDatas().containsKey(NewFashionManager.HEAD_FRAME_TYPE)) {
            int headFrameId = player.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_FRAME_TYPE).getFashionID();
            pwi.setFashionHeadFrameId(headFrameId);
        }
        pwi.setCustomHeadPath(player.getCustomHeadPath());
        pwi.setUseCustomHead(player.isUseCustomHead());

        pwi.setFashionBodyId(player.getNewFashionData().getBodyID());
        pwi.setFashionWeaponId(player.getNewFashionData().getWeaponID());//灵压法宝id
        pwi.setCreateTime(player.getCreateTime());
        pwi.setPlat(player.getPlatformName());
        pwi.setSpiritId(player.getSpiritData().getSpiritId());
        pwi.setSoulArmorId(player.getSoulArmor().getWearId());

        pwi.getIntdata().set(0, 0);
        pwi.getIntdata().set(1, player.getWing().getCurrentModelId());
        pwi.getIntdata().set(3, player.getStifleData().getNature().getCurrentModelId());
        pwi.setSex(player.getSex());
        int haloID = Manager.immortalEquipManager.manager().getImmFacadeForType(player, 32);
        int matrixID = Manager.immortalEquipManager.manager().getImmFacadeForType(player, 33);
        pwi.setFashionHalo(haloID);
        pwi.setFashionMatrix(matrixID);
        if (isSave) {
            Manager.saveThreadManager.getOtherServerSave().deal(pwi, DbSqlName.PLAYERWORLDINFO_INSERT, SaveServer.MERGE);
        }
        //TODO 同步社交服务器
        PlayerMessage.G2SSynPlayerSocialInfo.Builder mPlayer = PlayerMessage.G2SSynPlayerSocialInfo.newBuilder();
        mPlayer.setGlobalPlayerWorldInfo(pwi.toGlobalPlayerWorldInfo());
        mPlayer.setType(2);
        MessageUtils.send_to_social(PlayerMessage.G2SSynPlayerSocialInfo.MsgID.eMsgID_VALUE, mPlayer.build().toByteArray());

        HomeMessage.G2SUpdatePlayerInfo.Builder homeInfo = HomeMessage.G2SUpdatePlayerInfo.newBuilder();
        homeInfo.setPopularity(player.getHistoryCoin().get(ItemCoinType.Popularity));
        homeInfo.setExpRate(player.gainExpRate());
        homeInfo.setHomeLevel(player.getHome().getLevel());
        MessageUtils.send_to_social(player, HomeMessage.G2SUpdatePlayerInfo.MsgID.eMsgID_VALUE, homeInfo.build().toByteArray());
    }

    /**
     * remove player
     *
     * @param player
     */
    @Override
    public void removePlayer(Player player) {
        Manager.playerManager.getPlayersCache().remove(player.getId());
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.ErrorLogReportBaseScript);
        if (is instanceof IErrorReportScript) {
            ((IErrorReportScript) is).removeErrorLog(player.getId());
        } else {
            log.error("错误报告的实例没有找到！");
        }

        if (player.playerCrossData.toFightSid > 0) {
            log.info(player.nameIdString() + " 在跨服服务器ID：" + player.playerCrossData.toFightSid + "有活动=" + player.playerCrossData.toZoneModelId + ",发送退出协议！");
            Manager.playerManager.managerExt().onCrossPlayerOut(player);
        }
    }

    /**
     * remove player
     *
     * @param roleId
     */
    @Override
    public void removePlayer(long roleId) {

        Manager.playerManager.getAllPlayerWorldInfo().remove(roleId);

        Player player = Manager.playerManager.getPlayerCache(roleId);
        if (player == null)
            return;
        removePlayer(player);
    }

    /**
     * 生成数据库bean
     *
     * @param player
     * @return
     */
    @Override
    public roleBean makeRoleBeanByPlayer(Player player) {
        roleBean role = new roleBean();
        role.setCreateTime(player.getCreateTime());
        role.setPlatformName(player.getPlatformName());
        role.setRoledata(VersionUpdateUtil.dataSave(JsonUtils.toJSONString(player)));
        role.setRoleid(player.getId());
        role.setRolename(player.getName());
        role.setLv(player.getLevel());
        role.setServerId(player.getCreateServerId());
        role.setCareer(player.getCareer());
        role.setUserId(player.getUserId());
        Equip weapon = Manager.equipManager.getEquipByType(player, 1);
        role.setWeapon(null == weapon ? 0 : weapon.getItemModelId());
        role.setWingId(player.getWing().getCurrentId());
        role.setDeleteTime(player.getDeleteTime());
        role.setEquipMinStar(Manager.equipManager.getClothesStar(player));
        role.setLanguageType(player.getLanguageType());
        role.setFashionBodyId(player.getNewFashionData().getBodyID());
        role.setFashionWeaponId(player.getNewFashionData().getWeaponID());
        role.setLastLoginTime(player.getLastLoginTime());//最后一次登录的时间
        role.setUseIconState(player.getUseIconState());//自定义头像状态
        return role;
    }

    /**
     * 切换玩家的PK模式
     *
     * @param player   指定玩家
     * @param state    PK模式
     * @param isNotice 是否通知
     */
    @Override
    public void onUpdatePkState(Player player, int state, boolean isNotice) {
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (map == null) {
            return;
        }

//		if (player.playerCrossData.isToFightServer()) {
//			MessageUtils.notify_player(player, Notify.ERROR, MessageString.CanNotChangePkState);
//			return;
//		}

        player.setPkState(state);

        PlayerMessage.ResUpdataPkStateResult.Builder msg = PlayerMessage.ResUpdataPkStateResult.newBuilder();
        msg.setReason(0);
        msg.setCurPkState(player.getPkState());
        msg.setNotice(isNotice);
        MessageUtils.send_to_player(player, PlayerMessage.ResUpdataPkStateResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        if (state == PlayerDefine.PkStatePeace) {
            Fighter fighter = player.gainCurAttackTarget(map);
            if (fighter == null) {
                player.setCurAttackTargetId(0);
                if (player.getHatreds().isEmpty()) {
                    return;
                }
                fighter = player.getHatreds().get(0).getTarget();
                player.setCurAttackTargetId(fighter.getId());
            }

            boolean change = false;
            if (fighter instanceof Player) {
                change = true;
            }

            if (!change) {
                return;
            }

            player.clearHatred();
            player.setCurAttackTargetId(0);
        }
    }

    /**
     * 玩家等级变化
     *
     * @param player
     * @param oldLevel  升级之前等级
     * @param oldExp    升级之前经验
     * @param nowLevel  当前等级
     * @param nowExp    当前经验
     * @param changeExp 改变的经验
     * @param reason    升级原因
     * @param action
     */
    @Override
    public void playerLevelUp(Player player, int oldLevel, long oldExp, int nowLevel, long nowExp, long changeExp, int reason, long action) {
        Manager.playerManager.deal(ScriptEnum.PlayerLevelChangeBaseScript).levelUp(player, oldLevel);
        writeLevelUpLog(player, oldLevel, oldExp, changeExp, reason, action);
        Manager.biManager.get4399Script().levelupBiTo4399(player, oldLevel, oldExp, nowExp);

        //保存
        SavePlayer(player, SavePlayerLevel.HalfMinLater);
        player.setCurHp(player.getAttribute().MaxHP());//设置血量
        player.onHpChange(null);
        Manager.rankListManager.deal().setLevel(player, player.getLevel());
    }

    /**
     * 增加修改玩家职业的接口
     *
     * @param player
     * @param career
     */
    @Override
    public void onChangeCareer(Player player, int career) {
        if (player.getCareer() == career) {
            sendChangeCareer(player, career, 1, 0);//同职业不能转
            return;
        }

        if (career > 3) {
            sendChangeCareer(player, career, 1, 0);//同职业不能转
            return;
        }

        if (player.playerCrossData.isReqFight) {
            sendChangeCareer(player, career, 3, 0);//玩家正在进行跨服， 不能进入
            return;
        }

        if (player.playerCrossData.isToFightServer()) {
            sendChangeCareer(player, career, 3, 0);//玩家正在进行跨服， 不能进入
            return;
        }

        if (player.isInBattle()) {
            sendChangeCareer(player, career, 6, 0);//玩家正在进行跨服， 不能进入
            return;
        }

        if (player.getCurGenderTask() != null) {
            sendChangeCareer(player, career, 7, 0);//玩家正在进行跨服， 不能进入
            return;
        }

        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (map.getType() != MapDefine.WORLD_MAP) {
            sendChangeCareer(player, career, 8, 0);//玩家正在进行跨服， 不能进入
            return;
        }
        long actionId = IDConfigUtil.getLogId();

        Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(Global.JobChangeNeedItem.get(0));
        if (itemBean == null) {
            sendChangeCareer(player, career, 9, Global.JobChangeNeedItem.get(0));//玩家正在进行跨服， 不能进入
            return;
        }

        if (itemBean.getLevel() > player.getLevel()) {
            sendChangeCareer(player, career, 10, itemBean.getLevel());//玩家正在进行跨服， 不能进入
            return;
        }

        //扣除物品
        if (!Manager.backpackManager.manager().onRemoveItem(player, Global.JobChangeNeedItem.get(0), Global.JobChangeNeedItem.get(1), ItemChangeReason.ChangeJobDeleteEquipDec, actionId)) {
            sendChangeCareer(player, career, 9, Global.JobChangeNeedItem.get(0));//玩家正在进行跨服， 不能进入
            Manager.backpackManager.manager().sendItemNotEnough(player, Global.JobChangeNeedItem.get(0));
            return;
        }

        //检查玩家身上是否有装备
        for (int i = 0; i < EquipDefine.EquipPart_Num; ++i) {
            Equip equip = Manager.equipManager.getEquipByType(player, i);
            if (equip != null) {
                int modelId = equip.getItemModelId();
                Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(modelId);
                if (bean == null) {
                    sendChangeCareer(player, career, 4, modelId);//身上还有装备，请脱下
                    return;
//                    continue;
                }
                Manager.backpackManager.manager().writeItemLog(player, equip.getId(), equip.getItemModelId(), equip.getNum(),
                        0, ItemChangeReason.ChangeJobDeleteEquipDec, ItemChangeAction.REMOVE, actionId, 0, 0, equip.getGridId());
            }
        }
        int oldCareer = player.getCareer();
        player.setCareer((byte) career);
        player.setSex((byte) GlobalType.getSexByCareer(career));
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.BASE, PlayerAttributeType.Skill, PlayerAttributeType.EQUIP);

        //改变技能完成
        sendChangeCareer(player, career, 0, 0);
        //保存玩家的数据
        Manager.playerManager.savePlayer(player, SavePlayerLevel.RightNow);
        Manager.rankListManager.deal().syncRankPlayer(player);
        syncPlayerWorldInfo(player, true);
        //保存玩家的最新数据到rolestate
        RoleUpdateLogService.getInstance().updateRoleDate(player.getId());
        Manager.biManager.get4399Script().updatePlayer(player);

        //职业变更后更改登录服数据
        changeLoginCareer(player.getId(), career);

        G2PPlayerCareerChange.Builder msg = G2PPlayerCareerChange.newBuilder();
        msg.setCareerNo(career);
        msg.setRoleId(player.getId());
        MessageUtils.send_to_public(G2PPlayerCareerChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        //写日志
        PlayerChangeCareerLog pccl = new PlayerChangeCareerLog();
        pccl.setPlayer(player);
        pccl.setOldCareer(oldCareer);
        pccl.setNewCareer(career);
        pccl.setItemId(Global.JobChangeNeedItem.get(0));
        pccl.setActionId(actionId);
        LogService.getInstance().execute(pccl);
        //删除竞技场排名
        Manager.peakManager.addCommand(new JJCChangeCareerCommand(oldCareer, player));
    }

    @Override
    public void changeLoginName(long playerId, String name) {
        RoleLoginBean bean = new RoleLoginBean();
        bean.setRoleId(playerId);
        bean.setRoleName(name);

        RoleLoginDao dao = new RoleLoginDao();
        dao.updateName(bean);
    }

    @Override
    public void changeLoginLevel(long playerId, int level) {
        RoleLoginBean bean = new RoleLoginBean();
        bean.setRoleId(playerId);
        bean.setLv(level);

        RoleLoginDao dao = new RoleLoginDao();
        dao.updateLv(bean);
    }

    @Override
    public void changeLoginCareer(long playerId, int career) {
        RoleLoginBean bean = new RoleLoginBean();
        bean.setRoleId(playerId);
        bean.setCareer(career);

        RoleLoginDao dao = new RoleLoginDao();
        dao.updateCareer(bean);
    }

    @Override
    public void changeLoginDelete(long playerId, int deleteTime) {
        RoleLoginBean bean = new RoleLoginBean();
        bean.setRoleId(playerId);
        bean.setDeleteTime(deleteTime);

        RoleLoginDao dao = new RoleLoginDao();
        dao.updateDelete(bean);
    }

    @Override
    public void changeLoginFight(long playerId, long fight) {
        RoleLoginBean bean = new RoleLoginBean();
        bean.setRoleId(playerId);
        bean.setFight(fight);

        RoleLoginDao dao = new RoleLoginDao();
        dao.updateFight(bean);
    }

    @Override
    public void changeLoginUserId(long playerId, long userId) {
        RoleLoginBean bean = new RoleLoginBean();
        bean.setRoleId(playerId);
        bean.setUserId(userId);

        RoleLoginDao dao = new RoleLoginDao();
        dao.updateUserId(bean);
    }

    @Override
    public void insertLoginData(long roleId, long userId, int serverId, String roleName, int lv, int career, int deleteTime) {
        RoleLoginBean bean = new RoleLoginBean();
        bean.setRoleId(roleId);
        bean.setUserId(userId);
        bean.setServerId(serverId);
        bean.setRoleName(roleName);
        bean.setLv(lv);
        bean.setCareer(career);
        bean.setDeleteTime(deleteTime);

        RoleLoginDao dao = new RoleLoginDao();
        dao.insert(bean);
    }

    /**
     * TODO 玩家零点刷新
     *
     * @param player
     */
    @Override
    public void zeroClockPlayerDeal(Player player) {

        //跨零点的时候重新计算一下当前登录等级
        if ((int) Manager.countManager.getVariant(player, VariantType.Today_First_Login_Level) < 1) {
            Manager.countManager.addVariant(player, VariantType.Today_First_Login_Level, player.getLevel());
        }

        Manager.controlManager.operate(player, FunctionVariable.Sever_Open_Day, 1);

        // 0点检查重置数据后 检查登录天数成就检查
        long now = TimeUtils.Time();
        if (!TimeUtils.isSameDay(now, player.getLastLoginTime())) {
            player.setAccumOnlineDays(player.getAccumOnlineDays() + 1);
            player.setLastLoginTime(TimeUtils.Time());
            Manager.controlManager.operate(player, FunctionVariable.CumulativeLogin, 1);
            sendResPlayerTodayData(player);
        }

        try {
            Manager.commercializeManager.playerOnline(player);
            Manager.openServerAcManager.deal().checkTimeOver(player);

            // 跨天登记
            boolean canRetrieveRes = Manager.retrieveResManager.getScript().canRetrieveRes(player);
            Manager.biManager.getScript().biLogin(player, 3, canRetrieveRes ? 1 : 0);
            Manager.shopManager.freeShop().refresh(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendChangeCareer(Player player, int career, int state, int modelId) {

        int msgId = 0;
        switch (state) {
            case 1:
                msgId = MessageString.CHANGEJOBERROR1;
                break;
            case 2:
                msgId = MessageString.CHANGEJOBERROR2;
                break;
            case 3:
                msgId = MessageString.CHANGEJOBERROR3;
                break;
            case 4:
                msgId = MessageString.CHANGEJOBERROR4;
                break;
            case 5:
                msgId = MessageString.CHANGEJOBERROR5;
                break;
            case 6:
                msgId = MessageString.CHANGEJOBERROR6;
                break;
            case 7:
                msgId = MessageString.CHANGEJOBERROR7;
                break;
            case 8:
                msgId = MessageString.CHANGEJOBERROR8;
                break;
            case 10:
                msgId = MessageString.C_COPY_OPEN_NEEDLEVEL;
                break;
            default:
                break;
        }
        if (msgId > 0) {
            if (state == 10) {
                MessageUtils.notify_player(player, Notify.ERROR, msgId, String.valueOf(modelId));
            } else {
                MessageUtils.notify_player(player, Notify.ERROR, msgId, modelId > 0 ? Manager.backpackManager.manager().getName(modelId) : "");
            }
        }
        ResPlayerCareerChange.Builder msg = ResPlayerCareerChange.newBuilder();
        msg.setCareerNo(career);
        msg.setRoleId(player.getId());
        msg.setState(state);
//        if (state == 0) {
//            MessageUtils.send_to_roundPlayer(player, ResPlayerCareerChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
//        } else {
        MessageUtils.send_to_player(player, ResPlayerCareerChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
//        }
    }

    //发送退出游戏到客户端
    private void sendQuitGameInfo(Player player, int reason) {
        RegisterMessage.ResQuit.Builder msg = RegisterMessage.ResQuit.newBuilder();
        msg.setReason(reason);
        MessageUtils.send_to_player(player, RegisterMessage.ResQuit.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    //发送玩家累计登录天数
    private void sendResPlayerTodayData(Player player) {
        PlayerMessage.ResPlayerTodayData.Builder msg = PlayerMessage.ResPlayerTodayData.newBuilder();
        msg.setContinuousDays(0);
        msg.setAccumOnlineDays(player.getAccumOnlineDays());
//        log.info("======================ResPlayerTodayData:" + msg.build().getAccumOnlineDays());
        MessageUtils.send_to_player(player, PlayerMessage.ResPlayerTodayData.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private Player roleBeanToPlayer(roleBean role) throws Exception {
        Player player = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(role.getRoledata()), Player.class);
        if (ServerConfig.isTestServer()) {//机器人测试数据修正
            player.setId(role.getRoleid());
        }
        player.setName(role.getRolename());
        player.setUserId(role.getUserId());
        player.setPlatformName(role.getPlatformName());
        player.setCareer(role.getCareer());
        player.setCreateTime(role.getCreateTime());
        player.setCreateServerID(role.getServerId());
        player.setDeleteTime(role.getDeleteTime());
        player.setUseIconState(role.getUseIconState());
        player.setChildId(player.getChildId());
        return player;
    }

    //添加等级变化log
    private void writeLevelUpLog(Player player, int oldLevel, long oldExp, long changeExp, int reason, long action) {
        try {
            long exp = Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.EXP);
            LevelChangeLog levelUpLog = new LevelChangeLog();
            levelUpLog.setActionId(action);
            levelUpLog.setAfterExp(exp);
            levelUpLog.setBeforeExp(oldExp);
            levelUpLog.setChangeExp(changeExp);
            levelUpLog.setNowLevel(player.getLevel());
            levelUpLog.setOldLevel(oldLevel);
            levelUpLog.setReason(reason);
            levelUpLog.setRoleId(player.getId());
            levelUpLog.setSid(player.getCreateServerId());
            LogService.getInstance().execute(levelUpLog);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    private boolean checkOffPlayer(Player player, long now, long offTime) {
        if (EntityState.LoginGame.compare(player.getState())) {
            return false;
        }
        if (GameServer.getInstance().IsFightServer()) {
            if (player.isOnline()) {
                return false;
            }
            return now - player.getOffLineTime() >= offTime;
        } else {
            if (player.getIosession() != null) {
                return false;
            }
            if (player.isOnline()) {
                return false;
            }
        }
        return now - player.getOffLineTime() >= offTime;
    }

}