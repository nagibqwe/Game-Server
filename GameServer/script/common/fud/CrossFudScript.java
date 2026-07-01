package common.fud;

import com.data.*;
import com.data.bean.Cfg_Cross_fudi_boss_Bean;
import com.data.bean.Cfg_Cross_fudi_main_Bean;
import com.data.bean.Cfg_Cross_fudi_point_gift_reward_Bean;
import com.data.bean.Cfg_Cross_fudi_score_reward_Bean;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.backpack.structs.Item;
import com.game.chat.structs.Notify;
import com.game.copymap.structs.FightRoomState;
import com.game.count.structs.BooleanForever;
import com.game.guildcrossfud.script.IFudScript;
import com.game.guildcrossfud.timer.ClearTvEvent;
import com.game.guildcrossfud.timer.FudCloseEvent;
import com.game.guildcrossfud.timer.FudTickOutEvent;
import com.game.guildcrossfud.timer.RefreshBossEvent;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.GlobalType;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.utils.ServerParamUtil;
import com.game.utils.Utils;
import game.core.net.Config.ServerConfig;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.AlienBossMessage;
import game.message.CommonMessage;
import game.message.GuildCrossFudMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc TODO
 * @Date 2021/2/3 17:54
 * @Auth ZUncle
 */
public class CrossFudScript implements IFudScript {

    final Logger logger = LogManager.getLogger(CrossFudScript.class);

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.CrossFudScript;
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

    CommonMessage.CrossRole.Builder pack(Player player) {
        CommonMessage.CrossRole.Builder info = CommonMessage.CrossRole.newBuilder();
        info.setRoleId(player.getId());
        info.setName(player.getName());
        info.setPlatform(ServerConfig.getServerPlatform());
        info.setServerId(ServerConfig.getServerId());
        info.setPower(player.getFightPoint());
        info.setCareer(player.getCareer());
        return info;
    }

    /**
     * 获取所有福地列表
     *
     * @param player
     * @param mess
     */
    @Override
    public void reqAllCrossFudInfoHandler(Player player, GuildCrossFudMessage.ReqAllCrossFudInfo mess) {
        GuildCrossFudMessage.G2PAllCrossFudInfo.Builder message = GuildCrossFudMessage.G2PAllCrossFudInfo.newBuilder();
        message.setRole(pack(player));
        MessageUtils.send_to_public(GuildCrossFudMessage.G2PAllCrossFudInfo.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 打开福地宝箱
     *
     * @param player
     * @param mess
     */
    @Override
    public void reqCrossFudBoxOpenHandler(Player player, GuildCrossFudMessage.ReqCrossFudBoxOpen mess) {
        GuildCrossFudMessage.G2PCrossFudBoxOpen.Builder message = GuildCrossFudMessage.G2PCrossFudBoxOpen.newBuilder();
        message.setRole(pack(player));
        message.setCityId(mess.getCityId());
        MessageUtils.send_to_public(GuildCrossFudMessage.G2PCrossFudBoxOpen.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 关注boss
     *
     * @param player
     * @param mess
     */
    @Override
    public void reqCrossFudCareBossHandler(Player player, GuildCrossFudMessage.ReqCrossFudCareBoss mess) {
        GuildCrossFudMessage.G2PCrossFudCareBoss.Builder message = GuildCrossFudMessage.G2PCrossFudCareBoss.newBuilder();
        message.setRole(pack(player));
        message.setType(mess.getType());
        message.setCityId(mess.getCityId());
        message.setBossId(mess.getBossId());
        MessageUtils.send_to_public(GuildCrossFudMessage.G2PCrossFudCareBoss.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 获取福地数据
     *
     * @param player
     * @param mess
     */
    @Override
    public void reqCrossFudCityInfoHandler(Player player, GuildCrossFudMessage.ReqCrossFudCityInfo mess) {
        GuildCrossFudMessage.G2PCrossFudCityInfo.Builder message = GuildCrossFudMessage.G2PCrossFudCityInfo.newBuilder();
        message.setRole(pack(player));
        message.setCity(mess.getCity());
        message.setType(mess.getType());
        MessageUtils.send_to_public(GuildCrossFudMessage.G2PCrossFudCityInfo.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 获取福地积分排名
     *
     * @param player
     * @param mess
     */
    @Override
    public void reqCrossFudRankHandler(Player player, GuildCrossFudMessage.ReqCrossFudRank mess) {
        GuildCrossFudMessage.G2PCrossFudRank.Builder message = GuildCrossFudMessage.G2PCrossFudRank.newBuilder();
        message.setType(mess.getType());
        message.setRole(pack(player));
        MessageUtils.send_to_public(GuildCrossFudMessage.G2PCrossFudRank.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 打开福地个人积分宝箱奖励
     *
     * @param player
     * @param mess
     */
    @Override
    public void reqCrossFudScoreBoxOpenHandler(Player player, GuildCrossFudMessage.ReqCrossFudScoreBoxOpen mess) {
        GuildCrossFudMessage.G2PCrossFudScoreBoxOpen.Builder message = GuildCrossFudMessage.G2PCrossFudScoreBoxOpen.newBuilder();
        message.setRole(pack(player));
        message.setBoxId(mess.getBoxId());
        MessageUtils.send_to_public(GuildCrossFudMessage.G2PCrossFudScoreBoxOpen.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 解锁福地个人积分宝箱
     *
     * @param player
     * @param mess
     */
    @Override
    public void reqCrossFudUnLockScoreBoxHandler(Player player, GuildCrossFudMessage.ReqCrossFudUnLockScoreBox mess) {
        Cfg_Cross_fudi_score_reward_Bean bean = CfgManager.getCfg_Cross_fudi_score_reward_Container().getValueByKey(mess.getBoxId());
        if (bean == null || bean.getIf_pay() <= 0) {
            bean = Utils.findOne(CfgManager.getCfg_Cross_fudi_score_reward_Container().getValuees(), o -> o.getIf_pay() > 0);
        }
        if (bean == null) {
            return;
        }
        if (!Manager.currencyManager.manager().canDecBindGoldOrGold(player, bean.getIf_pay())) {
            return;
        }

        GuildCrossFudMessage.G2PCrossFudUnLockScoreBox.Builder message = GuildCrossFudMessage.G2PCrossFudUnLockScoreBox.newBuilder();
        message.setRole(pack(player));
        message.setBoxId(bean.getId());
        MessageUtils.send_to_public(GuildCrossFudMessage.G2PCrossFudUnLockScoreBox.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 请求进入跨服福地
     *
     * @param player
     * @param mess
     */
    @Override
    public void reqCrossFudEnterHandler(Player player, GuildCrossFudMessage.ReqCrossFudEnter mess) {

        if (player.playerCrossData.toFightServer) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CopyIngNotOperate);
            return;
        }

        GuildCrossFudMessage.G2PCrossFudEnter.Builder message = GuildCrossFudMessage.G2PCrossFudEnter.newBuilder();
        message.setRole(pack(player));
        message.setCityId(mess.getCityId());
        message.setType(mess.getType());
        MessageUtils.send_to_public(GuildCrossFudMessage.G2PCrossFudEnter.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 福地个人积分宝箱解锁
     */
    @Override
    public void P2GCrossFudBoxUnLock(GuildCrossFudMessage.P2GCrossFudBoxUnLock mess) {
        Player player = Manager.playerManager.getPlayer(mess.getRoleId());
        Cfg_Cross_fudi_score_reward_Bean bean = CfgManager.getCfg_Cross_fudi_score_reward_Container().getValueByKey(mess.getBoxId());
        Manager.currencyManager.manager().decBindGoldOrGold(player, bean.getIf_pay(), ItemChangeReason.CrossFudUnLockCost, IDConfigUtil.getLogId());

        logger.info("跨服福地个人积分解锁 box={} player={}", mess.getBoxId(), player);
    }

    /**
     * 福地占领奖励
     */
    @Override
    public void P2GCrossFudReward(GuildCrossFudMessage.P2GCrossFudReward mess) {

        Player player = Manager.playerManager.getPlayer(mess.getRoleId());

        List<Item> items = new ArrayList<>();
        for (CommonMessage.ShowItemInfo reward : mess.getRewardListList()) {
            items.add(Item.createItem(reward.getModelId(), reward.getCount(), false));
        }
        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.CrossFudCityBoxGain, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.CrossFudCityBoxGain);
        }
        //记录领取占领奖励次数
        Manager.controlManager.operate(player, FunctionVariable.Cross_fudi_hold_reward, 1);

        logger.info("跨服福地占领奖励发放 reward={} player={}", mess, player);
    }

    /**
     * 福地个人积分宝箱奖励
     */
    @Override
    public void P2GCrossFudScoreBoxOpen(GuildCrossFudMessage.P2GCrossFudScoreBoxOpen mess) {

        Player player = Manager.playerManager.getPlayer(mess.getRoleId());

        Cfg_Cross_fudi_score_reward_Bean bean = CfgManager.getCfg_Cross_fudi_score_reward_Container().getValueByKey(mess.getBoxId());
        List<Item> items = Item.createItems(bean.getReward());
        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.CrossFudScoreBoxGain, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.CrossFudScoreBoxGain);
        }
        logger.info("跨服福地个人积分奖励发放 box={} player={}", mess.getBoxId(), player);
    }

    /**
     * 福地状态刷新
     *
     * @param
     * @param mess
     */
    @Override
    public void P2GCrossFudProcess(GuildCrossFudMessage.P2GCrossFudProcess mess) {

        MapObject map = Manager.mapManager.getMap(mess.getRoomId());
        if (map == null) {
            Manager.crossServerManager.getCrossServer().SendFightStateToPublic(mess.getRoomId(), FightRoomState.FIGHTEND);
            logger.info("跨服福地房间已销毁 roomId={}", mess);
            return;
        }
        if (mess.getType() == 0) {
            Manager.mapManager.addCommand(new RefreshBossEvent(map));
        }
        if (mess.getType() == 1) {
            Manager.mapManager.addCommand(new FudCloseEvent(map));
        }
        if (mess.getType() == 2) {
            Manager.mapManager.addCommand(new FudTickOutEvent(map));
        }
        if (mess.getType() == 3) {
            Manager.mapManager.addCommand(new ClearTvEvent(map));
        }
    }

    /**
     * 福地占领奖励广播
     *
     * @param mess
     */
    @Override
    public void P2GCrossFudOwnerNotice(GuildCrossFudMessage.P2GCrossFudOwnerNotice mess) {

        GuildCrossFudMessage.ResCrossFudOwnerNotice.Builder message = GuildCrossFudMessage.ResCrossFudOwnerNotice.newBuilder();
        message.setFirst(mess.getFirst());
        message.setCity(mess.getCity());
        message.addAllKillRank(mess.getKillRankList());
        message.addAllScoreRank(mess.getScoreRankList());

        MessageUtils.send_to_all_player(GuildCrossFudMessage.ResCrossFudOwnerNotice.MsgID.eMsgID_VALUE, message.build().toByteArray());

        // logger.info("跨服福地占领奖励广播+++++ city={}", mess.getCity());
    }

    /**
     * 获取魔王缝隙怪物列表
     *
     * @param player
     * @param messInfo
     */
    @Override
    public void ReqDevilBossListHandler(Player player, GuildCrossFudMessage.ReqDevilBossList messInfo) {

        GuildCrossFudMessage.G2PDevilBossList.Builder message = GuildCrossFudMessage.G2PDevilBossList.newBuilder();
        message.setRole(pack(player));
        MessageUtils.send_to_public(GuildCrossFudMessage.G2PDevilBossList.MsgID.eMsgID_VALUE, message.build().toByteArray());

    }

    /**
     * 自动关注boss
     *
     * @param player
     */
    @Override
    public void autoFollowBoss(Player player) {

        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.CrossFuDi)) {
            return;
        }
        if (Manager.countManager.getBooleanCountValue(player, BooleanForever.AutoFollowCrossFudBoss)) {
            return;
        }

        Manager.countManager.setBooleanCountValue(player, BooleanForever.AutoFollowCrossFudBoss, true);

        for (Cfg_Cross_fudi_boss_Bean bean : CfgManager.getCfg_Cross_fudi_boss_Container().getValuees()) {
            if (bean.getDefaultFollowOpen() == 0) {
                continue;
            }
            GuildCrossFudMessage.G2PCrossFudCareBoss.Builder message = GuildCrossFudMessage.G2PCrossFudCareBoss.newBuilder();
            message.setRole(pack(player));
            message.setType(1);
            message.setBossId(bean.getId());
            for (Cfg_Cross_fudi_main_Bean city : CfgManager.getCfg_Cross_fudi_main_Container().getValuees()) {
                if (bean.getPosition() != city.getPosition()) {
                    continue;
                }
                message.setCityId(city.getId());
                MessageUtils.send_to_public(GuildCrossFudMessage.G2PCrossFudCareBoss.MsgID.eMsgID_VALUE, message.build().toByteArray());
            }
        }
        logger.info("诸界远征自动关注 player={}", player);
    }

    /**
     * 玩家开启道具宝箱
     *  @param player
     * @param num
     * @return
     */
    @Override
    public boolean useBox(Player player, int num) {

        int openDay = TimeUtils.getOpenServerDay();
        int worldLevel = ServerParamUtil.worldLv;

        Cfg_Cross_fudi_point_gift_reward_Bean box = null;

        for (Cfg_Cross_fudi_point_gift_reward_Bean bean : CfgManager.getCfg_Cross_fudi_point_gift_reward_Container().getValuees()) {
            if (openDay >= bean.getDay().get(0) && openDay <= bean.getDay().get(1)
                    && worldLevel >= bean.getLevel().get(0) && worldLevel <= bean.getLevel().get(1)
            ) {
                box = bean;
            }
        }
        if (box == null) {
            logger.error("宝箱奖励配置错误1 player={} num={}", player, num);
            return false;
        }
        ReadIntegerArrayEs rewardCfg;
        switch (player.getCareer()) {
            case 0:
                rewardCfg = box.getReward0();
                break;
            case 1:
                rewardCfg = box.getReward1();
                break;
            case 2:
                rewardCfg = box.getReward2();
                break;
            case 3:
                rewardCfg = box.getReward3();
                break;
            default:
                logger.error("宝箱奖励配置错误2 player={} num={}", player, num);
                return false;
        }

        ReadArray<Integer> random = RandomUtils.random(rewardCfg.getValuees(), o -> 1);

        List<Item> items = Item.createItems(random, 1);
        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.CrossFudScoreBoxGain, IDConfigUtil.getLogId())){
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.CrossFudScoreBoxGain);
        }
        return true;
    }

    /**
     * 进入混沌虚空
     *
     * @param player
     * @param mess
     */
    @Override
    public void reqEnterCrossAlienHandler(Player player, AlienBossMessage.ReqEnterCrossAlien mess) {
        if (player.playerCrossData.toFightServer) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CopyIngNotOperate);
            return;
        }

        AlienBossMessage.G2PEnterCrossAlien.Builder message = AlienBossMessage.G2PEnterCrossAlien.newBuilder();
        message.setRole(pack(player));
        message.setCityId(mess.getCityId());
        MessageUtils.send_to_public(AlienBossMessage.G2PEnterCrossAlien.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 进入须弥宝库
     *
     * @param player
     * @param mess
     */
    @Override
    public void reqEnterCrossAlienGemHandler(Player player, AlienBossMessage.ReqEnterCrossAlienGem mess) {

        AlienBossMessage.G2PEnterCrossAlienGem.Builder message = AlienBossMessage.G2PEnterCrossAlienGem.newBuilder();
        message.setRole(pack(player));
        message.setCityId(mess.getCityId());
        MessageUtils.send_to_public(AlienBossMessage.G2PEnterCrossAlienGem.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 获取虚空幻境信息
     *
     * @param player
     * @param mess
     */
    @Override
    public void reqCrossAlienCityHandler(Player player, AlienBossMessage.ReqCrossAlienCity mess) {

        AlienBossMessage.G2PCrossAlienCity.Builder message = AlienBossMessage.G2PCrossAlienCity.newBuilder();
        message.setRole(pack(player));
        message.setCityId(mess.getCityId());
        MessageUtils.send_to_public(AlienBossMessage.G2PCrossAlienCity.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }
}
