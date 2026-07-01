/**
 * Auto generated, do not edit it
 *
 * marry_dinner配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Marry_dinner_Bean; 

	
public final class Cfg_Marry_dinner_Load implements IScriptConfig<Cfg_Marry_dinner_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Marry_dinner_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_Marry_dinner_Bean(1,"普通婚宴","12,10000","30301,1,1,9}1053,1,1,9",1,0,0));
        contaioners.put(2, new Cfg_Marry_dinner_Bean(2,"高级婚宴","1,520","1078,1,1,0}1079,1,1,1}1172,1,1,2}1173,1,1,3}30302,1,1,9}1053,1,1,9",2,0,1));
        contaioners.put(3, new Cfg_Marry_dinner_Bean(3,"豪华婚宴","1,1314","1078,1,1,0}1079,1,1,1}1172,1,1,2}1173,1,1,3}30303,1,1,9}1053,1,1,9",3,0,1));
    }

}
