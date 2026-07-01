/**
 * Auto generated, do not edit it
 *
 * recharge_daily_superreward配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Recharge_daily_superreward_Bean; 

	
public final class Cfg_Recharge_daily_superreward_Load implements IScriptConfig<Cfg_Recharge_daily_superreward_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Recharge_daily_superreward_Bean> contaioners){
        contaioners.clear();
        contaioners.put(101, new Cfg_Recharge_daily_superreward_Bean(101,1,"15013,1,1,9",6,0,1));
        contaioners.put(102, new Cfg_Recharge_daily_superreward_Bean(102,1,"19007,1,1,9",12,0,1));
        contaioners.put(103, new Cfg_Recharge_daily_superreward_Bean(103,1,"15002,1,1,9",18,0,1));
        contaioners.put(104, new Cfg_Recharge_daily_superreward_Bean(104,1,"24020,8,1,9",30,0,1));
        contaioners.put(105, new Cfg_Recharge_daily_superreward_Bean(105,1,"24021,8,1,9",42,0,1));
        contaioners.put(106, new Cfg_Recharge_daily_superreward_Bean(106,1,"15013,1,1,9",60,0,1));
        contaioners.put(107, new Cfg_Recharge_daily_superreward_Bean(107,1,"24022,8,1,9",90,0,1));
        contaioners.put(108, new Cfg_Recharge_daily_superreward_Bean(108,1,"24023,8,1,9",120,0,1));
        contaioners.put(109, new Cfg_Recharge_daily_superreward_Bean(109,1,"19008,1,1,9",150,0,1));
        contaioners.put(110, new Cfg_Recharge_daily_superreward_Bean(110,1,"10115,1,1,9",250,1,1));
    }

}
