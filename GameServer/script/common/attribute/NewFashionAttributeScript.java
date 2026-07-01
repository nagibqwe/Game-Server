package common.attribute;

import com.data.CfgManager;
import com.data.bean.Cfg_Fashion_Bean;
import com.data.bean.Cfg_Fashion_link_Bean;
import com.data.bean.Cfg_Fashion_total_Bean;
import com.data.struct.ReadArray;
import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.attribute.script.IAttributeScript;
import com.game.newfashion.structs.FashionData;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.structs.AttributeType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cxl on 2020/7/21.
 */
public class NewFashionAttributeScript implements IAttributeScript {


    private final static Logger log = LogManager.getLogger(MarriageAttributeScript.class);

    @Override
    public PlayerAttributeType getType() {
        return PlayerAttributeType.NewFashion;
    }


    @Override
    public BaseIntAttribute getPlayerAttribute(Player player, boolean sycRank) {
        BaseIntAttribute att = player.PlayerCalculators().get(getType());
        if (att == null) {
            att = new BaseIntAttribute(AttributeType.ATTR_MAX);
            player.PlayerCalculators().put(getType(), att);
        }
        att.clean();
        calcFashionAttribute( player,  att);
        calcTjAttribute(player, att);
        return att;
    }


    private void calcFashionAttribute(Player player, BaseIntAttribute att){

        HashMap<Integer, FashionData> fashionDataMap =  player.getNewFashionData().getActivtyFsDatas();
        for (Map.Entry<Integer, FashionData> entry : fashionDataMap.entrySet()){
            Cfg_Fashion_Bean bean = CfgManager.getCfg_Fashion_Container().getValueByKey(entry.getKey());
            if (bean==null){
                continue;
            }
            FashionData fashionData = entry.getValue();
            for (ReadArray<Integer> readArray : bean.getRent_att().getValuees()){
                int needAdd = fashionData.getStar() * readArray.get(2);
                att.addAttribute(readArray.get(0), readArray.get(1)+ needAdd);
            }
        }
    }

    private void calcTjAttribute(Player player,BaseIntAttribute att){

        HashMap<Integer, Integer> tjdataList = player.getNewFashionData().getActityTjDatas();
        HashMap<Integer, FashionData> fashionDataMap =  player.getNewFashionData().getActivtyFsDatas();
        Cfg_Fashion_link_Bean[] beans = CfgManager.getCfg_Fashion_link_Container().getValuees();

        //图鉴未激活时,达成了多少件时装属性加成
        for (Cfg_Fashion_link_Bean bean : beans){
            int num = 0;
            for (ReadArray<Integer> readArray :bean.getNeed_fashion_id().getValuees()){
                if (fashionDataMap.containsKey(readArray.get(1))){
                    num++;
                }
            }
            for (int i = 0; i < num;i++){
                if (i >=  bean.getRent_att().size()){
                    break;
                }
                ReadArray<Integer> array =  bean.getRent_att().get(i);
                att.addAttribute(array.get(1), array.get(2));
            }
        }

        //激活后的图鉴升星
        for (Map.Entry<Integer, Integer> entry : tjdataList.entrySet()){
            Cfg_Fashion_link_Bean bean = CfgManager.getCfg_Fashion_link_Container().getValueByKey(entry.getKey());
            int star = entry.getValue();
            for (ReadArray<Integer> array1 : bean.getActivation_att().getValuees()){
                att.addAttribute(array1.get(0), array1.get(1));
            }
            for (ReadArray<Integer> array2 : bean.getStar_att().getValuees()){
                att.addAttribute(array2.get(0), array2.get(1)*star);
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

    @Override
    public int getId() {
        return ScriptEnum.NewFashionAttributeScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }


}
