/**
 * Auto generated, do not edit it
 *
 * pet_soul配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Pet_soul_Bean; 

	
public final class Cfg_Pet_soul_Load implements IScriptConfig<Cfg_Pet_soul_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Pet_soul_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_Pet_soul_Bean(1,10,210,16196,"1,79}2,1880","神佑魂珠"));
        contaioners.put(2, new Cfg_Pet_soul_Bean(2,10,210,16197,"1,83}3,58","狂暴魂珠"));
        contaioners.put(3, new Cfg_Pet_soul_Bean(3,10,210,16198,"2,2506}3,58","噬骨魂珠"));
        contaioners.put(4, new Cfg_Pet_soul_Bean(4,10,210,16199,"1,71}4,55","防护魂珠"));
        contaioners.put(5, new Cfg_Pet_soul_Bean(5,10,210,16200,"2,1880}4,61","金刚魂珠"));
    }

}
