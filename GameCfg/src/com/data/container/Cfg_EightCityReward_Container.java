/**
 * Auto generated, do not edit it
 *
 * EightCityReward配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_EightCityReward_Bean; 
import config.Cfg_EightCityReward_Load;

	
public final class Cfg_EightCityReward_Container extends ConfigBase<Cfg_EightCityReward_Bean> {
	
    /**
     * 初始化EightCityReward配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_EightCityReward_Container(){
        Cfg_EightCityReward_Load load = new Cfg_EightCityReward_Load();
        Map<Integer, Cfg_EightCityReward_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_EightCityReward_Bean[] newBeanArray(int size) {
        return new Cfg_EightCityReward_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_EightCityReward_Container manager;
        
        Singleton() {
            this.manager = new Cfg_EightCityReward_Container();
        }
        
        Cfg_EightCityReward_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_EightCityReward_Container的实例对象
     *
     * @return
     */
    public static Cfg_EightCityReward_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
