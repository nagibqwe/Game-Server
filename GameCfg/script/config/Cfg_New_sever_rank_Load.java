/**
 * Auto generated, do not edit it
 *
 * new_sever_rank配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_New_sever_rank_Bean; 

	
public final class Cfg_New_sever_rank_Load implements IScriptConfig<Cfg_New_sever_rank_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_New_sever_rank_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_New_sever_rank_Bean(1,0,101,2,250,"100,50","修炼狂人",10,"0,14"));
        contaioners.put(8, new Cfg_New_sever_rank_Bean(8,0,103,3,250,"100,50","一骑当先",10,"0,14"));
        contaioners.put(4, new Cfg_New_sever_rank_Bean(4,0,106,4,250,"100,50","炼器宗师",10,"0,14"));
        contaioners.put(5, new Cfg_New_sever_rank_Bean(5,0,117,5,250,"100,50","御宠天尊",10,"0,14"));
        contaioners.put(6, new Cfg_New_sever_rank_Bean(6,0,114,6,250,"100,50","五光十色",10,"0,14"));
        contaioners.put(7, new Cfg_New_sever_rank_Bean(7,0,102,7,250,"100,50","器化融神",10,"0,14"));
    }

}
