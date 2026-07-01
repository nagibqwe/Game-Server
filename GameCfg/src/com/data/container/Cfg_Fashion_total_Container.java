/**
 * Auto generated, do not edit it
 *
 * fashion_total配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Fashion_total_Bean; 
import config.Cfg_Fashion_total_Load;

	
public final class Cfg_Fashion_total_Container extends ConfigBase<Cfg_Fashion_total_Bean> {
	
    /**
     * 初始化fashion_total配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Fashion_total_Container(){
        Cfg_Fashion_total_Load load = new Cfg_Fashion_total_Load();
        Map<Integer, Cfg_Fashion_total_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Fashion_total_Bean[] newBeanArray(int size) {
        return new Cfg_Fashion_total_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Fashion_total_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Fashion_total_Container();
        }
        
        Cfg_Fashion_total_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Fashion_total_Container的实例对象
     *
     * @return
     */
    public static Cfg_Fashion_total_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
