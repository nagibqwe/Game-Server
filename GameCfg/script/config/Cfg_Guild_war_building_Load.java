/**
 * Auto generated, do not edit it
 *
 * guild_war_building配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Guild_war_building_Bean; 

	
public final class Cfg_Guild_war_building_Load implements IScriptConfig<Cfg_Guild_war_building_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Guild_war_building_Bean> contaioners){
        contaioners.clear();
        contaioners.put(20000101, new Cfg_Guild_war_building_Bean(20000101,"灵门·外",3,"91,64",229,1100003,"DynamicBlocker4",10,"100,50","75,50","100,50","75,50",2001,120));
        contaioners.put(20000102, new Cfg_Guild_war_building_Bean(20000102,"灵门·外",3,"91,64",400,1100003,"DynamicBlocker4",10,"100,50","75,50","100,50","75,50",2001,120));
        contaioners.put(20000103, new Cfg_Guild_war_building_Bean(20000103,"灵门·外",3,"91,64",800,1100003,"DynamicBlocker4",10,"100,50","75,50","100,50","75,50",2001,120));
        contaioners.put(20000201, new Cfg_Guild_war_building_Bean(20000201,"灵门·中",2,"148,63",229,1100004,"DynamicBlocker5",5,"200,100","150,100","200,100","150,100",2002,120));
        contaioners.put(20000202, new Cfg_Guild_war_building_Bean(20000202,"灵门·中",2,"148,63",400,1100004,"DynamicBlocker5",5,"200,100","150,100","200,100","150,100",2002,120));
        contaioners.put(20000203, new Cfg_Guild_war_building_Bean(20000203,"灵门·中",2,"148,63",800,1100004,"DynamicBlocker5",5,"200,100","150,100","200,100","150,100",2002,120));
        contaioners.put(20000301, new Cfg_Guild_war_building_Bean(20000301,"灵门·内",1,"199,64",229,1100005,"DynamicBlocker6",3,"300,150","250,150","300,150","250,150",2003,120));
        contaioners.put(20000302, new Cfg_Guild_war_building_Bean(20000302,"灵门·内",1,"199,64",400,1100005,"DynamicBlocker6",3,"300,150","250,150","300,150","250,150",2003,120));
        contaioners.put(20000303, new Cfg_Guild_war_building_Bean(20000303,"灵门·内",1,"199,64",800,1100005,"DynamicBlocker6",3,"300,150","250,150","300,150","250,150",2003,120));
        contaioners.put(20000401, new Cfg_Guild_war_building_Bean(20000401,"上古灵核",0,"230,64",229,0,"",3,"500,350","400,350","","",0,0));
        contaioners.put(20000402, new Cfg_Guild_war_building_Bean(20000402,"上古灵核",0,"230,64",400,0,"",3,"500,350","400,350","","",0,0));
        contaioners.put(20000403, new Cfg_Guild_war_building_Bean(20000403,"上古灵核",0,"230,64",800,0,"",3,"500,350","400,350","","",0,0));
    }

}
