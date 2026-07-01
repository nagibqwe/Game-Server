/**
 * Auto generated, do not edit it
 *
 * state_stifle配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_State_stifle_Bean; 
import config.Cfg_State_stifle_Load;

	
public final class Cfg_State_stifle_Container extends ConfigBase<Cfg_State_stifle_Bean> {
	
    /**
     * 初始化state_stifle配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_State_stifle_Container(){
        Cfg_State_stifle_Load load = new Cfg_State_stifle_Load();
        Map<Integer, Cfg_State_stifle_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_State_stifle_Bean[] newBeanArray(int size) {
        return new Cfg_State_stifle_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_State_stifle_Container manager;
        
        Singleton() {
            this.manager = new Cfg_State_stifle_Container();
        }
        
        Cfg_State_stifle_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_State_stifle_Container的实例对象
     *
     * @return
     */
    public static Cfg_State_stifle_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
