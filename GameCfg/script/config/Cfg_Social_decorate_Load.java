/**
 * Auto generated, do not edit it
 *
 * social_decorate配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Social_decorate_Bean; 

	
public final class Cfg_Social_decorate_Load implements IScriptConfig<Cfg_Social_decorate_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Social_decorate_Bean> contaioners){
        contaioners.clear();
        contaioners.put(101, new Cfg_Social_decorate_Bean(101,"装饰1",1,1,0));
        contaioners.put(102, new Cfg_Social_decorate_Bean(102,"装饰2",1,2,0));
        contaioners.put(103, new Cfg_Social_decorate_Bean(103,"装饰3",1,3,0));
        contaioners.put(104, new Cfg_Social_decorate_Bean(104,"装饰4",1,4,0));
        contaioners.put(105, new Cfg_Social_decorate_Bean(105,"装饰5",1,5,0));
        contaioners.put(201, new Cfg_Social_decorate_Bean(201,"挂件1",2,1,0));
        contaioners.put(202, new Cfg_Social_decorate_Bean(202,"挂件2",2,2,0));
        contaioners.put(203, new Cfg_Social_decorate_Bean(203,"挂件3",2,3,0));
        contaioners.put(204, new Cfg_Social_decorate_Bean(204,"挂件4",2,4,0));
        contaioners.put(205, new Cfg_Social_decorate_Bean(205,"挂件5",2,5,0));
    }

}
