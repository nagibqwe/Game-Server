package common.attribute;

import com.data.CfgManager;
import com.data.bean.*;
import com.data.struct.ReadArray;
import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.attribute.script.IAttributeScript;
import com.game.holyEquip.struct.HolyEquipPart;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.structs.AttributeType;
import com.game.unrealEquip.struct.UnrealEquipPart;
import com.game.utils.MessageUtils;
import game.message.HolyEquipMessage;
import game.message.UnrealEquipMessage;
import org.apache.commons.lang.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 幻装属性
 */
public class UnrealEquipAttributeScript implements IAttributeScript {

    private final static Logger logger = LogManager.getLogger(UnrealEquipAttributeScript.class);

    private boolean suitEffect10 = false;
    private boolean suitEffect8  = false;
    private boolean suitEffect6  = false;
    private boolean suitEffect4  = false;
    private boolean suitEffect2  = false;


    @Override
    public PlayerAttributeType getType() {
        return PlayerAttributeType.UnrealEquip;
    }

    @Override
    public int getId() {
        return ScriptEnum.UnrealEquipAttributeScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    @Override
    public BaseIntAttribute getPlayerAttribute(Player player, boolean sycRank) {

        BaseIntAttribute att = player.PlayerCalculators().get(getType());
        if (att == null) {
            att = new BaseIntAttribute(AttributeType.ATTR_MAX);
            player.PlayerCalculators().put(getType(), att);
        }
        att.clean();
        BaseSystemIntAttribute systemAttr = player.PlayerCalSystemCulators().get(getType());
        calUrealSoulAttribute(player, att);
        calUnrealEquipBaseAttribute(player, att, systemAttr);
        calUnrealEquipSuitAttribute(player,att,null,1);
        int power = Manager.playerAttAttributeManager.deal().calcFightPower(att);
        if (sycRank) {
            //同步幻装战力
            UnrealEquipMessage.ResUnrealEquipFightPower.Builder msg = UnrealEquipMessage.ResUnrealEquipFightPower.newBuilder();
            msg.setFightPower(power);
            MessageUtils.send_to_player(player,   UnrealEquipMessage.ResUnrealEquipFightPower.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
        return att;
    }

    //计算幻魂
    private void  calUrealSoulAttribute(Player player,BaseIntAttribute att)
    {
        for (Integer key: player.getUnrealEquipBaseInfo().getUnrealSoulInfoList().keySet()) {
            int num =   player.getUnrealEquipBaseInfo().getUnrealSoulInfoList().get(key);
            if (num <=0)
                continue;
            Cfg_Item_Bean bean = CfgManager.getCfg_Item_Container().getValueByKey(key);
            if (bean == null)
                continue;
            for (ReadArray<Integer> effect :bean.getEffect_num().getValuees()) {
                att.addAttribute(effect.get(1), effect.get(2)*num);
            }
        }
    }
    private void calUnrealEquipBaseAttribute(Player player,BaseIntAttribute att,BaseSystemIntAttribute systemAttr) {
        for (UnrealEquipPart holyEquipPart:player.getUnrealEquipBaseInfo().getUnrealEquipPartList().values()) {
            if (holyEquipPart.getEquipItem() == null)
                continue;
            Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(holyEquipPart.getEquipItem().getItemModelId());
            if (bean == null)
                continue;
            for (ReadArray<Integer> baseATT:bean.getAttribute1().getValuees()) {
                att.addAttribute(baseATT.get(0), baseATT.get(1));
                //int attrType =  getAttributeType(baseATT.get(0));
                //int value =  attrType == 0 ?  baseATT.get(1): baseATT.get(1) * ((10000 + systemAttr.getAttribute(attrType)) / 10000);
                //att.addAttribute(baseATT.get(0), value);
            }
        }
    }

    private void calUnrealEquipSuitAttribute(Player player,BaseIntAttribute att,BaseSystemIntAttribute attSystem,int attType)
    {
         ConcurrentHashMap<Integer,Integer> suitHashMap =  new ConcurrentHashMap<>();
        for (UnrealEquipPart holyEquipPart: player.getUnrealEquipBaseInfo().getUnrealEquipPartList().values()) {
            if (holyEquipPart.getEquipItem()!=null) {
                Cfg_Equip_Bean bean =  CfgManager.getCfg_Equip_Container().getValueByKey(holyEquipPart.getEquipItem().getItemModelId());
                if (bean == null){
                    logger.error("Cfg_Equip_Bean  为空 " +   holyEquipPart.getEquipItem().getItemModelId()   );
                    continue;
                }
                int suitID = bean.getGrade() +10000;
                if (suitHashMap.get(suitID) == null) {
                    suitHashMap.put(suitID, 1);
                } else {
                    suitHashMap.put(suitID,suitHashMap.get(suitID) + 1);
                }
            }
        }
        calSuitAll( suitHashMap, att,attSystem,attType);
    }

    private void calSuitAll( ConcurrentHashMap<Integer,Integer> typelist,BaseIntAttribute att,BaseSystemIntAttribute attSystem ,int attType) {

        suitEffect2 = false;
        suitEffect4 = false;
        suitEffect6 = false;
        suitEffect8 = false;
        suitEffect10 = false;
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
            ReadArray<Integer>[] listadd = getSuitAtt(suitID,allNum,attType);
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

    private ReadArray<Integer>[] getSuitAtt(int sid, int num,int attType) {
        Cfg_Equip_Magic_suit_Bean bean = CfgManager.getCfg_Equip_Magic_suit_Container().getValueByKey(sid);
        ReadArray<Integer>[] readArrays = new ReadArray[]{};
        if (bean == null) {
            logger.error("Cfg_Equip_Magic_suit_Bean  为空 " +   sid   );
            return readArrays;
        }
        ReadArray<Integer>[] readArrays2 ;
        ReadArray<Integer>[] readArrays4 ;
        ReadArray<Integer>[] readArrays6 ;
        ReadArray<Integer>[] readArrays8 ;
        ReadArray<Integer>[] readArrays10 ;
        if (attType ==1){
            readArrays2 =  bean.getAttribute_2().getValuees();
            readArrays4 =  bean.getAttribute_4().getValuees();
            readArrays6 =  bean.getAttribute_6().getValuees();
            readArrays8 =  bean.getAttribute_8().getValuees();
            readArrays10 =  bean.getAttribute_10().getValuees();
        }else {
            //留给百分比属性准备的
            readArrays2 =  bean.getElementAttribute_2().getValuees();
            readArrays4 =  bean.getElementAttribute_4().getValuees();
            readArrays6 =  bean.getElementAttribute_6().getValuees();
            readArrays8 =  bean.getElementAttribute_8().getValuees();
            readArrays10 =  bean.getElementAttribute_10().getValuees();
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
            case 7:
                if (!suitEffect2) {
                    readArrays = (ReadArray<Integer>[]) ArrayUtils.addAll(readArrays,readArrays2);
                    suitEffect2  = true;
                }
                if (!suitEffect4) {
                    readArrays = (ReadArray<Integer>[]) ArrayUtils.addAll(readArrays, readArrays4);
                    suitEffect4 = true;
                }
                if (!suitEffect6) {
                    readArrays = (ReadArray<Integer>[]) ArrayUtils.addAll(readArrays,readArrays6);
                    suitEffect6 = true;
                }
                break;
            case 8:
            case 9:
                if (!suitEffect2) {
                    readArrays = (ReadArray<Integer>[]) ArrayUtils.addAll(readArrays,readArrays2);
                    suitEffect2  = true;
                }
                if (!suitEffect4) {
                    readArrays = (ReadArray<Integer>[]) ArrayUtils.addAll(readArrays, readArrays4);
                    suitEffect4 = true;
                }
                if (!suitEffect6) {
                    readArrays = (ReadArray<Integer>[]) ArrayUtils.addAll(readArrays,readArrays6);
                    suitEffect6 = true;
                }
                if (!suitEffect8) {
                    readArrays = (ReadArray<Integer>[]) ArrayUtils.addAll(readArrays,readArrays8);
                    suitEffect8= true;
                }
                break;
            case 10:
                if (!suitEffect2) {
                    readArrays = (ReadArray<Integer>[]) ArrayUtils.addAll(readArrays,readArrays2);
                    suitEffect2  = true;
                }
                if (!suitEffect4) {
                    readArrays = (ReadArray<Integer>[]) ArrayUtils.addAll(readArrays, readArrays4);
                    suitEffect4 = true;
                }
                if (!suitEffect6) {
                    readArrays = (ReadArray<Integer>[]) ArrayUtils.addAll(readArrays,readArrays6);
                    suitEffect6 = true;
                }
                if (!suitEffect8) {
                    readArrays = (ReadArray<Integer>[]) ArrayUtils.addAll(readArrays,readArrays8);
                    suitEffect8= true;
                }
                if (!suitEffect10) {
                    readArrays = (ReadArray<Integer>[]) ArrayUtils.addAll(readArrays,readArrays10);
                    suitEffect10= true;
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


        calUnrealEquipSuitAttribute(player,null,att,2);
        return att;
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
            logger.error("幻装加成属性错误");
        }
        return attrType;
    }
}
