/**
 * Auto generated, do not edit it
 *
 * social_house配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Social_house_Bean; 

	
public final class Cfg_Social_house_Load implements IScriptConfig<Cfg_Social_house_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Social_house_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_Social_house_Bean(1,"初级房屋",0,0,"1,998",10,10));
        contaioners.put(2, new Cfg_Social_house_Bean(2,"高级房屋",0,0,"1,3998",16,16));
        contaioners.put(3, new Cfg_Social_house_Bean(3,"豪华房屋",0,0,"",20,20));
    }

}
