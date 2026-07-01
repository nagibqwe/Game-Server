/**
 * Auto generated, do not edit it
 *
 * across配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Across_Bean; 

	
public final class Cfg_Across_Load implements IScriptConfig<Cfg_Across_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Across_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_Across_Bean(1,100,2));
        contaioners.put(2, new Cfg_Across_Bean(2,200,4));
        contaioners.put(3, new Cfg_Across_Bean(3,300,8));
        contaioners.put(4, new Cfg_Across_Bean(4,400,16));
        contaioners.put(5, new Cfg_Across_Bean(5,500,32));
    }

}
