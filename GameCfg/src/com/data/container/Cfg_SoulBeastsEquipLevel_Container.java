/**
 * Auto generated, do not edit it
 *
 * SoulBeastsEquipLevel配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_SoulBeastsEquipLevel_Bean; 
import config.Cfg_SoulBeastsEquipLevel_Load;

	
public final class Cfg_SoulBeastsEquipLevel_Container extends ConfigBase<Cfg_SoulBeastsEquipLevel_Bean> {
	
    /**
     * 初始化SoulBeastsEquipLevel配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_SoulBeastsEquipLevel_Container(){
        Cfg_SoulBeastsEquipLevel_Load load = new Cfg_SoulBeastsEquipLevel_Load();
        Map<Integer, Cfg_SoulBeastsEquipLevel_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_SoulBeastsEquipLevel_Bean[] newBeanArray(int size) {
        return new Cfg_SoulBeastsEquipLevel_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_SoulBeastsEquipLevel_Container manager;
        
        Singleton() {
            this.manager = new Cfg_SoulBeastsEquipLevel_Container();
        }
        
        Cfg_SoulBeastsEquipLevel_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_SoulBeastsEquipLevel_Container的实例对象
     *
     * @return
     */
    public static Cfg_SoulBeastsEquipLevel_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
