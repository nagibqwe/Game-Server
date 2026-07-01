package common.achievement;

import com.data.CfgManager;
import com.data.FunctionStart;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.struct.ReadIntegerArray;
import com.game.achievement.script.IAchievementScript;
import com.game.achievement.structs.AchievementInfo;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.chat.structs.Notify;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import game.core.util.IDConfigUtil;
import game.message.AchievementMessage;
import game.message.AchievementMessage.ResGetAchievement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class AchievementScript implements IAchievementScript {

    private static final Logger log = LogManager.getLogger(AchievementScript.class);

    @Override
    public int getId() {
        return ScriptEnum.AchievementBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void sendAchievementInfo(Player player) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.ChengJiu)) {
            return;
        }
        AchievementMessage.ResAchievementInfo.Builder builder = AchievementMessage.ResAchievementInfo.newBuilder();
        ConcurrentHashMap<Integer, AchievementInfo> achievementInfo = player.getAchievementInfo();
        for (AchievementInfo info : achievementInfo.values()) {
            switch (info.getState()) {
                case AchievementInfo.STATE_DRAW:
                    builder.addHasGetIds(info.getId());
                    break;
                case AchievementInfo.STATE_FINISH:
                    builder.addCanGetIds(info.getId());
                    break;
                case AchievementInfo.STATE_UNFINISH:
                    AchievementMessage.achivementInfo.Builder achInfo = AchievementMessage.achivementInfo.newBuilder();
                    achInfo.setId(info.getId());
                    achInfo.setPro(info.getProgress());
                    achInfo.setState(info.getState());
                    builder.addInfos(achInfo);
                    break;
                default:
                    break;
            }
        }
        MessageUtils.send_to_player(player, AchievementMessage.ResAchievementInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void checkAchievement(Player player, int type) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.ChengJiu)) {
            return;
        }
       // List<AchievementMessage.achivementInfo.Builder> list = new ArrayList<>();
       // ReadIntegerArray condition;
       // for (Cfg_Achievement_Bean bean : CfgManager.getCfg_Achievement_Container().getValuees()) {
       //     condition = bean.getCondition();
       //     if (condition == null || condition.size() < 2) {
       //         continue;
       //     }
//
       //     if (condition.get(0) != type) {
       //         continue;
       //     }
       //     AchievementInfo info = player.getAchievementInfo().get(bean.getId());
       //     if (info == null) {
       //         info = new AchievementInfo(bean.getId());
       //         player.getAchievementInfo().put(bean.getId(), info);
       //     }
//
       //     //已完成或已领取
       //     if (info.getState() == AchievementInfo.STATE_DRAW || info.getState() == AchievementInfo.STATE_FINISH) {
       //         continue;
       //     }
//
       //     int progress = Manager.controlManager.deal().getFuncProgress(player, condition);
       //     if (progress <= info.getProgress()) {
       //         continue;
       //     }
//
       //     boolean finish = Manager.controlManager.deal().checkFuncProgress(player, condition);
       //     info.setProgress(progress);
       //     if(finish){
       //         info.setState(AchievementInfo.STATE_FINISH);
//     //           Manager.biManager.getScript().biRealm(player,2,1,0, bean.getId(), progress+"/"+bean.getCondition().get(bean.getCondition().size()-1));
       //     }else{
       //         info.setState(AchievementInfo.STATE_UNFINISH);
//     //           Manager.biManager.getScript().biRealm(player,2,0,0, bean.getId(), progress+"/"+bean.getCondition().get(bean.getCondition().size()-1));
       //     }
//
       //     AchievementMessage.achivementInfo.Builder infoBuilder = AchievementMessage.achivementInfo.newBuilder();
       //     infoBuilder.setId(info.getId());
       //     infoBuilder.setPro(info.getProgress());
       //     infoBuilder.setState(info.getState());
       //     list.add(infoBuilder);
       // }
       // if (list.isEmpty()) {
       //     return;
       // }
       // AchievementMessage.ResUpdateAchivement.Builder builder = AchievementMessage.ResUpdateAchivement.newBuilder();
       // list.forEach(builder::addInfos);
       // MessageUtils.send_to_player(player, AchievementMessage.ResUpdateAchivement.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void getAchievement(Player player, int id) {
      // if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.ChengJiu)) {
      //     return;
      // }
      // if (player.getAchievementInfo().get(id).getState() != AchievementInfo.STATE_FINISH) {
      //     log.error("已经领取或者还未完成：" + player.getAchievementInfo().get(id).getState());
      //     return;
      // }
      // Cfg_Achievement_Bean bean = CfgManager.getCfg_Achievement_Container().getValueByKey(id);
      // if (bean == null) {
      //     log.error("Cfg_AchievementBean配置表不存在：" + id);
      //     return;
      // }

      // //发奖励
      // long actionId = IDConfigUtil.getLogId();
      // List<Item> itemList = Item.createItems(bean.getItem());
      // int msId = Manager.backpackManager.manager().onHasAddSpaces(player, itemList);
      // if (msId != 0) {
      //     MessageUtils.notify_player(player, Notify.ERROR, msId);
      //     return;
      // }
      // if (!Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.AchievementGet, actionId)) {
      //     MessageUtils.notify_player(player, Notify.ERROR, MessageString.ItemToBagFailed);
      //     return;
      // }
      // Manager.currencyManager.manager().onAddItemCoin(player, ItemCoinType.BindGold, bean.getAddBindGold(), ItemChangeReason.AchievementGet, actionId);
      // Manager.currencyManager.manager().onAddItemCoin(player, ItemCoinType.Achievement, bean.getAddAchievement(), ItemChangeReason.AchievementGet, actionId);

      // //领取后的处理
      // player.getAchievementInfo().get(id).setState(AchievementInfo.STATE_DRAW);

      // //返回消息
      // ResGetAchievement.Builder builder = ResGetAchievement.newBuilder();
      // builder.setId(id);
      // MessageUtils.send_to_player(player, ResGetAchievement.MsgID.eMsgID_VALUE, builder.build().toByteArray());

//        Manager.biManager.getScript().biRealm(player,2,2,0, id, "");
    }

}
