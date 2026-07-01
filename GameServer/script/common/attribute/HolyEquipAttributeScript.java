package common.attribute;

import com.data.CfgManager;
import com.data.FunctionVariable;
import com.data.bean.Cfg_Equip_Bean;
import com.data.bean.Cfg_Equip_holy_levelup_Bean;
import com.data.bean.Cfg_Equip_holy_suit_Bean;
import com.data.bean.Cfg_Item_Bean;
import com.data.struct.ReadArray;
import com.game.attribute.AttributeUtils;
import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.holyEquip.struct.HolyEquipPart;
import com.game.manager.Manager;
import com.game.attribute.script.IAttributeScript;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.structs.AttributeType;
import com.game.utils.MessageUtils;
import game.message.HolyEquipMessage;
import org.apache.commons.lang.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.game.structs.AttributeType.*;

/**
 * Created by CXL on 2019/10/30.
 */
public class HolyEquipAttributeScript implements IAttributeScript {

    private final static Logger logger = LogManager.getLogger(EquipAttributeScript.class);
    @Override
    public PlayerAttributeType getType() {
        return PlayerAttributeType.HolyEquip;
    }

    private boolean suitEffect6 = false;
    private boolean suitEffect5 = false;
    private boolean suitEffect4 = false;
    private boolean suitEffect2 = false;

    @Override
    public int getId() {
        return ScriptEnum.HolyEquipAttributeScript;
    }

    public BaseIntAttribute getPlayerAttribute(Player player, boolean sycRank) {
        BaseIntAttribute att = player.PlayerCalculators().get(getType());
        if (att == null) {
            att = new BaseIntAttribute(AttributeType.ATTR_MAX);
            player.PlayerCalculators().put(getType(), att);
        }
        att.clean();
        BaseSystemIntAttribute systemAttr = player.PlayerCalSystemCulators().get(getType());
        calHolySoulAttribute(player, att);
        calHolyEquipBaseAttribute( player, att, systemAttr);
        calHolyEquipSuitAttribute(player,att,null,1);
        addHolyAllAttribute(att,systemAttr);

        int power = Manager.playerAttAttributeManager.deal().calcFightPower(att);
        Manager.holyEquipManager.getFightPowerMap().put(player.getId(), power);
        if(power > 0){
            Manager.controlManager.operate(player, FunctionVariable.EquipHolyPower, 1);
        }
        if (sycRank) {
            //同步圣装战力
            HolyEquipMessage.ResHolyEquipFightPower.Builder msg = HolyEquipMessage.ResHolyEquipFightPower.newBuilder();
            msg.setFightPower(power);
            MessageUtils.send_to_player(player,  HolyEquipMessage.ResHolyEquipFightPower.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            Manager.rankListManager.deal().setHolyEuiqpPower(player, power);
        }
        return att;
    }

    //计算圣魂
    private void  calHolySoulAttribute(Player player,BaseIntAttribute att)
    {
       for (Integer key: player.getHolyEquipBaseInfo().getHolySoulInfoList().keySet())
       {
          int num =   player.getHolyEquipBaseInfo().getHolySoulInfoList().get(key);
          if (num <=0)
              continue;
           Cfg_Item_Bean bean = CfgManager.getCfg_Item_Container().getValueByKey(key);
           if (bean == null)
               continue;
           for (ReadArray<Integer> effect :bean.getEffect_num().getValuees())
           {
               att.addAttribute(effect.get(1), effect.get(2)*num);
           }
       }
    }

    private void calHolyEquipBaseAttribute(Player player,BaseIntAttribute att,BaseSystemIntAttribute systemAttr)
    {
        for (HolyEquipPart holyEquipPart:player.getHolyEquipBaseInfo().getHolyEquipPartList().values())
        {
            if (holyEquipPart.getEquipItem() == null)
                continue;
            Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(holyEquipPart.getEquipItem().getItemModelId());
            if (bean == null)
                continue;
            for (ReadArray<Integer> baseATT:bean.getAttribute1().getValuees()) {
                int attrType =  getAttributeType(baseATT.get(0));
                int value =  attrType == 0 ?  baseATT.get(1): baseATT.get(1) * ((10000 + systemAttr.getAttribute(attrType)) / 10000);
                att.addAttribute(baseATT.get(0), value);
            }
            int partItemId = ( holyEquipPart.getPart() - 101)%11 * 10000 + holyEquipPart.getPartLevel();
            Cfg_Equip_holy_levelup_Bean holy_levelup_bean = CfgManager.getCfg_Equip_holy_levelup_Container().getValueByKey(partItemId);
            if (holy_levelup_bean == null)
                continue;
            for (ReadArray<Integer> partAtt:holy_levelup_bean.getAtt().getValuees()) {
                att.addAttribute(partAtt.get(0), partAtt.get(1));
            }
        }
    }

    private void calHolyEquipSuitAttribute(Player player,BaseIntAttribute att,BaseSystemIntAttribute attSystem,int attType)
    {
        ConcurrentHashMap<Integer,ConcurrentHashMap<Integer,Integer>> suitHashMap =  new ConcurrentHashMap<>();
        for (HolyEquipPart holyEquipPart: player.getHolyEquipBaseInfo().getHolyEquipPartList().values()) {
            if (holyEquipPart.getEquipItem()!=null) {
                Cfg_Equip_Bean bean =  CfgManager.getCfg_Equip_Container().getValueByKey(holyEquipPart.getEquipItem().getItemModelId());
                if (bean == null){
                    logger.error("Cfg_Equip_Bean  为空 " +   holyEquipPart.getEquipItem().getItemModelId()   );
                    continue;
                }
                int suitID = bean.getGrade() * 10000 + bean.getQuality() *100+ bean.getHolySuitType();
                int suitIndex = suitID % 10000;
                ConcurrentHashMap<Integer, Integer> sList = suitHashMap.get(suitIndex);
                if (sList == null) {
                    sList = new ConcurrentHashMap<>();
                    suitHashMap.put(suitIndex, sList);
                    sList.put(suitID, 1);
                } else {
                    if (sList.containsKey(suitID)){
                        sList.put(suitID, sList.get(suitID) + 1);
                    }else {
                        sList.put(suitID,1);
                    }
                }
            }
        }
        for (Integer key :suitHashMap.keySet()) {
            ConcurrentHashMap<Integer,Integer> typelist =  suitHashMap.get(key);
            if (typelist.size() <=0)
                continue;
            calSuitAll( typelist, att,attSystem, key,attType);
        }
    }

    private void calSuitAll( ConcurrentHashMap<Integer,Integer> typelist,BaseIntAttribute att,BaseSystemIntAttribute attSystem ,int type,int attType)
    {
        suitEffect5 = false;
        suitEffect2 = false;
        suitEffect4 = false;
        suitEffect6 = false;
        List<Integer> list  =new ArrayList<>();
        for (int suitID :typelist.keySet()) {
            if (suitID<2000)
                continue;
            list.add(suitID);
        }
        Collections.sort(list, (o1, o2) -> {
            if (o1 < o2) {
                return 1;
            } else if (o1 > o2) {
                return -1;
            }
            return 0;
        });

        int allNum = 0;
        for (Integer suitID:list) {
            int num =   typelist.get(suitID);
            allNum +=num;
            ReadArray<Integer>[] listadd = getSuitAtt(suitID,allNum,type,attType);
            if (listadd.length != 0) {
                for (ReadArray<Integer> aii : listadd) {
                    if (attType == 1)
                         att.addAttribute(aii.get(0), aii.get(1));
                    else
                        attSystem.addSystemAttribute(aii.get(0),aii.get(1));
                }
            }
        }
    }

    private ReadArray<Integer>[] getSuitAtt(int sid, int num,int type,int attType) {
        Cfg_Equip_holy_suit_Bean bean = CfgManager.getCfg_Equip_holy_suit_Container().getValueByKey(sid);
        ReadArray<Integer>[] readArrays = new ReadArray[]{};
        if (bean == null) {
            logger.error("Cfg_Equip_holy_suit_Bean  为空 " +   sid   );
            return readArrays;
        }
        ReadArray<Integer>[] readArrays2 = new ReadArray[]{};
        ReadArray<Integer>[] readArrays4 = new ReadArray[]{};
        ReadArray<Integer>[] readArrays5 = new ReadArray[]{};
        ReadArray<Integer>[] readArrays6 = new ReadArray[]{};
        if (attType ==1){
            readArrays2 =  bean.getAttribute_2().getValuees();
            readArrays4 =  bean.getAttribute_4().getValuees();
            readArrays5 =  bean.getAttribute_5().getValuees();
            readArrays6 =  bean.getAttribute_6().getValuees();
        }else {
            readArrays2 =  bean.getElementAttribute_2().getValuees();
            readArrays4 =  bean.getElementAttribute_4().getValuees();
            readArrays5 =  bean.getElementAttribute_5().getValuees();
            readArrays6 =  bean.getElementAttribute_6().getValuees();
        }
        if (type % 2 == 0 && num == 5) {
            if (!suitEffect5) {
                readArrays = (ReadArray<Integer>[]) ArrayUtils.addAll(readArrays, readArrays5);
                suitEffect5 =  true;
            }
            if (!suitEffect4) {
                readArrays = (ReadArray<Integer>[]) ArrayUtils.addAll(readArrays,readArrays4);
                suitEffect4 = true;
            }
            if (!suitEffect2) {
                readArrays = (ReadArray<Integer>[]) ArrayUtils.addAll(readArrays, readArrays2);
                suitEffect2 = true;
            }
            return readArrays;
        }
        switch (num) {
            case 3:
            case 2:
                if (!suitEffect2){
                     readArrays = (ReadArray<Integer>[]) ArrayUtils.addAll(readArrays, readArrays2);
                    suitEffect2  = true;
                }
                break;
            case 4:
            case 5:
                if (!suitEffect2) {
                    readArrays = (ReadArray<Integer>[]) ArrayUtils.addAll(readArrays, readArrays2);
                    suitEffect2  = true;
                }
                if (!suitEffect4) {
                    readArrays = (ReadArray<Integer>[]) ArrayUtils.addAll(readArrays, readArrays4);
                    suitEffect4 = true;
                }
                break;
            case 6:
                if (!suitEffect2) {
                    readArrays = (ReadArray<Integer>[]) ArrayUtils.addAll(readArrays,readArrays2);
                    suitEffect2  = true;
                }
                if (!suitEffect4) {
                    readArrays = (ReadArray<Integer>[]) ArrayUtils.addAll(readArrays, readArrays4);
                    suitEffect4 = true;
                }
                if (!suitEffect5) {
                    readArrays = (ReadArray<Integer>[]) ArrayUtils.addAll(readArrays, readArrays5);
                    suitEffect5 = true;
                }
                if (!suitEffect6) {
                    readArrays = (ReadArray<Integer>[]) ArrayUtils.addAll(readArrays,readArrays6);
                    suitEffect6 = true;
                }
                break;
            default:
                break;
        }
        return readArrays;
    }
    @Override
    public BaseSystemIntAttribute getPlayerSystemAttribute(Player player) {
        BaseSystemIntAttribute att = player.PlayerCalSystemCulators().get(getType());
        if (att == null) {
            att = new BaseSystemIntAttribute(AttributeType.SystemAttr_Max);
            player.PlayerCalSystemCulators().put(getType(), att);
        }
        att.clean();

        calHolyEquipSuitAttribute(player,null,att,2);
        return att;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    private int getAttributeType(int id) {
        int attrType =  0;
        switch (id) {
            case 1:
                attrType = AttributeType.ATTR_HolyBase_Atk;
                break;
            case 2:
                attrType = AttributeType.ATTR_HolyBase_Hp;
                break;
            case 3:
                attrType = AttributeType.ATTR_HolyBase_Break;
                break;
            case 4:
                attrType = AttributeType.ATTR_HolyBase_Df;
                break;
            default:
                break;
        }
        if (attrType == 0) {
            logger.error("圣装加成属性错误");
        }
        return attrType;
    }

    /**
     *
     * @param att
     * @param systemAtt
     */
    private void addHolyAllAttribute(BaseIntAttribute att, BaseSystemIntAttribute systemAtt) {
        for(int i = 1; i < systemAtt.getLength(); i++) {
            double rate = systemAtt.getAttribute(i) / 10000.0f + 1.0f;
            switch (i) {
                case  AttributeType.ATTR_HolyAll_Atk:
                    AttributeUtils.attributeEnlarge(att, ATTR_Atk, rate);
                    break;
                case AttributeType.ATTR_HolyAll_Hp:
                    AttributeUtils.attributeEnlarge(att, ATTR_MaxHp, rate);
                    break;
                case AttributeType.ATTR_HolyAll_Break:
                    AttributeUtils.attributeEnlarge(att, ATTR_DefBreak, rate);
                    break;
                case AttributeType.ATTR_HolyAll_Df:
                    AttributeUtils.attributeEnlarge(att, ATTR_Def, rate);
                    break;
                default:
                    break;
            }
        }
        //圣装总属性
        int value =   systemAtt.getAttribute(ATTR_HolyAll_Attribute);
        if (value<=0)
            return;
        for (int i = 1;i<att.getLength();i++) {
            double rate = value / 10000.0f + 1.0f;
            AttributeUtils.attributeEnlarge(att, i, rate);
        }
    }

}
