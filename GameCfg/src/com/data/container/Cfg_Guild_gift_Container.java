/**
 * Auto generated, do not edit it
 *
 * guild_gift配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Guild_gift_Bean; 
import config.Cfg_Guild_gift_Load;

	
public final class Cfg_Guild_gift_Container extends ConfigBase<Cfg_Guild_gift_Bean> {
	
    /**
     * 初始化guild_gift配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Guild_gift_Container(){
        Cfg_Guild_gift_Load load = new Cfg_Guild_gift_Load();
        Map<Integer, Cfg_Guild_gift_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Guild_gift_Bean[] newBeanArray(int size) {
        return new Cfg_Guild_gift_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Guild_gift_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Guild_gift_Container();
        }
        
        Cfg_Guild_gift_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Guild_gift_Container的实例对象
     *
     * @return
     */
    public static Cfg_Guild_gift_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
