/**
 * Auto generated, do not edit it
 *
 * SoulArmor_signet_lottery_object配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_SoulArmor_signet_lottery_object_Bean; 

	
public final class Cfg_SoulArmor_signet_lottery_object_Load implements IScriptConfig<Cfg_SoulArmor_signet_lottery_object_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_SoulArmor_signet_lottery_object_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_SoulArmor_signet_lottery_object_Bean(1,"地灵回响",1,83086,"1,20",10));
        contaioners.put(2, new Cfg_SoulArmor_signet_lottery_object_Bean(2,"天灵回响",2,83087,"1,30",20));
        contaioners.put(3, new Cfg_SoulArmor_signet_lottery_object_Bean(3,"洪荒回响",3,83088,"1,50",50));
    }

}
