/**
 * Auto generated, do not edit it
 *
 * pet_equip_inten配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Pet_equip_inten_Bean; 
import config.Cfg_Pet_equip_inten_Load;

	
public final class Cfg_Pet_equip_inten_Container extends ConfigBase<Cfg_Pet_equip_inten_Bean> {
	
    /**
     * 初始化pet_equip_inten配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Pet_equip_inten_Container(){
        Cfg_Pet_equip_inten_Load load = new Cfg_Pet_equip_inten_Load();
        Map<Integer, Cfg_Pet_equip_inten_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Pet_equip_inten_Bean[] newBeanArray(int size) {
        return new Cfg_Pet_equip_inten_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Pet_equip_inten_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Pet_equip_inten_Container();
        }
        
        Cfg_Pet_equip_inten_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Pet_equip_inten_Container的实例对象
     *
     * @return
     */
    public static Cfg_Pet_equip_inten_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
