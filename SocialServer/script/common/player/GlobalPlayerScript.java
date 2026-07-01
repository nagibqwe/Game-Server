package common.player;


import com.game.db.dao.PlayerDao;
import com.game.manager.Manager;
import com.game.player.manager.GlobalPlayerManager;
import com.game.player.script.IGlobalPlayerScript;
import com.game.player.structs.GlobalPlayerWorldInfo;
import com.game.player.structs.Item;
import com.game.player.structs.ItemCoin;
import com.game.player.structs.SaveDeal;
import com.game.script.struct.ScriptEnum;
import com.game.server.SocialServer;
import com.game.utils.MessageUtils;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.CommonMessage;
import game.message.CrossServerMessage;
import game.message.PlayerMessage;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * 全局玩家接口脚本
 *
 * @author ZUncle
 */
public class GlobalPlayerScript implements IGlobalPlayerScript {

    static final Logger logger = LogManager.getLogger(GlobalPlayerScript.class);

    final int PlayerSynStateLogin = 0;
    final int PlayerSynStateQuit = 1;
    final int PlayerSynStateUpdate = 2;

    @Override
    public int getId() {
        return ScriptEnum.GlobalPlayerScript;
    }


    @Override
    public Object call(Object... args) {
        return null;
    }

    /**
     * 玩家心跳
     */
    @Override
    public void tick() {

        long curTime = TimeUtils.Time();

        for (GlobalPlayerWorldInfo player : Manager.globalPlayerManager.getPlayers().values()) {
            try {
                tick(curTime, player);
            } catch (Exception e) {
                logger.error(e, e);
            }
        }
    }

    /**
     * 玩家心跳
     *
     * @param curTime
     * @param player
     */
    @Override
    public void tick(long curTime, GlobalPlayerWorldInfo player) {

        //TODO 检测数据库保存
        if (player.getReadySaveDB() != 0 && player.getReadySaveDB() < curTime) {
            SocialServer.getInstance().playerSaveThread.dealSave(player);
            player.setLastSaveDB(0);
            player.setReadySaveDB(0);
//            logger.warn("保存玩家数据 player={}", player);
        }
    }

    /**
     * 加载全球玩家数据
     */
    @Override
    public void loadAll() {
        PlayerDao db = new PlayerDao();
        List<GlobalPlayerWorldInfo> players = db.selectAll();
        for (GlobalPlayerWorldInfo player : players) {
            GlobalPlayerWorldInfo bean = player.toCache();
            Manager.globalPlayerManager.getPlayers().put(bean.getId(), bean);
        }
        logger.warn("加载玩家 len={}", players.size());
    }

    /**
     * 获得货币
     *
     * @param player
     * @param coin
     * @param count
     * @param changeReason
     */
    @Override
    public void addCoin(GlobalPlayerWorldInfo player, ItemCoin coin, long count, int changeReason) {
        List<Item> items = Item.createItems(coin.getCoin(), count, false);
        addItems(player, items, changeReason);
    }

    /**
     * 发放道具
     *
     * @param player
     * @param items
     * @param changeReason
     */
    @Override
    public void addItems(GlobalPlayerWorldInfo player, List<Item> items, int changeReason) {
        CrossServerMessage.F2GSendReward.Builder dropMsg = CrossServerMessage.F2GSendReward.newBuilder();
        for (Item item : items) {
            CrossServerMessage.dropItemInfo.Builder info = CrossServerMessage.dropItemInfo.newBuilder();
            info.setItemModelId(item.getModelId());
            info.setNum(item.getCount());
            info.setIsBind(item.isBind());
            info.setNotice(false);
            dropMsg.addItems(info);
        }
        dropMsg.setRoleId(player.getId());
        dropMsg.setReason(changeReason);
        dropMsg.setActionId(IDConfigUtil.getId());
        MessageUtils.send_to_server(player, CrossServerMessage.F2GSendReward.MsgID.eMsgID_VALUE, dropMsg.build().toByteArray());
    }

    /**
     * 保存玩家
     *
     * @param player
     * @param deal
     */
    @Override
    public void save2DB(GlobalPlayerWorldInfo player, SaveDeal deal) {
        long curTime = TimeUtils.Time();

        if (player.getLastSaveDB() == 0) {
            player.setLastSaveDB(curTime);
        }
        if (player.getReadySaveDB() == 0){
            player.setReadySaveDB(player.getLastSaveDB() + deal.getTime());
        }
        //TODO 重置保存时间
        if (player.getLastSaveDB() + deal.getTime() < player.getReadySaveDB()) {
            player.setReadySaveDB(player.getLastSaveDB() + deal.getTime());
        }
    }

    /**
     * 更新玩家数据
     *
     * @param channel
     * @param mess
     */
    @Override
    public void G2SSynPlayerSocialInfo(ChannelHandlerContext channel, PlayerMessage.G2SSynPlayerSocialInfo mess) {

        GlobalPlayerWorldInfo player = Manager.globalPlayerManager.getPlayers().getOrDefault(mess.getGlobalPlayerWorldInfo().getRoleid(), new GlobalPlayerWorldInfo());
        player.update(mess.getGlobalPlayerWorldInfo());
        Manager.globalPlayerManager.getPlayers().put(player.getId(), player);

        if (mess.getType() == PlayerSynStateLogin) {
            Manager.globalPlayerManager.getOnlinePlayers().put(player.getId(), player);
            save2DB(player, SaveDeal.RightNow);
            //加载玩家好友数据
            //Manager.friendManager.getPlayerRelation(player.getId());
        }
        if (mess.getType() == PlayerSynStateQuit) {
            Manager.globalPlayerManager.getOnlinePlayers().remove(player.getId());
            save2DB(player, SaveDeal.OneMinLater);
        }
        if (mess.getType() == PlayerSynStateUpdate) {
            save2DB(player, SaveDeal.TenMinLater);
        }

//        logger.info("玩家数据更新 type={}, player={}", mess.getType(), player);
    }

    /**
     * @param messInfo
     */
    public void G2SReqPlayerSummaryInfoHandler(ChannelHandlerContext channel, PlayerMessage.G2SReqPlayerSummaryInfo messInfo) {
        GlobalPlayerWorldInfo globalPlayerWorldInfo = GlobalPlayerManager.getInstance().getPlayers().get(messInfo.getTargetRoleId());
        if (globalPlayerWorldInfo != null) {
            PlayerMessage.ResPlayerSummaryInfo.Builder builder = PlayerMessage.ResPlayerSummaryInfo.newBuilder();
            builder.setRoleId(globalPlayerWorldInfo.getId());
            builder.setRoleName(globalPlayerWorldInfo.getRoleName());
            builder.setCareer(globalPlayerWorldInfo.getCareer());
            builder.setRoleLv(globalPlayerWorldInfo.getLevel());
            builder.setFightpower(globalPlayerWorldInfo.getFightPower());


//            builder.setFashionHead(globalPlayerWorldInfo.getFashionHeadId());
//            builder.setFashionFrame(globalPlayerWorldInfo.getFashionHeadFrameId());
            builder.setHead(GlobalPlayerManager.getHead(globalPlayerWorldInfo));

            builder.setServerId(globalPlayerWorldInfo.getCreateServerId());

            CommonMessage.FacadeAttribute.Builder facade = CommonMessage.FacadeAttribute.newBuilder();

            facade.setFashionBody(globalPlayerWorldInfo.getFashionBodyId());
            facade.setFashionWeapon(globalPlayerWorldInfo.getFashionWeaponId());
            facade.setFashionHalo(globalPlayerWorldInfo.getFashionHalo());
            facade.setFashionMatrix(globalPlayerWorldInfo.getFashionMatrix());
            facade.setSoulArmorId(globalPlayerWorldInfo.getSoulArmorId());
            facade.setSpiritId(globalPlayerWorldInfo.getSpiritId());
            facade.setWingId(globalPlayerWorldInfo.getWingId());
            builder.setFacade(facade);

            //发送给客户端
            MessageUtils.send_to_player(channel, messInfo.getRoleId(), PlayerMessage.ResPlayerSummaryInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        }
    }

    /**
     * 保存所有玩家数据
     */
    @Override
    public void saveAll() {
        for (GlobalPlayerWorldInfo player : Manager.globalPlayerManager.getPlayers().values()) {
            save2DB(player, SaveDeal.RightNow);
        }

        logger.warn("玩家人数len={}", Manager.globalPlayerManager.getPlayers().size());
    }
}
