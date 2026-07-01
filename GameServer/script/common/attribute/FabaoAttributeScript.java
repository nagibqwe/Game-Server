package common.attribute;

import com.data.CfgManager;
import com.data.FunctionStart;
import com.data.bean.*;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.attribute.AttributeUtils;
import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.manager.Manager;
import com.game.nature.structs.Drug;
import com.game.nature.structs.Huaxin;
import com.game.attribute.script.IAttributeScript;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.statestifle.structs.PlayerSateStifleData;
import com.game.statestifle.structs.SoulSpiritInfo;
import com.game.structs.AttributeType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.game.structs.AttributeType.*;
import static com.game.structs.AttributeType.ATTR_Def;

public class FabaoAttributeScript implements IAttributeScript {

    private final static Logger log = LogManager.getLogger(FabaoAttributeScript.class);

    @Override
    public int getId() {
        return ScriptEnum.StifleFabaoAttributeScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public PlayerAttributeType getType() {
        return PlayerAttributeType.StifleFabao;
    }

    @Override
    public BaseIntAttribute getPlayerAttribute(Player player, boolean sycRank) {
        BaseIntAttribute att = player.PlayerCalculators().get(getType());
        if (att == null) {
            att = new BaseIntAttribute(AttributeType.ATTR_MAX);
            player.PlayerCalculators().put(getType(), att);
        }
        att.clean();

        if(!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.RealmStifle)) {
            return att;
        }
        //计算灵压法宝属性
        calculateFabaoAttribute(player, att);
        //同步排行榜
        if (sycRank) {
            int power = Manager.playerAttAttributeManager.deal().calcFightPower(att);
            player.getStifleData().getNature().setPower(power);
            Manager.rankListManager.deal().setMagicWeaponDamage(player, power);
        }
        return att;
    }

    private void calculateFabaoAttribute(Player player, BaseIntAttribute att) {
        //法宝自身属性加成
        PlayerSateStifleData stifleData = player.getStifleData();
        int configId = Manager.stateStifleManager.deal().getConfigIdByLevelAndStar(stifleData.getLevel(), stifleData.getStar());
        Cfg_State_stifle_Bean sbean = CfgManager.getCfg_State_stifle_Container().getValueByKey(configId);
        if (sbean == null) {
            log.error("Cfg_State_stifle_Bean配置表不存在：" + configId);
            return;
        }
        for (int i = 0; i < sbean.getAtt().size(); i++) {
            ReadArray<Integer> array = sbean.getAtt().get(i);
            att.addAttribute(array.get(0), array.get(1));
        }
        BaseSystemIntAttribute systemAtt = player.getSysAttriBute();
        for (int i = 1; i < systemAtt.getLength(); i++) {
            double rate = systemAtt.getAttribute(i) / 10000.0f + 1.0f;
            switch (i) {
                case ATTR_Fabao_Attack:
                    AttributeUtils.attributeEnlarge(att, ATTR_Atk, rate);
                    break;
                case ATTR_Fabao_Hp:
                    AttributeUtils.attributeEnlarge(att, ATTR_MaxHp, rate);
                    break;
                case ATTR_Fabao_DefBreak:
                    AttributeUtils.attributeEnlarge(att, ATTR_DefBreak, rate);
                    break;
                case ATTR_Fabao_Defence:
                    AttributeUtils.attributeEnlarge(att, ATTR_Def, rate);
                    break;
                default:
                    break;
            }
        }

        for (Drug drug : player.getStifleData().getNature().getDrugs().values()) {
            int belongType = drug.getBelongType();
            int pos = drug.getPos();
            int excelId;
            Cfg_Nature_att_Bean bean;
            for (int i = 0; i < drug.getLevel(); i++) {
                excelId = belongType * 1000 + pos * 100 + i;
                bean = CfgManager.getCfg_Nature_att_Container().getValueByKey(excelId);
                int levelLimit = bean.getLeve_limit();

                for (ReadArray array : bean.getAttribute().getValuees()) {
                    att.addAttribute((int) array.get(0), (int) array.get(1) * levelLimit);
                }
            }

            bean = CfgManager.getCfg_Nature_att_Container().getValueByKey(drug.getExcelId());
            if (null != bean) {
                for (ReadArray array : bean.getAttribute().getValuees()) {
                    att.addAttribute((int) array.get(0), (int) array.get(1) * drug.getUseNumber());
                }
            }
        }
        
        /*
          化形的属性，要对每个化形进行计算
          属性等于表上rent_att*（星级+1）【已激活】
          */
        for (Huaxin huaxin : player.getStifleData().getNature().getHuaxins().values()) {
            int level = huaxin.getLevel();
            Cfg_Huaxingfabao_Bean bean = CfgManager.getCfg_Huaxingfabao_Container().getValueByKey(huaxin.getExcelId());
            ReadIntegerArrayEs arrays = bean.getRent_att();
            if (null != arrays) {
                for (ReadArray array : arrays.getValuees()) {
                    att.addAttribute((int) array.get(0), (int)array.get(1) + (int)array.get(2) * level);
                }
            }
        }

        //计算法宝器灵属性
        calSoulSpiritAttribute(player, att);
    }

    private void calSoulSpiritAttribute(Player player, BaseIntAttribute att) {

        int totalPromoteLv = 0;
        for (SoulSpiritInfo info : player.getStifleData().getSpiritMap().values()) {
            totalPromoteLv += info.getPromoteLv();
            //晋升属性加成
            int promoteConfigId = info.getId() * 100 + info.getPromoteLv();
            Cfg_State_stifle_add_level_Bean promoteBean = CfgManager.getCfg_State_stifle_add_level_Container().getValueByKey(promoteConfigId);
            if (promoteBean == null) {
                log.error("Cfg_State_stifle_add_level_Bean配置表不存在：" + promoteConfigId);
                continue;
            }
            for (int i = 0; i < promoteBean.getAttribute().size(); i++) {
                att.addAttribute(promoteBean.getAttribute().get(i).get(0), promoteBean.getAttribute().get(i).get(1));
            }

            //进化属性加成
            int evolveConfigId = info.getId() * 100 + info.getEvolveLv();
            Cfg_State_stifle_add_Bean evolveBean = CfgManager.getCfg_State_stifle_add_Container().getValueByKey(evolveConfigId);
            if (evolveBean == null) {
                log.error("Cfg_State_stifle_add_Bean配置表不存在：" + promoteConfigId);
                continue;
            }
            for (int i = 0; i < evolveBean.getAttribute().size(); i++) {
                att.addAttribute(evolveBean.getAttribute().get(i).get(0), evolveBean.getAttribute().get(i).get(1));
            }
        }

        //晋升总等级属性加成
        Cfg_State_stifle_add_level_all_Bean bean = null;
        for (Cfg_State_stifle_add_level_all_Bean tempBean : CfgManager.getCfg_State_stifle_add_level_all_Container().getValuees()) {
            if (totalPromoteLv >= tempBean.getLevel_all()) {
                bean = tempBean;
            }
        }
        if (bean != null) {
            for (int i = 0; i < bean.getAttribute().size(); i++) {
                att.addAttribute(bean.getAttribute().get(i).get(0), bean.getAttribute().get(i).get(1));
            }
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

        for (Drug drug : player.getStifleData().getNature().getDrugs().values()){
            int excelId = drug.getExcelId();
            Cfg_Nature_att_Bean bean = CfgManager.getCfg_Nature_att_Container().getValueByKey(excelId);
            if (null != bean) {
                ReadIntegerArray array = bean.getPeiyang_att();
                if (null != array) {
                    att.addSystemAttribute(array.get(0), array.get(1));
                }
            }
        }

        int totalPromoteLv = 0;
        for (SoulSpiritInfo info : player.getStifleData().getSpiritMap().values()) {
            totalPromoteLv += info.getPromoteLv();

            //晋升百分比属性加成
            int promoteConfigId = info.getId() * 100 + info.getPromoteLv();
            Cfg_State_stifle_add_level_Bean promoteBean = CfgManager.getCfg_State_stifle_add_level_Container().getValueByKey(promoteConfigId);
            if (promoteBean == null) {
                log.error("Cfg_State_stifle_add_level_Bean配置表不存在：" + promoteConfigId);
                continue;
            }
            for (int i = 0; i < promoteBean.getPer_attribute().size(); i++) {
                att.addSystemAttribute(promoteBean.getPer_attribute().get(i).get(0), promoteBean.getPer_attribute().get(i).get(1));
            }

            //进化百分比属性加成
            int evolveConfigId = info.getId() * 100 + info.getEvolveLv();
            Cfg_State_stifle_add_Bean evolveBean = CfgManager.getCfg_State_stifle_add_Container().getValueByKey(evolveConfigId);
            if (evolveBean == null) {
                log.error("Cfg_State_stifle_add_Bean配置表不存在：" + promoteConfigId);
                continue;
            }
            for (int i = 0; i < evolveBean.getPer_attribute().size(); i++) {
                att.addSystemAttribute(evolveBean.getPer_attribute().get(i).get(0), evolveBean.getPer_attribute().get(i).get(1));
            }
        }
        //晋升总等级百分比属性加成
        Cfg_State_stifle_add_level_all_Bean bean = null;
        for (Cfg_State_stifle_add_level_all_Bean tempBean : CfgManager.getCfg_State_stifle_add_level_all_Container().getValuees()) {
            if (totalPromoteLv >= tempBean.getLevel_all()) {
                bean = tempBean;
            }
        }
        if (bean != null) {
            for (int i = 0; i < bean.getPer_attribute().size(); i++) {
                att.addSystemAttribute(bean.getPer_attribute().get(i).get(0), bean.getPer_attribute().get(i).get(1));
            }
        }
        return att;
    }
}