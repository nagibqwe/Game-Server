/**
 * Auto generated, do not edit it
 *
 * Treasure_Hunt配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Treasure_Hunt_Bean; 
import config.Cfg_Treasure_Hunt_Load;

	
public final class Cfg_Treasure_Hunt_Container extends ConfigBase<Cfg_Treasure_Hunt_Bean> {
	
    /**
     * 初始化Treasure_Hunt配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Treasure_Hunt_Container(){
        Cfg_Treasure_Hunt_Load load = new Cfg_Treasure_Hunt_Load();
        Map<Integer, Cfg_Treasure_Hunt_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Treasure_Hunt_Bean[] newBeanArray(int size) {
        return new Cfg_Treasure_Hunt_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Treasure_Hunt_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Treasure_Hunt_Container();
        }
        
        Cfg_Treasure_Hunt_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Treasure_Hunt_Container的实例对象
     *
     * @return
     */
    public static Cfg_Treasure_Hunt_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
