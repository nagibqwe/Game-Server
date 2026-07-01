/**
 * Auto generated, do not edit it
 *
 * relive配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Relive_Bean; 
import config.Cfg_Relive_Load;

	
public final class Cfg_Relive_Container extends ConfigBase<Cfg_Relive_Bean> {
	
    /**
     * 初始化relive配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Relive_Container(){
        Cfg_Relive_Load load = new Cfg_Relive_Load();
        Map<Integer, Cfg_Relive_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Relive_Bean[] newBeanArray(int size) {
        return new Cfg_Relive_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Relive_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Relive_Container();
        }
        
        Cfg_Relive_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Relive_Container的实例对象
     *
     * @return
     */
    public static Cfg_Relive_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
