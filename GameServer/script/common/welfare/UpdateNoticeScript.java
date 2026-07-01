package common.welfare;

import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.backpack.structs.Item;
import com.game.chat.structs.Notify;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import com.game.utils.ServerParamUtil;
import com.game.welfare.script.IUpdateNoticeScript;
import game.core.util.IDConfigUtil;
import game.message.WelfareMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * @desc 更新公告脚本逻辑
 * @author gaozhaoguang
 * @date Created on 2020/7/14 16:53
 **/
public class UpdateNoticeScript implements IUpdateNoticeScript {
    private final Logger log = LogManager.getLogger(UpdateNoticeScript.class);
    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.UpdateNoticScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    @Override
    public void receiveAward(Player player) {
        //判断角色是否可以领取奖励
        if(!ServerParamUtil.updateNoticeData.isReceivableAward(player.getId())) {

            //1.判断角色背包是否已经满了
            ReadIntegerArrayEs intArr = new ReadIntegerArrayEs(ServerParamUtil.updateNoticeData.getAwardItems(), "}", ",");
            List<Item> items = Item.createItems(intArr);
            if (Manager.backpackManager.manager().onHasAddSpaces(player, items) != 0) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.MainTaskNoBagCell);
                return;
            }

            //2.发送领取成功的消息
            WelfareMessage.ResGetUpdateNoticeAwardRet.Builder msg = WelfareMessage.ResGetUpdateNoticeAwardRet.newBuilder();
            msg.setRetCode(1);
            MessageUtils.send_to_player(player, WelfareMessage.ResGetUpdateNoticeAwardRet.MsgID.eMsgID_VALUE, msg.build().toByteArray());

            //3.设置标记
            ServerParamUtil.updateNoticeData.addReceive(player.getId());
            ServerParamUtil.saveUpdateNoticeData();

            //4.给角色发送物品奖励
            Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.UpdateNoticeAwardGet, IDConfigUtil.getLogId());
        }
    }

    @Override
    public void refreshNotice(String text, String items, Boolean resetReceives) {

        //保存公告信息
        ServerParamUtil.updateNoticeData.setNoticeText(text);
        ServerParamUtil.updateNoticeData.setAwardItems(items);
        if(resetReceives) {
            ServerParamUtil.updateNoticeData.clearReceives();
        }
        ServerParamUtil.saveUpdateNoticeData();

        log.error("刷新公告信息:"+resetReceives);
        if(resetReceives) {
            //仅对重置领取信息的时候,才会发送
            WelfareMessage.ResUpdateNoticData.Builder msg = createResUpdateNoticeData();
            if(msg != null) {
                //发给所有在线的玩家
                MessageUtils.send_to_all_player(WelfareMessage.ResUpdateNoticData.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }
        }
    }

    @Override
    public void playerOnline(Player player) {
        if(player != null) {
            //仅对重置领取信息的时候,才会发送
            WelfareMessage.ResUpdateNoticData.Builder msg = createResUpdateNoticeData();
            if(msg != null) {
                boolean isReceived = ServerParamUtil.updateNoticeData.isReceivableAward(player.getId());
                msg.setIsGet(isReceived);
                //发送给某个玩家
                MessageUtils.send_to_player(player, WelfareMessage.ResUpdateNoticData.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }
        }
    }

    @Override
    public void freshDataNtf(Player player) {
        return;
    }

    /**
     * 创建更新公告的网络消息
     * @return 更新公告网络信息
     */
    private WelfareMessage.ResUpdateNoticData.Builder createResUpdateNoticeData()
    {
        if(ServerParamUtil.updateNoticeData.isValidNotice()) {
            WelfareMessage.ResUpdateNoticData.Builder msg = WelfareMessage.ResUpdateNoticData.newBuilder();

            msg.setIsGet(false);
            msg.setText(ServerParamUtil.updateNoticeData.getNoticeText());
            ReadIntegerArrayEs intArr = new ReadIntegerArrayEs(ServerParamUtil.updateNoticeData.getAwardItems(), "}", ",");
            for (ReadArray<Integer> itemtabs : intArr.getValuees()) {
                int length = itemtabs.size();
                if (length < 2) continue;
                int itemModelId = 0;
                int num = 0;
                boolean bind = true;
                itemModelId = itemtabs.get(0);
                num = itemtabs.get(1);
                if (length >= 3) {
                    bind = (itemtabs.get(2) == 1);
                }
                WelfareMessage.ItemInfo.Builder item = WelfareMessage.ItemInfo.newBuilder();
                item.setItemID(itemModelId);
                item.setNum(num);
                item.setBind(bind);
                msg.addItems(item);
            }
            return msg;
        }
        else{
            return null;
        }
    }

}
