/**
 * Auto generated, do not edit it
 *
 * wash_best配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Wash_best_Bean; 

	
public final class Cfg_Wash_best_Load implements IScriptConfig<Cfg_Wash_best_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Wash_best_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1000, new Cfg_Wash_best_Bean(1000,0,45000,"35,300",220));
        contaioners.put(1001, new Cfg_Wash_best_Bean(1001,1,45000,"35,300",240));
        contaioners.put(1002, new Cfg_Wash_best_Bean(1002,2,45000,"35,300",260));
        contaioners.put(1003, new Cfg_Wash_best_Bean(1003,3,45000,"35,300",280));
        contaioners.put(1004, new Cfg_Wash_best_Bean(1004,4,45000,"36,300",300));
        contaioners.put(1005, new Cfg_Wash_best_Bean(1005,5,45000,"36,300",320));
        contaioners.put(1006, new Cfg_Wash_best_Bean(1006,6,45000,"36,300",340));
        contaioners.put(1007, new Cfg_Wash_best_Bean(1007,7,45000,"36,300",360));
        contaioners.put(1008, new Cfg_Wash_best_Bean(1008,8,45000,"36,300",380));
        contaioners.put(1009, new Cfg_Wash_best_Bean(1009,9,45000,"36,300",400));
        contaioners.put(1010, new Cfg_Wash_best_Bean(1010,10,45000,"36,300",420));
    }

}
