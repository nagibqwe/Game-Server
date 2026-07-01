/**
 * Auto generated, do not edit it
 *
 * HappyWeek配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_HappyWeek_Bean; 

	
public final class Cfg_HappyWeek_Load implements IScriptConfig<Cfg_HappyWeek_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_HappyWeek_Bean> contaioners){
        contaioners.clear();
        contaioners.put(601, new Cfg_HappyWeek_Bean(601,6,1,"双倍传道，购买额外的活跃药水，祝您急速升级。（购买可同时获得等价值的灵玉）",101));
        contaioners.put(602, new Cfg_HappyWeek_Bean(602,6,2,"双倍传道，购买额外的活跃药水，祝您急速升级。（购买可同时获得等价值的灵玉）",102));
    }

}
