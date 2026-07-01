/**
 * Auto generated, do not edit it
 *
 * HuaxingMagic配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_HuaxingMagic_Bean; 
import config.Cfg_HuaxingMagic_Load;

	
public final class Cfg_HuaxingMagic_Container extends ConfigBase<Cfg_HuaxingMagic_Bean> {
	
    /**
     * 初始化HuaxingMagic配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_HuaxingMagic_Container(){
        Cfg_HuaxingMagic_Load load = new Cfg_HuaxingMagic_Load();
        Map<Integer, Cfg_HuaxingMagic_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_HuaxingMagic_Bean[] newBeanArray(int size) {
        return new Cfg_HuaxingMagic_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_HuaxingMagic_Container manager;
        
        Singleton() {
            this.manager = new Cfg_HuaxingMagic_Container();
        }
        
        Cfg_HuaxingMagic_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_HuaxingMagic_Container的实例对象
     *
     * @return
     */
    public static Cfg_HuaxingMagic_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
