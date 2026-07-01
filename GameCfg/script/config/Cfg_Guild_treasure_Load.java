/**
 * Auto generated, do not edit it
 *
 * guild_treasure配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Guild_treasure_Bean; 

	
public final class Cfg_Guild_treasure_Load implements IScriptConfig<Cfg_Guild_treasure_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Guild_treasure_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_Guild_treasure_Bean(1,1,1,"20001,1}10001,10"));
        contaioners.put(2, new Cfg_Guild_treasure_Bean(2,2,5,"20002,1}10001,8"));
        contaioners.put(6, new Cfg_Guild_treasure_Bean(6,6,10,"20003,1}10001,6"));
        contaioners.put(11, new Cfg_Guild_treasure_Bean(11,11,60,"20004,1}10001,4"));
    }

}
