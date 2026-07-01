/**
 * Auto generated, do not edit it
 *
 * MysBuShop配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_MysBuShop_Bean; 
import config.Cfg_MysBuShop_Load;

	
public final class Cfg_MysBuShop_Container extends ConfigBase<Cfg_MysBuShop_Bean> {
	
    /**
     * 初始化MysBuShop配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_MysBuShop_Container(){
        Cfg_MysBuShop_Load load = new Cfg_MysBuShop_Load();
        Map<Integer, Cfg_MysBuShop_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_MysBuShop_Bean[] newBeanArray(int size) {
        return new Cfg_MysBuShop_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_MysBuShop_Container manager;
        
        Singleton() {
            this.manager = new Cfg_MysBuShop_Container();
        }
        
        Cfg_MysBuShop_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_MysBuShop_Container的实例对象
     *
     * @return
     */
    public static Cfg_MysBuShop_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
