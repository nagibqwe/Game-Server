/**
 * Auto generated, do not edit it
 *
 * HuaxingTalisman配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_HuaxingTalisman_Bean; 
import config.Cfg_HuaxingTalisman_Load;

	
public final class Cfg_HuaxingTalisman_Container extends ConfigBase<Cfg_HuaxingTalisman_Bean> {
	
    /**
     * 初始化HuaxingTalisman配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_HuaxingTalisman_Container(){
        Cfg_HuaxingTalisman_Load load = new Cfg_HuaxingTalisman_Load();
        Map<Integer, Cfg_HuaxingTalisman_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_HuaxingTalisman_Bean[] newBeanArray(int size) {
        return new Cfg_HuaxingTalisman_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_HuaxingTalisman_Container manager;
        
        Singleton() {
            this.manager = new Cfg_HuaxingTalisman_Container();
        }
        
        Cfg_HuaxingTalisman_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_HuaxingTalisman_Container的实例对象
     *
     * @return
     */
    public static Cfg_HuaxingTalisman_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
