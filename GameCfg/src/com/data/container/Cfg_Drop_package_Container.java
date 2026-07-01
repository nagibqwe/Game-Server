/**
 * Auto generated, do not edit it
 *
 * drop_package配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Drop_package_Bean; 
import config.Cfg_Drop_package_Load;

	
public final class Cfg_Drop_package_Container extends ConfigBase<Cfg_Drop_package_Bean> {
	
    /**
     * 初始化drop_package配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Drop_package_Container(){
        Cfg_Drop_package_Load load = new Cfg_Drop_package_Load();
        Map<Integer, Cfg_Drop_package_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Drop_package_Bean[] newBeanArray(int size) {
        return new Cfg_Drop_package_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Drop_package_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Drop_package_Container();
        }
        
        Cfg_Drop_package_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Drop_package_Container的实例对象
     *
     * @return
     */
    public static Cfg_Drop_package_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
