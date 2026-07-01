/**
 * Auto generated, do not edit it
 *
 * SZZQAward配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_SZZQAward_Bean; 

	
public final class Cfg_SZZQAward_Load implements IScriptConfig<Cfg_SZZQAward_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_SZZQAward_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_SZZQAward_Bean(1,"第1名",1,1,1388,"16001,15,1}1017,20,1}9,50,1",0));
        contaioners.put(2, new Cfg_SZZQAward_Bean(2,"第2-3名",2,3,1389,"16001,14,1}1017,18,1}9,40,1",1));
        contaioners.put(3, new Cfg_SZZQAward_Bean(3,"第4-6名",4,6,1390,"16001,13,1}1017,16,1}9,35,1",2));
        contaioners.put(4, new Cfg_SZZQAward_Bean(4,"第7-9名",7,9,1391,"16001,12,1}1017,14,1}9,30,1",3));
    }

}
