/**
 * Auto generated, do not edit it
 *
 * EightCity配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_EightCity_Bean; 
import config.Cfg_EightCity_Load;

	
public final class Cfg_EightCity_Container extends ConfigBase<Cfg_EightCity_Bean> {
	
    /**
     * 初始化EightCity配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_EightCity_Container(){
        Cfg_EightCity_Load load = new Cfg_EightCity_Load();
        Map<Integer, Cfg_EightCity_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_EightCity_Bean[] newBeanArray(int size) {
        return new Cfg_EightCity_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_EightCity_Container manager;
        
        Singleton() {
            this.manager = new Cfg_EightCity_Container();
        }
        
        Cfg_EightCity_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_EightCity_Container的实例对象
     *
     * @return
     */
    public static Cfg_EightCity_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
