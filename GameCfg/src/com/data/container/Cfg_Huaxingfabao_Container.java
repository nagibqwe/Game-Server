/**
 * Auto generated, do not edit it
 *
 * Huaxingfabao配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Huaxingfabao_Bean; 
import config.Cfg_Huaxingfabao_Load;

	
public final class Cfg_Huaxingfabao_Container extends ConfigBase<Cfg_Huaxingfabao_Bean> {
	
    /**
     * 初始化Huaxingfabao配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Huaxingfabao_Container(){
        Cfg_Huaxingfabao_Load load = new Cfg_Huaxingfabao_Load();
        Map<Integer, Cfg_Huaxingfabao_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Huaxingfabao_Bean[] newBeanArray(int size) {
        return new Cfg_Huaxingfabao_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Huaxingfabao_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Huaxingfabao_Container();
        }
        
        Cfg_Huaxingfabao_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Huaxingfabao_Container的实例对象
     *
     * @return
     */
    public static Cfg_Huaxingfabao_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
