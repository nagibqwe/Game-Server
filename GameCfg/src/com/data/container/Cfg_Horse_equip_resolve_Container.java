/**
 * Auto generated, do not edit it
 *
 * Horse_equip_resolve配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Horse_equip_resolve_Bean; 
import config.Cfg_Horse_equip_resolve_Load;

	
public final class Cfg_Horse_equip_resolve_Container extends ConfigBase<Cfg_Horse_equip_resolve_Bean> {
	
    /**
     * 初始化Horse_equip_resolve配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Horse_equip_resolve_Container(){
        Cfg_Horse_equip_resolve_Load load = new Cfg_Horse_equip_resolve_Load();
        Map<Integer, Cfg_Horse_equip_resolve_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Horse_equip_resolve_Bean[] newBeanArray(int size) {
        return new Cfg_Horse_equip_resolve_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Horse_equip_resolve_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Horse_equip_resolve_Container();
        }
        
        Cfg_Horse_equip_resolve_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Horse_equip_resolve_Container的实例对象
     *
     * @return
     */
    public static Cfg_Horse_equip_resolve_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
