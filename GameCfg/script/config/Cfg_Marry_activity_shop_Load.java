/**
 * Auto generated, do not edit it
 *
 * marry_activity_shop配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Marry_activity_shop_Bean; 

	
public final class Cfg_Marry_activity_shop_Load implements IScriptConfig<Cfg_Marry_activity_shop_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Marry_activity_shop_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_Marry_activity_shop_Bean(1,"萍水相逢礼包","12,6000","12,998",3,"50001,20,1,9}16101,10,1,9}1052,10,1,9"));
        contaioners.put(2, new Cfg_Marry_activity_shop_Bean(2,"比翼双飞礼包","12,20000","12,9998",3,"50002,1,1,9}16101,15,1,9}1052,15,1,9"));
        contaioners.put(3, new Cfg_Marry_activity_shop_Bean(3,"情比金坚礼包","1,1200","1,600",3,"50002,3,1,9}16101,15,1,9}1052,15,1,9}16006,1,1,9"));
        contaioners.put(4, new Cfg_Marry_activity_shop_Bean(4,"海枯石烂礼包","1,2000","1,998",3,"50003,2,1,9}16101,20,1,9}1052,20,1,9"));
        contaioners.put(5, new Cfg_Marry_activity_shop_Bean(5,"至死不渝礼包","1,2200","1,1098",3,"50003,1,1,9}16101,20,1,9}1052,20,1,9}16007,1,1,9"));
    }

}
