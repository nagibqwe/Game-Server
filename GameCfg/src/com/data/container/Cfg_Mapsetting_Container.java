/**
 * Auto generated, do not edit it
 *
 * Mapsetting配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Mapsetting_Bean; 
import config.Cfg_Mapsetting_Load;

	
public final class Cfg_Mapsetting_Container extends ConfigBase<Cfg_Mapsetting_Bean> {
	
    /**
     * 初始化Mapsetting配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Mapsetting_Container(){
        Cfg_Mapsetting_Load load = new Cfg_Mapsetting_Load();
        Map<Integer, Cfg_Mapsetting_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Mapsetting_Bean[] newBeanArray(int size) {
        return new Cfg_Mapsetting_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Mapsetting_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Mapsetting_Container();
        }
        
        Cfg_Mapsetting_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Mapsetting_Container的实例对象
     *
     * @return
     */
    public static Cfg_Mapsetting_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
