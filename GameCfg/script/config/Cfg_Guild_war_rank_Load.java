/**
 * Auto generated, do not edit it
 *
 * guild_war_rank配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Guild_war_rank_Bean; 

	
public final class Cfg_Guild_war_rank_Load implements IScriptConfig<Cfg_Guild_war_rank_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Guild_war_rank_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_Guild_war_rank_Bean(1,"天仙仙盟",3,"1,3","19007,3,0","19007,3,0}19008,3,0","19007,3,0","11,2000,1,9}1017,500,1,9","11,1500,1,9}1017,500,1,9","11,1500,1,9}1017,500,1,9"));
        contaioners.put(2, new Cfg_Guild_war_rank_Bean(2,"神仙仙盟",3,"4,6","19007,3,0","19007,2,0","19007,2,0","11,2000,1,9}1017,500,1,9","11,1500,1,9}1017,500,1,9","11,1500,1,9}1017,500,1,9"));
        contaioners.put(3, new Cfg_Guild_war_rank_Bean(3,"金仙仙盟",3,"7,9","19007,2,0","19007,1,0","19007,1,0","11,2000,1,9}1017,500,1,9","11,1500,1,9}1017,500,1,9","11,1500,1,9}1017,500,1,9"));
        contaioners.put(4, new Cfg_Guild_war_rank_Bean(4,"鬼仙仙盟",3,"-1","19007,1,0","19007,1,0","19007,1,0","11,2000,1,9}1017,500,1,9","11,1500,1,9}1017,500,1,9","11,1500,1,9}1017,500,1,9"));
    }

}
