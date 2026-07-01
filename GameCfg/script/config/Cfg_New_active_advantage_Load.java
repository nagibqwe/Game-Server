/**
 * Auto generated, do not edit it
 *
 * new_active_advantage配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_New_active_advantage_Bean; 

	
public final class Cfg_New_active_advantage_Load implements IScriptConfig<Cfg_New_active_advantage_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_New_active_advantage_Bean> contaioners){
        contaioners.clear();
        contaioners.put(116, new Cfg_New_active_advantage_Bean(116,"完成续充",1,1,"93,360","10003,1,1,9}16003,1,1,9}1017,50,1,0"));
        contaioners.put(112, new Cfg_New_active_advantage_Bean(112,"万妖卷层数达到10",1,1,"35,10","1017,60,1,9}10001,5,1,9}3,100000,1,9"));
        contaioners.put(113, new Cfg_New_active_advantage_Bean(113,"装备强化等级达到30",1,1,"20,30","1017,120,1,9}10001,10,1,9}3,200000,1,9"));
        contaioners.put(115, new Cfg_New_active_advantage_Bean(115,"穿戴6件5阶红1星以上品质的装备",1,1,"15,5,7,1,6","1017,30,1,9}10001,2,1,9}3,60000,1,9"));
        contaioners.put(117, new Cfg_New_active_advantage_Bean(117,"次日登录",1,1,"7,2","1017,50,1,9}10001,3,1,9}3,80000,1,9"));
        contaioners.put(211, new Cfg_New_active_advantage_Bean(211,"总任务进度",1,2,"","2310471,1,1,9"));
        contaioners.put(121, new Cfg_New_active_advantage_Bean(121,"低级婚礼",2,1,"149,1,1","50001,1,1,9}1052,5,1,9}16101,5,1,9}3,50000,1,9"));
        contaioners.put(122, new Cfg_New_active_advantage_Bean(122,"中级婚礼",2,1,"149,2,1","50001,6,1,9}1052,10,1,9}16101,10,1,9}3,100000,1,9"));
        contaioners.put(123, new Cfg_New_active_advantage_Bean(123,"高级婚礼",2,1,"149,3,1","50001,24,1,9}1052,20,1,9}16101,20,1,9}3,200000,1,9"));
        contaioners.put(221, new Cfg_New_active_advantage_Bean(221,"总任务进度",2,2,"","30309,1,1,9"));
    }

}
