package common.chat;

import com.data.CfgManager;
import com.data.ItemChangeReason;
import com.data.bean.Cfg_Share_Bean;
import com.game.backpack.structs.Equip;
import com.game.backpack.structs.Item;
import com.game.chat.script.IShareScript;
import com.game.chat.structs.ShareType;
import com.game.equip.struct.EquipDefine;
import com.game.manager.Manager;
import  com.data.MessageString;
import com.game.player.structs.Player;
import com.game.player.structs.SavePlayerLevel;
import com.game.script.structs.ScriptEnum;
import com.game.task.structs.TaskHelp;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import game.core.script.IScript;
import game.message.ChatMessage.ResGetShareRewardResult;
import game.message.ChatMessage.ResShareNotice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分享功能脚本
 *
 * @author luosv
 * Created on 2018/6/7 0007.
 */
public class ShareScript implements IScript, IShareScript {

    private static final Logger LOGGER = LogManager.getLogger("ShareScript");

    @Override
    public int getId() {
        return ScriptEnum.ShareBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    /**
     * 请求领取分享奖励
     *
     * @param player  玩家
     * @param shareId 分享ID
     */
    @Override
    public void onReqGetShareReward(Player player, int shareId) {
        int minId = 10000;
        if (player == null || shareId < minId) {
            return;
        }
        if (!Manager.shareManager.canShare(player, shareId)) {
            sendResGetShareRewardResult(player, shareId, -1);
            return;
        }
        Cfg_Share_Bean bean = CfgManager.getCfg_Share_Container().getValueByKey(shareId);
        if (bean == null) {
            sendResGetShareRewardResult(player, shareId, -1);
            return;
        }
        List<Item> rewards = Item.createItems(bean.getRewards());
        if (rewards == null) {
            sendResGetShareRewardResult(player, shareId, -1);
            return;
        }
        Manager.mailManager.sendMailToPlayer(player.getId()
                , 1
                , MessageString.System
                , MessageString.System
                , MessageString.PLAYER_SHARE_REWARD
                , rewards,ItemChangeReason.PlatformEvaluateShare);
        //记录一哈
        Manager.shareManager.addShare(player, shareId);
        sendResGetShareRewardResult(player, shareId, 1);
        Manager.playerManager.savePlayer(player, SavePlayerLevel.HalfMinLater);
    }

    /**
     * 返回分享奖励领取结果
     */
    private void sendResGetShareRewardResult(Player player, int shareId, int result) {
        ResGetShareRewardResult.Builder msg = ResGetShareRewardResult.newBuilder();
        msg.setShareId(shareId);
        msg.setResult(result);
        MessageUtils.send_to_player(player, ResGetShareRewardResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 通知客户端分享
     *
     * @param player 玩家
     * @param type   类型
     * @param params 参数
     */
    @Override
    public void sendShareNotice(Player player, int type, String... params) {
        if (player == null || type < 1) {
            return;
        }
        int shareId;
        List<Item> items = new ArrayList<>();
        switch (type) {
            case ShareType.SHARE_GET_EQUIP: {
                if (params.length != 1) {
                    return;
                }
                int modelId = Integer.parseInt(params[0]), color;
                Item item = Manager.backpackManager.manager().getItemByModelId(player, modelId);
                if (item == null) {
                    return;
                }
                if (!(item instanceof Equip)) {
                    return;
                }
                if (!Utils.isUpOrangeEquip((Equip) item)) {
                    return;
                }
                color = Utils.getEquipColor((Equip) item);
                if (color < EquipDefine.EquipQuality_White || color > EquipDefine.EquipQuality_Red) {
                    return;
                }
                items.add(item);
                shareId = getShareIdByTypeAndCond(type, color + "");
                break;
            }
            case ShareType.SHARE_UP_LEVEL: {
                if (params.length != 1) {
                    return;
                }
                int oldLv = Integer.parseInt(params[0]), confLv = 0;
                //等级配置
                Map<Integer, Integer> lvMap = getLvConfig();
                if (lvMap == null) {
                    return;
                }
                for (Integer i : lvMap.values()) {
                    //曲线判定，避免一次提升多级跳过配置等级的情况
                    if (oldLv < i && player.getLevel() >= i) {
                        confLv = i;
                        break;
                    }
                }
                if (confLv < 1) {
                    return;
                }
                shareId = getShareIdByTypeAndCond(type, confLv + "");
                break;
            }
            default: {
                if (params.length != 1) {
                    return;
                }
                shareId = getShareIdByTypeAndCond(type, params);
                break;
            }
        }
        if (shareId < 1) {
            return;
        }
        if (!Manager.shareManager.canShare(player, shareId)) {
            return;
        }
        sendResShareNotice(player, shareId, items, params);
    }

    private int getShareIdByTypeAndCond(int type, String... params) {
        if (type < 1 || params.length < 1) {
            return 0;
        }
        int shareId = type * 10000 + Integer.parseInt(params[0]), minId = 10000;
        if (shareId < minId) {
            return 0;
        }
        if (CfgManager.getCfg_Share_Container().getValueByKey(shareId) == null) {
            return 0;
        }
        return shareId;
    }

    private Map<Integer, Integer> getLvConfig() {
        Map<Integer, Integer> lvMap = new HashMap<>(16);
        for (Cfg_Share_Bean bean : CfgManager.getCfg_Share_Container().getValuees()) {
            if (bean.getType() == ShareType.SHARE_UP_LEVEL) {
                lvMap.put(bean.getId(), bean.getCondition());
            }
        }
        return lvMap;
    }

    private void sendResShareNotice(Player player, int shareId, List<Item> items, String... params) {
        ResShareNotice.Builder msg = ResShareNotice.newBuilder();
        msg.setShareId(shareId);
        for (Item item : items) {
            msg.addItems(Manager.backpackManager.manager().buildItemInfo(item));
        }
        for (String str : params) {
            msg.addParamStr(str);
        }
        MessageUtils.send_to_player(player, ResShareNotice.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 玩家拒绝了你的分享
     *
     * @param player  玩家
     * @param shareId 分享ID
     */
    @Override
    public void onReqRefuseShare(Player player, int shareId) {
        int minId = 10000;
        if (player == null || shareId < minId) {
            return;
        }
        Manager.shareManager.addShare(player, shareId);
        Map<Integer, Integer> lvMap = getLvConfig();
        if (lvMap != null) {
            if (lvMap.values().contains(shareId)) {
                for (Integer i : lvMap.values()) {
                    if (i == shareId) {
                        continue;
                    }
                    Manager.shareManager.addShare(player, i);
                }
            }
        }
        Manager.playerManager.savePlayer(player, SavePlayerLevel.HalfMinLater);
    }

    /**
     * GM操作
     *
     * @param player 玩家
     * @param params 参数
     */
    @Override
    public void gmAction(Player player, String[] params) {
        int len = 2;
        if (player == null || params == null || params.length < len) {
            return;
        }
        int gmAction = Integer.parseInt(params[1]);
        switch (gmAction) {
            case 1: {
                //请求领取分享奖励
                len = 3;
                if (params.length < len) {
                    return;
                }
                onReqGetShareReward(player, Integer.parseInt(params[2]));
                break;
            }
            case 2: {
                //通知客户端分享
                len = 4;
                if (params.length < len) {
                    return;
                }
//                sendShareNotice(player, Integer.parseInt(params[2]), params[3]);
                break;
            }
            case 3: {
                //重置分享
                player.getShareMap().clear();
                break;
            }
            case 4: {
                //重置指定类型分享
                len = 3;
                if (params.length < len) {
                    return;
                }
                player.getShareMap().put(Integer.parseInt(params[2]), 0);
            }
            default:
                LOGGER.error(TaskHelp.getPlayerInfo(player) + " 请求错误的gmAction, gmAction=" + gmAction);
                break;
        }
    }

}
