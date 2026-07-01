/**
 * Auto generated, do not edit it
 *
 * peakBattleJoinReward配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_PeakBattleJoinReward_Bean; 

	
public final class Cfg_PeakBattleJoinReward_Load implements IScriptConfig<Cfg_PeakBattleJoinReward_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_PeakBattleJoinReward_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_PeakBattleJoinReward_Bean(1,1,"16001,1}81101,1}29,2000"));
        contaioners.put(2, new Cfg_PeakBattleJoinReward_Bean(2,5,"16001,2}81101,1}29,6000"));
        contaioners.put(3, new Cfg_PeakBattleJoinReward_Bean(3,10,"16001,3}81102,1}29,12000"));
    }

}
