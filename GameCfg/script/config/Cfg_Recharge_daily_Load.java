/**
 * Auto generated, do not edit it
 *
 * recharge_daily配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Recharge_daily_Bean; 

	
public final class Cfg_Recharge_daily_Load implements IScriptConfig<Cfg_Recharge_daily_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Recharge_daily_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_Recharge_daily_Bean(1,60,1,60,"81081,1,1}1059,1,1}16002,1,1}18001,20,1}20001,1,1"));
        contaioners.put(3, new Cfg_Recharge_daily_Bean(3,300,1,300,"81081,2,1}1059,1,1}16002,2,1}18001,40,1}60004,2,1"));
        contaioners.put(5, new Cfg_Recharge_daily_Bean(5,680,1,680,"81081,3,1}1059,1,1}16002,3,1}18001,60,1}60004,2,1"));
        contaioners.put(7, new Cfg_Recharge_daily_Bean(7,1280,1,1280,"81081,4,1}1059,1,1}16002,4,1}18001,80,1}60004,2,1"));
        contaioners.put(2, new Cfg_Recharge_daily_Bean(2,60,2,60,"81005,1,1}1059,1,1}16002,1,1}18001,10,1}21001,1,1"));
        contaioners.put(4, new Cfg_Recharge_daily_Bean(4,300,2,200,"81081,1,1}1021,1,1}16002,2,1}18001,20,1}21001,2,1"));
        contaioners.put(6, new Cfg_Recharge_daily_Bean(6,680,2,600,"1021,1,1}1059,1,1}16002,3,1}18001,30,1}21001,3,1"));
        contaioners.put(8, new Cfg_Recharge_daily_Bean(8,1280,2,1200,"81081,2,1}1021,1,1}16002,4,1}18001,40,1}21001,4,1"));
    }

}
