/**
 * Auto generated, do not edit it
 *
 * NatureWeapon配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_NatureWeapon_Bean; 
import config.Cfg_NatureWeapon_Load;

	
public final class Cfg_NatureWeapon_Container extends ConfigBase<Cfg_NatureWeapon_Bean> {
	
    /**
     * 初始化NatureWeapon配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_NatureWeapon_Container(){
        Cfg_NatureWeapon_Load load = new Cfg_NatureWeapon_Load();
        Map<Integer, Cfg_NatureWeapon_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_NatureWeapon_Bean[] newBeanArray(int size) {
        return new Cfg_NatureWeapon_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_NatureWeapon_Container manager;
        
        Singleton() {
            this.manager = new Cfg_NatureWeapon_Container();
        }
        
        Cfg_NatureWeapon_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_NatureWeapon_Container的实例对象
     *
     * @return
     */
    public static Cfg_NatureWeapon_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
