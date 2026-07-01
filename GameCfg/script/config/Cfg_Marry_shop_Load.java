/**
 * Auto generated, do not edit it
 *
 * marry_shop配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Marry_shop_Bean; 

	
public final class Cfg_Marry_shop_Load implements IScriptConfig<Cfg_Marry_shop_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Marry_shop_Bean> contaioners){
        contaioners.clear();
        contaioners.put(16318, new Cfg_Marry_shop_Bean(16318,"12,1000",10,30,0,400004));
        contaioners.put(16319, new Cfg_Marry_shop_Bean(16319,"1,20",50,8,1,400004));
        contaioners.put(50002, new Cfg_Marry_shop_Bean(50002,"1,99",99,10,0,0));
        contaioners.put(50003, new Cfg_Marry_shop_Bean(50003,"1,999",999,1,0,0));
    }

}
