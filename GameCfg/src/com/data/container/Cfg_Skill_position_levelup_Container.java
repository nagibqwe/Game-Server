/**
 * Auto generated, do not edit it
 *
 * skill_position_levelup配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Skill_position_levelup_Bean; 
import config.Cfg_Skill_position_levelup_Load;

	
public final class Cfg_Skill_position_levelup_Container extends ConfigBase<Cfg_Skill_position_levelup_Bean> {
	
    /**
     * 初始化skill_position_levelup配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Skill_position_levelup_Container(){
        Cfg_Skill_position_levelup_Load load = new Cfg_Skill_position_levelup_Load();
        Map<Integer, Cfg_Skill_position_levelup_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Skill_position_levelup_Bean[] newBeanArray(int size) {
        return new Cfg_Skill_position_levelup_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Skill_position_levelup_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Skill_position_levelup_Container();
        }
        
        Cfg_Skill_position_levelup_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Skill_position_levelup_Container的实例对象
     *
     * @return
     */
    public static Cfg_Skill_position_levelup_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
