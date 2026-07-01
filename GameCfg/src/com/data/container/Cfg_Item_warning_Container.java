/**
 * Auto generated, do not edit it
 *
 * item_warning配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Item_warning_Bean; 
import config.Cfg_Item_warning_Load;

	
public final class Cfg_Item_warning_Container extends ConfigBase<Cfg_Item_warning_Bean> {
	
    /**
     * 初始化item_warning配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Item_warning_Container(){
        Cfg_Item_warning_Load load = new Cfg_Item_warning_Load();
        Map<Integer, Cfg_Item_warning_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Item_warning_Bean[] newBeanArray(int size) {
        return new Cfg_Item_warning_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Item_warning_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Item_warning_Container();
        }
        
        Cfg_Item_warning_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Item_warning_Container的实例对象
     *
     * @return
     */
    public static Cfg_Item_warning_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
