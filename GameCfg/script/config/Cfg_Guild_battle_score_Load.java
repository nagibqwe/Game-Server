/**
 * Auto generated, do not edit it
 *
 * guild_battle_score配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Guild_battle_score_Bean; 

	
public final class Cfg_Guild_battle_score_Load implements IScriptConfig<Cfg_Guild_battle_score_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Guild_battle_score_Bean> contaioners){
        contaioners.clear();
        contaioners.put(80, new Cfg_Guild_battle_score_Bean(80,"1025,1,1,9","1025,1"));
        contaioners.put(160, new Cfg_Guild_battle_score_Bean(160,"60002,3,1,9","60002,3"));
        contaioners.put(330, new Cfg_Guild_battle_score_Bean(330,"1026,1,1,9","1026,1"));
        contaioners.put(670, new Cfg_Guild_battle_score_Bean(670,"1027,1,1,9","1027,1"));
        contaioners.put(800, new Cfg_Guild_battle_score_Bean(800,"1028,1,1,9","1028,1"));
    }

}
