/**
 * Auto generated, do not edit it
 *
 * daily_reward配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Daily_reward_Bean; 
import config.Cfg_Daily_reward_Load;

	
public final class Cfg_Daily_reward_Container extends ConfigBase<Cfg_Daily_reward_Bean> {
	
    /**
     * 初始化daily_reward配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Daily_reward_Container(){
        Cfg_Daily_reward_Load load = new Cfg_Daily_reward_Load();
        Map<Integer, Cfg_Daily_reward_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Daily_reward_Bean[] newBeanArray(int size) {
        return new Cfg_Daily_reward_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Daily_reward_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Daily_reward_Container();
        }
        
        Cfg_Daily_reward_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Daily_reward_Container的实例对象
     *
     * @return
     */
    public static Cfg_Daily_reward_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
