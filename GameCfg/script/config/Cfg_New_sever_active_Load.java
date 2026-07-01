/**
 * Auto generated, do not edit it
 *
 * new_sever_active配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_New_sever_active_Bean; 

	
public final class Cfg_New_sever_active_Load implements IScriptConfig<Cfg_New_sever_active_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_New_sever_active_Bean> contaioners){
        contaioners.clear();
        contaioners.put(101, new Cfg_New_sever_active_Bean(101,1,1,"","50,1","创建1个宗派",0,"60001,1"));
        contaioners.put(102, new Cfg_New_sever_active_Bean(102,1,2,"","51,2","任命2个副宗主",0,"60002,1"));
        contaioners.put(103, new Cfg_New_sever_active_Bean(103,1,3,"","52,20","宗派成员到达20个",0,"60003,1"));
        contaioners.put(104, new Cfg_New_sever_active_Bean(104,1,4,"","52,30","宗派成员到达30个",0,"60004,1"));
        contaioners.put(105, new Cfg_New_sever_active_Bean(105,1,5,"","53,2","宗派等级到达2级",0,"60001,1"));
        contaioners.put(106, new Cfg_New_sever_active_Bean(106,1,6,"","53,3","宗派等级到达3级",0,"60001,1"));
        contaioners.put(201, new Cfg_New_sever_active_Bean(201,2,1,"","19,3","境界到达开光级",0,"60001,1"));
        contaioners.put(202, new Cfg_New_sever_active_Bean(202,2,2,"","19,4","境界到达灵虚级",0,"60002,1"));
        contaioners.put(203, new Cfg_New_sever_active_Bean(203,2,3,"","19,5","境界到达灵寂级",0,"60003,1"));
        contaioners.put(204, new Cfg_New_sever_active_Bean(204,2,4,"","19,6","境界到达结丹级",10,"60004,1"));
        contaioners.put(205, new Cfg_New_sever_active_Bean(205,2,5,"","19,7","境界到达金丹级",0,"60001,1"));
        contaioners.put(206, new Cfg_New_sever_active_Bean(206,2,6,"","19,8","境界到达元婴级",0,"60001,1"));
        contaioners.put(301, new Cfg_New_sever_active_Bean(301,3,1,"","54,1","完成普通结婚",0,"60001,1"));
        contaioners.put(302, new Cfg_New_sever_active_Bean(302,3,2,"","54,2","完成豪华结婚",0,"60002,1"));
        contaioners.put(303, new Cfg_New_sever_active_Bean(303,3,3,"","54,3","完成奢华结婚",0,"60003,1"));
        contaioners.put(304, new Cfg_New_sever_active_Bean(304,3,4,"","54,4","前10个完成奢华结婚",10,"60004,1"));
        contaioners.put(401, new Cfg_New_sever_active_Bean(401,4,1,"6,0","19,3","联盟争霸中霸主的宗主",0,"60001,1"));
        contaioners.put(402, new Cfg_New_sever_active_Bean(402,4,2,"","19,4","联盟争霸中积分排名1领取",0,"60002,1"));
        contaioners.put(403, new Cfg_New_sever_active_Bean(403,4,3,"","19,5","联盟争霸中积分排名2-5领取",0,"60003,1"));
        contaioners.put(404, new Cfg_New_sever_active_Bean(404,4,4,"","19,6","联盟争霸中积分排名6-30领取",0,"60004,1"));
        contaioners.put(405, new Cfg_New_sever_active_Bean(405,4,5,"","19,7","联盟争霸中非霸主的宗派领取",0,"60001,1"));
        contaioners.put(406, new Cfg_New_sever_active_Bean(406,4,6,"","19,8","联盟争霸中霸主宗派成员领取",0,"60001,1"));
    }

}
