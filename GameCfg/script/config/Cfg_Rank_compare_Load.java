/**
 * Auto generated, do not edit it
 *
 * Rank_compare配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Rank_compare_Bean; 

	
public final class Cfg_Rank_compare_Load implements IScriptConfig<Cfg_Rank_compare_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Rank_compare_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_Rank_compare_Bean(1,"坐骑",170000));
        contaioners.put(3, new Cfg_Rank_compare_Bean(3,"装备",20000));
        contaioners.put(8, new Cfg_Rank_compare_Bean(8,"宝石",162000));
        contaioners.put(10, new Cfg_Rank_compare_Bean(10,"强化",161100));
        contaioners.put(11, new Cfg_Rank_compare_Bean(11,"其他",2000000));
    }

}
