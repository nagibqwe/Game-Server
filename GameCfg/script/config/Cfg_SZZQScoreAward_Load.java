/**
 * Auto generated, do not edit it
 *
 * SZZQScoreAward配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_SZZQScoreAward_Bean; 

	
public final class Cfg_SZZQScoreAward_Load implements IScriptConfig<Cfg_SZZQScoreAward_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_SZZQScoreAward_Bean> contaioners){
        contaioners.clear();
        contaioners.put(500, new Cfg_SZZQScoreAward_Bean(500,"16001,1,1}1017,5,1}9,10,1",0));
        contaioners.put(1200, new Cfg_SZZQScoreAward_Bean(1200,"16001,2,1}1017,5,1",1));
        contaioners.put(1700, new Cfg_SZZQScoreAward_Bean(1700,"16001,3,1}1017,5,1}9,15,1",2));
        contaioners.put(2300, new Cfg_SZZQScoreAward_Bean(2300,"16001,4,1}1017,5,1",3));
        contaioners.put(3000, new Cfg_SZZQScoreAward_Bean(3000,"16001,5,1}1017,5,1}9,25,1",4));
    }

}
