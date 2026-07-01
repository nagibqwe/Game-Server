/**
 * Auto generated, do not edit it
 *
 * shop_Mystery配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Shop_Mystery_Bean; 

	
public final class Cfg_Shop_Mystery_Load implements IScriptConfig<Cfg_Shop_Mystery_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Shop_Mystery_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1000, new Cfg_Shop_Mystery_Bean(1000,1006,1,1,-1,2,1000000,1,1,0));
        contaioners.put(1001, new Cfg_Shop_Mystery_Bean(1001,1004,1,1,-1,4,50,2,0,0));
        contaioners.put(1002, new Cfg_Shop_Mystery_Bean(1002,1005,1,1,-1,4,100,3,1,0));
        contaioners.put(2001, new Cfg_Shop_Mystery_Bean(2001,55001,2,1,-1,4,100,4,1,0));
        contaioners.put(2002, new Cfg_Shop_Mystery_Bean(2002,55101,2,1,-1,4,100,5,1,0));
        contaioners.put(2003, new Cfg_Shop_Mystery_Bean(2003,1007,2,1,-1,2,100000,6,0,0));
        contaioners.put(3000, new Cfg_Shop_Mystery_Bean(3000,50002,3,1,-1,4,10,8,1,0));
        contaioners.put(3001, new Cfg_Shop_Mystery_Bean(3001,50004,3,1,-1,4,80,9,1,0));
        contaioners.put(3002, new Cfg_Shop_Mystery_Bean(3002,50006,3,1,-1,4,400,10,1,0));
        contaioners.put(3003, new Cfg_Shop_Mystery_Bean(3003,50029,3,1,-1,4,10,11,1,0));
        contaioners.put(3004, new Cfg_Shop_Mystery_Bean(3004,50027,3,1,-1,4,10,12,1,0));
        contaioners.put(3005, new Cfg_Shop_Mystery_Bean(3005,50005,3,1,-1,4,150,12,1,0));
    }

}
