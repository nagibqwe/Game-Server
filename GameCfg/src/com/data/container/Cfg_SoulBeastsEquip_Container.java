/**
 * Auto generated, do not edit it
 *
 * SoulBeastsEquip配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_SoulBeastsEquip_Bean; 
import config.Cfg_SoulBeastsEquip_Load;

	
public final class Cfg_SoulBeastsEquip_Container extends ConfigBase<Cfg_SoulBeastsEquip_Bean> {
	
    /**
     * 初始化SoulBeastsEquip配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_SoulBeastsEquip_Container(){
        Cfg_SoulBeastsEquip_Load load = new Cfg_SoulBeastsEquip_Load();
        Map<Integer, Cfg_SoulBeastsEquip_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_SoulBeastsEquip_Bean[] newBeanArray(int size) {
        return new Cfg_SoulBeastsEquip_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_SoulBeastsEquip_Container manager;
        
        Singleton() {
            this.manager = new Cfg_SoulBeastsEquip_Container();
        }
        
        Cfg_SoulBeastsEquip_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_SoulBeastsEquip_Container的实例对象
     *
     * @return
     */
    public static Cfg_SoulBeastsEquip_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
