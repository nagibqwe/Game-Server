/**
 * Auto generated, do not edit it
 *
 * HuaxingWeapon配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_HuaxingWeapon_Bean; 
import config.Cfg_HuaxingWeapon_Load;

	
public final class Cfg_HuaxingWeapon_Container extends ConfigBase<Cfg_HuaxingWeapon_Bean> {
	
    /**
     * 初始化HuaxingWeapon配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_HuaxingWeapon_Container(){
        Cfg_HuaxingWeapon_Load load = new Cfg_HuaxingWeapon_Load();
        Map<Integer, Cfg_HuaxingWeapon_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_HuaxingWeapon_Bean[] newBeanArray(int size) {
        return new Cfg_HuaxingWeapon_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_HuaxingWeapon_Container manager;
        
        Singleton() {
            this.manager = new Cfg_HuaxingWeapon_Container();
        }
        
        Cfg_HuaxingWeapon_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_HuaxingWeapon_Container的实例对象
     *
     * @return
     */
    public static Cfg_HuaxingWeapon_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
