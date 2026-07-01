/**
 * Auto generated, do not edit it
 *
 * guild_official配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Guild_official_Bean; 

	
public final class Cfg_Guild_official_Load implements IScriptConfig<Cfg_Guild_official_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Guild_official_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_Guild_official_Bean(1,0,1000,0,0,0,0,0,0,1,0,0,0));
        contaioners.put(2, new Cfg_Guild_official_Bean(2,0,10,0,0,0,0,0,0,1,0,0,0));
        contaioners.put(3, new Cfg_Guild_official_Bean(3,1,2,1,1,1,1,1,1,1,1,1,1));
        contaioners.put(4, new Cfg_Guild_official_Bean(4,1,1,1,1,1,1,1,1,1,1,1,1));
    }

}
