/**
 * Auto generated, do not edit it
 *
 * Cross_Alien_Gem_Copy配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Cross_Alien_Gem_Copy_Bean; 

	
public final class Cfg_Cross_Alien_Gem_Copy_Load implements IScriptConfig<Cfg_Cross_Alien_Gem_Copy_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Cross_Alien_Gem_Copy_Bean> contaioners){
        contaioners.clear();
        contaioners.put(101, new Cfg_Cross_Alien_Gem_Copy_Bean(101,2025));
        contaioners.put(102, new Cfg_Cross_Alien_Gem_Copy_Bean(102,2026));
        contaioners.put(103, new Cfg_Cross_Alien_Gem_Copy_Bean(103,2027));
    }

}
