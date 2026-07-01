/**
 * Auto generated, do not edit it
 *
 * state_xisui配置表
 */
 
package config;

import com.data.script.IScriptConfig;
import java.util.Map;
import com.data.bean.Cfg_State_xisui_Bean; 

	
public final class Cfg_State_xisui_Load implements IScriptConfig<Cfg_State_xisui_Bean> {
	
    /**
     * 数据加载
     * @param contaioners
     */
    @Override
    public void load(Map<Integer,Cfg_State_xisui_Bean> contaioners){
        contaioners.clear();
        contaioners.put(1, new Cfg_State_xisui_Bean(1,"意通周天","元婴","收集【灵气精粹】点亮星窍，贯通周天抵御元婴之劫（击败任意首领，离线挂机，掌门传道均有机会获取）","1,210"));
        contaioners.put(2, new Cfg_State_xisui_Bean(2,"五灵灌体","化神","修复【五灵洞真图】引五灵真气灌体抵御化神之劫（击败任意首领，离线挂机，掌门传道均有机会获取）","1,350"));
        contaioners.put(3, new Cfg_State_xisui_Bean(3,"神火锻体","合体","收集【六丁神火】净化凡尘混浊炼成混元金身抵御合体之劫（击败任意首领，离线挂机，掌门传道均有机会获取）","1,460"));
        contaioners.put(4, new Cfg_State_xisui_Bean(4,"一气化三清","大乘","收集【道气】炼成三清玉体抵御大乘之劫（击败任意首领，离线挂机，掌门传道均有机会获取）","1,620"));
        contaioners.put(5, new Cfg_State_xisui_Bean(5,"斩三世身","地仙","收集【因果之丝】凝聚因果之剑斩三世身抵御地仙之劫（击败任意首领，离线挂机，掌门传道均有机会获取）","1,740"));
        contaioners.put(6, new Cfg_State_xisui_Bean(6,"占位不开启","占位","占位不开启","1,9999}2,9999"));
    }

}
