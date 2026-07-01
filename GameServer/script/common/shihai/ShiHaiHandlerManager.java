package common.shihai;

import com.data.CfgManager;
import com.data.FunctionVariable;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_PlayerShiHai_Bean;
import com.data.struct.ReadArray;
import com.game.bi.manager.BIDefine;
import com.game.chat.structs.Notify;
import com.game.log.db.RoleGrowLog;
import com.game.log.grow.GrowType;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.shihai.script.IShiHaiHandler;
import com.game.shihai.struct.ShiHaiData;
import com.game.utils.MessageUtils;
import game.core.script.IScript;
import game.message.MapMessage;
import game.message.ShiHaiMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author admin
 */
public class ShiHaiHandlerManager implements IShiHaiHandler, IScript {

    private final static Logger logger = LogManager.getLogger(ShiHaiHandlerManager.class);

    @Override
    public int getId() {
        return ScriptEnum.ShiHaiBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void shiHaiData(Player player, ShiHaiMessage.ReqShiHaiData messInfo) {
        sendShiHaiData(player);
    }

    @Override
    public void upLevel(Player player, ShiHaiMessage.ReqUpLevel messInfo) {
       ShiHaiData data = player.getShiHaiData();
       int id = data.getCfgId();
         Cfg_PlayerShiHai_Bean bean = CfgManager.getCfg_PlayerShiHai_Container().getValueByKey(id);
       if (bean == null) {
           logger.error("Cfg_PlayerShiHaiBean找不到数据，id = " + id);
           return;
       }
       //是否达到最后一级
       int nextId = id + 1;
       Cfg_PlayerShiHai_Bean nextBean = CfgManager.getCfg_PlayerShiHai_Container().getValueByKey(nextId);
       if (nextBean == null) {
           MessageUtils.notify_player(player, Notify.ERROR, MessageString.SHIHAI_NOT_NEXTLEVEL);
           return;
       }
       //判断条件是否满足进阶
       if( player.getSingleTowerData().getCurLayer() - 1 < bean.getNeed_copy_level()) {
           MessageUtils.notify_player(player, Notify.ERROR, MessageString.SHIHAI_WANGYAO);
           return;
       }

       if( bean.getNeed_item() != null &&  bean.getNeed_item().size()>0){
           if(!Manager.backpackManager.manager().canDeleteItemNum(player, bean.getNeed_item(), 1)) {
               MessageUtils.notify_player(player, Notify.ERROR, MessageString.ItemNotEnough);
               Manager.backpackManager.manager().sendItemNotEnough(player, bean.getNeed_item().get(0).get(0));
               return;
           }
           Manager.backpackManager.manager().removeItemOrCurrencies(player, bean.getNeed_item(), id, ItemChangeReason.ShiHaiDec);
       }
       //升级
       data.setCfgId(nextId);
       //加属性
       for(ReadArray value : bean.getAdd_attribute().getValuees()) {
           if(value.size() < 2) {
               continue;
           }
           data.getShiHaiAttr().addAttribute((int)value.get(0), (int)value.get(1));
       }
       Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.ShiHai);
       /**
        * 同步玩家石海层数到排行榜数据中
        * */
       Manager.rankListManager.deal().setShihaiLayer(player, nextId);
       Manager.controlManager.operate(player, FunctionVariable.ShiHaiLevel, 0);
       sendShiHaiData(player);

       // 广播识海发生变化
       MapMessage.ResShiHaiBroadcast.Builder builder = MapMessage.ResShiHaiBroadcast.newBuilder();
       builder.setPlayerId(player.getId());
       builder.setShiHaiCfgId(nextId);
       MessageUtils.send_to_roundPlayer(player, MapMessage.ResShiHaiBroadcast.MsgID.eMsgID_VALUE, builder.build().toByteArray(), true);

       //成长BI
       Manager.biManager.getScript().biGrow(player, GrowType.shihai_levelUp.getType(), 0, BIDefine.GrowLevelUp, id, nextId, id);
    }

    private void sendShiHaiData(Player player) {
        ShiHaiData data = player.getShiHaiData();
        ShiHaiMessage.ResShiHaiData.Builder msg = ShiHaiMessage.ResShiHaiData.newBuilder();
        msg.setCfgId(data.getCfgId());
        msg.setLayer(player.getSingleTowerData().getCurLayer());
        MessageUtils.send_to_player(player, ShiHaiMessage.ResShiHaiData.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }
}
