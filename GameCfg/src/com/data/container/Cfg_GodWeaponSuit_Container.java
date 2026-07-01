/**
 * Auto generated, do not edit it
 *
 * GodWeaponSuit配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_GodWeaponSuit_Bean; 
import config.Cfg_GodWeaponSuit_Load;

	
public final class Cfg_GodWeaponSuit_Container extends ConfigBase<Cfg_GodWeaponSuit_Bean> {
	
    /**
     * 初始化GodWeaponSuit配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_GodWeaponSuit_Container(){
        Cfg_GodWeaponSuit_Load load = new Cfg_GodWeaponSuit_Load();
        Map<Integer, Cfg_GodWeaponSuit_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_GodWeaponSuit_Bean[] newBeanArray(int size) {
        return new Cfg_GodWeaponSuit_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_GodWeaponSuit_Container manager;
        
        Singleton() {
            this.manager = new Cfg_GodWeaponSuit_Container();
        }
        
        Cfg_GodWeaponSuit_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_GodWeaponSuit_Container的实例对象
     *
     * @return
     */
    public static Cfg_GodWeaponSuit_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
