/**
 * Auto generated, do not edit it
 *
 * SZZQAward配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_SZZQAward_Bean; 
import config.Cfg_SZZQAward_Load;

	
public final class Cfg_SZZQAward_Container extends ConfigBase<Cfg_SZZQAward_Bean> {
	
    /**
     * 初始化SZZQAward配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_SZZQAward_Container(){
        Cfg_SZZQAward_Load load = new Cfg_SZZQAward_Load();
        Map<Integer, Cfg_SZZQAward_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_SZZQAward_Bean[] newBeanArray(int size) {
        return new Cfg_SZZQAward_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_SZZQAward_Container manager;
        
        Singleton() {
            this.manager = new Cfg_SZZQAward_Container();
        }
        
        Cfg_SZZQAward_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_SZZQAward_Container的实例对象
     *
     * @return
     */
    public static Cfg_SZZQAward_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
