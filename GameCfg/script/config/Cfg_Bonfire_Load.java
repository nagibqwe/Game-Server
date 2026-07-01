/**
 * Auto generated, do not edit it
 *
 * bonfire配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Bonfire_Bean; 

	
public final class Cfg_Bonfire_Load implements IScriptConfig<Cfg_Bonfire_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Bonfire_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_Bonfire_Bean(1,"跨服活动",1,6000,120,0,0));
        contaioners.put(2, new Cfg_Bonfire_Bean(2,"帮会活动",2,6001,900,0,0));
    }

}
