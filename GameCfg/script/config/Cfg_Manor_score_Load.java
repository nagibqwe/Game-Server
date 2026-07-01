/**
 * Auto generated, do not edit it
 *
 * manor_score配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Manor_score_Bean; 

	
public final class Cfg_Manor_score_Load implements IScriptConfig<Cfg_Manor_score_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Manor_score_Bean> contaioners){
        contaioners.clear();
        contaioners.put(10, new Cfg_Manor_score_Bean(10,60001,"10001,10002,1003,1004"));
        contaioners.put(30, new Cfg_Manor_score_Bean(30,60002,"10001,10002,1003,1004"));
        contaioners.put(50, new Cfg_Manor_score_Bean(50,60003,"10001,10002,1003,1004"));
        contaioners.put(80, new Cfg_Manor_score_Bean(80,60004,"10001,10002,1003,1004"));
        contaioners.put(100, new Cfg_Manor_score_Bean(100,60001,"10001,10002,1003,1004"));
    }

}
