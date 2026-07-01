package common.fud;

import com.data.CfgManager;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.*;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.count.structs.CountVariant;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.db.dao.FudDao;
import com.game.fightroom.manager.FightManager;
import com.game.fightroom.structs.FightRoom;
import com.game.fightroom.structs.FightRoomState;
import com.game.gameserver.manager.GameServerManager;
import com.game.gameserver.structs.ServerInfo;
import com.game.guildcrossfud.script.IFudScript;
import com.game.guildcrossfud.struct.*;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.server.MainServer;
import com.game.servermatch.manager.ServerMatchManager;
import com.game.servermatch.structs.GameServerInfo;
import com.game.structs.ServerType;
import com.game.structs.SessionKey;
import com.game.utils.MessageUtils;
import com.game.utils.ServerParamUtil;
import com.game.utils.Utils;
import com.game.zone.structs.ZoneMapDefine;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import game.core.util.StringUtils;
import game.core.util.TimeUtils;
import game.message.CommonMessage;
import game.message.CrossFightMessage;
import game.message.GuildCrossFudMessage;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Desc TODO
 * @Date 2021/2/2 14:37
 * @Auth ZUncle
 */
public class FudScript implements IFudScript {

    final Logger logger = LogManager.getLogger(FudScript.class);

    final int RankLimit = 20;       //排名数据展示前20名
    final int CityRankLimit = 10;   //占领积分数量限制

    // 0=未占领 1=临时占领 2=已占领
    final int CityStateUnOwn = 0;
    final int CityStateTemp = 1;
    final int CityStateOwn = 2;

    FudDao fudDao = new FudDao();

    GuildCrossFudMessage.CrossFudBox.Builder mBox(int boxId, boolean isGet, boolean isLock) {
        GuildCrossFudMessage.CrossFudBox.Builder box = GuildCrossFudMessage.CrossFudBox.newBuilder();
        box.setBoxId(boxId);
        box.setIsGet(isGet);
        box.setIsLock(isLock);
        return box;
    }

    GuildCrossFudMessage.CrossFudCamp.Builder pack(FudCamp camp, int score) {
        GuildCrossFudMessage.CrossFudCamp.Builder mCamp = GuildCrossFudMessage.CrossFudCamp.newBuilder();
        mCamp.setCamp(camp.getCamp());
        mCamp.addAllServerId(camp.getServerList());
        mCamp.setScore(score);
        return mCamp;
    }

    GuildCrossFudMessage.CrossFudCity.Builder pack(FudRole role, FudGroup group, FudCity city) {

        FudCamp camp = group.getCamp().get(city.getCamp());
        List<FudBoss> bossList = Utils.find(city.getBoss().values(), boss -> boss.getHp() > 0);
        List<FudBoss> devilList = Utils.find(city.getDevilBoss().values(), boss -> boss.getHp() > 0);

        GuildCrossFudMessage.CrossFudBox.Builder box = mBox(city.getCityBean() == null ? 0 : city.getCityBean().getId(), city.getOpenBoxSign().contains(role.getRoleId()), false);

        GuildCrossFudMessage.CrossFudCity.Builder mCity = GuildCrossFudMessage.CrossFudCity.newBuilder();
        mCity.setCityId(city.getCityId());
        if (camp != null) {
            mCity.setCamp(pack(camp, 0));
        }
        mCity.setState(city.getState());
        mCity.setBox(box);
        mCity.setEnterRole(city.getEnterRole().getOrDefault(role.getServerId(), 0));
        mCity.setRemainBoss(bossList.size());
        mCity.setRemainDevilBoss(devilList.size());
        return mCity;
    }

    GuildCrossFudMessage.CrossFudBoss.Builder pack(FudCity city, FudBoss boss, FudRole role) {
        GuildCrossFudMessage.CrossFudBoss.Builder mBoss = GuildCrossFudMessage.CrossFudBoss.newBuilder();
        mBoss.setBossId(boss.getBossId());
        mBoss.setHp(boss.getHp());
        mBoss.setIsDie(boss.getHp() <= 0);
        if (boss.getRefreshTime() > 0) {
            long curTime = TimeUtils.Time();
            mBoss.setTime((int) (boss.getRefreshTime() - curTime));
        } else {
            mBoss.setTime(0);
        }
        HashSet<Integer> follow = role.getFollow().getOrDefault(city.getCityId(), new HashSet<>());
        mBoss.setCare(follow.contains(boss.getBossId()));
        return mBoss;
    }

    GuildCrossFudMessage.CrossFudRole.Builder mRole(FudRole role) {
        GuildCrossFudMessage.CrossFudRole.Builder mRole = GuildCrossFudMessage.CrossFudRole.newBuilder();
        mRole.setPlayerId(role.getRoleId());
        mRole.setName(role.getName());
        mRole.setScore(role.getScore());
        mRole.setKill(role.getKill());
        return mRole;
    }

    //初始化跨服玩家
    FudRole initFudRole(CommonMessage.CrossRole cr) {
        FudRole role = new FudRole();
        role.setPlatform(cr.getPlatform());
        role.setServerId(cr.getServerId());
        role.setRoleId(cr.getRoleId());
        role.setName(cr.getName());
        role.setLock(1);
        role.setTime(TimeUtils.Time());

        for (Cfg_Cross_fudi_boss_Bean bean : CfgManager.getCfg_Cross_fudi_boss_Container().getValuees()) {
            if (bean.getDefaultFollowOpen() == 0) {
                continue;
            }
            for (Cfg_Cross_fudi_main_Bean city : CfgManager.getCfg_Cross_fudi_main_Container().getValuees()) {
                if (bean.getPosition() != city.getPosition()) {
                    continue;
                }
                HashSet<Integer> follow = role.getFollow().getOrDefault(city.getId(), new HashSet<>());
                follow.add(bean.getId());
                role.getFollow().put(city.getId(), follow);
            }
        }
        Manager.fudManager.getFudRole().put(role.getRoleId(), role);
        return role;
    }

    /**
     * 请求跨服福地数据
     *
     * @param context
     * @param mess
     */
    @Override
    public void G2PAllCrossFudInfo(ChannelHandlerContext context, GuildCrossFudMessage.G2PAllCrossFudInfo mess) {
        String serverKey = context.channel().attr(SessionKey.SERVERPLATID).get();
        int groupID = ServerMatchManager.deal().getGroupIDForCurOpenDay(serverKey, DailyActiveDefine.CrossFud);

        CommonMessage.CrossRole cr = mess.getRole();
        FudGroup group = Manager.fudManager.getGroups().get(groupID);
        if (group == null) {
            logger.info("你所在服务器没有跨服福地参与资格！！！role={}", cr);
            return;
        }

        ServerInfo serverInfo = Manager.gameServerManager.getServerCache().get(serverKey);

        FudRole role = Manager.fudManager.getFudRole().get(cr.getRoleId());
        if (role == null) {
            role = initFudRole(cr);
        }
        role.setServerId(cr.getServerId());
        role.setName(cr.getName());

        GuildCrossFudMessage.ResAllCrossFudInfo.Builder message = GuildCrossFudMessage.ResAllCrossFudInfo.newBuilder();
        for (FudCity city : group.getCity().values()) {
            GuildCrossFudMessage.CrossFudCity.Builder mCity = pack(role, group, city);
            message.addCityList(mCity);
        }
        for (Cfg_Cross_fudi_score_reward_Bean bean : CfgManager.getCfg_Cross_fudi_score_reward_Container().getValuees()) {
            GuildCrossFudMessage.CrossFudBox.Builder box = mBox(bean.getId(), role.checkRewardState(bean.getId()), bean.getIf_pay() > 0 && role.getLock() == 1);
            message.addBoxList(box);
        }
        message.setScore(role.getScore());
        message.setTValue(role.gettValue());
        message.setWorldLevel(serverInfo.getServerWorldLv());

        MessageUtils.send_to_player(context, cr.getRoleId(), GuildCrossFudMessage.ResAllCrossFudInfo.MsgID.eMsgID_VALUE, message.build().toByteArray());

    }

    /**
     * 解锁积分宝箱
     *
     * @param context
     * @param mess
     */
    @Override
    public void G2PCrossFudUnLockScoreBox(ChannelHandlerContext context, GuildCrossFudMessage.G2PCrossFudUnLockScoreBox mess) {
        String serverKey = context.channel().attr(SessionKey.SERVERPLATID).get();
        int groupID = ServerMatchManager.deal().getGroupIDForCurOpenDay(serverKey, DailyActiveDefine.CrossFud);

        CommonMessage.CrossRole cr = mess.getRole();
        FudGroup group = Manager.fudManager.getGroups().get(groupID);
        if (group == null) {
            logger.info("你所在服务器没有跨服福地参与资格！！！role={}", cr);
            return;
        }

        FudRole role = Manager.fudManager.getFudRole().get(cr.getRoleId());
        if (role == null) {
            role = initFudRole(cr);
        }
        role.setServerId(cr.getServerId());

        if (role.getLock() == 0) {
            return;
        }
        role.setLock(0);

        List<Integer> unLock = new ArrayList<>();
        for (Cfg_Cross_fudi_score_reward_Bean bean : CfgManager.getCfg_Cross_fudi_score_reward_Container().getValuees()) {
            if (bean.getIf_pay() > 0) {
                unLock.add(bean.getId());
            }
        }
        sendScoreBoxStateUpdate(context, role, unLock.toArray(new Integer[unLock.size()]));

        GuildCrossFudMessage.P2GCrossFudBoxUnLock.Builder message = GuildCrossFudMessage.P2GCrossFudBoxUnLock.newBuilder();
        message.setBoxId(mess.getBoxId());
        message.setRoleId(cr.getRoleId());
        MessageUtils.send_to_game(context, GuildCrossFudMessage.P2GCrossFudBoxUnLock.MsgID.eMsgID_VALUE, message.build().toByteArray());
        logger.info("福地个人积分宝箱解锁成功 box={} role={}", mess.getBoxId(), cr);
    }

    /**
     * 更新宝箱状态
     *
     * @param context
     * @param role
     * @param boxIds
     */
    void sendScoreBoxStateUpdate(ChannelHandlerContext context, FudRole role, Integer... boxIds) {
        GuildCrossFudMessage.ResUpdateCrossFudScoreBox.Builder message = GuildCrossFudMessage.ResUpdateCrossFudScoreBox.newBuilder();
        for (int boxId : boxIds) {
            Cfg_Cross_fudi_score_reward_Bean bean = CfgManager.getCfg_Cross_fudi_score_reward_Container().getValueByKey(boxId);
            GuildCrossFudMessage.CrossFudBox.Builder box = mBox(bean.getId(), role.checkRewardState(bean.getId()), bean.getIf_pay() > 0 && role.getLock() == 1);
            message.addBoxList(box);
        }
        MessageUtils.send_to_player(context, role.getRoleId(), GuildCrossFudMessage.ResUpdateCrossFudScoreBox.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 领取积分宝箱奖励
     *
     * @param context
     * @param mess
     */
    @Override
    public void G2PCrossFudScoreBoxOpen(ChannelHandlerContext context, GuildCrossFudMessage.G2PCrossFudScoreBoxOpen mess) {
        String serverKey = context.channel().attr(SessionKey.SERVERPLATID).get();
        int groupID = ServerMatchManager.deal().getGroupIDForCurOpenDay(serverKey, DailyActiveDefine.CrossFud);

        CommonMessage.CrossRole cr = mess.getRole();
        FudGroup group = Manager.fudManager.getGroups().get(groupID);
        if (group == null) {
            logger.info("你所在服务器没有跨服福地参与资格！！！role={}", cr);
            return;
        }
        FudRole role = Manager.fudManager.getFudRole().get(cr.getRoleId());
        if (role == null) {
            role = initFudRole(cr);
        }
        role.setServerId(cr.getServerId());
        Cfg_Cross_fudi_score_reward_Bean bean = CfgManager.getCfg_Cross_fudi_score_reward_Container().getValueByKey(mess.getBoxId());
        if (bean == null || role.getScore() < bean.getNeed()) {
            return;
        }
        //奖励未解锁
        if (bean.getIf_pay() > 0 && role.getLock() > 0) {
            return;
        }
        role.signRewardState(bean.getId(), true);

        sendScoreBoxStateUpdate(context, role, bean.getId());

        GuildCrossFudMessage.P2GCrossFudScoreBoxOpen.Builder message = GuildCrossFudMessage.P2GCrossFudScoreBoxOpen.newBuilder();
        message.setBoxId(bean.getId());
        message.setRoleId(cr.getRoleId());
        MessageUtils.send_to_game(context, GuildCrossFudMessage.P2GCrossFudScoreBoxOpen.MsgID.eMsgID_VALUE, message.build().toByteArray());

        logger.info("福地个人积分宝箱开启 box={} role={}", mess.getBoxId(), cr);
    }

    /**
     * 领取福地宝箱奖励
     *
     * @param context
     * @param mess
     */
    @Override
    public void G2PCrossFudBoxOpen(ChannelHandlerContext context, GuildCrossFudMessage.G2PCrossFudBoxOpen mess) {
        String serverKey = context.channel().attr(SessionKey.SERVERPLATID).get();
        int groupID = ServerMatchManager.deal().getGroupIDForCurOpenDay(serverKey, DailyActiveDefine.CrossFud);

        CommonMessage.CrossRole cr = mess.getRole();
        FudGroup group = Manager.fudManager.getGroups().get(groupID);
        if (group == null) {
            logger.info("你所在服务器没有跨服福地参与资格！！！role={}", cr);
            return;
        }

        FudRole role = Manager.fudManager.getFudRole().get(cr.getRoleId());
        if (role == null) {
            role = initFudRole(cr);
        }
        role.setServerId(cr.getServerId());

        Integer campId = group.getServerCamp().get(role.getServerId());

        FudCity city = group.getCity().get(mess.getCityId());
        if (city == null) {
            return;
        }
        //是否占领
        if (city.getCamp() != campId || city.getState() != CityStateOwn) {
            GuildCrossFudMessage.ResUpdateCrossFudBox.Builder update = GuildCrossFudMessage.ResUpdateCrossFudBox.newBuilder();
            update.setCityId(city.getCityId());
            update.setBox(mBox(-1, false, false));
            MessageUtils.send_to_player(context, cr.getRoleId(), GuildCrossFudMessage.ResUpdateCrossFudBox.MsgID.eMsgID_VALUE, update.build().toByteArray());

            MessageUtils.notify_player(context, cr.getRoleId(), MessageString.REWARD_INVALID);
            return;
        }
        //领奖状态
        if (city.getOpenBoxSign().contains(role.getRoleId())) {
            return;
        }

        if (city.getCityBean() == null) {
            logger.error("福地占领奖励配置错误 city={} openDay={} worldLevel={}", city.getCityId(), group.getOpenDay(), group.getWorldLevel());
            return;
        }
        //根据玩家积分排名获取奖励
        ReadIntegerArrayEs reward = getReward(cr.getCareer(), city.getCityBean());
        int i = calcCityRewardIndex(city, role);
        ReadArray<Integer> array = reward.get(i);

        city.getOpenBoxSign().add(role.getRoleId());

        CommonMessage.ShowItemInfo.Builder mItem = CommonMessage.ShowItemInfo.newBuilder();
        mItem.setModelId(array.get(0));
        mItem.setCount(array.get(1));

        GuildCrossFudMessage.P2GCrossFudReward.Builder message = GuildCrossFudMessage.P2GCrossFudReward.newBuilder();
        message.setRoleId(cr.getRoleId());
        message.setReason(ItemChangeReason.CrossFudCityBoxGain);
        message.addRewardList(mItem);

        MessageUtils.send_to_game(context, GuildCrossFudMessage.P2GCrossFudReward.MsgID.eMsgID_VALUE, message.build().toByteArray());

        GuildCrossFudMessage.ResUpdateCrossFudBox.Builder update = GuildCrossFudMessage.ResUpdateCrossFudBox.newBuilder();
        update.setCityId(city.getCityId());
        update.setBox(mBox(city.getCityBean().getId(), true, false));
        MessageUtils.send_to_player(context, cr.getRoleId(), GuildCrossFudMessage.ResUpdateCrossFudBox.MsgID.eMsgID_VALUE, update.build().toByteArray());

        logger.info("领取福地占领奖励 box={} role={}", array, cr);
    }

    ReadIntegerArrayEs getReward(int career, Cfg_Cross_fudi_hold_reward_Bean bean) {
        switch (career) {
            case 0:
                return bean.getReward0();
            case 1:
                return bean.getReward1();
            case 2:
                return bean.getReward2();
            case 3:
                return bean.getReward3();
            default:
        }
        return null;
    }

    //根据玩家积分排名计算福地宝箱奖励索引
    int calcCityRewardIndex(FudCity city, FudRole role) {
        int rank = city.getRank().indexOf(role.getRoleId()) + 1;
        for (int i = 0; i < city.getCityBean().getRank().size(); i++) {
            ReadArray<Integer> array = city.getCityBean().getRank().get(i);
            if (rank >= array.get(0) && rank <= array.get(1)) {
                return i;
            }
        }
        return city.getCityBean().getRank().size() - 1;
    }

    //计算城市怪物
    void allocCityBoss(FudGroup group, FudCity city) {
        Cfg_Cross_fudi_main_Bean cityBean = CfgManager.getCfg_Cross_fudi_main_Container().getValueByKey(city.getCityId());
        for (Cfg_Cross_fudi_boss_Bean bean : CfgManager.getCfg_Cross_fudi_boss_Container().getValuees()) {
            if (cityBean.getPosition() != bean.getPosition()) {
                continue;
            }
            if (group.getOpenDay() >= bean.getDay().get(0)
                    && group.getOpenDay() <= bean.getDay().get(1)
                    && group.getWorldLevel() >= bean.getLevel().get(0)
                    && group.getWorldLevel() <= bean.getLevel().get(1)
            ) {
                FudBoss boss = new FudBoss();
                boss.setBossId(bean.getId());
                city.getBoss().put(boss.getBossId(), boss);
            }
        }
    }

    //计算福地奖励
    void allocCityReward(FudGroup group, FudCity city) {

        if (city.getCityBean() != null) {
            return;
        }
        Cfg_Cross_fudi_main_Bean cityBean = CfgManager.getCfg_Cross_fudi_main_Container().getValueByKey(city.getCityId());
        for (Cfg_Cross_fudi_hold_reward_Bean bean : CfgManager.getCfg_Cross_fudi_hold_reward_Container().getValuees()) {
            if (cityBean.getPosition() != bean.getPosition()) {
                continue;
            }
            if (group.getOpenDay() >= bean.getDay().get(0)
                    && group.getOpenDay() <= bean.getDay().get(1)
                    && group.getWorldLevel() >= bean.getLevel().get(0)
                    && group.getWorldLevel() <= bean.getLevel().get(1)
            ) {
                city.setCityBean(bean);
                break;
            }
        }
    }

    /**
     * 获取福地详情
     *
     * @param context
     * @param mess
     */
    @Override
    public void G2PCrossFudCityInfo(ChannelHandlerContext context, GuildCrossFudMessage.G2PCrossFudCityInfo mess) {
        String serverKey = context.channel().attr(SessionKey.SERVERPLATID).get();
        int groupID = ServerMatchManager.deal().getGroupIDForCurOpenDay(serverKey, DailyActiveDefine.CrossFud);

        CommonMessage.CrossRole cr = mess.getRole();
        FudGroup group = Manager.fudManager.getGroups().get(groupID);
        if (group == null) {
            logger.info("你所在服务器没有跨服福地参与资格！！！role={}", cr);
            return;
        }
        FudRole role = Manager.fudManager.getFudRole().get(cr.getRoleId());
        if (role == null) {
            role = initFudRole(cr);
        }
        role.setServerId(cr.getServerId());

        FudCity city = group.getCity().get(mess.getCity());
        if (city == null) {
            return;
        }

        GuildCrossFudMessage.ResCrossFudCityInfo.Builder message = GuildCrossFudMessage.ResCrossFudCityInfo.newBuilder();
        message.setCityId(mess.getCity());
        message.setType(mess.getType());
        message.setEnterRole(city.getEnterRole().getOrDefault(role.getServerId(), 0));

        for (Map.Entry<Integer, Integer> campScore : city.getCampScore().entrySet()) {
            FudCamp camp = group.getCamp().get(campScore.getKey());
            GuildCrossFudMessage.CrossFudCamp.Builder mCamp = pack(camp, campScore.getValue());
            message.addCampList(mCamp);
        }
        Collection<FudBoss> bossList = mess.getType() == 0 ? city.getBoss().values() : city.getDevilBoss().values();
        for (FudBoss boss : bossList) {
            message.addBossList(pack(city, boss, role));
        }

        MessageUtils.send_to_player(context, cr.getRoleId(), GuildCrossFudMessage.ResCrossFudCityInfo.MsgID.eMsgID_VALUE, message.build().toByteArray());
//        logger.info("获取福地boss列表信息 city={} role={}", city.getCityId(), cr);
    }

    /**
     * 获取福地积分排名
     *
     * @param context
     * @param mess
     */
    @Override
    public void G2PCrossFudRank(ChannelHandlerContext context, GuildCrossFudMessage.G2PCrossFudRank mess) {
        String serverKey = context.channel().attr(SessionKey.SERVERPLATID).get();
        int groupID = ServerMatchManager.deal().getGroupIDForCurOpenDay(serverKey, DailyActiveDefine.CrossFud);

        CommonMessage.CrossRole cr = mess.getRole();
        FudGroup group = Manager.fudManager.getGroups().get(groupID);
        if (group == null) {
            logger.info("你所在服务器没有跨服福地参与资格！！！role={}", cr);
            return;
        }

        FudRole role = Manager.fudManager.getFudRole().get(cr.getRoleId());
        if (role == null) {
            role = initFudRole(cr);
        }
        role.setServerId(cr.getServerId());
        //个人排名信息
        Integer campId = group.getServerCamp().get(role.getServerId());
        FudCamp camp = group.getCamp().get(campId);

        GuildCrossFudMessage.CrossFudRole.Builder self = mRole(role);
        self.setCamp(pack(camp, 0));

        GuildCrossFudMessage.ResCrossFudRankInfo.Builder message = GuildCrossFudMessage.ResCrossFudRankInfo.newBuilder();
        message.setType(mess.getType());
        message.setMy(self);
        List<FudRole> rankRole = mess.getType() == 0 ? group.getScoreRank() : group.getKillRank();
        int rank = 1;
        for (FudRole fr : rankRole) {
            campId = group.getServerCamp().get(fr.getServerId());
            if (campId == null) {
                continue;
            }
            camp = group.getCamp().get(campId);
            if (camp == null) {
                continue;
            }
            GuildCrossFudMessage.CrossFudRole.Builder mRole = mRole(fr);
            mRole.setRank(rank++);
            mRole.setCamp(pack(camp, 0));
            message.addRankList(mRole);
            if (fr.getRoleId() == role.getRoleId()) {
                message.setMy(mRole);
            }
        }

        MessageUtils.send_to_player(context, cr.getRoleId(), GuildCrossFudMessage.ResCrossFudRankInfo.MsgID.eMsgID_VALUE, message.build().toByteArray());
        logger.info("获取福地排名数据 type={} role={}", mess.getType(), cr);
    }

    /**
     * 关注boss
     *
     * @param context
     * @param mess
     */
    @Override
    public void G2PCrossFudCareBoss(ChannelHandlerContext context, GuildCrossFudMessage.G2PCrossFudCareBoss mess) {
        String serverKey = context.channel().attr(SessionKey.SERVERPLATID).get();
        int groupID = ServerMatchManager.deal().getGroupIDForCurOpenDay(serverKey, DailyActiveDefine.CrossFud);

        CommonMessage.CrossRole cr = mess.getRole();
        FudGroup group = Manager.fudManager.getGroups().get(groupID);
        if (group == null) {
            logger.info("你所在服务器没有跨服福地参与资格！！！role={}", cr);
            return;
        }

        FudRole role = Manager.fudManager.getFudRole().get(cr.getRoleId());
        if (role == null) {
            role = initFudRole(cr);
        }
        role.setServerId(cr.getServerId());

        FudCity city = group.getCity().get(mess.getCityId());
        if (city == null) {
            return;
        }
        FudBoss boss = city.getBoss().get(mess.getBossId());
        if (boss == null) {
            return;
        }
        HashSet<Integer> follow = role.getFollow().getOrDefault(city.getCityId(), new HashSet<>());
        if (mess.getType() == 1) {
            follow.add(boss.getBossId());
        } else {
            follow.remove(boss.getBossId());
        }
        role.getFollow().put(city.getCityId(), follow);

        GuildCrossFudMessage.ResCrossFudCareBoss.Builder message = GuildCrossFudMessage.ResCrossFudCareBoss.newBuilder();
        message.setCityId(city.getCityId());
        message.setBoss(pack(city, boss, role));

        MessageUtils.send_to_player(context, cr.getRoleId(), GuildCrossFudMessage.ResCrossFudCareBoss.MsgID.eMsgID_VALUE, message.build().toByteArray());
        logger.info("福地boss关注 type={} role={}", mess.getType(), cr);
    }

    /**
     * 加载跨服福地数据
     */
    @Override
    public void load() {

        //加载福地分组
        List<FudGroup> fudGroups = fudDao.selectFud();
        for (FudGroup g : fudGroups) {
            toClass(g);
            Manager.fudManager.getGroups().put(g.getGroupId(), g);
        }
        logger.info("加载福地分组数据 len={}", fudGroups.size());

        //加载玩家积分数据
        List<FudRole> fudRoles = fudDao.selectFudRole();
        for (FudRole role : fudRoles) {
            toClass(role);
            Manager.fudManager.getFudRole().put(role.getRoleId(), role);
        }
        logger.info("加载福地玩家数据 len={}", fudRoles.size());
        //初始化积分排名
        for (FudGroup g : fudGroups) {
            resetRank(g);
            for (FudCity city : g.getCity().values()) {
                //根据世界等级和开发天数分配福利 和 boss
                allocCityReward(g, city);
                //根据世界等级和开发天数分配福利 和 boss
                allocCityBoss(g, city);
            }
        }
    }

    /**
     * 关闭服务器
     */
    @Override
    public void close() {

        for (FudGroup g : Manager.fudManager.getGroups().values()) {
            toDB(g);
            fudDao.insertOrUpdateFud(g);
        }

        for (FudRole r : Manager.fudManager.getFudRole().values()) {
            toDB(r);
            fudDao.insertOrUpdateFudRole(r);
        }

        logger.info("保存跨服福地数据");
    }

    //分组排名
    void resetRank(FudGroup g) {
        List<Integer> serverList = new ArrayList<>();
        for (FudCamp camp : g.getCamp().values()) {
            serverList.addAll(camp.getServerList());
        }
        List<FudRole> fudRoles = Utils.find(Manager.fudManager.getFudRole().values(), role -> serverList.contains(role.getServerId()));
        if (fudRoles.isEmpty()) {
            return;
        }

        fudRoles.sort(Comparator.comparingInt(FudRole::getScore).thenComparingInt(FudRole::getKill).reversed());
        g.getScoreRank().clear();
        g.getScoreRank().addAll(fudRoles.subList(0, Math.min(RankLimit, fudRoles.size())));

        fudRoles.sort(Comparator.comparingInt(FudRole::getKill).thenComparingInt(FudRole::getScore).reversed());
        g.getKillRank().clear();
        g.getKillRank().addAll(fudRoles.subList(0, Math.min(RankLimit, fudRoles.size())));
        logger.info("福地积分击杀排名刷新！！！！");
    }

    //序列化
    void toDB(FudGroup group) {
        group.setData(JsonUtils.toJSONString(group));
    }

    //反序列化
    void toClass(FudGroup group) {
        FudGroup temp = JsonUtils.parseObject(group.getData(), FudGroup.class);
        group.setCity(temp.getCity());
        group.setCamp(temp.getCamp());
        group.setServerCamp(temp.getServerCamp());
        group.setWorldLevel(temp.getWorldLevel());
        group.setOpenDay(temp.getOpenDay());
    }

    //序列化
    void toDB(FudRole role) {
        role.setData(JsonUtils.toJSONString(role));
    }

    //反序列化
    void toClass(FudRole role) {
        if (StringUtils.isEmpty(role.getData())) {
            role.setData("{}");
        }
        FudRole temp = JsonUtils.parseObject(role.getData(), FudRole.class);
        role.setFollow(temp.getFollow());
    }

    void closeCityRoom() {
        for (FudGroup group : Manager.fudManager.getGroups().values()) {
            for (FudCity city : group.getCity().values()) {
                Cfg_Cross_fudi_main_Bean bean = CfgManager.getCfg_Cross_fudi_main_Container().getValueByKey(city.getCityId());
                if (bean.getPosition() == 0) {
                    continue;
                }
                FightRoom room = Manager.fightManager.getFrcache().get(city.getRoomId());
                if (room == null) {
                    continue;
                }
                ChannelHandlerContext socket = Manager.gameServerManager.GetSession(room.getPlat(), room.getServerId());
                //通知房间关闭
                GuildCrossFudMessage.P2GCrossFudProcess.Builder message = GuildCrossFudMessage.P2GCrossFudProcess.newBuilder();
                message.setRoomId(city.getRoomId());
                message.setType(1);
                MessageUtils.send_to_game(socket, GuildCrossFudMessage.P2GCrossFudProcess.MsgID.eMsgID_VALUE, message.build().toByteArray());
                logger.info("跨服福地通知刷新boss city={} ", city);
            }
        }
    }

    /**
     * 重置天禁值
     */
    void resetTV() {
        for (FudRole role : Manager.fudManager.getFudRole().values()) {
            //活动结束重置天禁值
            role.settValue(0);
            //logger.info("活动开始重置天禁值 player={}", role.getRoleId());
        }
        for (FudGroup group : Manager.fudManager.getGroups().values()) {
            for (FudCity city : group.getCity().values()) {
                Cfg_Cross_fudi_main_Bean bean = CfgManager.getCfg_Cross_fudi_main_Container().getValueByKey(city.getCityId());
                if (bean.getPosition() == 0) {
                    continue;
                }
                FightRoom room = Manager.fightManager.getFrcache().get(city.getRoomId());
                if (room == null) {
                    continue;
                }
                ChannelHandlerContext socket = Manager.gameServerManager.GetSession(room.getPlat(), room.getServerId());
                GuildCrossFudMessage.P2GCrossFudProcess.Builder message = GuildCrossFudMessage.P2GCrossFudProcess.newBuilder();
                message.setRoomId(city.getRoomId());
                message.setType(3);
                MessageUtils.send_to_game(socket, GuildCrossFudMessage.P2GCrossFudProcess.MsgID.eMsgID_VALUE, message.build().toByteArray());
            }
        }
    }

    /**
     * 踢出玩家
     */
    void tickOutCityRoom() {
        for (FudGroup group : Manager.fudManager.getGroups().values()) {
            for (FudCity city : group.getCity().values()) {
                Cfg_Cross_fudi_main_Bean bean = CfgManager.getCfg_Cross_fudi_main_Container().getValueByKey(city.getCityId());
                if (bean.getPosition() == 0) {
                    continue;
                }
                FightRoom room = Manager.fightManager.getFrcache().get(city.getRoomId());
                if (room == null) {
                    continue;
                }
                ChannelHandlerContext socket = Manager.gameServerManager.GetSession(room.getPlat(), room.getServerId());
                //通知房间关闭
                GuildCrossFudMessage.P2GCrossFudProcess.Builder message = GuildCrossFudMessage.P2GCrossFudProcess.newBuilder();
                message.setRoomId(city.getRoomId());
                message.setType(2);
                MessageUtils.send_to_game(socket, GuildCrossFudMessage.P2GCrossFudProcess.MsgID.eMsgID_VALUE, message.build().toByteArray());
                logger.info("跨服福地通知踢人 city={} ", city);
            }
        }
    }

    /**
     * 重新分配福地
     */
    @Override
    public void allocCity(boolean zeroMatchTick) {

        logger.info("跨服福地分配福地 !!!!Day={}", TimeUtils.format2string(TimeUtils.Time()));

        if (zeroMatchTick) {
//            tickOutCityRoom();
            resetTV();
        }

        //福地开放
        HashSet<Integer> stageConfig = new HashSet<>();
        for (Cfg_Cross_fudi_main_Bean bean : CfgManager.getCfg_Cross_fudi_main_Container().getValuees()) {
            stageConfig.add(bean.getCross_stage());
        }

        List<FudGroup> groups = new ArrayList<>();
        HashMap<Integer, HashMap<Integer, List<GameServerInfo>>> serverState = ServerMatchManager.deal().getAllReachStageServerInfo(DailyActiveDefine.CrossFud);
        for (Map.Entry<Integer, HashMap<Integer, List<GameServerInfo>>> g : serverState.entrySet()) {

            for (Map.Entry<Integer, List<GameServerInfo>> mg : g.getValue().entrySet()) {
                int stage = mg.getKey();
                if (stageConfig.contains(stage)) {
                    //分配福地组
                    groups.add(allocCamp(g.getKey(), stage, mg.getValue()));
                }
            }
        }

        //分配福地
        allocCity(groups);
        //0点特殊刷新
        if (zeroMatchTick) {
            //关闭旧房间
            closeCityRoom();
            //清空福地
            Manager.fudManager.getGroups().clear();
            fudDao.clearFud();
        } else {
            groups.removeIf(g -> Manager.fudManager.getGroups().containsKey(g.getGroupId()));
        }

        for (FudGroup group : groups) {
            logger.info("分配组成功 group={}", group);
            Manager.fudManager.getGroups().put(group.getGroupId(), group);
            //刷新排名
            resetRank(group);
            //分配福地房间
            for (FudCity city : group.getCity().values()) {
                allocRoom(group, city);
            }
        }
    }

    FightRoom createRoom(FudGroup fud, FudCity city, ServerInfo server, Cfg_Clone_map_Bean bean) {
        FightRoom room = new FightRoom();
        room.setpPlat(server.getPlatName());
        room.setServerId(server.getServerId());
        room.setServerGroupId(fud.getGroupId());
        room.setStageId(fud.getStage());
        room.setModelId(bean.getId());
        room.setCtime(TimeUtils.Time());
        room.setCrId(city.getCityId());
        room.setCname("跨服福地");
        room.setFid(IDConfigUtil.getLogId());
        room.setAllReadyStart(true);
        room.setType(bean.getType());
        room.setWaitTime(room.getCtime());
        room.setEndwait(room.getCtime() + bean.getEnter_time());
        room.setFightTime(0);
        room.setAttackValue(0);
        room.setMinP(ZoneMapDefine.GM_ZONE_MAP_MIN_NUM);
        room.setRstate(FightRoomState.CREATEROOM);
        FightManager.getInstance().SaveRoomInfo(room, server.getPlatName(), server.getServerId());//保存并且写log
        return room;
    }

    //分配福地房间服务器
    FightRoom allocRoom(FudGroup group, FudCity city) {
        Cfg_Cross_fudi_main_Bean bean = CfgManager.getCfg_Cross_fudi_main_Container().getValueByKey(city.getCityId());
        if (bean.getPosition() == 0) {
            return null;
        }
        if (city.getBoss().isEmpty()) {
//            logger.error("跨服福地boss size=0 group={} city={}", group, city);
            return null;
        }
        Cfg_Clone_map_Bean map_bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(bean.getClone_id());
        //开始分配战斗服务器
        ServerInfo serverInfo = Manager.fightManager.deal().getFightServerId(0);
        if (serverInfo == null) {
            List<ServerInfo> list = Manager.gameServerManager.GetType(ServerType.FIGHTSERVER);
            logger.error("没有战斗服连接在线， 请运维检查一下战斗服是否有！ 战斗服个数：" + list.size());
            return null;
        }

        FightRoom room = createRoom(group, city, serverInfo, map_bean);
        city.setRoomId(room.getFid());

        List<CommonMessage.CrossAttribute> args = new ArrayList<>();
        //0=组ID 1=城市ID 2=boss数据 3=服务器阵营
        CommonMessage.CrossAttribute.Builder info = CommonMessage.CrossAttribute.newBuilder();
        info.setType(0);
        info.setParam1(group.getGroupId());
        args.add(info.build());
        CommonMessage.CrossAttribute.Builder info1 = CommonMessage.CrossAttribute.newBuilder();
        info1.setType(1);
        info1.setParam1(city.getCityId());
        args.add(info1.build());
        for (FudBoss boss : city.getBoss().values()) {
            CommonMessage.CrossAttribute.Builder mBoss = CommonMessage.CrossAttribute.newBuilder();
            mBoss.setType(2);
            mBoss.setParam1(boss.getBossId());
            args.add(mBoss.build());
        }

        CrossFightMessage.P2FCreateCityMap.Builder msg = CrossFightMessage.P2FCreateCityMap.newBuilder();
        msg.setModelID(bean.getClone_id());
        msg.setRoomID(city.getRoomId());
        msg.addAllMapSetList(args);
        MessageUtils.send_to_game(serverInfo.getSession(), CrossFightMessage.P2FCreateCityMap.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        return room;
    }

    //分配福地
    void allocCity(List<FudGroup> groups) {
        for (FudGroup group : groups) {
            List<FudCity> birthCityList = new ArrayList<>();
            Cfg_Cross_fudi_main_Bean[] beans = CfgManager.getCfg_Cross_fudi_main_Container().getValuees();
            //初始化福地
            for (Cfg_Cross_fudi_main_Bean bean : beans) {
                if (bean.getCross_stage() == group.getStage()) {
                    FudCity city = new FudCity();
                    city.setState(CityStateUnOwn);
                    city.setCityId(bean.getId());
                    group.getCity().put(city.getCityId(), city);
                    if (bean.getPosition() == 0) {
                        birthCityList.add(city);
                    }
                    //根据世界等级和开发天数分配福利 和 boss
                    allocCityReward(group, city);
                    //根据世界等级和开发天数分配福利 和 boss
                    allocCityBoss(group, city);
                }
            }
            //关联福地
            for (Cfg_Cross_fudi_main_Bean bean : beans) {
                if (bean.getCross_stage() == group.getStage()) {
                    FudCity city = group.getCity().get(bean.getId());
                    for (int sonId : bean.getEnter_position().getValue()) {
                        if (city.getCityId() == sonId) {
                            continue;
                        }
                        FudCity son = group.getCity().get(sonId);
                        son.getRootNode().add(city.getCityId());
                    }
                }
            }
            List<FudCamp> campList = new ArrayList<>(group.getCamp().values());
            //分配出生地福地
            for (int i = 0; i < campList.size(); i++) {
                FudCamp camp = campList.get(i);
                FudCity city = birthCityList.get(i);
                city.setState(CityStateOwn);
                city.setCamp(camp.getCamp());
                logger.info("分配出生福地 group={} camp={} city={}", group, camp, city);
            }
        }
    }

    //分配阵营
    FudGroup allocCamp(int groupId, int stage, List<GameServerInfo> servers) {
        FudGroup group = new FudGroup();
        group.setGroupId(groupId);
        group.setStage(stage);
        float totalDay = 0;
        float totalLevel = 0;
        List<GameServerInfo> reach = new ArrayList<>();
        for (int c = 0; c < servers.size(); c++) {
            GameServerInfo gameServerInfo = servers.get(c);
            FudCamp camp = new FudCamp();
            camp.setCamp(c + 1);
            camp.getServerList().add(gameServerInfo.getServerId());

            group.getCamp().put(camp.getCamp(), camp);
            group.getServerCamp().put(gameServerInfo.getServerId(), camp.getCamp());

            int tempId = ServerMatchManager.deal().getGroupIDForCurOpenDay(gameServerInfo.serverKey(), DailyActiveDefine.CrossFud);
            if (tempId != groupId) {
                continue;
            }
            reach.add(gameServerInfo);

            int openDay = GameServerManager.getOpenServerDay(gameServerInfo.getOpenTime());
            totalDay += openDay;

            totalLevel += gameServerInfo.getServerWorldLv();

        }
        if (reach.size() > 0) {
            group.setOpenDay((int) Math.ceil(totalDay / reach.size()));
            group.setWorldLevel((int) Math.ceil(totalLevel / reach.size()));
        }
        return group;
    }

    /**
     * 活动开启
     */
    @Override
    public void activeBegin() {
        logger.info("跨服福地活动开启");
        //活动开始分配福地
        allocCity(false);
        //活动开始刷新一轮boss
        for (FudGroup group : Manager.fudManager.getGroups().values()) {
            for (FudCity city : group.getCity().values()) {
                Cfg_Cross_fudi_main_Bean bean = CfgManager.getCfg_Cross_fudi_main_Container().getValueByKey(city.getCityId());
                if (bean.getPosition() == 0) {
                    continue;
                }
                city.setNextRefreshTime(TimeUtils.Time());
                refreshCityBoss(group, city);
            }
        }
        MainServer.getInstance().addTimerEvent(Manager.fudManager.getTimer());
    }

    /**
     * 活动关闭
     */
    @Override
    public void activeEnd() {
        MainServer.getInstance().removeTimerEvent(Manager.fudManager.getTimer());

        tickOutCityRoom();

        logger.info("跨服福地活动关闭");
    }

    /**
     * 计算下一次刷新时间
     *
     * @param city
     * @param bean
     * @param nowMin
     */
    void calcRefreshTime(FudCity city, Cfg_Cross_fudi_main_Bean bean, int nowMin) {
        if (city.getNextRefreshTime() != 0) {
            return;
        }
        for (int refresh : bean.getRefresh_time().getValue()) {
            if (refresh >= nowMin) {
                city.setNextRefreshTime(TimeUtils.getTodayBeginTime() + refresh * 60 * 1000);
                return;
            }
        }
    }

    /**
     * 福地心跳
     */
    @Override
    public void tick() {
        //检测怪物刷新
        long nowTime = TimeUtils.Time();
        int nowMin = TimeUtils.getDayOfHour(nowTime) * 60 + TimeUtils.getDayOfMin(nowTime);

        for (FudGroup group : Manager.fudManager.getGroups().values()) {
            for (FudCity city : group.getCity().values()) {
                Cfg_Cross_fudi_main_Bean bean = CfgManager.getCfg_Cross_fudi_main_Container().getValueByKey(city.getCityId());
                if (bean.getPosition() == 0) {
                    continue;
                }
                calcRefreshTime(city, bean, nowMin);

                if (nowTime >= city.getNextRefreshTime() && city.getNextRefreshTime() != 0) {
                    refreshCityBoss(group, city);
                }
            }
        }
        if (Manager.countManager.getVariant(() -> ServerParamUtil.counts, CountVariant.CrossFudScore) == 0) {
            Manager.countManager.setVariant(() -> ServerParamUtil.counts, CountVariant.CrossFudScore, 1);
            ServerParamUtil.saveCounts();
            //周刷新排名
            for (FudRole role : Manager.fudManager.getFudRole().values()) {
                role.setKill(0);
                role.setScore(0);
                role.setScoreReward(0);
            }
            logger.info("刷新玩家福地积分！！！！！");
        }
    }

    /**
     * 刷新福地boss
     *
     * @param group
     * @param city
     */
    @Override
    public void refreshCityBoss(FudGroup group, FudCity city) {
        FightRoom room = Manager.fightManager.getFrcache().get(city.getRoomId());
        if (room == null) {
            room = allocRoom(group, city);
        }
        if (room == null) {
            return;
        }
        //boss刷新,重置占领状态->临时占领
        city.setNextRefreshTime(0);
        city.getRank().clear();
        city.getKill().clear();
        city.getScore().clear();
        city.getEnterRole().clear();
        city.getCampScore().clear();
        city.getOpenBoxSign().clear();
        if (city.getCamp() > 0) {
            city.setState(CityStateTemp);
        }
        ChannelHandlerContext socket = Manager.gameServerManager.GetSession(room.getPlat(), room.getServerId());
        //通知房间刷新boss
        GuildCrossFudMessage.P2GCrossFudProcess.Builder message = GuildCrossFudMessage.P2GCrossFudProcess.newBuilder();
        message.setRoomId(city.getRoomId());
        message.setType(0);
        MessageUtils.send_to_game(socket, GuildCrossFudMessage.P2GCrossFudProcess.MsgID.eMsgID_VALUE, message.build().toByteArray());
        // logger.info("跨服福地通知刷新boss city={} ", city);
    }

    /**
     * 获取魔王缝隙怪物列表
     *
     * @param context
     * @param mess
     */
    @Override
    public void G2PDevilBossList(ChannelHandlerContext context, GuildCrossFudMessage.G2PDevilBossList mess) {

        String serverKey = context.channel().attr(SessionKey.SERVERPLATID).get();
        int groupID = ServerMatchManager.deal().getGroupIDForCurOpenDay(serverKey, DailyActiveDefine.CrossFud);

        CommonMessage.CrossRole cr = mess.getRole();
        long dailyTime = Manager.dailyActiveManager.deal().getDailyNearlyEndTime(DailyActiveDefine.CrossFud);
        if (dailyTime == 0) {
            MessageUtils.notify_player(context, cr.getRoleId(), MessageString.DAILY_NOT_OPEN);
            return;
        }
        FudGroup group = Manager.fudManager.getGroups().get(groupID);
        if (group == null) {
            logger.info("你所在服务器没有跨服福地参与资格！！！role={}", cr);
            return;
        }

        FudRole role = Manager.fudManager.getFudRole().get(cr.getRoleId());
        if (role == null) {
            role = initFudRole(cr);
        }
        role.setServerId(cr.getServerId());


        GuildCrossFudMessage.ResDevilBossList.Builder message = GuildCrossFudMessage.ResDevilBossList.newBuilder();
        for (FudCity city : group.getCity().values()) {
            for (FudBoss boss : city.getDevilBoss().values()) {
                message.addBossId(boss.getBossId());
            }
        }
        MessageUtils.send_to_player(context, cr.getRoleId(), GuildCrossFudMessage.ResDevilBossList.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 通知公共服检测房间
     *
     * @param context
     */
    @Override
    public void G2PSyncRoomInfo(ChannelHandlerContext context) {

        for (FudGroup group : Manager.fudManager.getGroups().values()) {
            for (FudCity city : group.getCity().values()) {
                Cfg_Cross_fudi_main_Bean bean = CfgManager.getCfg_Cross_fudi_main_Container().getValueByKey(city.getCityId());
                if (bean.getPosition() == 0) {
                    continue;
                }
                FightRoom room = Manager.fightManager.getFrcache().get(city.getRoomId());
                if (room == null) {
                    allocRoom(group, city);
                } else {
                    ChannelHandlerContext socket = Manager.gameServerManager.GetSession(room.getPlat(), room.getServerId());
                    if (!context.equals(socket)) {
                        continue;
                    }
                    Manager.fightManager.getFrcache().remove(city.getRoomId());

                    //战斗服务器重连, 重新刷新boss
                    city.setRoomId(0);
                    city.setNextRefreshTime(TimeUtils.Time());
                    refreshCityBoss(group, city);
                }
            }
        }
    }

    /**
     * 福地boss击杀
     *
     * @param context
     * @param mess
     */
    @Override
    public void F2PKillFudBoss(ChannelHandlerContext context, GuildCrossFudMessage.F2PKillFudBoss mess) {
        FudGroup group = Manager.fudManager.getGroups().get(mess.getGroupId());
        if (group == null) {
            logger.info("福地数据更新失败 group={} city={}", mess.getGroupId(), mess.getCityId());
            return;
        }
        FudCity city = group.getCity().get(mess.getCityId());
        if (city == null) {
            logger.info("福地数据更新失败 group={} city={} roomId={}", mess.getGroupId(), mess.getCityId(), mess.getRoomId());
            return;
        }
        if (city.getRoomId() != mess.getRoomId()) {
            return;
        }
        Cfg_Cross_fudi_boss_Bean bean = CfgManager.getCfg_Cross_fudi_boss_Container().getValueByKey(mess.getBoss());

        for (long roleId : mess.getKillerList()) {
            FudRole role = Manager.fudManager.getFudRole().get(roleId);
            if (role == null) {
                continue;
            }
            role.settValue(role.gettValue() + bean.getRage());

            GuildCrossFudMessage.ResUpdateTJValue.Builder ms = GuildCrossFudMessage.ResUpdateTJValue.newBuilder();
            ms.setTValue(role.gettValue());
            MessageUtils.send_to_player(role.getPlatform(), role.getServerId(), role.getRoleId(), GuildCrossFudMessage.ResUpdateTJValue.MsgID.eMsgID_VALUE, ms.build().toByteArray());
        }
    }

    /**
     * 战斗服 同步福地数据到公共服
     *
     * @param context
     * @param mess
     */
    @Override
    public void F2PCrossFudInfo(ChannelHandlerContext context, GuildCrossFudMessage.F2PCrossFudInfo mess) {

        FudGroup group = Manager.fudManager.getGroups().get(mess.getGroupId());
        if (group == null) {
            logger.info("福地数据更新失败 group={} city={}", mess.getGroupId(), mess.getCityId());
            return;
        }
        FudCity city = group.getCity().get(mess.getCityId());
        if (city == null) {
            logger.info("福地数据更新失败 group={} city={} roomId={}", mess.getGroupId(), mess.getCityId(), mess.getRoomId());
            return;
        }

        if (city.getDevilRoomId() == mess.getRoomId()) {
            Manager.fudManager.devil().F2PCrossFudInfo(context, mess);
            return;
        }

        if (city.getRoomId() != mess.getRoomId()) {
            return;
        }
        List<Integer> needNotifyFollow = new ArrayList<>();
        //更新boss
        for (GuildCrossFudMessage.CrossFudBoss mBoss : mess.getBossList()) {
            FudBoss boss = city.getBoss().get(mBoss.getBossId());
            if (boss == null) {
                continue;
            }
            if (boss.getHp() <= 0 && mBoss.getHp() > 0) {
                needNotifyFollow.add(boss.getBossId());
            }
            boss.setHp(mBoss.getHp());
        }
        boolean needResetRank = false;
        HashMap<Integer, Integer> serverRole = new HashMap<>();
        //更新玩家积分
        for (GuildCrossFudMessage.CrossFudRole mRole : mess.getRoleList()) {
            FudRole role = Manager.fudManager.getFudRole().get(mRole.getPlayerId());
            int oldScore = city.getScore().getOrDefault(mRole.getPlayerId(), 0);
            if (mRole.getScore() > oldScore) {
                int add = mRole.getScore() - oldScore;
                role.setScore(role.getScore() + add);
                needResetRank = true;
            }
            int oldKill = city.getKill().getOrDefault(mRole.getPlayerId(), 0);
            if (mRole.getKill() > oldKill) {
                int add = mRole.getKill() - oldKill;
                role.setKill(role.getKill() + add);
                needResetRank = true;
            }
            city.getScore().put(mRole.getPlayerId(), mRole.getScore());
            city.getKill().put(mRole.getPlayerId(), mRole.getKill());

            if (mRole.getInFud()) {
                int old = serverRole.getOrDefault(role.getServerId(), 0);
                serverRole.put(role.getServerId(), old + 1);
            }
        }
        city.setEnterRole(serverRole);
        //更新阵营积分
        for (GuildCrossFudMessage.CrossFudCamp camp : mess.getCampList()) {
            city.getCampScore().put(camp.getCamp(), camp.getScore());
        }
        if (needResetRank) {
            resetRank(group);
        }

        //BOSS 关注广播
        GuildCrossFudMessage.ResCrossFudCareBossRefresh.Builder notify = GuildCrossFudMessage.ResCrossFudCareBossRefresh.newBuilder();
        notify.setCityId(city.getCityId());
        for (int bossId : needNotifyFollow) {
            notify.setBossId(bossId);
            for (FudRole role : Manager.fudManager.getFudRole().values()) {
                HashSet<Integer> follow = role.getFollow().getOrDefault(city.getCityId(), new HashSet<>());
                if (!follow.contains(bossId)) {
                    continue;
                }
                int camp = group.getServerCamp().get(role.getServerId());
                //检测福地是否可进入
                if (!checkRootCity(new HashSet<>(), group, city, camp)) {
                    continue;
                }
                MessageUtils.send_to_player(role.getPlatform(), role.getServerId(), role.getRoleId(), GuildCrossFudMessage.ResCrossFudCareBossRefresh.MsgID.eMsgID_VALUE, notify.build().toByteArray());
            }
        }
    }

    /**
     * 福地占领通知
     *
     * @param context
     * @param mess
     */
    @Override
    public void F2PCrossFudGain(ChannelHandlerContext context, GuildCrossFudMessage.F2PCrossFudGain mess) {
        FudGroup group = Manager.fudManager.getGroups().get(mess.getGroupId());
        if (group == null) {
            logger.info("占领福地失败 group={} city={}", mess.getGroupId(), mess.getCityId());
            return;
        }
        FudCity city = group.getCity().get(mess.getCityId());
        if (city == null || city.getRoomId() != mess.getRoomId()) {
            logger.info("占领福地失败 group={} city={} roomId={}", mess.getGroupId(), mess.getCityId(), mess.getRoomId());
            return;
        }
        FudCamp camp = group.getCamp().get(mess.getCamp().getCamp());
        if (camp == null) {
            logger.info("占领福地失败 group={} city={} camp={}", mess.getGroupId(), mess.getCityId(), mess.getCamp());
            return;
        }
        city.setCamp(camp.getCamp());
        city.setState(CityStateOwn);
        //击杀排序
        List<Map.Entry<Long, Integer>> killRank = city.getKill().entrySet().stream().sorted(Map.Entry.comparingByValue((v1, v2) -> v2 - v1)).collect(Collectors.toList());
        //积分排序
        List<Map.Entry<Long, Integer>> scoreRank = city.getScore().entrySet().stream().sorted(Map.Entry.comparingByValue((v1, v2) -> v2 - v1)).collect(Collectors.toList());
        for (Map.Entry<Long, Integer> e : scoreRank) {
            city.getRank().add(e.getKey());
        }

        GuildCrossFudMessage.CrossFudCity.Builder mCity = GuildCrossFudMessage.CrossFudCity.newBuilder();
        mCity.setCityId(city.getCityId());
        mCity.setCamp(pack(camp, 0));
        mCity.setState(city.getState());
        mCity.setBox(mBox(city.getCityBean().getId(), false, false));

        GuildCrossFudMessage.P2GCrossFudOwnerNotice.Builder message = GuildCrossFudMessage.P2GCrossFudOwnerNotice.newBuilder();
        message.setCity(mCity);
        message.setFirst(mess.getFirst());
        for (int i = 0; i < CityRankLimit; i++) {
            if (killRank.size() > i) {
                Map.Entry<Long, Integer> kill = killRank.get(i);
                FudRole role = Manager.fudManager.getFudRole().get(kill.getKey());
                int campId = group.getServerCamp().get(role.getServerId());
                FudCamp camp1 = group.getCamp().get(campId);
                GuildCrossFudMessage.CrossFudRole.Builder mRole = mRole(role);
                mRole.setRank(i + 1);
                mRole.setCamp(pack(camp1, 0));
                mRole.setKill(kill.getValue());
                mRole.setScore(0);
                message.addKillRank(mRole);
            }
            if (scoreRank.size() > i) {
                Map.Entry<Long, Integer> score = scoreRank.get(i);
                FudRole role = Manager.fudManager.getFudRole().get(score.getKey());
                int campId = group.getServerCamp().get(role.getServerId());
                FudCamp camp1 = group.getCamp().get(campId);
                GuildCrossFudMessage.CrossFudRole.Builder mRole = mRole(role);
                mRole.setRank(i + 1);
                mRole.setCamp(pack(camp1, 0));
                mRole.setScore(score.getValue());
                mRole.setKill(0);
                message.addScoreRank(mRole);
            }
        }
        for (ServerInfo server : Manager.gameServerManager.getServerCache().values()) {
            if (camp.getServerList().contains(server.getServerId())) {
                MessageUtils.send_to_game(server.getSession(), GuildCrossFudMessage.P2GCrossFudOwnerNotice.MsgID.eMsgID_VALUE, message.build().toByteArray());
            }
        }
        logger.info("占领福地 group={} city={} camp={}", mess.getGroupId(), mess.getCityId(), camp);
    }

    /**
     * 请求进入跨服福地
     *
     * @param context
     * @param mess
     */
    @Override
    public void G2PCrossFudEnter(ChannelHandlerContext context, GuildCrossFudMessage.G2PCrossFudEnter mess) {

        String serverKey = context.channel().attr(SessionKey.SERVERPLATID).get();
        int groupID = ServerMatchManager.deal().getGroupIDForCurOpenDay(serverKey, DailyActiveDefine.CrossFud);

        CommonMessage.CrossRole cr = mess.getRole();

        long dailyTime;
        if (mess.getType() == 0) {
            dailyTime = Manager.dailyActiveManager.deal().getDailyNearlyEndTime(DailyActiveDefine.CrossFud);
        } else {
            dailyTime = Manager.dailyActiveManager.deal().getDailyNearlyEndTime(DailyActiveDefine.CROSS_FUD_Devil);
        }
        if (dailyTime == 0) {
            MessageUtils.notify_player(context, cr.getRoleId(), MessageString.DAILY_NOT_OPEN);
            return;
        }
        FudGroup group = Manager.fudManager.getGroups().get(groupID);
        if (group == null) {
            logger.info("你所在服务器没有跨服福地参与资格！！！role={}", cr);
            return;
        }

        FudRole role = Manager.fudManager.getFudRole().get(cr.getRoleId());
        if (role == null) {
            role = initFudRole(cr);
        }
        role.setServerId(cr.getServerId());

        int camp = group.getServerCamp().get(role.getServerId());

        FudCity city = group.getCity().get(mess.getCityId());
        Cfg_Cross_fudi_main_Bean bean = CfgManager.getCfg_Cross_fudi_main_Container().getValueByKey(city.getCityId());
        if (bean.getPosition() == 0) {
            return;
        }

        //检测福地是否可进入
        if (!checkRootCity(new HashSet<>(), group, city, camp)) {
            logger.info("没有进入权限！！！role={}", cr);
            return;
        }
        FightRoom room;
        //诸界远征
        if (mess.getType() == 0) {
            room = Manager.fightManager.getFrcache().get(city.getRoomId());
            if (room == null) {
                room = allocRoom(group, city);
            }
            if (room == null) {
                logger.info("跨服福地已销毁 city={} role={}", city.getCityId(), cr);
                return;
            }
        } else {
            //魔王缝隙
            room = Manager.fightManager.getFrcache().get(city.getDevilRoomId());
            if (room == null) {
                logger.info("魔王缝隙已销毁 city={} role={}", city.getCityId(), cr);
                return;
            }
        }
        int fc = 1 << (camp + 16);  //低16位是怪物位
        CrossFightMessage.roleAtt.Builder mRole = CrossFightMessage.roleAtt.newBuilder();
        mRole.setCampNo(fc);
        mRole.setRoleId(role.getRoleId());
        //天禁值
        CommonMessage.CrossAttribute.Builder param = CommonMessage.CrossAttribute.newBuilder();
        param.setType(0);
        param.setParam1(role.gettValue());
        //服务器阵营
        CommonMessage.CrossAttribute.Builder param1 = CommonMessage.CrossAttribute.newBuilder();
        param1.setType(1);
        param1.setValue(role.getServerId());
        param1.setParam1(camp);

        CrossFightMessage.P2GResFightStart.Builder msg = CrossFightMessage.P2GResFightStart.newBuilder();
        msg.setFightId(room.getFid());
        msg.setZoneModelId(room.getModelId());
        msg.setFightServerId(room.getServerId());
        msg.setMapModelId(room.getMapmodelId());
        msg.setOnlyJoin(true);
        msg.addRoleInfo(mRole);
        msg.addMapSetList(param);
        msg.addMapSetList(param1);
        MessageUtils.send_to_game(context, CrossFightMessage.P2GResFightStart.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        logger.info("玩家进入{} city={} role={}", mess.getType() == 0 ? "诸界远征" : "魔王缝隙", city.getCityId(), role);
    }

    /**
     * 检测出生的福地
     *
     * @param group
     * @param city
     * @param campId
     * @return
     */
    boolean checkRootCity(Set<Integer> checkPass, FudGroup group, FudCity city, Integer campId) {

        if (city.getRootNode().isEmpty()) {
            return true;
        }
        if (checkPass.contains(city.getCityId())) {
            return false;
        }
        checkPass.add(city.getCityId());

        ArrayList<Boolean> arr = new ArrayList<>();
        for (int nodeId : city.getRootNode()) {
            FudCity node = group.getCity().get(nodeId);
            if (node.getCamp() == campId) {
                arr.add(checkRootCity(checkPass, group, node, campId));
            } else {
                arr.add(false);
            }
        }
        for (boolean root : arr) {
            if (root) {
                return true;
            }
        }
        return false;
    }


    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.FudScript;
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
}
