/**
 * Auto generated, do not edit it
 *
 * JJCAeward配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_JJCAeward_Bean; 
import config.Cfg_JJCAeward_Load;

	
public final class Cfg_JJCAeward_Container extends ConfigBase<Cfg_JJCAeward_Bean> {
	
    /**
     * 初始化JJCAeward配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_JJCAeward_Container(){
        Cfg_JJCAeward_Load load = new Cfg_JJCAeward_Load();
        Map<Integer, Cfg_JJCAeward_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_JJCAeward_Bean[] newBeanArray(int size) {
        return new Cfg_JJCAeward_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_JJCAeward_Container manager;
        
        Singleton() {
            this.manager = new Cfg_JJCAeward_Container();
        }
        
        Cfg_JJCAeward_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_JJCAeward_Container的实例对象
     *
     * @return
     */
    public static Cfg_JJCAeward_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
