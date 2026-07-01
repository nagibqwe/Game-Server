/**
 * Auto generated, do not edit it
 *
 * across配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Across_Bean; 
import config.Cfg_Across_Load;

	
public final class Cfg_Across_Container extends ConfigBase<Cfg_Across_Bean> {
	
    /**
     * 初始化across配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Across_Container(){
        Cfg_Across_Load load = new Cfg_Across_Load();
        Map<Integer, Cfg_Across_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Across_Bean[] newBeanArray(int size) {
        return new Cfg_Across_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Across_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Across_Container();
        }
        
        Cfg_Across_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Across_Container的实例对象
     *
     * @return
     */
    public static Cfg_Across_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
