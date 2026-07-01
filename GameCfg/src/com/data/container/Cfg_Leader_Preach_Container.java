/**
 * Auto generated, do not edit it
 *
 * Leader_Preach配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_Leader_Preach_Bean; 
import config.Cfg_Leader_Preach_Load;

	
public final class Cfg_Leader_Preach_Container extends ConfigBase<Cfg_Leader_Preach_Bean> {
	
    /**
     * 初始化Leader_Preach配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_Leader_Preach_Container(){
        Cfg_Leader_Preach_Load load = new Cfg_Leader_Preach_Load();
        Map<Integer, Cfg_Leader_Preach_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_Leader_Preach_Bean[] newBeanArray(int size) {
        return new Cfg_Leader_Preach_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_Leader_Preach_Container manager;
        
        Singleton() {
            this.manager = new Cfg_Leader_Preach_Container();
        }
        
        Cfg_Leader_Preach_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_Leader_Preach_Container的实例对象
     *
     * @return
     */
    public static Cfg_Leader_Preach_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
