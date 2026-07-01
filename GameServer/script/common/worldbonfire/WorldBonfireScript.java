package common.worldbonfire;

import com.data.CfgManager;
import com.data.Global;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Gather_Bean;
import com.data.bean.Cfg_World_bonfire_Bean;
import com.data.struct.ReadArray;
import com.game.backpack.structs.Item;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.structs.Notify;
import com.game.manager.Manager;
import com.game.map.manager.MapManager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapParam;
import com.game.map.structs.MapUtils;
import com.game.newfashion.manager.NewFashionManager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Gather;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.worldbonfire.manager.WorldBonfireManager;
import com.game.worldbonfire.script.IWorldBonfireScript;
import common.copyMap.marry.MarryHoneyScript;
import game.core.map.Position;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.WorldBonfireMessage.*;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @auther lw
 * @create 2019-10-15 14:36
 */
public class WorldBonfireScript implements IWorldBonfireScript {


    private static final Logger logger = LogManager.getLogger(WorldBonfireScript.class);
    @Override
    public void onBonfireLevel(Player player) {
        if (null == player) {
            return;
        }

        G2PWorldBonfireAddWoodCheck.Builder msgCheck = G2PWorldBonfireAddWoodCheck.newBuilder();
        msgCheck.setRoleId(player.getId());
        MessageUtils.send_to_public(G2PWorldBonfireAddWoodCheck.MsgID.eMsgID_VALUE, msgCheck.build().toByteArray());
    }


    private void refreshGather(Player player,int lv){
        Cfg_World_bonfire_Bean bean = CfgManager.getCfg_World_bonfire_Container().getValueByKey(lv);
        if (bean == null) {
            return;
        }
        MapObject mapObject = Manager.mapManager.getMap( player.gainMapId());
        if (mapObject == null){
            return;
        }
        //刷新采集物
        Cfg_Gather_Bean gatherCfg = CfgManager.getCfg_Gather_Container().getValueByKey(bean.getAdd_gather());
        if (null == gatherCfg) {
            logger.error("Cfg_Gather_Bean无法找到指定数据，id = " + bean.getAdd_gather());
            return;
        }

        List<Position> poslist = new ArrayList<>();
        for (ReadArray<Integer> aii : bean.getGather_location().getValuees()) {
            poslist.add(MapManager.getPos(aii.get(0), aii.get(1)));
        }
        for (int k = 0; k < bean.getAdd_gather_num(); ++k) {
            if (poslist.size() <= 0) {
                logger.error(" 策划配置的 坐标 长度不够");
                break;
            }
            Position pos = poslist.get(0);
            int randIndex = -1;
            if (poslist.size() > 1) {
                randIndex = RandomUtils.random(poslist.size());
                pos = poslist.get(randIndex);
                poslist.remove(randIndex);
            }

            Gather gather = Manager.gatherManager.deal().createGather(mapObject, gatherCfg, pos);
            if (gather == null) {
                logger.error(mapObject.getName() + " 创建采集物时出错了", new NullPointerException());
                if (randIndex >= 0) {
                    poslist.add(pos);
                }
                continue;
            }
        }
    }
    @Override
    public void onBonfireDecItem(Player player, int lv) {
        Cfg_World_bonfire_Bean bean = CfgManager.getCfg_World_bonfire_Container().getValueByKey(lv);
        if (bean == null) {
            return;
        }

        if (!Manager.backpackManager.manager().removeItemOrCurrency(player, bean.getWood_cost().get(0), bean.getWood_cost().get(1),
                IDConfigUtil.getId(), ItemChangeReason.WorldBonfireLevelDec)) {
            return;
        }

        G2FWorldBonfireAddWoodDecItem.Builder msgCheck = G2FWorldBonfireAddWoodDecItem.newBuilder();
        msgCheck.setRoleId(player.getId());
        msgCheck.setLv(lv);
        MessageUtils.send_to_fight(player, G2FWorldBonfireAddWoodDecItem.MsgID.eMsgID_VALUE, msgCheck.build().toByteArray());

        //BI
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.WorldBonfire, ItemChangeReason.WorldBonfireLevelDec, lv);
    }

    @Override
    public void onBonfireCrossDecItem(ChannelHandlerContext context, Player player, int lv) {
        Cfg_World_bonfire_Bean bean = CfgManager.getCfg_World_bonfire_Container().getValueByKey(lv);
        Manager.backpackManager.manager().removeItemOrCurrency(player, bean.getWood_cost().get(0), bean.getWood_cost().get(1),
                IDConfigUtil.getId(), ItemChangeReason.WorldBonfireLevelDec);
        F2PWorldBonfireAddWood.Builder builder = F2PWorldBonfireAddWood.newBuilder();
        builder.setLv(lv);
        builder.setRoleID(player.getId());
        builder.setName(player.getName());
        MessageUtils.send_to_public(F2PWorldBonfireAddWood.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        //刷新采集物
        refreshGather(player ,lv);
    }

    @Override
    public void onBonfireCrossLevel(P2FWorldBonfireAddWoodLv messInfo) {
        ResWorldBonfirePanel.Builder msg = ResWorldBonfirePanel.newBuilder();
        MapObject mapObject = Manager.mapManager.getMap(messInfo.getMapID());
        if (mapObject == null)
            return;
        msg.setParam1(messInfo.getLv());
        msg.setParam2(messInfo.getExp());
        long createTime = MapParam.getMapWorldBonfire(mapObject).getBonFireCreateTime();
        long nowTime = TimeUtils.Time();
        long activeTime = WorldBonfireManager.getActiveTime();
        msg.setRemainTime((int) (activeTime - (nowTime - createTime)));
        MapParam.getMapWorldBonfire(mapObject).setExp(messInfo.getExp());
        MapParam.getMapWorldBonfire(mapObject).setLv(messInfo.getLv());
        MessageUtils.send_to_map(mapObject, ResWorldBonfirePanel.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        MessageUtils.notify_Map(mapObject, Notify.CHAT_SYS_MARQUEE, MessageString.WorldBonfireWood, messInfo.getName());
    }


    @Override
    public void onBonfireFingerMatch(Player player) {
        if (null == player) {
            return;
        }

        int headId = player.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_TYPE).getFashionID();
        int headFrameId = player.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_FRAME_TYPE).getFashionID();
        G2PWorldBonfireMatch.Builder builder = G2PWorldBonfireMatch.newBuilder();
        WorldBonfireMember.Builder mm = WorldBonfireMember.newBuilder();
        mm.setRemainWine(0);
        mm.setRoleId(player.getId());
        mm.setName(player.getName());
        mm.setCarrer(player.getCareer());
        mm.setFacade(MapUtils.getFacade(player));
//        mm.setHeadId(headId);
//        mm.setHeadFrameId(headFrameId);
        mm.setHead(MapUtils.getHead(player));
        builder.setMember(mm);
        MessageUtils.send_to_public(G2PWorldBonfireMatch.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void onBonfireFingerGuess(Player player, long teamId, int total, int type) {
        if (null == player) {
            return;
        }

        G2PWorldBonfireFinger.Builder builder = G2PWorldBonfireFinger.newBuilder();
        builder.setRoleId(player.getId());
        builder.setTeamId(teamId);
        builder.setTotal(total);
        builder.setType(type);
        MessageUtils.send_to_public(G2PWorldBonfireFinger.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void onBonfireFingerLeave(Player player, long teamId) {
        if (null == player) {
            return;
        }

        G2PWorldBonfireLeave.Builder builder = G2PWorldBonfireLeave.newBuilder();
        builder.setRoleId(player.getId());
        builder.setTeamId(teamId);
        MessageUtils.send_to_public(G2PWorldBonfireLeave.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void onBonfireFingerReward(Player player) {
        if (null == player) {
            return;
        }

        G2PWorldBonfireReward.Builder builder = G2PWorldBonfireReward.newBuilder();
        builder.setRoleId(player.getId());
        MessageUtils.send_to_public(G2PWorldBonfireReward.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void onBonfireFingerLocalReward(Player player) {
        if (null == player) {
            return;
        }

        Item item = Item.createItem(Global.World_Bonfire_Game_Reward.get(0), Global.World_Bonfire_Game_Reward.get(1),
                Global.World_Bonfire_Game_Reward.get(2) == 1);
        if (Manager.backpackManager.manager().onHasAddSpace(player, item)) {
            Manager.backpackManager.manager().addItem(player, item, ItemChangeReason.WorldBonfireRewardGet,
                    Global.World_Bonfire_Game_Reward.get(0));
        } else {
            List<Item> items = new ArrayList<>();
            items.add(item);
            Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, items, ItemChangeReason.WorldBonfireRewardGet);
        }

        ResWorldBonfireReward.Builder builder = ResWorldBonfireReward.newBuilder();
        MessageUtils.send_to_player(player, ResWorldBonfireReward.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        //跨服不发了
        // G2FWorldBonfireReward.Builder builder1 = G2FWorldBonfireReward.newBuilder();
        // builder1.setRoleId(player.getId());
        // MessageUtils.send_to_fight(player, G2FWorldBonfireReward.MsgID.eMsgID_VALUE, builder1.build().toByteArray());

        //BI
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.WorldBonfire, ItemChangeReason.WorldBonfireRewardGet);
    }


    @Override
    public void onBonfireFingerCrossReward(Player player) {
        if (null == player) {
            return;
        }

        //跨服就不处理邮件了
        Item item = Item.createItem(Global.World_Bonfire_Game_Reward.get(0), Global.World_Bonfire_Game_Reward.get(1),
                Global.World_Bonfire_Game_Reward.get(2) == 1);
        if (Manager.backpackManager.manager().onHasAddSpace(player, item)) {
            Manager.backpackManager.manager().addItem(player, item, ItemChangeReason.WorldBonfireRewardGet,
                    Global.World_Bonfire_Game_Reward.get(0));
        }
    }

    @Override
    public void onBonfireFingerCancelMatch(Player player) {
        if (null == player) {
            return;
        }

        G2PWorldBonfireCalcelMatch.Builder builder = G2PWorldBonfireCalcelMatch.newBuilder();
        builder.setRoleId(player.getId());
        MessageUtils.send_to_public(G2PWorldBonfireCalcelMatch.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public int getId() {
        return ScriptEnum.WorldBonfireBaseScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }
}
