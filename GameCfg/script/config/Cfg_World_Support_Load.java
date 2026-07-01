/**
 * Auto generated, do not edit it
 *
 * World_Support配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_World_Support_Bean; 

	
public final class Cfg_World_Support_Load implements IScriptConfig<Cfg_World_Support_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_World_Support_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_World_Support_Bean(1,"1,250","9,100,1500","1030,5","81011,1","81011,1,3",5,10));
        contaioners.put(2, new Cfg_World_Support_Bean(2,"251,450","9,100,1500","1030,5","81011,1","81011,1,3",5,10));
        contaioners.put(3, new Cfg_World_Support_Bean(3,"451,600","9,100,1500","1030,5","81011,1","81011,1,3",5,10));
        contaioners.put(4, new Cfg_World_Support_Bean(4,"601,700","9,100,1500","1030,5","81011,1","81011,1,3",5,10));
        contaioners.put(5, new Cfg_World_Support_Bean(5,"701,800","9,100,1500","1030,5","81011,1","81011,1,3",5,10));
    }

}
