/**
 * Auto generated, do not edit it
 *
 * ConvoyGirl配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_ConvoyGirl_Bean; 
import config.Cfg_ConvoyGirl_Load;

	
public final class Cfg_ConvoyGirl_Container extends ConfigBase<Cfg_ConvoyGirl_Bean> {
	
    /**
     * 初始化ConvoyGirl配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_ConvoyGirl_Container(){
        Cfg_ConvoyGirl_Load load = new Cfg_ConvoyGirl_Load();
        Map<Integer, Cfg_ConvoyGirl_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_ConvoyGirl_Bean[] newBeanArray(int size) {
        return new Cfg_ConvoyGirl_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_ConvoyGirl_Container manager;
        
        Singleton() {
            this.manager = new Cfg_ConvoyGirl_Container();
        }
        
        Cfg_ConvoyGirl_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_ConvoyGirl_Container的实例对象
     *
     * @return
     */
    public static Cfg_ConvoyGirl_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
