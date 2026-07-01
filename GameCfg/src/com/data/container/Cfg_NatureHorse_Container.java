/**
 * Auto generated, do not edit it
 *
 * NatureHorse配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_NatureHorse_Bean; 
import config.Cfg_NatureHorse_Load;

	
public final class Cfg_NatureHorse_Container extends ConfigBase<Cfg_NatureHorse_Bean> {
	
    /**
     * 初始化NatureHorse配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_NatureHorse_Container(){
        Cfg_NatureHorse_Load load = new Cfg_NatureHorse_Load();
        Map<Integer, Cfg_NatureHorse_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_NatureHorse_Bean[] newBeanArray(int size) {
        return new Cfg_NatureHorse_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_NatureHorse_Container manager;
        
        Singleton() {
            this.manager = new Cfg_NatureHorse_Container();
        }
        
        Cfg_NatureHorse_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_NatureHorse_Container的实例对象
     *
     * @return
     */
    public static Cfg_NatureHorse_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
