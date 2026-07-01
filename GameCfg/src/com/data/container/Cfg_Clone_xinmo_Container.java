/**
 * Auto generated, do not edit it
 *
 * clone_xinmo配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Clone_xinmo_Bean; 
import config.Cfg_Clone_xinmo_Load;

	
public final class Cfg_Clone_xinmo_Container extends ConfigBase<Cfg_Clone_xinmo_Bean> {
	
    /**
     * 初始化clone_xinmo配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Clone_xinmo_Container(){
        Cfg_Clone_xinmo_Load load = new Cfg_Clone_xinmo_Load();
        Map<Integer, Cfg_Clone_xinmo_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Clone_xinmo_Bean[] newBeanArray(int size) {
        return new Cfg_Clone_xinmo_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Clone_xinmo_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Clone_xinmo_Container();
        }
        
        Cfg_Clone_xinmo_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Clone_xinmo_Container的实例对象
     *
     * @return
     */
    public static Cfg_Clone_xinmo_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
