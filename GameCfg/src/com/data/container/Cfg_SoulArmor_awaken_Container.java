/**
 * Auto generated, do not edit it
 *
 * SoulArmor_awaken配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_SoulArmor_awaken_Bean; 
import config.Cfg_SoulArmor_awaken_Load;

	
public final class Cfg_SoulArmor_awaken_Container extends ConfigBase<Cfg_SoulArmor_awaken_Bean> {
	
    /**
     * 初始化SoulArmor_awaken配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_SoulArmor_awaken_Container(){
        Cfg_SoulArmor_awaken_Load load = new Cfg_SoulArmor_awaken_Load();
        Map<Integer, Cfg_SoulArmor_awaken_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_SoulArmor_awaken_Bean[] newBeanArray(int size) {
        return new Cfg_SoulArmor_awaken_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_SoulArmor_awaken_Container manager;
        
        Singleton() {
            this.manager = new Cfg_SoulArmor_awaken_Container();
        }
        
        Cfg_SoulArmor_awaken_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_SoulArmor_awaken_Container的实例对象
     *
     * @return
     */
    public static Cfg_SoulArmor_awaken_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
