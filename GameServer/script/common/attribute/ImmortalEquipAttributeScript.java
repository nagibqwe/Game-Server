package common.attribute;

import com.data.CfgManager;
import com.data.bean.Cfg_Equip_Bean;
import com.data.bean.Cfg_Equip_xianjia_suit_Bean;
import com.data.struct.ReadArray;
import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.immortalequip.manager.ImmortalEquipManager;
import com.game.immortalequip.structs.ImmortalEquipPart;
import com.game.attribute.script.IAttributeScript;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.structs.AttributeType;
import com.game.utils.MessageUtils;
import game.message.ImmortalEquipMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Created by 542 on 2020/2/13.
 */
public class ImmortalEquipAttributeScript implements IAttributeScript {

    private final static Logger logger = LogManager.getLogger(EquipAttributeScript.class);
    @Override
    public PlayerAttributeType getType() {
        return PlayerAttributeType.ImmortalEquip;
    }

    @Override
    public int getId() {
        return ScriptEnum.ImmortalEquipAttributeScript;
    }

    @Override
    public BaseIntAttribute getPlayerAttribute(Player player, boolean sycRank) {
        BaseIntAttribute att = player.PlayerCalculators().get(getType());
        if (att == null) {
            att = new BaseIntAttribute(AttributeType.ATTR_MAX);
            player.PlayerCalculators().put(getType(), att);
        }
        att.clean();

        calcEquipbase( player,  att);

        //仙甲 套装属性计算
        //每套14个孔位
//        int suitNum = 14;
//        for (int i = 0;i<Manager.immortalEquipManager.MaxSuitNum;i++){
//            calcEquipSuit(player,att,Manager.immortalEquipManager.Suit_1_Start+suitNum*i,Manager.immortalEquipManager.Suit_1_End+suitNum*i,0);
//            calcEquipSuit(player,att,Manager.immortalEquipManager.Suit_2_Start+suitNum*i,Manager.immortalEquipManager.Suit_2_End+suitNum*i,1);
//            calcEquipSuit(player,att,Manager.immortalEquipManager.Suit_3_Start+suitNum*i,Manager.immortalEquipManager.Suit_3_End+suitNum*i,2);
//            calcEquipSuit(player,att,Manager.immortalEquipManager.Suit_4_Start+suitNum*i,Manager.immortalEquipManager.Suit_4_End+suitNum*i,3);
//        }
        calcEquipSuit(player, att);
        //同步排行榜
        if (sycRank) {
            int power = Manager.playerAttAttributeManager.deal().calcFightPower(att);
            Manager.rankListManager.deal().setImmEuiqpPower(player, power);
            //仙甲战力暂时 特殊处理 客户端计算不出来，暂由服务器发放
            ImmortalEquipMessage.ResSyncImmEquipFightPower.Builder msg = ImmortalEquipMessage.ResSyncImmEquipFightPower.newBuilder();
            msg.setFightPower(power);
            MessageUtils.send_to_player(player, ImmortalEquipMessage.ResSyncImmEquipFightPower.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            //同步八卦战斗力
            BaseIntAttribute baguaAtt = new BaseIntAttribute(AttributeType.ATTR_MAX);
            calcEquipBagua(player, baguaAtt);
            calcEquipSuitBagua(player, baguaAtt);
            int baguaPower = Manager.playerAttAttributeManager.deal().calcFightPower(baguaAtt);
            ImmortalEquipMessage.ResSyncImmEquipBaguaFightPower.Builder msgBagua = ImmortalEquipMessage.ResSyncImmEquipBaguaFightPower.newBuilder();
            msgBagua.setFightPower(baguaPower);
            MessageUtils.send_to_player(player, ImmortalEquipMessage.ResSyncImmEquipBaguaFightPower.MsgID.eMsgID_VALUE, msgBagua.build().toByteArray());
            //八卦排行
            Manager.rankListManager.deal().setBaguaPower(player, baguaPower);
        }
        return att;
    }

    private void calcEquipSuitBagua(Player player, BaseIntAttribute baguaAtt) {
        //玩家套装等级
        Map<Integer,Integer> suits = new HashMap<>();
        //玩家八卦任意四件套的等级
        Map<Integer,Integer> baguaFourSuits = new HashMap<>();
        for (Cfg_Equip_xianjia_suit_Bean suit_bean : CfgManager.getCfg_Equip_xianjia_suit_Container().getValuees()){
            if(suit_bean.getType() < 4){
                continue;
            }
            int suitKey = getSuitKey(suit_bean);
            Integer level = suits.get(suitKey);
            if(level == null){
                //计算套装
                for(int part : suit_bean.getPart().getValue()){
                    ImmortalEquipPart immortalEquipPart =  player.getImmortalEquipPartLisit().get(part);
                    if (immortalEquipPart == null || immortalEquipPart.getEquip() == null){
                        level = 0;
                        break;
                    }
                    Cfg_Equip_Bean equip_bean = CfgManager.getCfg_Equip_Container().getValueByKey(immortalEquipPart.getEquip().getItemModelId());
                    if (equip_bean!=null){
                        if (level == null || equip_bean.getDiamond_Number()  < level){
                            level = equip_bean.getDiamond_Number();
                        }
                    }
                }
                suits.put(suitKey, level);
            }
            //计算八卦四件套
            if(suit_bean.getType() == 5 && baguaFourSuits.get(suitKey) == null){
                //计算四件套
                Integer[] parts = suit_bean.getPart().getValue();
                int length = parts.length;
                int[] levels = new int[length];
                for(int i = 0; i < length; i++){
                    ImmortalEquipPart immortalEquipPart =  player.getImmortalEquipPartLisit().get(parts[i]);
                    if (immortalEquipPart == null || immortalEquipPart.getEquip() == null){
                        continue;
                    }
                    Cfg_Equip_Bean equip_bean = CfgManager.getCfg_Equip_Container().getValueByKey(immortalEquipPart.getEquip().getItemModelId());
                    if (equip_bean!=null){
                        levels[i] = equip_bean.getDiamond_Number();
                    }
                }
                Arrays.sort(levels);
                //四件套最小等级
                int fourLevel = levels[4];
                baguaFourSuits.put(suitKey, fourLevel);
            }
        }

        //计算玩家套装战力
        for (Cfg_Equip_xianjia_suit_Bean suit_bean : CfgManager.getCfg_Equip_xianjia_suit_Container().getValuees()) {
            int suitKey = getSuitKey(suit_bean);
            Integer level = suits.get(suitKey);
            if(level != null && suit_bean.getLevel() == level.intValue()){
                for (ReadArray<Integer> aii : suit_bean.getAttribute().getValuees()) {
                    baguaAtt.addAttribute(aii.get(0), aii.get(1));
                }
            }
            if(suit_bean.getType() == 5){
                level = baguaFourSuits.get(suitKey);
                if(level != null && suit_bean.getLevel() == level.intValue()){
                    for (ReadArray<Integer> aii : suit_bean.getAttribute1().getValuees()) {
                        baguaAtt.addAttribute(aii.get(0), aii.get(1));
                    }
                }
            }
        }
    }

    private void calcEquipBagua(Player player, BaseIntAttribute att) {
        for (ImmortalEquipPart immortalEquipPart:player.getImmortalEquipPartLisit().values()){
            if( immortalEquipPart.getEquip()!=null
                    && immortalEquipPart.getPart() >= ImmortalEquipManager.baguaMinPart
                    && immortalEquipPart.getPart() <= ImmortalEquipManager.baguaMaxPart){
                Cfg_Equip_Bean  equip_bean = CfgManager.getCfg_Equip_Container().getValueByKey( immortalEquipPart.getEquip().getItemModelId());
                if (equip_bean == null){
                    logger.info("equip_bean == null  " + immortalEquipPart.getEquip().getItemModelId());
                    continue;
                }
                if ( equip_bean.getAttribute1()!=null && equip_bean.getAttribute1().size()>0){
                    for (ReadArray<Integer> aii : equip_bean.getAttribute1().getValuees()) {
                        att.addAttribute(aii.get(0), aii.get(1));
                    }
                }
                if ( equip_bean.getAttribute2()!=null && equip_bean.getAttribute2().size()>0){
                    for (ReadArray<Integer> aii : equip_bean.getAttribute2().getValuees()) {
                        att.addAttribute(aii.get(0), aii.get(1));
                    }
                }
            }
        }
    }

    private void calcEquipbase(Player player, BaseIntAttribute att){
        for (ImmortalEquipPart immortalEquipPart:player.getImmortalEquipPartLisit().values()){
            if( immortalEquipPart.getEquip()!=null){
                Cfg_Equip_Bean  equip_bean = CfgManager.getCfg_Equip_Container().getValueByKey( immortalEquipPart.getEquip().getItemModelId());
                if (equip_bean == null){
                    logger.info("equip_bean == null  " + immortalEquipPart.getEquip().getItemModelId());
                    continue;
                }
                if ( equip_bean.getAttribute1()!=null && equip_bean.getAttribute1().size()>0){
                    for (ReadArray<Integer> aii : equip_bean.getAttribute1().getValuees()) {
                        att.addAttribute(aii.get(0), aii.get(1));
                    }
                }
                if ( equip_bean.getAttribute2()!=null && equip_bean.getAttribute2().size()>0){
                    for (ReadArray<Integer> aii : equip_bean.getAttribute2().getValuees()) {
                        att.addAttribute(aii.get(0), aii.get(1));
                    }
                }
            }
        }
    }

    private void calcEquipSuit(Player player, BaseIntAttribute attribute,int start,int end,int type){
        int minLevel = 999;
        for ( int i = start;i<end;i++){
            ImmortalEquipPart immortalEquipPart =  player.getImmortalEquipPartLisit().get(i);
            if (immortalEquipPart == null || immortalEquipPart.getEquip() == null)
                return;
            Cfg_Equip_Bean equip_bean = CfgManager.getCfg_Equip_Container().getValueByKey(immortalEquipPart.getEquip().getItemModelId());
            if (equip_bean!=null){
                if (equip_bean.getDiamond_Number()  < minLevel){
                    minLevel  = equip_bean.getDiamond_Number();
                }
            }
        }

        for (Cfg_Equip_xianjia_suit_Bean suit_bean :   CfgManager.getCfg_Equip_xianjia_suit_Container().getValuees()){
            if (suit_bean.getType() == type && suit_bean.getLevel() == minLevel){
                if ( suit_bean.getAttribute()!=null && suit_bean.getAttribute().size()>0){
                    for (ReadArray<Integer> aii : suit_bean.getAttribute().getValuees()) {
                        attribute.addAttribute(aii.get(0), aii.get(1));
                    }
                }
            }
        }
    }

    /**
     * 计算套装
     * @param player 玩家
     */
    private void calcEquipSuit(Player player, BaseIntAttribute attribute){
        //玩家套装等级
        Map<Integer,Integer> suits = new HashMap<>();
        //玩家八卦任意四件套的等级
        Map<Integer,Integer> baguaFourSuits = new HashMap<>();
        for (Cfg_Equip_xianjia_suit_Bean suit_bean : CfgManager.getCfg_Equip_xianjia_suit_Container().getValuees()){
            int suitKey = getSuitKey(suit_bean);
            Integer level = suits.get(suitKey);
            if(level == null){
                //计算套装
                for(int part : suit_bean.getPart().getValue()){
                    ImmortalEquipPart immortalEquipPart =  player.getImmortalEquipPartLisit().get(part);
                    if (immortalEquipPart == null || immortalEquipPart.getEquip() == null){
                        level = 0;
                        break;
                    }
                    Cfg_Equip_Bean equip_bean = CfgManager.getCfg_Equip_Container().getValueByKey(immortalEquipPart.getEquip().getItemModelId());
                    if (equip_bean!=null){
                        if (level == null || equip_bean.getDiamond_Number()  < level){
                            level = equip_bean.getDiamond_Number();
                        }
                    }
                }
                suits.put(suitKey, level);
            }
            //计算八卦四件套
            if(suit_bean.getType() == 5 && baguaFourSuits.get(suitKey) == null){
                //计算四件套
                Integer[] parts = suit_bean.getPart().getValue();
                int length = parts.length;
                int[] levels = new int[length];
                for(int i = 0; i < length; i++){
                    ImmortalEquipPart immortalEquipPart =  player.getImmortalEquipPartLisit().get(parts[i]);
                    if (immortalEquipPart == null || immortalEquipPart.getEquip() == null){
                        continue;
                    }
                    Cfg_Equip_Bean equip_bean = CfgManager.getCfg_Equip_Container().getValueByKey(immortalEquipPart.getEquip().getItemModelId());
                    if (equip_bean!=null){
                        levels[i] = equip_bean.getDiamond_Number();
                    }
                }
                Arrays.sort(levels);
                //四件套最小等级
                int fourLevel = levels[4];
                baguaFourSuits.put(suitKey, fourLevel);
            }
        }

        //计算玩家套装战力
        for (Cfg_Equip_xianjia_suit_Bean suit_bean : CfgManager.getCfg_Equip_xianjia_suit_Container().getValuees()) {
            int suitKey = getSuitKey(suit_bean);
            Integer level = suits.get(suitKey);
            if(level != null && suit_bean.getLevel() == level.intValue()){
                for (ReadArray<Integer> aii : suit_bean.getAttribute().getValuees()) {
                    attribute.addAttribute(aii.get(0), aii.get(1));
                }
            }
            if(suit_bean.getType() == 5){
                level = baguaFourSuits.get(suitKey);
                if(level != null && suit_bean.getLevel() == level.intValue()){
                    for (ReadArray<Integer> aii : suit_bean.getAttribute1().getValuees()) {
                        attribute.addAttribute(aii.get(0), aii.get(1));
                    }
                }
            }
        }
    }

    private Integer getSuitKey(Cfg_Equip_xianjia_suit_Bean suit_bean) {
        return suit_bean.getOder() * 10000 + suit_bean.getType();
    }

    public BaseSystemIntAttribute getPlayerSystemAttribute(Player player) {
        BaseSystemIntAttribute att = player.PlayerCalSystemCulators().get(getType());
        if (att == null) {
            att = new BaseSystemIntAttribute(AttributeType.SystemAttr_Max);
            player.PlayerCalSystemCulators().put(getType(), att);
        }
        att.clean();
        return att;
    }
    @Override
    public Object call(Object... objects) {
        return null;
    }
}
