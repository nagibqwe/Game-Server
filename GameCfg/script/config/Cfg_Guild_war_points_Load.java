/**
 * Auto generated, do not edit it
 *
 * guild_war_points配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Guild_war_points_Bean; 

	
public final class Cfg_Guild_war_points_Load implements IScriptConfig<Cfg_Guild_war_points_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Guild_war_points_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_Guild_war_points_Bean(1,50,0));
        contaioners.put(2, new Cfg_Guild_war_points_Bean(2,30,0));
        contaioners.put(3, new Cfg_Guild_war_points_Bean(3,100,0));
        contaioners.put(4, new Cfg_Guild_war_points_Bean(4,100,50));
        contaioners.put(5, new Cfg_Guild_war_points_Bean(5,200,0));
        contaioners.put(6, new Cfg_Guild_war_points_Bean(6,200,50));
        contaioners.put(7, new Cfg_Guild_war_points_Bean(7,300,0));
        contaioners.put(8, new Cfg_Guild_war_points_Bean(8,300,50));
        contaioners.put(9, new Cfg_Guild_war_points_Bean(9,500,0));
        contaioners.put(10, new Cfg_Guild_war_points_Bean(10,0,0));
        contaioners.put(11, new Cfg_Guild_war_points_Bean(11,10,0));
    }

}
