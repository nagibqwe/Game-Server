/**
 * Auto generated, do not edit it
 *
 * new_active_advantage配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_New_active_advantage_Bean; 
import config.Cfg_New_active_advantage_Load;

	
public final class Cfg_New_active_advantage_Container extends ConfigBase<Cfg_New_active_advantage_Bean> {
	
    /**
     * 初始化new_active_advantage配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_New_active_advantage_Container(){
        Cfg_New_active_advantage_Load load = new Cfg_New_active_advantage_Load();
        Map<Integer, Cfg_New_active_advantage_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_New_active_advantage_Bean[] newBeanArray(int size) {
        return new Cfg_New_active_advantage_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_New_active_advantage_Container manager;
        
        Singleton() {
            this.manager = new Cfg_New_active_advantage_Container();
        }
        
        Cfg_New_active_advantage_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_New_active_advantage_Container的实例对象
     *
     * @return
     */
    public static Cfg_New_active_advantage_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
