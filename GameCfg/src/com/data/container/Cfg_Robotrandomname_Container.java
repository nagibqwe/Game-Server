/**
 * Auto generated, do not edit it
 *
 * robotrandomname配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Robotrandomname_Bean; 
import config.Cfg_Robotrandomname_Load;

	
public final class Cfg_Robotrandomname_Container extends ConfigBase<Cfg_Robotrandomname_Bean> {
	
    /**
     * 初始化robotrandomname配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Robotrandomname_Container(){
        Cfg_Robotrandomname_Load load = new Cfg_Robotrandomname_Load();
        Map<Integer, Cfg_Robotrandomname_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Robotrandomname_Bean[] newBeanArray(int size) {
        return new Cfg_Robotrandomname_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Robotrandomname_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Robotrandomname_Container();
        }
        
        Cfg_Robotrandomname_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Robotrandomname_Container的实例对象
     *
     * @return
     */
    public static Cfg_Robotrandomname_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
