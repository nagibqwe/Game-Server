/**
 * Auto generated, do not edit it
 *
 * npc_friend配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Npc_friend_Bean; 

	
public final class Cfg_Npc_friend_Load implements IScriptConfig<Cfg_Npc_friend_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Npc_friend_Bean> contaioners){
        contaioners.clear();
        contaioners.put(10001, new Cfg_Npc_friend_Bean(10001,"大师姐·姬如月",1,1,1100000008,-1,720,"1,143",0));
    }

}
