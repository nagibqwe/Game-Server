/**
 * Auto generated, do not edit it
 *
 * investPeak_Level配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_InvestPeak_Level_Bean; 

	
public final class Cfg_InvestPeak_Level_Load implements IScriptConfig<Cfg_InvestPeak_Level_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_InvestPeak_Level_Bean> contaioners){
        contaioners.clear();
        contaioners.put(3343, new Cfg_InvestPeak_Level_Bean(3343,1,880,1,"投资[FF0000FF]880[-]灵玉，十五倍返利#n可领取[FF0000FF]14100[-]绑定元宝"));
        contaioners.put(3344, new Cfg_InvestPeak_Level_Bean(3344,1,9999,0,"投资[FF0000FF]128[-]元，十倍返利#n可领取[FF0000FF]14410[-]绑定元宝"));
        contaioners.put(3345, new Cfg_InvestPeak_Level_Bean(3345,1,9999,0,"投资[FF0000FF]188[-]元，十倍返利#n可领取[FF0000FF]6880[-]绑定元宝"));
    }

}
