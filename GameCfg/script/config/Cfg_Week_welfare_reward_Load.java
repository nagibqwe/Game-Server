/**
 * Auto generated, do not edit it
 *
 * week_welfare_reward配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Week_welfare_reward_Bean; 

	
public final class Cfg_Week_welfare_reward_Load implements IScriptConfig<Cfg_Week_welfare_reward_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Week_welfare_reward_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_Week_welfare_reward_Bean(1,0,1,"16,10,1,9,0}50002,1,1,9,1}20004,1,1,9,1}21004,1,1,9,1}81012,1,1,0,300}81013,1,1,1,300",5000,10000));
        contaioners.put(2, new Cfg_Week_welfare_reward_Bean(2,1,2,"1011,1,1,9,0}60011,3,1,9,0}12,2000,1,9,1}20003,1,1,9,1}21003,1,1,9,1",15000,40000));
        contaioners.put(3, new Cfg_Week_welfare_reward_Bean(3,2,2,"20002,1,1,9,0}21002,1,1,9,0",20000,50000));
        contaioners.put(4, new Cfg_Week_welfare_reward_Bean(4,3,3,"12,1000,1,9,0}50001,1,1,9,0}10001,2,1,9,0",60000,0));
    }

}
