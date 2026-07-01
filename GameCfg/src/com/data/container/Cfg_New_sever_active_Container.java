/**
 * Auto generated, do not edit it
 *
 * new_sever_active配置表
 */
package com.data.container;


import com.data.ConfigBase;
import java.util.Map;
import com.data.bean.Cfg_New_sever_active_Bean; 
import config.Cfg_New_sever_active_Load;

	
public final class Cfg_New_sever_active_Container extends ConfigBase<Cfg_New_sever_active_Bean> {
	
    /**
     * 初始化new_sever_active配置表容器数据
     */
    @SuppressWarnings({"deprecation"})
    private Cfg_New_sever_active_Container(){
        Cfg_New_sever_active_Load load = new Cfg_New_sever_active_Load();
        Map<Integer, Cfg_New_sever_active_Bean> map = newMapValuees();
        load.load(map);
        initValue(map);
    }

    @Override
    protected Cfg_New_sever_active_Bean[] newBeanArray(int size) {
        return new Cfg_New_sever_active_Bean[size];
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        
        INSTANCE;                               //一个枚举的元素，它就代表了Singleton的一个实例
        Cfg_New_sever_active_Container manager;
        
        Singleton() {
            this.manager = new Cfg_New_sever_active_Container();
        }
        
        Cfg_New_sever_active_Container getProcessor() {
            return manager;
        }
    }
    
    /**
     * 获取Cfg_New_sever_active_Container的实例对象
     *
     * @return
     */
    public static Cfg_New_sever_active_Container GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
