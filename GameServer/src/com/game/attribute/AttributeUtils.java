package com.game.attribute;

import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.utils.RandomUtils;
import com.game.utils.Symbol;
import com.game.utils.Utils;
import game.core.util.AutoIncrementIntArray;
import game.core.util.AutoIncrementLongArray;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有属性点的添加，拷贝， 删除接口处理
 *
 */
public class AttributeUtils {

    /**
     * 属性求和
     *
     * @param attr
     * @param addAttr
     */
    public static void addAttribute(BaseIntAttribute attr, int[][] addAttr) {
        for (int[] addAttr1 : addAttr) {
            attr.addAttribute(addAttr1[0], addAttr1[1]);
        }
    }

    /**
     * 属性成倍计算
     */
    public static void attributeEnlarge(BaseIntAttribute attr, double rate){
        for (int i = 0; i < attr.getLength(); i++) {
            if (attr.getAdditionValue(i) > 0) {
                attr.setAttribute(i, (int) (attr.getAdditionValue(i) * rate));
            }
        }
    }

    /**
     * 属性成倍计算
     * */
    public static void attributeEnlarge(BaseIntAttribute attribute, int type, double rate) {
        attribute.setAttribute(type, (int) (attribute.getAdditionValue(type) * rate));
    }

    /**
     * 属性求和
     *
     * @param attr
     * @param addAttr
     */
    public static void addAttribute(BaseLongAttribute attr, BaseLongAttribute addAttr) {
        for (int i = 0; i < attr.getLength(); i++) {
            attr.addAttribute(i, addAttr.getAdditionValue(i));
        }
        attr.cleanMaxHP();
        attr.addMaxHP(addAttr.MaxHP());
    }
    /**
     * 属性求和
     *
     * @param attr
     * @param addAttr
     */
    public static void addAttribute(BaseLongAttribute attr, BaseIntAttribute addAttr) {
        for (int i = 0; i < attr.getLength(); i++) {
            attr.addAttribute(i, addAttr.getAdditionValue(i));
        }
        attr.cleanMaxHP();
        attr.addMaxHP(attr.MaxHP());
    }

    /**
     * 属性求和
     *
     * @param attr
     * @param addAttr
     */
    public static void addAttribute(BaseIntAttribute attr, BaseIntAttribute addAttr) {
        for (int i = 0; i < attr.getLength(); i++) {
            attr.addAttribute(i, addAttr.getAdditionValue(i));
        }
        attr.cleanMaxHP();
        attr.addMaxHP(attr.MaxHP());
    }

    public static void copyAttribute(BaseIntAttribute to, BaseIntAttribute from) {
        for (int i = 0; i < from.getLength(); i++) {
            to.addAttribute(i, from.getAdditionValue(i));
        }
        to.cleanMaxHP();
        to.addMaxHP(from.MaxHP());
        to.calFinalAttackSpeed();
        to.calFinalMoveSpeed();
    }

    public static void copyAttribute(BaseIntAttribute to, BaseLongAttribute from) {
        for (int i = 0; i < from.getLength(); i++) {
            to.addAttribute(i, (int)from.getAdditionValue(i));
        }
        to.cleanMaxHP();
        to.addMaxHP(from.MaxHP());
        to.calFinalAttackSpeed();
        to.calFinalMoveSpeed();
    }

    /**
     * 当前的属性做减法
     *
     * @param attr
     * @param addAttr
     */
    public static void subAttribute(BaseLongAttribute attr, BaseIntAttribute addAttr) {
        for (int i = 0; i < attr.getLength(); i++) {
            attr.addAttribute(i, 0 - addAttr.getAdditionValue(i));
        }
    }

    /**
     * 属性求和
     *
     * @param attr
     * @param addAttr
     */
    public static void addAttribute(BaseLongAttribute attr, AutoIncrementIntArray addAttr) {
        for (int i = 0; i < attr.getLength(); i++) {
            attr.addAttribute(i, addAttr.get(i));
        }
        attr.cleanMaxHP();
        attr.addMaxHP(attr.MaxHP());
    }
    /**
     * 属性求和
     *
     * @param attr
     * @param addAttr
     */
    public static void addAttribute(BaseIntAttribute attr, AutoIncrementIntArray addAttr) {
        for (int i = 0; i < attr.getLength(); i++) {
            attr.addAttribute(i, addAttr.get(i));
        }
        attr.cleanMaxHP();
        attr.addMaxHP(attr.MaxHP());
    }

    /**
     * 清理基础属性
     *
     * @param attr
     */
    public static void clean(BaseAttribute attr) {
        attr.clean();
    }

    /**
     * 初始化属性， 只支持 type_value:type_value ....格式
     *
     * @param arg
     * @param att
     */
    public static void init(String arg, BaseIntAttribute att) {
        String[] cells = arg.split(Symbol.FENHAO_REG);
        for (String cell : cells) {
            if (StringUtils.isBlank(arg)) {
                continue;
            }
            String[] at = cell.split(Symbol.XIAHUAXIAN_REG);
            int type = 0;
            int value = 0;
            try {
                type = Integer.parseInt(at[0].trim());
                value = Integer.parseInt(at[1].trim());
            } catch (Exception e) {
                continue;
            }
            if (type == 0) {
                continue;
            }
            att.addAttribute(type, value);
        }
    }

    /**
     * 初始化属性， 只支持 type_value:type_value ....格式
     *
     * @param list
     * @param att
     */
    public static void init(ReadIntegerArrayEs list, BaseIntAttribute att) {
        for (ReadArray<Integer> cell : list.getValuees()) {
            if (cell.size() < 0) {
                continue;
            }
            int type = cell.get(0);
            int value = cell.get(1);
            if (type == 0) {
                continue;
            }
            att.addAttribute(type, value);
        }
    }

    public static void AotuCopyData(AutoIncrementIntArray dest, AutoIncrementIntArray src) {
        if (dest == null) {
            return;
        }
        if (src == null) {
            return;
        }

        for (int i = 0; i < src.length(); i++) {
            dest.set(i, src.get(i));
        }
    }
    
     public static void CopyAotuLongToAutoLong(AutoIncrementLongArray dest, AutoIncrementLongArray src) {
        if (dest == null) {
            return;
        }
        if (src == null) {
            return;
        }

        for (int i = 0; i < src.length(); i++) {
            dest.set(i, src.get(i));
        }
    }


    public static void AotuIntToAutoLong(AutoIncrementLongArray dest, AutoIncrementIntArray src) {
        if (dest == null) {
            return;
        }
        if (src == null) {
            return;
        }

        for (int i = 0; i < src.length(); i++) {
            dest.set(i, src.get(i));
        }
    }

    public static void ListAutoIntToListAutoLong(List<AutoIncrementLongArray> dest, List<AutoIncrementIntArray> src) {
        if (dest == null) {
            return;
        }
        if (src == null) {
            return;
        }
        dest.clear();
        for (AutoIncrementIntArray aii : src) {
            AutoIncrementLongArray ail = new AutoIncrementLongArray(aii.length());
            AotuIntToAutoLong(ail, aii);
            dest.add(ail);
        }
    }

    /**
     * 属性翻倍(传入的是万分比)
     * @param base
     * @param rate
     * @return
     */
    public static List<AutoIncrementIntArray> rateAttributePercent(List<AutoIncrementIntArray> base, int rate){
        List<AutoIncrementIntArray> result = new ArrayList<>();
        for (AutoIncrementIntArray array : base) {
            AutoIncrementIntArray resultArray = new AutoIncrementIntArray();
            int value = (int) (array.get(1) * (1f + rate / 10000f));
            resultArray.set(0, array.get(0));
            resultArray.set(1, value);
            result.add(resultArray);
        }
        return result;
    }

    /**
     * 属性翻倍
     * @param base
     * @param rate
     * @return
     */
    public static List<AutoIncrementIntArray> rateAttribute(List<AutoIncrementIntArray> base, int rate){
        List<AutoIncrementIntArray> result = new ArrayList<>();
        for (AutoIncrementIntArray array : base) {
            AutoIncrementIntArray resultArray = new AutoIncrementIntArray();
            int value = (array.get(1) * (1 + rate));
            resultArray.set(0, array.get(0));
            resultArray.set(1, value);
            result.add(resultArray);
        }
        return result;
    }

    /**
     * 特殊的属性获取，目前只用在魂兽中
     * @param base
     * @param num
     * @return
     */
    public static ReadIntegerArrayEs specialAttributeCalc(ReadIntegerArrayEs base, int num){
        List<ReadArray<Integer>> result = new ArrayList<>();
        List<Integer> attributeType = new ArrayList<>();
        for (ReadArray<Integer> array : base.getValuees()) {
            attributeType.add(array.get(0));
        }
        //返回一个空的属性配置
        if (num > attributeType.size()){
            return Utils.toReadIntegerArrayEsByArray(result);
        }
        final List<Integer> chooseType = RandomUtils.mChooseN(attributeType, num);
        for (ReadArray<Integer> array : base.getValuees()) {
            if (chooseType.contains(array.get(0))){
                result.add(array);
            }
        }
        return Utils.toReadIntegerArrayEsByArray(result);
    }
}
