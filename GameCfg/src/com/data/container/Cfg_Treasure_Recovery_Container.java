/**
 * Auto generated, do not edit it
 *
 * Treasure_Recovery配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Treasure_Recovery_Bean; 
import config.Cfg_Treasure_Recovery_Load;

	
public final class Cfg_Treasure_Recovery_Container extends ConfigBase<Cfg_Treasure_Recovery_Bean> {
	
    /**
     * 初始化Treasure_Recovery配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Treasure_Recovery_Container(){
        Cfg_Treasure_Recovery_Load load = new Cfg_Treasure_Recovery_Load();
        Map<Integer, Cfg_Treasure_Recovery_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Treasure_Recovery_Bean[] newBeanArray(int size) {
        return new Cfg_Treasure_Recovery_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Treasure_Recovery_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Treasure_Recovery_Container();
        }
        
        Cfg_Treasure_Recovery_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Treasure_Recovery_Container的实例对象
     *
     * @return
     */
    public static Cfg_Treasure_Recovery_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
