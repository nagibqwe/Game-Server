/**
 * Auto generated, do not edit it
 *
 * daily_reward配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Daily_reward_Bean; 

	
public final class Cfg_Daily_reward_Load implements IScriptConfig<Cfg_Daily_reward_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Daily_reward_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_Daily_reward_Bean(1,50,"1379,1"));
        contaioners.put(2, new Cfg_Daily_reward_Bean(2,105,"1004,1"));
        contaioners.put(3, new Cfg_Daily_reward_Bean(3,160,"60004,10"));
        contaioners.put(4, new Cfg_Daily_reward_Bean(4,240,"1004,1"));
        contaioners.put(5, new Cfg_Daily_reward_Bean(5,325,"1018,1"));
    }

}
