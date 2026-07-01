package common.couplefight;


import com.data.CfgManager;
import com.data.ItemChangeReason;
import com.data.bean.Cfg_Marry_battle_exchange_Bean;
import com.data.container.Cfg_Marry_battle_exchange_Container;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.couplefight.scripts.ICoupleShop;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import game.core.util.IDConfigUtil;
import game.message.CouplefightMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 仙女对决商城
 */
public class CoupleShopScript implements ICoupleShop {


    final Logger log = LogManager.getLogger(CoupleShopScript.class);

    @Override
    public int getId() {
        return ScriptEnum.CoupleShopScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    @Override
    public void onLieInitShopData(Player player) {

        CouplefightMessage.ResOnlieInitCoupleShop.Builder msg = CouplefightMessage.ResOnlieInitCoupleShop.newBuilder();
        CouplefightMessage.CoupleShopData.Builder builder ;
        for (Cfg_Marry_battle_exchange_Bean bean : CfgManager.getCfg_Marry_battle_exchange_Container().getValuees()){
            int count = (int) Manager.countManager.getCount(player, BaseCountType.CoupleShop, bean.getId());
            builder = CouplefightMessage.CoupleShopData.newBuilder();
            builder.setId(bean.getId());
            builder.setCount(count);
            msg.addShopDataList(builder.build());
        }
        MessageUtils.send_to_player(player, CouplefightMessage.ResOnlieInitCoupleShop.MsgID.eMsgID_VALUE,
                msg.build().toByteArray());

    }

    @Override
    public void onReqBuyCoupleItem(Player player, int id) {

        Cfg_Marry_battle_exchange_Bean bean =  CfgManager.getCfg_Marry_battle_exchange_Container().getValueByKey(id);
        if (bean == null){
            log.error("Cfg_Marry_battle_exchange_Bean  is null == {}",id);
            sendResBuyCoupleItemResult(player,0,id,0);
            return;
        }
        int count = (int) Manager.countManager.getCount(player, BaseCountType.CoupleShop, bean.getId()) + 1;
        if (bean.getLimitType() != 0){
            if (count > bean.getBuyNum()){
                log.error("购买次数已满   {}  count  {} ",id ,count);
                sendResBuyCoupleItemResult(player,0,id,0);
                return;
            }
        }
        long logid = IDConfigUtil.getLogId();
        boolean cost = Manager.backpackManager.manager().onRemoveItem(player,bean.getPay().get(0)
                ,bean.getPay().get(1), ItemChangeReason.CoupleShopCost,logid);
        if (!cost){
            log.error("货币不足 ");
            sendResBuyCoupleItemResult(player,0,id,0);
            return;
        }
        Manager.backpackManager.manager().addItem(player, bean.getItemID(), 1,
                false, 0, ItemChangeReason.CoupleShopGet, logid);


        int type = getType( bean.getLimitType());
        Count.RefreshType rType = Count.RefreshType.convert(type);
        Manager.countManager.addCount(player, BaseCountType.CoupleShop, bean.getId(), rType, 1);

        sendResBuyCoupleItemResult(player,1,id,count);
    }

    private void sendResBuyCoupleItemResult(Player player,int result,int id,int count){
        CouplefightMessage.ResBuyCoupleItemResult.Builder msg =  CouplefightMessage.ResBuyCoupleItemResult.newBuilder();
        msg.setResult(result);
        msg.setId(id);
        msg.setCount(count);
        MessageUtils.send_to_player(player, CouplefightMessage.ResBuyCoupleItemResult.MsgID.eMsgID_VALUE,
                msg.build().toByteArray());
    }


    @Override
    public void onReqOpenCoupleShop(Player player) {

        onLieInitShopData(player);
    }

    /**
     *    CountType_Forever(4), //永久
     *     CountType_Year(3), //每年刷新
     *     CountType_Month(2), //每月刷新
     *     CountType_Week(1, 24), //每周刷新
     *     CountType_Day(0),  //每日凌晨
     *     CountType_Day_Five(0, 5),  //每日5点
     *     CountType_Hour(5),  //每小时刷新
     */

    private int getType(int type){
        if (type == 2){
            return 4;
        }else {
            return type;
        }
    }
}
