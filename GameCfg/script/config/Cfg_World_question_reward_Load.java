/**
 * Auto generated, do not edit it
 *
 * world_question_reward配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_World_question_reward_Bean; 

	
public final class Cfg_World_question_reward_Load implements IScriptConfig<Cfg_World_question_reward_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_World_question_reward_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_World_question_reward_Bean(1,2400,4000,"90001,1,1}9,100,1"));
        contaioners.put(2, new Cfg_World_question_reward_Bean(2,1600,2200,"9,80,1"));
        contaioners.put(3, new Cfg_World_question_reward_Bean(3,1200,1400,"9,50,1"));
        contaioners.put(4, new Cfg_World_question_reward_Bean(4,0,1000,"9,30,1"));
    }

}
