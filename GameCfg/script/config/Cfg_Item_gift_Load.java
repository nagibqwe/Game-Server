/**
 * Auto generated, do not edit it
 *
 * item_gift配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Item_gift_Bean; 

	
public final class Cfg_Item_gift_Load implements IScriptConfig<Cfg_Item_gift_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Item_gift_Bean> contaioners){
        contaioners.clear();
        contaioners.put(50001, new Cfg_Item_gift_Bean(50001,"一朵红玫瑰",2,10,0));
        contaioners.put(50002, new Cfg_Item_gift_Bean(50002,"一束红玫瑰",2,120,0));
        contaioners.put(50003, new Cfg_Item_gift_Bean(50003,"玫瑰花篮",2,1314,0));
    }

}
