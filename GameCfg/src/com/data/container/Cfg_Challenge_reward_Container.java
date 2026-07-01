/**
 * Auto generated, do not edit it
 *
 * challenge_reward配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Challenge_reward_Bean; 
import config.Cfg_Challenge_reward_Load;

	
public final class Cfg_Challenge_reward_Container extends ConfigBase<Cfg_Challenge_reward_Bean> {
	
    /**
     * 初始化challenge_reward配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Challenge_reward_Container(){
        Cfg_Challenge_reward_Load load = new Cfg_Challenge_reward_Load();
        Map<Integer, Cfg_Challenge_reward_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Challenge_reward_Bean[] newBeanArray(int size) {
        return new Cfg_Challenge_reward_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Challenge_reward_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Challenge_reward_Container();
        }
        
        Cfg_Challenge_reward_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Challenge_reward_Container的实例对象
     *
     * @return
     */
    public static Cfg_Challenge_reward_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
