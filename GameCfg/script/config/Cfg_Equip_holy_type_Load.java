/**
 * Auto generated, do not edit it
 *
 * Equip_holy_type配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Equip_holy_type_Bean; 

	
public final class Cfg_Equip_holy_type_Load implements IScriptConfig<Cfg_Equip_holy_type_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Equip_holy_type_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_Equip_holy_type_Bean(1,"真火圣装",1,"101,102,103,104,105,106,107,108,109,110,111",560,6,110000011,6,210000011,8,7,110000012,210000012));
        contaioners.put(2, new Cfg_Equip_holy_type_Bean(2,"天雷圣装",9999,"112,113,114,115,116,117,118,119,120,121,122",560,10,110000013,10,210000013,12,8,110000014,210000014));
        contaioners.put(3, new Cfg_Equip_holy_type_Bean(3,"玄冰圣装",9999,"123,124,125,126,127,128,129,130,131,132,133",560,14,110000015,14,210000015,16,8,110000016,210000016));
    }

}
