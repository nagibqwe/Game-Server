/**
 * Auto generated, do not edit it
 *
 * RankAwardType配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_RankAwardType_Bean; 

	
public final class Cfg_RankAwardType_Load implements IScriptConfig<Cfg_RankAwardType_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_RankAwardType_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_RankAwardType_Bean(1,0,112,8,8,"洗炼战力"));
        contaioners.put(2, new Cfg_RankAwardType_Bean(2,0,109,9,9,"神兵战力"));
        contaioners.put(3, new Cfg_RankAwardType_Bean(3,0,104,10,10,"仙羽战力"));
        contaioners.put(5, new Cfg_RankAwardType_Bean(5,0,127,14,15,"魂甲战力"));
        contaioners.put(8, new Cfg_RankAwardType_Bean(8,0,130,21,22,"魔魂战力"));
        contaioners.put(10, new Cfg_RankAwardType_Bean(10,0,131,25,27,"脉轮战力"));
        contaioners.put(11, new Cfg_RankAwardType_Bean(11,1,126,28,30,"消费送礼"));
    }

}
