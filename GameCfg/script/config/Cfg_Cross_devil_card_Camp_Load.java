/**
 * Auto generated, do not edit it
 *
 * Cross_devil_card_Camp配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Cross_devil_card_Camp_Bean; 

	
public final class Cfg_Cross_devil_card_Camp_Load implements IScriptConfig<Cfg_Cross_devil_card_Camp_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Cross_devil_card_Camp_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_Cross_devil_card_Camp_Bean(1,"魔兵","305,306,307,308",""));
        contaioners.put(2, new Cfg_Cross_devil_card_Camp_Bean(2,"魔将","309,310,311,312",""));
        contaioners.put(3, new Cfg_Cross_devil_card_Camp_Bean(3,"魔尊","313,314,315,316",""));
        contaioners.put(4, new Cfg_Cross_devil_card_Camp_Bean(4,"魔主","317,318,319,320",""));
    }

}
