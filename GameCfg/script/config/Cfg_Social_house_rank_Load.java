/**
 * Auto generated, do not edit it
 *
 * social_house_rank配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Social_house_rank_Bean; 

	
public final class Cfg_Social_house_rank_Load implements IScriptConfig<Cfg_Social_house_rank_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Social_house_rank_Bean> contaioners){
        contaioners.clear();
        contaioners.put(101, new Cfg_Social_house_rank_Bean(101,1,"1,1","10001,1}10002,2}10003,3",50001));
        contaioners.put(102, new Cfg_Social_house_rank_Bean(102,1,"2,2","10001,1}10002,2}10003,2",50001));
        contaioners.put(103, new Cfg_Social_house_rank_Bean(103,1,"3,3","10001,1}10002,2}10003,1",50001));
        contaioners.put(104, new Cfg_Social_house_rank_Bean(104,1,"4,10","10001,1}10002,2",50001));
        contaioners.put(105, new Cfg_Social_house_rank_Bean(105,1,"10,20","10001,1}10002,1",50001));
        contaioners.put(106, new Cfg_Social_house_rank_Bean(106,1,"20,999","10001,1",50001));
        contaioners.put(201, new Cfg_Social_house_rank_Bean(201,2,"1,1","10001,1}10002,2}10003,3",60001));
        contaioners.put(202, new Cfg_Social_house_rank_Bean(202,2,"2,2","10001,1}10002,2}10003,2",60001));
        contaioners.put(203, new Cfg_Social_house_rank_Bean(203,2,"3,3","10001,1}10002,2}10003,1",60001));
        contaioners.put(204, new Cfg_Social_house_rank_Bean(204,2,"4,10","10001,1}10002,2",60001));
        contaioners.put(205, new Cfg_Social_house_rank_Bean(205,2,"10,20","10001,1}10002,1",60001));
        contaioners.put(206, new Cfg_Social_house_rank_Bean(206,2,"20,999","10001,1",60001));
    }

}
