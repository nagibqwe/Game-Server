/**
 * Auto generated, do not edit it
 *
 * rank配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_Rank_Bean; 

	
public final class Cfg_Rank_Load implements IScriptConfig<Cfg_Rank_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_Rank_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1101000, new Cfg_Rank_Bean(1101000,"战斗力排行","1,80"));
        contaioners.put(1102000, new Cfg_Rank_Bean(1102000,"等级排行","1,80"));
        contaioners.put(1103000, new Cfg_Rank_Bean(1103000,"财富排行","1,80"));
        contaioners.put(1104000, new Cfg_Rank_Bean(1104000,"竞技场排行","1,80"));
        contaioners.put(1105000, new Cfg_Rank_Bean(1105000,"坐骑排行","96,1"));
        contaioners.put(1106000, new Cfg_Rank_Bean(1106000,"翅膀排行","127,1"));
        contaioners.put(1107000, new Cfg_Rank_Bean(1107000,"宠物精灵球","7,1"));
        contaioners.put(1108000, new Cfg_Rank_Bean(1108000,"战魂战力","123,1"));
        contaioners.put(1109000, new Cfg_Rank_Bean(1109000,"时装强化","1,80"));
        contaioners.put(1110000, new Cfg_Rank_Bean(1110000,"装备封印","126,1"));
        contaioners.put(1111000, new Cfg_Rank_Bean(1111000,"装备技能","125,1"));
        contaioners.put(1112000, new Cfg_Rank_Bean(1112000,"装备升星","19,1"));
        contaioners.put(1113000, new Cfg_Rank_Bean(1113000,"翅膀等级","127,1"));
        contaioners.put(1114000, new Cfg_Rank_Bean(1114000,"坐骑等级","128,1"));
        contaioners.put(1115000, new Cfg_Rank_Bean(1115000,"器灵等级","129,1"));
        contaioners.put(1116000, new Cfg_Rank_Bean(1116000,"宠物献祭","184,1"));
        contaioners.put(1117000, new Cfg_Rank_Bean(1117000,"时装回收","185,1"));
    }

}
