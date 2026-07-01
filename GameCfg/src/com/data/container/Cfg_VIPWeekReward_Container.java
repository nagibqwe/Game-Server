/**
 * Auto generated, do not edit it
 *
 * VIPWeekReward配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_VIPWeekReward_Bean; 
import config.Cfg_VIPWeekReward_Load;

	
public final class Cfg_VIPWeekReward_Container extends ConfigBase<Cfg_VIPWeekReward_Bean> {
	
    /**
     * 初始化VIPWeekReward配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_VIPWeekReward_Container(){
        Cfg_VIPWeekReward_Load load = new Cfg_VIPWeekReward_Load();
        Map<Integer, Cfg_VIPWeekReward_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_VIPWeekReward_Bean[] newBeanArray(int size) {
        return new Cfg_VIPWeekReward_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_VIPWeekReward_Container manager;
        
        Singleton() {
            this.manager = new Cfg_VIPWeekReward_Container();
        }
        
        Cfg_VIPWeekReward_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_VIPWeekReward_Container的实例对象
     *
     * @return
     */
    public static Cfg_VIPWeekReward_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
