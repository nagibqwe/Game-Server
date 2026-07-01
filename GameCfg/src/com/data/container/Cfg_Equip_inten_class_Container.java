/**
 * Auto generated, do not edit it
 *
 * equip_inten_class配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Equip_inten_class_Bean; 
import config.Cfg_Equip_inten_class_Load;

	
public final class Cfg_Equip_inten_class_Container extends ConfigBase<Cfg_Equip_inten_class_Bean> {
	
    /**
     * 初始化equip_inten_class配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Equip_inten_class_Container(){
        Cfg_Equip_inten_class_Load load = new Cfg_Equip_inten_class_Load();
        Map<Integer, Cfg_Equip_inten_class_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Equip_inten_class_Bean[] newBeanArray(int size) {
        return new Cfg_Equip_inten_class_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Equip_inten_class_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Equip_inten_class_Container();
        }
        
        Cfg_Equip_inten_class_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Equip_inten_class_Container的实例对象
     *
     * @return
     */
    public static Cfg_Equip_inten_class_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
