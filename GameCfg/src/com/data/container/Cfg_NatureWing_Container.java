/**
 * Auto generated, do not edit it
 *
 * NatureWing配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_NatureWing_Bean; 
import config.Cfg_NatureWing_Load;

	
public final class Cfg_NatureWing_Container extends ConfigBase<Cfg_NatureWing_Bean> {
	
    /**
     * 初始化NatureWing配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_NatureWing_Container(){
        Cfg_NatureWing_Load load = new Cfg_NatureWing_Load();
        Map<Integer, Cfg_NatureWing_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_NatureWing_Bean[] newBeanArray(int size) {
        return new Cfg_NatureWing_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_NatureWing_Container manager;
        
        Singleton() {
            this.manager = new Cfg_NatureWing_Container();
        }
        
        Cfg_NatureWing_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_NatureWing_Container的实例对象
     *
     * @return
     */
    public static Cfg_NatureWing_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
