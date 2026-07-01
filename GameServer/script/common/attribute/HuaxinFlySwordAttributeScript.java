package common.attribute;

import com.data.CfgManager;
import com.data.bean.Cfg_HuaxingFlySword_Advanced_Bean;
import com.data.bean.Cfg_HuaxingFlySword_Bean;
import com.data.bean.Cfg_HuaxingFlySword_levelup_Bean;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.huaxinflysword.structs.FlyswordData;
import com.game.attribute.script.IAttributeScript;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.ranklist.manager.RankListManager;
import com.game.script.structs.ScriptEnum;
import com.game.structs.AttributeType;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by cxl on 2020/5/21.
 */
public class HuaxinFlySwordAttributeScript  implements IAttributeScript {

    @Override
    public int getId() {
        return ScriptEnum.HuaxinFlySwordAttributeScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public PlayerAttributeType getType() {
        return PlayerAttributeType.HuaxinFlySword;
    }

    @Override
    public BaseIntAttribute getPlayerAttribute(Player player, boolean sycRank) {
        BaseIntAttribute att = player.PlayerCalculators().get(getType());
        if (att == null) {
            att = new BaseIntAttribute(AttributeType.ATTR_MAX);
            player.PlayerCalculators().put(getType(), att);
        }
        att.clean();
        calFlyswordAtt(player,att);
        if(sycRank){
            int power = Manager.playerAttAttributeManager.deal().calcFightPower(att);
            Manager.rankListManager.deal().setFlySwordPower(player, power);
        }
        return att;
    }

    private void calFlyswordAtt(Player player ,BaseIntAttribute att){
        if (player.getFlyswordAllInfo().getFlyswordDataMap().size()<=0)
            return ;
        Iterator< Map.Entry<Integer, FlyswordData>> iter = player.getFlyswordAllInfo().getFlyswordDataMap().entrySet().iterator();
        Cfg_HuaxingFlySword_Advanced_Bean advanced_bean = null;
        Cfg_HuaxingFlySword_levelup_Bean levelup_bean = null;
        Cfg_HuaxingFlySword_Bean sword_bean = null;
        HashMap<Integer,Integer> allAttMap = new HashMap<>();
        while (iter.hasNext()) {
            Map.Entry<Integer, FlyswordData> en = iter.next();
            FlyswordData flyswordData  = en.getValue();
            allAttMap.clear();
            int max = 0;
            for (int swordID : flyswordData.getActivateList()){
                if (swordID>max)
                    max = swordID;
            }
            sword_bean = CfgManager.getCfg_HuaxingFlySword_Container().getValueByKey(max);
            if (sword_bean!=null){
                ReadIntegerArrayEs arrays = sword_bean.getRent_att();
                if (null != arrays && arrays.size() >0) {
                    for (ReadArray<Integer> array : arrays.getValuees()) {
                        if (array.size()<3)
                            continue;
                        allAttMap.put(array.get(0), array.get(1));
                    }
                }
            }
            levelup_bean = CfgManager.getCfg_HuaxingFlySword_levelup_Container().getValueByKey(flyswordData.getLevel());
            if (levelup_bean ==null){
               continue;
            }
            ReadIntegerArrayEs arrays = levelup_bean.getAttribute();
            addAtt(allAttMap,arrays);
            advanced_bean = CfgManager.getCfg_HuaxingFlySword_Advanced_Container().getValueByKey(flyswordData.getSteps());
            if (advanced_bean == null){
                continue;
            }
            arrays =  advanced_bean.getRent_att();
            addAtt(allAttMap,arrays);

            for (Map.Entry<Integer,Integer> attArr:  allAttMap.entrySet()){
                float add =  (advanced_bean.getAtt_all_add() + 10000.0f) / 10000;
                int  allAdd =   (int)(attArr.getValue() * add);
                att.addAttribute(attArr.getKey(),allAdd);
            }
        }
    }

    private void addAtt( HashMap<Integer,Integer> allAttMap, ReadIntegerArrayEs arrays){
        if (null != arrays && arrays.size() >0) {
            for (ReadArray<Integer> array : arrays.getValuees()) {
                if (array.size()<3)
                    continue;
                if (!allAttMap.containsKey(array.get(0))){
                    allAttMap.put(array.get(0), array.get(1));
                }else {
                    allAttMap.put(array.get(0), allAttMap.get(array.get(0)) + array.get(1)) ;
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
        return att;
    }

}
