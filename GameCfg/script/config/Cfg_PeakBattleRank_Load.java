/**
 * Auto generated, do not edit it
 *
 * peakBattleRank配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_PeakBattleRank_Bean; 

	
public final class Cfg_PeakBattleRank_Load implements IScriptConfig<Cfg_PeakBattleRank_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_PeakBattleRank_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_PeakBattleRank_Bean(1,"1,1","1001,50}3,500}1,100"));
        contaioners.put(2, new Cfg_PeakBattleRank_Bean(2,"2,4","3,70008}7,55127"));
        contaioners.put(3, new Cfg_PeakBattleRank_Bean(3,"5,16","11,50"));
        contaioners.put(4, new Cfg_PeakBattleRank_Bean(4,"17,32","11,25"));
        contaioners.put(5, new Cfg_PeakBattleRank_Bean(5,"33,50","9,5"));
    }

}
