/**
 * Auto generated, do not edit it
 *
 * recharge_daily_cangzhenge配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Recharge_daily_cangzhenge_Bean; 
import config.Cfg_Recharge_daily_cangzhenge_Load;

	
public final class Cfg_Recharge_daily_cangzhenge_Container extends ConfigBase<Cfg_Recharge_daily_cangzhenge_Bean> {
	
    /**
     * 初始化recharge_daily_cangzhenge配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Recharge_daily_cangzhenge_Container(){
        Cfg_Recharge_daily_cangzhenge_Load load = new Cfg_Recharge_daily_cangzhenge_Load();
        Map<Integer, Cfg_Recharge_daily_cangzhenge_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Recharge_daily_cangzhenge_Bean[] newBeanArray(int size) {
        return new Cfg_Recharge_daily_cangzhenge_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Recharge_daily_cangzhenge_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Recharge_daily_cangzhenge_Container();
        }
        
        Cfg_Recharge_daily_cangzhenge_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Recharge_daily_cangzhenge_Container的实例对象
     *
     * @return
     */
    public static Cfg_Recharge_daily_cangzhenge_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
