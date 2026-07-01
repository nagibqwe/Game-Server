/**
 * Auto generated, do not edit it
 *
 * invest_Level配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Invest_Level_Bean; 

	
public final class Cfg_Invest_Level_Load implements IScriptConfig<Cfg_Invest_Level_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Invest_Level_Bean> contaioners){
        contaioners.clear();
        contaioners.put(27, new Cfg_Invest_Level_Bean(27,1,880,1,"投资[FF0000FF]68[-]元，十倍返利#n可领取[FF0000FF]6880[-]绑定元宝"));
        contaioners.put(28, new Cfg_Invest_Level_Bean(28,1,1280,0,"投资[FF0000FF]128[-]元，十倍返利#n可领取[FF0000FF]6880[-]绑定元宝"));
        contaioners.put(29, new Cfg_Invest_Level_Bean(29,1,1880,0,"投资[FF0000FF]188[-]元，十倍返利#n可领取[FF0000FF]6880[-]绑定元宝"));
    }

}
