/**
 * Auto generated, do not edit it
 *
 * JJCAeward配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_JJCAeward_Bean; 

	
public final class Cfg_JJCAeward_Load implements IScriptConfig<Cfg_JJCAeward_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_JJCAeward_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_JJCAeward_Bean(1,"1.0","9,200"));
        contaioners.put(2, new Cfg_JJCAeward_Bean(2,"2,5","9,160"));
        contaioners.put(3, new Cfg_JJCAeward_Bean(3,"6,20","9,150"));
        contaioners.put(4, new Cfg_JJCAeward_Bean(4,"21,100","9,140"));
        contaioners.put(5, new Cfg_JJCAeward_Bean(5,"101,300","9,130"));
        contaioners.put(6, new Cfg_JJCAeward_Bean(6,"301,500","9,120"));
        contaioners.put(7, new Cfg_JJCAeward_Bean(7,"501,800","9,110"));
        contaioners.put(8, new Cfg_JJCAeward_Bean(8,"801,1000","9,100"));
    }

}
