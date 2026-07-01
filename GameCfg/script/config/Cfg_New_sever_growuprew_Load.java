/**
 * Auto generated, do not edit it
 *
 * new_sever_growuprew配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_New_sever_growuprew_Bean; 

	
public final class Cfg_New_sever_growuprew_Load implements IScriptConfig<Cfg_New_sever_growuprew_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_New_sever_growuprew_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_New_sever_growuprew_Bean(1,800,"1010,1,1,9"));
        contaioners.put(2, new Cfg_New_sever_growuprew_Bean(2,1500,"17004,1,1,9"));
        contaioners.put(3, new Cfg_New_sever_growuprew_Bean(3,2000,"60078,1,1,9"));
        contaioners.put(4, new Cfg_New_sever_growuprew_Bean(4,3000,"60079,1,1,9"));
        contaioners.put(5, new Cfg_New_sever_growuprew_Bean(5,5000,"19007,1,1,9"));
        contaioners.put(6, new Cfg_New_sever_growuprew_Bean(6,7000,"15006,1,1,9"));
    }

}
