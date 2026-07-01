/**
 * Auto generated, do not edit it
 *
 * GMitem配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_GMitem_Bean; 
import config.Cfg_GMitem_Load;

	
public final class Cfg_GMitem_Container extends ConfigBase<Cfg_GMitem_Bean> {
	
    /**
     * 初始化GMitem配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_GMitem_Container(){
        Cfg_GMitem_Load load = new Cfg_GMitem_Load();
        Map<Integer, Cfg_GMitem_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_GMitem_Bean[] newBeanArray(int size) {
        return new Cfg_GMitem_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_GMitem_Container manager;
        
        Singleton() {
            this.manager = new Cfg_GMitem_Container();
        }
        
        Cfg_GMitem_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_GMitem_Container的实例对象
     *
     * @return
     */
    public static Cfg_GMitem_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
