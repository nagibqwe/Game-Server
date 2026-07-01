/**
 * Auto generated, do not edit it
 *
 * Hall_Fame配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Hall_Fame_Bean; 

	
public final class Cfg_Hall_Fame_Load implements IScriptConfig<Cfg_Hall_Fame_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Hall_Fame_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_Hall_Fame_Bean(1,3,"tex_zp_1","1,3,70001,30001}4,10,70002,14991}11,30,70003,7492"));
        contaioners.put(2, new Cfg_Hall_Fame_Bean(2,7,"tex_zp_1","1,3,70011,45018}4,10,70012,22509}11,30,70013,11246"));
        contaioners.put(3, new Cfg_Hall_Fame_Bean(3,15,"tex_zp_1","1,3,70021,67527}4,10,70022,33754}11,30,70023,16864"));
        contaioners.put(4, new Cfg_Hall_Fame_Bean(4,30,"tex_zp_1","1,3,70031,101281}4,10,70032,50637}11,30,70033,25318"));
        contaioners.put(5, new Cfg_Hall_Fame_Bean(5,365,"tex_zp_1","1,3,70041,151944}4,10,70042,75963}11,30,70043,37969"));
        contaioners.put(6, new Cfg_Hall_Fame_Bean(6,999,"tex_zp_1","1,3,70051,227907}4,10,70052,113950}11,30,70053,56966"));
    }

}
