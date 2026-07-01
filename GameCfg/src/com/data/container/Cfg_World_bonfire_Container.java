/**
 * Auto generated, do not edit it
 *
 * world_bonfire配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_World_bonfire_Bean; 
import config.Cfg_World_bonfire_Load;

	
public final class Cfg_World_bonfire_Container extends ConfigBase<Cfg_World_bonfire_Bean> {
	
    /**
     * 初始化world_bonfire配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_World_bonfire_Container(){
        Cfg_World_bonfire_Load load = new Cfg_World_bonfire_Load();
        Map<Integer, Cfg_World_bonfire_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_World_bonfire_Bean[] newBeanArray(int size) {
        return new Cfg_World_bonfire_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_World_bonfire_Container manager;
        
        Singleton() {
            this.manager = new Cfg_World_bonfire_Container();
        }
        
        Cfg_World_bonfire_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_World_bonfire_Container的实例对象
     *
     * @return
     */
    public static Cfg_World_bonfire_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
