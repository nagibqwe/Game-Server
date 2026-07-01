/**
 * Auto generated, do not edit it
 *
 * HuaxingWing配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_HuaxingWing_Bean; 
import config.Cfg_HuaxingWing_Load;

	
public final class Cfg_HuaxingWing_Container extends ConfigBase<Cfg_HuaxingWing_Bean> {
	
    /**
     * 初始化HuaxingWing配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_HuaxingWing_Container(){
        Cfg_HuaxingWing_Load load = new Cfg_HuaxingWing_Load();
        Map<Integer, Cfg_HuaxingWing_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_HuaxingWing_Bean[] newBeanArray(int size) {
        return new Cfg_HuaxingWing_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_HuaxingWing_Container manager;
        
        Singleton() {
            this.manager = new Cfg_HuaxingWing_Container();
        }
        
        Cfg_HuaxingWing_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_HuaxingWing_Container的实例对象
     *
     * @return
     */
    public static Cfg_HuaxingWing_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
