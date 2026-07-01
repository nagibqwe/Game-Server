/**
 * Auto generated, do not edit it
 *
 * today_function_recharge配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Today_function_recharge_Bean; 
import config.Cfg_Today_function_recharge_Load;

	
public final class Cfg_Today_function_recharge_Container extends ConfigBase<Cfg_Today_function_recharge_Bean> {
	
    /**
     * 初始化today_function_recharge配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Today_function_recharge_Container(){
        Cfg_Today_function_recharge_Load load = new Cfg_Today_function_recharge_Load();
        Map<Integer, Cfg_Today_function_recharge_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Today_function_recharge_Bean[] newBeanArray(int size) {
        return new Cfg_Today_function_recharge_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Today_function_recharge_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Today_function_recharge_Container();
        }
        
        Cfg_Today_function_recharge_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Today_function_recharge_Container的实例对象
     *
     * @return
     */
    public static Cfg_Today_function_recharge_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
