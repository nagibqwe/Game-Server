/**
 * Auto generated, do not edit it
 *
 * PlayerOccupation配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_PlayerOccupation_Bean; 
import config.Cfg_PlayerOccupation_Load;

	
public final class Cfg_PlayerOccupation_Container extends ConfigBase<Cfg_PlayerOccupation_Bean> {
	
    /**
     * 初始化PlayerOccupation配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_PlayerOccupation_Container(){
        Cfg_PlayerOccupation_Load load = new Cfg_PlayerOccupation_Load();
        Map<Integer, Cfg_PlayerOccupation_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_PlayerOccupation_Bean[] newBeanArray(int size) {
        return new Cfg_PlayerOccupation_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_PlayerOccupation_Container manager;
        
        Singleton() {
            this.manager = new Cfg_PlayerOccupation_Container();
        }
        
        Cfg_PlayerOccupation_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_PlayerOccupation_Container的实例对象
     *
     * @return
     */
    public static Cfg_PlayerOccupation_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
