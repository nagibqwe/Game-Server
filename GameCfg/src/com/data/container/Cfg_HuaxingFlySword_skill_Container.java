/**
 * Auto generated, do not edit it
 *
 * HuaxingFlySword_skill配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_HuaxingFlySword_skill_Bean; 
import config.Cfg_HuaxingFlySword_skill_Load;

	
public final class Cfg_HuaxingFlySword_skill_Container extends ConfigBase<Cfg_HuaxingFlySword_skill_Bean> {
	
    /**
     * 初始化HuaxingFlySword_skill配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_HuaxingFlySword_skill_Container(){
        Cfg_HuaxingFlySword_skill_Load load = new Cfg_HuaxingFlySword_skill_Load();
        Map<Integer, Cfg_HuaxingFlySword_skill_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_HuaxingFlySword_skill_Bean[] newBeanArray(int size) {
        return new Cfg_HuaxingFlySword_skill_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_HuaxingFlySword_skill_Container manager;
        
        Singleton() {
            this.manager = new Cfg_HuaxingFlySword_skill_Container();
        }
        
        Cfg_HuaxingFlySword_skill_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_HuaxingFlySword_skill_Container的实例对象
     *
     * @return
     */
    public static Cfg_HuaxingFlySword_skill_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
