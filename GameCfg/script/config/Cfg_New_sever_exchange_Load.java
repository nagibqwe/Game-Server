/**
 * Auto generated, do not edit it
 *
 * new_sever_exchange配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_New_sever_exchange_Bean; 

	
public final class Cfg_New_sever_exchange_Load implements IScriptConfig<Cfg_New_sever_exchange_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_New_sever_exchange_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_New_sever_exchange_Bean(1,"2001,1}2002,1",0,"2011,1"));
        contaioners.put(2, new Cfg_New_sever_exchange_Bean(2,"2001,1}2003,1",0,"2012,1"));
        contaioners.put(3, new Cfg_New_sever_exchange_Bean(3,"2002,1}2003,1",0,"2013,1"));
        contaioners.put(4, new Cfg_New_sever_exchange_Bean(4,"2001,1}2002,1}2003,1}2004,1",2,"2014,1"));
    }

}
