/**
 * Auto generated, do not edit it
 *
 * Cross_devil_Group_Boss配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Cross_devil_Group_Boss_Bean; 

	
public final class Cfg_Cross_devil_Group_Boss_Load implements IScriptConfig<Cfg_Cross_devil_Group_Boss_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Cross_devil_Group_Boss_Bean> contaioners){
        contaioners.clear();
        contaioners.put(28001, new Cfg_Cross_devil_Group_Boss_Bean(28001,"1,9999","1,9999",15,"玄灵魔尊",720,100));
        contaioners.put(28002, new Cfg_Cross_devil_Group_Boss_Bean(28002,"1,9999","1,9999",15,"祝火魔尊",721,100));
        contaioners.put(28003, new Cfg_Cross_devil_Group_Boss_Bean(28003,"1,9999","1,9999",15,"御守魔尊",722,100));
        contaioners.put(28004, new Cfg_Cross_devil_Group_Boss_Bean(28004,"1,9999","1,9999",15,"巫凤魔尊",723,100));
    }

}
