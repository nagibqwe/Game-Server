/**
 * Auto generated, do not edit it
 *
 * RechargeAward配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_RechargeAward_Bean; 
import config.Cfg_RechargeAward_Load;

	
public final class Cfg_RechargeAward_Container extends ConfigBase<Cfg_RechargeAward_Bean> {
	
    /**
     * 初始化RechargeAward配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_RechargeAward_Container(){
        Cfg_RechargeAward_Load load = new Cfg_RechargeAward_Load();
        Map<Integer, Cfg_RechargeAward_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_RechargeAward_Bean[] newBeanArray(int size) {
        return new Cfg_RechargeAward_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_RechargeAward_Container manager;
        
        Singleton() {
            this.manager = new Cfg_RechargeAward_Container();
        }
        
        Cfg_RechargeAward_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_RechargeAward_Container的实例对象
     *
     * @return
     */
    public static Cfg_RechargeAward_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
