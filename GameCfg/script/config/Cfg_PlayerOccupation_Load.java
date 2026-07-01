/**
 * Auto generated, do not edit it
 *
 * PlayerOccupation配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_PlayerOccupation_Bean; 

	
public final class Cfg_PlayerOccupation_Load implements IScriptConfig<Cfg_PlayerOccupation_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_PlayerOccupation_Bean> contaioners){
        contaioners.clear();
        contaioners.put(0, new Cfg_PlayerOccupation_Bean(0,1,"玄剑",1));
        contaioners.put(1, new Cfg_PlayerOccupation_Bean(1,0,"天英",0));
        contaioners.put(2, new Cfg_PlayerOccupation_Bean(2,1,"地藏",0));
        contaioners.put(3, new Cfg_PlayerOccupation_Bean(3,0,"罗刹",0));
    }

}
