package common.attribute;

import com.data.CfgManager;
import com.data.FunctionStart;
import com.data.bean.Cfg_Cross_devil_card_Break_Bean;
import com.data.bean.Cfg_Cross_devil_card_Train_Bean;
import com.data.bean.Cfg_Equip_Bean;
import com.data.container.Cfg_Cross_devil_card_Break_Container;
import com.data.container.Cfg_Cross_devil_card_Train_Container;
import com.data.struct.ReadArray;
import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.attribute.script.IAttributeScript;
import com.game.devilseries.structs.DevilCamp;
import com.game.devilseries.structs.DevilCard;
import com.game.devilseries.structs.DevilEquipPart;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.structs.AttributeType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/5/7 14:04
 */
public class DevilSeriesAttributeScript implements IAttributeScript{

    private final static Logger log = LogManager.getLogger(DevilSeriesAttributeScript.class);

    @Override
    public int getId() {
        return ScriptEnum.DevilSeriesAttributeScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    @Override
    public PlayerAttributeType getType() {
        return PlayerAttributeType.DevilSeries;
    }

    @Override
    public BaseIntAttribute getPlayerAttribute(Player player, boolean sycRank) {
        log.info("玩家：{} 计算魔魂属性", player.getId());
        BaseIntAttribute att = player.PlayerCalculators().get(getType());
        if (att == null) {
            att = new BaseIntAttribute(AttributeType.ATTR_MAX);
            player.PlayerCalculators().put(getType(), att);
        }
        att.clean();

        //功能开启检测
        if(!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.DevilSoulMain)) {
            return att;
        }

        //属性计算
        for(DevilCamp camp : player.getDevil().getCamps().values()){
            for(DevilCard card : camp.getCards().values()){
                BaseIntAttribute cardAtt = new BaseIntAttribute(AttributeType.ATTR_MAX);
                //突破属性
                if(card.getBreak_level() > 0){
                    int breakKey = card.getId() * 1000 + card.getBreak_level();
                    Cfg_Cross_devil_card_Break_Bean breakCfg = Cfg_Cross_devil_card_Break_Container.GetInstance().getValueByKey(breakKey);
                    if(breakCfg != null){
                        for(ReadArray<Integer> aii : breakCfg.getAtt().getValuees()){
                            cardAtt.addAttribute(aii.get(0), aii.get(1));
                            att.addAttribute(aii.get(0), aii.get(1));
                        }
                    }else{
                        log.warn("魔魂突破配置不存在id:{}",breakKey);
                    }
                }
                //升级属性
                int rank = card.getRank();
                int level = card.getLevel();
                if(rank != 0 || level != 0){
                    int key = camp.getId()*100000+rank*10000+card.getId()*1000+level;
                    Cfg_Cross_devil_card_Train_Bean trainCfg = Cfg_Cross_devil_card_Train_Container.GetInstance().getValueByKey(key);
                    if(trainCfg == null){
                        log.warn("魔魂升级{}配置不存在", key);
                    }else{
                        for(ReadArray<Integer> aii : trainCfg.getAtt().getValuees()){
                            cardAtt.addAttribute(aii.get(0), aii.get(1));
                            att.addAttribute(aii.get(0), aii.get(1));
                        }
                    }
                }
                //装备属性
                for(DevilEquipPart part : card.getParts().values()){
                    if(part.getEquip() == null){
                        continue;
                    }
                    //============================装备基础属性生效
                    Cfg_Equip_Bean model = CfgManager.getCfg_Equip_Container().getValueByKey(part.getEquip().getItemModelId());
                    if (model == null) {
                        log.warn("错误的装备 modelId:" + part.getEquip().getItemModelId());
                        continue;
                    }
                    //装备基础属性中的Attribute1属性生效
                    for (ReadArray<Integer> aii : model.getAttribute1().getValuees()) {
                        cardAtt.addAttribute(aii.get(0), aii.get(1));
                        att.addAttribute(aii.get(0), aii.get(1));
                    }
                    //装备基础属性中的Attribute2属性生效
                    for (ReadArray<Integer> aii : model.getAttribute2().getValuees()) {
                        cardAtt.addAttribute(aii.get(0), aii.get(1));
                        att.addAttribute(aii.get(0), aii.get(1));
                    }
                }
                int power = Manager.playerAttAttributeManager.deal().calcFightPower(cardAtt);
                card.setFightPoint(power);
            }
        }

        if (sycRank) {
            int power = Manager.playerAttAttributeManager.deal().calcFightPower(att);
            Manager.rankListManager.deal().setDevilSoulPower(player, power);
        }

        return att;
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
}
