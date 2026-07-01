/**
 * Auto generated, do not edit it
 *
 * bag_grid配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Bag_grid_Bean; 
import config.Cfg_Bag_grid_Load;

	
public final class Cfg_Bag_grid_Container extends ConfigBase<Cfg_Bag_grid_Bean> {
	
    /**
     * 初始化bag_grid配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Bag_grid_Container(){
        Cfg_Bag_grid_Load load = new Cfg_Bag_grid_Load();
        Map<Integer, Cfg_Bag_grid_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Bag_grid_Bean[] newBeanArray(int size) {
        return new Cfg_Bag_grid_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Bag_grid_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Bag_grid_Container();
        }
        
        Cfg_Bag_grid_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Bag_grid_Container的实例对象
     *
     * @return
     */
    public static Cfg_Bag_grid_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
