/**
 * Auto generated, do not edit it
 *
 * social_house_gift配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Social_house_gift_Bean; 

	
public final class Cfg_Social_house_gift_Load implements IScriptConfig<Cfg_Social_house_gift_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Social_house_gift_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_Social_house_gift_Bean(1,"礼物1",0,"12,3000",70,5));
        contaioners.put(2, new Cfg_Social_house_gift_Bean(2,"礼物2",0,"1,150",150,5));
        contaioners.put(3, new Cfg_Social_house_gift_Bean(3,"礼物3",0,"1,300",300,10));
    }

}
