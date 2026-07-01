/**
 * Auto generated, do not edit it
 *
 * VipRebate配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_VipRebate_Bean; 
import config.Cfg_VipRebate_Load;

	
public final class Cfg_VipRebate_Container extends ConfigBase<Cfg_VipRebate_Bean> {
	
    /**
     * 初始化VipRebate配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_VipRebate_Container(){
        Cfg_VipRebate_Load load = new Cfg_VipRebate_Load();
        Map<Integer, Cfg_VipRebate_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_VipRebate_Bean[] newBeanArray(int size) {
        return new Cfg_VipRebate_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_VipRebate_Container manager;
        
        Singleton() {
            this.manager = new Cfg_VipRebate_Container();
        }
        
        Cfg_VipRebate_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_VipRebate_Container的实例对象
     *
     * @return
     */
    public static Cfg_VipRebate_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
