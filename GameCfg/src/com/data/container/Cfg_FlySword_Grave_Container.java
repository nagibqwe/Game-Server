/**
 * Auto generated, do not edit it
 *
 * FlySword_Grave配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_FlySword_Grave_Bean; 
import config.Cfg_FlySword_Grave_Load;

	
public final class Cfg_FlySword_Grave_Container extends ConfigBase<Cfg_FlySword_Grave_Bean> {
	
    /**
     * 初始化FlySword_Grave配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_FlySword_Grave_Container(){
        Cfg_FlySword_Grave_Load load = new Cfg_FlySword_Grave_Load();
        Map<Integer, Cfg_FlySword_Grave_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_FlySword_Grave_Bean[] newBeanArray(int size) {
        return new Cfg_FlySword_Grave_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_FlySword_Grave_Container manager;
        
        Singleton() {
            this.manager = new Cfg_FlySword_Grave_Container();
        }
        
        Cfg_FlySword_Grave_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_FlySword_Grave_Container的实例对象
     *
     * @return
     */
    public static Cfg_FlySword_Grave_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
