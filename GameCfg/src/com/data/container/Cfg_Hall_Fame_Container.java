/**
 * Auto generated, do not edit it
 *
 * Hall_Fame配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Hall_Fame_Bean; 
import config.Cfg_Hall_Fame_Load;

	
public final class Cfg_Hall_Fame_Container extends ConfigBase<Cfg_Hall_Fame_Bean> {
	
    /**
     * 初始化Hall_Fame配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Hall_Fame_Container(){
        Cfg_Hall_Fame_Load load = new Cfg_Hall_Fame_Load();
        Map<Integer, Cfg_Hall_Fame_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Hall_Fame_Bean[] newBeanArray(int size) {
        return new Cfg_Hall_Fame_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Hall_Fame_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Hall_Fame_Container();
        }
        
        Cfg_Hall_Fame_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Hall_Fame_Container的实例对象
     *
     * @return
     */
    public static Cfg_Hall_Fame_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
