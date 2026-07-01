/**
 * Auto generated, do not edit it
 *
 * activity_task_type配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Activity_task_type_Bean; 

	
public final class Cfg_Activity_task_type_Load implements IScriptConfig<Cfg_Activity_task_type_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Activity_task_type_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_Activity_task_type_Bean(1,"当日首次登录游戏",159,0));
        contaioners.put(2, new Cfg_Activity_task_type_Bean(2,"当日累计在线{0}分钟  {1}/{2}",166,0));
        contaioners.put(3, new Cfg_Activity_task_type_Bean(3,"当日充值任意金额",161,2750000));
        contaioners.put(4, new Cfg_Activity_task_type_Bean(4,"世界频道发言{0}次  {1}/{2}",180,0));
        contaioners.put(5, new Cfg_Activity_task_type_Bean(5,"当日进行掌门传道消耗{0}活跃点  {1}/{2}",168,2660000));
        contaioners.put(6, new Cfg_Activity_task_type_Bean(6,"当日进行修行{0}分钟  {1}/{2}",169,0));
        contaioners.put(7, new Cfg_Activity_task_type_Bean(7,"当日领取{0}次剑灵阁收益  {1}/{2}",170,1340000));
        contaioners.put(8, new Cfg_Activity_task_type_Bean(8,"当日挑战{0}次竞技场  {1}/{2}",171,1050000));
        contaioners.put(9, new Cfg_Activity_task_type_Bean(9,"当日击杀个人首领{0}次  {1}/{2}",172,1215000));
        contaioners.put(10, new Cfg_Activity_task_type_Bean(10,"当日击杀无限首领{0}次  {1}/{2}",173,1218000));
        contaioners.put(11, new Cfg_Activity_task_type_Bean(11,"当日击杀世界首领{0}次  {1}/{2}",174,1212000));
        contaioners.put(12, new Cfg_Activity_task_type_Bean(12,"当日击杀VIP首领{0}次  {1}/{2}",227,1213000));
        contaioners.put(13, new Cfg_Activity_task_type_Bean(13,"当日参与天禁之门{0}次  {1}/{2}",176,1231300));
        contaioners.put(14, new Cfg_Activity_task_type_Bean(14,"当日完成赏金之道{0}次  {1}/{2}",177,2100000));
        contaioners.put(15, new Cfg_Activity_task_type_Bean(15,"当日活跃度达到{0}  {1}/{2}",178,2100000));
    }

}
