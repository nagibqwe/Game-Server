/**
 * Auto generated, do not edit it
 *
 * PrayCost配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_PrayCost_Bean; 
import config.Cfg_PrayCost_Load;

	
public final class Cfg_PrayCost_Container extends ConfigBase<Cfg_PrayCost_Bean> {
	
    /**
     * 初始化PrayCost配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_PrayCost_Container(){
        Cfg_PrayCost_Load load = new Cfg_PrayCost_Load();
        Map<Integer, Cfg_PrayCost_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_PrayCost_Bean[] newBeanArray(int size) {
        return new Cfg_PrayCost_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_PrayCost_Container manager;
        
        Singleton() {
            this.manager = new Cfg_PrayCost_Container();
        }
        
        Cfg_PrayCost_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_PrayCost_Container的实例对象
     *
     * @return
     */
    public static Cfg_PrayCost_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
