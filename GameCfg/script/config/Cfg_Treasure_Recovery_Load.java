/**
 * Auto generated, do not edit it
 *
 * Treasure_Recovery配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Treasure_Recovery_Bean; 

	
public final class Cfg_Treasure_Recovery_Load implements IScriptConfig<Cfg_Treasure_Recovery_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Treasure_Recovery_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1001, new Cfg_Treasure_Recovery_Bean(1001,"14102,0}14202,1}14302,2",0,"6,3000"));
        contaioners.put(1002, new Cfg_Treasure_Recovery_Bean(1002,"60081,9",0,"6,1500"));
        contaioners.put(1003, new Cfg_Treasure_Recovery_Bean(1003,"60085,9",0,"6,1500"));
        contaioners.put(2001, new Cfg_Treasure_Recovery_Bean(2001,"14103,0}14203,1}14303,2",2553000,"6,4000"));
        contaioners.put(2002, new Cfg_Treasure_Recovery_Bean(2002,"60082,9",2553000,"6,2000"));
        contaioners.put(2003, new Cfg_Treasure_Recovery_Bean(2003,"60086,9",2553000,"6,2000"));
    }

}
