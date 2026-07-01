package com.game.backpack.structs;

import com.data.CfgManager;
import com.data.bean.Cfg_Item_Bean;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import com.data.struct.ReadIntegerArrayEs;
import com.data.struct.ReadLongArrayEs;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.activity.struct.RewardData;
import com.game.backpack.log.ItemLog;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.server.GameServer;
import com.game.soulbeast.manager.SoulBeastManager;
import com.game.structs.GameObject;
import com.game.utils.RandomUtils;
import com.game.utils.Symbol;
import game.core.dblog.LogService;
import game.core.util.AutoIncrementIntArray;
import game.core.util.DateFormatUtils;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.util.*;

public abstract class Item extends GameObject implements Comparable<Item>, Cloneable {

    @JsonIgnore
    protected transient static final Logger logger = LogManager.getLogger(Item.class);

    private int itemModelId;

    private int num;

    private int gridId;
    //是否绑定
    private boolean bind;
    //失效时间
    private int losttime;

    public abstract boolean use(Player player, int userNum, int index, long actionId);

    public abstract boolean unuse(Player player, int unUseNum, long actionId);

    public static List<Item> createItems(String str, String bigSplit, String smallSpilt) {
        return createItems(str, bigSplit, smallSpilt, 1);
    }

    public static List<Item> createItems(ReadIntegerArrayEs list) {
        return createItems(list, 1);
    }

    public static List<Item> createItems(ReadLongArrayEs list) {
        return createItems(list, 1);
    }

    /**
     * 返回真实的物品数量，主要是争取有经验值的情况下的数量值
     *
     * @return 返回真实的数量
     */
    public long realNum() {
        return num;
    }


    /**
     * 道具复制
     *
     * @param array
     * @return
     */
    public static AutoIncrementIntArray itemCopy(AutoIncrementIntArray array) {
        AutoIncrementIntArray result = new AutoIncrementIntArray(array.length());
        for (int i = 0; i < array.length(); i++) {
            result.set(i, array.get(i));
        }
        return result;
    }

    /**
     * 根据字符串创建物品列表，分隔符不能用-
     *
     * @param str:int      itemModelId, int num, boolean bind(1绑定0非绑), string
     *                     losttime(过期时间0或者yyyy-MM-dd HH:mm格式),int strengLv, int
     *                     starLv(前两个参数是必须的),依次； 例如:1000_1_1_0_0_0;1001_2_1;1001_2_1_2015-11-2
     *                     15:00_0_0
     * @param bigSplit     大分隔符
     * @param smallSpilt   小分隔符
     * @param multiplicity 多倍
     * @return
     */
    public static List<Item> createItems(String str, String bigSplit, String smallSpilt, int multiplicity) {
        List<Item> list = new ArrayList<>();
        if (multiplicity < 1) {
            return list;
        }
        String[] itemstr = str.split(bigSplit);

        for (String itemtabs : itemstr) {
            int length = 0;
            int itemModelId = 0;
            int num = 0;
            boolean bind = true;
            long losttime = 0;
            int strengLv = 0;
            int starLv = 0;
            String[] item = itemtabs.split(smallSpilt);
            length = item.length;
            if (length < 2) {
                continue;
            }
            itemModelId = Integer.parseInt(item[0].trim());
            num = Integer.parseInt(item[1].trim()) * multiplicity;
            if (length >= 3 && !item[2].isEmpty()) {
                bind = Integer.parseInt(item[2].trim()) == 1;
            }
            if (length >= 4 && !item[3].isEmpty()) {
                if ("0".equals(item[3])) {
                    losttime = 0;
                } else {
                    try {
                        Date d = DateFormatUtils.parse(item[3], "yyyy-MM-dd HH:mm:ss");//sdf.parse(item[3]);
                        losttime = d.getTime();
                    } catch (ParseException ex) {
                        logger.error(ex, ex);
                        losttime = 0;
                    }
                }
            }
            if (length >= 5 && !item[4].isEmpty()) {
                strengLv = Integer.parseInt(item[4].trim());
            }
            if (length >= 6 && !item[5].isEmpty()) {
                starLv = Integer.parseInt(item[5].trim());
            }
            list.addAll(createItems(itemModelId, num, bind, losttime, strengLv, starLv));
        }
        return list;
    }


    public static List<Item> createItems(Collection<List<Integer>> list, float multiplicity) {
        List<Item> items = new ArrayList<>();
        if (multiplicity < 1) {
            return items;
        }
        if (list == null || list.isEmpty()) {
            return items;
        }
        int length, itemModelId, num;
        boolean bind = true;
        long lostTime = 0;
        int strongLv = 0;
        int starLv = 0;
        for (List<Integer> item : list) {
            length = item.size();
            if (length < 2) {
                continue;
            }
            itemModelId = item.get(0);
            num = (int) (item.get(1) * multiplicity);
            if (length >= 3) {
                bind = item.get(2) == 1;
            }
            items.addAll(createItems(itemModelId, num, bind, lostTime, strongLv, starLv));
        }
        return items;
    }

    public static List<Item> createItemsWithLongCollection(Collection<List<Long>> list, float multiplicity) {
        List<Item> items = new ArrayList<>();
        if (multiplicity < 1) {
            return items;
        }
        if (list == null || list.isEmpty()) {
            return items;
        }
        int length,itemModelId;
        long num;
        boolean bind = true;
        long lostTime = 0;
        int strongLv = 0;
        int starLv = 0;
        for (List<Long> item : list) {
            length = item.size();
            if (length < 2) {
                continue;
            }
            itemModelId = item.get(0).intValue();
            num = (int) (item.get(1) * multiplicity);
            if (length >= 3) {
                bind = item.get(2) == 1;
            }
            items.addAll(createItems(itemModelId, num, bind, lostTime, strongLv, starLv));
        }
        return items;
    }

    public static List<Item> createItems(List<List<Long>> list, float multiplicity) {
        List<Item> items = new ArrayList<>();
        if (multiplicity < 1) {
            return items;
        }
        if (list == null || list.isEmpty()) {
            return items;
        }
        int length, itemModelId;
        long num;
        boolean bind = true;
        long lostTime = 0;
        int strongLv = 0;
        int starLv = 0;
        for (List<Long> item : list) {
            length = item.size();
            if (length < 2) {
                continue;
            }
            itemModelId = item.get(0).intValue();
            num = (long) (item.get(1) * multiplicity);
            if (length >= 3) {
                bind = item.get(2) == 1;
            }
            items.addAll(createItems(itemModelId, num, bind, lostTime, strongLv, starLv));
        }
        return items;
    }

    public static List<Item> createItems(ReadArray<Integer> array, float multiplicity) {
        List<Item> list = new ArrayList<>();
        if (multiplicity < 1 || array.size() < 2) {
            return list;
        }
        int itemModelId = array.get(0);
        int num = (int) (array.get(1) * multiplicity);
        boolean bind = array.get(2, 1) == 1;
        long losttime = array.get(3, 0) * 1000;
        int strengLv = array.get(4, 0);
        int starLv = array.get(5, 0);
        return createItems(itemModelId, num, bind, losttime, strengLv, starLv);
    }

    public static List<Item> createItems(ReadArray<Long> array, int multiplicity) {
        List<Item> list = new ArrayList<>();
        if (multiplicity < 1 || array.size() < 2) {
            return list;
        }
        long itemModelId = array.get(0);
        long num = array.get(1) * multiplicity;
        boolean bind = array.get(2, 1l) == 1l;
        long losttime = array.get(3, 0l) * 1000;
        long strengLv = array.get(4, 0l);
        long starLv = array.get(5, 0l);
        return createItems((int) itemModelId, num, bind, losttime, (int) strengLv, (int) starLv);
    }

    public static List<Item> createItems(ReadIntegerArrayEs arrays, float multiplicity) {
        List<Item> list = new ArrayList<>();
        for (ReadArray<Integer> array : arrays.getValuees()) {
            List<Item> items = createItems(array, multiplicity);
            list.addAll(items);
        }
        return list;
    }

    public static List<Item> createItems(ReadLongArrayEs arrays, int multiplicity) {
        List<Item> list = new ArrayList<>();
        for (ReadArray<Long> array : arrays.getValuees()) {
            List<Item> items = createItems(array, multiplicity);
            list.addAll(items);
        }
        return list;
    }

    /**
     * 创建没有失效时间物品对象（装备强化和升星等级为0）
     *
     * @param itemModelId
     * @param num
     * @param bind
     * @return
     */
    public static List<Item> createItems(int itemModelId, long num, boolean bind) {
        return createItems(itemModelId, num, bind, 0, 0, 0);
    }

    /**
     * 创建物品对象（装备强化和升星等级为0）
     *
     * @param itemModelId
     * @param num
     * @param bind
     * @param losttime    失效时间 单位ms
     * @return
     */
    public static List<Item> createItems(int itemModelId, long num, boolean bind, long losttime) {
        return createItems(itemModelId, num, bind, losttime, 0, 0);
    }

    /**
     * 创建物品对象
     *
     * @param itemModelId
     * @param num
     * @param bind
     * @param losttime    单位ms
     * @param strengLv    强化等级
     * @param starLv      升星等级
     * @return
     */
    public static List<Item> createItems(int itemModelId, long num, boolean bind, long losttime, int strengLv, int starLv) {
        List<Item> list = new ArrayList<>();
        if (num <= 0) {
            logger.error("物品模型ID" + itemModelId + "创建的物品数量配置错误！", new Exception("通知策划检查数据物品模型ID：" + itemModelId + "配置的数量！"));
            return list;
        }

        Cfg_Item_Bean q_itemBean = CfgManager.getCfg_Item_Container().getValueByKey(itemModelId);
        if (q_itemBean == null) {
            logger.error("物品模型ID " + itemModelId + "找不到");
            return list;
        }

        long maxValue = q_itemBean.getMax();
        if (itemModelId == ItemCoinType.EXP) {
            maxValue = Manager.currencyManager.manager().getMaxCurrencyLimit(ItemCoinType.EXP);
        }

        if (maxValue <= 0) {
            logger.error("物品模型ID " + itemModelId + "最大堆叠数问题");
            return list;
        }

        // 超量保护
        if ((num / maxValue) > 1000) {
            String errorStr = "物品模型ID" + q_itemBean.getName() + "(" + itemModelId + ")的堆叠数是" + maxValue + "， 当前要增加的个数是:" + num;
            logger.error(errorStr);//, new Exception("通知策划检查数据物品模型ID或者最大堆叠数问题" + itemModelId + "找不到"));
            GameServer.getInstance().getErrorLogThread().pushErrorExcptionLog("Item.CreateItems", errorStr + Arrays.toString(Thread.currentThread().getStackTrace()));
            return list;
        }

        long count = num;
        while (count > 0) {
            long rn = Math.min(count, maxValue);
            count -= rn;
            Item item = newItem(itemModelId,  rn, bind, q_itemBean.getDead_time(), q_itemBean.getType());
            //日志
//            createLog(item, q_itemBean.getType());
            list.add(item);
        }
        return list;
    }

    /**
     * FIXME 支持本类四元组
     * ID_数量_是否绑定（0否1是）_职业
     * ID：对应Item表主键
     * num:数量
     * bind:0未绑定 1绑定
     * occ：0男 1女 9通用
     *
     * @param _career
     * @param list
     * @param ber     数量倍数
     * @return
     */
    public static List<Item> createItems(int _career, ReadLongArrayEs list, int ber) {
        List<Item> ret = new ArrayList<>();
        if (list == null) {
            return ret;
        }
        for (ReadArray<Long> cfg : list.getValuees()) {
            int itemId = cfg.get(0).intValue();
            long count = cfg.get(1) * ber;
            boolean bind = cfg.get(2, 1l) == 1l;
            long career = cfg.get(3, 9l);
            if (career == 9 || career == _career) {
                List<Item> items = Item.createItems(itemId, count, bind);
                ret.addAll(items);
            }
        }
        return ret;
    }

    /**
     * FIXME 支持本类四元组
     * ID_数量_是否绑定（0否1是）_职业
     * ID：对应Item表主键
     * num:数量
     * bind:0未绑定 1绑定
     * occ：0男 1女 9通用
     *
     * @param _career
     * @param list
     * @param ber     数量倍数
     * @return
     */
    public static List<Item> createItems(int _career, ReadIntegerArrayEs list, int ber) {
        List<Item> ret = new ArrayList<>();
        if (list == null) {
            return ret;
        }
        for (ReadArray<Integer> cfg : list.getValuees()) {
            int itemId = cfg.get(0);
            long count = cfg.get(1) * ber;
            boolean bind = cfg.get(2, 1) == 1;
            int career = cfg.get(3, 9);
            if (career == 9 || career == _career) {
                List<Item> items = Item.createItems(itemId, count, bind);
                ret.addAll(items);
            }
        }
        return ret;
    }

    /**
     * FIXME 本类型支持结构  career_itemId_count_bind
     * @param _career
     * @param list
     * @param ber
     * @return
     */
    public static List<Item> createItemsByCareer(int _career, ReadIntegerArrayEs list, int ber) {
        List<Item> ret = new ArrayList<>();
        if (list == null) {
            return ret;
        }
        for (ReadArray<Integer> cfg : list.getValuees()) {
            int career = cfg.get(0);
            int itemId = cfg.get(1);
            long count = cfg.get(2) * ber;
            boolean bind = cfg.get(3, 1) == 1;
            if (career == 9 || career == _career) {
                List<Item> items = Item.createItems(itemId, count, bind);
                ret.addAll(items);
            }
        }
        return ret;
    }

    /**
     * @param _career
     * @param list
     * @return
     */
    public static List<Item> createItems(int _career, List<RewardData> list) {
        List<Item> ret = new ArrayList<>();
        if (list == null) {
            return ret;
        }
        for (RewardData cfg : list) {
            if (cfg.getC() == 9 || cfg.getC() == _career) {
                List<Item> items = Item.createItems(cfg.getI(), cfg.getN(), cfg.getB() == 1);
                ret.addAll(items);
            }
        }
        return ret;
    }

    /**
     * @param _career
     * @param
     * @return
     */
    public static List<Item> createItems(int _career, Collection<RewardData> list) {
        List<Item> ret = new ArrayList<>();
        if (list == null) {
            return ret;
        }
        for (RewardData cfg : list) {
            if (cfg.getC() == 9 || cfg.getC() == _career) {
                List<Item> items = Item.createItems(cfg.getI(), cfg.getN(), cfg.getB() == 1);
                ret.addAll(items);
            }
        }
        return ret;
    }

    /**
     * 创建单个物品对象,数量超过最大上限是设置为上限数量
     *
     * @param itemModelId
     * @param num
     * @param bind
     * @return
     */
    public static Item createItem(int itemModelId, long num, boolean bind) {
        return createItem(itemModelId, num, bind, 0, 0, 0);
    }

    /**
     * 创建单个物品对象,数量超过最大上限是设置为上限数量
     *
     * @param itemModelId
     * @param num
     * @param bind
     * @param losttime
     * @return
     */
    public static Item createItem(int itemModelId, long num, boolean bind, long losttime) {
        return createItem(itemModelId, num, bind, losttime, 0, 0);
    }

    /**
     * 创建单个物品对象,数量超过最大上限是设置为上限数量
     *
     * @param itemModelId
     * @param num
     * @param bind
     * @param losttime    单位ms
     * @param strengLv    强化等级
     * @param starLv      升星等级
     * @return
     */
    public static Item createItem(int itemModelId, long num, boolean bind, long losttime, int strengLv, int starLv) {
        Cfg_Item_Bean q_itemBean = CfgManager.getCfg_Item_Container().getValueByKey(itemModelId);
        if (q_itemBean == null) {
            logger.error("createItem物品模型ID " + itemModelId + "找不到");
            return null;
        }
        long maxValue = q_itemBean.getMax();
        if (itemModelId == ItemCoinType.EXP) {
            maxValue = Manager.currencyManager.manager().getMaxCurrencyLimit(ItemCoinType.EXP);
        }
        if (maxValue <= 0) {
            logger.error("createItem物品模型ID " + itemModelId + "最大堆叠数量问题");
            return null;
        }

        if (num > maxValue) {
            try {
                throw new Exception("createItem num > maxValue ：" + itemModelId + " stace : ");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        Item item = newItem(itemModelId,  Math.min(num, maxValue), bind, q_itemBean.getDead_time(), q_itemBean.getType());
        //日志
//        createLog(item, q_itemBean.getType());
        return item;
    }

    // 100金币 101元宝 102绑定元宝 103荣誉值 104真气值 105善恶值 106经验值 107 铁精 108绑定金币 109成就值 110帮贡 111竞技场
    public static Item createItemCoin(int ItemModelId, long num) {
        if (ItemModelId == ItemCoinType.EXP) {
            ExpValueItem item = new ExpValueItem();
            item.setItemModelId(ItemModelId);
            item.setNum(1);
            item.setExpNum(num);
            item.setId(IDConfigUtil.getId());
            return item;
        }
        Item item = new CommonGoods();
        item.setItemModelId(ItemModelId);
        item.setNum((int) num);
        item.setId(IDConfigUtil.getId());
        return item;
    }

    /**
     * 创建新道具(需求变动:修改为使用配置的过期时间)
     *
     * @param itemModelId
     * @param num
     * @param bind
     * @param lostTime
     * @param type
     * @return
     */
    private static Item newItem(int itemModelId, long num, boolean bind, int lostTime, int type) {
        Item item;
        switch (type) {
            case ItemTypeConst.COPPER:
                if (itemModelId == ItemCoinType.EXP) {
                    item = new ExpValueItem();
                    ((ExpValueItem) item).setExpNum(num);
                } else {
                    item = new CommonGoods();
                }
                break;
            case ItemTypeConst.IMM_EQUIP:
            case ItemTypeConst.EQUIP:
            case ItemTypeConst.PET_EQUIP:
            case ItemTypeConst.HORSE_EQUIP:
            case ItemTypeConst.Soul_EQUIP:
            case ItemTypeConst.DEVIL_EQUIP:
                item = new Equip();
                break;

            case ItemTypeConst.HolyEuiqp:
                item = Manager.holyEquipManager.deal().createHolyEquip(itemModelId);
                break;

            case ItemTypeConst.UNREAL_EQUIP:
                item = Manager.unrealEquipManager.deal().createUnrealEquip(itemModelId);
                break;
            case ItemTypeConst.SoulItem:
                item = Manager.immortalSoulManager.manager().createSoul(itemModelId);
                break;
            case ItemTypeConst.GIFT_PACKAGE:
            case ItemTypeConst.SLECTGIFT:
            case ItemTypeConst.XiSui:
                item = new Gift();
                break;
            case ItemTypeConst.SoulBeastEquip:
                item = SoulBeastManager.getInstance().deal().createSoulBeastEquip(itemModelId);
                break;
            case ItemTypeConst.SoulBeastItem:
                item = SoulBeastManager.getInstance().deal().createSoulBeastItem(itemModelId);
                break;
            case ItemTypeConst.COMMONGOODS:
            case ItemTypeConst.MATERIAL:
            case ItemTypeConst.GIFT:
            case ItemTypeConst.SPECIAL:
            case ItemTypeConst.FRAGMENTATION:
            case ItemTypeConst.EFFECT_ITEM:
            case ItemTypeConst.GEM:
            case ItemTypeConst.TITLE:
            case ItemTypeConst.XiSuiNormal:
            case ItemTypeConst.VIPEXP:
            case ItemTypeConst.WeddingItem:
            case ItemTypeConst.DEVIL_CARD:
                item = new CommonGoods();
                break;
            default:
//                logger.error("找不到定义的物品类型" + type + " id =" + itemModelId, new Exception());
                item = new CommonGoods();
                break;
        }
        item.setBind(bind);
        item.setItemModelId(itemModelId);
        if (itemModelId == ItemCoinType.EXP) {
            item.setNum(1);
        } else {
            item.setNum((int)num);
        }
        item.setId(IDConfigUtil.getId());
        if (lostTime != 0) {
            item.setLosttime((int) TimeUtils.TimeSec() + lostTime);
        }

        return item;
    }

    private static void createLog(Item item, int type) {
        ItemLog log = new ItemLog();
        log.setItemId(item.getId());
        log.setModelId(item.getItemModelId());
        log.setChangeNum(item.getNum());
        log.setReason(-1);
        log.setChangeAction("new");
        log.setType(type);
        log.setCreateDate(new Date());
        LogService.getInstance().execute(log);
    }

    public int getItemModelId() {
        return itemModelId;
    }

    public void setItemModelId(int itemModelId) {
        this.itemModelId = itemModelId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getGridId() {
        return gridId;
    }

    public void setGridId(int gridId) {
        this.gridId = gridId;
    }

    public boolean isBind() {
        return bind;
    }

    public void setBind(boolean bind) {
        this.bind = bind;
    }

    public int getLosttime() {
        return losttime;
    }

    public void setLosttime(int losttime) {
        this.losttime = losttime;
    }


    @Override
    public int compareTo(Item o) {
        if (getItemModelId() != o.getItemModelId()) {
            return o.getItemModelId() - getItemModelId();
        }
//        if (this instanceof Equip && o instanceof Equip) {
//            Equip a = (Equip) this;
//            Equip b = (Equip) o;
////            if (a.getStarLv() != b.getStarLv()) {
////                return b.getStarLv() - a.getStarLv();
////            }
////            if (a.getStrengLv() != b.getStrengLv()) {
////                return b.getStrengLv() - a.getStrengLv();
////            }
//        }
        if (getLosttime() != o.getLosttime()) {
            return o.getLosttime() - getLosttime();
        }
        if (isBind() != o.isBind()) {
            return isBind() ? -1 : 1;
        }
        if (getNum() != o.getNum()) {
            return o.getNum() - getNum();
        }
        return 0;
    }

    @Override
    public Item clone() throws CloneNotSupportedException {
        return (Item) super.clone();
    }

    /**
     * 拷贝
     *
     * @param items
     * @return
     */
    public static List<Item> clone(List<Item> items) {
        if (items == null || items.size() == 0)
            return items;

        List<Item> ret = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            try {
                ret.add(items.get(i).clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    @Override
    public String toString() {
        return "[cellid=" + getGridId() + "][itemId=" + getId() + "][itemModleId=" + getItemModelId() + "][num=" + getNum() + "]";
    }

    /**
     * 是否己过时,不能用is
     *
     * @return
     */
    public boolean haveLost() {
        return losttime > 0 && TimeUtils.Time() > losttime * 1000L;
    }


    /* -- 这个代码注释,并删除掉,因为在服务端增加颜色码,会导致客户端翻译不出来
    public static String getNormalItemInfo(int itemId) {
        Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(itemId);
        if (itemBean == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[").append(getColorCode(itemBean.getColor())).append("]");
        sb.append(Manager.backpackManager.manager().getItemName(itemId)).append("[-]");
        return sb.toString();
    }

    private static String getColorCode(int color) {
        switch (color) {
            case 1:
                return "FFFFFF";
            case 2:
                return "00FF14";
            case 3:
                return "006BFF";
            case 4:
                return "E802E2";
            case 5:
                return "FC9300";
            case 6:
                return "FCEF00";
            case 7:
                return "F23634";
            default:
                return "000000";
        }
    }
*/
    //活动奖励物品要根据玩家职业来显示和发放
    //根据玩家职业从活动奖励物品字串中提取属于自己的物品，strItemList格式：itemModelId,itemNum,isBind,lostTime,strengLv,starLv,career;...
    public static String extractItemList(String strItemList, int playerCareer) {
        StringBuilder sb = new StringBuilder();
        String[] rewardItem = strItemList.split(Symbol.FENHAO);
        for (String item : rewardItem) {
            String[] itemArr = item.split(Symbol.DOUHAO);
            if (itemArr.length >= 7) {
                int career = Integer.parseInt(itemArr[6]);
                if (career != playerCareer) {
                    continue;
                }

                sb.append(itemArr[0]).append(Symbol.DOUHAO).append(itemArr[1]);
                if (!itemArr[5].equals("")) {
                    sb.append(Symbol.DOUHAO).append(itemArr[2]).append(Symbol.DOUHAO).append(itemArr[3]).append(Symbol.DOUHAO).append(itemArr[4]).append(Symbol.DOUHAO).append(itemArr[5]).append(Symbol.FENHAO);
                } else if (!itemArr[4].equals("")) {
                    sb.append(Symbol.DOUHAO).append(itemArr[2]).append(Symbol.DOUHAO).append(itemArr[3]).append(Symbol.DOUHAO).append(itemArr[4]).append(Symbol.FENHAO);
                } else if (!itemArr[3].equals("")) {
                    sb.append(Symbol.DOUHAO).append(itemArr[2]).append(Symbol.DOUHAO).append(itemArr[3]).append(Symbol.FENHAO);
                } else if (!itemArr[2].equals("")) {
                    sb.append(Symbol.DOUHAO).append(itemArr[2]).append(Symbol.FENHAO);
                }
            } else {
                sb.append(item).append(Symbol.FENHAO);
            }
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1); //去掉最后一个“;”
        }
        return sb.toString();
    }

    public static Item createOwnedItem(int itemModelId, long num, boolean bind) {
        Cfg_Item_Bean q_itemBean = CfgManager.getCfg_Item_Container().getValueByKey(itemModelId);
        if (q_itemBean == null) {
            logger.error("createItem物品模型ID " + itemModelId + "找不到");
            return null;
        }
        long maxValue = q_itemBean.getMax();
        if (itemModelId == ItemCoinType.EXP) {
            maxValue = Manager.currencyManager.manager().getMaxCurrencyLimit(ItemCoinType.EXP);
        }
        if (maxValue <= 0) {
            logger.error("createItem物品模型ID " + itemModelId + "最大堆叠数量问题");
            return null;
        }

        if (num > maxValue) {
            try {
                throw new Exception("createItem num > maxValue ：" + itemModelId + " stace : ");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return newItem(itemModelId, Math.min(num, maxValue), bind, q_itemBean.getDead_time(), q_itemBean.getType());
    }

    /**
     * 随机数量 奖励物品 转换正常配置
     * @param oldHelpReward
     * @return
     */
    public static ReadIntegerArrayEs randomNumAward(ReadIntegerArrayEs oldHelpReward){
        List<ReadArray<Integer>> itemlist= new ArrayList<>();
        for (ReadArray<Integer> array : oldHelpReward.getValuees()) {
            String itemStr = "";
            int itemModelId = array.get(0);
            int minnum = (int) (array.get(1));
            int maxnum = (int) (array.get(2));
            int bind = array.get(3);
            int career = array.get(4, 9);
            int num = RandomUtils.random(minnum,maxnum);
            itemStr = itemModelId +"_"+num + "_"+bind+"_"+career;
            ReadArray<Integer> newArray = new ReadIntegerArray(itemStr,"_");
            itemlist.add(newArray);
        }
        ReadIntegerArrayEs newHelpReward = new ReadIntegerArrayEs(itemlist);
        return newHelpReward;

    }
}
