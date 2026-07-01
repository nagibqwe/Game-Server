/**
 * Auto generated, do not edit it
 *
 * VipRebate配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_VipRebate_Bean; 

	
public final class Cfg_VipRebate_Load implements IScriptConfig<Cfg_VipRebate_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_VipRebate_Bean> contaioners){
        contaioners.clear();
        contaioners.put(101, new Cfg_VipRebate_Bean(101,"1,175",1,0));
        contaioners.put(102, new Cfg_VipRebate_Bean(102,"3,300000",1,0));
        contaioners.put(103, new Cfg_VipRebate_Bean(103,"11,1",1,0));
        contaioners.put(104, new Cfg_VipRebate_Bean(104,"142,1",1,0));
        contaioners.put(105, new Cfg_VipRebate_Bean(105,"87,1,5",1,0));
        contaioners.put(106, new Cfg_VipRebate_Bean(106,"38,3",1,0));
        contaioners.put(107, new Cfg_VipRebate_Bean(107,"40,10",1,0));
        contaioners.put(108, new Cfg_VipRebate_Bean(108,"55,5,7,1,3",1,0));
        contaioners.put(201, new Cfg_VipRebate_Bean(201,"1,210",2,101));
        contaioners.put(202, new Cfg_VipRebate_Bean(202,"3,500000",2,102));
        contaioners.put(203, new Cfg_VipRebate_Bean(203,"87,16,5",2,0));
        contaioners.put(204, new Cfg_VipRebate_Bean(204,"35,15",2,0));
        contaioners.put(205, new Cfg_VipRebate_Bean(205,"22,20",2,0));
        contaioners.put(206, new Cfg_VipRebate_Bean(206,"56,30",2,0));
        contaioners.put(207, new Cfg_VipRebate_Bean(207,"20,60",2,0));
        contaioners.put(208, new Cfg_VipRebate_Bean(208,"40,15",2,107));
        contaioners.put(301, new Cfg_VipRebate_Bean(301,"1,250",3,201));
        contaioners.put(302, new Cfg_VipRebate_Bean(302,"3,1000000",3,202));
        contaioners.put(303, new Cfg_VipRebate_Bean(303,"87,16,15",3,203));
        contaioners.put(304, new Cfg_VipRebate_Bean(304,"86,5",3,0));
        contaioners.put(305, new Cfg_VipRebate_Bean(305,"40,25",3,208));
        contaioners.put(306, new Cfg_VipRebate_Bean(306,"180,1",3,0));
        contaioners.put(307, new Cfg_VipRebate_Bean(307,"56,50",3,206));
        contaioners.put(308, new Cfg_VipRebate_Bean(308,"22,50",3,205));
        contaioners.put(401, new Cfg_VipRebate_Bean(401,"86,10",4,304));
        contaioners.put(402, new Cfg_VipRebate_Bean(402,"137,1",4,0));
        contaioners.put(403, new Cfg_VipRebate_Bean(403,"100,3",4,0));
        contaioners.put(404, new Cfg_VipRebate_Bean(404,"55,7,7,5,3",4,0));
        contaioners.put(405, new Cfg_VipRebate_Bean(405,"150,50",4,0));
        contaioners.put(406, new Cfg_VipRebate_Bean(406,"87,3,3",4,0));
        contaioners.put(407, new Cfg_VipRebate_Bean(407,"198,5",4,0));
        contaioners.put(408, new Cfg_VipRebate_Bean(408,"40,35",4,305));
    }

}
