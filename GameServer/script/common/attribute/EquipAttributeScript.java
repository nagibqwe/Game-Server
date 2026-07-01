package common.attribute;

import com.data.CfgManager;
import com.data.bean.Cfg_Equip_Bean;
import com.data.bean.Cfg_Equip_inten_class_Bean;
import com.data.bean.Cfg_Equip_suit_Bean;
import com.data.bean.Cfg_Wash_best_Bean;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.attribute.AttributeUtils;
import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.backpack.structs.Equip;
import com.game.equip.struct.EquipDefine;
import com.game.equip.struct.EquipWash;
import com.game.equip.struct.EquipPart;
import com.game.manager.Manager;
import com.game.attribute.script.IAttributeScript;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.structs.AttributeType;
import com.game.utils.IntegerMapHelp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class EquipAttributeScript implements IAttributeScript {

    private final static Logger logger = LogManager.getLogger(EquipAttributeScript.class);

    /**
     * 首饰套装
     */
    private final static int SUIT_TYPE_0 = 0;

    /**
     * 其他套装
     */
    private final static int SUIT_TYPE_1 = 1;

    @Override
    public PlayerAttributeType getType() {
        return PlayerAttributeType.EQUIP;
    }


    @Override
    public BaseIntAttribute getPlayerAttribute(Player player, boolean sycRank) {
        BaseIntAttribute att = player.PlayerCalculators().get(getType());
        if (att == null) {
            att = new BaseIntAttribute(AttributeType.ATTR_MAX);
            player.PlayerCalculators().put(getType(), att);
        }

        att.clean();

        //套装id列表
        HashMap<Integer, Integer> suitList = new HashMap<>();

        //用于计算强化属性，排行榜需要
        BaseIntAttribute strengthenAttr = new BaseIntAttribute(AttributeType.ATTR_MAX);

        for (EquipPart equipPart : player.getEquipParts()) {
            if (equipPart != null && null != equipPart.getEquip()) {
                Equip equip = equipPart.getEquip();
                if (equip.haveLost()) {
                    continue;
                }
                if (!equip.canEquip(player)) {
                    continue;
                }

                //fixBug 穿戴了装备的才计算属性
                if (equip.getGridId() < 0 || equip.getGridId() > 10) {
                    continue;
                }
                int power = compEquipAttr(att, equip);
                equipPart.setEquipPower(power);
            }
        }

        AttributeUtils.attributeEnlarge(att, AttributeType.ATTR_MaxHp,
                player.getSysAttriBute().getAttribute(AttributeType.ATTR_EquipBase_Hp) / 10000.0f + 1.0f);

        AttributeUtils.attributeEnlarge(att, AttributeType.ATTR_Def,
                player.getSysAttriBute().getAttribute(AttributeType.ATTR_EquipBase_Def) / 10000.0f + 1.0f);

        AttributeUtils.attributeEnlarge(att, AttributeType.ATTR_DefBreak,
                player.getSysAttriBute().getAttribute(AttributeType.ATTR_EquipBase_DefBreak) / 10000.0f + 1.0f);

        AttributeUtils.attributeEnlarge(att, AttributeType.ATTR_Atk,
                player.getSysAttriBute().getAttribute(AttributeType.ATTR_EquipBase_Atk) / 10000.0f + 1.0f);

        //logger.info(player.getName()+"equip base*sys Attr:"+":"+att.toString());

        for (EquipPart equipPart : player.getEquipParts()) {
            if (equipPart != null && null != equipPart.getEquip()) {
                Equip equip = equipPart.getEquip();
                if (equip.haveLost()) {
                    continue;
                }
                if (!equip.canEquip(player)) {
                    continue;
                }

                //fixBug 穿戴了装备的才计算属性
                if (equip.getGridId() < 0 || equip.getGridId() > 10) {
                    continue;
                }

                //logger.info(player.getName()+"equip qianghua level:" + equipPart.getStrengthenExcelId()+"::"+equip.getGridId());
                //logger.info(player.getName()+"equip xilian level:" + equipPart.getWashNum()+"::"+equip.getGridId());
                //计算部位强化带来的属性
                compEquipPartAttr(player, strengthenAttr, equipPart);

                //计算装备洗练属性
                calcEquipWashAttr(att, equip, equipPart);

                if (equip.getSuitId() > 0) {
                    IntegerMapHelp.put(suitList, equip.getSuitId(), 1);
                }
            }
        }

        //计算套装增加的属性
        calcEquipSuit_1(player, att);
        calcEquipSuit_2(player, att);
        for (int i = 1; i <= AttributeType.ATTR_MAX; i++) {
            att.addAttribute(i, strengthenAttr.getAdditionValue(i));
        }
        if (sycRank) {
            //更新排行榜装备和部位战力
            int power = Manager.playerAttAttributeManager.deal().calcFightPower(att);
            Manager.rankListManager.deal().setEquipPower(player, power);

            //更新排行榜强化战力
            int strengthenPower = Manager.playerAttAttributeManager.deal().calcFightPower(strengthenAttr);
            Manager.rankListManager.deal().setStrengthrenPower(player, strengthenPower);
        }
        return att;
    }

    /**
     * 计算部位强化的属性
     */
    private void compEquipPartAttr(Player player, BaseIntAttribute attribute, EquipPart part) {
        Manager.equipManager.deal().compEquipPartAttr(player, attribute, part);
    }

    /**
     * 现在装备上的属性直接读表
     */
    private int compEquipAttr(BaseIntAttribute att, Equip equip) {
        Cfg_Equip_Bean model = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
        if (model == null) {
            logger.error("错误的装备 modelId:" + equip.getItemModelId());
            return 0 ;
        }

        BaseIntAttribute temp = new BaseIntAttribute(AttributeType.ATTR_MAX);

        for (ReadArray<Integer> aii : model.getAttribute1().getValuees()) {
            att.addAttribute(aii.get(0), aii.get(1));
            temp.addAttribute(aii.get(0), aii.get(1));
        }

        for (ReadArray<Integer> aii : model.getAttribute2().getValuees()) {
            att.addAttribute(aii.get(0), aii.get(1));
            temp.addAttribute(aii.get(0), aii.get(1));
        }

        for (ReadArray<Integer> aii : model.getAttribute3().getValuees()) {
            att.addAttribute(aii.get(0), aii.get(1));
            temp.addAttribute(aii.get(0), aii.get(1));
        }

        return Manager.playerAttAttributeManager.deal().calcFightPower(temp);
    }

    private void calcEquipWashAttr(BaseIntAttribute att, Equip equip, EquipPart equipPart) {
        Cfg_Equip_Bean model = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
        for (int i = 0; i < model.getDiamond_Number(); i++) {
            EquipWash equipWash = equipPart.getEquipWashs().get(i + 1);
            if (equipWash == null){
                continue;
            }
            if (model.getWash().size() <= i) {
                continue;
            }
            ReadArray<Integer> prop = model.getWash().get(i);
            att.addAttribute(prop.get(0), equipWash.getPer() * (prop.get(2) - prop.get(1)) / 10000 + prop.get(1));
        }

        Cfg_Wash_best_Bean bestBean = CfgManager.getCfg_Wash_best_Container().getValueByKey(equipPart.getType() + 1000);
        if (bestBean == null) {
            return;
        }
        Iterator<EquipWash> iter = equipPart.getEquipWashs().values().iterator();
        int allPer = 0;
        while (iter.hasNext()) {
            allPer += iter.next().getPer();
        }

        if (allPer >= bestBean.getCondition()) {
            for (ReadArray<Integer> aii : bestBean.getAttribute().getValuees()) {
                att.addAttribute(aii.get(0), aii.get(1));
            }
        }
    }

    private void calcEquipSuit_1(Player player, BaseIntAttribute attribute) {
        boolean isCalOne = false;
        boolean isCalTwo = false;
        HashMap<Integer, Integer> suitMap = new HashMap<>();
        getSuitMap(player, suitMap, SUIT_TYPE_0);

        List<Integer> idList = new ArrayList<Integer>(suitMap.keySet());
        Collections.sort(idList);
        Collections.reverse(idList);
        int curLvMinGrad = 0;
        int curNum = 0;
        int lastSuitId = 0;

        for (int i = 0; i <= idList.size(); i++) {
            if (isCalOne && isCalTwo) {
                return;
            }
            boolean isMax = (i == idList.size());
            if (isMax || (curLvMinGrad != 0 && curLvMinGrad != (idList.get(i) / 10000))) {
                Cfg_Equip_suit_Bean bean = CfgManager.getCfg_Equip_suit_Container().getValueByKey(lastSuitId);
                if (curNum >= 1 && (!isCalOne)) {
                    addSuitAttr(attribute, bean.getAttribute_1());
                    isCalOne = true;
                }

                if (curNum >= 2 && (!isCalTwo)) {
                    addSuitAttr(attribute, bean.getAttribute_2());
                    isCalTwo = true;
                }

                if (isMax) {
                    return;
                }

                curNum = 0;
            }
            curNum += suitMap.get(idList.get(i));
            curLvMinGrad = idList.get(i) / 10000;
            lastSuitId = idList.get(i);
        }
    }

    private void calcEquipSuit_2(Player player, BaseIntAttribute attribute) {
        boolean isCalTwo = false;
        boolean isCalFour = false;
        boolean isCalSix = false;

        HashMap<Integer, Integer> suitMap = new HashMap<>();
        getSuitMap(player, suitMap, SUIT_TYPE_1);
        List<Integer> idList = new ArrayList<Integer>(suitMap.keySet());
        Collections.sort(idList);
        Collections.reverse(idList);
        int curLvGrad = 0;
        int curNum = 0;
        int lastSuitId = 0;

        for (int i = 0; i <= idList.size(); i++) {
            if (isCalTwo && isCalFour && isCalSix) {
                return;
            }
            boolean isMax = (i == idList.size());
            if (isMax || (curLvGrad != 0 && curLvGrad != (idList.get(i) / 10000))) {
                Cfg_Equip_suit_Bean bean = CfgManager.getCfg_Equip_suit_Container().getValueByKey(lastSuitId);


                if (curNum >= 2 && (!isCalTwo)) {
                    addSuitAttr(attribute, bean.getAttribute_2());
                    isCalTwo = true;
                }

                if (curNum >= 4 && (!isCalFour)) {
                    addSuitAttr(attribute, bean.getAttribute_4());
                    isCalFour = true;
                }

                if (curNum >= 6 && (!isCalSix)) {
                    addSuitAttr(attribute, bean.getAttribute_6());
                    isCalSix = true;
                }

                if (isMax) {
                    return;
                }
                curNum = 0;
            }
            curLvGrad = idList.get(i) / 10000;
            curNum += suitMap.get(idList.get(i));
            lastSuitId = idList.get(i);
        }
    }

    @Override
    public BaseSystemIntAttribute getPlayerSystemAttribute(Player player) {
        BaseSystemIntAttribute att = player.PlayerCalSystemCulators().get(getType());
        if (att == null) {
            att = new BaseSystemIntAttribute(AttributeType.SystemAttr_Max);
            player.PlayerCalSystemCulators().put(getType(), att);
        }
        att.clean();

        int totalLv = 0;
        for (EquipPart equipPart : player.getEquipParts()) {
            if (equipPart != null && null != equipPart.getEquip()) {
                totalLv += equipPart.getLevel();
            }
        }

        Cfg_Equip_inten_class_Bean bean = null;
        for (Cfg_Equip_inten_class_Bean tempBean : CfgManager.getCfg_Equip_inten_class_Container().getValuees()) {
            if (tempBean.getLevel() <= totalLv) {
                bean = tempBean;
            } else {
                break;
            }
        }

        if (null == bean) {
            return att;
        }

        for (ReadArray<Integer> aii : bean.getValue().getValuees()) {
            att.addSystemAttribute(aii.get(0), aii.get(1));
        }

        return att;
    }

    @Override
    public int getId() {
        return ScriptEnum.EquipAttributeScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    private void getSuitMap(Player player, HashMap<Integer, Integer> map, int type) {
        for (EquipPart equipPart : player.getEquipParts()) {
            if (null == equipPart || null == equipPart.getEquip()) {
                continue;
            }

            if (type == SUIT_TYPE_0) {
                if (equipPart.getType() != EquipDefine.EquipPart_Cuff &&
                        equipPart.getType() != EquipDefine.EquipPart_Ring) {
                    continue;
                }
            }

            if (type == SUIT_TYPE_1) {
                if (equipPart.getType() == EquipDefine.EquipPart_Cuff ||
                        equipPart.getType() == EquipDefine.EquipPart_Ring) {
                    continue;
                }
            }

            Equip equip = equipPart.getEquip();
            int forceStop = 0;
            getSuitParentMap(map, equip.getSuitId(), forceStop);
            if (forceStop >= 3) {
                logger.error("策划配置错误， 目前最多三阶段");
            }
        }
    }

    private void getSuitParentMap(HashMap<Integer, Integer> map, int suitId, int forceStop) {
        if (suitId == 0) {
            return;
        }
        if (forceStop >= 3) {
            return;
        }
        int suit = map.getOrDefault(suitId, 0);
        map.put(suitId, suit + 1);

        Cfg_Equip_suit_Bean bean = CfgManager.getCfg_Equip_suit_Container().getValueByKey(suitId);
        getSuitParentMap(map, bean.getParent_ID(), forceStop + 1);
    }

    private void addSuitAttr(BaseIntAttribute attribute, ReadIntegerArrayEs es) {
        for (ReadArray<Integer> aii : es.getValuees()) {
            attribute.addAttribute(aii.get(0), aii.get(1));
        }
    }

}
