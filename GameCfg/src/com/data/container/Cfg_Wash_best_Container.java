/**
 * Auto generated, do not edit it
 *
 * wash_best配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Wash_best_Bean; 
import config.Cfg_Wash_best_Load;

	
public final class Cfg_Wash_best_Container extends ConfigBase<Cfg_Wash_best_Bean> {
	
    /**
     * 初始化wash_best配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Wash_best_Container(){
        Cfg_Wash_best_Load load = new Cfg_Wash_best_Load();
        Map<Integer, Cfg_Wash_best_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Wash_best_Bean[] newBeanArray(int size) {
        return new Cfg_Wash_best_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Wash_best_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Wash_best_Container();
        }
        
        Cfg_Wash_best_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Wash_best_Container的实例对象
     *
     * @return
     */
    public static Cfg_Wash_best_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
