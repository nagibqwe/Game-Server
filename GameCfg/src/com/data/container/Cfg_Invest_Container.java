/**
 * Auto generated, do not edit it
 *
 * invest配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Invest_Bean; 
import config.Cfg_Invest_Load;

	
public final class Cfg_Invest_Container extends ConfigBase<Cfg_Invest_Bean> {
	
    /**
     * 初始化invest配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Invest_Container(){
        Cfg_Invest_Load load = new Cfg_Invest_Load();
        Map<Integer, Cfg_Invest_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Invest_Bean[] newBeanArray(int size) {
        return new Cfg_Invest_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Invest_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Invest_Container();
        }
        
        Cfg_Invest_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Invest_Container的实例对象
     *
     * @return
     */
    public static Cfg_Invest_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
