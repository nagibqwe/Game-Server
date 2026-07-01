/**
 * Auto generated, do not edit it
 *
 * new_sever_luckcard配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_New_sever_luckcard_Bean; 

	
public final class Cfg_New_sever_luckcard_Load implements IScriptConfig<Cfg_New_sever_luckcard_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_New_sever_luckcard_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_New_sever_luckcard_Bean(1,"129,1",120));
        contaioners.put(2, new Cfg_New_sever_luckcard_Bean(2,"95,4,1",600));
        contaioners.put(3, new Cfg_New_sever_luckcard_Bean(3,"95,5,1",1760));
        contaioners.put(4, new Cfg_New_sever_luckcard_Bean(4,"130,27,1",1360));
    }

}
