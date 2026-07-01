package common.backpack;

import com.alibaba.druid.util.Base64;
import com.data.*;
import com.data.bean.*;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.backpack.log.CellOpenLog;
import com.game.backpack.log.ItemChangeLog;
import com.game.backpack.script.IBackpackManagerScript;
import com.game.backpack.sort.EquipItemSort;
import com.game.backpack.sort.ItemSort;
import com.game.backpack.structs.*;
import com.game.boss.struct.BossTypeConst;
import com.game.chat.structs.Notify;
import com.game.chum.struct.ChumPrivilege;
import com.game.connectfightserver.manager.ConnectFightManager;
import com.game.cooldown.structs.CooldownTypes;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.count.structs.VariantType;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.home.manager.HomeManager;
import com.game.hook.struct.HookInfo;
import com.game.log.LogDataManager;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.marriage.struct.Marriage;
import com.game.marriage.struct.WeddingMapInfo;
import com.game.marriage.struct.WeddingOperation;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.player.structs.PlayerDefine;
import com.game.player.structs.SavePlayerLevel;
import com.game.recharge.structs.Recharge;
import com.game.recharge.structs.RechargeDefine;
import com.game.recharge.structs.RechargeItemInfo;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.server.social.SocialServerClient;
import com.game.server.structs.ErrorLog;
import com.game.structs.GlobalType;
import com.game.structs.ItemChangeAction;
import com.game.structs.ServerStr;
import com.game.task.structs.Task;
import com.game.team.structs.TeamInfo;
import com.game.utils.MessageUtils;
import game.core.dblog.LogService;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage;
import game.message.MapMessage;
import game.message.backpackMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BackpackManagerScript implements IScript, IBackpackManagerScript {

    private static final Logger log = LogManager.getLogger("BackpackManagerScript");

    @Override
    public int getId() {
        return ScriptEnum.BackpackManagerBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    private boolean onRemoveItemByItem(Player player, Item item, int reasons, long action) {
        if (null == player || null == item || -1 == item.getGridId()) {
            return false;
        }

        player.getBackpackItems().remove(item.getGridId());

        sendItemDelete(player, reasons, item.getId());
        Manager.playerManager.savePlayer(player, SavePlayerLevel.OneMinLater);
        Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
        if (model != null) {
            writeItemLog(player, item.getId(), item.getItemModelId(), item.getNum(),
                    0, reasons, ItemChangeAction.REMOVE, action, 0, 0, item.getGridId());
        }
        return true;
    }

    @Override
    public boolean onRemoveItemByItemNotNotice(Player player, Item item, int reasons, long action) {
        if (null == player || null == item || -1 == item.getGridId()) {
            return false;
        }
        player.getBackpackItems().remove(item.getGridId());
        writeItemLog(player, item.getId(), item.getItemModelId(), item.getNum(),
                0, reasons, ItemChangeAction.REMOVE, action, 0, 0, item.getGridId());
        Manager.playerManager.savePlayer(player, SavePlayerLevel.OneMinLater);
        return true;
    }

    @Override
    public int calWorldLvRate(Player player) {

        int openDay = TimeUtils.getOpenServerDay();
        if (Global.WorldLevelDatNeed >= openDay) {
            return 0;
        }
        if (player.getLevel() < Global.WorldLevelNeed) {
            return 0;
        }
        float worldLvRate = (float) player.getLevel()
                / (player.getLevel() + GlobalType.getWorldLevel() - Global.WorldLevelPram1)
                * (Global.WorldLevelPram3 / 10000f)
                //每级加成经验，万分比数值 / 100
                * (GlobalType.getWorldLevel() - Global.WorldLevelPram2 - player.getLevel());


        worldLvRate = (float) Math.floor(worldLvRate * 100);
        worldLvRate = Math.min(worldLvRate, Global.WorldLevelPram4);
        worldLvRate = Math.max(worldLvRate, 0);
        return (int) worldLvRate;
    }

    @Override
    @Deprecated
    public int calFightPointRate(Player player) {
        Cfg_On_hook_Bean bean = CfgManager.getCfg_On_hook_Container().getValueByKey(player.getLevel());
        if (bean == null) {
            log.info("Cfg_On_hookBean 找不到，id" + player.getLevel());
            return 0;
        }
        int fightRate = (int) ((player.getFightPoint() - bean.getBattle()) * 100 / bean.getBattle() / 2);
        //战力放大倍数（0~0.5）
        fightRate = Math.min(fightRate, 50);
        fightRate = Math.max(0, fightRate);
        return fightRate;
    }

    @Override
    public int calTeamRate(Player player) {
        TeamInfo info = Manager.teamManager.getTeam(player.getTeamId());
        if (info == null) {
            return 0;
        }
        int teamNum = 1;
        for (Long roleId : info.getMembers()) {
            if (roleId == player.getId()) {
                continue;
            }
            Player member = Manager.playerManager.getPlayerOnline(roleId);
            if (member == null) {
                continue;
            }
            if (player.getGuildId() <= 0 || member.getGuildId() <= 0) {
                continue;
            }
            if (player.gainMapId() == member.gainMapId() && player.getGuildId() == member.getGuildId()) {
                teamNum++;
            }
        }
        ReadIntegerArrayEs teamExp = Global.New_team_exp;
        for (int i = 0; i < teamExp.size(); i++) {
            if (teamExp.get(i).get(0) == teamNum) {
                return teamExp.get(i).get(1);
            }
        }
        return 0;
    }

    /**
     * 结婚组队经验加成
     *
     * @param player
     * @return
     */
    public int calMarriageRate(Player player) {
        TeamInfo info = Manager.teamManager.getTeam(player.getTeamId());
        if (info == null) {
            return 0;
        }
        if (player.getMarriageUid() <= 0) {
            return 0;
        }
        Marriage marriage = Manager.marriageManager.getMarriageList().get(player.getMarriageUid());
        if (marriage == null) {
            return 0;
        }
        for (Long roleId : info.getMembers()) {
            if (roleId == player.getId()) {
                continue;
            }
            if (marriage.getBeMarriageId() == roleId || roleId == marriage.getMarriageId()) {
                return Global.Marry_team_exp_add;
            }
        }
        return 0;
    }

    @Override
    public boolean canDeleteItemNum(Player player, ReadIntegerArrayEs arrays, int rate) {
        if (arrays == null || arrays.isEmpty()) {
            return false;
        }
        boolean result = true;
        for (ReadArray<Integer> array : arrays.getValuees()) {
            if (array.size() < 2) {
                result = false;
                break;
            }
            result &= canDeleteItemNum(player, array.get(0), array.get(1) * rate);
        }
        return result;
    }

    @Override
    public boolean canDeleteItemNumList(Player player, ArrayList<Integer[]> itemlist) {
        for (Integer[] integers : itemlist) {
            if (!canDeleteItemNum(player, integers[0], integers[1])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canDeleteItemNum(Player player, int itemModelId, int num) {
        if (null == player || 0 > num) {
            return false;
        }
        Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(itemModelId);
        if (null == model) {
            return false;
        }

        if (model.getType() == ItemTypeConst.COPPER) {//货币类型
            if (itemModelId == ItemCoinType.GemCoin) {
                return Manager.currencyManager.manager().canRemoveGold(player, num);
            } else {
                return Manager.currencyManager.manager().canDecItemCoin(player, num, itemModelId);
            }
        } else {//物品类型

            return getItemNum(player, itemModelId) >= num && num > 0;
        }
    }

    /**
     * 获得物品数量
     *
     * @param player      玩家
     * @param itemModelId 物品模板id
     * @return 物品数量
     */
    public int getItemNum(Player player, int itemModelId) {
        Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(itemModelId);
        if (model.getType() == ItemTypeConst.COPPER) {
            return Manager.currencyManager.manager().getCurrencyIntNum(player, itemModelId);
        }
        int num = 0;
        // 遍历物品
        for (Item item : player.getBackpackItems().values()) {
            if (item.getItemModelId() == itemModelId && !item.haveLost()) {
                num += item.getNum();
            }
        }
        return num;
    }

    /**
     * 发送背包物品消息
     *
     * @param player 玩家
     */
    public void sendItemInfos(Player player) {
        backpackMessage.ResItemInfos.Builder msg = backpackMessage.ResItemInfos.newBuilder();
        msg.setCellnum(player.getBagCellsNum());
        msg.setCellTime(player.getBagCellTimeCount());
        ConcurrentHashMap<Integer, Item> backpack = player.getBackpackItems();
        List<Equip> equips = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        sortItems(backpack, equips, items, player.getCareer());
        int i = 0;
        Integer index = 0;
        for (; i < equips.size(); i++) {
            Equip equip = equips.get(i);
            index = i + 1;
            backpackMessage.ItemInfo.Builder info = addItemInfoToList(index, equip, backpack, player);
            msg.addItemInfoList(info);
        }

        for (int j = 0; j < items.size(); j++) {
            Item item = items.get(j);
            index = ++i;
            backpackMessage.ItemInfo.Builder info = addItemInfoToList(index, item, backpack, player);
            msg.addItemInfoList(info);
        }
        MessageUtils.send_to_player(player, backpackMessage.ResItemInfos.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 上线或者整理背包时，对重新排序后的物品的处理
     * 1、有需要的话，调整物品在背包的位置
     */
    public backpackMessage.ItemInfo.Builder addItemInfoToList(Integer index, Item item, ConcurrentHashMap<Integer, Item> backpack, Player player) {
        Integer gridId = item.getGridId();
        if (!index.equals(gridId)) {
            /**
             * 由于此时item所在的格子可能已经换成别的item了
             * 所以需要对item和格子上的item进行比较，是同一个item，才能删除，否则不能删除
             * */
            Item tempItem = backpack.get(gridId);
            if (null != tempItem && item.getId() == tempItem.getId()) {
                backpack.remove(gridId);
            }
            item.setGridId(index);
            backpack.put(index, item);
        }
        return makeItemInfoBuilder(item);
    }

    /**
     * 获取指定绑定属性的数量
     *
     * @param player      玩家
     * @param itemModelId 物品配置表id
     * @param bind        绑定状态
     * @return 结果
     */
    public int getItemNum(Player player, int itemModelId, boolean bind) {
        int num = 0;
        try {
            Iterator<Item> iter = player.getBackpackItems().values().iterator();
            // 遍历物品
            while (iter.hasNext()) {
                Item item = iter.next();
                if (item.getItemModelId() == itemModelId && !item.haveLost() && item.isBind() == bind) {
                    num += item.getNum();
                }
            }
        } catch (Exception e) {
            log.error(e, e);
        }
        return num;
    }

    /**
     * 检查所有的道具或货币是否足够
     */
    private boolean checkItemsAndCurrenciesIsEnough(Player player, ReadIntegerArrayEs items, int reason) {
        boolean checkResult = true;
        if (items.size() == 0) {
            checkResult = false;
        }
        for (ReadArray<Integer> aii : items.getValuees()) {
            if (aii.size() < 2) {
                checkResult = false;
            }
            Integer itemModelId = aii.get(0);
            Integer num = aii.get(1);
            if (!canDeleteItemNum(player, itemModelId, num)) {
                checkResult = false;
            }
        }
        return checkResult;
    }

    @Override
    public boolean removeItemByCellId(Player player, int cellId, int reasons, long action) {
        return onRemoveItemByItem(player, this.getItemByCellId(player, cellId), reasons, action);
    }

    @Override
    public boolean removeItemOrCurrencies(Player player, ReadIntegerArrayEs items, long actionId, int reason) {
        boolean result = true;
        if (!checkItemsAndCurrenciesIsEnough(player, items, reason)) {
            return false;
        }
        for (ReadArray<Integer> aii : items.getValuees()) {
            int itemModelId = aii.get(0);
            int num = aii.get(1);
            result &= onRemoveItem(player, itemModelId, num, reason, actionId);
        }
        return result;
    }

    @Override
    public boolean removeItemOrCurrency(Player player, int itemModelId, int num, long actionId, int reason) {
        Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(itemModelId);
        if (model.getType() == ItemTypeConst.COPPER) {
            return Manager.currencyManager.manager().onDecItemCoin(player, num, reason, actionId, itemModelId);
        } else {
            return onRemoveItem(player, itemModelId, num, reason, actionId);
        }
    }

    /**
     * 批量添加 货币消耗
     *
     * @param player   玩家
     * @param items    物品
     * @param reasons  原因
     * @param action   日志id
     * @param costType 消耗货币类型
     * @param costNum  消耗数量
     * @return 结果
     */
    public boolean addItems(Player player, List<Item> items, int reasons, long action, int costType, float costNum) {
        if (items == null) {
            log.error("不应该走到这里，传入物品列表为空");
            return false;
        }
        if (items.isEmpty()) {
            return true;
        }

        if (GameServer.getInstance().IsFightServer()) {
            Manager.crossServerManager.getCrossServer().sendReward(player, items, reasons);
            return true;
        }

        if (0 != onHasAddSpaces(player, items)) {
            return false;
        }
        //TODO 2020年8月14日 新增活动道具通知合并
        HashMap<Integer, Integer> sumNotify = new HashMap<>();

        float eachCost = costNum / items.size();
        for (Item item : items) {
            item.setGridId(-1);
            int count = item.getNum();
            boolean state = onAddItem(player, item, reasons, action, costType, eachCost, false);
            if (state) {
                sumNotify.put(item.getItemModelId(), sumNotify.getOrDefault(item.getItemModelId(), 0) + count);
            }
        }
        //TODO 2020年8月14日 道具通知 ————合并
        for (Map.Entry<Integer, Integer> item : sumNotify.entrySet()) {
            Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(item.getKey());
            if (model.getType() == ItemTypeConst.TITLE || model.getType() == ItemTypeConst.XiSui || model.getType() == ItemTypeConst.COPPER) {
                continue;
            }
            if (item.getValue() <= 0) {
                continue;
            }
            MessageUtils.notify_player(player, Notify.CHAT, MessageString.GetEquipsTips, getName(item.getKey()), item.getValue());
        }
        return true;
    }

    @Override
    public boolean addItems(Player player, List<Item> items, int reasons, long action) {
        return addItems(player, items, reasons, action, 0, 0);
    }

    @Override
    public void addOrSendItems(Player player, List<Item> items, int reasons, long action) {
        boolean success = addItems(player, items, reasons, action, 0, 0);
        if (!success && items != null && items.size() > 0) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, reasons, action);
        }
    }

    /**
     * 成功开启格子
     *
     * @param player     角色
     * @param cellId     开启的格子
     * @param actionType 1 元宝开启 0时间开启
     * @param costGold   扣除的元宝数量
     * @param actionId   日志id
     */
    public void openBagCellSuccess(Player player, int cellId, byte actionType, int costGold, long actionId) {
        int beforeCellId = player.getBagCellsNum();
        player.setBagCellsNum(cellId);
        player.setBagCellTimeCount(0);
        Manager.controlManager.operate(player, FunctionVariable.OpenBackpackNum, 0);

        //通知客户端
        backpackMessage.ResOpenBagCellSuccess.Builder msg = backpackMessage.ResOpenBagCellSuccess.newBuilder();
        msg.setNowCellId(cellId);
        MessageUtils.send_to_player(player, backpackMessage.ResOpenBagCellSuccess.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        //写log
        writeOpenCellLog(player, actionType, beforeCellId, costGold, actionId);
    }

    /**
     * 如果有绑定的同类物品，先删除绑定的
     */
    @Override
    public boolean onRemoveItem(Player player, int itemModelId, int num, int reasons, long action) {
        if (num <= 0) {
            return false;
        }

        Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(itemModelId);
        if (null == model) {
            return false;
        }
        //货币类型物品
        if (model.getType() == ItemTypeConst.COPPER) {
            return Manager.currencyManager.manager().onDecItemCoin(player, num, reasons, action, itemModelId);
        }

        if (getItemNum(player, itemModelId) < num) {
            return false;
        }

        int tempNumber = num;
        int bindItemNum = getItemNum(player, itemModelId, true);
        int itemNum = getItemNum(player, itemModelId, false);
        if (bindItemNum + itemNum < num) {
            log.error("正常不会执行到这部 检查代码", new Exception());
            return false;
        }

        long actionId = action != 0 ? action : IDConfigUtil.getLogId();
        if (bindItemNum > 0) {
            if (bindItemNum >= tempNumber) {
                onRemoveItem(player, itemModelId, tempNumber, true, reasons, actionId);
                tempNumber = 0;
            } else {
                onRemoveItem(player, itemModelId, bindItemNum, true, reasons, actionId);
                tempNumber -= bindItemNum;
            }
        }

        if (itemNum > 0 && tempNumber > 0) {
            if (itemNum >= tempNumber) {
                onRemoveItem(player, itemModelId, tempNumber, false, reasons, actionId);
            } else {
                return false;
            }
        }

        Manager.playerManager.savePlayer(player, SavePlayerLevel.FiveMinLater);
        return true;
    }

    @Override
    public boolean onRemoveItem(Player player, int itemModelId, int num, boolean bind, int reasons, long action) {
        if (getItemNum(player, itemModelId, bind) < num) {
            // 数量不足
            return false;
        }
        Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(itemModelId);
        if (null == model) {
            return false;
        }

        long actionId = action != 0 ? action : IDConfigUtil.getLogId();
        List<Item> sortList = new ArrayList<>();
        for (int i = player.getBagCellsNum(); i >= 1 && num > 0; i--) {
            Item item = player.getBackpackItems().get(i);
            if (item == null) {
                continue;
            }

            if (item.isBind() == bind && item.getItemModelId() == itemModelId && !item.haveLost()) {
                sortList.add(item);
            }
        }
        //使用的没有时限的道具数量
        int noLostTimeNum = 0;
        // 有时限未过期物品→绑定物品→非绑定物品
        Collections.sort(sortList);
        Iterator<Item> it = sortList.iterator();
        while (it.hasNext() && num > 0) {
            Item item = it.next();
            if (item.getNum() <= num) {
                onRemoveItem(player, item, item.getNum(), reasons, actionId);
                num = num - item.getNum();
                if (item.getLosttime() == 0) {
                    noLostTimeNum += item.getNum();
                }
            } else {
                int beforeNum = item.getNum();
                int changeNum = num;
                item.setNum(item.getNum() - num);
                if (item.getLosttime() == 0) {
                    noLostTimeNum += num;
                }
                num = 0;
                sendItemChange(player, ItemChangeReason.ComBin, item);
                writeItemLog(player, item.getId(), itemModelId, beforeNum,
                        item.getNum(), reasons, ItemChangeAction.CHANGE, actionId, 0, 0, item.getGridId());
            }
        }
        Manager.playerManager.savePlayer(player, SavePlayerLevel.FiveMinLater);
        return true;
    }

    @Override
    public boolean onRemoveItem(Player player, Item item, int num, int reasons, long action) {
        if (item.getNum() < num) {
            //数量不足
            log.error("物品数量不足外部检查出错", new Exception());
            return false;
        }
        Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
        if (model == null) {
            log.error("玩家(" + player.getId() + ")模型:" + item.getItemModelId() + "任务物品加入包裹开始检查失败找不到物品的模型物品ID" + item.getId());
            return false;
        }

        //todo  测试用 yuanyin
        // ChatMessage.ChatResSC.Builder msg = Manager.chatManager.MakeChatResBuilder(player, "onRemoveItem 物品-->id:"+item.getItemModelId() + " name:"+ model.getName() + " reason:"+ reasons, 0,ChatManager.CHATTYPE_WORD, Manager.playerManager.getPlayerWorldInfo(player.getId()));
        //MessageUtils.send_to_player(player, ChatMessage.ChatResSC.MsgID.eMsgID_VALUE, msg.build().toByteArray());


        long actionId = action != 0 ? action : IDConfigUtil.getLogId();
        //圣装扣除判断
        if (model.getType() == ItemTypeConst.HolyEuiqp) {
            boolean res = Manager.holyEquipManager.deal().delHolyEquip(player, item, reasons);
            if (res) {
                writeItemLog(player, item.getId(), item.getItemModelId(), item.getNum(), 0, reasons, ItemChangeAction.REMOVE, actionId, 0, 0, 0);
            }
            return res;
        }
        //幻装扣除判断
        if (model.getType() == ItemTypeConst.UNREAL_EQUIP) {
            boolean res = Manager.unrealEquipManager.deal().delHolyEquip(player, item, reasons);
            if (res) {
                writeItemLog(player, item.getId(), item.getItemModelId(), item.getNum(), 0, reasons, ItemChangeAction.REMOVE, actionId, 0, 0, 0);
            }
            return res;
        }
        //仙甲扣除判断
        if (model.getType() == ItemTypeConst.IMM_EQUIP) {
            boolean res = Manager.immortalEquipManager.manager().delImmEquip(player, item, reasons);
            if (res) {
                writeItemLog(player, item.getId(), item.getItemModelId(), item.getNum(), 0, reasons, ItemChangeAction.REMOVE, actionId, 0, 0, 0);
            }
            return res;
        }
        boolean result;
        if (item.getNum() == num) {
            Item item1 = getItemByCellId(player, item.getGridId());
            result = onRemoveItemByItem(player, item1, reasons, action);
        } else {
            int beforeNum = item.getNum();
            item.setNum(item.getNum() - num);
            result = true;
            sendItemChange(player, reasons, item);
            writeItemLog(player, item.getId(), item.getItemModelId(), beforeNum,
                    item.getNum(), reasons, ItemChangeAction.CHANGE, actionId, 0, 0, item.getGridId());
        }
        Manager.playerManager.savePlayer(player, SavePlayerLevel.FiveMinLater);
        return result;
    }

    @Override
    public boolean onRemoveItemNotNoticeClinet(Player player, Item item, int num, int reasons, long action, Set<Long> deleteList, Set<Long> changeList) {
        if (item.getNum() < num) {
            //数量不足
            log.error("物品数量不足外部检查出错", new Exception());
            return false;
        }
        boolean result;
        long actionId = action != 0 ? action : IDConfigUtil.getLogId();
        if (item.getNum() == num) {
            Item item1 = getItemByCellId(player, item.getGridId());
            result = this.onRemoveItemByItemNotNotice(player, item1, reasons, action);
            if (result) {
                deleteList.add(item1.getId());
            }
        } else {
            int beforeNum = item.getNum();
            item.setNum(item.getNum() - num);
            result = true;
            changeList.add(item.getId());
            writeItemLog(player, item.getId(), item.getItemModelId(), beforeNum,
                    item.getNum(), reasons, ItemChangeAction.CHANGE, actionId, 0, 0, item.getGridId());
        }
//        for (int i = 0; i < num; i++) {
//            boolean useItem = Manager.activityManager.holidayDeal().holidayActivityAction(player, ActivityType.HOLIDAY_HONOR_ACTION, ActionType.HONOR_ACTION_USE_ITEM, item.getItemModelId());
//        }
        Manager.playerManager.savePlayer(player, SavePlayerLevel.FiveMinLater);
        int item_level = item.getItemModelId();
        Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
        if (model != null) {
            item_level = model.getColor();
        }
//        Manager.biManager.getScript().biItem(player, item.getId(), ItemTypeConst.EQUIP, item_level, item.getItemModelId(), Manager.backpackManager.manager().getName(item.getItemModelId()), 1, 0, reasons, actionId, 0, player.getCurGps().getModelId());
        return result;
    }

    @Override
    public boolean onAddItem(Player player, Item item, int reason, long action, int costType, float costNum, boolean needMergeFlag) {
        //用于记录关联操作
        action = action != 0 ? action : IDConfigUtil.getLogId();
        Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
        if (model == null) {
            log.error("玩家(" + player.getId() + ")模型:" + item.getItemModelId() + "任务物品加入包裹开始检查失败找不到物品的模型物品ID" + item.getId());
            return false;
        }

        if (model.getBind() == 1) {
            item.setBind(true);
        }
        // 数据检查
        if (item.getNum() <= 0) {
            log.error("玩家(" + player.getId() + ")非法参数物品数量 为0");
            return false;
        }

        //todo  测试用 yuanyin
        //ChatMessage.ChatResSC.Builder msg = Manager.chatManager.MakeChatResBuilder(player, "物品-->id:"+item.getItemModelId() + " name:"+ model.getName() + " reason:"+ reason, 0,ChatManager.CHATTYPE_WORD, Manager.playerManager.getPlayerWorldInfo(player.getId()));
        //MessageUtils.send_to_player(player, ChatMessage.ChatResSC.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        //货币类型物品
        if (model.getType() == ItemTypeConst.COPPER) {
            return Manager.currencyManager.manager().onAddItemCoin(player, item.getItemModelId(), item.realNum(), reason, action);
        }

        int num = item.getNum();
        if (num > model.getMax()) {
            log.error("玩家(" + player.getId() + ")物品数量超过了最大可堆叠数量ID:" + item.getId() + " 模型:" + item.getItemModelId());
            return false;
        }
        //魂兽装备
        if (model.getType() == ItemTypeConst.SoulBeastEquip || model.getType() == ItemTypeConst.SoulBeastItem) {
            boolean res = Manager.soulBeastManager.deal().addSoulBeastEquip(player, item, reason, action);
            if (res) {
                writeItemLogAndBI(player, 0, item.getNum(), item, reason, action);
            }
            return res;
        }

        //圣装
        if (model.getType() == ItemTypeConst.HolyEuiqp) {
            boolean res = Manager.holyEquipManager.deal().addHolyEquip(player, item, reason);
            if (res) {
                writeItemLogAndBI(player, 0, item.getNum(), item, reason, action);
            }
            return res;
        }
        //幻装
        if (model.getType() == ItemTypeConst.UNREAL_EQUIP) {
            boolean res = Manager.unrealEquipManager.deal().addUnrealEquip(player, item, reason);
            if (res) {
                writeItemLogAndBI(player, 0, item.getNum(), item, reason, action);
            }
            return res;
        }

        //仙甲
        if (model.getType() == ItemTypeConst.IMM_EQUIP) {
            boolean res = Manager.immortalEquipManager.manager().addImmEquip(player, item, reason);
            if (res) {
                writeItemLogAndBI(player, 0, item.getNum(), item, reason, action);
            }
            return res;
        }

        //灵魄
        if (model.getType() == ItemTypeConst.SoulItem) {
            boolean res = Manager.immortalSoulManager.manager().addSoul(player, item, reason);
            return res;
        }

        //宠物装备
        if (model.getType() == ItemTypeConst.PET_EQUIP) {
            boolean res = Manager.petManager.deal().addPetEquip(player, item, reason);
            return res;
        }

        //坐骑装备
        if (model.getType() == ItemTypeConst.HORSE_EQUIP) {
            boolean res = Manager.horseManager.deal().addHorseEquip(player, item, reason, action);
            return res;
        }

        //魂印
        if (model.getType() == ItemTypeConst.Soul_EQUIP) {
            boolean res = Manager.soulArmorManager.script().addSoulArmorEquip(player, item, reason);
            return res;
        }
        //魔魂装备
        if (model.getType() == ItemTypeConst.DEVIL_EQUIP) {
            boolean res = Manager.devilSeriesManager.getScript().addDevilEquip(player, item, reason, action);
            return res;
        }

        //自动使用的称号不进背包
        if (model.getType() == ItemTypeConst.TITLE) {
            ReadArray<Integer> title = model.getEffect_num().get(0);
            Cfg_Title_Bean titleBean = CfgManager.getCfg_Title_Container().getValueByKey(title.get(1));
            if (titleBean != null && titleBean.getActivation() == 1) {
                Manager.titleManager.deal().useTitleItem(player, titleBean.getId(), item.getNum(), reason);
                return true;
            }
        }

        // 转职洗髓道具
        if (model.getType() == ItemTypeConst.XiSui) {
            Manager.playerManager.xiSuiScript().useItem(player, item.getItemModelId(), item.getNum(), reason, action);
            return true;
        }

        //取消重复检查
//        if (!onHasAddSpace(player, item)) {
//            log.error("玩家(" + player.getId() + ")不应该走到这里，外部包裹检查出错", new Exception());
//            return false;
//        }

        int gridId = getBackpackAbleAddGridId(player, item);
        if (-1 == gridId) {
            log.error("玩家(" + player.getId() + ")这一步就不应该找不到位置了 如果执行到这里说明上方的检查方法出错", new Exception());
            return false;
        }

        int oldNum = 0;
        int afterNum = 0;
        //不可堆叠物品
        if (model.getMax() == 1) {
            if (item.getGridId() <= 0) {
                item.setGridId(gridId);
            }
            player.getBackpackItems().put(item.getGridId(), item);

            if (!isResolve(player, item, model)) {
                sendItemAdd(player, reason, item);
            }
            afterNum = item.getNum();
            writeItemLog(player, item.getId(), item.getItemModelId(), oldNum, item.getNum(), reason, ItemChangeAction.ADD, action, costType, costNum, item.getGridId());
        } else {
            //可堆叠物品
            float eachItemGoldNum = costNum / num;
            for (int i = 1; i <= player.getBagCellsNum() && num > 0; i++) {
                Item target = player.getBackpackItems().get(i);
                if (target == null) {
                    //优先找同类可堆叠的物品
                    continue;
                }
                if (!checkMerge(item, target)) {
                    continue;
                }
                if (target.getNum() >= model.getMax()) {
                    continue;
                }
                oldNum = target.getNum();//log用
                boolean changeFlag = false;
                long ablenum = model.getMax() - target.getNum();
                if (num + target.getNum() <= model.getMax()) {
                    target.setNum(target.getNum() + num);
                    num = 0;
                    changeFlag = true;
                } else if (needMergeFlag) {
                    num -= ablenum;
                    target.setNum((int) model.getMax());
                    changeFlag = true;
                }
                if (changeFlag) {
                    /* start
                     *  用于修复gridId被重置为-1
                     *  被重置的原因：同一对象被addItems()方法执行两次(即一个物品被多次添加到背包)时，可能出现
                     */
                    if (target.getGridId() != i) {
                        target.setGridId(i);
                    }
                    //end
                    sendItemChange(player, reason, target);
                    float expendGoldNum = eachItemGoldNum * (target.getNum() - oldNum);
                    afterNum = target.getNum();
                    writeItemLog(player, target.getId(), item.getItemModelId(), oldNum, target.getNum(),
                            reason, ItemChangeAction.CHANGE, action, costType, expendGoldNum, target.getGridId());
                }
            }

            /**
             * 在以前的格子堆叠后，还有剩余的物品，需要找新的格子放置
             * huangzhaomin comment by 2019-04-10
             * */
            if (num > 0) {
                int backpackAbleAddGridId = getBackpackAbleAddGridId(player, item);
                if (backpackAbleAddGridId == -1) {
                    log.error("这一步不应该获取不到位置的", new Exception());
                    return false;
                }
                item.setGridId(backpackAbleAddGridId);
                item.setNum(num);
                oldNum = 0;
                player.getBackpackItems().put(item.getGridId(), item);
                if (!isResolve(player, item, model)) {
                    sendItemAdd(player, reason, item);
                }
                afterNum = item.getNum();
                writeItemLog(player, item.getId(), item.getItemModelId(), oldNum,
                        item.getNum(), reason, ItemChangeAction.ADD, action, costType, eachItemGoldNum * item.getNum(), item.getGridId());
            }
        }

        //检测装备自动售卖和自动熔炼
        if (item instanceof Equip) {
            List<Long> ids = new ArrayList<>();
            ids.add(item.getId());
            //自动熔炼
            if (player.isAutoRecycle()) {
                Manager.recycleManager.deal().autoRecycle(player, (Equip) item);
            }
        }
        int emptyGridNum = getEmptyGridNum(player);
        if (emptyGridNum <= Global.Bag_Not_Enough) {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.BackGridNumLessNotice, emptyGridNum + "");
        }
        Manager.playerManager.savePlayer(player, SavePlayerLevel.OneMinLater);


        //通知客户端分享
//        Manager.shareManager.deal().sendShareNotice(player, ShareType.SHARE_GET_EQUIP, item.getItemModelId() + "");

//        pushCollectLog(player, item.getItemModelId(), num);
        return true;
    }

    @Override
    public boolean onHasAddSpace(Player player, Item item) {
        Cfg_Item_Bean q_itemBean = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
        if (q_itemBean == null) {
            log.error("找不到的物品模型ID" + item.getItemModelId(), new NullPointerException());
            return false;
        }
        if (q_itemBean.getType() == ItemTypeConst.COPPER) {
            // 如果是货币，直接就可以添加
            return true;
        }
        List<Item> tmp = new ArrayList<>();
        tmp.add(item);
        return onHasAddSpaces(player, tmp) == 0;
    }

    @Override
    public int onHasAddSpaces(Player player, Collection<Item> items) {
        if (items.size() <= 0) {
            return 0;
        }
        //正常背包的流程
        int needBackpackGridNum = 0;
        int needSoulBeastGridNum = 0;
        List<Item> holyItemList = new ArrayList<>();
        List<Item> soulItemList = new ArrayList<>();
        List<Item> immItemList = new ArrayList<>();
        List<Item> soulEquipList = new ArrayList<>();
        List<Item> urealEuiqpList = new ArrayList<>();
        //魔魂
        int needDevilSoulGridNum = 0;
        //坐骑脉轮
        int horseEquipNum = 0;
        //宠物装备
        int petEquipNum = 0;

        for (Item item : items) {
            if (Manager.currencyManager.manager().isCoinItem(item.getItemModelId())) {
                //如果货币已经达到了上限， 也返回失败
                if (!Manager.currencyManager.manager().canAddCurrency(player, item.getItemModelId(), item.realNum())) {
                    return MessageString.COINADDOVER;
                }
                continue; //货币不占背包格子的
            }
            Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
            //魂兽背包的检查
            if (itemBean.getType() == ItemTypeConst.SoulBeastEquip || itemBean.getType() == ItemTypeConst.SoulBeastItem) {
                ++needSoulBeastGridNum;
                continue;
            }
            if (itemBean.getType() == ItemTypeConst.HolyEuiqp) {
                holyItemList.add(item);
                continue;
            }
            if (itemBean.getType() == ItemTypeConst.SoulItem) {
                soulItemList.add(item);
                continue;
            }

            if (itemBean.getType() == ItemTypeConst.IMM_EQUIP) {
                immItemList.add(item);
                continue;
            }
            if (itemBean.getType() == ItemTypeConst.Soul_EQUIP) {
                soulEquipList.add(item);
                continue;
            }
            if (itemBean.getType() == ItemTypeConst.UNREAL_EQUIP) {
                urealEuiqpList.add(item);
            }
            if (itemBean.getType() == ItemTypeConst.DEVIL_EQUIP) {
                needDevilSoulGridNum++;
                continue;
            }
            if (itemBean.getType() == ItemTypeConst.HORSE_EQUIP) {
                horseEquipNum++;
                continue;
            }
            if (itemBean.getType() == ItemTypeConst.PET_EQUIP) {
                petEquipNum++;
                continue;
            }
            ++needBackpackGridNum;
        }
        if (needSoulBeastGridNum > 0 && Manager.soulBeastManager.getEmptyGridNum(player) < needSoulBeastGridNum) {
            return MessageString.Soul_Beast_Bag_Not_Enough;
        }
        if (holyItemList.size() > 0) {
            if (!Manager.holyEquipManager.deal().canAddHolyEquipBag(player, holyItemList)) {
                return MessageString.HolyBigIsNotEnough;
            }
        }
        if (urealEuiqpList.size() > 0) {
            if (!Manager.unrealEquipManager.deal().canAddUnrealEquipBag(player, urealEuiqpList)) {
                return MessageString.NoBagCell;
            }
        }
        if (soulItemList.size() > 0) {
            if (!Manager.immortalSoulManager.manager().canAddSoulBag(player, soulItemList)) {
                return MessageString.ImmBagIsFullSoResolve;
            }
        }
        if (immItemList.size() > 0) {
            if (!Manager.immortalEquipManager.manager().canAddImmEquipBag(player, immItemList)) {
                return MessageString.NoBagCell;
            }
        }
        if (soulEquipList.size() > 0) {
            if (!Manager.soulArmorManager.script().canAdd(player, soulEquipList)) {
                return MessageString.NoBagCell;
            }
        }
        if (needDevilSoulGridNum > 0) {
            if (!Manager.devilSeriesManager.getScript().canAdd(player, needDevilSoulGridNum)) {
                return MessageString.DEVIL_SOUL_BAG_NOT_ENOUGH;
            }
        }
        if (horseEquipNum > 0) {
            if (!Manager.horseManager.deal().canAdd(player, horseEquipNum)) {
                return MessageString.MOUNT_EQUIP_BAG_NOT_ENOUGH;
            }
        }
        if (petEquipNum > 0) {
            if (!Manager.petManager.deal().canAdd(player, petEquipNum)) {
                return MessageString.PET_EQUIP_BAG_NOT_ENOUGH;
            }
        }
        if (getEmptyGridNum(player) < needBackpackGridNum) {
            return MessageString.NoBagCell;
        }
        return 0;
    }

    /**
     * 判断两个物品属性是否一至 可合并 忽略 最大数量
     *
     * @param item1 物品1
     * @param item2 物品2
     * @return 结果
     */
    private boolean checkMerge(Item item1, Item item2) {
        Cfg_Item_Bean sourcemodel = CfgManager.getCfg_Item_Container().getValueByKey(item1.getItemModelId());
        Cfg_Item_Bean targetmodel = CfgManager.getCfg_Item_Container().getValueByKey(item2.getItemModelId());

        if (sourcemodel == null || targetmodel == null) {
            log.info(item1 + " " + item2 + "物品模型找不到", new Exception());
            return false;
        }
        if (sourcemodel != targetmodel) {
            //模型不一致不能合并
            return false;
        }

        if (sourcemodel.getMax() <= 1) {
            //不可叠加的物品
            return false;
        }

        if (item1.isBind() != item2.isBind()) {
            //绑定属性不一至 不能合并
            return false;
        }

        return item1.getLosttime() == item2.getLosttime();
    }

    @Override
    public boolean useItem(Player player, long itemId, int num, int index) {
        Item item = getItemById(player, itemId);
        if (item == null) {
            return false;
        }
        Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
        if (model == null) {
            return false;
        }

        boolean useFlag = false;
        boolean batchUseFlag = false;
        for (int i = 0; i < model.getButton_type().size(); i++) {
            if (2 == model.getButton_type().get(i)) {
                useFlag = true;
                //策划要求多选一宝箱在单次使用时可以支持批量
                if (model.getType() == ItemTypeConst.SLECTGIFT) {
                    batchUseFlag = true;
                }
            } else if (3 == model.getButton_type().get(i)) {
                batchUseFlag = true;
            }
        }
        //校验是否能够使用
        if (!useFlag && !batchUseFlag) {
            return false;
        }
        //校验是否能够批量使用
        if (num > 1 && !batchUseFlag) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.NoUseMultiItem);
            return false;
        }

        if (num < 0) {
            return false;
        }

        if (num > 1 && model.getMax() <= 1) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.NoUseMultiItem);
            return false;
        }

        if (num > item.getNum()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.NotEnounghItem);
            return false;
        }

        if (player.isDie()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.PlayerDieNoUseItem);
            return false;
        }
        //未在包裹中
        if (item.getGridId() == -1) {
            return false;
        }

        // 性别检查
        if (model.getSex() != PlayerDefine.SEX_ALL && model.getSex() != player.getSex()) {
            if (model.getSex() == PlayerDefine.SEX_MAN) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.SexLimitMan);
            } else {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.SexLimitWomanMan);
            }
            return false;
        }

        //职业检查
        if (!model.getOccupation().contains(PlayerDefine.CAREER_All) && !model.getOccupation().contains((int) player.getCareer())) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.UseItemCareerLimit);
            return false;
        }

        //等级处理
        if (player.getLevel() < model.getLevel()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.LevelNotEnough, "", String.valueOf(model.getLevel()),
                    "", (model.getMax_level() > 0 ? String.valueOf(model.getMax_level()) : String.valueOf(Global.PlayerMaxLevel)));//在跨服战场中不能使用此物品
            return false;
        }

        //等级处理
        if (model.getMax_level() > 0 && player.getLevel() > model.getMax_level()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.LevelNotEnough, "", String.valueOf(model.getLevel()), "", String.valueOf(model.getMax_level()));//在跨服战场中不能使用此物品
            return false;
        }

        if (item.haveLost()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ItemOverDue);
            return false;
        }

        //获取物品实际能使用的数量
        num = getRallyUseNum(player, model, num);
        //是否达到使用物品数量上限
        int canuse = model.getDaily_max_use();
        if (canuse > 0) {
            long alreadynum = Manager.countManager.getCount(player, BaseCountType.ITEM_USE, item.getItemModelId());
            if (alreadynum < canuse) {
                if (num > canuse - (int) alreadynum) {
                    num = canuse - (int) alreadynum;
                }
            } else {
                log.error(player.getName() + "(" + player.getId() + ")使用物品" + model.getName() + "- " + item.getItemModelId() + " 达到使用上限:" + alreadynum);
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.UseMaxCountLimit);
                return false;
            }
        }

        //是否达到总使用物品数量上限
        int canAlluse = model.getMax_use();
        if (canAlluse > 0) {
            long alreadynum = Manager.countManager.getCount(player, BaseCountType.ITEM_AllUSE, item.getItemModelId());
            if (alreadynum < canAlluse) {
                if (num > canAlluse - (int) alreadynum) {
                    num = canAlluse - (int) alreadynum;
                }
            } else {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.UseMaxCountLimit);
                return false;
            }
        }

        if (model.getBind() == 2) {
            item.setBind(true);
        }

        long actionId = IDConfigUtil.getLogId();
        for (ReadArray<Integer> aii : model.getEffect_num().getValuees()) {
            int type = aii.get(0);
            if (type == ItemEffectType.RefreshBoss || type == ItemEffectType.CallBoss) {
                int bossType = aii.get(1);
                if (bossType == BossTypeConst.SOULANIMAL_BOSS) {
                    sendUseItem2Fight(player, item, num, true);
                    return false;
                }
            }
            if (type == ItemEffectType.AddDailyCount) {
                sendUseItem2Fight(player, item, num, false);
            }
        }
        boolean isUse = item.use(player, num, index, actionId);
        if (isUse) {
            //TODO:这里需要检查合成时的逻辑是否正确
            if (canuse > 0) {
                Manager.countManager.addCount(player, BaseCountType.ITEM_USE, item.getItemModelId(), Count.RefreshType.CountType_Day, num);
            }
            if (canAlluse > 0) {
                Manager.countManager.addCount(player, BaseCountType.ITEM_AllUSE, item.getItemModelId(), Count.RefreshType.CountType_Forever, num);
            }
        }
        return isUse;
    }

    void sendUseItem2Fight(Player player, Item item, int num, boolean cost) {
        if (!GameServer.getInstance().IsFightServer()) {
            if (player.playerCrossData.isToFightServer()) {
                CrossServerMessage.G2FReqCrossUseItem.Builder builder = CrossServerMessage.G2FReqCrossUseItem.newBuilder();
                builder.setItemId(item.getId());
                builder.setModelId(item.getItemModelId());
                builder.setNum(num);
                builder.setRoleId(player.getId());
                builder.setCost(cost ? 1 : 0);
                ConnectFightManager.GetInstance().send_to_fight(player.playerCrossData.toFightSid, -1, CrossServerMessage.G2FReqCrossUseItem.MsgID.eMsgID_VALUE, builder.build().toByteArray());
            } else {
                if (cost) {
                    MessageUtils.notify_player(player, Notify.NORMAL, MessageString.BossItem_CantUse_NotInCross);
                }
            }
        }
    }

    /**
     * 获取实际能使用的物品数量
     *
     * @param player 玩家
     * @param bean   物品配置表
     * @param num    手动使用的数量
     * @return 结果
     */
    private int getRallyUseNum(Player player, Cfg_Item_Bean bean, int num) {
        for (ReadArray<Integer> tab : bean.getEffect_num().getValuees()) {
            int type = (int) tab.get(0);
            int addNum = (int) (tab.get(1, 0) * num);
            switch (type) {
                case ItemEffectType.AddHookTime:
                    if (player.getHookInfo().getHookTime() + addNum > Global.OnHookMaxNum * 60) {
                        num = (Global.OnHookMaxNum * 60 - player.getHookInfo().getHookTime()) / (int) tab.get(1);
                        if ((Global.OnHookMaxNum * 60 - player.getHookInfo().getHookTime()) % (int) tab.get(1) != 0) {
                            num += 1;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return num;
    }

    @Override
    public void bagClearUp(Player player, boolean isgm) {
        if (!isgm) {
            if (Manager.cooldownManager.isCooldowning(player, CooldownTypes.BAG_CLEAR, null)) {
                long cooldownTime = Manager.cooldownManager.getCooldownTime(player, CooldownTypes.BAG_CLEAR, null);
                if (cooldownTime / 1000L != 0) {//不提示0
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.BagClearUpCooling, String.valueOf(cooldownTime / 1000L));
                }
                return;
            }
            Manager.cooldownManager.addCooldown(player, CooldownTypes.BAG_CLEAR, null, GlobalType.ClearUp_Ttime_Interval);
        }
        long actionId = IDConfigUtil.getLogId();
        List<Map.Entry<Integer, Item>> list = new ArrayList<>();
        ConcurrentHashMap<Integer, Item> container = player.getBackpackItems();
        list.addAll(container.entrySet());
        for (int i = 0; i < list.size(); i++) {
            Map.Entry<Integer, Item> entry = list.get(i);
            Item value = entry.getValue();
            Cfg_Item_Bean valuemodel = CfgManager.getCfg_Item_Container().getValueByKey(value.getItemModelId());
            if (valuemodel == null) {
                log.error("整理背包失败 物品找不到了 playerId:" + player.getId() + " itemModelid:" + value.getItemModelId());
                return;
            }
            // 如果第一个物品是满的那么直接略过
            if (valuemodel.getMax() > 1 && valuemodel.getMax() > value.getNum()) {
                for (int j = i + 1; j < list.size(); j++) {
                    Map.Entry<Integer, Item> entry2 = list.get(j);
                    Item value2 = entry2.getValue();
                    Cfg_Item_Bean valuemodel2 = CfgManager.getCfg_Item_Container().getValueByKey(value2.getItemModelId());
                    if (isMergeAble(value, value2)) {
                        // 合并
                        if (value.getNum() + value2.getNum() <= valuemodel.getMax()) {
                            int logOldNum = value.getNum();
                            int logOldNum2 = value2.getNum();
                            value.setNum(value.getNum() + value2.getNum());
                            // 移除原来物品
                            // 这里直接移除的话会倒致下标溢出，所以置为0
                            value2.setNum(0);
                            writeItemLog(player, value.getId(), value.getItemModelId(), logOldNum,
                                    value.getNum(), ItemChangeReason.BagClearUp, ItemChangeAction.MOVE, actionId, 0, 0, value.getGridId());
                            writeItemLog(player, value2.getId(), value2.getItemModelId(), logOldNum2,
                                    0, ItemChangeReason.BagClearUp, ItemChangeAction.MOVE, actionId, 0, 0, value2.getGridId());
//                            }
                        } else {
                            int all = value.getNum() + value2.getNum();
                            int beforNum1 = value.getNum();
                            int beforNum2 = value2.getNum();
                            value.setNum((int) valuemodel.getMax());
                            value2.setNum(all - (int) valuemodel.getMax());

                            writeItemLog(player, value.getId(), value.getItemModelId(), beforNum1,
                                    value.getNum(), ItemChangeReason.BagClearUp, ItemChangeAction.MOVE, actionId, 0, 0, value.getGridId());
                            writeItemLog(player, value2.getId(), value2.getItemModelId(), beforNum2,
                                    value2.getNum(), ItemChangeReason.BagClearUp, ItemChangeAction.MOVE, actionId, 0, 0, value2.getGridId());
//                            }
                        }
                        if (value.getNum() >= valuemodel.getMax()) {
                            break;
                        }
                    }
                }
            }
        }
        //清除数量为零的
        for (Map.Entry<Integer, Item> entry : list) {
            if (entry.getValue().getNum() == 0) {
                container.remove(entry.getKey());
            }
        }
        log.warn("整理背包 player={}", player);
        sendItemInfos(player);
        Manager.playerManager.savePlayer(player, SavePlayerLevel.FiveMinLater);
    }

    @Override
    public boolean isMergeAble(Item item1, Item item2) {
        if (item1.getNum() == 0 && item2.getNum() == 0) {
            return false;
        }
        if (!checkMerge(item1, item2)) {
            return false;
        }
        Cfg_Item_Bean sourcemodel = CfgManager.getCfg_Item_Container().getValueByKey(item1.getItemModelId());
        return !(item1.getNum() >= sourcemodel.getMax() || item2.getNum() >= sourcemodel.getMax());
    }

    /**
     * 验证物品是否可以添加
     *
     * @param player 玩家
     * @param item   物品
     * @param useNum 数量
     * @return list
     */
    @Override
    public boolean canUse(Player player, Item item, int useNum) {
        if (useNum < 1) {
            return false;
        }
        Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
        if (model == null) {
            log.error("无效的物品");
            return false;
        }
        if (model.getEffect_num() == null || model.getEffect_num().size() < 1) {
            log.error("参数配置错误 作用类型和作用数量配置错误 物品id:" + item.getItemModelId());
            return false;
        }

        for (ReadArray<Integer> aii : model.getEffect_num().getValuees()) {
            int type = aii.get(0);
            switch (type) {
                case ItemEffectType.ActiveTitle:
                    ConcurrentHashMap<Integer, Integer> titelList = player.getTitleData().getTitleList();
                    if (titelList.containsKey(item.getItemModelId()) && titelList.get(item.getItemModelId()) == 0) {
                        MessageUtils.notify_player(player, Notify.NORMAL, MessageString.TITLE_HAS_ACTIVED);
                        return false;
                    }
                    break;
                case ItemEffectType.AddExp:
                    if (player.getLevel() < aii.get(1)) {
                        return false;
                    }
                    break;
                case ItemEffectType.AddDailyCount:
                    Cfg_Daily_Bean dailyBean = CfgManager.getCfg_Daily_Container().getValueByKey(aii.get(1));
                    int function = 0;
                    if (dailyBean.getId() == DailyActiveDefine.WORLD_BOSS.getValue()) {
                        function = FunctionStart.WorldBoss;
                    } else if (dailyBean.getId() == DailyActiveDefine.SUIT_BOSS.getValue()) {
                        function = FunctionStart.WorldBoss1;
                    } else if (dailyBean.getId() == DailyActiveDefine.SOUL_ANIMAL_ISLAND_BOSS.getValue()) {
                        function = FunctionStart.GodIsland;
                    }
                    if (function != 0) {
                        if (!Manager.controlManager.deal().isOpenFunction(player, function)) {
                            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.SHITU_FUNCNOTOPEN);
                            return false;
                        }
                    }
                    return true;
                case ItemEffectType.RefreshBoss:
                    int bossType = aii.get(1);
                    if (bossType == BossTypeConst.SOULANIMAL_BOSS) {
                        return Manager.soulAnimalForestCrossManager.cloneScript().canResetBossData(player, null, aii.get(2) == 1, true);
                    }
                    return Manager.bossManager.manager().canResetBossData(player, null, aii.get(2) == 1, true);
                case ItemEffectType.CallBoss:
                    int callbossType = aii.get(1);
                    if (callbossType == BossTypeConst.SOULANIMAL_BOSS) {
                        return Manager.soulAnimalForestCrossManager.cloneScript().canCallBoss(player) != 0;
                    }
                    return Manager.bossManager.manager().canCallBoss(player, callbossType) != 0;
                case ItemEffectType.Recharge:
                    int id = aii.get(1);
                    RechargeItemInfo cfg = Manager.rechargeManager.getRechargeItemInfoMap().get(id);
                    // Cfg_RechargeItem_Bean cfg = CfgManager.getCfg_RechargeItem_Container().getValueByKey(id);
                    boolean canUse = (cfg != null && Manager.rechargeManager.deal().canReward(player, cfg));
                    if (!canUse) {
                        MessageUtils.notify_player(player, Notify.NORMAL, MessageString.WoodsCannotBeUsed);
                    }
                    return canUse;
                case ItemEffectType.Furniture:
                    if (SocialServerClient.getInstance().channel == null) {
                        return false;
                    }
                case ItemEffectType.MarriageEffect:
//                    WeddingMapInfo wedding = Manager.marriageManager.getWedding();
//                    if (wedding == null)
//                        return false;
//                    WeddingOperation weddingOperation = wedding.getOperation().get(player.getId());
//                    if (weddingOperation == null)
//                        return false;
//                    Cfg_Marry_shop_Bean bean = CfgManager.getCfg_Marry_shop_Container().getValueByKey(item.getItemModelId());
//                    if (bean == null || bean.getHot() <= 0)
//                        return false;
//                    int useCount = bean.getExp_index() == 1 ? weddingOperation.getUseBigFireCount() : weddingOperation.getUseSamllFireCount();
//                    return useCount < bean.getUse_max();
                    //修改婚宴中礼花用到次数后，不能再次使用，且主城图也不能使用
                    return true;

                default:
                    break;
            }

        }
        return true;
    }

    /**
     * 物品的作用效果列表，返回null表示作用效果失败不能，不能使用物品
     *
     * @param player   目标玩家
     * @param item     物品
     * @param num      数量
     * @param actionId 行为ID值
     */
    @Override
    public void doEffects(Player player, Item item, int num, long actionId) {
        boolean isCalPlayerAttributs = false;
        Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());

        for (ReadArray<Integer> tab : itemBean.getEffect_num().getValuees()) {
            int type = tab.get(0);
            long addValue = tab.get(1) * num;
            switch (type) {
                case ItemEffectType.UseExpItem:
                    if (tab.size() < 3) {
                        return;
                    }
                    int rate = tab.get(1);
                    int time = tab.get(2) * num;
                    if (rate <= 0 || time <= 0) {
                        return;
                    }
                    ConcurrentHashMap<Integer, Integer> itemExpAddRateTime = player.getHookInfo().getItemExpAddRateTime();
                    if (itemExpAddRateTime.containsKey(rate / 100)) {
                        itemExpAddRateTime.put(rate / 100, itemExpAddRateTime.get(rate / 100) + time);
                    } else {
                        itemExpAddRateTime.put(rate / 100, time);
                    }
                    Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.MEDICINESATTRIBUTE);
                    Manager.playerHookManager.deal().onReqHookSetInfoHandler(player);
                    break;
                case ItemEffectType.AddAttribute:
                    int attType = tab.get(1);
                    isCalPlayerAttributs = true;
                    addAttribute(player, attType, tab.get(2) * num);
                    break;
                case ItemEffectType.AddHookTime:
                    HookInfo info = player.getHookInfo();
                    info.setHookTime(Math.min((int) (info.getHookTime() + addValue), Global.OnHookMaxNum * 60));
                    Manager.playerHookManager.deal().onReqHookSetInfoHandler(player);
                    break;
                case ItemEffectType.ActiveTitle:
                    Manager.titleManager.deal().useTitleItem(player, tab.get(1), num, ItemChangeReason.OwnUseDec);
                    break;
                case ItemEffectType.PersonalBossTime:
                    Manager.bossManager.personalBossScript().call(player, "useItem", item.getItemModelId(), num);
                    break;
                case ItemEffectType.StateVipBossEnterCount:
                    Manager.bossManager.stateBoss().doBuyBossCount(player);
                    break;
                case ItemEffectType.ChumGDRCItem:
                    Manager.chumManager.getScript().useItem(player, ChumPrivilege.get(tab.get(1)));
                    break;
                case ItemEffectType.AddActive:
                    Manager.countManager.addVariant(player, VariantType.AddDailyActiveMaxCount, num);
                    Manager.countManager.addVariant(player, VariantType.AddDailyActiveMaxNum, addValue);
                    break;
                case ItemEffectType.AddExp:
                    int key = player.getLevel();
                    if (player.getLevel() >= tab.get(2)) {
                        key = tab.get(2);
                    }

                    Cfg_Characters_Bean bean = CfgManager.getCfg_Characters_Container().getValueByKey(key);
                    if (bean == null) {
                        break;
                    }

                    long exp = (bean.getExp() * tab.get(3) / 10000) * num;
                    Manager.currencyManager.manager().addEXP(player, exp, ItemChangeReason.ExpAddItemGet, actionId);
                    break;
                case ItemEffectType.AddDailyCount:
                    Cfg_Daily_Bean dailyBean = CfgManager.getCfg_Daily_Container().getValueByKey(tab.get(1));
                    Manager.dailyActiveManager.deal().addCountByItem(player, dailyBean.getId(), tab.get(2) * num);
                    Manager.bossManager.manager().dealBossDailyCountAdd(player, dailyBean.getId());

                    if (!GameServer.getInstance().IsFightServer()) {
                        String func = ServerStr.getChatTableName(dailyBean.getName());
                        MessageUtils.notify_player(player, Notify.NORMAL, MessageString.USEITEMADDCLONETIME, func, tab.get(2) * num);
                        Manager.dailyActiveManager.deal().sendDailyActivePanelInfo(player);
                    }
                    break;
                case ItemEffectType.AddVipExp:
                    Manager.vipManager.deal().addVipExp(player, tab.get(1) * num, ItemChangeReason.VipExpItemGet, actionId);
                    break;
                case ItemEffectType.AddCoin:
                    int coinModelId = tab.get(1);
                    Cfg_Item_Bean item_bean = CfgManager.getCfg_Item_Container().getValueByKey(coinModelId);
                    if (item_bean == null) {
                        return;
                    }
                    long addCoinNum = tab.get(2) * num;
                    boolean flag = Manager.currencyManager.manager().onAddItemCoin(player, coinModelId, addCoinNum, ItemChangeReason.OwnUseDec, 0);
                    if (!flag) {
                        log.error(player.getName() + "使用物品增加货币失败：" + itemBean.getId());
                    }
                    break;
                case ItemEffectType.ActiveFashion:
                    int fashionModelId = tab.get(1);
                    Manager.newFashionManager.deal().addFashionID(player, fashionModelId);
                    break;
                case ItemEffectType.RefreshBoss:
                    int bossType = tab.get(1);
                    boolean all = tab.get(2) == 1;
                    switch (bossType) {
                        case BossTypeConst.WORLD_BOSS:
//                        case BossTypeConst.SUIT_BOSS:
                            Manager.bossManager.manager().resetBossData(player, bossType, all);
                            break;
                        case BossTypeConst.SOULANIMAL_BOSS:
                            if (GameServer.getInstance().IsFightServer()) {
                                Manager.soulAnimalForestCrossManager.cloneScript().resetBossData(player, all);
                            }
                            break;
                    }
                    break;
                case ItemEffectType.CallBoss:
                    int callBossType = tab.get(1);
                    switch (callBossType) {
                        case BossTypeConst.WORLD_BOSS:
                        case BossTypeConst.SUIT_BOSS:
                            Manager.bossManager.manager().callBoss(player, callBossType);
                            break;
                        case BossTypeConst.SOULANIMAL_BOSS:
                            if (GameServer.getInstance().IsFightServer()) {
                                Manager.soulAnimalForestCrossManager.cloneScript().callBoss(player);
                            }
                            break;
                    }
                    break;
                case ItemEffectType.Recharge:
                    int id = tab.get(1);

                    RechargeItemInfo rbean = Manager.rechargeManager.getRechargeItemInfoMap().get(id);

                    Recharge recharge = new Recharge();
                    recharge.setOrder_no("INTERNAL_" + IDConfigUtil.getId());
                    recharge.setGoods_id(id);
                    recharge.setGoods_type("1");
                    recharge.setGoods_ext("");
                    recharge.setTotal_fee(Manager.rechargeManager.deal().getMoney(player, rbean, "CNY"));
                    recharge.setGoods_code(String.valueOf(id));
                    recharge.setGoods_name(rbean.getGoods_name());
                    recharge.setItem_id(0);
                    recharge.setGame_money(0);
                    recharge.setRole_id(player.getId());
                    recharge.setExt_param("internal");
                    recharge.setSign_type("1");
                    recharge.setSign("tiancheng==");
                    Manager.rechargeManager.AddRecharge(null, recharge, JsonUtils.toJSONString(recharge), RechargeDefine.SRC_GM);
                    break;
                case ItemEffectType.MarriageEffect:
                    MapMessage.ResPlayerPlayVfx.Builder vfx = MapMessage.ResPlayerPlayVfx.newBuilder();
                    vfx.setPlayerId(player.getId());
                    vfx.setVfxId(tab.get(1));
                    MessageUtils.send_to_roundPlayer(player, MapMessage.ResPlayerPlayVfx.MsgID.eMsgID_VALUE, vfx.build().toByteArray(), true);
                    //修改再主城也能使用
                    WeddingMapInfo wedding = Manager.marriageManager.getWedding();
                    if (wedding == null) {
                        break;
                    }
                    WeddingOperation weddingOperation = wedding.getOperation().get(player.getId());
                    if (weddingOperation == null)
                        break;
                    Cfg_Marry_shop_Bean cfg_Marry_shop_Bean = CfgManager.getCfg_Marry_shop_Container().getValueByKey(item.getItemModelId());
                    if (cfg_Marry_shop_Bean == null || cfg_Marry_shop_Bean.getHot() <= 0)
                        break;
                    int useCount = cfg_Marry_shop_Bean.getExp_index() == 1 ? weddingOperation.getUseBigFireCount() : weddingOperation.getUseSamllFireCount();
                    //只有效果，没有奖
                    if (useCount >= cfg_Marry_shop_Bean.getUse_max()) {
                        break;
                    }
                    //使用效果
                    Manager.marriageManager.manager().doItemEffect(player, item.getItemModelId());
                    break;
                case ItemEffectType.VipPearl:
                    Manager.vipManager.pearl().useVipPearlItem(player, item.getItemModelId(), tab, ItemChangeReason.OwnUseDec);
                    break;
                case ItemEffectType.FudBox:
                    Manager.crossFudManager.deal().useBox(player, num);
                    break;
                case ItemEffectType.Furniture:
                    HomeManager.getInstance().deal().addFurniture(player, tab.get(1), num, ItemChangeReason.OpenGiftGet);
                    break;
                default:
                    log.error(player.getName() + "(" + player.getId() + ")错误的物品效果类型 type:" + type + " itemModelId:" + item.getItemModelId() + "  action:" + actionId + " changnum:" + item.getNum() + " value:" + addValue);
                    break;
            }
        }
        if (isCalPlayerAttributs) {
            Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.MEDICINESATTRIBUTE);
        }
    }

    //根据item获取聊天框公告的Info (装备格式：<t=5>强化等级_升星等级,itemModelId_是否有特殊属性,itemName</t>) (非装备格式：<t=2>itemId,itemModelId,itemName</t>)
    @Override
    public String getChatInfo(Item item) {
        int itemModelId = item.getItemModelId();
        Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(itemModelId);
        if (itemBean == null) {
            return "";
        }

        String itemName = itemBean.getName();
        StringBuilder sb = new StringBuilder();
        if (itemBean.getType() == ItemTypeConst.EQUIP
                || itemBean.getType() == ItemTypeConst.PET_EQUIP
                || itemBean.getType() == ItemTypeConst.HORSE_EQUIP || itemBean.getType() == ItemTypeConst.HolyEuiqp || itemBean.getType() == ItemTypeConst.SoulBeastEquip) { //装备
            backpackMessage.ItemInfo.Builder info = buildItemInfo(item);
            sb.append("<t=5>");
            sb.append(Base64.byteArrayToBase64(info.build().toByteArray()));
            sb.append(",");
            sb.append(itemModelId);
            sb.append(",");
            sb.append(itemName);
            sb.append("</t>");
        } else { //非装备
            sb.append("<t=2>").append(item.getId()).append(",").append(itemModelId).append(",").append(itemName).append("</t>");
            if (item.getNum() > 1) {
                sb.append("<t=0>,,*");
                sb.append(item.realNum());
                sb.append("</t>");
            }
        }
        return sb.toString();
    }

    /**
     * 礼包卡使用
     *
     * @param player   玩家背包
     * @param useNum   使用个数
     * @param actionId 行为编码
     * @param model    礼包物品
     * @return 结果
     */
    @Override
    public boolean useGift(Player player, int useNum, int index, long actionId, Gift model) {
        Cfg_Item_Bean bean = CfgManager.getCfg_Item_Container().getValueByKey(model.getItemModelId());
        if (bean == null) {
            return false;
        }
        if (bean.getType() == ItemTypeConst.XiSui) {
            log.error(player.getInfo() + " 使用转职洗髓礼包，不应该走到这里来！ " + bean.getId() + " - " + useNum);
            return false;
        }
        //活动掉落
        boolean isTrigger = false;
        for (int i = 0; i < useNum; i++) {
            isTrigger = (isTrigger || Manager.activityManager.deal().boxDrop(player, model.getItemModelId()));
        }
        if (isTrigger) {
            return true;
        }
        //诸界远征宝箱特殊掉落
        if (bean.getEffect_num().size() > 0) {
            ReadArray<Integer> array = bean.getEffect_num().get(0);
            if (array.get(0, -1) == ItemEffectType.FudBox) {
                return Manager.crossFudManager.deal().useBox(player, useNum);
            }
        }
        //特殊掉落
        if (bean.getType() == ItemTypeConst.SLECTGIFT) {
            Cfg_Item_special_gift_Bean giftBean = CfgManager.getCfg_Item_special_gift_Container().getValueByKey(bean.getGift());
            if (giftBean == null) {
                return false;
            }
            ReadArray<Integer> array = giftBean.getItem().get(index - 1);
            if (array == null) {
                return false;
            }
            List<Item> items = Item.createItems(array.get(0), array.get(1) * useNum, true);
            if (!addItems(player, items, ItemChangeReason.OpenGiftGet, actionId)) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail, MessageString.System, MessageString.System, MessageString.Task_Content, items, ItemChangeReason.OpenGiftGet, actionId);
            }
            return true;
        }
        //普通掉落
        Map<Integer, List<Integer>> dropMap = new HashMap<>();
        for (int i = 0; i < useNum; i++) {
            List<List<Integer>> dropList = Manager.dropManager.deal().getItemDrops(player, bean.getUes_gift(), model.isBind() ? 1 : 0);
            if (dropList == null || dropList.isEmpty()) {
                continue;
            }
            for (List<Integer> item : dropList) {
                if (dropMap.containsKey(item.get(0))) {
                    dropMap.get(item.get(0)).set(1, item.get(1) + dropMap.get(item.get(0)).get(1));
                } else {
                    dropMap.put(item.get(0), item);
                }
            }
        }
        int itemChangeReason = ItemChangeReason.OpenGiftGet;
        if (isSpecialGift(bean.getId())) {
            itemChangeReason = ItemChangeReason.OpenSpecialGift;
        }
        List<Item> dropItems = Item.createItems(dropMap.values(), 1);
        if (!dropItems.isEmpty()) {
            if (!addItems(player, dropItems, itemChangeReason, actionId)) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail, MessageString.System, MessageString.System, MessageString.Task_Content, dropItems, itemChangeReason, actionId);
            }
        }
        return true;
    }

    private boolean isSpecialGift(int itemId) {
        for (int i = 0; i < Global.Boss_Special_Gift.size(); i++) {
            if (itemId == Global.Boss_Special_Gift.get(i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 物品排序
     */
    @Override
    public void sortItems(ConcurrentHashMap<Integer, Item> container, List<Equip> equips, List<Item> items, int gender) {
        //仙甲装备
        List<Equip> sameGenderImmortals = new ArrayList<>();
        List<Equip> diffGenderImmortals = new ArrayList<>();
        //普通装备
        List<Equip> sameGenderEquips = new ArrayList<>();
        List<Equip> diffGenderEquips = new ArrayList<>();
        /**
         * 增加背包物品排序，规则如下：
         * 1、类型排序，先装备后其他物品
         * 2、同一类型，装备类型按id从大到小，其他物品按id从小到大
         * */
        Iterator<Item> itemIterator = container.values().iterator();
        while (itemIterator.hasNext()) {
            Item item = itemIterator.next();
            if (item == null) {
                log.info("咋回事,排序分类物品为空!!!!");
                continue;
            }
            if (item instanceof Equip) {
                Equip equip = (Equip) item;
                ReadIntegerArray equipGender = getEquipGender(equip);
                if (equipGender == null) {
                    equipGender = new ReadIntegerArray(new Integer[]{0});
                }
                //9表示通用装备
                if (equipGender.contains(gender) || equipGender.contains(9)) {
                    if (isImmortalEquip(equip)) {
                        sameGenderImmortals.add(equip);
                    } else {
                        sameGenderEquips.add(equip);
                    }
                } else {
                    if (isImmortalEquip(equip)) {
                        diffGenderImmortals.add(equip);
                    } else {
                        diffGenderEquips.add(equip);
                    }
                }
            } else {
                items.add(item);
            }
        }
        EquipItemSort equipItemSort = new EquipItemSort();
        Collections.sort(sameGenderImmortals, equipItemSort);
        Collections.sort(diffGenderImmortals, equipItemSort);
        Collections.sort(sameGenderEquips, equipItemSort);
        Collections.sort(diffGenderEquips, equipItemSort);
//        StrBuilder builder = new StrBuilder();
//        for(Equip equip : sameGenderEquips){
//            builder.append("战力:"+equipItemSort.getFightPower(equip)).append("品阶:"+equipItemSort.getEquipGrade(equip)).append("---品质:"+equipItemSort.getEquipQuality(equip)).append("---砖石:"+equipItemSort.getEquipDiamond(equip)).append("---部位:"+equipItemSort.getEquipPart(equip)).append("\n");
//        }
//        System.out.println(builder.toString());
        if (sameGenderImmortals.size() != 0) {
            equips.addAll(sameGenderImmortals);
        }
        if (diffGenderImmortals.size() != 0) {
            equips.addAll(diffGenderImmortals);
        }
        if (sameGenderEquips.size() != 0) {
            equips.addAll(sameGenderEquips);
        }
        if (diffGenderEquips.size() != 0) {
            equips.addAll(diffGenderEquips);
        }
        equips.remove(null);
        Collections.sort(items, new ItemSort());
    }

    /**
     * 获取装备职业
     */
    private ReadIntegerArray getEquipGender(Equip equip) {
        if (null == equip) {
            return null;
        }
        Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
        if (null == bean) {
            return null;
        }
        return bean.getGender();
    }

    private boolean isImmortalEquip(Equip equip) {
        if (null == equip) {
            return false;
        }
        Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
        if (null == bean) {
            return false;
        }
        return bean.getPart() >= 30 && 43 >= bean.getPart();
    }

    /**
     * 获取包裹空余位置或第一个同模板物品且物品数量满足可放置的位置
     *
     * @param player 角色
     * @param yitem  物品
     * @return
     */
    private int getBackpackAbleAddGridId(Player player, Item yitem) {
        //获取物品模型
        Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(yitem.getItemModelId());
        if (model.getMax() > 1) {
            for (int i = 1; i <= player.getBagCellsNum(); i++) {
                Item item = player.getBackpackItems().get(i);
                if (item == null) {
                    continue;
                }
                if (item.getItemModelId() != yitem.getItemModelId()) {
                    //模型不一致 下一个
                    continue;
                }
                if (item.getLosttime() != yitem.getLosttime()) {
                    //失效时间不一至
                    continue;
                }
                if (item.isBind() != yitem.isBind()) {
                    //绑定属性不一致
                    continue;
                }
                if (model.getMax() == 1) {
                    continue;
                }
                if (item.getNum() + yitem.getNum() > model.getMax()) {
                    continue;
                }
                if (!checkMerge(yitem, item)) {
                    continue;
                }
                return i;

            }
        }
        for (int i = 1; i <= player.getBagCellsNum(); i++) {
            Item item = player.getBackpackItems().get(i);
            if (item == null) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 加属性
     */
    private void addAttribute(Player player, int attributeType, int addValue) {
        if (attributeType > 1000) {
            player.getMedicinesAttributeSys().addSystemAttribute(attributeType, addValue);
        } else {
            player.getMedicinesAttribute().addAttribute(attributeType, addValue);
        }
    }

    /**
     * 写格子开启log
     *
     * @param player       角色
     * @param actionType   1 元宝开启 0时间开启
     * @param beforeCellId 开启前格子数
     * @param costGold     扣除的元宝数量
     * @param actionId     关联id
     */
    private void writeOpenCellLog(Player player, byte actionType, int beforeCellId, int costGold, long actionId) {
        try {
            CellOpenLog cellOpenLog = new CellOpenLog();
            cellOpenLog.setActionType(actionType);
            cellOpenLog.setActionId(actionId);
            cellOpenLog.setAfterCells(player.getBagCellsNum());
            cellOpenLog.setBeforeCells(beforeCellId);
            cellOpenLog.setResumeGold(costGold);
            cellOpenLog.setPlayer(player);
            cellOpenLog.setType((byte) 1);
            LogService.getInstance().execute(cellOpenLog);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    @Override
    public void pushCollectLog(Player player, int modelId, long num) {
        try {
            String key = "" + player.getId();
            ErrorLog cllectLog = new ErrorLog();
            cllectLog.setType(modelId);
            cllectLog.setValue(num);
            GameServer.getInstance().getErrorLogThread().pushErrorLog(key, cllectLog);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    /**
     * 增加的物品是装备 并且能被分解
     */
    private boolean isResolve(Player player, Item item, Cfg_Item_Bean model) {
        if (!(item instanceof Equip)) {
            return false;
        }
        return player.getResolveSettings().contains(model.getColor());
    }

    @Override
    public List<List<Integer>> getAfterMergeItemList(List<List<Integer>> itemList) {
        if (null == itemList)
            return null;

        int size = itemList.size();
        if (size == 0)
            return null;

        List<List<Integer>> retList = new ArrayList<>();
        if (size == 1) {
            List<Integer> list = merge(itemList.get(0));
            if (list != null)
                retList.add(list);
        } else if (size == 2 && !itemList.get(0).get(0).equals(itemList.get(1).get(0))) {
            List<Integer> list1 = merge(itemList.get(0));
            if (list1 != null)
                retList.add(list1);
            List<Integer> list2 = merge(itemList.get(1));
            if (list2 != null)
                retList.add(list2);
        } else {
            HashMap<Integer, Integer> noBindMap = new HashMap<>();
            HashMap<Integer, Integer> bindMap = new HashMap<>();

            for (List<Integer> integers : itemList) {
                List<Integer> list = merge(integers);
                if (list == null)
                    continue;
                if (list.get(2) == 1)
                    bindMap.put(list.get(0), bindMap.getOrDefault(list.get(0), 0) + list.get(1));
                else
                    noBindMap.put(list.get(0), noBindMap.getOrDefault(list.get(0), 0) + list.get(1));
            }
            for (Map.Entry<Integer, Integer> entry : noBindMap.entrySet()) {
                List<Integer> l = new ArrayList<>();
                l.add(0, entry.getKey());
                l.add(1, entry.getValue());
                l.add(2, 0);
                retList.add(l);
            }
            for (Map.Entry<Integer, Integer> entry : bindMap.entrySet()) {
                List<Integer> l = new ArrayList<>();
                l.add(0, entry.getKey());
                l.add(1, entry.getValue());
                l.add(2, 1);
                retList.add(l);
            }
        }
        return retList;
    }

    private List<Integer> merge(List<Integer> item) {
        if (item == null)
            return null;

        int size = item.size();
        if (size < 2)
            return null;

        int itemID = item.get(0);
        int num = item.get(1);
        int bind = 1;
        if (size > 2 && item.get(2) == 0)
            bind = 0;

        List<Integer> ret = new ArrayList<>();
        ret.add(0, itemID);
        ret.add(1, num);
        ret.add(2, bind);
        return ret;
    }

    @Override
    public String getName(int model) {
        Cfg_Item_Bean bean = CfgManager.getCfg_Item_Container().getValueByKey(model);
        if (bean != null) {
            return ServerStr.getChatTableName(bean.getName());
        }
        Cfg_Equip_Bean cfg_equipBean = CfgManager.getCfg_Equip_Container().getValueByKey(model);
        if (cfg_equipBean != null) {
            return ServerStr.getChatTableName(cfg_equipBean.getName());
        }

        return "item" + model;
    }

    @Override
    public String getItemName(int model) {
        Cfg_Item_Bean bean = CfgManager.getCfg_Item_Container().getValueByKey(model);
        if (bean != null) {
            return bean.getName();
        }
        Cfg_Equip_Bean cfg_equipBean = CfgManager.getCfg_Equip_Container().getValueByKey(model);
        if (cfg_equipBean != null) {
            return cfg_equipBean.getName();
        }
        return "item" + model;
    }

    @Override
    public boolean addItem(Player player, int itemID, int num, boolean isBind, long lostTime, int reason, long action) {
        Item item = Item.createItem(itemID, num, isBind, lostTime);
        if (item == null)
            return false;
        return addItem(player, item, reason, action);
    }

    @Override
    public boolean addItem(Player player, Item item, int reason, long action) {
        return addItem(player, item, reason, action, 0, 0F);
    }

    private boolean addItem(Player player, Item item, int reason, long action, int costType, float costNum) {
        return addItemHaveMergeFlag(player, item, reason, action, costType, costNum, true);
    }

    @Override
    public boolean addItemHaveMergeFlag(Player player, Item item, int reason, long action, int costType, float costNum, boolean needMergeFlag) {
        if (!onHasAddSpace(player, item)) {
            return false;
        }
        int count = item.getNum();
        boolean state = onAddItem(player, item, reason, action, costType, costNum, needMergeFlag);

        //TODO 2020年8月14日 优化消息提示
        Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
        if (model.getType() == ItemTypeConst.TITLE || model.getType() == ItemTypeConst.XiSui || model.getType() == ItemTypeConst.COPPER) {
            return state;
        }
        if (state) {
            MessageUtils.notify_player(player, Notify.CHAT, MessageString.GetEquipsTips, getName(item.getItemModelId()), count);
        }
        return state;
    }

    @Override
    public void sendItemAdd(Player player, int reason, Item item) {
        backpackMessage.ResItemAdd.Builder msg = backpackMessage.ResItemAdd.newBuilder();
        msg.setReason(reason);
        msg.setItemInfo(buildItemInfo(item));
        MessageUtils.send_to_player(player, backpackMessage.ResItemAdd.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void sendItemChange(Player player, int reason, Item item) {
        backpackMessage.ResItemChange.Builder msg = backpackMessage.ResItemChange.newBuilder();
        msg.setReason(reason);
        backpackMessage.ItemInfo.Builder info = makeItemInfoBuilder(item);
        msg.setItemInfo(info);
        MessageUtils.send_to_player(player, backpackMessage.ResItemChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void sendItemDelete(Player player, int reason, long itemId) {
        backpackMessage.ResItemDelete.Builder msg = backpackMessage.ResItemDelete.newBuilder();
        msg.setReason(reason);
        msg.setItemId(itemId);
        MessageUtils.send_to_player(player, backpackMessage.ResItemDelete.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void sendItemDeleteList(Player player, int reason, Set<Long> itemIds) {
        backpackMessage.ResItemListDelete.Builder msg = backpackMessage.ResItemListDelete.newBuilder();
        msg.setReason(reason);
        msg.addAllItemIds(itemIds);
        MessageUtils.send_to_player(player, backpackMessage.ResItemListDelete.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void sendItemNotEnough(Player player, int itemModelId) {
        backpackMessage.ResItemNotEnough.Builder resMsg = backpackMessage.ResItemNotEnough.newBuilder();
        resMsg.setItemModelId(itemModelId);
        MessageUtils.send_to_player(player, backpackMessage.ResItemNotEnough.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());
    }

    private backpackMessage.ItemInfo.Builder makeItemInfoBuilder(Item item) {
        backpackMessage.ItemInfo.Builder info = buildItemInfo(item);
        info.setCdTime(0);
        return info;
    }

    @Override
    public List<backpackMessage.ItemInfo> buildItemInfo(List<Item> items) {
        List<backpackMessage.ItemInfo> result = new ArrayList<>();
        for (Item item : items) {
            result.add(buildItemInfo(item).build());
        }
        return result;
    }

    @Override
    public backpackMessage.ItemInfo.Builder buildItemInfo(Item item) {
        backpackMessage.ItemInfo.Builder a = backpackMessage.ItemInfo.newBuilder();
        a.setGridId(item.getGridId());
        a.setNum(item.realNum());
        a.setIsbind(item.isBind());
        a.setItemId(item.getId());
        a.setItemModelId(item.getItemModelId());
        a.setLostTime(item.getLosttime());
        if (item instanceof Equip) {
            Equip eq = (Equip) item;
            a.setSuitId(eq.getSuitId());
        }
        return a;
    }

    @Override
    public backpackMessage.Key_Value.Builder buildKeyValue(int key, int value) {
        backpackMessage.Key_Value.Builder a = backpackMessage.Key_Value.newBuilder();
        a.setKey(key);
        a.setValue(value);
        return a;
    }

    @Override
    public int getEmptyGridNum(Player player) {
        return player.getBagCellsNum() - player.getBackpackItems().size();
    }

    @Override
    public Item getItemByCellId(Player player, int cellId) {
        return player.getBackpackItems().get(cellId);
    }

    @Override
    public Item getItemById(Player player, long itemId) {
        for (Item item : player.getBackpackItems().values()) {
            if (item.getId() == itemId) {
                return item;
            }
        }
        //圣装背包
        if (player.getHolyEquipBaseInfo().getHolyEquipItemList().containsKey(itemId)) {
            return player.getHolyEquipBaseInfo().getHolyEquipItemList().get(itemId);
        }

        //仙甲背包
        if (player.getImmEquipItemList().containsKey(itemId)) {
            return player.getImmEquipItemList().get(itemId);
        }
        //幻装背包
        if (player.getUnrealEquipBaseInfo().getUnrealEquipItemList().containsKey(itemId)) {
            return player.getUnrealEquipBaseInfo().getUnrealEquipItemList().get(itemId);
        }
        return null;
    }

    @Override
    public Item getItemByModelId(Player player, int modelId) {
        for (Item item : player.getBackpackItems().values()) {
            if (item.getItemModelId() == modelId) {
                return item;
            }
        }
        return null;
    }

    @Override
    public Item getItemByModelIdIsBind(Player player, int modelId, boolean bind) {
        for (Item item : player.getBackpackItems().values()) {
            if (item.getItemModelId() == modelId && item.isBind() == bind) {
                return item;
            }
        }
        return null;
    }

    @Override
    public void writeItemLog(Player player, long itemId, int modelId, int oldNum, int afterNum, int reason, ItemChangeAction changeAction, long actionId, int coinType, float costNum, int cellId) {
        try {
            //特殊处理
            if (reason == ItemChangeReason.GemInlayDown || reason == ItemChangeReason.JadeInlayDown//宝石镶嵌不计入消耗
                    || reason == ItemChangeReason.InlayImmortalEquip || reason == ItemChangeReason.HolyInlayReasonGet//圣装仙甲穿戴
                    || reason == ItemChangeReason.SoulArmorUnWearGet //魂甲.魂印卸下
                    || reason == ItemChangeReason.DressPetEquipDec || reason == ItemChangeReason.ReplacePetEquipGet//宠物装备穿戴替换
                    || reason == ItemChangeReason.WearEquipDec || reason == ItemChangeReason.UnWearEquipGet//穿戴装备
                    || reason == ItemChangeReason.SpiritDec || reason == ItemChangeReason.SpiritGet//灵体穿戴和替换
            ) {
                return;
            }
            //我在这儿处理收集任务的变化处理
            Cfg_Item_Bean bean = CfgManager.getCfg_Item_Container().getValueByKey(modelId);
            if (bean == null) {
                log.warn("没有找到对应的物品:{}", modelId);
                return;
            }
            if (bean != null && bean.getNeedTaskCheck() > 0) {
                Manager.taskManager.deal().action(player, Task.ACTION_TYPE_COLLECT_REAL_ITEM, modelId, afterNum);
            }
            ItemChangeLog itemChangeLog = new ItemChangeLog();
            itemChangeLog.setRoleLevel(player.getLevel());
            itemChangeLog.setActionId(actionId);
            itemChangeLog.setChangeAction(changeAction.toString());
            itemChangeLog.setModelId(modelId);
            itemChangeLog.setOldNum(oldNum);
            itemChangeLog.setNewNum(afterNum);
            itemChangeLog.setChangeNum(afterNum - oldNum);
            itemChangeLog.setItemId(itemId);
            itemChangeLog.setReason(reason);
            itemChangeLog.setUserId(player.getUserId());
            itemChangeLog.setPlayer(player);
            itemChangeLog.setCoinType(coinType);
            itemChangeLog.setCostNum(costNum);
            itemChangeLog.setCellId(cellId);
            itemChangeLog.setType(bean.getType());
            LogService.getInstance().execute(itemChangeLog);
            int changeType = 0;
            int changeNum = afterNum - oldNum;
            if (changeNum > 0) {
                changeType = 1;
            }
            LogDataManager.instance.onItemChange(player.getCreateServerId(), modelId, bean.getType(), bean.getName(), oldNum, afterNum);
            Manager.biManager.getScript().biItem(player, itemId, bean.getType(), bean.getColor(), bean.getId(), bean.getName(),
                    Math.abs(afterNum - oldNum), afterNum, reason, actionId, changeType, player.getCurGps().getModelId());
            Manager.biManager.get4399Script().itemBiTo4399(player, reason, modelId, oldNum, afterNum);
        } catch (Exception e) {
            log.error("modelId:" + modelId + " itemId:" + itemId, e);
        }
    }


    public void writeItemLogAndBI(Player player, int oldNum, int afterNum, Item item, int reason, long actionId) {
        try {
            if (oldNum == afterNum) {
                log.warn("invalid item change log, because no change");
                return;
            }
            ItemChangeAction itemChangeAction = null;
            if (oldNum == 0) {
                itemChangeAction = ItemChangeAction.ADD;
            } else if (afterNum == 0) {
                itemChangeAction = ItemChangeAction.REMOVE;
            } else {
                itemChangeAction = ItemChangeAction.CHANGE;
            }
            Manager.backpackManager.manager().writeItemLog(player, item.getId(), item.getItemModelId(), oldNum,
                    afterNum, reason, itemChangeAction, actionId, 0, 0, item.getGridId());
        } catch (Exception e) {
            log.error("log error", e);
        }
    }
}
