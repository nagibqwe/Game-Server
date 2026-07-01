/**
 * Auto generated, do not edit it
 *
 * Leader_Preach_value配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Leader_Preach_value_Bean; 
import config.Cfg_Leader_Preach_value_Load;

	
public final class Cfg_Leader_Preach_value_Container extends ConfigBase<Cfg_Leader_Preach_value_Bean> {
	
    /**
     * 初始化Leader_Preach_value配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Leader_Preach_value_Container(){
        Cfg_Leader_Preach_value_Load load = new Cfg_Leader_Preach_value_Load();
        Map<Integer, Cfg_Leader_Preach_value_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Leader_Preach_value_Bean[] newBeanArray(int size) {
        return new Cfg_Leader_Preach_value_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Leader_Preach_value_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Leader_Preach_value_Container();
        }
        
        Cfg_Leader_Preach_value_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Leader_Preach_value_Container的实例对象
     *
     * @return
     */
    public static Cfg_Leader_Preach_value_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
