/**
 * Auto generated, do not edit it
 *
 * Cross_fudi_score_reward配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Cross_fudi_score_reward_Bean; 

	
public final class Cfg_Cross_fudi_score_reward_Load implements IScriptConfig<Cfg_Cross_fudi_score_reward_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Cross_fudi_score_reward_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_Cross_fudi_score_reward_Bean(1,1000,"82376,1",0));
        contaioners.put(2, new Cfg_Cross_fudi_score_reward_Bean(2,5000,"82377,1",0));
        contaioners.put(3, new Cfg_Cross_fudi_score_reward_Bean(3,11000,"82378,1",0));
        contaioners.put(4, new Cfg_Cross_fudi_score_reward_Bean(4,18000,"82379,1",0));
        contaioners.put(5, new Cfg_Cross_fudi_score_reward_Bean(5,27000,"82380,1",300));
        contaioners.put(6, new Cfg_Cross_fudi_score_reward_Bean(6,36000,"82381,1",300));
        contaioners.put(7, new Cfg_Cross_fudi_score_reward_Bean(7,45000,"82382,1",300));
        contaioners.put(8, new Cfg_Cross_fudi_score_reward_Bean(8,54000,"82383,1",300));
    }

}
