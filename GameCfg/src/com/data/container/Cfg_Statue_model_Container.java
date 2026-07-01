/**
 * Auto generated, do not edit it
 *
 * statue_model配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Statue_model_Bean; 
import config.Cfg_Statue_model_Load;

	
public final class Cfg_Statue_model_Container extends ConfigBase<Cfg_Statue_model_Bean> {
	
    /**
     * 初始化statue_model配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Statue_model_Container(){
        Cfg_Statue_model_Load load = new Cfg_Statue_model_Load();
        Map<Integer, Cfg_Statue_model_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Statue_model_Bean[] newBeanArray(int size) {
        return new Cfg_Statue_model_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Statue_model_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Statue_model_Container();
        }
        
        Cfg_Statue_model_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Statue_model_Container的实例对象
     *
     * @return
     */
    public static Cfg_Statue_model_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
