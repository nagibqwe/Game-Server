/**
 * Auto generated, do not edit it
 *
 * Equip_Collection_start配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Equip_Collection_start_Bean; 
import config.Cfg_Equip_Collection_start_Load;

	
public final class Cfg_Equip_Collection_start_Container extends ConfigBase<Cfg_Equip_Collection_start_Bean> {
	
    /**
     * 初始化Equip_Collection_start配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Equip_Collection_start_Container(){
        Cfg_Equip_Collection_start_Load load = new Cfg_Equip_Collection_start_Load();
        Map<Integer, Cfg_Equip_Collection_start_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Equip_Collection_start_Bean[] newBeanArray(int size) {
        return new Cfg_Equip_Collection_start_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Equip_Collection_start_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Equip_Collection_start_Container();
        }
        
        Cfg_Equip_Collection_start_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Equip_Collection_start_Container的实例对象
     *
     * @return
     */
    public static Cfg_Equip_Collection_start_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
