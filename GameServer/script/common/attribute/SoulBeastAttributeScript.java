package common.attribute;

import com.data.CfgManager;
import com.data.bean.Cfg_SoulBeastsEquipLevel_Bean;
import com.data.bean.Cfg_SoulBeastsEquip_Bean;
import com.data.bean.Cfg_SoulBeasts_Bean;
import com.data.struct.ReadArray;
import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.attribute.script.IAttributeScript;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.soulbeast.structs.ExtraAttrbute;
import com.game.soulbeast.structs.SoulBeast;
import com.game.soulbeast.structs.SoulBeastConst;
import com.game.soulbeast.structs.SoulBeastEquip;
import com.game.structs.AttributeType;
import com.game.task.structs.TaskHelp;
import com.game.utils.IntegerMapHelp;
import com.game.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lw
 */
public class SoulBeastAttributeScript implements IAttributeScript {

    private final static Logger log = LogManager.getLogger(SoulBeastAttributeScript.class);


    @Override
    public PlayerAttributeType getType() {
        return PlayerAttributeType.SOUL_BEAST;
    }


    @Override
    public BaseIntAttribute getPlayerAttribute(Player player, boolean sycRank) {
        BaseIntAttribute att = player.PlayerCalculators().get(getType());
        if (att == null) {
            att = new BaseIntAttribute(AttributeType.ATTR_MAX);
            player.PlayerCalculators().put(getType(), att);
        }
        att.clean();
        calcSoulBeast(player, att);
        if (sycRank) {
            int power = Manager.playerAttAttributeManager.deal().calcFightPower(att);
            Manager.rankListManager.deal().setMonstorPower(player, power);
        }
        return att;
    }

    private void calcSoulBeast(Player player, BaseIntAttribute att) {
        Map<Integer, Integer> extendAttr = new HashMap<>();
        Map<Integer, Map<Integer, Integer>> attrBase = calcBaseAttr(player, extendAttr);
        Map<Integer, Integer> allExtend = new HashMap<>();
        //计算出所有的全体加的额外属性比例
        List<SoulBeast> soulBeasts = Utils.find(player.getSoulBeastInfo().getSoulBeasts().values(), sb -> sb.isWork());

        for (SoulBeast soulBeast : soulBeasts) {
            final Cfg_SoulBeasts_Bean beastsBean = CfgManager.getCfg_SoulBeasts_Container().getValueByKey(soulBeast.getBeastId());
            for (ReadArray<Integer> array : beastsBean.getAtt_skill().getValuees()) {
                if (array.get(2) == SoulBeastConst.ALL_SOUL_BEAST_ADD) {
                    IntegerMapHelp.put(allExtend, array.get(0), array.get(1));
                }
            }
        }
        for (SoulBeast soulBeast : soulBeasts) {
            Map<Integer, Integer> integerMap = attrBase.get(soulBeast.getBeastId());
            if (integerMap == null) {
                log.error("此处不应该为null：" + soulBeast.getBeastId());
                continue;
            }
            for (Map.Entry<Integer, Integer> entry : integerMap.entrySet()) {
                // ① 基础属性
                att.addAttribute(entry.getKey(), entry.getValue());
            }
            final Cfg_SoulBeasts_Bean beastsBean = CfgManager.getCfg_SoulBeasts_Container().getValueByKey(soulBeast.getBeastId());
            for (ReadArray<Integer> array : beastsBean.getAtt_skill().getValuees()) {
                //表示是处理单个魂兽的
                if (array.get(2) == SoulBeastConst.SINGLE_SOUL_BEAST_ADD) {
                    if (integerMap.containsKey(array.get(0))) {
                        //② 单体属性
                        att.addAttribute(array.get(0), (int) (integerMap.get(array.get(0)) * 1.0 * array.get(1) / 10000));
                    }
                }
            }
            //总属性
            for (Map.Entry<Integer, Integer> entry : allExtend.entrySet()) {
                if (integerMap.containsKey(entry.getKey())) {
                    //③ 全局属性
                    att.addAttribute(entry.getKey(), (int) (integerMap.get(entry.getKey()) * 1.0 * entry.getValue() / 10000));
                }
            }
        }
        for (Map.Entry<Integer, Integer> entry : extendAttr.entrySet()) {
            att.addAttribute(entry.getKey(), entry.getValue());
        }
    }

    /**
     * @param player
     */
    private Map<Integer, Map<Integer, Integer>> calcBaseAttr(Player player, Map<Integer, Integer> extendAttr) {
        List<SoulBeast> soulBeasts = Utils.find(player.getSoulBeastInfo().getSoulBeasts().values(), SoulBeast::isWork);

        Map<Integer, Map<Integer, Integer>> attrBase = new HashMap<>();
        for (SoulBeast soulBeast : soulBeasts) {
            Map<Integer, Integer> attr = new HashMap<>();
            attrBase.put(soulBeast.getBeastId(), attr);

            //魂兽基本属性
            final Cfg_SoulBeasts_Bean beastsBean = CfgManager.getCfg_SoulBeasts_Container().getValueByKey(soulBeast.getBeastId());
            for (ReadArray<Integer> array : beastsBean.getAttribute().getValuees()) {
                IntegerMapHelp.put(attr, array.get(0), array.get(1));
            }
            //装备属性
            for (Map.Entry<Integer, SoulBeastEquip> equipEntry : soulBeast.getEquip().entrySet()) {
                SoulBeastEquip soulBeastEquip = equipEntry.getValue();
                Cfg_SoulBeastsEquip_Bean beastsEquipBean = CfgManager.getCfg_SoulBeastsEquip_Container().getValueByKey(soulBeastEquip.getItemModelId());

                //TODO 基础属性
                for (ReadArray<Integer> array : beastsEquipBean.getAttribute1().getValuees()) {
                    IntegerMapHelp.put(attr, array.get(0), array.get(1));
                }
                //TODO 加上强化属性
                int level = beastsEquipBean.getPart() * 10000 + soulBeastEquip.getLevel();
                Cfg_SoulBeastsEquipLevel_Bean levelBean = CfgManager.getCfg_SoulBeastsEquipLevel_Container().getValueByKey(level);
                for (ReadArray<Integer> array : levelBean.getAtt().getValuees()) {
                    IntegerMapHelp.put(attr, array.get(0), array.get(1));
                }
            }
        }
        return attrBase;
    }

    @Override
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
    public int getId() {
        return ScriptEnum.SoulBeastAttributeScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }
}
