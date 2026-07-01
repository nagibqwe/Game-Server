/**
 * Auto generated, do not edit it
 *
 * extra_rewards配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Extra_rewards_Bean; 
import config.Cfg_Extra_rewards_Load;

	
public final class Cfg_Extra_rewards_Container extends ConfigBase<Cfg_Extra_rewards_Bean> {
	
    /**
     * 初始化extra_rewards配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Extra_rewards_Container(){
        Cfg_Extra_rewards_Load load = new Cfg_Extra_rewards_Load();
        Map<Integer, Cfg_Extra_rewards_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Extra_rewards_Bean[] newBeanArray(int size) {
        return new Cfg_Extra_rewards_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Extra_rewards_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Extra_rewards_Container();
        }
        
        Cfg_Extra_rewards_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Extra_rewards_Container的实例对象
     *
     * @return
     */
    public static Cfg_Extra_rewards_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
