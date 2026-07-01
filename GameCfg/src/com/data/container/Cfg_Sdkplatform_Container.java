/**
 * Auto generated, do not edit it
 *
 * sdkplatform配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Sdkplatform_Bean; 
import config.Cfg_Sdkplatform_Load;

	
public final class Cfg_Sdkplatform_Container extends ConfigBase<Cfg_Sdkplatform_Bean> {
	
    /**
     * 初始化sdkplatform配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Sdkplatform_Container(){
        Cfg_Sdkplatform_Load load = new Cfg_Sdkplatform_Load();
        Map<Integer, Cfg_Sdkplatform_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Sdkplatform_Bean[] newBeanArray(int size) {
        return new Cfg_Sdkplatform_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Sdkplatform_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Sdkplatform_Container();
        }
        
        Cfg_Sdkplatform_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Sdkplatform_Container的实例对象
     *
     * @return
     */
    public static Cfg_Sdkplatform_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
