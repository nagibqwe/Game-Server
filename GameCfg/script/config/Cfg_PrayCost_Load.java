/**
 * Auto generated, do not edit it
 *
 * PrayCost配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_PrayCost_Bean; 

	
public final class Cfg_PrayCost_Load implements IScriptConfig<Cfg_PrayCost_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_PrayCost_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_PrayCost_Bean(1,"50,1","40,1"));
        contaioners.put(2, new Cfg_PrayCost_Bean(2,"55,1","42,1"));
        contaioners.put(3, new Cfg_PrayCost_Bean(3,"60,1","44,1"));
        contaioners.put(4, new Cfg_PrayCost_Bean(4,"65,1","46,1"));
        contaioners.put(5, new Cfg_PrayCost_Bean(5,"70,1","48,1"));
        contaioners.put(6, new Cfg_PrayCost_Bean(6,"75,1","50,1"));
        contaioners.put(7, new Cfg_PrayCost_Bean(7,"80,1","52,1"));
        contaioners.put(8, new Cfg_PrayCost_Bean(8,"85,1","54,1"));
        contaioners.put(9, new Cfg_PrayCost_Bean(9,"90,1","56,1"));
        contaioners.put(10, new Cfg_PrayCost_Bean(10,"95,1","58,1"));
    }

}
