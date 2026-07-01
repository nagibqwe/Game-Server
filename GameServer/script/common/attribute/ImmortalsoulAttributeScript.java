package common.attribute;


import com.data.CfgManager;
import com.data.bean.Cfg_Immortal_soul_attribute_Bean;
import com.data.bean.Cfg_Immortal_soul_core_att_Bean;
import com.data.container.Cfg_Immortal_soul_core_att_Container;
import com.data.struct.ReadArray;
import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.immortalsoul.structs.Immortalsoul;
import com.game.attribute.script.IAttributeScript;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.structs.AttributeType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Created by 542 on 2019/7/5.
 */
public class ImmortalsoulAttributeScript implements IAttributeScript {

    private final static Logger logger = LogManager.getLogger(EquipAttributeScript.class);
    @Override
    public PlayerAttributeType getType() {
        return PlayerAttributeType.Immortalsoul;
    }


    private int getAttributeType(int id) {
        int attrType =  0;
        id = id - 1000;
        switch (id) {
            case AttributeType.ATTR_EquipBase_Hp:
                attrType = AttributeType.ATTR_MaxHp;
                break;
            case AttributeType.ATTR_EquipBase_Def:
                attrType = AttributeType.ATTR_Def;
                break;
            case AttributeType.ATTR_EquipBase_DefBreak:
                attrType = AttributeType.ATTR_DefBreak;
                break;
            case AttributeType.ATTR_EquipBase_Atk:
                attrType = AttributeType.ATTR_Atk;
                break;
            default:
                break;
        }
        if(attrType == 0){
            logger.error("灵魄百分比属性 ID 配置错误 " + id);
        }

        return attrType;
    }
    @Override
    public BaseIntAttribute getPlayerAttribute(Player player, boolean sycRank) {
        BaseIntAttribute att = player.PlayerCalculators().get(getType());
        if (att == null) {
            att = new BaseIntAttribute(AttributeType.ATTR_MAX);
            player.PlayerCalculators().put(getType(), att);
        }
        att.clean();

        for (Immortalsoul soul:  player.getEquipSouls().values()){
            Cfg_Immortal_soul_attribute_Bean bean = CfgManager.getCfg_Immortal_soul_attribute_Container().getValueByKey(soul.getItemID());
            //基础属性+++
            if ( bean.getDemand_value().size()>0){
                for (ReadArray<Integer> basis : bean.getDemand_value().getValuees()){
                    att.addAttribute(basis.get(0), basis.get(1));
                }
            }
            if (bean.getBasic_attributes().size()>0) {
                //等级 属性添加 (level-1)*value
                for (ReadArray<Integer> levelAdd : bean.getBasic_attributes().getValuees()) {
                    att.addAttribute(levelAdd.get(0), levelAdd.get(1) * (soul.getLevel() - 1));
                }
            }

        }

        //计算每个剑灵的灵魂核心属性
        for(Map.Entry<Integer, Integer> data : player.getImmortalsoulCores().entrySet()){
            if(data.getValue() > 0){
                Cfg_Immortal_soul_core_att_Bean cfg_att = Cfg_Immortal_soul_core_att_Container.GetInstance().getValueByKey(data.getValue());
                if(cfg_att != null && cfg_att.getAdd_att().size() > 0){
                    for (ReadArray<Integer> add : cfg_att.getAdd_att().getValuees()) {
                        att.addAttribute(add.get(0), add.get(1));
                    }
                }
            }
        }

        if (sycRank) {
            int power = Manager.playerAttAttributeManager.deal().calcFightPower(att);
            Manager.rankListManager.deal().setImmortalsoulPower(player, power);
        }

        return att;

//
//        BaseIntAttribute equipAtt =  player.PlayerCalculators().get(PlayerAttributeType.EQUIP);
//        BaseSystemIntAttribute systemAttr = player.PlayerCalSystemCulators().get(getType());
//        Iterator<Immortalsoul> iter = player.getEquipSouls().values().iterator();
//        while (iter.hasNext()) {
//            Immortalsoul soul = iter.next();
//            if (soul.getLocation() == ImmortalsoulDefine.Location_0) {
//                continue;
//            }
//            calcSoul(soul, att,equipAtt,systemAttr);
//        }


       // AttributeUtils.attributeEnlarge(att, AttributeType.ATTR_MaxHp,
       //         systemAttr.getAttribute(AttributeType.ATTR_EquipBase_Hp) / 10000.0f + 1.0f);
       // AttributeUtils.attributeEnlarge(att, AttributeType.ATTR_Def,
       //         systemAttr.getAttribute(AttributeType.ATTR_EquipBase_Def) / 10000.0f + 1.0f);
       // AttributeUtils.attributeEnlarge(att, AttributeType.ATTR_DefBreak,
       //         systemAttr.getAttribute(AttributeType.ATTR_EquipBase_Break) / 10000.0f + 1.0f);
       // AttributeUtils.attributeEnlarge(att, AttributeType.ATTR_Atk,
       //         systemAttr.getAttribute(AttributeType.ATTR_EquipBase_Atk) / 10000.0f + 1.0f);
//        return att;
    }
    @Override
    public BaseSystemIntAttribute getPlayerSystemAttribute(Player player) {
        BaseSystemIntAttribute att = player.PlayerCalSystemCulators().get(getType());
        if (att == null) {
            att = new BaseSystemIntAttribute(AttributeType.SystemAttr_Max);
            player.PlayerCalSystemCulators().put(getType(), att);
        }
        att.clean();

        for (Immortalsoul soul:  player.getEquipSouls().values()){
            Cfg_Immortal_soul_attribute_Bean bean = CfgManager.getCfg_Immortal_soul_attribute_Container().getValueByKey(soul.getItemID());
            if (bean.getDemand_value_percent().size()>0){
                for (ReadArray<Integer> basis : bean.getDemand_value_percent().getValuees()){
                    att.addSystemAttribute(basis.get(0), basis.get(1));
                }
            }
            if (bean.getBasic_attributes_percent().size()>0){
                //等级 属性添加 (level-1)*value
                for (ReadArray<Integer> levelAdd : bean.getBasic_attributes_percent().getValuees()){
                    att.addSystemAttribute(levelAdd.get(0), levelAdd.get(1)*(soul.getLevel()-1));
                }
            }
        }
        return att;

//        Iterator<Immortalsoul> iter = player.getEquipSouls().values().iterator();
//        while (iter.hasNext()) {
//            Immortalsoul soul = iter.next();
//            if (soul.getLocation() == ImmortalsoulDefine.Location_0) {
//                continue;
//            }
//            calcSoulBase(soul, att);
//        }
//        return att;
    }

    private void calcSoulBase(Immortalsoul soul, BaseSystemIntAttribute att){
        Cfg_Immortal_soul_attribute_Bean bean = CfgManager.getCfg_Immortal_soul_attribute_Container().getValueByKey(soul.getItemID());
        if (bean!=null){
            //百分比属性+++
            if (bean.getDemand_value_percent().size()>0){
                for (ReadArray<Integer> basis : bean.getDemand_value_percent().getValuees()){
                    att.addSystemAttribute(basis.get(0), basis.get(1));
                }
            }
            if (bean.getBasic_attributes_percent().size()>0){
                //等级 属性添加 (level-1)*value
                for (ReadArray<Integer> levelAdd : bean.getBasic_attributes_percent().getValuees()){
                    att.addSystemAttribute(levelAdd.get(0), levelAdd.get(1)*(soul.getLevel()-1));
                }
            }
        }
    }

   private  void calcSoul(Immortalsoul soul, BaseIntAttribute att, BaseIntAttribute equipAtt, BaseSystemIntAttribute systemAttr) {

       Cfg_Immortal_soul_attribute_Bean bean = CfgManager.getCfg_Immortal_soul_attribute_Container().getValueByKey(soul.getItemID());
       if (bean!=null){

           //基础属性+++
           if ( bean.getDemand_value().size()>0){
               for (ReadArray<Integer> basis : bean.getDemand_value().getValuees()){
                   att.addAttribute(basis.get(0), basis.get(1));
               }
           }
           if (bean.getBasic_attributes().size()>0) {
               //等级 属性添加 (level-1)*value
               for (ReadArray<Integer> levelAdd : bean.getBasic_attributes().getValuees()) {
                   att.addAttribute(levelAdd.get(0), levelAdd.get(1) * (soul.getLevel() - 1));
               }
           }

           //百分比属性+++
           int actype  = 0;
           int baseValue = 0;
           int addValue = 0;
           int sysType = 0;
           if (bean.getDemand_value_percent().size()>0){
               for (ReadArray<Integer> basis : bean.getDemand_value_percent().getValuees()){
                   actype =  getAttributeType(basis.get(0));
                   baseValue =  equipAtt.getAdditionValue(actype);
                   sysType = basis.get(0)- 1000;
                   addValue = (int)(baseValue *   (systemAttr.getAttribute(sysType) / 10000f));
                   att.addAttribute(actype,addValue);
               }
           }
       }
   }


    @Override
    public int getId() {
        return ScriptEnum.ImmortalSoulAttributeScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }
}
