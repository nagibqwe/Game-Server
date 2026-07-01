/**
 * Auto generated, do not edit it
 *
 * StarAttribute配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_StarAttribute_Bean; 
import config.Cfg_StarAttribute_Load;

	
public final class Cfg_StarAttribute_Container extends ConfigBase<Cfg_StarAttribute_Bean> {
	
    /**
     * 初始化StarAttribute配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_StarAttribute_Container(){
        Cfg_StarAttribute_Load load = new Cfg_StarAttribute_Load();
        Map<Integer, Cfg_StarAttribute_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_StarAttribute_Bean[] newBeanArray(int size) {
        return new Cfg_StarAttribute_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_StarAttribute_Container manager;
        
        Singleton() {
            this.manager = new Cfg_StarAttribute_Container();
        }
        
        Cfg_StarAttribute_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_StarAttribute_Container的实例对象
     *
     * @return
     */
    public static Cfg_StarAttribute_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
