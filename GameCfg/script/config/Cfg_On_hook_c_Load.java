/**
 * Auto generated, do not edit it
 *
 * on_hook_c配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_On_hook_c_Bean; 

	
public final class Cfg_On_hook_c_Load implements IScriptConfig<Cfg_On_hook_c_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_On_hook_c_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_On_hook_c_Bean(1,"经验药剂",1099,1,200));
        contaioners.put(8, new Cfg_On_hook_c_Bean(8,"升仙令加成",1098,1,5));
        contaioners.put(10, new Cfg_On_hook_c_Bean(10,"神品手镯加成",2124,1,24));
        contaioners.put(11, new Cfg_On_hook_c_Bean(11,"神品耳环加成",2123,1,24));
        contaioners.put(6, new Cfg_On_hook_c_Bean(6,"法宝核心",1095,1,40));
        contaioners.put(5, new Cfg_On_hook_c_Bean(5,"世界等级",1097,0,0));
    }

}
