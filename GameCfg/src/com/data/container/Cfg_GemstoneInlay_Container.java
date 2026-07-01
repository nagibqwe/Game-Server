/**
 * Auto generated, do not edit it
 *
 * GemstoneInlay配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_GemstoneInlay_Bean; 
import config.Cfg_GemstoneInlay_Load;

	
public final class Cfg_GemstoneInlay_Container extends ConfigBase<Cfg_GemstoneInlay_Bean> {
	
    /**
     * 初始化GemstoneInlay配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_GemstoneInlay_Container(){
        Cfg_GemstoneInlay_Load load = new Cfg_GemstoneInlay_Load();
        Map<Integer, Cfg_GemstoneInlay_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_GemstoneInlay_Bean[] newBeanArray(int size) {
        return new Cfg_GemstoneInlay_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_GemstoneInlay_Container manager;
        
        Singleton() {
            this.manager = new Cfg_GemstoneInlay_Container();
        }
        
        Cfg_GemstoneInlay_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_GemstoneInlay_Container的实例对象
     *
     * @return
     */
    public static Cfg_GemstoneInlay_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
