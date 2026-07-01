package common.backpack;

import com.data.*;
import com.data.bean.Cfg_Characters_Bean;
import com.data.bean.Cfg_Item_Bean;
import com.data.bean.Cfg_Vip_Bean;
import com.data.container.Cfg_Item_Container;
import com.data.struct.HomeTaskType;
import com.game.backpack.log.*;
import com.game.backpack.script.ICurrencyManagerScript;
import com.game.backpack.structs.ItemCoinType;
import com.game.backpack.structs.ItemTypeConst;
import com.game.bi.enums.ResourceType;
import com.game.chat.structs.Notify;
import com.game.commercialize.inter.IDailyRechargeTotal;
import com.game.count.structs.VariantType;
import com.game.db.bean.GoldChange;
import com.game.fightserver.manager.FightClientManager;
import com.game.guild.structs.Guild;
import com.game.guild.structs.GuildMember;
import com.game.home.manager.HomeManager;
import com.game.home.structs.HomeTask;
import com.game.log.LogDataManager;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.openserverac.manager.OpenServerAcManager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.player.structs.SavePlayerLevel;
import com.game.redpacket.structs.RedPacketEnum;
import com.game.roleLog.RoleUpdateLogService;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import com.game.vip.manager.VipManager;
import com.game.vip.structs.VipPower;
import com.game.welfare.struct.RetrieveType;
import game.core.dblog.LogService;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.CommercializeMessage;
import game.message.CrossServerMessage;
import game.message.MapMessage;
import game.message.backpackMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class CurrencyManagerScript implements IScript, ICurrencyManagerScript {

    private static final Logger log = LogManager.getLogger("CurrencyManagerScript");

    @Override
    public int getId() {
        return ScriptEnum.CurrencyManagerBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public boolean isCoinItem(int itemModelId) {
        if (!checkCurrencyType(itemModelId)) {
            return false;
        }

        Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(itemModelId);
        if (null == model) {
            return false;
        }
        return model.getType() == ItemTypeConst.COPPER;
    }

    @Override
    public long getMaxCurrencyLimit(int currencyType) {
        if (!checkCurrencyType(currencyType)) {
            return 0;
        }
        Cfg_Item_Bean bean = CfgManager.getCfg_Item_Container().getValueByKey(currencyType);
        if (bean == null) {
            return 0;
        }
        return bean.getMax();
    }

    @Override
    public boolean canDecBindGoldOrGold(Player player, long num) {
        if (num < 0) {
            return false;
        }
        int remainGold = player.getGold().getReaminGold();
        //获取绑定
        int bindGid = getCurrencyIntNum(player, ItemCoinType.BindGemCoin);
        return remainGold + bindGid >= num;
    }

    @Override
    public boolean decBindGoldOrGold(Player player, int num, int reasons, long action) {
        if (!canDecBindGoldOrGold(player, num)) {
            return false;
        }
        //获取绑定
        int bindGid = getCurrencyIntNum(player, ItemCoinType.BindGemCoin);
        int needBind = 0;
        int noBind = 0;
        int leftBind = bindGid - num;
        if (leftBind >= 0) {
            needBind = num;
        } else {
            needBind = bindGid;
            noBind = Math.abs(leftBind);
        }
        if (needBind != 0) {
            removeCurrency(player, ItemCoinType.BindGemCoin, needBind, reasons, action);
        }
        if (noBind != 0) {
            decGold(player, noBind, reasons, action);
        }
        return true;
    }

    @Override
    public boolean canRemoveGold(Player player, long gold) {
        gold = Math.abs(gold);
        if (gold == 0) {
            return true;
        }
        long goldnum = player.getGold().getReaminGold();
        return goldnum - gold >= 0;
    }

    @Override
    public boolean canDecItemCoin(Player player, long num, int itemModleId) {
        if (itemModleId == ItemCoinType.GemCoin) {
            return canRemoveGold(player, num);
        }
        return canRemoveCurrency(player, itemModleId, num);
    }

    @Override
    public boolean canRemoveCurrency(Player player, int currencyType, long num) {
        if (currencyType == ItemCoinType.GemCoin) {
            log.error("该接口不支持非绑定元宝的移除验证！！！");
            return false;
        }
        num = Math.abs(num);
        if (num == 0) {
            return false;
        }
        return player.getCurrencys().get(currencyType) >= num;
    }

    @Override
    public boolean canAddCurrency(Player player, int currencyType, long num) {
        if (currencyType == ItemCoinType.GemCoin) {
            return canAddGold(player, num);
        }
        if (currencyType == ItemCoinType.EXP) {
            return true;
        }
        if (num < 1) {
            return false;
        }
        if (currencyType == ItemCoinType.GemCoin) {
            log.error("该接口不支持非绑定元宝的验证！！！");
            return false;
        }
        if (player.getCurrencys().length() < currencyType) {
            for (int i = player.getCurrencys().length(); i < currencyType; i++) {
                player.getCurrencys().add(i, 0);
            }
        }
        return player.getCurrencys().get(currencyType) + num <= getMaxCurrencyLimit(currencyType);
    }

    @Override
    public boolean canAddGold(Player player, long gold) {
        if (gold < 1) {
            return false;
        }
        if (player.getGold() == null) {
            Manager.goldManager.playerOnLine(player);//如果元宝记录真的为空了， 则重新构建出来
            LogManager.getLogger("AbstractCurrencyManager").error("没有初始化玩家的元宝数据， 这里初始化一下:" + player.nameIdString());//正常应该不会走到这的，外部判断了数量
        }
        int itemModleId = ItemCoinType.GemCoin;
        Cfg_Item_Bean bean = Cfg_Item_Container.GetInstance().getValueByKey(itemModleId);
        if (bean == null) {
            log.error(String.format("判断添加元宝失败,Item表中找到不对应类型{%s}!!!", itemModleId));
            return false;
        }

        if (player.getGold().getReaminGold() >= bean.getMax()) {
            return false;
        }
        long goldnum = player.getGold().getReaminGold() + gold;
        boolean isadd = !(goldnum < 0 || goldnum >= getMaxCurrencyLimit(ItemCoinType.GemCoin));
        System.out.println(bean.getName() + ""+isadd);
        return isadd;
    }

    @Override
    public boolean addEXP(Player player, long num, int reasons, long action) {
        if (num <= 0) {
            log.error("经验增加传入数量小于0");
            return false;
        }
        return onChangeFinalExp(player, num, reasons, action);
    }

    @Override
    public boolean onChangeFinalExp(Player player, long changeExp, int reasons, long action) {
        long oldExp = getCurrencyNum(player, ItemCoinType.EXP);
        int oldLevel = player.getLevel();
        if (oldExp + changeExp < 0) {
            log.info("script 经验变化之和小于0");
            return false;
        }
        Cfg_Characters_Bean next = CfgManager.getCfg_Characters_Container().getValueByKey(player.getLevel() + 1);
        if (next == null) {
            return false;
        }
        //如果是战斗发把经验同步发回本服发送
        if (changeExpToNormalGame(player, changeExp, reasons)) {
            return true;
        }
        if (oldLevel == Global.PlayerMaxLevel) {
            //策划需求只存下一级的经验
            if (oldExp > next.getExp()) {
                return false;
            }
            changeExp = Math.min(next.getExp() - oldExp, changeExp);
        }
        if (changeExp <= 0){
            return false;
        }
        //增加经验
        player.getCurrencys().add(ItemCoinType.EXP, changeExp);

        //增加累计经验
        BigInteger beforeExp = player.getTotalExp();
        player.setTotalExp(player.getTotalExp().add(BigInteger.valueOf(changeExp)));

        //法宝经验器灵增加进度
        Manager.stateStifleManager.deal().addSoulSpiritProgress(player, 1, changeExp, 0);
        //判断是否升级
        long updateExp = checkLevelUp(player);
        boolean levelUp = updateExp > 0;
        long nowExp = player.getCurrencys().get(ItemCoinType.EXP);
        if (levelUp) {
            Manager.playerManager.manager().playerLevelUp(player, oldLevel, oldExp, player.getLevel(), nowExp, changeExp, reasons, action);
            Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.BASE);
        }

        // 高频经验只在升级的时候，记录一条
        boolean expBi = true;
        if (reasons == ItemChangeReason.LeaderPreachAddExpGet
                || reasons == ItemChangeReason.HookOnlineGet
                || reasons == ItemChangeReason.HookMapGet
                || reasons == ItemChangeReason.ExpCopyGet
                || reasons == ItemChangeReason.WorldBonfireExpGet) {
            expBi = levelUp;
        }

        //同步客户端经验值
        sendExpChange(player, reasons, nowExp, changeExp);

        //更换了掌门传道的记录位置
        if(reasons != ItemChangeReason.LeaderPreachAddExpGet && reasons != ItemChangeReason.HookOnlineGet){
            Manager.biManager.getScript().biResource(player, changeExp>0?1:0, ItemCoinType.EXP, BigInteger.valueOf(changeExp), beforeExp, player.getTotalExp(), oldLevel, player.getLevel(), reasons, action);
        }
        writeLog(player, oldExp, nowExp, changeExp, updateExp, reasons, action, ItemCoinType.EXP);
//        if (expBi) {
//            Manager.biManager.getScript().biExp(player, player.getLoginIP(), oldLevel, player.getLevel(), num, nowExp, reasons, action);
//            writeLog(player, oldExp, nowExp, num, updateExp, reasons, action, ItemCoinType.EXP);
//        }

        return true;
    }

    /**
     * 增加元宝
     *
     * @param player
     * @param gold
     * @param reasons
     * @param action  这个方法一定要被override，添加日志等记录
     */
    private void addGold(Player player, long gold, int reasons, long action) {
        if (player.getGold().getReaminGold() >= getMaxCurrencyLimit(ItemCoinType.GemCoin)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.MaxGold);
        }
        int beforeGold = player.getGold().getReaminGold();
        int afterGold = beforeGold + (int) gold;
        if (afterGold < 0) {//正常应该不会走到这的，外部判断了数量
            LogManager.getLogger("AbstractCurrencyManager").error("元宝数量超范围大小了！");
            return;
        }
        long maxCurrencyLimit = getMaxCurrencyLimit(ItemCoinType.GemCoin);
        if (afterGold > maxCurrencyLimit) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.MaxGold);
            afterGold = (int) maxCurrencyLimit;
        } else {
            afterGold = beforeGold + (int) gold;
        }
        //设置变化后的元宝值
        player.getGold().setReaminGold(afterGold);

        Manager.biManager.getScript().biMoney(player, player.getLoginIP(),ItemCoinType.GemCoin,(int) gold,beforeGold,afterGold,reasons,action,1, player.getCurGps().getModelId());
        Manager.biManager.get4399Script().goldBiTo4399(player, ItemCoinType.GemCoin, (int) gold, afterGold, 1, reasons);

        MessageUtils.notify_player(player, Notify.CHAT,MessageString.GetEquipsTips,Manager.backpackManager.manager().getName(ItemCoinType.GemCoin),gold+"");
       // Manager.chatManager.sendSystemStrToPlayer(player, "获得" + gold + Manager.backpackManager.manager().getName(ItemCoinType.GoldType));
        sendPlayerCurrencyAttribute(player, ItemCoinType.GemCoin, player.getGold().getReaminGold(), reasons);

        long after = player.getGold().getReaminGold();
        changeGold(player, beforeGold, gold, after, reasons, action);
    }

    @Override
    public boolean onAddGold(Player player, int gold, int itemChangeReason, long actionId) {
        if (!canAddGold(player, gold)) {
            return false;
        }

        addGold(player, gold, itemChangeReason, actionId);
        return true;
    }

    @Override
    public boolean onAddItemCoin(Player player, int itemModelId, long num, int reson, long action) {
        if (!checkCurrencyType(itemModelId)) {
            log.info(String.format("此接口只针对货币的增加，道具增加请用道具接口!!!{%s}增加货币失败reson{%s}", itemModelId, reson));
            return false;
        }
        if (itemModelId == ItemCoinType.GemCoin) {
            if (!canAddGold(player, num)) {
                log.error(player.getName() + "(" + player.getId() + ")的元宝不能再增加了！");
                return false;
            } else {
                addGold(player, num, reson, action);
                return true;
            }
        }

        if (!canAddCurrency(player, itemModelId, num)) {
            log.info(player.getInfo() + "的[" + Manager.backpackManager.manager().getName(itemModelId) + "]不能再增加了！当前数量:"+player.getCurrencys().get(itemModelId)+",增加数量:"+num);
            return false;
        }

        if (ItemCoinType.EXP == itemModelId) {
            return addEXP(player, num, reson, action);
        }
        return addCurrency(player, itemModelId, num, reson, action);
    }

    /**
     * 增加货币(非元宝，经验)
     *
     * @param player
     * @param currencyType 货币类型
     * @param num
     * @param reasons
     * @param action
     * @return 新增加是否已经到顶了 这个方法一定要被override，添加日志等记录
     */
    private boolean addCurrency(Player player, int currencyType, long num, int reasons, long action) {
        //元宝与金币都不能走到这个函数
        if (currencyType == ItemCoinType.GemCoin) {
            log.info("该接口不支持非绑定元宝的增加！！");
            return false;
        }
        if (currencyType == ItemCoinType.EXP) {
            log.info("该接口不支持经验的增加！！");
            return false;
        }
        long beforeNum = getCurrencyNum(player, currencyType);
        boolean isTop = addCurrency(player, currencyType, num, reasons);
        long after = getCurrencyNum(player, currencyType);
        Manager.biManager.getScript().biMoney(player, player.getLoginIP(),currencyType,(int) num,beforeNum, after,reasons,action,1, player.getCurGps().getModelId());
        //注释掉重复的数据
//        Manager.biManager.getScript().biResource(player, 1, currencyType, BigInteger.valueOf(num), BigInteger.valueOf(beforeNum), BigInteger.valueOf(after), player.getLevel(), player.getLevel(), reasons, action);
        Manager.biManager.get4399Script().goldBiTo4399(player, currencyType, (int) num, (int) after, 1, reasons);
        writeLog(player, beforeNum, after, num, 0, reasons, action, currencyType);
        if (currencyType == ItemCoinType.PhysicalStrength) {
            Manager.playerManager.savePlayer(player, SavePlayerLevel.TenMinLater);//体力10分存一次
        } else {
            Manager.playerManager.savePlayer(player, SavePlayerLevel.FiveMinLater);
        }
        if (isTop) {
            switch (currencyType) {
                case ItemCoinType.BindMoney:
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.MaxMoney);
                    break;
                case ItemCoinType.BindGemCoin:
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.MaxGiftCash);
                    break;
                case ItemCoinType.GoodEvil:
                    break;
                default:
                    String cuname = Manager.backpackManager.manager().getName(currencyType);
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.MaxOtherCoin, cuname, cuname);
                    break;
            }
        }
        coinChangeEvent(player, currencyType, beforeNum, num, after, reasons, action);
        return after != beforeNum;
    }

    private boolean addCurrency(Player player, int currencyType, long num, int reasons) {
        boolean isTop = false;
        long currencynum = player.getCurrencys().get(currencyType) + num;
        long maxLimit = getMaxCurrencyLimit(currencyType);
        if (currencynum < 0 || currencynum >= maxLimit) {
            player.getCurrencys().set(currencyType, maxLimit);
            currencynum = maxLimit;//最大金额值
            isTop = true;
        } else {
            player.getCurrencys().set(currencyType, currencynum);
        }

        MessageUtils.notify_player(player, Notify.CHAT,MessageString.GetEquipsTips,Manager.backpackManager.manager().getName(currencyType),num+"");
       // Manager.chatManager.sendSystemStrToPlayer(player, "获得" + num + Manager.backpackManager.manager().getName(currencyType));
        sendPlayerCurrencyAttribute(player, currencyType, currencynum, reasons);
        return isTop;
    }

    @Override
    public boolean decGold(Player player, int gold, int reasons, long action) {
        if (!canRemoveGold(player, gold)) {
            return false;
        }

        int g = removeGold(player, gold, reasons, action);
        return g > 0;
    }

    /**
     * 减少元宝
     *
     * @param player
     * @param gold    正数负数都行 都是扣
     * @param reasons
     * @param action
     * @return int数组 int[0]花费元宝， int[1]花费绑定元宝 这个方法一定要被override，添加日志等记录
     */
    private int removeGold(Player player, long gold, int reasons, long action) {
        long before = player.getGold().getReaminGold();
        gold = Math.abs(gold);
        int removeGold = (int) gold;
        int beforeGold = player.getGold().getReaminGold();
        if (beforeGold - gold < 0) {
            LogManager.getLogger("AbstractCurrencyManager").error("元宝扣除 数量不足");//正常应该不会走到这的，外部判断了数量
            return 0;
        }
        player.getGold().setReaminGold(player.getGold().getReaminGold() - removeGold);
        sendPlayerCurrencyAttribute(player, ItemCoinType.GemCoin, player.getGold().getReaminGold(), reasons);

        long after = player.getGold().getReaminGold();
        if (removeGold < 1) {
            return 0;
        }

        Manager.activityManager.coinCostTrigger(player, ItemCoinType.GemCoin,removeGold);
        OpenServerAcManager.getInstance().deal().onCostGold(player, removeGold);
        //每日消耗累计
        IDailyRechargeTotal script = (IDailyRechargeTotal) Manager.commercializeManager.getScript(CommercializeMessage.Commercialize.NewDailyRecharge);
        if (script != null)
            script.totalConsume(player, removeGold);
        Manager.biManager.getScript().biMoney(player, player.getLoginIP(),ItemCoinType.GemCoin,(int) gold,beforeGold,after,reasons,action,0, player.getCurGps().getModelId());
        Manager.biManager.get4399Script().goldBiTo4399(player, ItemCoinType.GemCoin, (int) gold, (int) after, -1, reasons);
        changeGold(player, before, -gold, after, reasons, action);
        return removeGold;
    }

    @Override
    public boolean onDecItemCoin(Player player, long num, int reason, long action, int itemModelId) {
        if (num == 0) {
            log.info(String.format("{%s}扣除货币失败reason{%s},扣除数量不能为0!!!", itemModelId, reason));
            return false;
        }
        if (!checkCurrencyType(itemModelId)) {
            log.info(String.format("此接口只针对货币的扣除，道具扣除请用道具接口!!!{%s}扣除货币失败reson{%s}", itemModelId, reason));
            return false;
        }
        if (itemModelId == ItemCoinType.GemCoin) {
            if (!canRemoveGold(player, num)) {
                log.info(player.getName() + "(" + player.getId() + ")的元宝数量不足！");
                return false;
            } else {
                removeGold(player, num, reason, action);
                return true;
            }
        }
        if (!canRemoveCurrency(player, itemModelId, num)) {
            log.info(player.getInfo() + "的[" + Manager.backpackManager.manager().getName(itemModelId) + "]不能再扣除了！当前数量:"+player.getCurrencys().get(itemModelId)+",扣除数量:"+num);
            return false;
        }

        removeCurrency(player, itemModelId, num, reason, action);
        return true;
    }

    /**
     * 减少货币
     *
     * @param player
     * @param currencyType 货币类型
     * @param num          正数负数都行 都是扣
     * @param reasons
     * @param action       这个方法一定要被override，添加日志等记录
     */
    private void removeCurrency(Player player, int currencyType, long num, int reasons, long action) {
        //元宝都不能走到这个函数
        if (currencyType == ItemCoinType.GemCoin) {
            return;
        }
        long beforeNum = getCurrencyNum(player, currencyType);
        num = Math.abs(num);
        player.getCurrencys().add(currencyType, -num);
        long currencynum = player.getCurrencys().get(currencyType);
        sendPlayerCurrencyAttribute(player, currencyType, currencynum, reasons);

        //针对活跃值,要触发运营活动的货币消耗处理
        if(currencyType == ItemCoinType.ActivePoint){
            Manager.activityManager.coinCostTrigger(player, ItemCoinType.ActivePoint,(int)num);
        }

        long after = getCurrencyNum(player, currencyType);
        Manager.biManager.getScript().biMoney(player, player.getLoginIP(),currencyType,(int) num,beforeNum,after,reasons,action,0, player.getCurGps().getModelId());
        //去掉重复的数据
//        Manager.biManager.getScript().biResource(player, 0, currencyType, BigInteger.valueOf(num), BigInteger.valueOf(beforeNum), BigInteger.valueOf(after), player.getLevel(), player.getLevel(), reasons, action);
        Manager.biManager.get4399Script().goldBiTo4399(player, currencyType,(int) num, (int) after, -1, reasons);
        writeLog(player, beforeNum, after, -num, 0, reasons, action, currencyType);
        Manager.playerManager.savePlayer(player, SavePlayerLevel.FiveMinLater);
        coinChangeEvent(player, currencyType, beforeNum, -num, after, reasons, action);
    }

    @Override
    public long getCurrencyNum(Player player, int currencyType) {
        return getCurrentyAttr(player, currencyType);
    }

    @Override
    public int getCurrencyIntNum(Player player, int currencyType) {
        return (int)getCurrentyAttr(player, currencyType);
    }

    private backpackMessage.ItemCoin.Builder buildItemCoinInfo(int type, long value) {
        backpackMessage.ItemCoin.Builder b = backpackMessage.ItemCoin.newBuilder();
        b.setType(type);
        b.setValue(value);
        return b;
    }

    @Override
    public void coinChangeEvent(Player player, int type, long beforeMoney, long num, long afterMoney, int reasons, long action) {
        //货币历史累加值
        if (num > 0) {
            long before = player.getHistoryCoin().get(type);
            if (before + num < Long.MAX_VALUE) {
                player.getHistoryCoin().add(type, num);
            } else {
                player.getHistoryCoin().set(type, Long.MAX_VALUE - 1);
            }
        }

        if (type == ItemCoinType.Popularity) {
            List<Integer> params = new ArrayList<>();
            params.add(1);
            HomeManager.getInstance().deal().doTaskAction(player, HomeTaskType.Popularity.getValue(), params);
            Manager.playerManager.manager().syncPlayerWorldInfo(player, false);
        }

        if (type == ItemCoinType.BindMoney) {
            if (num < 0) {
                Manager.countManager.addVariant(player, VariantType.ConsumeBindCoin, (int) (beforeMoney - afterMoney));
            }
            Manager.controlManager.operate(player, FunctionVariable.Boundgold, (int) (beforeMoney - afterMoney));
        } else if (type == ItemCoinType.BindGemCoin) {
            if (num < 0) {
                Manager.countManager.addVariant(player, VariantType.ConsumeBindGold, (int) (beforeMoney - afterMoney));
                Manager.controlManager.operate(player, FunctionVariable.ConsumeBindDiamonds, (int) (beforeMoney - afterMoney));
            }
        } else if (type == ItemCoinType.GoodEvil) {
//            int round = (int) afterMoney;
//            PlayerMessage.ResUpdataPkValue.Builder msg = PlayerMessage.ResUpdataPkValue.newBuilder();
//            msg.setPlayerId(player.getId());
//            msg.setPkValue(round);
//            MessageUtils.send_to_roundPlayer(player, PlayerMessage.ResUpdataPkValue.MsgID.eMsgID_VALUE, msg.build().toByteArray());
//
//            //增加罪大恶极buff,读取配置表
//            if (afterMoney >= PlayerDefine.PkGoodEvil) {
//                int buffID = Global.Killbuff2;
//                Manager.buffManager.deal().onAddBuff(player, player, buffID);
//                log.error(player.nameIdString() + " 添加罪大恶极BUFFID=" + buffID);
//            }
//
//            if (afterMoney < PlayerDefine.PkGoodEvil) {
//                int buffID = Global.Killbuff2;
//                Manager.buffManager.deal().onRemoveBuff(player, buffID);
//                log.error(player.nameIdString() + " 删除罪大恶极BUFFID=" + buffID);
//            }
//
//            if (afterMoney >= PlayerDefine.PkRedName) {
//                int buffID = Global.Killbuff1;
//                Manager.buffManager.deal().onAddBuff(player, player, buffID);
//                log.error(player.nameIdString() + " 添加小BUFFID=" + buffID);
//            }
//
//            if (afterMoney < PlayerDefine.PkRedName) {
//                int buffID = Global.Killbuff1;
//                Manager.buffManager.deal().onRemoveBuff(player, buffID);
//                log.error(player.nameIdString() + " 删除小BUFFID=" + buffID);
//            }
        } else if (type == ItemCoinType.GuildContibute) {
            if (num > 0) {
                if (player.isHaveGuild()) {
                    Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());
                    GuildMember guildMember = guild.getMembers().get(player.getId());
                    guildMember.setContribute(guildMember.getContribute() + num);
                    Manager.guildsManager.changeGuildExpCommand(guild, num * Global.Guild_contribution_funds, reasons, player.getId());
                }
            }
        }else if(type == ItemCoinType.ActivePoint){
            Manager.retrieveResManager.getScript().count(player, RetrieveType.Activity, afterMoney);
            if(num > 0)
            {
                Manager.controlManager.operate(player, FunctionVariable.ActiveValueGet, (int)num);
            }
        }

        else if (type == ItemCoinType.Achievement) {
            Manager.controlManager.operate(player, FunctionVariable.AchievementPoints, 0);
        } else if (type == ItemCoinType.VipExp) {
            int nowVip = Manager.vipManager.deal().getVipLvByExp(getCurrentyAttr(player, ItemCoinType.VipExp));
            if (nowVip != player.getVipLv()) {
                int startVip = player.getVipLv() + 1;
                player.setVipLv(nowVip);
                Manager.controlManager.operate(player, FunctionVariable.VipLevel, 1);

                MapMessage.ResVipLvBroadCast.Builder builder = MapMessage.ResVipLvBroadCast.newBuilder();
                builder.setVipLv(player.getVipLv());
                builder.setRoleId(player.getId());
                MessageUtils.send_to_roundPlayer(player, MapMessage.ResVipLvBroadCast.MsgID.eMsgID_VALUE,
                        builder.build().toByteArray(), true);

                if (nowVip != 1) {
                    MessageUtils.notify_allOnlinePlayer(Notify.CHAT_SYS_URL_MARQUEE, MessageString.VIP_LEVEL_UP_TITLE, player.getName(), nowVip + "", Utils.makeUrlStr(MessageString.VIP_LEVEL_UP_TITLE));
                }

                //每一等级都需要遍历
                for (int i = startVip; i <= nowVip; i++) {
                    Cfg_Vip_Bean bean = CfgManager.getCfg_Vip_Container().getValueByKey(i);
                    if (bean != null && bean.getTitleReward() != 0) {
                        Manager.titleManager.deal().useTitleItem(player, bean.getTitleReward(), 1, ItemChangeReason.VipLevelUpGet);
                    }

                    if (Manager.vipManager.power().isCanFreeRecycle(player) && !player.isAutoRecycle() && player.getLevel() >= 65) {
                        Manager.recycleManager.deal().setAutoRecycle(player, true);
                    }

                    int buffId1 = Manager.vipManager.power().getVipFreeNum(player, VipPower.POWER_8);
                    int buffId2 = Manager.vipManager.power().getVipFreeNum(player, VipPower.POWER_12);
                    Manager.buffManager.deal().onAddBuff(player, player, buffId1);
                    Manager.buffManager.deal().onAddBuff(player, player, buffId2);
                    String content = MessageString.VIPLevelUpMailContext + "@_@" + i;
                    Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail, MessageString.System, MessageString.VIPLevelUpMailTitle, content);
                    Manager.shopManager.limitShop().refresh(player);

                    //发红包
                    Manager.redPacketManager.createRedpacket(player, RedPacketEnum.getByVip(i));
                }
            }
        }

        if (type != ItemCoinType.EXP) {
//            Manager.backpackManager.manager().pushCollectLog(player, type, num);
        }
    }

    //元宝变化
    @Override
    public boolean changeGold(Player player, long before, long num, long after, int reasons, long action) {
        writeLog(player, before, after, num, 0, reasons, action, ItemCoinType.GemCoin);
        Manager.saveThreadManager.getSaveGoldThread().addGold(player.getGold(), makeGoldChange(player, (int) before, reasons));
        player.getGold().setRechargeGold((int) Manager.rechargeManager.deal().rechargeAll(player));
        RoleUpdateLogService.getInstance().goldChange(player.getGold());

        if (num >= 0) {
            return true;
        }

        if (reasons == ItemChangeReason.MarketBuyItemDec) {
            return true;
        }
        if (reasons == ItemChangeReason.TradeDec) {
            return true;
        }

        if (reasons == ItemChangeReason.AuctionGet) {
            return true;
        }

        Manager.countManager.addVariant(player, VariantType.ConsumeGold, (int) (-num));
        Manager.controlManager.operate(player, FunctionVariable.ConsumeDiamonds, (int)(-num));
        Manager.openServerAcManager.deal().onReqOpenServerSpecSaveRed(player, (int) (-num));

        //是否有跨服消耗活动有的话就发送到跨服
//        if (Manager.activityManager.isContainActivity(ActivityType.CROSS_CONSUME_RANK_ACTION)) {
//            Manager.activityManager.deal().sendCrossRankData(player, -num, ActivityType.CROSS_CONSUME_RANK_ACTION);
//        }
        return true;
    }

    private GoldChange makeGoldChange(Player player, int beforeGold, int reason) {
        GoldChange change = new GoldChange();
        change.setAfterNum(player.getGold().getReaminGold());
        change.setBeforeNum(beforeGold);
        change.setChangeNum(player.getGold().getReaminGold() - beforeGold);
        change.setPlatformName(player.getPlatformName());
        change.setReason(reason);
        change.setServerId(player.getCreateServerId());
        change.setTime((int) (TimeUtils.Time() / 1000));
        change.setUserId(player.getUserId());
        change.setRoleId(player.getId());
        return change;
    }

    private boolean changeExpToNormalGame(Player player, long num, int reasons) {
        if (GameServer.getInstance().IsFightServer()) {
            CrossServerMessage.F2GAddExp.Builder msg = CrossServerMessage.F2GAddExp.newBuilder();
            msg.setRoleId(player.getId());
            msg.setExp(num);
            msg.setReason(reasons);
            FightClientManager.GetInstance().send_to_game(player.getIosession(), CrossServerMessage.F2GAddExp.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            return true;
        }
        return false;
    }

    private long checkLevelUp(Player player) {
        long updateExp = 0;
        while (true) {
            if (player.getLevel() >= Global.PlayerMaxLevel) {
                break;
            }
            Cfg_Characters_Bean model = CfgManager.getCfg_Characters_Container().getValueByKey(player.getLevel());
            if (model == null) {
                break;
            }
            //转职等级判断
            if (player.getXsGrade() < model.getChangejob_limit()) {
                break;
            }
            //是否升级
            if (getCurrencyNum(player, ItemCoinType.EXP) >= model.getExp()) {
                updateExp += model.getExp();
                player.getCurrencys().add(ItemCoinType.EXP, -model.getExp());
                player.setLevel(player.getLevel() + 1);
                //等级变化bi
                BigInteger bigChange = BigInteger.valueOf(1);
                BigInteger beforeNum = BigInteger.valueOf(player.getLevel() - 1);
                BigInteger afterNum = BigInteger.valueOf(player.getLevel());
                Manager.biManager.getScript().biResource(player, 1, ResourceType.RoleLevel.getId(),bigChange,beforeNum,afterNum,0,0, 0,IDConfigUtil.getLogId());
            } else {
                break;
            }
        }
        return updateExp;
    }

    @Override
    public void sendItemCoinChange(Player player, int reason, int type, long v) {
        backpackMessage.ResCoinChange.Builder msg = backpackMessage.ResCoinChange.newBuilder();
        msg.setReason(reason);
        msg.setChangeCoin(buildItemCoinInfo(type, v));
        MessageUtils.send_to_player(player, backpackMessage.ResCoinChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void sendItemCoinInfos(Player player) {
        backpackMessage.ResCoinInfos.Builder msg = backpackMessage.ResCoinInfos.newBuilder();
        int gold = 0;
        if (player.getGold() != null) {
            gold = player.getGold().getReaminGold();
        }
        for (int type = 1; type < ItemCoinType.ItemCoinMaxId; ++type) {
            if (type == ItemCoinType.GemCoin) {
                msg.addCoinList(buildItemCoinInfo(type, gold));
            } else {
                msg.addCoinList(buildItemCoinInfo(type, getCurrencyNum(player, type)));
            }
        }
        MessageUtils.send_to_player(player, backpackMessage.ResCoinInfos.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void sendExpChange(Player player, int reason, long value, long changeValue) {
        backpackMessage.ResExpChange.Builder msg = backpackMessage.ResExpChange.newBuilder();
        msg.setReason(reason);
        msg.setChangeCoin(buildItemCoinInfo(ItemCoinType.EXP, value));
        msg.setChangeNum(changeValue);
        MessageUtils.send_to_player(player, backpackMessage.ResExpChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 发送给前端,单项个人货币属性数值消息
     *
     * @param player
     * @param currencyType 货币类型
     * @param value        变化的数量
     * @param reasons      理由
     */
    private void sendPlayerCurrencyAttribute(Player player, int currencyType, long value, int reasons) {
        backpackMessage.ResCoinChange.Builder msg = backpackMessage.ResCoinChange.newBuilder();
        msg.setReason(reasons);
        msg.setChangeCoin(buildItemCoinInfo(currencyType, value));
        MessageUtils.send_to_player(player, backpackMessage.ResCoinChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void writeLog(Player player, long beforeNum, long afterNum, long changeNum, long otherNum, int value, long action, int currencyType) {
        switch (currencyType) {
            case ItemCoinType.EXP:
                writeExpLog(player, beforeNum, afterNum, changeNum, value, action, otherNum);
                break;
            case ItemCoinType.GemCoin:
                writeGoldLog(player, (int) beforeNum, changeNum, value, action);
                LogDataManager.instance.onItemChange(player.getCreateServerId(), currencyType,ItemTypeConst.COPPER,"元宝",beforeNum,afterNum);
                break;
            case ItemCoinType.BindGemCoin:
                writeBindGoldLog(player, beforeNum, afterNum, changeNum, value, action, currencyType);
                LogDataManager.instance.onItemChange(player.getCreateServerId(), currencyType,ItemTypeConst.COPPER,"",beforeNum,afterNum);
                break;
            case ItemCoinType.BindMoney:
                writeMoneyLog(player, beforeNum, afterNum, changeNum, value, action, currencyType);
                LogDataManager.instance.onItemChange(player.getCreateServerId(), currencyType,ItemTypeConst.COPPER,"",beforeNum,afterNum);
                break;
            default:
                writeCoinLog(player, beforeNum, afterNum, changeNum, value, action, currencyType);
                LogDataManager.instance.onItemChange(player.getCreateServerId(), currencyType,ItemTypeConst.COPPER,"",beforeNum,afterNum);
                break;
        }
    }

    private void writeGoldLog(Player player, long beforeGold, long changeNum, int reason, long actionId) {
        try {
            GoldChangeLog goldChangeLog = new GoldChangeLog();
            goldChangeLog.setActionId(actionId);
            goldChangeLog.setAfterNum(player.getGold().getReaminGold());
            goldChangeLog.setBeforeNum((int) beforeGold);
            goldChangeLog.setChangeNum((int) changeNum);
            goldChangeLog.setLoginIp(player.getLoginIP());
            goldChangeLog.setReason(reason);
            goldChangeLog.setRoleLevel(player.getLevel());
            goldChangeLog.setPlayer(player);
            LogService.getInstance().execute(goldChangeLog);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    private void writeMoneyLog(Player player, long beforeNum, long afterNum, long changeNum, int reason, long actionId, int moneyType) {
        try {
            if (moneyType == ItemCoinType.BindMoney) {
                MoneyChangeLog moneyChangeLog = new MoneyChangeLog();
                moneyChangeLog.setActionId(actionId);
                moneyChangeLog.setAfterNum(afterNum);
                moneyChangeLog.setBeforeNum(beforeNum);
                moneyChangeLog.setChangeNum(changeNum);
                moneyChangeLog.setLoginIp(player.getLoginIP());
                moneyChangeLog.setReason(reason);
                moneyChangeLog.setRoleLevel(player.getLevel());
                moneyChangeLog.setMoneyType(moneyType);
                moneyChangeLog.setPlayer(player);
                LogService.getInstance().execute(moneyChangeLog);
            } else {
                writeCoinLog(player, beforeNum, afterNum, changeNum, reason, actionId, moneyType);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    private void writeCoinLog(Player player, long beforeNum, long afterNum, long changeNum, int reason, long actionId, int moneyType) {
        try {
            CoinChangeLog coinChangeLog = new CoinChangeLog();
            coinChangeLog.setActionId(actionId);
            coinChangeLog.setRoleLevel(player.getLevel());
            coinChangeLog.setAfterNum(afterNum);
            coinChangeLog.setBeforeNum(beforeNum);
            coinChangeLog.setChangeNum(changeNum);
            coinChangeLog.setLoginIp(player.getLoginIP());
            coinChangeLog.setReason(reason);
            coinChangeLog.setPlayer(player);
            coinChangeLog.setMoneyType(moneyType);
            coinChangeLog.setPlayer(player);
            LogService.getInstance().execute(coinChangeLog);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    private void writeBindGoldLog(Player player, long beforeNum, long afterNum, long changeNum, int reason, long actionId, int moneyType) {
        try {
            BindGoldChangeLog bindGoldChangeLog = new BindGoldChangeLog();
            bindGoldChangeLog.setActionId(actionId);
            bindGoldChangeLog.setAfterNum(afterNum);
            bindGoldChangeLog.setBeforeNum(beforeNum);
            bindGoldChangeLog.setChangeNum(changeNum);
            bindGoldChangeLog.setLoginIp(player.getLoginIP());
            bindGoldChangeLog.setReason(reason);
            bindGoldChangeLog.setPlayer(player);
            bindGoldChangeLog.setMoneyType(moneyType);
            bindGoldChangeLog.setPlayer(player);
            LogService.getInstance().execute(bindGoldChangeLog);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    private void writeExpLog(Player player, long beforeNum, long afterNum, long changeNum, int reason, long actionId, long upLevelExp) {
        //掉落经验达到一定值才记录
        boolean isDrop = reason == ItemChangeReason.DropGet || reason == ItemChangeReason.DropByFightServerGet;
        if (isDrop) {
            long lastexp = player.getLastDropExp();
            lastexp += changeNum;
            if (lastexp > (player.getLevel() * 1000000000L)) {
                player.setLastDropExp(0);
                player.setLastDropTime(TimeUtils.Time());
                DropExpChangeLog dec = new DropExpChangeLog();
                dec.setChangeNum(lastexp);
                dec.setPlayer(player);
                dec.setLastTime((int) (player.getLastDropTime() / 1000));
                dec.setMapId(player.gainMapModelId());
                LogService.getInstance().execute(dec);
            } else {
                player.setLastDropExp(lastexp);
            }
            return;
        }

        try {
            ExpChangeLog expChangeLog = new ExpChangeLog();
            expChangeLog.setActionId(actionId);
            expChangeLog.setAfterNum(afterNum);
            expChangeLog.setBeforeNum(beforeNum);
            expChangeLog.setChangeNum(changeNum);
            expChangeLog.setReason(reason);
            expChangeLog.setRoleLevel(player.getLevel());
            expChangeLog.setUpLevelExp(upLevelExp);
            expChangeLog.setPlayer(player);
            LogService.getInstance().execute(expChangeLog);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    private boolean checkCurrencyType(int currencyType) {
        return currencyType > ItemCoinType.ItemCoinMinId && currencyType < ItemCoinType.ItemCoinMaxId;
    }

    public long getCurrentyAttr(Player player, int type) {
        // 货币属性
        if (ItemCoinType.GemCoin == type) {
            return player.getGold().getReaminGold();
        } else {
            return player.getCurrencys().get(type);
        }
    }

}
