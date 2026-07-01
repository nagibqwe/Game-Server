package common.attribute;

import com.data.CfgManager;
import com.data.FunctionStart;
import com.data.Global;
import com.data.bean.*;
import com.data.struct.ReadArray;
import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.equip.struct.SpiritInfo;
import com.game.manager.Manager;
import com.game.attribute.script.IAttributeScript;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.structs.AttributeType;
import com.game.title.manager.TitleManager;
import com.game.utils.MessageUtils;
import game.message.SpiritMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gsj
 */
public class SpiritAttributeScript implements IAttributeScript {

    private static final Logger logger = LogManager.getLogger(SpiritAttributeScript.class);

    @Override
    public int getId() {
        return ScriptEnum.SpiritAttributeScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public PlayerAttributeType getType() {
        return PlayerAttributeType.Spirit;
    }

    @Override
    public BaseIntAttribute getPlayerAttribute(Player player, boolean sycRank) {
        BaseIntAttribute attr = player.PlayerCalculators().get(getType());
        if (attr == null) {
            attr = new BaseIntAttribute(AttributeType.ATTR_MAX);
            player.PlayerCalculators().put(getType(), attr);
        }
        attr.clean();


        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.LingTi)) {
            return attr;
        }

        for (ReadArray<Integer> att : Global.Base_LingLi_att.getValuees()) {
            attr.addAttribute(att.get(0), att.get(1));
        }

        //灵星属性
        int star = player.getSpiritData().getOpenStar();
        Cfg_Equip_Collection_star_Bean starBean = CfgManager.getCfg_Equip_Collection_star_Container().getValueByKey(star);
        if (starBean != null) {
            for (int i = 0; i < starBean.getAttribute().size(); i++) {
                attr.addAttribute(starBean.getAttribute().get(i).get(0), starBean.getAttribute().get(i).get(1));
            }
        }

        //灵体属性
        ConcurrentHashMap<Integer, SpiritInfo> spiritInfoMap = player.getSpiritData().getSpiritInfoMap();
        for (SpiritInfo info : spiritInfoMap.values()) {
            HashMap<Integer, Long> maps = new HashMap<>();
            int redFiveStarNum = 0;
            for (Integer equipId : info.getEquipList()) {
                if (equipId == 0) {
                    continue;
                }
                Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(equipId);
                if (bean == null) {
                    logger.error("Cfg_Equip_Bean配置表不存在：" + equipId);
                    continue;
                }
                if (bean.getQuality() > 7 || (bean.getQuality() == 7 && bean.getDiamond_Number() >= 5)) {
                    redFiveStarNum ++;
                }
                for (int i = 0; i < bean.getAttribute1().size(); i++) {
                    attr.addAttribute(bean.getAttribute1().get(i).get(0), bean.getAttribute1().get(i).get(1));

                    Long v = maps.get(bean.getAttribute1().get(i).get(0));
                    if (v == null) {
                        maps.put(bean.getAttribute1().get(i).get(0), (long) bean.getAttribute1().get(i).get(1));
                    } else {
                        maps.put(bean.getAttribute1().get(i).get(0), bean.getAttribute1().get(i).get(1) + v);
                    }
                }
                for (int i = 0; i < bean.getAttribute2().size(); i++) {
                    attr.addAttribute(bean.getAttribute2().get(i).get(0), bean.getAttribute2().get(i).get(1));
                }
            }

            Cfg_Equip_Collection_Bean b = CfgManager.getCfg_Equip_Collection_Container().getValueByKey(player.getCareer() * 100 + info.getSpiritId());
            if (b != null) {
                if (info.isActive()) {
                    for (ReadArray<Integer> atta : b.getAtt().getValuees()) {
                        attr.addAttribute(atta.get(0), atta.get(1));
                    }
                }
                info.setActiveExt(redFiveStarNum >= b.getStandard_power());

                if (info.isActiveExt()) {
                    for (ReadArray<Integer> atta : b.getAtt1().getValuees()) {
                        attr.addAttribute(atta.get(0), atta.get(1));
                    }
                    if(b.getAtt2() != null){
                        attr.addAttribute(b.getAtt2().get(0), b.getAtt2().get(1));
                    }
                }
            }
        }
        //同步排行榜
        if (sycRank) {
            int power = Manager.playerAttAttributeManager.deal().calcFightPower(attr);
            SpiritMessage.ResSyncFightPower.Builder msg =  SpiritMessage.ResSyncFightPower.newBuilder();
            msg.setFightPower(power);
            MessageUtils.send_to_player(player,  SpiritMessage.ResSyncFightPower.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            Manager.rankListManager.deal().setSpiritPower(player, power);
        }
        return attr;
    }

    @Override
    public BaseSystemIntAttribute getPlayerSystemAttribute(Player player) {
        BaseSystemIntAttribute att = player.PlayerCalSystemCulators().get(getType());
        if (att == null) {
            att = new BaseSystemIntAttribute(AttributeType.SystemAttr_Max);
            player.PlayerCalSystemCulators().put(getType(), att);
        }
        att.clean();

        int star = player.getSpiritData().getOpenStar();
        Cfg_Equip_Collection_star_Bean starBean = CfgManager.getCfg_Equip_Collection_star_Container().getValueByKey(star);
        if (starBean != null) {
            for (int i = 0; i < starBean.getAttribute_pre().size(); i++) {
                att.addSystemAttribute(starBean.getAttribute_pre().get(i).get(0), starBean.getAttribute_pre().get(i).get(1));
            }
        }
        return att;
    }
}

