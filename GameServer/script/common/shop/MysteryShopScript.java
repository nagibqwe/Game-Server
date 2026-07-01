package common.shop;

import com.data.CfgManager;
import com.data.FunctionStart;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Limit_mystery_shop_Bean;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import com.game.backpack.structs.Item;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.structs.Notify;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.map.structs.MapUtils;
import com.game.player.structs.Player;
import com.game.recharge.script.IRechargeReward;
import com.game.script.structs.ScriptEnum;
import com.game.shop.script.IMysteryShop;
import com.game.shop.structs.LimitShop;
import com.game.structs.GlobalType;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import game.core.net.Config.ServerConfig;
import game.core.util.TimeUtils;
import game.message.shopMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by 542 on 2020/5/26.
 */
public class MysteryShopScript implements IMysteryShop ,IRechargeReward {
    private final Logger log = LogManager.getLogger(LimitShopScript.class);

    // 充值
    private final int RECHARGE = -1;

    // 玩家等级
    private final int COND_LVL = 1;
    // 任务
    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.MysteryShopScript;
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


    public boolean canReward(Player player, int goodId) {

        LimitShop shop = player.getSteryShop();
        if (shop == null)
            return false;

        if (shop.getBuys().contains(goodId))
            return false;
        return true;
    }

    /**
     * 给奖励之后的逻辑处理
     *
     * @param player
     * @param orderId
     * @param goodId
     */
    @Override
    public void afterReward(Player player, String orderId, int goodId) {
        LimitShop shop = player.getSteryShop();
        if (shop == null)
            return ;
        long now = TimeUtils.Time();

        boolean isActive =false;
        boolean isActiveOther = false;
        int  id = 0;
        int  overTime = 0;

        for (Cfg_Limit_mystery_shop_Bean bean :  CfgManager.getCfg_Limit_mystery_shop_Container().getValuees()){
                if (!isActive && bean.getGroup() == goodId){
                    id = bean.getId();
                    isActive = true;
                    overTime = bean.getTime();
                    continue;
                }
                if (isActive  && bean.getGroup() == goodId){
                    shop.getBuys().add(bean.getId());
                    continue;
                }
                if (!isActiveOther && bean.getPrice().get(0) <=0 && bean.getGroup()!=goodId ){
                    if (player.getLevel() >= bean.getCondition().get(1)){
                        long endTime = MapUtils.getEndTime(bean.getTime());
                        shop.getShops().put(bean.getId(),endTime);
                    }
                }
        }
        if (id<=0)
            return;
        if (!shop.getShops().containsKey(id)){
            long endTime = MapUtils.getEndTime(overTime);
            shop.getShops().put(id,endTime);
        }
        onReqMysteryShopBuy( player,  id);
    }



    /**
     * 购买或者领取
     */
    @Override
     public  void onReqMysteryShopBuy(Player player, int id){

        LimitShop shop = player.getSteryShop();
        if (shop == null)
            return;
        if (!shop.getShops().containsKey(id))
            return;

        long now = TimeUtils.Time();
        long endTime = shop.getShops().get(id);
        if (endTime <= now) {
            // 商品已经过期
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.LimitShopTimeOut);
            return;
        }

        Cfg_Limit_mystery_shop_Bean bean = CfgManager.getCfg_Limit_mystery_shop_Container().getValueByKey(id);
        if (bean == null) {
            repairData(player, shop, id);
            return;
        }

        if (!checkCondition(player, bean.getCondition())) {
            repairData(player, shop, id);
            return;
        }

        int type = bean.getPrice().get(0);
        int needMoney = bean.getPrice().get(1);
        boolean isCanGetReward =false;

        if (type <=0){
            if (player.getFirstRechargeTime() > 0){
                isCanGetReward = true;
            }
        }else {
            if (!Manager.currencyManager.manager().canDecItemCoin(player, needMoney, type))
                return;
            // 扣钱 Reason
            Manager.currencyManager.manager().onDecItemCoin(player, needMoney, ItemChangeReason.MyShopRewardDec, id,type);
            isCanGetReward = true;
        }

        if (!isCanGetReward)
            return;
        List<ReadArray<Integer>> reward = new ArrayList<>();
        for (ReadArray<Integer>array :  bean.getReward().getValuees()){
            int career =   array.get(3);
            if (career == player.getCareer() || career == 9){
                reward.add(array);
            }
        }
        List<Item> items = Item.createItems(Utils.toReadIntegerArrayEsByArray(reward));
        if (Manager.backpackManager.manager().onHasAddSpaces(player, items) != 0)
            return;


        shop.getBuys().add(id);
        shop.getShops().remove(id);

        for (Cfg_Limit_mystery_shop_Bean bean1 : CfgManager.getCfg_Limit_mystery_shop_Container().getValuees()){
            if (bean.getGroup() == bean1.getGroup()){
                if (shop.getShops().contains(bean1.getId())){
                    shop.getShops().remove(bean1.getId());
                }
                shop.getBuys().add(bean1.getId());
            }
        }
        // reward Reason
        Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.MyShopRewardGet, id);
        log.info(player.getInfo() + " 购买神秘商店物品成功：" + id);

        fresh(player);
        syncClient(player, false,id);
        //记录bi数据
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.MYSTERY_SHOP, ItemChangeReason.MyShopReward, id);
    }

    /**
     * 刷新商品
     *
     * @param player
     */
    @Override
    public void refresh(Player player) {
        if (fresh(player)){
            sendOverdueReward(player);
            syncClient(player, false,0);
        }
    }


    private void sendOverdueReward(Player player)
    {
        LimitShop shop = player.getSteryShop();
        if (shop == null)
            return ;
        long now = TimeUtils.Time();
        Iterator<Map.Entry<Integer, Long>> iterator1 = shop.getShops().entrySet().iterator();
        List<Integer> grouplist = new ArrayList<>();
        while (iterator1.hasNext()) {
            Map.Entry<Integer, Long> item = iterator1.next();
            if (now  > item.getValue()){
                Cfg_Limit_mystery_shop_Bean bean = CfgManager.getCfg_Limit_mystery_shop_Container().getValueByKey(item.getKey());
                if (bean == null)
                    continue;
                if (bean.getPrice().get(0) <=0  && player.getFirstRechargeTime() >0){
                    List<ReadArray<Integer>> reward = new ArrayList<>();
                    for (ReadArray<Integer>array :  bean.getReward().getValuees()){
                        int career =   array.get(3);
                        if (career == player.getCareer() || career == 9){
                            reward.add(array);
                        }
                    }
                    if (!grouplist.contains(bean.getGroup())){
                        List<Item> items = Item.createItems(Utils.toReadIntegerArrayEsByArray(reward));
                        Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail,  1,
                                MessageString.LIMIT_MYSTERY_SHOP_REWARD_MAIL, MessageString.LIMIT_MYSTERY_SHOP_REWARD_TITLE, items, ItemChangeReason.MyShopReward);
                        grouplist.add(bean.getGroup());
                    }
                    if (!shop.getBuys().contains(item.getKey()))
                        shop.getBuys().add(item.getKey());

                    for (Cfg_Limit_mystery_shop_Bean other :CfgManager.getCfg_Limit_mystery_shop_Container().getValuees()){
                        if (other.getGroup() == bean.getGroup()){
                            if (!shop.getBuys().contains(other.getId()))
                                shop.getBuys().add(other.getId());
                        }
                    }
                    iterator1.remove();
                }
            }
        }

        for (Integer id : shop.getBuys()){
            if (shop.getShops().containsKey(id)){
                shop.getShops().remove(id);
            }
        }
    }

    @Override
    public void online(Player player) {
        fresh(player);
        sendOverdueReward(player);
        syncClient(player, true,0);
    }


    private boolean fresh(Player player) {
        LimitShop shop = player.getSteryShop();
        if (shop == null)
            return false;

        boolean ret = false;
        long now = TimeUtils.Time();
        for (Cfg_Limit_mystery_shop_Bean bean : CfgManager.getCfg_Limit_mystery_shop_Container().getValuees()) {
            if (shop.getShops().containsKey(bean.getId())) continue;
            if (shop.getBuys().contains(bean.getId())) continue;
            if (!checkCondition(player, bean.getCondition())) continue;
            ret = true;

            long endTime = MapUtils.getEndTime(bean.getTime());
            shop.getShops().put(bean.getId(),endTime);
        }
        return ret;
    }

    private boolean checkCondition(Player player, ReadIntegerArray condition) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.LimitShop))
            return false;
            if (condition.size() < 2) return false;
            switch (condition.get(0)) {
                case COND_LVL:
                    if (player.getLevel() >= condition.get(1))
                        return true;
                    break;
                default:
                    return false;
            }
        return false;
    }

    private void repairData(Player player, LimitShop shop, int id) {
        shop.getShops().remove(id);
        shop.getBuys().remove((Object) id);
        log.error(player.getInfo() + " 策划配置错误，先拥有，后缺失，修复数据：" + id);
    }


    private void syncClient(Player player, boolean online,int id) {

        LimitShop shop = player.getSteryShop();
        if (shop == null)
            return;

        long now = TimeUtils.Time();
        shopMessage.SyncMysteryShop.Builder builder = shopMessage.SyncMysteryShop.newBuilder();
        for (Map.Entry<Integer, Long> entry : shop.getShops().entrySet()) {
            long endTime = entry.getValue();
            boolean isOverTime = false;
            //同一天只延迟时间一次
            boolean hasDelayTime = TimeUtils.isSameDay(shop.getExpiredShops().getOrDefault(entry.getKey(), 0L), now);
            if (online && endTime > player.getOffLineTime() && now > endTime && !hasDelayTime) {
                endTime =  MapUtils.getEndTime(5 * 60);
                shop.getShops().put(entry.getKey(), endTime);
                shop.getExpiredShops().put(entry.getKey(), endTime);
                isOverTime = true;
            }
            if (endTime <= now) continue;
            shopMessage.LimitShop.Builder ls = shopMessage.LimitShop.newBuilder();
            ls.setId(entry.getKey());
            ls.setEndTime(endTime);
            ls.setIsOverTime(isOverTime);
            builder.addShops(ls);
        }
        builder.addAllBuyIds(shop.getBuys());
        builder.setSuccID(id);
        MessageUtils.send_to_player(player, shopMessage.SyncMysteryShop.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }
}
