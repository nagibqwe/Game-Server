/**
 * Auto generated, do not edit it
 *
 * HuaxingFlySword_skill配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_HuaxingFlySword_skill_Bean; 

	
public final class Cfg_HuaxingFlySword_skill_Load implements IScriptConfig<Cfg_HuaxingFlySword_skill_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_HuaxingFlySword_skill_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_HuaxingFlySword_skill_Bean(1,"剑灵·恢复",90019,1,"1,4","[00ff00]轩辕剑灵[-]培养至[00ff00]4阶[-]可以激活"));
        contaioners.put(2, new Cfg_HuaxingFlySword_skill_Bean(2,"剑灵·攻击",90020,2,"2,7","[00ff00]任意2个剑灵[-]都培养到[00ff00]7阶[-]可以激活"));
        contaioners.put(3, new Cfg_HuaxingFlySword_skill_Bean(3,"剑灵·暴击",90021,2,"3,10","[00ff00]任意3个剑灵[-]都培养到[00ff00]10阶[-]可以激活"));
        contaioners.put(4, new Cfg_HuaxingFlySword_skill_Bean(4,"剑灵·破甲",90022,2,"4,12","[00ff00]任意4个剑灵[-]都培养到[00ff00]12阶[-]可以激活"));
        contaioners.put(5, new Cfg_HuaxingFlySword_skill_Bean(5,"剑灵·灵攻",90023,2,"5,15","[00ff00]任意5个剑灵[-]都培养到[00ff00]15阶[-]可以激活"));
        contaioners.put(6, new Cfg_HuaxingFlySword_skill_Bean(6,"剑灵·豁免",90024,2,"6,18","[00ff00]任意6个剑灵[-]都培养到[00ff00]18阶[-]可以激活"));
    }

}
