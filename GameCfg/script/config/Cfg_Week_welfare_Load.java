/**
 * Auto generated, do not edit it
 *
 * week_welfare配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Week_welfare_Bean; 

	
public final class Cfg_Week_welfare_Load implements IScriptConfig<Cfg_Week_welfare_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Week_welfare_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_Week_welfare_Bean(1,"12,200",2100000,"26,1,1,9"));
        contaioners.put(2, new Cfg_Week_welfare_Bean(2,"161,1",2750000,"26,1,1,9"));
        contaioners.put(3, new Cfg_Week_welfare_Bean(3,"7,1",201000,"26,1,1,9"));
    }

}
