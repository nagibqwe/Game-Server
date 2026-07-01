/**
 * Auto generated, do not edit it
 *
 * Gather配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Gather_Bean; 
import config.Cfg_Gather_Load;

	
public final class Cfg_Gather_Container extends ConfigBase<Cfg_Gather_Bean> {
	
    /**
     * 初始化Gather配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Gather_Container(){
        Cfg_Gather_Load load = new Cfg_Gather_Load();
        Map<Integer, Cfg_Gather_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Gather_Bean[] newBeanArray(int size) {
        return new Cfg_Gather_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Gather_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Gather_Container();
        }
        
        Cfg_Gather_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Gather_Container的实例对象
     *
     * @return
     */
    public static Cfg_Gather_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
