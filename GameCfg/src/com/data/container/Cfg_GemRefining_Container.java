/**
 * Auto generated, do not edit it
 *
 * GemRefining配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_GemRefining_Bean; 
import config.Cfg_GemRefining_Load;

	
public final class Cfg_GemRefining_Container extends ConfigBase<Cfg_GemRefining_Bean> {
	
    /**
     * 初始化GemRefining配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_GemRefining_Container(){
        Cfg_GemRefining_Load load = new Cfg_GemRefining_Load();
        Map<Integer, Cfg_GemRefining_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_GemRefining_Bean[] newBeanArray(int size) {
        return new Cfg_GemRefining_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_GemRefining_Container manager;
        
        Singleton() {
            this.manager = new Cfg_GemRefining_Container();
        }
        
        Cfg_GemRefining_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_GemRefining_Container的实例对象
     *
     * @return
     */
    public static Cfg_GemRefining_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
