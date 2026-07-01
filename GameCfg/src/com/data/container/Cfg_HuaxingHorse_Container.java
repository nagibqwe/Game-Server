/**
 * Auto generated, do not edit it
 *
 * HuaxingHorse配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_HuaxingHorse_Bean; 
import config.Cfg_HuaxingHorse_Load;

	
public final class Cfg_HuaxingHorse_Container extends ConfigBase<Cfg_HuaxingHorse_Bean> {
	
    /**
     * 初始化HuaxingHorse配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_HuaxingHorse_Container(){
        Cfg_HuaxingHorse_Load load = new Cfg_HuaxingHorse_Load();
        Map<Integer, Cfg_HuaxingHorse_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_HuaxingHorse_Bean[] newBeanArray(int size) {
        return new Cfg_HuaxingHorse_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_HuaxingHorse_Container manager;
        
        Singleton() {
            this.manager = new Cfg_HuaxingHorse_Container();
        }
        
        Cfg_HuaxingHorse_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_HuaxingHorse_Container的实例对象
     *
     * @return
     */
    public static Cfg_HuaxingHorse_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
