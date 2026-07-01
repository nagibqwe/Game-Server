/**
 * Auto generated, do not edit it
 *
 * pet_equip_unlock配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Pet_equip_unlock_Bean; 
import config.Cfg_Pet_equip_unlock_Load;

	
public final class Cfg_Pet_equip_unlock_Container extends ConfigBase<Cfg_Pet_equip_unlock_Bean> {
	
    /**
     * 初始化pet_equip_unlock配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Pet_equip_unlock_Container(){
        Cfg_Pet_equip_unlock_Load load = new Cfg_Pet_equip_unlock_Load();
        Map<Integer, Cfg_Pet_equip_unlock_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Pet_equip_unlock_Bean[] newBeanArray(int size) {
        return new Cfg_Pet_equip_unlock_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Pet_equip_unlock_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Pet_equip_unlock_Container();
        }
        
        Cfg_Pet_equip_unlock_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Pet_equip_unlock_Container的实例对象
     *
     * @return
     */
    public static Cfg_Pet_equip_unlock_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
