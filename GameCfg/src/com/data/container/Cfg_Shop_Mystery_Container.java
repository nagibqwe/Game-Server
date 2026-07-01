/**
 * Auto generated, do not edit it
 *
 * shop_Mystery配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Shop_Mystery_Bean; 
import config.Cfg_Shop_Mystery_Load;

	
public final class Cfg_Shop_Mystery_Container extends ConfigBase<Cfg_Shop_Mystery_Bean> {
	
    /**
     * 初始化shop_Mystery配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Shop_Mystery_Container(){
        Cfg_Shop_Mystery_Load load = new Cfg_Shop_Mystery_Load();
        Map<Integer, Cfg_Shop_Mystery_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Shop_Mystery_Bean[] newBeanArray(int size) {
        return new Cfg_Shop_Mystery_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Shop_Mystery_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Shop_Mystery_Container();
        }
        
        Cfg_Shop_Mystery_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Shop_Mystery_Container的实例对象
     *
     * @return
     */
    public static Cfg_Shop_Mystery_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
