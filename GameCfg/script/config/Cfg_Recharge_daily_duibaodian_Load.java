/**
 * Auto generated, do not edit it
 *
 * recharge_daily_duibaodian配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Recharge_daily_duibaodian_Bean; 

	
public final class Cfg_Recharge_daily_duibaodian_Load implements IScriptConfig<Cfg_Recharge_daily_duibaodian_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Recharge_daily_duibaodian_Bean> contaioners){
        contaioners.clear();
        contaioners.put(101, new Cfg_Recharge_daily_duibaodian_Bean(101,1,"15014,1,1,9","1069,2000",1,1));
        contaioners.put(103, new Cfg_Recharge_daily_duibaodian_Bean(103,1,"17011,1,1,9","1069,500",1,1));
        contaioners.put(104, new Cfg_Recharge_daily_duibaodian_Bean(104,1,"25,1500,1,9","1069,100",5,1));
        contaioners.put(105, new Cfg_Recharge_daily_duibaodian_Bean(105,1,"1011,1,1,9","1069,100",5,1));
    }

}
