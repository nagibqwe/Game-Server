/**
 * Auto generated, do not edit it
 *
 * GroundBuff配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_GroundBuff_Bean; 

	
public final class Cfg_GroundBuff_Load implements IScriptConfig<Cfg_GroundBuff_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_GroundBuff_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_GroundBuff_Bean(1,5000,39,100,5,1,3000,0));
        contaioners.put(2, new Cfg_GroundBuff_Bean(2,5001,40,100,0,1,3000,0));
        contaioners.put(3, new Cfg_GroundBuff_Bean(3,5002,41,100,0,1,3000,0));
        contaioners.put(4, new Cfg_GroundBuff_Bean(4,5004,62,8000,8,30,1000,-1));
        contaioners.put(5, new Cfg_GroundBuff_Bean(5,5005,39,100,2,1,3000,0));
        contaioners.put(6, new Cfg_GroundBuff_Bean(6,5006,40,100,2,1,3000,0));
        contaioners.put(7, new Cfg_GroundBuff_Bean(7,5007,41,100,2,1,3000,0));
        contaioners.put(8, new Cfg_GroundBuff_Bean(8,80056,103,15000,8,9999,3000,-1));
    }

}
