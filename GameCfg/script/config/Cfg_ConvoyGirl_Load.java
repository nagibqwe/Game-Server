/**
 * Auto generated, do not edit it
 *
 * ConvoyGirl配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_ConvoyGirl_Bean; 

	
public final class Cfg_ConvoyGirl_Load implements IScriptConfig<Cfg_ConvoyGirl_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_ConvoyGirl_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_ConvoyGirl_Bean(1,"精卫",2275,"3,20000,0,9",60,"","tex_n_b_xiannvhusong1"));
        contaioners.put(2, new Cfg_ConvoyGirl_Bean(2,"嫦娥",2276,"3,40000,0,9",60,"1379,1","tex_n_b_xiannvhusong2"));
        contaioners.put(3, new Cfg_ConvoyGirl_Bean(3,"羲和",2277,"3,60000,0,9",60,"1379,2","tex_n_b_xiannvhusong3"));
        contaioners.put(4, new Cfg_ConvoyGirl_Bean(4,"九天玄女",2278,"3,80000,0,9",60,"1379,3","tex_n_b_xiannvhusong4"));
    }

}
