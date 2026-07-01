package common.attribute;

import com.data.CfgManager;
import com.data.FunctionStart;
import com.data.bean.Cfg_HuaxingWing_Bean;
import com.data.bean.Cfg_NatureWing_Bean;
import com.data.bean.Cfg_Nature_att_Bean;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.attribute.AttributeUtils;
import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.manager.Manager;
import com.game.nature.structs.Drug;
import com.game.nature.structs.Huaxin;
import com.game.nature.structs.Nature;
import com.game.attribute.script.IAttributeScript;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.structs.AttributeType;

import static com.game.structs.AttributeType.*;

public class WingAttributeScript implements IAttributeScript {

    @Override
    public int getId() {
        return ScriptEnum.WingAttributeScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public PlayerAttributeType getType() {
        return PlayerAttributeType.WING;
    }

    @Override
    public BaseIntAttribute getPlayerAttribute(Player player, boolean sycRank) {
        BaseIntAttribute att = player.PlayerCalculators().get(getType());
        if (att == null) {
            att = new BaseIntAttribute(AttributeType.ATTR_MAX);
            player.PlayerCalculators().put(getType(), att);
        }
        att.clean();

        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.NatureWing)) {
            return att;
        }
        calculateMagicAttribute(player, att);

        //同步排行榜
        if (sycRank) {
            int power = Manager.playerAttAttributeManager.deal().calcFightPower(att);
            player.getWing().setPower(power);
            Manager.rankListManager.deal().setWingPower(player, power);
        }
        return att;
    }

    private void calculateMagicAttribute(Player player, BaseIntAttribute att) {
        Nature wing = player.getWing();
        Cfg_NatureWing_Bean wingBean = CfgManager.getCfg_NatureWing_Container().getValueByKey(wing.getCurrentId());
        if (wingBean != null) {
            for (ReadArray array : wingBean.getAttribute().getValuees()) {
                if (array.size() >= 3) {
                    att.addAttribute((int) array.get(0), (int) array.get(1));
                }
            }
        }

        BaseSystemIntAttribute systemAtt = player.getSysAttriBute();
        for (int i = 1; i < systemAtt.getLength(); i++) {
            double rate = systemAtt.getAttribute(i) / 10000.0 + 1.0;
            switch (i) {
                case ATTR_Wing_Attack:
                    AttributeUtils.attributeEnlarge(att, ATTR_Atk, rate);
                    break;
                case ATTR_Wing_Hp:
                    AttributeUtils.attributeEnlarge(att, ATTR_MaxHp, rate);
                    break;
                case ATTR_Wing_DefBreak:
                    AttributeUtils.attributeEnlarge(att, ATTR_DefBreak, rate);
                    break;
                case ATTR_Wing_Defence:
                    AttributeUtils.attributeEnlarge(att, ATTR_Def, rate);
                    break;
                default:
                    break;
            }
        }
        for (Drug drug : player.getWing().getDrugs().values()) {
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
        for (Huaxin huaxin : player.getWing().getHuaxins().values()) {
            int level = huaxin.getLevel();
            Cfg_HuaxingWing_Bean huaxingWingBean = CfgManager.getCfg_HuaxingWing_Container().getValueByKey(huaxin.getExcelId());
            if (huaxingWingBean == null){
                continue;
            }
            ReadIntegerArrayEs arrays = huaxingWingBean.getRent_att();
            if (null != arrays) {
                for (ReadArray array : arrays.getValuees()) {
                    att.addAttribute((int) array.get(0), (int) array.get(1) + (int) array.get(2) * level);
                }
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

        for (Drug drug : player.getWing().getDrugs().values()){
            int excelId = drug.getExcelId();
            Cfg_Nature_att_Bean bean = CfgManager.getCfg_Nature_att_Container().getValueByKey(excelId);
            if (null != bean) {
                ReadIntegerArray array = bean.getPeiyang_att();
                if (null != array) {
                    att.addSystemAttribute(array.get(0), array.get(1));
                }
            }
        }
        return att;
    }
}
