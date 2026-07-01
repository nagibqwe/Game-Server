/**
 * Auto generated, do not edit it
 *
 * Thai_score配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Thai_score_Bean; 

	
public final class Cfg_Thai_score_Load implements IScriptConfig<Cfg_Thai_score_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Thai_score_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_Thai_score_Bean(1,12,0,0,220));
    }

}
