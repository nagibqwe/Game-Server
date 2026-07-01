/**
 * Auto generated, do not edit it
 *
 * relive配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Relive_Bean; 

	
public final class Cfg_Relive_Load implements IScriptConfig<Cfg_Relive_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Relive_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_Relive_Bean(1,1,0,0,"0,1",0,0,"",0,0));
        contaioners.put(2, new Cfg_Relive_Bean(2,1,2,5000,"",0,0,"",0,0));
        contaioners.put(3, new Cfg_Relive_Bean(3,0,1,5000,"",0,0,"",0,0));
        contaioners.put(4, new Cfg_Relive_Bean(4,1,0,0,"0,1",0,5000,"",0,0));
        contaioners.put(5, new Cfg_Relive_Bean(5,1,2,10000,"0,1",0,5000,"5000,60000",60000,0));
        contaioners.put(6, new Cfg_Relive_Bean(6,0,2,10000,"1.0",0,0,"",0,0));
        contaioners.put(7, new Cfg_Relive_Bean(7,0,0,0,"",0,0,"",0,0));
        contaioners.put(8, new Cfg_Relive_Bean(8,1,2,10000,"0,1",0,0,"",0,0));
        contaioners.put(9, new Cfg_Relive_Bean(9,1,2,10000,"1.0",5000,0,"",0,0));
        contaioners.put(10, new Cfg_Relive_Bean(10,1,2,10000,"0,1",0,0,"",0,1));
        contaioners.put(11, new Cfg_Relive_Bean(11,1,2,10000,"0,1",3000,0,"",0,0));
    }

}
