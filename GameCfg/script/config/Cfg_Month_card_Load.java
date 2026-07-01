/**
 * Auto generated, do not edit it
 *
 * month_card配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Month_card_Bean; 

	
public final class Cfg_Month_card_Load implements IScriptConfig<Cfg_Month_card_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Month_card_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_Month_card_Bean(1,7,"1,300","2,300,1","2,600,1}60071,18,1}16,48,1",0,5000));
        contaioners.put(2, new Cfg_Month_card_Bean(2,30,"1,680","2,880,1","2,600,1}60071,18,1",0,15000));
        contaioners.put(4, new Cfg_Month_card_Bean(4,30,"1,300","2,300,1","2,120,1",0,5000));
        contaioners.put(5, new Cfg_Month_card_Bean(5,30,"1,680","2,680,1","2,210,1",0,15000));
    }

}
