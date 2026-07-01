package com.game.backpack.struct;

import com.data.CfgManager;
import com.data.bean.Cfg_Item_Bean;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.manager.Manager;
import com.game.player.structs.Player;

import com.game.utils.Symbol;
import game.core.util.AutoIncrementIntArray;
import game.core.util.DateFormatUtils;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public abstract class Item implements Comparable<Item>, Cloneable {

    private long id;

    private int itemModelId;

    private int num;

    private int gridId;
    //是否绑定
    private boolean bind;
    //失效时间
    private int losttime;

    /**
     * @param player
     * @param userNum  使用数量
     * @param actionId
     * @return
     */
    public abstract boolean use(Player player, int userNum, long actionId);

    public abstract boolean unuse(Player player, int unUseNum, long actionId);

    /**
     * 返回真实的物品数量，主要是争取有经验值的情况下的数量值
     *
     * @return 返回真实的数量
     */
    public long realNum() {
        return num;
    }

    /**
     * 道具翻倍 only use for itemId_count_....
     *
     * @param list
     * @param rate
     * @return
     */
    public static List<AutoIncrementIntArray> itemsPlus(List<AutoIncrementIntArray> list, int rate) {
        List<AutoIncrementIntArray> temps = new ArrayList<>();
        for (AutoIncrementIntArray item : list) {
            AutoIncrementIntArray temp = new AutoIncrementIntArray(item.length() + 1);

            for (int i = 0; i < item.length(); i++) {
                temp.set(i, item.get(i));
            }
            int count = item.get(1);
            temp.set(1, count * rate);
            //表示默认是绑定的
            temp.set(2, 1);
            temps.add(temp);
        }
        return temps;
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
     * 100金币 101元宝 102绑定元宝 103荣誉值 104真气值 105善恶值 106经验值 107铁精 108绑定金币 109成就 110帮贡
     * 111竞技场积分 112寻宝积分 113伙伴积分
     *
     * @return
     */
    public int getItemModelId() {
        return itemModelId;
    }

    /**
     * 100金币 101元宝 102绑定元宝 103荣誉值 104真气值 105善恶值 106经验值 107铁精 108绑定金币 109成就 110帮贡
     * 111竞技场积分 112寻宝积分
     *
     * @param itemModelId
     */
    public void setItemModelId(int itemModelId) {
        this.itemModelId = itemModelId;
    }

    public int getNum() {
        return num;
    }

    public Cfg_Item_Bean acqItemModel() {
        return CfgManager.getCfg_Item_Container().getValueByKey(itemModelId);
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

    /**
     * 判断能否叠加
     * @param target
     * @return boolean
     */
    public boolean canMerge(Item target) {
        if (target == null)
            return false;
        return canMerge(target.itemModelId, target.losttime, target.bind);
    }

    /**
     * 判断能否叠加
     * @param itemID
     * @param lt
     * @param bind
     * @return boolean
     */
    public boolean canMerge(int itemID, int lt, boolean bind) {
        Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(itemModelId);
        if (model == null || model.getMax() == 1)
            return false;
        return this.itemModelId == itemID && this.losttime == lt && this.bind == bind;
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
        return "[cellid=" + getGridId() + "][itemId=" + this.id + "][itemModleId=" + getItemModelId() + "][num=" + getNum() + "]";
    }

    /**
     * 是否己过时,不能用is
     *
     * @return
     */
    public boolean haveLost() {
        return losttime > 0 && TimeUtils.Time() > losttime * 1000L;
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

}
