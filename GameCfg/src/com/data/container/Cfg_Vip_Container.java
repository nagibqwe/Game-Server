/**
 * Auto generated, do not edit it
 *
 * vip配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Vip_Bean; 
import config.Cfg_Vip_Load;

	
public final class Cfg_Vip_Container extends ConfigBase<Cfg_Vip_Bean> {
	
    /**
     * 初始化vip配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Vip_Container(){
        Cfg_Vip_Load load = new Cfg_Vip_Load();
        Map<Integer, Cfg_Vip_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Vip_Bean[] newBeanArray(int size) {
        return new Cfg_Vip_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Vip_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Vip_Container();
        }
        
        Cfg_Vip_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Vip_Container的实例对象
     *
     * @return
     */
    public static Cfg_Vip_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
