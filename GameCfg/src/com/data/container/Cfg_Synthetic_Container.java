/**
 * Auto generated, do not edit it
 *
 * synthetic配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Synthetic_Bean; 
import config.Cfg_Synthetic_Load;

	
public final class Cfg_Synthetic_Container extends ConfigBase<Cfg_Synthetic_Bean> {
	
    /**
     * 初始化synthetic配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Synthetic_Container(){
        Cfg_Synthetic_Load load = new Cfg_Synthetic_Load();
        Map<Integer, Cfg_Synthetic_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Synthetic_Bean[] newBeanArray(int size) {
        return new Cfg_Synthetic_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Synthetic_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Synthetic_Container();
        }
        
        Cfg_Synthetic_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Synthetic_Container的实例对象
     *
     * @return
     */
    public static Cfg_Synthetic_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
