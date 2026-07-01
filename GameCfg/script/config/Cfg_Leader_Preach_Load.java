/**
 * Auto generated, do not edit it
 *
 * Leader_Preach配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Leader_Preach_Bean; 

	
public final class Cfg_Leader_Preach_Load implements IScriptConfig<Cfg_Leader_Preach_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Leader_Preach_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_Leader_Preach_Bean(1,"太极阵法","tex_zmcd",80112,2,"30,100063,120"));
        contaioners.put(2, new Cfg_Leader_Preach_Bean(2,"玄灵阵法","tex_zmcd1",80113,5,"30,100063,120"));
        contaioners.put(3, new Cfg_Leader_Preach_Bean(3,"诛仙阵法","tex_zmcd2",80114,8,"30,100063,120"));
        contaioners.put(4, new Cfg_Leader_Preach_Bean(4,"无量阵法","tex_zmcd3",80111,10,"30,100063,120"));
    }

}
