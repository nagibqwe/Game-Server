package common.attribute;

import com.data.CfgManager;
import com.data.bean.Cfg_GemGrade_Bean;
import com.data.bean.Cfg_GemRefining_Bean;
import com.data.bean.Cfg_Item_Bean;
import com.data.struct.ReadArray;
import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.equip.struct.EquipPart;
import com.game.manager.Manager;
import com.game.attribute.script.IAttributeScript;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.structs.AttributeType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * @author gsj
 */
public class GemAttributeScript implements IAttributeScript {

    private static final Logger logger = LogManager.getLogger(GemAttributeScript.class);

    @Override
    public int getId() {
        return ScriptEnum.GemAttributeScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public PlayerAttributeType getType() {
        return PlayerAttributeType.GEM;
    }

    @Override
    public BaseIntAttribute getPlayerAttribute(Player player, boolean sycRank) {
        BaseIntAttribute attr = player.PlayerCalculators().get(getType());
        if (attr == null) {
            attr = new BaseIntAttribute(AttributeType.ATTR_MAX);
            player.PlayerCalculators().put(getType(), attr);
        }
        attr.clean();

        List<EquipPart> equipParts = player.getEquipParts();
        BaseSystemIntAttribute gemSystemAttr = player.PlayerCalSystemCulators().get(getType());
        for (EquipPart part : equipParts) {
            int partNo = part.getType();
            int level = part.getGemInfo().getLevel();
            List<Integer> gemIds = part.getGemInfo().getGemIds();
            List<Integer> jadeIds = part.getGemInfo().getJadeIds();

            //宝石属性
            calGemAttribute(partNo, gemIds, level, attr, gemSystemAttr);

            //仙玉属性
            calJadeAttribute(jadeIds, attr);
        }
        //计算宝石总等级加成
        calLevelAddition(player, attr);

        if (sycRank) {
            //更新宝石战力
            int power = Manager.playerAttAttributeManager.deal().calcFightPower(attr);
            Manager.rankListManager.deal().setGemPower(player, power);
        }

        return attr;
    }

    /**
     * 获取部位编号对应的宝石部位百分比加成属性类型
     *
     * @param partNo 部位编号
     * @return 属性加成类型
     */
    private int getPartAttrType(int partNo) {
        int attType = 0;
        switch (partNo) {
            case 0:
                attType = AttributeType.ATTR_Helmet_Gem;
                break;
            case 1:
                attType = AttributeType.ATTR_Weapon_Gem;
                break;
            case 2:
                attType = AttributeType.ATTR_Breastplate_Gem;
                break;
            case 3:
                attType = AttributeType.ATTR_Necklace_Gem;
                break;
            case 4:
                attType = AttributeType.ATTR_Belt_Gem;
                break;
            case 5:
                attType = AttributeType.ATTR_Leg_Armor_Gem;
                break;
            case 6:
                attType = AttributeType.ATTR_Shoe_Gem;
                break;
            case 7:
                attType = AttributeType.ATTR_Ring_Gem;
                break;
            case 8:
                attType = AttributeType.ATTR_Helmet_Gem;
                break;
            case 9:
                attType = AttributeType.ATTR_Weapon_Gem;
                break;
            case 10:
                attType = AttributeType.ATTR_Breastplate_Gem;
                break;
            default:
                break;
        }
        return attType;
    }

    /**
     * 计算宝石精炼等级系统属性
     *
     * @param partNo 部位编号
     * @param level  精炼等级
     * @param attr   属性
     */
    private void calGemRefineAttribute(int partNo, int level, BaseSystemIntAttribute attr) {
        if (level == 0) {
            return;
        }
        Cfg_GemRefining_Bean bean = CfgManager.getCfg_GemRefining_Container().getValueByKey(partNo * 1000 + level);
        if (bean == null) {
            logger.error("Cfg_GemRefiningBean配置表不存在：" + (partNo * 1000 + level));
            return;
        }
        for (ReadArray<Integer> array : bean.getAttribute_proportion().getValuees()) {
            if (array.get(0) < 1000) {
                logger.error("Cfg_GemRefiningBean配置表属性百分比加成配置错误 ：" + bean.getId());
                continue;
            }
            attr.addSystemAttribute(array.get(0), array.get(1));
        }
    }

    /**
     * 计算宝石属性
     *
     * @param partNo 部位编号
     * @param gemIds 宝石镶嵌列表
     * @param level  宝石精炼等级
     * @param attr   属性
     * @param systemAttr 宝石系统属性
     */
    private void calGemAttribute(int partNo, List<Integer> gemIds, int level, BaseIntAttribute attr, BaseSystemIntAttribute systemAttr) {
        for (Integer id : gemIds) {
            if (id <= 0) {
                continue;
            }
            Cfg_Item_Bean bean = CfgManager.getCfg_Item_Container().getValueByKey(id);
            if (bean == null) {
                logger.error("Cfg_ItemBean配置表不存在：" +id);
                continue;
            }
            int attType = getPartAttrType(partNo);
            if (attType == 0) {
                logger.error("部位编号错误！");
                return;
            }
            for (ReadArray<Integer> array : bean.getEffect_num().getValuees()) {
                if (array.get(0) != 1) {
                    continue;
                }
                if (array.get(0) <= 0 || array.get(0) > AttributeType.ATTR_MAX) {
                    continue;
                }
                if (array.get(0) >= AttributeType.ATTR_Helmet_Gem && array.get(0) <= AttributeType.ATTR_Ring_Gem){
                    continue;
                }
                attr.addAttribute((int) array.get(1), (int) array.get(2) * (10000 + systemAttr.getAttribute(attType)) / 10000);
            }
        }

        //精炼基础属性
        if (level == 0) {
            return;
        }
        Cfg_GemRefining_Bean bean = CfgManager.getCfg_GemRefining_Container().getValueByKey(partNo * 1000 + level);
        if (bean == null) {
            logger.error("Cfg_GemRefiningBean配置表不存在：" + (partNo * 1000 + level));
            return;
        }
        for (ReadArray<Integer> array : bean.getAttribute().getValuees()) {
            attr.addAttribute(array.get(0), array.get(1));
        }
    }

    /**
     * 计算仙玉属性
     *
     * @param jadeIds 仙玉镶嵌列表
     * @param attr    属性
     */
    private void calJadeAttribute(List<Integer> jadeIds, BaseIntAttribute attr) {
        for (Integer id : jadeIds) {
            if (id <= 0) {
                continue;
            }
            Cfg_Item_Bean bean = CfgManager.getCfg_Item_Container().getValueByKey(id);
            if (bean == null) {
                logger.error("Cfg_ItemBean配置表不存在：" + id);
                continue;
            }
            for (ReadArray<Integer> array : bean.getEffect_num().getValuees()) {
                if (array.get(0) != 1) {
                    continue;
                }
                if (array.get(0) <= 0 || array.get(0) > AttributeType.ATTR_MAX) {
                    continue;
                }
                attr.addAttribute((int) array.get(1), (int) array.get(2));
            }
        }
    }

    /**
     * 计算宝石总等级加成
     */
    private void calLevelAddition(Player player, BaseIntAttribute attr) {
        int allLevel = player.getEquipParts().stream()
                .map(n -> n.getGemInfo().getGemIds().stream()
                        .filter(m -> m > 0)
                        .map(m -> m % 100)
                        .reduce(0, Integer::sum))
                .reduce(0, Integer::sum);

        Cfg_GemGrade_Bean lastBean = null;
        for (Cfg_GemGrade_Bean bean : CfgManager.getCfg_GemGrade_Container().getValuees()) {
            if (bean.getLeve() <= allLevel) {
                lastBean = bean;
                continue;
            }
            if (lastBean == null) {
                return;
            }
            for (ReadArray<Integer> array : lastBean.getAddAttr().getValuees()) {
                attr.addAttribute(array.get(0), array.get(1));
            }
            break;
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

        List<EquipPart> equipParts = player.getEquipParts();
        for (EquipPart part : equipParts) {
            int partNo = part.getType();
            int level = part.getGemInfo().getLevel();
            //精炼属性，针对宝石属性的百分比加成和额外加成
            calGemRefineAttribute(partNo, level, att);
        }
        return att;
    }

}

